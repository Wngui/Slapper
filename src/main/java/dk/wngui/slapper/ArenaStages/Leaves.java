package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Random;

public class Leaves implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var leafList = new ArrayList<Material>();
        leafList.add(Material.ACACIA_LEAVES);
        leafList.add(Material.BIRCH_LEAVES);
        leafList.add(Material.OAK_LEAVES);
        leafList.add(Material.JUNGLE_LEAVES);
        leafList.add(Material.DARK_OAK_LEAVES);
        leafList.add(Material.SPRUCE_LEAVES);


        var leafHeight = 2;
        var random = new Random();

        for (int i = -leafHeight; i < 1; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), arenaElements.Radius, Material.ACACIA_LEAVES, true);
        }

        //Randomize brick type
        WorldEditor.CreateSphere(arenaElements.PlatformCenter, arenaElements.Radius, false)
                .stream().map(Location::getBlock).filter(block -> block.getType() == Material.ACACIA_LEAVES)
                .forEach(block -> {
                    var material = leafList.get(random.nextInt(leafList.size()));
                    block.setType(material);
                });

        var stageElements = new StageElements();
        stageElements.CustomChest = true;

        return stageElements;
    }
}
