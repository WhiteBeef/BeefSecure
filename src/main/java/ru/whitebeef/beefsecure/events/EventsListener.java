package ru.whitebeef.beefsecure.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import ru.whitebeef.beefsecure.BeefSecure;
import ru.whitebeef.beefsecure.utils.Messages;

public class EventsListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (BeefSecure.canPlace(event.getBlock().getLocation(), event.getBlock().getType()))
            return;
        event.getPlayer().playSound(event.getPlayer().getLocation(), BeefSecure.getInstance().getSound(), 1, 1);
        event.setCancelled(true);
        Messages.sendActionBarMessage(event.getPlayer());
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (BeefSecure.canPlace(event.getBlock().getLocation(), event.getBucket()))
            return;
        event.getPlayer().playSound(event.getPlayer().getLocation(), BeefSecure.getInstance().getSound(), 1, 1);
        event.setCancelled(true);
        Messages.sendActionBarMessage(event.getPlayer());
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (!BeefSecure.canSpawn(event.getVehicle().getType()))
            event.setCancelled(true);
    }
    @EventHandler
    public void onDispenserWork(BlockDispenseEvent event) {
        if(!BeefSecure.canPlace(event.getBlock().getLocation(), event.getItem().getType()))
            event.setCancelled(true);
    }

}
