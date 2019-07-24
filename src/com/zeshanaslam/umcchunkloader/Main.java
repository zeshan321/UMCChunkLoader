package com.zeshanaslam.umcchunkloader;

import co.aikar.commands.PaperCommandManager;
import com.zeshanaslam.umcchunkloader.config.ConfigStore;
import com.zeshanaslam.umcchunkloader.config.anchor.AnchorStore;
import com.zeshanaslam.umcchunkloader.events.AnchorEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static PaperCommandManager commandManager;
    public ConfigStore configStore;
    public List<UUID> override;

    @Override
    public void onEnable() {
        super.onEnable();

        override = new ArrayList<>();

        // Config
        saveDefaultConfig();
        configStore = new ConfigStore(this);
        configStore.anchorStore = new AnchorStore(this);

        // Commands
        if (commandManager == null) {
            commandManager = new PaperCommandManager(this);
            commandManager.enableUnstableAPI("help");
            commandManager.registerCommand(new AnchorCommands(this));
        }

        // Listeners
        getServer().getPluginManager().registerEvents(new AnchorEvents(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        configStore.save();
    }
}
