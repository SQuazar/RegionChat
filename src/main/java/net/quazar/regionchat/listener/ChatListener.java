package net.quazar.regionchat.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.quazar.regionchat.RegionChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.quazar.regionchat.util.ColorUtils.colorize;

public class ChatListener implements Listener {
    private final RegionChat plugin;

    public ChatListener(RegionChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (e.getMessage().length() > 1 && String.valueOf(e.getMessage().charAt(0)).equals(plugin.getConfig().getString("chat-symbol", "#"))) {
            e.setCancelled(true);

            Player player = e.getPlayer();
            if (!plugin.getSelectedChats().containsKey(e.getPlayer().getUniqueId())) {
                player.sendMessage(colorize("&a[RegionChat] &eЧат региона не выбран"));
                return;
            }
            String id = plugin.getSelectedChats().get(player.getUniqueId());
            ProtectedRegion region;
            if ((region = plugin.getRegionManager().getRegion(id)) == null) {
                player.sendMessage(colorize(String.format("&a[RegionChat] &eРегион &c%s&e не найден", id)));
                return;
            }
            if (!(region.getMembers().contains(player.getUniqueId()) || region.getOwners().contains(player.getUniqueId()))) {
                plugin.getSelectedChats().remove(player.getUniqueId());
                player.sendMessage(colorize("&a[RegionChat] &eЧат региона не выбран"));
                return;
            }
            String message = colorize(plugin.getConfig().getString("message-color")) +
                             String.format("[%s] %s > %s", id, player.getName(), e.getMessage().substring(1).trim());
            region.getMembers().getUniqueIds().forEach(uuid -> {
                Player member = Bukkit.getPlayer(uuid);
                if (member == null) return;
                member.sendMessage(message);
            });
            region.getOwners().getUniqueIds().forEach(uuid -> {
                Player member = Bukkit.getPlayer(uuid);
                if (member == null) return;
                member.sendMessage(message);
            });

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.hasPermission("rgchat.socialspy"))
                    p.sendMessage(colorize(plugin.getConfig().getString("message-color")) + String.format("[SPY] %s", message));
            });
        }
    }

}
