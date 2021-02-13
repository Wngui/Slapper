package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.Switch;

import java.util.Random;
import java.util.stream.Collectors;

public class Pistons implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        Random random = new Random();

        var sandHeight = 3;

        for (int i = 0; i < sandHeight; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, i, 0), arenaElements.Radius - 1, Material.SAND, true);
        }

        WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, sandHeight + 1, 0), arenaElements.Radius - 1, Material.STICKY_PISTON, true);

        WorldEditor.CreateSphere(arenaElements.PlatformCenter, arenaElements.Radius, false)
                .stream().map(Location::getBlock).filter(block -> block.getType() == Material.STICKY_PISTON)
                .forEach(block -> {
                    var data = (Piston) block.getBlockData();
                    data.setFacing(BlockFace.UP);
                    block.setBlockData(data);

                    var leverBlock = block.getLocation().subtract(0, 1, 0).getBlock();
                    leverBlock.setType(Material.LEVER);
                    var leverData = (Switch) leverBlock.getBlockData();
                    leverData.setAttachedFace(FaceAttachable.AttachedFace.CEILING);
                    leverBlock.setBlockData(leverData);
                });

        var levers = WorldEditor.CreateSphere(arenaElements.PlatformCenter, arenaElements.Radius, false)
                .stream().map(Location::getBlock).filter(block -> block.getType() == Material.LEVER)
                .collect(Collectors.toList());


        var scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> levers.forEach(lever -> {
            if (lever.getType() == Material.LEVER) {
                var data = (Switch) lever.getBlockData();
                data.setPowered(random.nextInt(10) > 5);
                lever.setBlockData(data);
            }
        }), 60L, 60L); //0 Tick initial delay, 20 Tick (1 Second) between repeats

        return new StageElements(scheduleId);
    }
}
