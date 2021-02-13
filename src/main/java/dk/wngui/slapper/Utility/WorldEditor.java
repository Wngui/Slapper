package dk.wngui.slapper.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorldEditor {

    public static void ClearAirSpace(Location location, int areaSize) {
        DrawSphere(location, areaSize, Material.AIR, false);
        UpdateWorldStateBySphere(location, areaSize + 10);
    }

    public static void DrawBox(Location startLocation, Location endLocation, Material material) {
        CreateBox(startLocation, endLocation).forEach(location -> {
            var block = location.getBlock();
            if (block.getType() != material) {
                location.getBlock().setType(material);
            }
        });
    }

    public static void DrawCircle(Location location, int radius, Material material, boolean fill) {
        var interval = radius * 14;
        for (int r = radius; r > -1; r -= 1) {
            CreateCircle(location, r, interval).forEach(point -> point.getBlock().setType(material));
            if (!fill) break;
        }
    }

    public static void DrawSphere(Location centerBlock, int radius, Material material, boolean hollow) {
        var blocks = CreateSphere(centerBlock, radius, hollow);
        blocks.forEach(location -> {
            if (location.getBlock().getType() != material) {
                location.getBlock().setType(material);
            }
        });
    }

    public static ArrayList<Location> CreateCircle(Location middle, int radius, int interval) {
        var circlePoints = new ArrayList<Location>();
        var theta = (2 * Math.PI);
        for (double i = 0; i < theta; i += theta / interval) {
            circlePoints.add(middle.clone().add(Math.round(radius * Math.cos(i)), 0, Math.round(radius * Math.sin(i))));
        }
        return circlePoints;
    }

    public static ArrayList<Location> CreateBox(Location startLocation, Location endLocation) {

        var startX = (int) startLocation.getX();
        var startZ = (int) startLocation.getZ();
        var startY = (int) startLocation.getY();

        var endX = (int) endLocation.getX();
        var endZ = (int) endLocation.getZ();
        var endY = (int) endLocation.getY();

        var boxPoints = new ArrayList<Location>();

        for (int y = startY; y != endY; y = y > endY ? y - 1 : y + 1) {
            for (int x = startX; x != endX; x = x > endX ? x - 1 : x + 1) {
                for (int z = startZ; z != endZ; z = z > endZ ? z - 1 : z + 1) {
                    boxPoints.add(new Location(startLocation.getWorld(), x, y, z));
                }
                boxPoints.add(new Location(startLocation.getWorld(), x, y, startZ));
            }
            boxPoints.add(new Location(startLocation.getWorld(), startX, y, startZ));
        }

        return boxPoints;
    }

    private ArrayList<Location> CreateCube(Location location, int areaSize) {
        var middleLocation = location.clone();

        var subtractArea = Math.floor(areaSize / 2);
        var startLocation = middleLocation.clone().subtract(subtractArea, subtractArea, subtractArea);

        var addArea = Math.ceil(areaSize / 2d);
        var endLocation = middleLocation.clone().add(addArea, addArea, addArea);

        return CreateBox(startLocation, endLocation);
    }

    public static ArrayList<Location> CreateSphere(Location centerBlock, int radius, boolean hollow) {
        ArrayList<Location> sphereBlocks = new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; ++x) {
            for (int y = by - radius; y <= by + radius; ++y) {
                for (int z = bz - radius; z <= bz + radius; ++z) {
                    double distance = (bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y);
                    if (distance < (double) (radius * radius) && (!hollow || distance >= (double) ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        sphereBlocks.add(l);
                    }
                }
            }
        }

        return sphereBlocks;
    }

    public static void UpdateWorldStateBySphere(Location center, int radius) {
        var sphere = CreateSphere(center, radius, false);
        UpdateWorldState(sphere, Bukkit.getOnlinePlayers());
    }

    private static void UpdateWorldState(List<Location> points, Collection<? extends Player> onlinePlayers) {
        points.forEach(point -> {
            var block = point.getBlock();
            onlinePlayers.forEach(player -> player.sendBlockChange(point, block.getBlockData()));
        });
    }
}
