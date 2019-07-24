package com.zeshanaslam.umcchunkloader.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

public class SafeLocation {
    public UUID world;
    public int x;
    public int y;
    public int z;
    public float pitch = -1;
    public float yaw = -1;

    public SafeLocation() {}

    public SafeLocation(UUID world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation() {
        Location location = new Location(Bukkit.getWorld(world), x, y, z);
        if (pitch != -1)
            location.setPitch(pitch);
        if (yaw != -1)
            location.setYaw(yaw);
        return location;
    }

    public SafeLocation fromLocation(Location location) {
        world = location.getWorld().getUID();
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafeLocation that = (SafeLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0 &&
                Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }
}
