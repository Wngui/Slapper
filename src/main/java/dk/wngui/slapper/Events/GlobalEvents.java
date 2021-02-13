package dk.wngui.slapper.Events;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.Common;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static org.bukkit.Bukkit.getServer;

public class GlobalEvents implements Listener {

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    @EventHandler
    public void OnPlayerBreakBlock(BlockBreakEvent event) {
        //Melon chunks should be red
        if (event.getBlock().getType() == Material.RED_MUSHROOM_BLOCK) {
            getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> Common.GetAdjacentBlocks(event.getBlock().getLocation(), Material.RED_MUSHROOM_BLOCK).forEach(location -> Common.AllowAllFaces(location.getBlock())), 1L);
        }
        //Bees should not freak out when a nest is broken
        if (event.getBlock().getType() == Material.BEE_NEST) {
            event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 10, 10, 10).forEach(entity -> {
                if (entity.getType() == EntityType.BEE) {
                    ((Bee) entity).setAnger(0);
                }
            });
        }

    }

    @EventHandler
    public void OnCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            creeper.setPowered(true);
        } else if (event.getEntity() instanceof Chicken) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (event.getTo().getY() < 60) {
                player.setHealth(0);
            }
        }
    }

    @EventHandler
    public void OnCreatureDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            var player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (player.getLocation().getY() > 60) event.setCancelled(true);
            }
            player.setHealth(20);
        } else if (event.getEntity() instanceof Bee) {
            //Bees explode when attacked
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) return;
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
            var bee = (Bee) event.getEntity();
            bee.setAnger(100);
            bee.setTarget(Common.RandomAlivePlayer());

            if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                return;
            }

            //BOOM
            bee.setGlowing(true);
            getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> bee.getWorld().createExplosion(bee.getLocation(), 3), 20L);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        //Stop liquids from flowing
        if (event.getBlock().getType() == Material.WATER) {
            event.setCancelled(true);
        } else if (event.getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
        } else if (event.getBlock().getType() == Material.BUBBLE_COLUMN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident) {
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent e) {
        //Melon chunks should be red after being blown up
        getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> e.blockList().forEach(block -> Common.GetAdjacentBlocks(block.getLocation(), Material.RED_MUSHROOM_BLOCK).forEach(location -> Common.AllowAllFaces(location.getBlock()))), 1L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() < 20) {
            event.getPlayer().setHealth(0);
        }
    }

}
