package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class CreeperGeddon implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.GREEN_WOOL, true);
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            var spawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, 2, 4);
            spawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0, 1, 0), EntityType.CREEPER));
        }, 250L);

        for (int i = 0; i < 5; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, i, 0), arenaElements.Radius - 1, Material.GREEN_WOOL, false);
        }

        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, 6, 0), arenaElements.Radius - 1, Material.GREEN_WOOL, true);

        return new StageElements(scheduleId);
    }
}
