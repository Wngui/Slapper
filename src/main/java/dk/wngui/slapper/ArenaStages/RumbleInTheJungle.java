package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.EntityType;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class RumbleInTheJungle implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            //Create dirt
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone(), arenaElements.Radius, Material.GRASS_BLOCK, true);

            var random = new Random();
            var world = arenaElements.PlatformCenter.getWorld();

            //Create jungle + parrots
            for (int i = arenaElements.Radius-3; i > 0; i--) {
                WorldEditor.CreateCircle(arenaElements.PlatformCenter, i, random.nextInt(i * 2)).forEach(location -> {
                    world.generateTree(location, TreeType.JUNGLE);
                    world.spawnEntity(location.add(random.nextInt(5), 10, random.nextInt(5)), EntityType.PARROT);
                });
            }
            //Panda :3
            world.spawnEntity(arenaElements.PlatformCenter.add(0, 1, 0), EntityType.PANDA);
        }, 0L);
        return new StageElements(scheduleId);
    }
}
