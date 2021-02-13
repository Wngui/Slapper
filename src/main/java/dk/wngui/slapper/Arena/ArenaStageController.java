package dk.wngui.slapper.Arena;

import dk.wngui.slapper.ArenaStages.Special.Store;
import dk.wngui.slapper.Events.StoreEvents;
import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.Common;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class ArenaStageController {

    private final ArenaStages arenaStages;
    private final Random random;
    private final ArenaController arenaController;
    private StageElements stageElements;
    private int shrinkScheduleId;
    private BukkitTask shopEndEvent;

    public ArenaStageController(ArenaController arenaController) {
        this.arenaController = arenaController;
        this.arenaStages = new ArenaStages();
        this.random = new Random();
    }

    public void StartStage(ArenaElements arenaElements, StageType type) {
        StopEvents();

        if (type == StageType.Shop) {
            stageElements = BuildShopStage(arenaElements);
        } else if (type == StageType.Battle) {
            stageElements = BuildRandomBattleStage(arenaElements);
        }

        StartStageWithElements(stageElements, arenaElements);
    }

    public void StartStageWithElements(StageElements stageElements, ArenaElements arenaElements) {
        if (stageElements.CustomChest) {
            if (stageElements.ChestSpawnLocation != null)
                CreateArenaChest(stageElements.ChestSpawnLocation);
        } else {
            CreateArenaChest(arenaElements.PlatformCenter);
        }
        if (!stageElements.CustomPlayerEffects) PlayerBattleStageEffects(arenaElements.Players);
        if (!stageElements.CustomEnvironment)
            SetupEnvironment(arenaElements.PlatformCenter.getWorld());
        if (stageElements.ShouldShrink) {
            this.shrinkScheduleId = new ArenaShrinker(arenaElements, arenaElements.Radius + 5).BeginShrinking();
        }
        if (!stageElements.CustomPlayerSpawns) {
            SpawnPlayers(arenaElements);
        }
    }

    private StageElements BuildRandomBattleStage(ArenaElements arenaElements) {
        Common.SendChatMessages(arenaElements.Players, ChatColor.GOLD + "Prepare to slap...");
        var stage = arenaStages.stageList.get(random.nextInt(arenaStages.stageList.size()));
        //Remove played stage from map pool.
        arenaStages.stageList.remove(stage);
        return stage.execute(arenaElements);
    }

    private StageElements BuildShopStage(ArenaElements arenaElements) {
        Common.SendChatMessages(arenaElements.Players, ChatColor.GOLD + "Time to buy supplies");
        var storeEvents = new StoreEvents(arenaController.playerInventoryManager);
        Bukkit.getPluginManager().registerEvents(storeEvents, Main.plugin);

        var store = new Store();
        shopEndEvent = new BukkitRunnable() {
            @Override
            public void run() {
                HandlerList.unregisterAll(storeEvents);
                arenaController.EndShopStage();
            }
        }.runTaskLater(Main.plugin, 750);
        return store.execute(arenaElements);
    }

    private void PlayerBattleStageEffects(ArrayList<Player> players) {
        players.forEach(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, 2));
        });
    }

    private void SetupEnvironment(World world) {
        world.setTime(18000);
        world.setStorm(false);
    }

    private void SpawnPlayers(ArenaElements arenaElements) {
        var i = 0;
        for (var player : arenaElements.Players) {
            try {
                player.teleport(Common.lookAt(arenaElements.SpawnPoints.get(i).clone().add(0.5, 1, 0.5), arenaElements.PlatformCenter));
                player.setFoodLevel(20);
                player.setHealth(20);
                i++;
            } catch (Exception e) {
                System.out.println("Attempted to move player " + player + " to arena, but ran out of room!");
            }
        }
        arenaElements.SpawnPoints.forEach(spawnBlock -> spawnBlock.getBlock().setType(Material.GOLD_BLOCK));
    }

    private void CreateArenaChest(Location location) {
        location.getBlock().setType(Material.CHEST);
        try {
            var chest = (Chest) location.getBlock().getState();
            chest.getBlockInventory().addItem(Loot.SuperTNT(5));
        } catch (Exception e) {
            System.out.println("Couldn't spawn chest..");
        }
    }

    public void StopEvents() {
        if (shopEndEvent != null) {
            shopEndEvent.cancel();
        }
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.cancelTask(shrinkScheduleId);
        if (stageElements != null) {
            scheduler.cancelTask(stageElements.ScheduleId);
            if (stageElements.StageEvents != null) HandlerList.unregisterAll(stageElements.StageEvents);
        }
    }
}
