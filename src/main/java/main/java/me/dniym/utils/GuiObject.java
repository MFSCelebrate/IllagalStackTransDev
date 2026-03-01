/* 自定义 GUI 对象示例
 *
 * 我从 Spigot Wiki 借用了这段代码，这样做相比使用 Bukkit 的普通 Inventory 对象有很多优势。
 * https://www.spigotmc.org/wiki/creating-a-gui-inventory/
 *
 * 这样做的好处是，你可以轻松识别 YOUR 的 GUI，并为 YOUR 的 GUI 处理事件，而不必在普通的监听器类中通过名称来识别它。
 */
package main.java.me.dniym.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiObject implements InventoryHolder, Listener {

    private final Inventory inv;

    public GuiObject() {
        // 创建一个新的物品栏，所有者设为 "this" 以便与其他物品栏比较，大小为 9，命名为“示例”
        inv = Bukkit.createInventory(this, 9, "示例");

        // 将物品放入物品栏
        initializeItems();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    // 你可以随时调用此方法来放入物品，或使用你现有的 GUI 代码
    public void initializeItems() {
        inv.addItem(createGuiItem(
                Material.DIAMOND_SWORD,
                "示例剑",
                "§a第一行描述",
                "§b第二行描述"
        ));
        inv.addItem(createGuiItem(
                Material.IRON_HELMET,
                "§b示例头盔",
                "§a第一行描述",
                "§b第二行描述"
        ));
    }

    // 一个便捷方法，用于创建带有自定义名称和描述的 GUI 物品
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // 设置物品的名称
        meta.setDisplayName(name);
        // 设置物品的描述
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    // 你可以使用此方法打开物品栏
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    /*
     * 强烈建议使用此方法，因为你可以通过简单的检查轻松识别你的自定义物品栏对象：
     * if (inventory.getHolder() instanceof GuiObject)
     */

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() != this) {
            return;
        }
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        // 确认当前物品不为空
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        final Player p = (Player) e.getWhoClicked();
        // 对于你的物品栏点击，使用槽位点击是最佳选择
        p.sendMessage("你点击了槽位 " + e.getRawSlot());
    }

}