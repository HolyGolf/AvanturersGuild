package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;git status
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class events implements Listener {
    private final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);

    public void load_ranks() throws SQLException {
        if (!plugin.getConfig().getStringList("Ranks").isEmpty()) {
            int i = 0;
            for (String rawData : plugin.getConfig().getStringList("Ranks")) {
                String[] raw = rawData.split(":");
                MySQL.setRank(raw[0], Integer.parseInt(raw[1]));
            }
        } else {
            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(operator -> operator.sendMessage(ChatColor.RED + "Ranks in config is empty."));
        }
    }

    public String ranks_determination(Integer lvl) throws SQLException {
        String rank_result = null;
        if (lvl < MySQL.minrank()) {
            rank_result = "UnRanked";
        } else {
            for (int i = MySQL.getrank_count(); i > 1; i--) {
                if (lvl >= MySQL.getrank_lvl(i)) {
                    rank_result = MySQL.getrank_name(i);
                }
            }
        }
        return rank_result;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        if (!MySQL.hasPlayer(event.getPlayer()) || !event.getPlayer().hasPlayedBefore()) {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO avanturersguilddata.rankdata(UUID, Player, PlRank) VALUES (?,?,?)");
            ps.setString(1, event.getPlayer().getUniqueId().toString());
            ps.setString(2, event.getPlayer().getName());
            ps.setInt(3, 1);
            ps.executeUpdate();
        }
    }
}
