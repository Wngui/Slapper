package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class IceRing implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.ICE, true);

        //Snowman
        WorldEditor.CreateCircle(arenaElements.PlatformCenter, 2, 1).forEach(location -> arenaElements.PlatformCenter.getWorld().spawnEntity(location.clone().add(0,1,0), EntityType.SNOWMAN));

        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            //Polar bear :O
            WorldEditor.CreateCircle(arenaElements.PlatformCenter, 3, 1).forEach(location -> arenaElements.PlatformCenter.getWorld().spawnEntity(location.clone().add(0,1,0), EntityType.POLAR_BEAR));

        }, 100L);
        return new StageElements(scheduleId);
    }
}
