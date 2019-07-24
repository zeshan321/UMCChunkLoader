package com.zeshanaslam.umcchunkloader.config.anchor;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.zeshanaslam.umcchunkloader.Main;
import com.zeshanaslam.umcchunkloader.config.SafeLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Anchor {

    public UUID owner;
    public SafeLocation safeLocation;
    private transient Hologram hologram;

    public Anchor(UUID owner, SafeLocation safeLocation) {
        this.owner = owner;
        this.safeLocation = safeLocation;
    }

    public void createHologram(Main main) {
        Location location = safeLocation.getLocation();
        location.add(main.configStore.xOffset, main.configStore.yOffset, main.configStore.zOffset);

        hologram = HologramsAPI.createHologram(main, location);
        for (String holoText: main.configStore.hologram) {
            hologram.appendTextLine(holoText.replace("%owner%",
                    Bukkit.getOfflinePlayer(owner).getName()));
        }
    }

    public void deleteHologram(Main main) {
        hologram.delete();
    }
}
