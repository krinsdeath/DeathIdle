package net.krinsoft.deathidle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author krinsdeath
 */
public class DeathIdle extends JavaPlugin {
    // logging and debug stuff
    private final static Logger LOGGER = Logger.getLogger("DeathIdle");
    private boolean debug = true;

    // instance stuff
    private PlayerListener playerListener;
    private EntityListener entityListener;
    private PluginManager pm;
    private Configuration config;

    // player list
    private List<String> players = new ArrayList<String>();

    @Override
    public void onEnable() {
        this.initListeners();
        this.buildConfiguration();
        this.info(this.getDescription().getFullName() + " by " + this.getDescription().getAuthors().toString().replaceAll("[\\]\\[]", "") + " enabled.");
    }

    @Override
    public void onDisable() {
        this.pm = null;
        this.config = null;
        this.playerListener = null;
        this.entityListener = null;
        this.info(this.getDescription().getFullName() + " disabled.");
    }

    private void initListeners() {
        this.pm = this.getServer().getPluginManager();
        this.playerListener = new PlayerListener(this);
        this.entityListener = new EntityListener(this);
        pm.registerEvent(Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);
        pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
    }

    private void buildConfiguration() {
        this.config = getConfiguration();
        boolean build = this.config.getBoolean("plugin.built", false);
        if (!build) {
            this.config.setHeader(
                    "# set plugin.built to false to rebuild the configuration for any reason.",
                    "# idle_time is the time in seconds before the player can be attacked (or issue an attack) after respawning.",
                    "# respawn_message is the message sent to players when they respawn. Fairly self-explanatory."
                    );
            this.config.setProperty("plugin.built", true);
            this.config.setProperty("plugin.version", this.getDescription().getVersion());
            this.config.setProperty("idle_time", 60);
            this.config.setProperty("idle_done", "You can now participate in PVP.");
            this.config.setProperty("idle_message", "You can engage in PVP combat again in %s seconds.");
            this.config.save();
        }
    }

    protected void info(String message) {
        message = "[" + this.getDescription().getName() + "] " + message;
        LOGGER.info(message);
    }

    protected void debug(String message) {
        if (this.debug) {
            message = "[" + this.getDescription().getName() + "] [Debug] " + message;
            LOGGER.info(message);
        }
    }

    public void togglePlayerIdle(String player) {
        if (this.players.contains(player)) {
            this.players.remove(player);
            if (this.getServer().getPlayer(player) != null) {
                String msg = this.config.getString("idle_done").replaceAll("(?i)&([0-F])", "\u00A7$1");
                this.getServer().getPlayer(player).sendMessage(msg);
            }
        } else {
            this.players.add(player);
        }
    }

    public boolean isPlayerIdle(String player) {
        return this.players.contains(player);
    }

    public void showRespawnMessage(String player) {
        if (this.getServer().getPlayer(player) != null) {
            String msg = String.format(this.config.getString("idle_message"), this.config.getInt("idle_time", 60)).replaceAll("(?i)&([0-F])", "\u00A7$1");
            this.getServer().getPlayer(player).sendMessage(msg);
        }
    }

}
