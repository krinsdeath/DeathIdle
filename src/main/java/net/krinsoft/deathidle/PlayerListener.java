package net.krinsoft.deathidle;

import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author krinsdeath
 */
class PlayerListener extends org.bukkit.event.player.PlayerListener {
    private DeathIdle plugin;

    public PlayerListener(DeathIdle aThis) {
        this.plugin = aThis;
    }

    @Override
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        // make sure the player isn't already idle
        if (!this.plugin.isPlayerIdle(event.getPlayer().getName())) {
            this.plugin.togglePlayerIdle(event.getPlayer().getName());
            this.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    plugin.togglePlayerIdle(event.getPlayer().getName());
                }

            }, plugin.getConfiguration().getInt("idle_time", 60) * 20);
            // show the message to the player
            this.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    plugin.showRespawnMessage(event.getPlayer().getName());
                }

            }, 2L);
        }
    }

}
