package dk.wngui.slapper.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Common {
    public static Location NormalizeLocation(Location location) {
        location.setX(Math.floor(location.getX()));
        location.setY(Math.floor(location.getY()));
        location.setZ(Math.floor(location.getZ()));
        return location;
    }

    public static Player RandomAlivePlayer() {
        var random = new Random();
        var alivePlayers = Bukkit.getOnlinePlayers().stream().filter(player -> !player.isDead()).collect(Collectors.toList());
        return alivePlayers.get(random.nextInt(alivePlayers.size()));
    }

    public static ArrayList<Location> GetAdjacentBlocks(Location centerBlock, Material ofType) {
        var adjacentBlocks = new ArrayList<Location>();
        for (int i = -1; i < 2; i++) {
            var blockX = centerBlock.clone().add(i, 0, 0);
            if (blockX.getBlock().getType() == ofType || ofType == null) {
                adjacentBlocks.add(blockX);
            }
            var blockY = centerBlock.clone().add(0, i, 0);
            if (blockY.getBlock().getType() == ofType || ofType == null) {
                adjacentBlocks.add(blockY);
            }
            var blockZ = centerBlock.clone().add(0, 0, i);
            if (blockZ.getBlock().getType() == ofType || ofType == null) {
                adjacentBlocks.add(blockZ);
            }
        }
        return adjacentBlocks;
    }

    public static void AllowAllFaces(Block block) {
        var data = (MultipleFacing) block.getBlockData();
        data.getAllowedFaces().forEach(blockFace -> data.setFace(blockFace, true));
        block.setBlockData(data);
    }

    public static void SendChatMessages(ArrayList<Player> players, String message) {
        players.forEach(player -> player.sendMessage(message));
    }

    public static Location lookAt(Location loc, Location lookat) {
        //Courtesy of bergerkiller
        //https://bukkit.org/threads/lookat-and-move-functions.26768/

        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
}
