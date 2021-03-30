package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class commands implements CommandExecutor {
    private final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0 || args[0].equals("help")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("Help_Command"))));
            } else if (args[0].equals("setrank") && !args[1].isEmpty() && !args[2].isEmpty() && sender.isOp()) {
                Player pl = Bukkit.getPlayerExact(args[1]);
                if (pl != null) {
                    try {
                        try {
                            Integer.parseInt(args[2]);
                            MySQL.SavePlayer(pl, Integer.parseInt(args[2]));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } finally {
                            try {
                                sender.sendMessage(ChatColor.YELLOW + "Now " + ChatColor.GOLD + pl.getDisplayName() + ChatColor.YELLOW + " rank is " + MySQL.GetRank(pl.getPlayer()) + " (" + new events().ranks_determination(MySQL.GetRank(pl.getPlayer())) + ")");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("IntError"))));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("PlayerNotFound"))));
                }
            } else if(args[0].equals("getrank") && !args[1].isEmpty() && sender.isOp()) {
                Player pl = Bukkit.getPlayerExact(args[1]);
                if (pl != null) {
                    try {
                        sender.sendMessage(ChatColor.GOLD + pl.getDisplayName() + ChatColor.YELLOW + " rank is " + MySQL.GetRank(pl.getPlayer()) + " (" + new events().ranks_determination(MySQL.GetRank(pl.getPlayer())) + ")");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }  else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("PlayerNotFound"))));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("UndefinedCommand"))));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("No_permission"))));
        }
        return true;
    }
}
