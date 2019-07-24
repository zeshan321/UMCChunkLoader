package com.zeshanaslam.umcchunkloader.config;

import com.zeshanaslam.umcchunkloader.utils.EnchantGlow;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemData {
    public String type;
    public Material material;
    public int byteData;
    public String display;
    public List<String> lore;
    public boolean glow;

    public ItemData(String type, Material material, int byteData, String display, List<String> lore, boolean glow) {
        this.type = type;
        this.material = material;
        this.byteData = byteData;
        this.display = display;
        this.lore = lore;
        this.glow = glow;
    }

    public ItemStack getItem() {
        ItemStack itemStack;
        if (byteData == -1) {
            itemStack = new ItemStack(material);
        } else {
            itemStack = new ItemStack(material, 1, (byte) byteData);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(display);
        itemMeta.setLore(lore);

        if (glow) {
            EnchantGlow glow = new EnchantGlow(255);
            itemMeta.addEnchant(glow, 1, true);
        }

        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(1);
        return itemStack;
    }
}
