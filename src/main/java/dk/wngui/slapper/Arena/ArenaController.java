package dk.wngui.slapper.Arena;

import dk.wngui.slapper.ArenaStages.Special.GameFinish;
import dk.wngui.slapper.Events.ArenaEvents;
import dk.wngui.slapper.Events.GlobalEvents;
import dk.wngui.slapper.Events.ItemEvents;
import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Inventory.PlayerInventoryManager;
import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.Common;
import net.minecraft.server.v1_16_R3.PacketPlayInClientCommand;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


public class ArenaController {
    private final int maxWins;
    private boolean roundOver = false;
    private final ArenaBuilder arenaStageBuilder;
    private final ArenaStageController arenaStageController;
    private final Location center;
    final ArrayList<Player> players;
    final PlayerInventoryManager playerInventoryManager;
    private final ArenaScoreboardManager arenaScoreboardManager;

    private int currentRound;

    public ArenaController(int maxWins, Location center, ArrayList<Player> players) {
        this.maxWins = maxWins;
        this.center = center;
        this.players = players;
        this.currentRound = 0;
        this.arenaStageBuilder = new ArenaBuilder(players, 100);
        this.playerInventoryManager = new PlayerInventoryManager();
        this.arenaStageController = new ArenaStageController(this);
        this.arenaScoreboardManager = new ArenaScoreboardManager(players);
    }

    public void Start() {
        SetupScoreboard();
        SetupPlayerInventory();
        SetupWorld();
        SetupEvents();
        StartNextRound();
    }

    private void SetupScoreboard() {
        arenaScoreboardManager.CreateScoreboard();
    }

    private void SetupEvents() {
        HandlerList.unregisterAll(Main.plugin);
        Bukkit.getPluginManager().registerEvents(new GlobalEvents(), Main.plugin);
        Bukkit.getPluginManager().registerEvents(new ItemEvents(), Main.plugin);
        Bukkit.getPluginManager().registerEvents(new ArenaEvents(playerInventoryManager, this), Main.plugin);
    }

    private void SetupWorld() {
        var world = center.getWorld();
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_TILE_DROPS, false);
    }

    private void SetupPlayerInventory() {
        players.forEach(player -> {
            playerInventoryManager.AddItem(player, Loot.Slapper());
            playerInventoryManager.AddItem(player, Loot.Rocket());
        });
    }

    private void StartNextRound() {
        roundOver = false;
        currentRound++;
        System.out.println("New round has begun! " + currentRound);
        arenaScoreboardManager.SetRound(currentRound);
        var arenaElements = arenaStageBuilder.Build(center);
        var stageType = StageType.Battle;
        if (currentRound != 0 && currentRound % 4 == 0) {
            stageType = StageType.Shop;
        }
        PreparePlayers();

        arenaStageController.StartStage(arenaElements, stageType);
    }

    private void PreparePlayers() {
        players.forEach(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            player.setGameMode(GameMode.SURVIVAL);
            playerInventoryManager.RefreshInventory(player);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        });
    }

    //Executed from timer
    public void EndShopStage() {
        players.forEach(player -> {
            player.closeInventory();
            int moneyAmount = player.getInventory().all(Material.EMERALD).values().stream().mapToInt(ItemStack::getAmount).sum();
            playerInventoryManager.SetItemAmount(player, Loot.Money(moneyAmount));
        });
        StartNextRound();
    }

    //Executed when a player dies
    public void CheckForRoundWinner() {
        var playersAlive = players.stream().filter(player -> player.getGameMode() == GameMode.SURVIVAL && player.isOnline()).collect(Collectors.toList());
        if (playersAlive.size() <= 1 && !roundOver) {
            roundOver = true;
            if (playersAlive.size() == 1) {
                var lastPlayerAlive = playersAlive.get(0);
                playerInventoryManager.AddItem(lastPlayerAlive, Loot.Money(5));
                arenaScoreboardManager.AddWin(lastPlayerAlive);
                Common.SendChatMessages(players, ChatColor.GOLD + lastPlayerAlive.getDisplayName() + " won the round!");
                lastPlayerAlive.playEffect(EntityEffect.FIREWORK_EXPLODE);
            } else {
                Common.SendChatMessages(players, ChatColor.GOLD + "Round draw...");
            }

            if (!CheckForGameWinner()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        StartNextRound();
                    }
                }.runTaskLater(Main.plugin, 30);
            }
        }
    }

    private boolean CheckForGameWinner() {
        var playerWithHighestScore = arenaScoreboardManager.GetScores().entrySet().stream().max(Map.Entry.comparingByValue()).get();
        if (playerWithHighestScore.getValue() >= maxWins) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    FinishGame(playerWithHighestScore.getKey());
                }
            }.runTaskLater(Main.plugin, 30);
            return true;
        }
        return false;
    }

    private void FinishGame(Player winner) {
        arenaStageController.StopEvents();
        StopArenaEvents();
        var arenaElements = arenaStageBuilder.Build(center);
        var winnerStage = new GameFinish(arenaScoreboardManager);
        winnerStage.execute(arenaElements);

        Common.SendChatMessages(players, "");
        Common.SendChatMessages(players, ChatColor.GREEN + winner.getDisplayName() + " has won the game!");
        //Cool winner effects!
        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    winner.spawnParticle(Particle.REDSTONE, 0, 0, 0, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.GREEN, 1));
                }
            }.runTaskLater(Main.plugin, i * 10);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Common.SendChatMessages(players, "");
                Common.SendChatMessages(players, ChatColor.GREEN + "Game ending in 15 seconds...");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        arenaStageBuilder.CleanupArenaArea(arenaElements.PlatformCenter, arenaStageBuilder.GetSizeWithBuffer());
                        arenaScoreboardManager.Remove();
                        players.stream().filter(player -> !player.isDead() && player.isOnline()).forEach(player -> {
                            //Give the players a nice landing when the platform is removed.
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 300, 1));
                        });
                    }
                }.runTaskLater(Main.plugin, 300);
            }
        }.runTaskLater(Main.plugin, 100);
    }

    public void StopArenaEvents() {
        arenaStageController.StopEvents();
        HandlerList.unregisterAll(Main.plugin);
        players.forEach(player -> player.setGameMode(GameMode.SURVIVAL));
    }
}
