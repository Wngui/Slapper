package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getServer;

public class Evoker implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->{
            var spawnCircle = WorldEditor.CreateCircle(arenaElements.PlatformCenter, 2, 2);
            spawnCircle.forEach(location -> location.getWorld().spawnEntity(location.add(0,1,0), EntityType.EVOKER));
        }, 250L);
        return new StageElements(scheduleId);
    }
}
