package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SymptonsDao;
import model.entities.Pacientes;
import model.entities.Symptons;

public class SymptonsDaoJDBC implements SymptonsDao{
    private Connection conn;
	
	public SymptonsDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Symptons> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM symptons "
					+ "ORDER BY id");
			
			rs = st.executeQuery();
			
			List<Symptons> list = new ArrayList<>();
			
			while (rs.next()) {
				Symptons obj = instantiateSymptons(rs);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
//TODO
	private Symptons instantiateSymptons(ResultSet rs) throws SQLException{
		Symptons obj = new Symptons();
		obj.setTranstorno(rs.getString("transtorno"));
		obj.setCid(rs.getString("cid"));
		obj.setSintomas_bio(rs.getString("sintomas_biologicos"));
		obj.setSintomas_soc(rs.getString("consequencias_sociais"));
		obj.setCaracte(rs.getString("caracteristicas"));
		return obj;
	}

	@Override
	public List<Symptons> findByName(String name) {
		// TODO Auto-generated method stub
		name = name+"%";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM symptons "
					+ "WHERE name like ?"
					+ "ORDER BY transtorno");
			
			st.setString(1, name);
			rs = st.executeQuery();
			
			List<Symptons> list = new ArrayList<>();
			
			while (rs.next()) {
				Symptons obj = instantiateSymptons(rs);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
