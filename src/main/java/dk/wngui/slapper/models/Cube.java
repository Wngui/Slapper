package dk.wngui.slapper.models;

import org.bukkit.Location;

public class Cube {
    public final Location StartLocation;
    public final Location EndLocation;

    public Cube(Location startLocation, Location endLocation){
        this.StartLocation = startLocation;
        this.EndLocation = endLocation;
    }


}