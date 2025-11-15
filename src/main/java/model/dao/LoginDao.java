package model.dao;

import java.util.List;

import model.entities.Usuario;

public interface LoginDao {
	boolean velidate(Usuario user);
	void insert(Usuario p);
	void edit(Usuario p);
	void deleteById(int id);
	Usuario findById(int id);
	List<Usuario> findAll();
	Usuario findByLogin(Usuario user);
}
