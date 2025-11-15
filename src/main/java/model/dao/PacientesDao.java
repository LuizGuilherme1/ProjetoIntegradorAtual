package model.dao;

import java.util.List;

import model.entities.Pacientes;
import model.entities.Usuario;

public interface PacientesDao {
	
	void insert(Pacientes p);
	void edit(Pacientes p);
	void deleteById(int id);
	Pacientes findById(int id);
	List<Pacientes> findAll(Usuario user);
	List<Pacientes> findByName(Usuario user, String name);
	
}
