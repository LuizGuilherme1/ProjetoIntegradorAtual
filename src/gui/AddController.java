package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entites.Pacientes;
import model.entites.Usuario;
import model.exceptions.ValidationException;
import model.service.PacienteService;

public class AddController implements Initializable{
	private Pacientes entity;
	
	private Usuario user;
	
	private PacienteService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txId;
	
	@FXML
	private TextField txName;
	
	@FXML
	private TextField txIdade;
	
	@FXML
	private DatePicker dpBirthdate;
	
	@FXML
	private TextField txSex;
	
	@FXML
	private TextField txCns;
	
	@FXML
	private TextField txCpf;
	
	@FXML
	private TextField txRg;
	
	@FXML
	private TextField txCep;
	
	@FXML
	private TextField txEndereco;
	
	@FXML
	private TextField txComplemento;
	
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	public void getUser(Usuario user) {
		this.user = user;
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onActionSave(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		} catch (DbException e) {
			Alerts.showAlert("Error Saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void setPaciente(Pacientes entity) {
		this.entity = entity;
	}
	
	public void setService(PacienteService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txId.setText(String.valueOf(entity.getId()));
		txName.setText(entity.getName());
		txIdade.setText(String.valueOf(entity.getIdade()));
		txCns.setText(entity.getCns());
		txCpf.setText(entity.getCpf());
		txRg.setText(entity.getRg());
		txCep.setText(entity.getCep());
		txEndereco.setText(entity.getEndereco());
		txComplemento.setText(entity.getComplemento());
		Locale.setDefault(Locale.US);
		if (entity.getBirthdate() != null) {
			dpBirthdate.setValue(LocalDate.ofInstant(entity.getBirthdate().toInstant(), ZoneId.systemDefault()));
		}
		txSex.setText(entity.getGender());
	}

	private Pacientes getFormData() {
		Pacientes obj = new Pacientes();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txId.getText()));
		
		if (txName.getText() == null || txName.getText().trim().equals("")) {
			exception.addError("nome", "Field can't be empty");
		}
		obj.setName(txName.getText());
		
		if (txCep.getText() == null || txCep.getText().trim().equals("")) {
			exception.addError("Cep", "Field can't be empty");
		}
		obj.setCep(txCep.getText());
		
		if (txCns.getText() == null || txCns.getText().trim().equals("")) {
			exception.addError("Cns", "Field can't be empty");
		}
		obj.setCns(txCns.getText());
		
		obj.setComplemento(txComplemento.getText());
		
		if (txCpf.getText() == null || txCpf.getText().trim().equals("")) {
			exception.addError("Cpf", "Field can't be empty");
		}
		obj.setCpf(txCpf.getText());
		
		if (txEndereco.getText() == null || txEndereco.getText().trim().equals("")) {
			exception.addError("Endereco", "Field can't be empty");
		}
		obj.setEndereco(txEndereco.getText());
		
		if (txRg.getText() == null || txRg.getText().trim().equals("")) {
			exception.addError("Rg", "Field can't be empty");
		}
		obj.setRg(txRg.getText());
		
		if (txSex.getText() == null || txSex.getText().trim().equals("")) {
			exception.addError("Sex", "Field can't be empty");
		}
		obj.setGender(txSex.getText());
		
		if (txIdade.getText() == null) {
			exception.addError("idade", "Field can't be empty");
		}else {
		obj.setIdade(Utils.tryParseToInt(txIdade.getText()));
		}
		
		if (dpBirthdate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
			Instant instant = Instant.from(dpBirthdate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthdate(Date.from(instant));
		}
		
		obj.setUser_id(user.getId());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txId);
		Constraints.setTextFieldInteger(txIdade);
		Utils.formatDatePicker(dpBirthdate, "dd/MM/yyyy");
		
	}
	
}
