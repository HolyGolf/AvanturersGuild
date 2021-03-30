package avanturersguild.avanturersguild.additional;

import avanturersguild.avanturersguild.AvanturersGuild;
import jdk.internal.org.jline.reader.ConfigurationPath;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

public class MySQL {

    public static Connection con;

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    // connect
    public static void connect() {
        final AvanturersGuild plugin = AvanturersGuild.getPlugin(AvanturersGuild.class);
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://"  + plugin.getConfig().getString("SQLHost") + ":" + plugin.getConfig().getString("SQLPort") + "/avanturersguilddata?user=" + plugin.getConfig().getString("SQLUser") + "&password=" + plugin.getConfig().getString("SQLPass") + "&useUnicode=true&characterEncoding=UTF-8");
                Statement s = con.createStatement();
                s.executeUpdate("CREATE DATABASE IF NOT EXISTS avanturersguilddata");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS avanturersguilddata.rankdata (UUID VARCHAR(255) PRIMARY KEY NOT NULL, Player VARCHAR(255) NOT NULL, PlRank SMALLINT(255) NOT NULL) ENGINE=InnoDB;");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS avanturersguilddata.ranks (ID SMALLINT(255) PRIMARY KEY NOT NULL AUTO_INCREMENT, RankName VARCHAR(100) NOT NULL, RankLvl SMALLINT(255) NOT NULL) ENGINE=InnoDB;");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                console.sendMessage("База данных подключена!");
            }
        }
    }

    // disconnect
    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                console.sendMessage("База данных отключена!");
            }
        }
    }

    // isConnected
    public static boolean isConnected() {
        return (con == null ? false : true);
    }

    // getConnection
    public static Connection getConnection() {
        return con;
    }

    public static void SavePlayer(Player Player, int rank) throws SQLException {
        try {
            if (isConnected()) {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE avanturersguilddata.rankdata SET PlRank = ? WHERE UUID = ?");
                ps.setInt(1, rank);
                ps.setString(2, Player.getUniqueId().toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setRank (String name, int ranklvl) throws SQLException {
        try {
            if (isConnected()) {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO avanturersguilddata.ranks (ID, RankName, RankLvl) VALUES (DEFAULT,?,?)");
                ps.setString(1, name);
                ps.setInt(2, ranklvl);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int minrank() throws SQLException {
        int minimum = 0;
        PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT MIN(RankLvl) AS minimum FROM avanturersguilddata.ranks");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            minimum = rs.getInt("minimum");
        }
        return minimum;
    }

    public static int getrank_lvl(int id) throws  SQLException {
        int RankLvl = 0;
        PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT RankLvl FROM avanturersguilddata.ranks WHERE ID = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            RankLvl = rs.getInt("RankLvl");
        }
        return RankLvl;
    }

    public static String getrank_name(int id) throws  SQLException {
        String RankName = null;
        PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT RankName FROM avanturersguilddata.ranks WHERE ID = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            RankName = rs.getString("RankName");
        }
        return RankName;
    }

    public static int getrank_count() throws  SQLException {
        int total = 0;
        PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT COUNT(ID) AS total FROM avanturersguilddata.ranks");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt("total");
        }
        return total;
    }

    public static boolean hasPlayer(Player Player) throws SQLException {
        boolean res = false;
        PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT PlRank FROM avanturersguilddata.rankdata WHERE UUID = ?");
        ps.setString(1, Player.getUniqueId().toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            res = true;
        }
        return res;
    }

    public static int GetRank(Player Player) throws SQLException {
        if (isConnected()) {
            int rank = 0;
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT PlRank FROM avanturersguilddata.rankdata WHERE UUID = ?");
            ps.setString(1, Player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rank = rs.getInt("PlRank");
            }
            return rank;
        } else {
            return 0;
        }
    }
}
