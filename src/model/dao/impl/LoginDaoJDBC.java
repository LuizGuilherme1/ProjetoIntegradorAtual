package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.LoginDao;
import model.entites.Pacientes;
import model.entites.Usuario;

public class LoginDaoJDBC implements LoginDao{
	private Connection conn;
	
	public LoginDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean velidate(Usuario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM usuario "
					+ "WHERE email = ? AND senha = ? ");
			st.setString(1, obj.getEmail());
			st.setString(2, obj.getSenha());
			
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		return false;
	}
	
	@Override
	public void insert(Usuario p){
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO usuario "
					+ "(nome,email,senha,acess) "
					+ "VALUES "
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, p.getName());
			st.setString(2, p.getEmail());
			st.setString(3, p.getSenha());
			st.setString(4, "True");
			
            int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					p.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		 }
	}

	@Override
	public void edit(Usuario p) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"update usuario "
					+ "set nome = ?, email = ?, senha = ?, "
					+ "acess = ? "
					+ "where id = ?");
			st.setString(1, p.getName());
			st.setString(2, p.getEmail());
			st.setString(3, p.getSenha());
			st.setString(4, "True");
			st.setInt(5, p.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(int id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM usuario WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Usuario findById(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM usuario "
					+ "WHERE id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Usuario obj = instantiateUsuarios(rs);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Usuario instantiateUsuarios(ResultSet rs) throws SQLException {
		Usuario obj = new Usuario();
		obj.setId(rs.getInt("id"));
		obj.setName(rs.getString("nome"));
		obj.setEmail(rs.getString("email"));
		obj.setSenha(rs.getString("senha"));
		obj.setKey("True");
		return obj;
	}

	@Override
	public List<Usuario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM usuario "
					+ "ORDER BY nome");
			
			rs = st.executeQuery();
			
			List<Usuario> list = new ArrayList<>();
			
			while (rs.next()) {
				Usuario obj = instantiateUsuarios(rs);
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

	@Override
	public Usuario findByLogin(Usuario user) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM usuario "
					+ "WHERE email = ?");
			
			st.setString(1, user.getEmail());
			rs = st.executeQuery();
			if (rs.next()) {
				Usuario obj = instantiateUsuarios(rs);
				return obj;
			}
			return null;
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
