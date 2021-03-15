package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.ServerOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class events implements Listener {
    private final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);
    public static HashMap<String, Integer> ranks = new HashMap<>();

    public void load_ranks(){
        if (!plugin.getConfig().getStringList("Ranks").isEmpty()) {
            for (String rawData : plugin.getConfig().getStringList("Ranks")) {
                String[] raw = rawData.split(":");
                ranks.put(raw[0], Integer.valueOf(raw[1]));
            }
        } else {
            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(operator -> operator.sendMessage(ChatColor.RED + "Ranks in config is empty."));
        }
    }

    public String ranks_determination(Player player) {
        String rank_result = null;
        if (Integer.parseInt(Objects.requireNonNull(plugin.getPlayer(player.getName()))) == ranks.values().stream().min(Integer::compare).get()) {
            for (Map.Entry<String,Integer> pair : ranks.entrySet()) {
                if (pair.getValue().equals(ranks.values().stream().min(Integer::compare).get())) {
                    rank_result = pair.getKey();
                }
            }
        } else if (Integer.parseInt(Objects.requireNonNull(plugin.getPlayer(player.getName()))) < ranks.values().stream().min(Integer::compare).get()) {
            rank_result = "UnRanked";
        } else {
            for (String ranks_key : ranks.keySet()) {
                if (Integer.parseInt(Objects.requireNonNull(plugin.getPlayer(player.getName()))) > ranks.get(ranks_key)) {
                    rank_result = ranks_key;
                }
            }
        }
        return rank_result;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getPlayer(event.getPlayer().getName()) == null) {
            plugin.savePlayer(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        TextComponent tc = new TextComponent("Информация");
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( ChatColor.AQUA + "Никнейм: " + event.getPlayer().getName() + ChatColor.GOLD + "\nРанг Авантюриста: " + ranks_determination(event.getPlayer())).create()));
        event.getPlayer().spigot().sendMessage(tc);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getPlayer(event.getPlayer().getName()) != null) {
            plugin.savePlayer(event.getPlayer(), Integer.parseInt(Objects.requireNonNull(event.getPlayer().getName())));
        } else {
            plugin.savePlayer(event.getPlayer(), 0);
        }
    }

}
