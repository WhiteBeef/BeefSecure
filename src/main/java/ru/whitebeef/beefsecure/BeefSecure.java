package ru.whitebeef.beefsecure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefsecure.commands.SecureCommandExecutor;
import ru.whitebeef.beefsecure.events.EventsListener;
import ru.whitebeef.beefsecure.utils.Messages;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public final class BeefSecure extends JavaPlugin {

    private static BeefSecure instance;

    private static HashMap<Material, Integer> minHeight = new HashMap<>();
    private static HashMap<Material, Integer> maxHeight = new HashMap<>();
    private static HashSet<EntityType> blockedVehicles = new HashSet<>();
    private Sound sound = Sound.BLOCK_LAVA_EXTINGUISH;
    private static boolean actionBarEnable = false;

    public static BeefSecure getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getCommand("secure").setExecutor(new SecureCommandExecutor());
        Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
        reload();
    }

    @Override
    public void onDisable() {

    }

    public static boolean canPlace(Location location, Material material) {
        if (location.getY() < getMinHeight(material))
            return false;
        if (location.getY() > getMaxHeight(material))
            return false;
        return true;
    }

    public static boolean canSpawn(EntityType entityType) {
        return !blockedVehicles.contains(entityType);
    }

    private static int getMinHeight(Material material) {
        return minHeight.getOrDefault(material, 0);
    }

    private static int getMaxHeight(Material material) {
        return maxHeight.getOrDefault(material, 255);
    }

    public Sound getSound() {
        return sound;
    }

    public static boolean isActionBarEnable() {
        return actionBarEnable;
    }

    public boolean reload() {
        boolean reloadIsSuccessful = true;
        try {
            File config = new File(getDataFolder() + File.separator + "config.yml");
            FileConfiguration cfg = getConfig();
            if (!config.exists()) {
                getLogger().warning("Config is now exists. Creating new...");
                getConfig().options().copyDefaults(true);
                cfg.save(config);
            }
            saveDefaultConfig();
            reloadConfig();
            instance = this;
            Messages.registerMessages();
            actionBarEnable = cfg.getBoolean("ActionBarMessage.Enable");
            minHeight.clear();
            maxHeight.clear();
            blockedVehicles.clear();
            cfg.getStringList("BlockedVehicles").forEach(str -> blockedVehicles.add(EntityType.valueOf(str)));
            for (String str : cfg.getConfigurationSection("Blocks").getKeys(false)) {
                try {
                    Material material = Material.getMaterial(str.toUpperCase());
                    if (material == null)
                        throw new NullPointerException("Material " + str + " is not defined");
                    minHeight.put(material, cfg.getInt("Blocks." + str + ".min"));
                    maxHeight.put(material, cfg.getInt("Blocks." + str + ".max"));
                } catch (Exception ex) {
                    reloadIsSuccessful = false;
                    ex.printStackTrace();
                    continue;
                }
            }
            sound = Sound.valueOf(cfg.getString("ActionBarMessage.Sound"));
        } catch (Exception ex) {
            ex.printStackTrace();
            reloadIsSuccessful = false;
        }
        return reloadIsSuccessful;
    }

}
