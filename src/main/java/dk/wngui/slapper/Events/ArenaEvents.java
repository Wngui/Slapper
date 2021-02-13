package dk.wngui.slapper.Events;

import dk.wngui.slapper.Arena.ArenaController;
import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Inventory.PlayerInventoryManager;
import dk.wngui.slapper.Main;
import net.minecraft.server.v1_16_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ArenaEvents implements Listener {

    private final PlayerInventoryManager playerInventoryManager;
    private final ArenaController arenaController;

    public ArenaEvents(PlayerInventoryManager playerInventoryManager, ArenaController arenaController) {
        this.playerInventoryManager = playerInventoryManager;
        this.arenaController = arenaController;
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        var victim = event.getEntity();
        var killer = event.getEntity().getKiller();
        if (killer != null && killer != victim) {
            Loot.UpgradeSlapperFish(killer);
            killer.sendMessage("Slapper Upgrade!");
            playerInventoryManager.AddItem(killer, Loot.Money(1));
        }

        //Toggle spectate mode if dead
        victim.setGameMode(GameMode.SPECTATOR);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            if (victim.isDead()) {
                ((CraftPlayer) victim).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
                var playerToSpectate = Bukkit.getOnlinePlayers().stream().parallel().filter(p -> p.getGameMode() != GameMode.SPECTATOR).findAny();
                playerToSpectate.ifPresent(p -> victim.teleport(p.getLocation().add(0, 5, 0)));
            }
        });

        //Payout consolation prize
        playerInventoryManager.AddItem(victim, Loot.Money(1));

        arenaController.CheckForRoundWinner();
    }
}
