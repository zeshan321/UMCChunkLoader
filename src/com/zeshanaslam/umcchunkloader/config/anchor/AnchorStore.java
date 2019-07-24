package com.zeshanaslam.umcchunkloader.config.anchor;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.gson.Gson;
import com.zeshanaslam.umcchunkloader.Main;
import com.zeshanaslam.umcchunkloader.config.SafeLocation;
import com.zeshanaslam.umcchunkloader.utils.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AnchorStore {

    private final Gson gson;
    private final Main main;
    public HashMap<SafeLocation, Anchor> anchors;

    public AnchorStore(Main main) {
        this.gson = new Gson();
        this.main = main;
        this.anchors = new HashMap<>();
        load();
    }

    private void load() {
        FileHandler fileHandler = new FileHandler("plugins/UMCChunkLoader/data.yml");
        if (fileHandler.contains("Data")) {
            List<String> data = fileHandler.getStringList("Data");
            for (String json: data) {
                Anchor anchor = gson.fromJson(json, Anchor.class);
                Location location = anchor.safeLocation.getLocation();

                // Load chunk
                location.getChunk().load();

                // Create hologram
                anchor.createHologram(main);

                anchors.put(anchor.safeLocation, anchor);
            }
        }

        System.out.println("Loaded " + anchors.size() + " blocks!");
    }

    public void save() {
        FileHandler fileHandler = new FileHandler("plugins/UMCChunkLoader/data.yml");

        List<String> json = new ArrayList<>();
        for (Anchor sign: anchors.values()) {
            json.add(gson.toJson(sign));
        }

        fileHandler.createNewStringList("Data", json);
        fileHandler.save();

        System.out.println("Saved " + json.size() + " blocks!");
    }

    public Anchor getAnchored(Chunk chunk) {
        Optional<Anchor> anchor = anchors.values().stream().findFirst().filter(a -> a.safeLocation.getLocation().getChunk().equals(chunk));
        return anchor.orElse(null);
    }
}
