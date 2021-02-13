package dk.wngui.slapper.Events;

import dk.wngui.slapper.Inventory.Loot;
import dk.wngui.slapper.Inventory.PlayerInventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

public class StoreEvents implements Listener {

    private final PlayerInventoryManager playerInventoryManager;

    public StoreEvents(PlayerInventoryManager playerInventoryManager) {
        this.playerInventoryManager = playerInventoryManager;
    }

    private Merchant CreateMerchant() {
        Merchant merchant = Bukkit.createMerchant("Arena Merchant");
        merchant.setRecipes(AddTradeGoods());
        return merchant;
    }

    private ArrayList<MerchantRecipe> AddTradeGoods() {
        var tradeGoods = new ArrayList<MerchantRecipe>();
        tradeGoods.add(CreateMerchantRecipe(Loot.EnderPearl(1), 1));
        tradeGoods.add(CreateMerchantRecipe(Loot.SandBlock(), 1));
        tradeGoods.add(CreateMerchantRecipe(Loot.SplashSlow(), 2));
        tradeGoods.add(CreateMerchantRecipe(Loot.FrostyBoots(), 3));
        tradeGoods.add(CreateMerchantRecipe(Loot.FishingRod(), 3));
        tradeGoods.add(CreateMerchantRecipe(Loot.StickyBomb(), 3));
        tradeGoods.add(CreateMerchantRecipe(Loot.BookOfTheDead(), 4));
        tradeGoods.add(CreateMerchantRecipe(Loot.HorseSaddle(), 5));
        tradeGoods.add(CreateMerchantRecipe(Loot.BeeEgg(), 5));
        tradeGoods.add(CreateMerchantRecipe(Loot.InvisibilityPotion(), 8));
        tradeGoods.add(CreateMerchantRecipe(Loot.IronGolemSpawner(), 10));
        //tradeGoods.add(CreateMerchantRecipe(Loot.JumpyBoots(), 3));
        tradeGoods.add(CreateMerchantRecipe(Loot.CreeperEgg(), 15));
        tradeGoods.add(CreateMerchantRecipe(Loot.Rocket(), 20));
        tradeGoods.add(CreateMerchantRecipe(Loot.AncientWitherSkull(), 20));
        return tradeGoods;
    }

    private MerchantRecipe CreateMerchantRecipe(ItemStack item, int emeraldCost) {
        var recipe = new MerchantRecipe(item, 10000);
        recipe.addIngredient(new ItemStack(Material.EMERALD, emeraldCost));
        return recipe;
    }

    @EventHandler
    public void OpenTrade(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof WanderingTrader) {
            e.setCancelled(true);
            e.getPlayer().openMerchant(CreateMerchant(), true);
        }
    }

    @EventHandler
    public void OnPlayerDamageBlock(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void OnBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void OnEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void OnBlockExplode(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void OnCreatureDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof WanderingTrader) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void TradeEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.MERCHANT) {
            if (event.getWhoClicked() instanceof Player) {
                if (event.getAction() == InventoryAction.PICKUP_ALL) {
                    playerInventoryManager.AddItem((Player) event.getWhoClicked(), event.getCurrentItem().clone());
                } else {
                    event.setCancelled(true);
                }

            }
        }
    }
}
