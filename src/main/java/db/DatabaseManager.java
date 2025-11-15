package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManager {

    private static final String TABLE_SCRIPT = "/Table.sql";
    private static final String INSERT_SCRIPT = "/InitialInserts.sql";

    public static void initialize() {
        try {
            Connection conn = DB.getConnection();

            // Verifica cada tabela individualmente
            String[] tables = {"usuario", "pacientes", "symptons"};
            boolean anyTableMissing = false;

            for (String table : tables) {
                if (!tableExists(conn, table)) {
                    anyTableMissing = true;
                    System.out.println("Tabela " + table + " não existe → será criada.");
                }
            }

            if (anyTableMissing) {
                // Cria todas as tabelas
                runScript(conn, TABLE_SCRIPT);
                System.out.println("Tabelas criadas com sucesso!");
            } else {
                System.out.println("Todas as tabelas já existem.");
            }

            // Agora valida os dados iniciais do insert
            if (!initialDataExists(conn)) {
                System.out.println("Inserindo dados iniciais...");
                runScript(conn, INSERT_SCRIPT);
                System.out.println("Dados iniciais inseridos!");
            } else {
                System.out.println("Dados iniciais já existem.");
            }

        } catch (Exception e) {
        	throw new DbException("Erro ao inicializar banco: " + e.getMessage());
        }
    }

    private static boolean tableExists(Connection conn, String table) throws Exception {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND LOWER(name)=LOWER('" + table + "')";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    private static boolean initialDataExists(Connection conn) throws Exception {
        String sql = "SELECT id FROM usuario WHERE email='admin@admin.com'";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    private static void runScript(Connection conn, String scriptPath) throws Exception {
        InputStream is = DatabaseManager.class.getResourceAsStream(scriptPath);
        if (is == null) {
            throw new RuntimeException(scriptPath + " NÃO encontrado no resources!");
        }
        ScriptRunner runner = new ScriptRunner(conn);
        runner.runScript(is);
    }
}
