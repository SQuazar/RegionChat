package net.quazar.regionchat;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.quazar.regionchat.command.RegionChatCommand;
import net.quazar.regionchat.command.RegionChatsCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RegionChat extends JavaPlugin implements Listener {
    private final Map<UUID, String> selectedChats = new HashMap<>();
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        regionManager = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(getConfig().getString("world", "world")));

        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("rgchat").setExecutor(new RegionChatCommand(this));
        getCommand("rgchats").setExecutor(new RegionChatsCommand(this));
    }

    @Override
    public void onDisable() {
        selectedChats.clear();
    }

    public void reloadPlugin() {
        reloadConfig();
        regionManager = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(getConfig().getString("world", "world")));
    }

    public Map<UUID, String> getSelectedChats() {
        return selectedChats;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
