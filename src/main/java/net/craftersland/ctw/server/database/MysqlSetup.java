package net.craftersland.ctw.server.database;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.Properties;

public class MysqlSetup {
    private final CTW ctw;
    private Connection conn;

    public MysqlSetup(final CTW ctw) {
        this.conn = null;
        this.ctw = ctw;
        this.setupDatabase();
        this.maintenanceTask();
    }

    public boolean setupDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final Properties properties = new Properties();
            properties.setProperty("user", this.ctw.getConfigHandler().getString("Database.mysql.user"));
            properties.setProperty("password", this.ctw.getConfigHandler().getString("Database.mysql.password"));
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", this.ctw.getConfigHandler().getString("Database.mysql.ssl"));
            properties.setProperty("requireSSL", this.ctw.getConfigHandler().getString("Database.mysql.ssl"));
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.ctw.getConfigHandler().getString("Database.mysql.host") + ":" + this.ctw.getConfigHandler().getString("Database.mysql.port") + "/" + this.ctw.getConfigHandler().getString("Database.mysql.databaseName"), properties);
        } catch (ClassNotFoundException e) {
            CTW.log.severe("Could not locate drivers for mysql!");
            return false;
        } catch (SQLException e2) {
            CTW.log.severe("Could not connect to mysql database!");
            return false;
        }
        this.setupTable();
        CTW.log.info("Mysql connection successful!");
        return true;
    }

    public void setupTable() {
        try {
            final Statement query = this.conn.createStatement();
            final String data = "CREATE TABLE IF NOT EXISTS `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` (id INT UNSIGNED NOT NULL AUTO_INCREMENT, player_uuid char(36) UNIQUE NOT NULL, player_name varchar(16) NOT NULL, score int(10) NOT NULL, melee_kills int(10) NOT NULL, bow_kills int(10) NOT NULL, bow_distance_kill int(10) NOT NULL, wool_placed int(10) NOT NULL, last_seen char(13) NOT NULL, PRIMARY KEY(id));";
            query.executeUpdate(data);
        } catch (SQLException e) {
            CTW.log.severe("Error creating tables! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean closeConnection() {
        try {
            this.conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reConnect() {
        try {
            long start = 0L;
            long end = 0L;
            start = System.currentTimeMillis();
            CTW.log.info("Attempting to establish a connection to the MySQL server!");
            Class.forName("com.mysql.jdbc.Driver");
            final Properties properties = new Properties();
            properties.setProperty("user", this.ctw.getConfigHandler().getString("Database.mysql.user"));
            properties.setProperty("password", this.ctw.getConfigHandler().getString("Database.mysql.password"));
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", this.ctw.getConfigHandler().getString("Database.mysql.ssl"));
            properties.setProperty("requireSSL", this.ctw.getConfigHandler().getString("Database.mysql.ssl"));
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.ctw.getConfigHandler().getString("Database.mysql.host") + ":" + this.ctw.getConfigHandler().getString("Database.mysql.port") + "/" + this.ctw.getConfigHandler().getString("Database.mysql.databaseName"), properties);
            end = System.currentTimeMillis();
            CTW.log.info("Connection to MySQL server established!");
            CTW.log.info("Connection took " + (end - start) + "ms!");
            return true;
        } catch (Exception e) {
            CTW.log.severe("Could not connect to MySQL server! because: " + e.getMessage());
            return false;
        }
    }

    public boolean checkConnection() {
        try {
            if (this.conn == null) {
                CTW.log.warning("Connection failed. Reconnecting...");
                return this.reConnect();
            }
            if (!this.conn.isValid(3)) {
                CTW.log.warning("Connection is idle or terminated. Reconnecting...");
                return this.reConnect();
            }
            if (this.conn.isClosed()) {
                CTW.log.warning("Connection is closed. Reconnecting...");
                return this.reConnect();
            }
            return true;
        } catch (Exception e) {
            CTW.log.severe("Could not reconnect to Database!");
            return true;
        }
    }

    public Connection getConnection() {
        this.checkConnection();
        return this.conn;
    }

    public void maintenanceTask() {
        if (this.ctw.getConfigHandler().getBoolean("Database.removeOldUsers.enabled")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
                @Override
                public void run() {
                    final long inactivityDays = Long.parseLong(MysqlSetup.this.ctw.getConfigHandler().getString("Database.removeOldUsers.inactive"));
                    final long inactivityMils = inactivityDays * 24L * 60L * 60L * 1000L;
                    final long curentTime = System.currentTimeMillis();
                    final long inactiveTime = curentTime - inactivityMils;
                    CTW.log.info("Maintenance task started...");
                    try {
                        final String sql = "DELETE FROM `" + MysqlSetup.this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `last_seen` <?";
                        final PreparedStatement preparedStatement = MysqlSetup.this.getConnection().prepareStatement(sql);
                        preparedStatement.setString(1, String.valueOf(inactiveTime));
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CTW.log.info("Maintenance complete!");
                }
            }, 380L);
        }
    }
}
