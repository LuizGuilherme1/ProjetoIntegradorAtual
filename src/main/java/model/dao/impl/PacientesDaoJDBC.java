package model.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import db.DB;
import db.DbException;
import model.dao.PacientesDao;
import model.entities.Pacientes;
import model.entities.Usuario;

public class PacientesDaoJDBC implements PacientesDao{
	
    private Connection conn;
	
	public PacientesDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Pacientes p) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO pacientes "
					+ "(paciente_name, idade, data_nascimento, sexo, cns, cpf, rg, cep, endereco, complemento, user_id) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, p.getName());
			st.setInt(2, p.getIdade());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String birthStr = sdf.format(p.getBirthdate());
			st.setString(3, birthStr);
			st.setString(4, p.getGender());
			st.setString(5, p.getCns());
			st.setString(6, p.getCpf());
			st.setString(7, p.getRg());
			st.setString(8, p.getCep());
			st.setString(9, p.getEndereco());
			st.setString(10, p.getComplemento());
			st.setInt(11, p.getUser_id());
			
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
	public void edit(Pacientes p) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"update pacientes "
					+ "set paciente_name = ?, idade = ?, data_nascimento = ?, "
					+ "sexo = ?, cns = ?, cpf = ?, rg = ?, cep = ?, endereco = ?, complemento = ? "
					+ "where paciente_id = ?");
			st.setString(1, p.getName());
			st.setInt(2, p.getIdade());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String birthStr = sdf.format(p.getBirthdate());
			st.setString(3, birthStr);
			st.setString(4, p.getGender());
			st.setString(5, p.getCns());
			st.setString(6, p.getCpf());
			st.setString(7, p.getRg());
			st.setString(8, p.getCep());
			st.setString(9, p.getEndereco());
			st.setString(10, p.getComplemento());
			st.setInt(11, p.getId());
			
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
			st = conn.prepareStatement("DELETE FROM pacientes WHERE paciente_id = ?");
			
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
	public Pacientes findById(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM pacientes "
					+ "WHERE paciente_id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Pacientes obj = instantiatePacientes(rs);
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
	
	private Pacientes instantiatePacientes(ResultSet rs) {
		Pacientes obj = new Pacientes();
		try {
		obj.setId(rs.getInt("paciente_id"));
		obj.setName(rs.getString("paciente_name"));
		obj.setIdade(rs.getInt("idade"));
		
		String dateStr = rs.getString("data_nascimento");

		Date utilDate;

		if (dateStr == null || dateStr.isEmpty()) {
		    utilDate = null;  // TODO throw exception
		} 
		else if (dateStr.matches("\\d+")) { 
		    // EPOCH MILLISECONDS
		    long millis = Long.parseLong(dateStr);
		    utilDate = new Date(millis);
		} 
		else {
		    // STRING DATE
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    utilDate = sdf.parse(dateStr);
		}

		obj.setBirthdate(utilDate);
		
		obj.setGender(rs.getString("sexo"));
		obj.setCns(rs.getString("cns"));
		obj.setCpf(rs.getString("cpf"));
		obj.setRg(rs.getString("rg"));
		obj.setCep(rs.getString("cep"));
		obj.setEndereco(rs.getString("endereco"));
		obj.setComplemento(rs.getString("complemento"));
		obj.setUser_id(rs.getInt("user_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Override
	public List<Pacientes> findAll(Usuario user) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM pacientes "
					+ "WHERE user_id = ? "
					+ "ORDER BY paciente_name");
			
			st.setInt(1, user.getId());
			rs = st.executeQuery();
			
			List<Pacientes> list = new ArrayList<>();
			
			while (rs.next()) {
				Pacientes obj = instantiatePacientes(rs);
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
	public List<Pacientes> findByName(Usuario user ,String name) {
		name = name+"%";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM pacientes "
					+ "WHERE user_id = ? and paciente_name like ?"
					+ "ORDER BY paciente_name");
			
			st.setInt(1, user.getId());
			st.setString(2, name);
			rs = st.executeQuery();
			
			List<Pacientes> list = new ArrayList<>();
			
			while (rs.next()) {
				Pacientes obj = instantiatePacientes(rs);
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
