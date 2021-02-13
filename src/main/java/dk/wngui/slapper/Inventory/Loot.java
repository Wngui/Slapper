package dk.wngui.slapper.Inventory;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Loot {

    public static void UpgradeSlapperFish(Player player) {
        var slapper = Loot.Slapper();
        var superSlapper = Loot.SuperSlapper();
        var ultraSlapper = Loot.UltraSlapper();
        var playerInventory = player.getInventory();

        if (playerInventory.contains(slapper)) {
            playerInventory.remove(slapper);
            playerInventory.addItem(superSlapper);
        } else if (playerInventory.contains(superSlapper)) {
            playerInventory.remove(superSlapper);
            playerInventory.addItem(ultraSlapper);
        } else if (playerInventory.contains(ultraSlapper)) {
            playerInventory.addItem(Loot.SuperTNT(5));
        }
    }

    public static ItemStack Slapper() {
        var slapper = new ItemStack(Material.SALMON);
        ItemDescription(slapper, "Slapper!", null);
        slapper.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        return slapper;
    }

    public static ItemStack SuperSlapper() {
        var superSlapper = new ItemStack(Material.TROPICAL_FISH);
        ItemDescription(superSlapper, "Super Slapper!", null);
        superSlapper.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        return superSlapper;
    }

    public static ItemStack UltraSlapper() {
        var superSlapper = new ItemStack(Material.PUFFERFISH);
        ItemDescription(superSlapper, "Ultra Slapper!", null);
        superSlapper.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        return superSlapper;
    }

    public static ItemStack SuperTNT(int amount) {
        var tnt = new ItemStack(Material.TNT);
        ItemDescription(tnt, "TNT!", null);
        tnt.setAmount(amount);
        return tnt;
    }

    public static ItemStack EnderPearl(int amount) {
        var pearl = new ItemStack(Material.ENDER_PEARL);
        ItemDescription(pearl, "Ender Pearl", "Teleport to thrown location");
        pearl.setAmount(amount);
        return pearl;
    }

    public static ItemStack Trident(int amount) {
        var trident = new ItemStack(Material.TRIDENT);
        ItemDescription(trident, "Infinite Tridents", "Time to sleep with the fishes!");
        trident.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        trident.setAmount(amount);
        SetUnbreakable(trident);
        return trident;
    }

    public static ItemStack FishingRod() {
        var rod = new ItemStack(Material.FISHING_ROD);
        ItemDescription(rod, "Fishing rod", "Get over here!");
        SetUnbreakable(rod);
        return rod;
    }

    public static ItemStack SplashSlow() {
        var potion = new ItemStack(Material.SPLASH_POTION);
        var meta = (PotionMeta) potion.getItemMeta();
        meta.setColor(Color.PURPLE);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 150, 1), true);
        potion.setItemMeta(meta);
        ItemDescription(potion, "Splash potion of slow", "Throw to slow enemies");
        return potion;
    }

    public static ItemStack InvisibilityPotion() {
        var potion = new ItemStack(Material.POTION);
        var meta = (PotionMeta) potion.getItemMeta();
        meta.setColor(Color.BLACK);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400, 1), true);
        potion.setItemMeta(meta);
        ItemDescription(potion, "Invisibility potion", "lasts 20 seconds");
        return potion;
    }

    public static ItemStack SandBlock() {
        var sand = new ItemStack(Material.SAND);
        ItemDescription(sand, "Sand block", "Might be useful for reaching high places?");
        return sand;
    }

    public static ItemStack IronGolemSpawner() {
        var golem = new ItemStack(Material.IRON_BLOCK);
        ItemDescription(golem, "Iron Golem Guardian", "Place the block to spawn a golem");
        return golem;
    }

    public static ItemStack BeeEgg() {
        var egg = new ItemStack(Material.BEE_SPAWN_EGG);
        ItemDescription(egg, "Bee egg", "Summon a friendly explosive bee");
        return egg;
    }

    public static ItemStack JumpyBoots() {
        var boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemDescription(boots, "Jumpy Boots", ChatColor.LIGHT_PURPLE + "F" + ChatColor.MAGIC + "UCK" + ChatColor.LIGHT_PURPLE + " Gravity!");
        SetUnbreakable(boots);
        return boots;
    }

    public static ItemStack FrostyBoots() {
        var boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.FROST_WALKER, 1);
        ItemDescription(boots, "Frosty Boots", "Walk on water");
        SetUnbreakable(boots);
        return boots;
    }

    public static ItemStack Rocket() {
        var rocket = new ItemStack(Material.FIREWORK_ROCKET);
        ItemDescription(rocket, "Rocket", "Launch into the air");
        return rocket;
    }

    public static ItemStack StickyBomb() {
        var item = new ItemStack(Material.SNOWBALL);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        ItemDescription(item, "Sticky bomb", "Attaches a bomb to an enemy");
        SetUnbreakable(item);
        return item;
    }

    public static ItemStack CreeperEgg() {
        var item = new ItemStack(Material.EGG);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemDescription(item, "Creeper egg", "Throwable creeper");
        SetUnbreakable(item);
        return item;
    }

    public static ItemStack BookOfTheDead() {
        var item = new ItemStack(Material.BOOK);
        item.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
        ItemDescription(item, ChatColor.LIGHT_PURPLE+"Book of the Dead", "Summon two skeletal minions near an enemy");
        SetUnbreakable(item);
        return item;
    }

    public static ItemStack HorseSaddle() {
        var item = new ItemStack(Material.SADDLE);
        ItemDescription(item, "Horse Saddle", "Summon a trusty steed");
        SetUnbreakable(item);
        return item;
    }

    public static ItemStack AncientWitherSkull() {
        var item = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemDescription(item, ChatColor.BLACK+"Ancient Skull", "Summon an ancient chaotic being into the arena...");
        SetUnbreakable(item);
        return item;
    }

    public static ItemStack Money(int amount) {
        return new ItemStack(Material.EMERALD, amount);
    }

    private static void ItemDescription(ItemStack item, String name, String loreText) {
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (loreText != null && !loreText.isEmpty()) {
                var lore = new ArrayList<String>();
                lore.add(ChatColor.LIGHT_PURPLE + loreText);
                meta.setLore(lore);
            }
        }
        item.setItemMeta(meta);
    }

    private static void SetUnbreakable(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
        }
        item.setItemMeta(meta);
    }
}
