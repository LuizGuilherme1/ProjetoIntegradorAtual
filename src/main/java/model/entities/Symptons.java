package model.entities;

public class Symptons {
	private String Transtorno;
	private String Cid;
	private String Sintomas_bio;
	private String Sintomas_soc;
	private String Caracte;
	
	public Symptons() {
	}

	public Symptons(String Transtorno, String Cid, String Sintomas_bio, String Sintomas_soc, String Caracte) {
		super();
		this.Transtorno = Transtorno;
		this.Cid = Cid;
		this.Sintomas_bio = Sintomas_bio;
		this.Sintomas_soc = Sintomas_soc;
		this.Caracte = Caracte;
	}

	public String getTranstorno() {
		return Transtorno;
	}

	public void setTranstorno(String transtorno) {
		Transtorno = transtorno;
	}

	public String getCid() {
		return Cid;
	}

	public void setCid(String cid) {
		Cid = cid;
	}

	public String getSintomas_bio() {
		return Sintomas_bio;
	}

	public void setSintomas_bio(String sintomas_bio) {
		Sintomas_bio = sintomas_bio;
	}

	public String getSintomas_soc() {
		return Sintomas_soc;
	}

	public void setSintomas_soc(String sintomas_soc) {
		Sintomas_soc = sintomas_soc;
	}

	public String getCaracte() {
		return Caracte;
	}

	public void setCaracte(String caracte) {
		Caracte = caracte;
	}
	
	
}
