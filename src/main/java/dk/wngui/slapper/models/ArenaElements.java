package dk.wngui.slapper.models;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaElements {
    public final ArrayList<Player> Players;
    public final ArrayList<Location> SpawnPoints;
    public final Location PlatformCenter;
    public final int Radius;

    public ArenaElements(ArrayList<Player> players, ArrayList<Location> spawnPoints, Location platformCenter, int radius) {
        this.Players = players;
        this.SpawnPoints = spawnPoints;
        this.PlatformCenter = platformCenter;
        this.Radius = radius;
    }

}