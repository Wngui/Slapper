package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class UnderTheSea implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        //Spawn ocean animals
        var seaAnimals = new ArrayList<EntityType>();
        seaAnimals.add(EntityType.PUFFERFISH);
        seaAnimals.add(EntityType.TROPICAL_FISH);
        seaAnimals.add(EntityType.DOLPHIN);
        seaAnimals.add(EntityType.SALMON);
        seaAnimals.add(EntityType.COD);
        seaAnimals.add(EntityType.TURTLE);

        var random = new Random();

        var firstSpawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, arenaElements.Radius - 3, 15);
        firstSpawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0, 1, 0), seaAnimals.get(random.nextInt(seaAnimals.size()))));

        var secondSpawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, arenaElements.Radius - 5, 10);
        secondSpawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0, 1, 0), seaAnimals.get(random.nextInt(seaAnimals.size()))));

        //Water!
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            var waterHeight = 6;

            //Create water
            for (int i = 0; i < waterHeight; i++) {
                WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, i + 1, 0), arenaElements.Radius - 1, Material.WATER, true);
            }

            //Create island
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, waterHeight, 0), 3, Material.GRASS_BLOCK, true);
            arenaElements.PlatformCenter.clone().add(0, waterHeight, 0).getBlock().setType(Material.GRASS_BLOCK);

            var palmTreeLocation = arenaElements.PlatformCenter.clone().add(0, waterHeight + 1, 0);
            palmTreeLocation.getWorld().generateTree(palmTreeLocation, TreeType.JUNGLE);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 9999, 10));
                player.getInventory().addItem(Loot.Trident(1));
            });

        }, 100L);
        return new StageElements(scheduleId);
    }
}
