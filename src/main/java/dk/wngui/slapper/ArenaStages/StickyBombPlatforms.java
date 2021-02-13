package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Material;

public class StickyBombPlatforms implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var platformRadius = 2;

        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone(), arenaElements.Radius, Material.AIR, true);

        arenaElements.SpawnPoints.forEach(location -> WorldEditor.DrawCircle(location.subtract(0, 1, 0), platformRadius, Material.GILDED_BLACKSTONE, true));

        arenaElements.Players.forEach(player -> {
            var bomb = Loot.StickyBomb();
            bomb.setAmount(64);
            player.getInventory().addItem(bomb);
        });

        var stageElements = new StageElements();
        stageElements.CustomChest = true;

        return stageElements;
    }
}
