package application;
	
import java.io.IOException;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import db.DB;
import db.ScriptRunner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	//sysmptons, pesquisa de usuario, mudar db para h2 mudar para .bat
	
	private static Scene mainScene;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			initializeDatabase();
			
			Parent loader = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
			Scene mainScene = new Scene(loader);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Login");
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeDatabase() {
        try {
            Connection conn = DB.getConnection();

            // Testa se já existe tabela "usuario"
            boolean exists = tableExists(conn, "USUARIO");

            if (!exists) {
                System.out.println("Tabelas não encontradas, criando automaticamente...");
                ScriptRunner runner = new ScriptRunner(conn);

                String scriptPath = new File("src/main/resources/schema.sql").getAbsolutePath();
                runner.runScript(scriptPath);

                System.out.println("Tabelas criadas com sucesso!");
            } else {
                System.out.println("Tabelas já existem. Nenhuma ação necessária.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        var meta = conn.getMetaData();
        try (var rs = meta.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
