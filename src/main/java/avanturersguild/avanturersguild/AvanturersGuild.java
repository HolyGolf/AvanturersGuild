package avanturersguild.avanturersguild;
import avanturersguild.avanturersguild.additional.MySQL;
import avanturersguild.avanturersguild.additional.commands;
import avanturersguild.avanturersguild.additional.events;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.Objects;

public final class AvanturersGuild extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            MySQL.connect();
            register_commands();
            register_events();
            loadConfig();
        } finally {
            this.getLogger().info("Плагин запущен!");
            this.getLogger().info("Версия плагина: " + this.getDescription().getVersion());
        }
    }

    public void register_commands() {
        try {
            Objects.requireNonNull(this.getCommand("aguild")).setExecutor(new commands());
        } finally {
            this.getLogger().info("Комманды загружены!");
        }
    }

    public void register_events() {
        try {
            getServer().getPluginManager().registerEvents(new events(), this);
            new events().load_ranks();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.getLogger().info("Ивенты загружены!");
        }
    }

    public void loadConfig() {
        try {
            File config = new File(getDataFolder() + File.separator + "config.yml");
            if (!config.exists()) {
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.getLogger().info("Конфиг загружен.");
        }
    }

    @Override
    public void onDisable() {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("TRUNCATE avanturersguilddata.ranks");
            ps.executeUpdate();
            MySQL.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.getLogger().info("Плагин выключен!");
        }
    }
}
