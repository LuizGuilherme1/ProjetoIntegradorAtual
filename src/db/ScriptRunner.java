package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;


public class ScriptRunner {

    private Connection conn;

    public ScriptRunner(Connection conn) {
        this.conn = conn;
    }

    public void runScript(String filePath) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue;
                }
                sb.append(line);

                if (line.trim().endsWith(";")) {
                    String sql = sb.toString();
                    sql = sql.substring(0, sql.length() - 1); // remove ;
                    try (Statement st = conn.createStatement()) {
                        st.execute(sql);
                    }
                    sb.setLength(0);
                }
            }
        }
    }
}
