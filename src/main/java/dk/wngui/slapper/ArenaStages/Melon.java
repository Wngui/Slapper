package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

import static org.bukkit.Bukkit.getServer;

public class Melon implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->{

            //Create melon
            WorldEditor.DrawSphere(arenaElements.PlatformCenter, arenaElements.Radius, Material.MELON, true);
            WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius, Material.MELON, false);

            //Create melon innards
            WorldEditor.DrawSphere(arenaElements.PlatformCenter, arenaElements.Radius-1, Material.RED_MUSHROOM_BLOCK, false);

        }, 0L);
        return new StageElements(scheduleId);
    }
}
