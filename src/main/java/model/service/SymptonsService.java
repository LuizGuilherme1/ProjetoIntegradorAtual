package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SymptonsDao;
import model.entities.Symptons;

public class SymptonsService {
	private SymptonsDao dao = DaoFactory.createSymptonsDao();
	
	public List<Symptons> findAll(){
		return dao.findAll();
	}
	
	public List<Symptons> findByName(String name){
		return dao.findByName(name);
	}
}
