package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import model.entites.Pacientes;
import model.entites.Usuario;
import model.service.LoginService;
import model.service.PacienteService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.*;

public class LoginController implements Initializable, DataChangeListener {
	private LoginService service = new LoginService();
	
	@FXML
	private Button btCheck;
	
	@FXML
	private Button btCadastro;
	
	@FXML
    private TextField txEmail;
	
	@FXML
    private TextField txPassword;
	
	public void onBtAction(ActionEvent event) {
			try {
				Usuario user = new Usuario();
				user.setEmail(txEmail.getText());
				user.setSenha(txPassword.getText());
				if (user.getEmail().trim() == ""||user.getSenha().trim() == ""||
						(user.getEmail() == null && user.getSenha() == null)) {
		            Alerts.showAlert("Error", null, "Não pode ser nulo", AlertType.ERROR);
		            return;
		        }
				if (service.validate(user)) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Pacientes.fxml"));
					ScrollPane scrollpane = loader.load();
					
					scrollpane.setFitToHeight(true);
					scrollpane.setFitToWidth(true);
					
					Stage parentStage = Utils.currentStage(event);
					Scene scene = Main.getMainScene();
					scene = new Scene(scrollpane);
					parentStage.setScene(scene);
					parentStage.setTitle("pacientes");
					parentStage.show();
					
					PacientesController controller = loader.getController();
					controller.updateTableView();
					
				}else {
				    Alerts.showAlert("Error", null, "Este email não existe ou senha incorreta", AlertType.ERROR);
					
				}

			} catch (IOException e) {
				e.printStackTrace();
		    }
	}
	
	public void btActionCadastro(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Usuario obj = new Usuario();
		createDialogForm(obj, "/gui/AddUsuario.fxml", parentStage);
	}	
	
	private void createDialogForm(Usuario obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AddUsuarioController controller = loader.getController();
			controller.setUsuario(obj);
			controller.setService(new LoginService());
			controller.subscribeDataChangeListener(this);
			//controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
