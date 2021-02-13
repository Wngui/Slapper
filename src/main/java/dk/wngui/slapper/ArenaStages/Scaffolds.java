package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

public class Scaffolds implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var scaffoldHeight = 3;

        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, scaffoldHeight+1, 0), arenaElements.Radius, Material.SLIME_BLOCK, true);

        for (int i = -scaffoldHeight; i < 1; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), arenaElements.Radius, Material.SCAFFOLDING, true);
        }

        var stageElements = new StageElements();
        stageElements.CustomChest = true;

        return stageElements;
    }
}
