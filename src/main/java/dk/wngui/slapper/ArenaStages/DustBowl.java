package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

import static org.bukkit.Bukkit.getServer;

public class DustBowl implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {

            var hiveHeight = 3;

            var workingCenter = arenaElements.PlatformCenter.clone();
            workingCenter.subtract(0, arenaElements.Radius + hiveHeight, 0);

            for (int i = 0; i < arenaElements.Radius; i++) {
                WorldEditor.DrawCircle(workingCenter, i - 1, Material.SANDSTONE, false);
                WorldEditor.DrawCircle(workingCenter, i, Material.SANDSTONE, false);
                workingCenter.add(0, 1, 0);
            }

            for (int i = 0; i < hiveHeight; i++) {
                WorldEditor.DrawCircle(workingCenter, i - 1, Material.SANDSTONE, false);
                WorldEditor.DrawCircle(workingCenter, i, Material.SANDSTONE, false);
                workingCenter.add(0, 1, 0);
            }

            for (int i = arenaElements.Radius; i > 0; i--) {
                WorldEditor.DrawCircle(workingCenter, i - 1, Material.SANDSTONE, false);
                WorldEditor.DrawCircle(workingCenter, i, Material.SANDSTONE, false);
                workingCenter.add(0, 1, 0);
            }


        }, 0L);
        return new StageElements(scheduleId);
    }
}
