package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.text.MessageFormat;

import static org.bukkit.Bukkit.getServer;

public class HorseMen implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.MOSSY_STONE_BRICKS, true);
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            var x = arenaElements.PlatformCenter.getX();
            var y = arenaElements.PlatformCenter.getY()+2;
            var z = arenaElements.PlatformCenter.getZ();
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    MessageFormat.format("summon skeleton_horse {0} {1} {2} ", x, y, z) + "{SkeletonTrap:1}");
        }, 250L);
        return new StageElements(scheduleId);
    }
}
