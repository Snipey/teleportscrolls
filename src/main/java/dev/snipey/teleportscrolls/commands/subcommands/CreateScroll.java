package dev.snipey.teleportscrolls.commands.subcommands;

import dev.snipey.teleportscrolls.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CreateScroll extends SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates a scroll with given amount of charges";
    }

    @Override
    public String getSyntax() {
        return "/scroll create <charges>";
    }

    @Override
    public void perform(Player player, String[] args) {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta meta = scroll.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        meta.setDisplayName("Scroll of Teleportation");
        lore.add(ChatColor.YELLOW + "Charges" + ChatColor.YELLOW + "" + ": " + ChatColor.LIGHT_PURPLE + "2000");
        meta.setLore(lore);
        scroll.setItemMeta(meta);
        player.getInventory().addItem(scroll);
    }
}
