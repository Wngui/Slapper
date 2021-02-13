package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

import static org.bukkit.Bukkit.getServer;

public class HotLava implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.CRIMSON_NYLIUM, true);
        //Lava!
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            var lavaHeight = 6;

            //Create lava
            for (int i = 0; i < lavaHeight; i++) {
                WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, i + 1, 0), arenaElements.Radius + 1, Material.LAVA, false);
            }

        }, 0);
        return new StageElements(scheduleId);
    }
}
