package dk.wngui.slapper.Arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.IntStream;

public class ArenaScoreboardManager {
    private final ArrayList<Player> players;
    private HashMap<Player, Score> scores;
    private Score roundCounter;
    private Objective scoreboard;
    private final ArrayList<ChatColor> playerColors = new ArrayList<>(EnumSet.allOf(ChatColor.class));

    public ArenaScoreboardManager(ArrayList<Player> players) {
        this.players = players;
    }

    public void CreateScoreboard() {
        this.scores = new HashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("Title", "dummy", ChatColor.BOLD + "" +ChatColor.GOLD + "SLAP ARENA");
        scoreboard.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.roundCounter = scoreboard.getScore(ChatColor.GRAY + "Round");
        this.roundCounter.setScore(1);

        //Add names to scoreboard
        IntStream.range(0, players.size()).forEach(i -> {
            var player = players.get(i);
            var playerScore = scoreboard.getScore(ChatColor.BOLD + "" + playerColors.get(i + 1) + player.getDisplayName());
            playerScore.setScore(0);
            scores.put(player, playerScore);
        });


        //Show scoreboard
        players.forEach(player -> player.setScoreboard(scoreboard.getScoreboard()));

    }

    public void AddWin(Player winningPlayer) {
        var scoreEntry = scores.get(winningPlayer);
        scoreEntry.setScore(scoreEntry.getScore() + 1);
    }

    public void SetRound(int round) {
        roundCounter.setScore(round);
    }

    public HashMap<Player, Integer> GetScores() {
        var wins = new HashMap<Player, Integer>();
        players.forEach(player -> wins.put(player, scores.get(player).getScore()));
        return wins;
    }

    public void Remove() {
        players.forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
    }
}
