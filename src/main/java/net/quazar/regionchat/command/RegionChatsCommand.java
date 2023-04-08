package net.quazar.regionchat.command;

import net.quazar.regionchat.RegionChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.quazar.regionchat.util.ColorUtils.colorize;

public final class RegionChatsCommand implements CommandExecutor {
    private final RegionChat plugin;

    public RegionChatsCommand(RegionChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[RegionChat] Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        StringBuilder chats = new StringBuilder(colorize("&a[RegionChat] &eВам доступны следующие чаты:\n"));
        AtomicBoolean hasRegions = new AtomicBoolean(false);
        plugin.getRegionManager().getRegions().values().forEach(region -> {
            if (region.getMembers().contains(player.getUniqueId()) || region.getOwners().contains(player.getUniqueId())) {
                chats.append(colorize(String.format("&e- &c%s", region.getId()))).append('\n');
                hasRegions.set(true);
            }
        });
        if (!hasRegions.get()) {
            player.sendMessage(colorize("&a[RegionChat] Вы не состоите ни в одном из регионов"));
            return true;
        }
        player.sendMessage(chats.toString());
        return true;
    }
}
