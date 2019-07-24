package com.zeshanaslam.umcchunkloader.config;

import com.zeshanaslam.umcchunkloader.Main;
import com.zeshanaslam.umcchunkloader.config.anchor.AnchorStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigStore {

    private final Main main;

    public HashMap<Messages, String> messages;
    public ItemData itemData;
    public AnchorStore anchorStore;
    public List<String> hologram;
    public double xOffset;
    public double yOffset;
    public double zOffset;

    public ConfigStore(Main main) {
        this.main = main;
        this.itemData = loadItem();

        this.messages = new HashMap<>();
        for (String key: main.getConfig().getConfigurationSection("Messages").getKeys(false)) {
            this.messages.put(Messages.valueOf(key), ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages." + key)));
        }

        this.hologram = new ArrayList<>();
        for (String key: main.getConfig().getStringList("Hologram.Text")) {
            this.hologram.add(ChatColor.translateAlternateColorCodes('&', key));
        }

        xOffset = main.getConfig().getDouble("Hologram.XOffset");
        yOffset = main.getConfig().getDouble("Hologram.YOffset");
        zOffset = main.getConfig().getDouble("Hologram.ZOffset");
    }

    public enum Messages {
        InventoryFull,
        GivenAnchor,
        GavePlayerAnchor,
        PlacedAnchor,
        NotOwner,
        BrokeAnchor
    }

    private ItemData loadItem() {
        String key = "Item";

        Material material = Material.matchMaterial(main.getConfig().getString(key + ".Material"));
        if (material == null) {
            System.out.println(main.getConfig().getString(key + ".Material") + ": Material not found!");
        }

        int byteData = main.getConfig().getInt(key + ".ByteData");
        String display = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString(key + ".Display"));
        List<String> lore = new ArrayList<>();
        for (String line:  main.getConfig().getStringList(key + ".Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        boolean glow = main.getConfig().getBoolean(key + ".Glow");

        return new ItemData(key, material, byteData, display, lore, glow);
    }

    public void save() {
        anchorStore.save();
    }
}
