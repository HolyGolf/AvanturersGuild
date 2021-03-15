package avanturersguild.avanturersguild;
import avanturersguild.avanturersguild.additional.commands;
import avanturersguild.avanturersguild.additional.events;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AvanturersGuild extends JavaPlugin {

    private final String storagePath = getDataFolder() + "/userdata.json";

    @Override
    public void onEnable() {
        register_events();
        checkStorage();
        loadConfig();
        this.getLogger().info("Плагин запущен!");
        this.getLogger().info("Версия плагина: " + this.getDescription().getVersion());
    }

    public void register_events() {
        try {
            getServer().getPluginManager().registerEvents(new events(), this);
            new events().load_ranks();
            Objects.requireNonNull(this.getCommand("-----")).setExecutor(new commands()); //Добавить комманду
            this.getLogger().info("Ивенты и комманды загружены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            File config = new File(getDataFolder() + File.separator + "config.yml");
            if (!config.exists()) {
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            }
            this.getLogger().info("Конфиг загружен.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            for (Player player : getServer().getOnlinePlayers()) {
                if (getPlayer(player.getName()) != null) {
                    savePlayer(player, Integer.parseInt(Objects.requireNonNull(getPlayer(player.getName()))));
                } else {
                    savePlayer(player, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().info("Плагин выключен!");
    }

    private void checkStorage() {
        File pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }

        File userdataFile = new File(storagePath);
        if (!userdataFile.exists()) {
            try {
                FileWriter writer = new FileWriter(userdataFile.getAbsoluteFile());
                writer.write((new JSONArray()).toJSONString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void savePlayer(Player player, Integer rank) {
        JSONObject target = new JSONObject();
        target.put("uuid", player.getUniqueId().toString());
        target.put("lastName", player.getDisplayName());
        target.put("rank", rank);
        writePlayer(target);
    }

    @SuppressWarnings("unchecked")
    private void writePlayer(JSONObject target) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);

            List<JSONObject> list = new ArrayList<>();
            for (Object player : players) {
                JSONObject player_JSON = (JSONObject) player;
                if (!player_JSON.get("uuid").equals(target.get("uuid")))
                    list.add(player_JSON);
            }
            for (int i = 0; i < list.size(); i++) {
                if (Integer.parseInt(target.get("rank").toString()) > Integer.parseInt(list.get(i).get("rank").toString())) {
                    JSONObject temp = list.get(i);
                    list.set(i, target);
                    target = temp;
                }
            }
            list.add(target);

            JSONArray sortedPlayers = new JSONArray();
            sortedPlayers.addAll(list);

            FileWriter writer = new FileWriter(storagePath);
            writer.write(sortedPlayers.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPlayer(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            for (Object o : players) {
                JSONObject player = (JSONObject) o;
                if (player.get("lastName").equals(name)) {
                    return player.get("rank").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
