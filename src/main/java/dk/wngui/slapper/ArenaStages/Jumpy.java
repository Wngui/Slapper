package dk.wngui.slapper.ArenaStages;

import dk.wngui.slapper.Main;
import dk.wngui.slapper.Utility.WorldEditor;
import dk.wngui.slapper.models.ArenaElements;
import dk.wngui.slapper.models.IArenaStage;
import dk.wngui.slapper.models.StageElements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class Jumpy implements IArenaStage {
    @Override
    public StageElements execute(ArenaElements arenaElements) {
        int scheduleId = getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {

            var rainbow = new ArrayList<Material>();
            rainbow.add(Material.RED_CONCRETE);
            rainbow.add(Material.ORANGE_CONCRETE);
            rainbow.add(Material.YELLOW_CONCRETE);
            rainbow.add(Material.GREEN_CONCRETE);
            rainbow.add(Material.CYAN_CONCRETE);
            rainbow.add(Material.BLUE_CONCRETE);
            rainbow.add(Material.PURPLE_CONCRETE);

            var materialIndex = 0;
            for (int i = arenaElements.Radius - 1; i > 1; i--) {
                WorldEditor.DrawCircle(arenaElements.PlatformCenter, i, rainbow.get(materialIndex), false);
                if (materialIndex > rainbow.size()) {
                    materialIndex = 0;
                } else {
                    materialIndex++;
                }
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 10));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
            });
        }, 0L);
        return new StageElements(scheduleId);
    }
}
