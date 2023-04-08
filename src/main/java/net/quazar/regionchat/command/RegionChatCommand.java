package net.quazar.regionchat.command;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.quazar.regionchat.RegionChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.quazar.regionchat.util.ColorUtils.colorize;

public final class RegionChatCommand implements CommandExecutor{
    private final RegionChat plugin;

    public RegionChatCommand(RegionChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender.hasPermission("rgchat.reload")) {
                plugin.reloadPlugin();
                sender.sendMessage("[RegionChat] Plugin configuration successfully reloaded");
                return true;
            }
            sender.sendMessage("[RegionChat] Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (plugin.getSelectedChats().containsKey(player.getUniqueId()))
                sender.sendMessage(colorize(String.format("&a[RegionChat] &eВыбран чат &c%s", plugin.getSelectedChats().get(player.getUniqueId()))));
            else
                sender.sendMessage(colorize("&a[RegionChat] &eЧат региона не выбран!"));
            return true;
        }
        String id = args[0];
        ProtectedRegion region;
        if ((region = plugin.getRegionManager().getRegion(id)) == null) {
            sender.sendMessage(colorize(String.format("&a[RegionChat] &eРегион &c%s&e не найден", id)));
            return true;
        }
        if (!(region.getMembers().contains(player.getUniqueId()) || region.getOwners().contains(player.getUniqueId()))) {
            sender.sendMessage(colorize("&a[RegionChat] &eВы не состоите в данном регионе"));
            return true;
        }
        plugin.getSelectedChats().put(player.getUniqueId(), id);
        sender.sendMessage(colorize(String.format("&a[RegionChat] &eВыбран чат региона &c%s", id)));
        return true;
    }
}
