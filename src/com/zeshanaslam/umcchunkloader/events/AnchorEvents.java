package com.zeshanaslam.umcchunkloader.events;

import com.zeshanaslam.umcchunkloader.Main;
import com.zeshanaslam.umcchunkloader.config.ConfigStore;
import com.zeshanaslam.umcchunkloader.config.SafeLocation;
import com.zeshanaslam.umcchunkloader.config.anchor.Anchor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

public class AnchorEvents implements Listener {

    private final Main main;

    public AnchorEvents(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
            return;

        Anchor anchor = main.configStore.anchorStore.getAnchored(event.getChunk());
        if (anchor != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();

        ItemStack itemInHand = event.getItemInHand();
        if (itemInHand == null || !itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName())
            return;

        if (main.configStore.itemData.display.equals(itemInHand.getItemMeta().getDisplayName())) {
            SafeLocation safeLocation = new SafeLocation().fromLocation(event.getBlockPlaced().getLocation());

            Anchor anchor = new Anchor(player.getUniqueId(), safeLocation);
            anchor.createHologram(main);

            main.configStore.anchorStore.anchors.put(safeLocation, anchor);
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.PlacedAnchor));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Anchor anchor = main.configStore.anchorStore.anchors.get(new SafeLocation().fromLocation(event.getBlock()
                .getLocation()));

        if (anchor != null) {
            if (!event.getPlayer().getUniqueId().equals(anchor.owner) && !main.override.contains(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotOwner));
            } else {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(),
                        main.configStore.itemData.getItem());

                // Remove hologram
                anchor.deleteHologram(main);

                main.configStore.anchorStore.anchors.remove(anchor.safeLocation);
                event.getPlayer().sendMessage(main.configStore.messages.get(ConfigStore.Messages.BrokeAnchor));
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
            return;

        event.blockList().removeIf(b -> main.configStore.anchorStore.anchors.containsKey(new SafeLocation()
            .fromLocation(b.getLocation())));
    }

    @EventHandler
    public void onExtend(BlockPistonExtendEvent event) {
        if (event.getBlocks().stream().findFirst().filter(b -> main.configStore.anchorStore.anchors
                .containsKey(new SafeLocation().fromLocation(b.getLocation()))).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent event) {
        if (event.getBlocks().stream().findFirst().filter(b -> main.configStore.anchorStore.anchors
                .containsKey(new SafeLocation().fromLocation(b.getLocation()))).isPresent()) {
            event.setCancelled(true);
        }
    }
}
