package com.zeshanaslam.umcchunkloader;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.zeshanaslam.umcchunkloader.config.ConfigStore;
import com.zeshanaslam.umcchunkloader.config.SafeLocation;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.util.HashTreeSet;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AnchorCommands extends BaseCommand {

    private Main plugin;

    public AnchorCommands(Main plugin) {
        super("umccl");
        this.plugin = plugin;
    }

    @HelpCommand
    public void help(CommandSender sender, CommandHelp help){
        help.showHelp();
    }

    @Subcommand("give")
    @CommandPermission("umccl.give")
    @Description("Gives player anchor item.")
    @Syntax("<player> <amount>")
    @CommandCompletion("@players")
    public void give(CommandSender sender, OnlinePlayer target, @Default("1") int amount) {
        Player player = target.getPlayer();

        ItemStack item = plugin.configStore.itemData.getItem();
        item.setAmount(amount);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);

            player.sendMessage(plugin.configStore.messages.get(ConfigStore.Messages.InventoryFull));
        } else {
            player.getInventory().addItem(item);
            player.sendMessage(plugin.configStore.messages.get(ConfigStore.Messages.GivenAnchor));
        }

        sender.sendMessage(plugin.configStore.messages.get(ConfigStore.Messages.GavePlayerAnchor));
    }

    @Subcommand("override")
    @CommandPermission("umccl.override")
    @Description("Allows breaking anchors that you do not own.")
    public void override(Player sender) {
        if (plugin.override.contains(sender.getUniqueId())) {
            plugin.override.remove(sender.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Override has been disabled!");
        } else {
            plugin.override.add(sender.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Override has been enabled!");
        }
    }
}
