package net.krinsoft.deathidle;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author krinsdeath
 */
class EntityListener extends org.bukkit.event.entity.EntityListener {
    private DeathIdle plugin;

    public EntityListener(DeathIdle aThis) {
        this.plugin = aThis;
    }

    @Override
    public void onEntityDamage(EntityDamageEvent evt) {
        // throw away cancelled events
        if (evt.isCancelled()) { return; }
        // check for EntityDamageByEntity
        if (!(evt instanceof EntityDamageByEntityEvent)) {
            return;
        }
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) evt;
        // make sure both entities are players
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player source = (Player) event.getDamager();
            // make sure neither player is idle
            if (this.plugin.isPlayerIdle(target.getName()) || this.plugin.isPlayerIdle(source.getName())) {
                evt.setCancelled(true);
                return;
            }
        }
    }

}
