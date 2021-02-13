package dk.wngui.slapper.Arena;

import dk.wngui.slapper.Utility.Common;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaBuilder {

    private final int height;
    final ArrayList<Player> players;

    public ArenaBuilder(ArrayList<Player> players, int height) {
        this.players = players;
        this.height = height;
    }

    public ArenaElements Build(Location location) {
        var startPosition = Common.NormalizeLocation(location.clone());
        startPosition.setY(height);
        CleanupArenaArea(startPosition, GetSizeWithBuffer());
        return GenerateArenaElements(startPosition, GetSize(), players);
    }

    private ArenaElements GenerateArenaElements(Location location, int size, ArrayList<Player> players) {
        var platformMiddle = CreateArenaPlatform(location, size);
        var spawnPoints = CreatePlayerSpawnPoints(platformMiddle, size, players.size());

        return new ArenaElements(players, spawnPoints, platformMiddle, size);
    }

    public void CleanupArenaArea(Location location, int size) {
        WorldEditor.ClearAirSpace(location, size);
        location.getWorld().getNearbyEntities(location, size * 2, size * 2, size * 2).stream()
                .filter(entity -> entity instanceof Item || (entity instanceof LivingEntity && !(entity instanceof Player)))
                .forEach(Entity::remove);
    }

    private ArrayList<Location> CreatePlayerSpawnPoints(Location platformMiddle, int size, int playerCount) {
        return WorldEditor.CreateCircle(platformMiddle, size, playerCount);
    }

    private Location CreateArenaPlatform(Location location, int size) {
        var platformMiddle = location.clone();
        WorldEditor.DrawCircle(platformMiddle, size, Material.NETHERITE_BLOCK, true);
        WorldEditor.DrawCircle(platformMiddle, size, Material.RED_STAINED_GLASS, false);
        WorldEditor.DrawCircle(platformMiddle, 3, Material.GOLD_BLOCK, true);
        return platformMiddle;
    }

    public int GetSize() {
        return (int) Math.round(5 + players.size() * 1.2);
    }

    public int GetSizeWithBuffer() {
        return GetSize() + 20;
    }
}
