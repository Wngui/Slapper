package dk.wngui.slapper.Arena;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitScheduler;

import static org.bukkit.Bukkit.getServer;

public class ArenaShrinker {

    private final ArenaElements arenaElements;
    private int shrinkRadius;
    private int shrinkScheduleId;

    public ArenaShrinker(ArenaElements arenaElements, int shrinkRadius) {
        this.arenaElements = arenaElements;
        this.shrinkRadius = shrinkRadius;
    }

    public int BeginShrinking() {
        BukkitScheduler scheduler = getServer().getScheduler();
        this.shrinkScheduleId = scheduler.scheduleSyncRepeatingTask(Main.plugin, () -> {

            var zoneHeight = 100;
            var zoneStart = arenaElements.PlatformCenter.clone().subtract(0, zoneHeight / 2, 0);

            if (shrinkRadius <= 0) {
                WorldEditor.DrawBox(zoneStart, zoneStart.clone().add(0, zoneHeight, 0), Material.AIR);
                scheduler.cancelTask(shrinkScheduleId);
                return;
            }

            //Purge outer rim
            for (int i = 0; i < zoneHeight; i++) {
                WorldEditor.CreateCircle(zoneStart.clone().add(0, i, 0), shrinkRadius, shrinkRadius * 10).forEach(location -> location.getBlock().setType(Material.AIR));
            }

            shrinkRadius--;

            //Mark blocks for purging
            for (int i = 0; i < zoneHeight; i++) {
                WorldEditor.CreateCircle(zoneStart.clone().add(0, i, 0), shrinkRadius, shrinkRadius * 10).forEach(location -> {
                    var block = location.getBlock();
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.RED_STAINED_GLASS);
                    }
                });
            }
            WorldEditor.UpdateWorldStateBySphere(arenaElements.PlatformCenter, arenaElements.Radius + 10);
        }, 100L, 100L);
        return this.shrinkScheduleId;
    }
}
