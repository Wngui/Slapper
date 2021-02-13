package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;

public class Tower implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var towerHeight = 7;
        var random = new Random();

        for (int i = 0; i < towerHeight; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, i, 0), arenaElements.Radius - 5, Material.STONE_BRICKS, false);
        }

        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, towerHeight, 0), arenaElements.Radius - 4, Material.STONE_BRICKS, true);
        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().add(0, towerHeight + 1, 0), arenaElements.Radius - 4, Material.CHISELED_STONE_BRICKS, false);

        arenaElements.PlatformCenter.clone().add(0, 1, arenaElements.Radius - 5).getBlock().setType(Material.AIR);
        arenaElements.PlatformCenter.clone().add(0, 2, arenaElements.Radius - 5).getBlock().setType(Material.AIR);

        arenaElements.PlatformCenter.clone().getBlock().setType(Material.SOUL_SAND);

        var water = WorldEditor.CreateBox(arenaElements.PlatformCenter.clone().add(0, 1, 0), arenaElements.PlatformCenter.clone().add(0, towerHeight + 1, 0));
        water.forEach(location -> location.getBlock().setType(Material.WATER));

        //Randomize brick type
        WorldEditor.CreateSphere(arenaElements.PlatformCenter, towerHeight + 1, false)
                .stream().map(Location::getBlock).filter(block -> block.getType() == Material.STONE_BRICKS)
                .forEach(block -> {
                    if (random.nextInt(10) > 7) {
                        block.setType(Material.MOSSY_STONE_BRICKS);
                    } else if (random.nextInt(10) > 7) {
                        block.setType(Material.CRACKED_STONE_BRICKS);
                    }
                });

        var stageElements = new StageElements();
        stageElements.CustomChest = true;
        stageElements.ChestSpawnLocation = arenaElements.PlatformCenter.clone().add(0,towerHeight+1,1);

        return stageElements;
    }
}
