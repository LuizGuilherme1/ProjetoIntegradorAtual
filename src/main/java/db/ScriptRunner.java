package db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class ScriptRunner {

    private final Connection conn;

    public ScriptRunner(Connection conn) {
        this.conn = conn;
    }

    public void runScript(InputStream scriptStream) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(scriptStream))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                sb.append(line).append(" ");

                if (line.endsWith(";")) {
                    String sql = sb.toString().trim();
                    try (Statement st = conn.createStatement()) {
                        st.execute(sql);
                    }
                    sb.setLength(0);
                }
            }
        }
    }
}
