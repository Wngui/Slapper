package dk.wngui.slapper.Events;

import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class ItemEvents implements Listener {

    @EventHandler
    public void OnBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        if (block.getType() == Material.TNT) {
            var tnt = event.getBlock();
            var fuse = tnt.getLocation().clone().add(0, 1, 0).getBlock();
            fuse.setType(Material.REDSTONE_TORCH);

            new BukkitRunnable() {
                @Override
                public void run() {
                    //Ignite the bomb
                    fuse.setType(Material.VOID_AIR);
                    tnt.setType(Material.VOID_AIR);
                    var primedTNT = tnt.getWorld().spawn(tnt.getLocation(), TNTPrimed.class);
                    primedTNT.setFuseTicks(10);
                    primedTNT.setGlowing(true);
                    primedTNT.setSource(event.getPlayer());
                }
            }.runTaskLater(Main.plugin, 30);
        }

        if (block.getType() == Material.IRON_BLOCK) {
            block.setType(Material.AIR);
            var golem = (IronGolem) block.getWorld().spawnEntity(block.getLocation(), EntityType.IRON_GOLEM);
            golem.setMetadata("GolemOwner", new FixedMetadataValue(Main.plugin, event.getPlayer().getUniqueId().toString()));
        }
    }

    @EventHandler
    public void OnGolemDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            if (event.getEntity() instanceof Player) {
                //If player is hit
                //Find Iron Golems in the area which belong to the player
                event.getEntity().getNearbyEntities(10, 10, 10).forEach(nearbyEntity -> {
                    if (nearbyEntity instanceof IronGolem) {
                        if (nearbyEntity.hasMetadata("GolemOwner")) {
                            var hitPlayer = (Player) event.getEntity();
                            var ownerId = nearbyEntity.getMetadata("GolemOwner").get(0).asString();
                            if (hitPlayer.getUniqueId().toString().equals(ownerId)) {
                                //Make the player golem attack the attacker!
                                ((IronGolem) nearbyEntity).setTarget((LivingEntity) event.getDamager());
                            }
                        }
                    }
                });
            } else if (event.getEntity() instanceof IronGolem && event.getDamager() instanceof Player) {
                //If golem hit by a player
                var golem = (IronGolem) event.getEntity();
                var attackingPlayer = (Player) event.getDamager();
                if (golem.hasMetadata("GolemOwner")) {
                    var ownerId = golem.getMetadata("GolemOwner").get(0).asString();
                    if (attackingPlayer.getUniqueId().toString().equals(ownerId)) {
                        //Friendly fire, cancel event
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRocket(PlayerInteractEvent event) {
        var item = event.getItem();
        if (item == null) return;
        Material inHand = item.getType();
        Action action = event.getAction();
        Player player = event.getPlayer();
        if (inHand == Material.FIREWORK_ROCKET) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                player.setVelocity(new Vector(0, 10, 0));
                event.setCancelled(true);
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().remove(event.getItem());
                }
            }
        }
    }

    @EventHandler
    public void onFireProjectile(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() == EntityType.TRIDENT) {
            var shooter = event.getEntity().getShooter();
            if (shooter instanceof Player) {
                var shooterInventory = ((Player) shooter).getInventory();
                if (shooterInventory.getItemInMainHand().getType() == Material.TRIDENT) {
                    shooterInventory.setItemInMainHand(Loot.Trident(1));
                }
            }
        }
    }

    @EventHandler
    public void onStickyBombHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            if (e.getHitEntity() instanceof LivingEntity) {
                var hitEntity = e.getHitEntity();
                hitEntity.setGlowing(true);
                getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
                    hitEntity.getWorld().createExplosion(hitEntity.getLocation(), 3);
                    hitEntity.setGlowing(false);
                }, 20L);
            }
        }
    }

    @EventHandler
    public void onCreeperEggHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg) {
            Location hitLocation;
            if (e.getHitBlock() != null) {
                hitLocation = e.getHitBlock().getLocation();
            } else {
                hitLocation = e.getHitEntity().getLocation();
            }

            e.getEntity().getWorld().spawnEntity(hitLocation.add(0, 1, 0), EntityType.CREEPER);
        }
    }

    @EventHandler
    public void HorseSaddle(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            if (e.getItem().getType() != Material.SADDLE) return;
            var horseSpawn = Objects.requireNonNull(e.getClickedBlock()).getLocation().clone().add(0, 1, 0);
            Horse horse = (Horse) e.getPlayer().getWorld().spawnEntity(horseSpawn, EntityType.HORSE);
            horse.playEffect(EntityEffect.LOVE_HEARTS);
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            horse.setHealth(4);
            e.setCancelled(true);
            var item = e.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                e.getPlayer().getInventory().remove(item);
            }
        }
    }

    @EventHandler
    public void WitherSkull(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            if (e.getItem().getType() != Material.WITHER_SKELETON_SKULL) return;
            var witherSpawn = Objects.requireNonNull(e.getClickedBlock()).getLocation().clone().add(0, 1, 0);
            Wither wither = (Wither) e.getPlayer().getWorld().spawnEntity(witherSpawn, EntityType.WITHER);
            wither.playEffect(EntityEffect.TELEPORT_ENDER);
            wither.setCustomName(ChatColor.MAGIC + "AEFIR");
            getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
                wither.playEffect(EntityEffect.TELEPORT_ENDER);
                wither.getWorld().createExplosion(wither.getLocation(), 2);
                wither.setHealth(0);
            }, 400);
            e.setCancelled(true);
            var item = e.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                e.getPlayer().getInventory().remove(item);
            }
        }
    }

    @EventHandler
    public void BookOfTheDead(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            if (e.getItem().getType() != Material.BOOK) return;

            var numberOfSkeletonsToSpawn = 2;

            var closestEnemyPlayer = e.getPlayer().getWorld().getNearbyEntities(e.getPlayer().getLocation(), 16, 16, 16).stream()
                    .filter(entity -> entity instanceof Player && entity != e.getPlayer()).findFirst();

            closestEnemyPlayer.ifPresent(entity -> {
                for (int i = 0; i < numberOfSkeletonsToSpawn; i++) {
                    Skeleton skeleton = (Skeleton) e.getPlayer().getWorld().spawnEntity(entity.getLocation().clone().add(i, 0, 0), EntityType.SKELETON);
                    skeleton.playEffect(EntityEffect.WITCH_MAGIC);
                    skeleton.setTarget((LivingEntity) entity);
                }
            });

            e.setCancelled(true);
            var item = e.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                e.getPlayer().getInventory().remove(item);
            }
        }
    }

    @EventHandler
    public void BeeEgg(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            if (e.getItem().getType() != Material.BEE_SPAWN_EGG) return;
            var beeSpawn = Objects.requireNonNull(e.getClickedBlock()).getLocation().clone().add(0, 1, 0);
            Bee bee = (Bee) e.getPlayer().getWorld().spawnEntity(beeSpawn, EntityType.BEE);
            bee.setAnger(999);
            var target = bee.getWorld().getNearbyEntities(bee.getLocation(), 16, 16, 16).stream().filter(entity -> entity != e.getPlayer()).findFirst();
            target.ifPresent(entity -> bee.setTarget((LivingEntity) entity));
            e.setCancelled(true);
            var item = e.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                e.getPlayer().getInventory().remove(item);
            }
        }
    }

    @EventHandler
    public void OnBeeAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Bee) {
            //BOOM
            var bee = event.getDamager();
            bee.setGlowing(true);
            getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> bee.getWorld().createExplosion(bee.getLocation(), 3), 20L);
        }
    }
}
