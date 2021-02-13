package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Random;

public class Portals implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {

        var grassHeight = 3;
        var portalPlatformBounds = 10;
        var arenaPlatformBuffer = 5;

        var random = new Random();

        for (int i = 0; i < grassHeight; i++) {
            WorldEditor.DrawCircle(arenaElements.PlatformCenter.clone().subtract(0, i, 0), arenaElements.Radius - 1, Material.GRASS_BLOCK, true);
        }

        WorldEditor.DrawCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, Material.GRASS_BLOCK, true);

        var portalLocations = new ArrayList<Location>();

        portalLocations.add(arenaElements.PlatformCenter);

        //Generate portal locations
        WorldEditor.CreateCircle(arenaElements.PlatformCenter, arenaElements.Radius - 1, 4).forEach(location -> {
            if (random.nextBoolean()) {
                portalLocations.add(location.clone().subtract(0, random.nextInt(portalPlatformBounds) + arenaPlatformBuffer, 0));
            } else {
                portalLocations.add(location.clone().add(0, random.nextInt(portalPlatformBounds) + arenaPlatformBuffer, 0));
            }
        });

        //Portals
        portalLocations.forEach(location -> {
            WorldEditor.DrawCircle(location.clone(), (arenaElements.Radius / 4) + 1, Material.LODESTONE, true);
            WorldEditor.DrawBox(location.clone().subtract(1, -1, 0), location.clone().add(2, 4, 0), Material.OBSIDIAN);
            WorldEditor.DrawBox(location.clone().add(0, 1, 0), location.clone().add(0, 3, 0), Material.NETHER_PORTAL);
        });

        var stageElements = new StageElements();
        stageElements.CustomChest = true;
        stageElements.StageEvents = new PortalEvents(portalLocations);

        Bukkit.getPluginManager().registerEvents(stageElements.StageEvents, Main.plugin);

        return stageElements;
    }

    public static class PortalEvents implements Listener {

        private final ArrayList<Location> portals;

        public PortalEvents(ArrayList<Location> portals) {
            this.portals = portals;
        }

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {
            if (event.getTo().getBlock().getType() == Material.NETHER_PORTAL) {
                var currentPortalLocation = event.getTo();
                var currentPortal = portals.stream()
                        .filter(location -> location.getX() == Math.floor(currentPortalLocation.getX()) && location.getZ() == Math.floor(currentPortalLocation.getZ()))
                        .findFirst();
                currentPortal.ifPresent(location -> {
                    var portalIndex = portals.indexOf(location);
                    Location connectingPortal;
                    if (portalIndex + 1 == portals.size()) {
                        connectingPortal = portals.get(0);
                    } else {
                        connectingPortal = portals.get(portalIndex + 1);
                    }
                    event.getPlayer().teleport(connectingPortal.clone().add(0, 1, 1));
                });
            }
        }
    }
}
