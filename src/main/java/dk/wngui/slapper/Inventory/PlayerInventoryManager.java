package dk.wngui.slapper.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInventoryManager {
    final HashMap<Player, ArrayList<ItemStack>> staticInventory;

    public PlayerInventoryManager() {
        staticInventory = new HashMap<>();
    }

    public void RefreshInventory(Player player) {
        var inventory = player.getInventory();
        inventory.clear();
        var items = GetInventoryItems(player);
        items.forEach(inventory::addItem);
    }

    public void AddItem(Player player, ItemStack item) {
        System.out.println(player.getDisplayName()+ " received "+item.toString());
        var inventoryItems = GetInventoryItems(player);
        inventoryItems.add(item);
    }

    private ArrayList<ItemStack> GetInventoryItems(Player player) {
        if (!staticInventory.containsKey(player)) {
            staticInventory.put(player, new ArrayList<>());
        }
        return staticInventory.get(player);
    }

    public void SetItemAmount(Player player, ItemStack item) {
        var inventoryItems = GetInventoryItems(player);
        var similarItemStacks = new ArrayList<ItemStack>();
        inventoryItems.stream().filter(itemStack -> itemStack.isSimilar(item)).forEach(similarItemStacks::add);
        inventoryItems.removeAll(similarItemStacks);
        inventoryItems.add(item);
    }
}
