package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static Connection conn = null;

    private static final String DB_FOLDER = "db";
    private static final String DB_FILE = "app.db";

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String dbPath = DB_FOLDER + "/" + DB_FILE;

                // Cria pasta /db se não existir
                java.io.File folder = new java.io.File(DB_FOLDER);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Conexão SQLite
                String url = "jdbc:sqlite:" + dbPath;
                conn = DriverManager.getConnection(url);

            } catch (SQLException e) {
                throw new DbException("Erro ao conectar ao SQLite: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar Statement: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar ResultSet: " + e.getMessage());
            }
        }
    }
}
