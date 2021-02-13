package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class BeeHive implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {

            var hiveHeight = 6;
            var hiveRadius = arenaElements.Radius - 1;

            var workingCenter = arenaElements.PlatformCenter.clone();
            workingCenter.subtract(0, arenaElements.Radius - Math.floor(hiveHeight / 2.5), 0);

            var random = new Random();
            var world = arenaElements.PlatformCenter.getWorld();
            world.setTime(0);

            //Bottom
            for (int i = 0; i < hiveRadius; i++) {
                WorldEditor.DrawCircle(workingCenter, i - 1, Material.HONEYCOMB_BLOCK, false);
                WorldEditor.DrawCircle(workingCenter, i, Material.BEE_NEST, false);
                workingCenter.add(0, 1, 0);
            }

            //Mid + BEES!
            for (int i = 0; i < hiveHeight; i++) {
                WorldEditor.DrawCircle(workingCenter, hiveRadius, Material.BEE_NEST, false);
                WorldEditor.DrawCircle(workingCenter, hiveRadius - 1, Material.HONEYCOMB_BLOCK, false);
                //Spawn bees
                WorldEditor.CreateCircle(workingCenter, hiveRadius - 2, random.nextInt(hiveRadius)).forEach(location -> location.getWorld().spawnEntity(location, EntityType.BEE));
                workingCenter.add(0, 1, 0);
            }

            //Top
            for (int i = hiveRadius - 1; i > 0; i--) {
                WorldEditor.DrawCircle(workingCenter, i - 1, Material.HONEYCOMB_BLOCK, false);
                WorldEditor.DrawCircle(workingCenter, i, Material.BEE_NEST, false);
                workingCenter.add(0, 1, 0);
            }

            //Lighting
            WorldEditor.DrawCircle(arenaElements.PlatformCenter, hiveRadius - 2, Material.GLOWSTONE, false);
            var topLight = arenaElements.PlatformCenter.clone().add(0, hiveHeight + hiveRadius - 1, 0);
            topLight.getBlock().setType(Material.GLOWSTONE);


        }, 0L);
        return new StageElements(scheduleId);
    }
}
