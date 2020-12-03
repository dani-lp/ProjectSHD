package com.a02.dbmanager;

import java.sql.*;

public class DBManager {

    public final DBManager dbManager = new DBManager();
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

}
