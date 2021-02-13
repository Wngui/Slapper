package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class Shulker implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.END_STONE_BRICKS, true);
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->{
            var spawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, 2, 4);
            spawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0,1,0), EntityType.SHULKER));
        }, 250L);

        WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0,4,0), arenaElements.Radius-3, 8).forEach(location -> location.getBlock().setType(Material.PURPLE_CONCRETE));

        WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0,8,0), arenaElements.Radius-5, 4).forEach(location -> location.getBlock().setType(Material.PURPLE_CONCRETE));

        return new StageElements(scheduleId);
    }
}
