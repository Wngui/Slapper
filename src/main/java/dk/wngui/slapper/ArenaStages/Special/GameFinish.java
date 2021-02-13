package dk.wngui.slapper.ArenaStages.Special;

import dk.wngui.slapper.Arena.ArenaScoreboardManager;
import dk.wngui.slapper.Utility.Common;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

public class GameFinish implements IArenaStage {

    private final ArenaScoreboardManager arenaScoreboardManager;

    public GameFinish(ArenaScoreboardManager arenaScoreboardManager) {
        this.arenaScoreboardManager = arenaScoreboardManager;
    }

    public StageElements execute(ArenaElements arenaElements) {

        var podiumMaterials = new ArrayList<Material>();
        podiumMaterials.add(Material.DIAMOND_BLOCK);
        podiumMaterials.add(Material.GOLD_BLOCK);
        podiumMaterials.add(Material.IRON_BLOCK);

        //Create base
        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius, Material.SOUL_SAND, true);

        //Create inner working circle for podiums
        var podiumCenters = WorldEditor.CreateCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, podiumMaterials.size());

        var podiumMaxHeight = 6;

        //Sort players by score descending
        var playersByScores = arenaScoreboardManager.GetScores().entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).collect(Collectors.toList());

        //Create podiums
        for (int i = 0; i < podiumCenters.size() && i < playersByScores.size(); i++) {
            var location = podiumCenters.get(i);
            var podiumLocation = location.clone().add(0, podiumMaxHeight - i, 0);
            WorldEditor.DrawBox(location, podiumLocation, podiumMaterials.get(i));
            WorldEditor.DrawCircle(podiumLocation, 2, podiumMaterials.get(i), true);
            SpawnPlayer(playersByScores.get(i), podiumLocation.clone().add(0, 1, 0), arenaElements.PlatformCenter);
        }

        //Spawn losers
        var defaultSpawns = WorldEditor.CreateCircle(arenaElements.PlatformCenter, arenaElements.Radius - 3, playersByScores.size());

        for (int i = podiumCenters.size(); i < playersByScores.size(); i++) {
            SpawnPlayer(playersByScores.get(i), defaultSpawns.get(i).clone().add(0, 1, 0), arenaElements.PlatformCenter);
        }

        var stageElements = new StageElements();
        stageElements.CustomChest = true;
        stageElements.CustomPlayerSpawns = true;
        stageElements.ShouldShrink = false;
        return stageElements;
    }

    private void SpawnPlayer(Player player, Location location, Location center) {
        try {
            player.teleport(Common.lookAt(location, center));
            player.setFoodLevel(20);
            player.setHealth(20);
        } catch (Exception e) {
            System.out.println("Attempted to move player " + player + " to winner stage, but ran out of room!");
        }
    }
}
