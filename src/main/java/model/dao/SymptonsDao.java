package model.dao;

import java.util.List;

import model.entities.Symptons;

public interface SymptonsDao {
	List<Symptons> findAll();
	List<Symptons> findByName(String name);
}
