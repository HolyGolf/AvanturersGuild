package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.util.Objects;

public class worlds {
    private final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);

    public void CreateWorld(String name) {
        try {
            File trg = new File(plugin.getDataFolder(), "/Worlds");
            if (!trg.exists()) {
                trg.mkdirs();
            }
            World target = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("DefaultWorld")));
            assert target != null;
            File targetFolder = target.getWorldFolder();
            File WorldToCopy = new File(plugin.getDataFolder(), "/Worlds/" + name);
            if (WorldToCopy.exists() && Bukkit.getWorld(name) == null) {
                FileUtil.copy(WorldToCopy, targetFolder);
            }
            loadWorld(name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Bukkit.broadcastMessage(ChatColor.GREEN + "World " + name + " has been loaded.");
        }
    }

    public void UnloadWorld(String wrl) {
        World world = Bukkit.getWorld(wrl);
        if(world != null) {
            try {
                Bukkit.getServer().unloadWorld(world, true);
            }
            finally {
                Bukkit.broadcastMessage(ChatColor.GREEN + "World " + wrl + " has been unloaded.");
            }
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "Unknown world: " + wrl);
        }
    }

    public void loadWorld(String wrl) {
        if(Bukkit.getWorld(wrl) == null) {
            Bukkit.getServer().createWorld(new WorldCreator(wrl));
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "Unknown world: " + wrl);
        }
    }

    public void TeleportPlayer(Player player, String wrld) {
        World world = Bukkit.getWorld(wrld);
        if(world != null) {
            try {
                player.teleport(world.getSpawnLocation());
            } finally {
                player.sendMessage(ChatColor.GREEN + "You has been teleported to " + world.getName() + " world");
            }
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "Unknown world: " + wrld);
        }
    }
}
