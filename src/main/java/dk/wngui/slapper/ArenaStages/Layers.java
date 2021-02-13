package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

import java.util.ArrayList;

public class Layers implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var layerMaterials = new ArrayList<Material>();
        layerMaterials.add(Material.PURPLE_STAINED_GLASS);
        layerMaterials.add(Material.BLUE_STAINED_GLASS);
        layerMaterials.add(Material.CYAN_STAINED_GLASS);
        layerMaterials.add(Material.GREEN_STAINED_GLASS);
        layerMaterials.add(Material.YELLOW_STAINED_GLASS);
        layerMaterials.add(Material.ORANGE_STAINED_GLASS);
        layerMaterials.add(Material.RED_STAINED_GLASS);

        for (int i = 0; i < layerMaterials.size(); i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, i, 0), arenaElements.Radius - 1, layerMaterials.get(i), true);
        }


        return new StageElements(0);
    }
}
