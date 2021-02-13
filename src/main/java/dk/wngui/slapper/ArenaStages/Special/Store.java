package dk.wngui.slapper.ArenaStages.Special;

import dk.wngui.slapper.Utility.Common;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Store implements IArenaStage {
    final int storeHeight = 6;
    final int storeRadius = 14;
    private WanderingTrader ShopKeeper;

    public StageElements execute(ArenaElements arenaElements) {
        GenerateStore(arenaElements);
        SpawnPlayers(arenaElements);

        var stageElements = new StageElements();
        stageElements.CustomChest = true;
        stageElements.CustomPlayerSpawns = true;
        stageElements.CustomEnvironment = true;
        stageElements.CustomPlayerEffects = true;
        stageElements.ShouldShrink = false;
        return stageElements;
    }

    private void SpawnPlayers(ArenaElements arenaElements) {
        var spawnPoints = WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, 1, 0), storeRadius / 4, arenaElements.Players.size());
        var i = 0;
        for (var player : arenaElements.Players) {
            try {
                player.teleport(Common.lookAt(spawnPoints.get(i).clone().add(0.5, 1, 0.5), ShopKeeper.getLocation()));
                player.setFoodLevel(20);
                player.setHealth(20);
                i++;
            } catch (Exception e) {
                System.out.println("Attempted to move player " + player + " to shop, but ran out of room!");
            }
        }
    }

    private void GenerateStore(ArenaElements arenaElements) {

        var fountainHeight = storeHeight / 2;

        var tile_floor = Material.LODESTONE;
        var tile_floor_center = Material.GOLD_BLOCK;
        var tile_wall = Material.MOSSY_STONE_BRICKS;
        var tile_top = Material.MOSSY_STONE_BRICKS;
        var liquid_fountain = Material.WATER;
        var tile_fountain_floor = Material.REDSTONE_LAMP;
        var lighting_floor = Material.REDSTONE_LAMP;

        var workingCenter = arenaElements.PlatformCenter.clone();

        var random = new Random();

        var world = arenaElements.PlatformCenter.getWorld();
        world.setTime(0);

        //Floor
        WorldEditor.DrawCircle(workingCenter, storeRadius, tile_floor, true);
        WorldEditor.DrawCircle(workingCenter, storeRadius / 4, tile_floor_center, true);
        workingCenter.clone().getBlock().setType(Material.YELLOW_STAINED_GLASS);
        workingCenter.clone().subtract(0, 1, 0).getBlock().setType(lighting_floor);
        workingCenter.clone().subtract(0, 2, 0).getBlock().setType(Material.REDSTONE_BLOCK);

        //Fountains
        WorldEditor.CreateCircle(arenaElements.PlatformCenter, storeRadius - 1, 4).forEach(location -> {
            WorldEditor.DrawCircle(location.clone().add(0, 1, 0), (storeRadius / 4) + 1, tile_floor, false);
            WorldEditor.DrawCircle(location.clone().add(0, 1, 0), storeRadius / 4, liquid_fountain, true);
            WorldEditor.DrawCircle(location.clone(), storeRadius / 4, tile_fountain_floor, true);
            WorldEditor.DrawCircle(location.clone().subtract(0, 1, 0), storeRadius / 4, Material.REDSTONE_BLOCK, true);
        });

        //Fountain Sprout
        for (int i = 1; i <= fountainHeight; i++) {
            WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), storeRadius - 1, 4).forEach(location -> location.getBlock().setType(liquid_fountain));
        }

        //Drain liquid from shop stall
        for (int i = 0; i <= fountainHeight; i++) {
            WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), storeRadius - 1, 1).forEach(location -> WorldEditor.DrawCircle(location.add(0, 1, 0), storeRadius / 4, Material.AIR, true));
        }

        //Mid
        for (int i = 0; i < storeHeight; i++) {
            //Wall
            WorldEditor.DrawCircle(workingCenter, storeRadius, tile_wall, false);
            workingCenter.add(0, 1, 0);
        }

        //Top
        for (int i = storeRadius - 1; i > 0; i--) {
            WorldEditor.DrawCircle(workingCenter, i, tile_top, false);
            workingCenter.add(0, 1, 0);
        }
        workingCenter.clone().subtract(0, 1, 0).getBlock().setType(Material.EMERALD_BLOCK);

        //Parrots
        WorldEditor.CreateCircle(workingCenter.clone().subtract(0, 3, 0), 2, 4).forEach(location -> world.spawnEntity(location, EntityType.PARROT));

        //Fountain origin
        WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, fountainHeight, 0), storeRadius, 4).forEach(location -> {
            location.getBlock().setType(liquid_fountain);
            Common.GetAdjacentBlocks(location, tile_wall).forEach(block -> block.getBlock().setType(Material.CHISELED_STONE_BRICKS));
        });
        WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, fountainHeight, 0), storeRadius + 1, 4).forEach(location -> location.getBlock().setType(tile_top));

        //Item shop wall decoration
        WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, fountainHeight, 0), storeRadius, 1).forEach(location -> {
            location.getBlock().setType(Material.CHISELED_STONE_BRICKS);
            Common.GetAdjacentBlocks(location, tile_wall).forEach(block -> block.getBlock().setType(Material.CHISELED_STONE_BRICKS));
        });
        workingCenter.subtract(0, fountainHeight + 1, 0);

        //Shop Keeper
        var shopKeeperLocation = WorldEditor.CreateCircle(arenaElements.PlatformCenter, storeRadius - 3, 1).stream().findFirst();
        shopKeeperLocation.ifPresent(location -> {
            ShopKeeper = (WanderingTrader) world.spawnEntity(Common.NormalizeLocation(location).add(0.5, 1, 0.5), EntityType.WANDERING_TRADER);
            ShopKeeper.teleport(Common.lookAt(ShopKeeper.getLocation(), arenaElements.PlatformCenter));
            ShopKeeper.setCustomNameVisible(true);
            ShopKeeper.setCustomName("Arena Merchant");
            ShopKeeper.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 10));
        });


        WorldEditor.UpdateWorldStateBySphere(arenaElements.PlatformCenter, 20);
    }
}
