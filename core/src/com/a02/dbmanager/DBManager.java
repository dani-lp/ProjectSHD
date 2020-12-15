package com.a02.dbmanager;

import com.a02.entity.Attacker;
import com.a02.entity.Defender;
import com.a02.entity.Enemy;
import com.a02.entity.Trap;

import java.sql.*;

public class DBManager {

    public static final DBManager dbManager = new DBManager();
    private Connection conn = null;

    private DBManager() {

    }

    /**
     * Crea una conexión con la BD.
     * @param dbPath Ruta de la BD
     * @throws DBException Error de conexión con la BD o el driver.
     */
    public void connect(String dbPath) throws DBException {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (ClassNotFoundException e) {
            throw new DBException("Error de carga del driver de la BD", e);
        } catch (SQLException e) {
            throw new DBException("Error de conexión a la BD", e);
        }
    }

    /**
     * Desconecta la BD.
     * @throws DBException Error de desconexión con la BD
     */
    public void disconnect() throws DBException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DBException("Error cerrando la conexión con la BD", e);
        }
    }

    public Enemy getEnemy(int id) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM enemy WHERE ID_E=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Enemy enemy = new Enemy();
                enemy.setId(rs.getInt("ID_E"));
                enemy.setHp(rs.getInt("HP_E"));
                enemy.setAttackDamage(rs.getInt("ATTACKDAMAGE_E"));
                enemy.setSpeed(rs.getFloat("SPEED_E"));
                enemy.setGoldValue(rs.getInt("GOLD_VALUE_E"));
                enemy.setWakpath(rs.getString("WALKPATH"));
                enemy.setAttackpath(rs.getString("ATTACKPATH"));
                enemy.setDeathpath(rs.getString("DEATHPATH"));
                return enemy;
            } else {
                return new Enemy();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo el enemigo con id " + id, e);
        }
    }

    public Trap getTrap(int id) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trap WHERE ID_T=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Trap trap = new Trap();
                trap.setId(rs.getInt("ID_T"));
                trap.setHp(rs.getInt("HP_T"));
                trap.setAttackDamage(rs.getInt("ATTACKDAMAGE_T"));
                int bool=rs.getInt("UNLOCKED_T");
                if (bool==1){
                    trap.setUnlocked(true);
                }
                trap.setPrice(rs.getInt("PRICE_T"));
                trap.setType(rs.getString("TYPE"));
                trap.setEffect(Trap.Effect.valueOf(rs.getString("EFFECT")));
                trap.setWidth(rs.getInt("WIDTH_T"));
                trap.setHeight(rs.getInt("HEIGHT_T"));
                return trap;
            } else {
                return new Trap();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }

    public Defender getDefender(int id) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM defender WHERE ID_D=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Defender defender = new Defender();
                defender.setId(rs.getInt("ID_D"));
                defender.setHp(rs.getInt("HP_A"));
                int bool = rs.getInt("UNLOCKED_D");
                if (bool == 1) defender.setUnlocked(true);
                defender.setPrice(rs.getInt("PRICE_D"));
                defender.setType(rs.getString("TYPE_D"));
                defender.setWidth(rs.getInt("WIDTH_D"));
                defender.setHeight(rs.getInt("HEIGHT_D"));
                return defender;
            } else {
                return new Defender();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }

    public Attacker getAttacker(int id) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM attacker WHERE ID_A=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Attacker attacker = new Attacker();
                attacker.setId(rs.getInt("ID_A"));
                attacker.setHp(rs.getInt("HP_A"));
                attacker.setAttackDamage(rs.getInt("ATTACKDAMAGE_A"));
                int bool=rs.getInt("UNLOCKED_A");
                if (bool==1){
                    attacker.setUnlocked(true);
                }
                attacker.setPrice(rs.getInt("PRICE_A"));
                attacker.setType(rs.getString("TYPE_A"));
                attacker.setAttackType(rs.getString("ATTACK_TYPE"));
                attacker.setWidth(rs.getInt("WIDTH_A"));
                attacker.setHeight(rs.getInt("HEIGHT_A"));
                return attacker;
            } else {
                return new Attacker();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }
}
