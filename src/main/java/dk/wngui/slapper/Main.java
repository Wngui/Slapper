package dk.wngui.slapper;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Objects.requireNonNull(this.getCommand("slap")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }
}
