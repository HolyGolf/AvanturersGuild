package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class commands implements CommandExecutor {
    private final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);
    private final events ev = new events();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("No_permission"))));
        }
        return true;
    }
}
