package dk.wngui.slapper.models;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public class StageElements {
    public int ScheduleId = 0;
    public boolean ShouldShrink = true;
    public boolean CustomChest = false;
    public Location ChestSpawnLocation;
    public boolean CustomPlayerSpawns = false;
    public boolean CustomEnvironment = false;
    public boolean CustomPlayerEffects = false;
    public Listener StageEvents;

    public StageElements(int scheduleId){
        this.ScheduleId = scheduleId;
    }

    public StageElements(){}
}
