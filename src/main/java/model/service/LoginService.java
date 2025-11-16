package model.service;

import model.dao.LoginDao;

import java.util.List;

import model.dao.DaoFactory;
import model.entities.Pacientes;
import model.entities.Usuario;

public class LoginService {
	private LoginDao dao = DaoFactory.createLoginDao();
	
	public boolean validate(Usuario cadastro) {
		return dao.velidate(cadastro);
	}
	
	public List<Usuario> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Usuario obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.edit(obj);
		}
	}
	
	public void remove(Usuario obj) {
		dao.deleteById(obj.getId());
	}
	
	public Usuario getUser(Usuario obj) {
		return dao.findByLogin(obj);
	}
	
	public List<Usuario> findByName(String name){
		return dao.findByName(name);
	}

}
