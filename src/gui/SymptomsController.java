package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entites.Pacientes;
import model.entites.Symptons;
import model.entites.Usuario;
import model.service.SymptonsService;

public class SymptomsController implements Initializable, DataChangeListener{
	
	private SymptonsService service;
	
    private Usuario user;
	
	public void setUser(Usuario user) {
		this.user = user;
	}
	
	@FXML
	private TableView<Symptons> tvSymptoms;
	
	@FXML
	private TableColumn<Symptons, String> tcTranstorno;
	
	@FXML
	private TableColumn<Symptons, String> tcCid;
	
	@FXML
	private TableColumn<Symptons, String> tcSinBio;
	
	@FXML
	private TableColumn<Symptons, String> tcSinSoc;
	
	@FXML
	private TableColumn<Symptons, String> tcCarac;
	
	@FXML
	private TextField txPesquisa;
	
	private ObservableList<Symptons> obsList;
	
	@FXML
	private Button btPacientes;
	
	@FXML
	private Button btUsuarios;
	
	@FXML
	private Button btPesquisa;
	
	@FXML
	private Button btAjuda;
	
	public void onBtPesquisa() {
		String nome = txPesquisa.getText();
		if(nome == null || nome.trim()=="") {
			updateTableView();
		}else {
			List<Symptons> list = service.findByName(nome);
			obsList = FXCollections.observableArrayList(list);
			tvSymptoms.setItems(obsList);
		}
	}
	
	@FXML
	public void onBtActionAjuda() {
		Alerts.showAlert("Sobre", "uma pagina para esplicar o que os botoes fazem", 
				"Pacientes redireciona para a pagina de Pacientes, "
				+ "Ajuda ver Sobre", AlertType.INFORMATION);
	}
	
	public void onBtPacientes(ActionEvent event) {
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
	
	public void onBtUsuario(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Usuarios.fxml"));
			ScrollPane scrollpane = loader.load();
			
			scrollpane.setFitToHeight(true);
			scrollpane.setFitToWidth(true);
			
			Stage parentStage = Utils.currentStage(event);
			Scene scene = Main.getMainScene();
			scene = new Scene(scrollpane);
			parentStage.setScene(scene);
			parentStage.setTitle("Usuarios");
			parentStage.show();
			
			UsuarioController controller = loader.getController();
			controller.setUser(user);
			controller.updateTableView();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Symptons> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tvSymptoms.setItems(obsList);
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}	
	
	private void initializeNodes() {
		//TODO
		tcTranstorno.setCellValueFactory(new PropertyValueFactory<>("Transtorno"));
		tcCid.setCellValueFactory(new PropertyValueFactory<>("cid"));
		tcSinBio.setCellValueFactory(new PropertyValueFactory<>("Sin Biologicos"));
		tcSinSoc.setCellValueFactory(new PropertyValueFactory<>("Sin Social"));
		tcCarac.setCellValueFactory(new PropertyValueFactory<>("Caracteristicas"));
		
	}
	
}
	
	
