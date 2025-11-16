package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Pacientes;
import model.entities.Usuario;
import model.service.LoginService;
import model.service.PacienteService;

public class UsuarioController implements Initializable, DataChangeListener{

	private LoginService service = new LoginService();
	
	private Usuario user;
	
	public void setUser(Usuario user) {
		this.user = user;
	}
	
	@FXML
	private TableView<Usuario> tvUsuarios;
	
	@FXML
	private TableColumn<Usuario, String> tcName;
	
	@FXML
	private TableColumn<Usuario, String> tcEmail;
	
	@FXML
	private TableColumn<Usuario, String> tckey;
	
	@FXML
	private TableColumn<Usuario, Usuario> tcEdit;
	
	@FXML
	private TableColumn<Usuario, Usuario> tcDelete;
	
	@FXML
	private Button btSintomas;
	
	@FXML
	private Button btPacientes;
	
	@FXML
	private Button btCadastro;
	
	@FXML
	private Button btPesquisa;
	
	@FXML
	private TextField txPesquisa;
	
	private ObservableList<Usuario> obsList;
	
	@FXML
	public void btActionCadastro(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Usuario obj = new Usuario();
		createDialogForm(obj, "/gui/AddUsuario.fxml", parentStage);
	}
	
	@FXML
	public void btActionSintomas(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Symptoms.fxml"));
			ScrollPane scrollpane = loader.load();
			
			scrollpane.setFitToHeight(true);
			scrollpane.setFitToWidth(true);
			
			Stage parentStage = Utils.currentStage(event);
			Scene scene = Main.getMainScene();
			scene = new Scene(scrollpane);
			parentStage.setScene(scene);
			parentStage.setTitle("simtomas");
			parentStage.show();
			
			SymptomsController controller = loader.getController();
			controller.setUser(user);
			controller.updateTableView();
		}catch (IOException e) {
			e.printStackTrace();
	    }
	}
	
	@FXML
	public void btActionPacientes(ActionEvent event) {
		try {
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
			controller.setUser(user);
			controller.updateTableView();
		}catch (IOException e) {
			e.printStackTrace();
	    }
	}
	
	@FXML
	public void btActionPesquisa() {
		//TODO
		String nome = txPesquisa.getText();
		if(nome == null || nome.trim()=="") {
			updateTableView();
		}else {
			List<Usuario> list = service.findByName(nome);
			obsList = FXCollections.observableArrayList(list);
			tvUsuarios.setItems(obsList);
			initEditButtons();
			initDeleteButtons();
		}
	}
	
	@FXML
	public void btActionAjuda() {
		Alerts.showAlert("Sobre", "uma pagina para esplicar o que os botoes fazem", 
				"Sintomas redireciona para a pagina de sintomas, Cadastro adiciona um novo paciente, "
				+ "pesquisa acha um paciente por id, Ajuda ver Sobre, Edit permit editar os dados de um paciente "
				+ "e Delete deleta um paciente.", AlertType.INFORMATION);
		//updateTableView();
	}
	
	public void setUsuarioServices(LoginService service) {
		this.service = service;
	}
	
	private void createDialogForm(Usuario obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AddUsuarioController controller = loader.getController();
			controller.setUsuario(obj);
			controller.setService(new LoginService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

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
	
	
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Usuario> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tvUsuarios.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}
	
	private void initEditButtons() {
		tcEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tcEdit.setCellFactory(param -> new TableCell<Usuario, Usuario>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Usuario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/AddUsuario.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initDeleteButtons() {
		tcDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tcDelete.setCellFactory(param -> new TableCell<Usuario, Usuario>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Usuario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Usuario obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirme", "Voce quer deletar esse Paciente ?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error Removing Object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	//
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		tcName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		tcEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		tckey.setCellValueFactory(new PropertyValueFactory<>("key"));
		
		//Stage stage = (Stage) Main.getMainScene().getWindow();
		//tvPacientes.prefHeightProperty().bind(stage.heightProperty());
	}

	@Override
	public void onDataChanged() {
		updateTableView();
		
	}

}
