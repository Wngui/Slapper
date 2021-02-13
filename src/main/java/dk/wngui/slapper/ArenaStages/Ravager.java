package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class Ravager implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.BEDROCK, true);
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> arenaElements.PlatformCenter.getWorld().spawnEntity(arenaElements.PlatformCenter.add(0, 1, 0), EntityType.RAVAGER), 250L);
        return new StageElements(scheduleId);
    }
}
