package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class Phantoms implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.BLUE_STAINED_GLASS, true);
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            var spawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, 2, 2);
            spawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0, 1, 0), EntityType.PHANTOM));
        }, 250L);

        for (int i = 0; i < 10; i++) {
            WorldEditor.CreateCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), arenaElements.Radius - 5, 4).forEach(location -> location.getBlock().setType(Material.LODESTONE));
        }

        return new StageElements(scheduleId);
    }
}
