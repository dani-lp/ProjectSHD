package com.a02.dbmanager;

import com.a02.entity.*;
import com.a02.game.Settings;

import java.sql.*;
import java.util.HashMap;

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

    /**
     * Devuelve un objeto Enemy.
     * @param id ID del objeto Enemy
     * @return Enemy con ID parámetro
     * @throws DBException Error de SQL
     */
    public Enemy getEnemy(int id) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM enemy WHERE ID_E=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Enemy enemy = new Enemy();
                enemy.setId(rs.getInt("ID_E"));
                enemy.setHp(rs.getInt("HP_E"));
                enemy.setWidth(rs.getInt("WIDTH_E"));
                enemy.setHeight(rs.getInt("HEIGHT_E"));
                enemy.setAttackDamage((int) (rs.getInt("ATTACKDAMAGE_E") * Settings.s.getDiff()));
                enemy.setSpeed(rs.getFloat("SPEED_E"));
                enemy.setGoldValue(rs.getInt("GOLD_VALUE_E"));
                return enemy;
            } else {
                return new Enemy();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo el enemigo con id " + id, e);
        }
    }

    public HashMap<Integer,Enemy> getAllEnemies() throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM enemy WHERE ID_E<7")) {
            ResultSet rs = stmt.executeQuery();
            HashMap<Integer,Enemy> enemies = new HashMap<>();
            while (rs.next()) {
                Enemy enemy = new Enemy();
                enemy.setId(rs.getInt("ID_E"));
                enemy.setHp(rs.getInt("HP_E"));
                enemy.setWidth(rs.getInt("WIDTH_E"));
                enemy.setHeight(rs.getInt("HEIGHT_E"));
                enemy.setAttackDamage((int) (rs.getInt("ATTACKDAMAGE_E") * Settings.s.getDiff()));
                enemy.setSpeed(rs.getFloat("SPEED_E"));
                enemy.setGoldValue(rs.getInt("GOLD_VALUE_E"));
                enemies.put(enemy.getId(),enemy);
            }
            return enemies;
        } catch (SQLException e) {
            throw new DBException("Error obteniendo los enemigos", e);
        }
    }

    public FinalBoss getBoss() throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM enemy WHERE ID_E=7")) {

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FinalBoss boss = new FinalBoss();
                boss.setId(rs.getInt("ID_E"));
                boss.setHp(rs.getInt("HP_E"));
                boss.setWidth(rs.getInt("WIDTH_E"));
                boss.setHeight(rs.getInt("HEIGHT_E"));
                boss.setAttackDamage((int) (rs.getInt("ATTACKDAMAGE_E") * Settings.s.getDiff()));
                boss.setSpeed(rs.getFloat("SPEED_E"));
                boss.setGoldValue(rs.getInt("GOLD_VALUE_E"));
                boss.setX(-32);
                boss.setY(90);
                boss.setStartTime(1100); //TODO
                boss.hpBar.setMaxHP(boss.getHp());
                boss.loadAnimations();
                boss.loadIdleTexture();
                return boss;
            } else {
                return new FinalBoss();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo el jefe con id 7", e);
        }
    }

    /**
     * Devuelve un objeto Trap.
     * @param id ID del objeto Trap
     * @return Trap con ID parámetro
     * @throws DBException Error de SQL
     */
    public Trap getTrap(int id, int round) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trap WHERE ID_T=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Trap trap = new Trap();
                trap.setId(rs.getInt("ID_T"));
                trap.setHp(rs.getInt("HP_T"));
                trap.setAttackDamage(rs.getInt("ATTACKDAMAGE_T"));
                trap.setUnlocked(false);
                trap.setPrice(rs.getInt("PRICE_T"));
                trap.setType(rs.getString("TYPE"));
                trap.setEffect(Trap.Effect.valueOf(rs.getString("EFFECT")));
                trap.setWidth(rs.getInt("WIDTH_T"));
                trap.setHeight(rs.getInt("HEIGHT_T"));
                trap.setUnlocked(false);
                switch (trap.getId()) {
                    case 0:
                    case 1:
                        trap.setUnlocked(true);
                        break;
                    case 2:
                    case 4:
                        if (round > 1 || round < 0) trap.setUnlocked(true);
                        break;
                    case 3:
                        if (round > 3 || round < 0) trap.setUnlocked(true);
                        break;
                }
                return trap;
            } else {
                return new Trap();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }

    /**
     * Devuelve un objeto Defender.
     * @param id ID del objeto Defender
     * @return Defender con ID parámetro
     * @throws DBException Error de SQL
     */
    public Defender getDefender(int id,int round) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM defender WHERE ID_D=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Defender defender = new Defender();
                defender.setId(rs.getInt("ID_D"));
                defender.setHp(rs.getInt("HP_A"));
                defender.setUnlocked(false);
                defender.setPrice(rs.getInt("PRICE_D"));
                defender.setType(rs.getString("TYPE_D"));
                defender.setWidth(rs.getInt("WIDTH_D"));
                defender.setHeight(rs.getInt("HEIGHT_D"));

                switch (defender.getId()) {
                    case 1:
                    case 2:
                        defender.setUnlocked(true);
                        break;
                    case 0:
                    case 3:
                        if (round > 2 || round < 0) defender.setUnlocked(true);
                        break;

                }
                return defender;
            } else {
                return new Defender();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }

    /**
     * Devuelve un objeto Attacker.
     * @param id ID del objeto Attacker
     * @return Attacker con ID parámetro
     * @throws DBException Error de SQL
     */
    public Attacker getAttacker(int id, int round) throws DBException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM attacker WHERE ID_A=?")) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Attacker attacker = new Attacker();
                attacker.setId(rs.getInt("ID_A"));
                attacker.setHp(rs.getInt("HP_A"));
                attacker.setAttackDamage(rs.getInt("ATTACKDAMAGE_A"));
                attacker.setUnlocked(false);
                attacker.setPrice(rs.getInt("PRICE_A"));
                attacker.setType(rs.getString("TYPE_A"));
                attacker.setAttackType(rs.getString("ATTACK_TYPE"));
                attacker.setWidth(rs.getInt("WIDTH_A"));
                attacker.setHeight(rs.getInt("HEIGHT_A"));
                switch (attacker.getId()) {
                    case 0:
                        attacker.setUnlocked(true);
                        break;
                    case 1:
                        if (round > 2 || round < 0) attacker.setUnlocked(true);
                        break;
                    case 2:
                        if (round > 1 || round < 0) attacker.setUnlocked(true);
                        break;
                    case 3:
                        if (round > 3 || round < 0) attacker.setUnlocked(true);
                        break;

                }
                return attacker;
            } else {
                return new Attacker();
            }
        } catch (SQLException e) {
            throw new DBException("Error obteniendo la trampa con id " + id, e);
        }
    }
}
