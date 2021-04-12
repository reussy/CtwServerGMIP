package net.craftersland.ctw.server.database;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataHandler {
    private final CTW ctw;
    private Connection conn;

    public DataHandler(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean hasAccount(final Player player) {
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "SELECT `player_uuid` FROM `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(sql);
            preparedUpdateStatement.setString(1, player.getUniqueId().toString());
            result = preparedUpdateStatement.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return false;
        } finally {
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            result.close();
            preparedUpdateStatement.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public boolean createAccount(final Player player) {
        PreparedStatement preparedStatement = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "INSERT INTO `" + this.ctw.getConfigHandler().getString("Database.TableName") + "`(`player_uuid`, `player_name`, `score`, `melee_kills`, `bow_kills`, `bow_distance_kill`, `wool_placed`, `last_seen`) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setString(8, String.valueOf(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                preparedStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public boolean setScore(final Player player, final Integer score) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String updateSql = "UPDATE `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` " + "SET `score` = ?" + ", `player_name` = ?" + ", `last_seen` = ?" + " WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(updateSql);
            preparedUpdateStatement.setInt(1, score);
            preparedUpdateStatement.setString(2, player.getName());
            preparedUpdateStatement.setString(3, String.valueOf(System.currentTimeMillis()));
            preparedUpdateStatement.setString(4, player.getUniqueId().toString());
            preparedUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public Integer getScore(final Player player) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "SELECT `score` FROM `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(sql);
            preparedUpdateStatement.setString(1, player.getUniqueId().toString());
            result = preparedUpdateStatement.executeQuery();
            if (result.next()) {
                return result.getInt("score");
            }
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        } finally {
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            result.close();
            preparedUpdateStatement.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public boolean setKills(final Player player, final int meleeKills, final int bowKills) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String updateSql = "UPDATE `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` " + "SET `melee_kills` = ?" + ", `bow_kills` = ?" + ", `player_name` = ?" + ", `last_seen` = ?" + " WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(updateSql);
            preparedUpdateStatement.setInt(1, meleeKills);
            preparedUpdateStatement.setInt(2, bowKills);
            preparedUpdateStatement.setString(3, player.getName());
            preparedUpdateStatement.setString(4, String.valueOf(System.currentTimeMillis()));
            preparedUpdateStatement.setString(5, player.getUniqueId().toString());
            preparedUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public Integer[] getKills(final Player player) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "SELECT * FROM `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(sql);
            preparedUpdateStatement.setString(1, player.getUniqueId().toString());
            result = preparedUpdateStatement.executeQuery();
            if (result.next()) {
                final int meleeKills = result.getInt("melee_kills");
                final int bowKills = result.getInt("bow_kills");
                final Integer[] kills = {meleeKills, bowKills};
                return kills;
            }
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        } finally {
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            result.close();
            preparedUpdateStatement.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public boolean setWoolsPlaced(final Player player, final Integer count) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String updateSql = "UPDATE `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` " + "SET `wool_placed` = ?" + ", `player_name` = ?" + ", `last_seen` = ?" + " WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(updateSql);
            preparedUpdateStatement.setInt(1, count);
            preparedUpdateStatement.setString(2, player.getName());
            preparedUpdateStatement.setString(3, String.valueOf(System.currentTimeMillis()));
            preparedUpdateStatement.setString(4, player.getUniqueId().toString());
            preparedUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public Integer getWoolsPlaced(final Player player) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "SELECT `wool_placed` FROM `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(sql);
            preparedUpdateStatement.setString(1, player.getUniqueId().toString());
            result = preparedUpdateStatement.executeQuery();
            if (result.next()) {
                return result.getInt("wool_placed");
            }
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        } finally {
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            result.close();
            preparedUpdateStatement.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public boolean setBowDistanceKill(final Player player, final Integer count) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String updateSql = "UPDATE `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` " + "SET `bow_distance_kill` = ?" + ", `player_name` = ?" + ", `last_seen` = ?" + " WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(updateSql);
            preparedUpdateStatement.setInt(1, count);
            preparedUpdateStatement.setString(2, player.getName());
            preparedUpdateStatement.setString(3, String.valueOf(System.currentTimeMillis()));
            preparedUpdateStatement.setString(4, player.getUniqueId().toString());
            preparedUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public Integer getBowDistanceKill(final Player player) {
        if (!this.hasAccount(player)) {
            this.createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        try {
            this.conn = this.ctw.getMysqlSetup().getConnection();
            final String sql = "SELECT `bow_distance_kill` FROM `" + this.ctw.getConfigHandler().getString("Database.TableName") + "` WHERE `player_uuid` = ? LIMIT 1";
            preparedUpdateStatement = this.conn.prepareStatement(sql);
            preparedUpdateStatement.setString(1, player.getUniqueId().toString());
            result = preparedUpdateStatement.executeQuery();
            if (result.next()) {
                return result.getInt("bow_distance_kill");
            }
        } catch (SQLException e) {
            CTW.log.warning("Error: " + e.getMessage());
            e.printStackTrace();
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        } finally {
            try {
                result.close();
                preparedUpdateStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            result.close();
            preparedUpdateStatement.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
