package dk.wngui.slapper;

import dk.wngui.slapper.Arena.ArenaController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Commands implements CommandExecutor {

    ArenaController arenaController;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("start")) {
                if (arenaController != null) {
                    arenaController.StopArenaEvents();
                }
                arenaController = new ArenaController(5, player.getLocation(), new ArrayList<>(Bukkit.getOnlinePlayers()));
                arenaController.Start();
            }
        }
        return true;
    }
}
