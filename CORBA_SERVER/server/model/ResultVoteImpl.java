package server.model;

import MAV._ResultVoteImplBase;
/**
 * 
 * @author breton
 *
 */
public class ResultVoteImpl extends _ResultVoteImplBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1122562587778046786L;
	private int idBV;
	private String nomBV;
	private int idCanton;
	private String canton;
	private int idCirc;
	private String circonscription;
	private int idDept;
	private String departement;
	
	
	private int idCandidat;
	private String nom;
	private String prenom;
	private int nbVote;
	
	public int idBV() {
		return this.idBV;
	}

	public String nomBV() {
		return this.nomBV;
	}

	public int idCanton() {
		return this.idCanton;
	}

	public String canton() {
		return this.canton;
	}

	public int idCirc() {
		return this.idCirc;
	}

	public String circonscription() {
		return this.circonscription;
	}

	public int idDept() {
		return this.idDept;
	}

	public String departement() {
		return this.departement;
	}

	public int idCandidat() {
		return this.idCandidat;
	}

	public String nom() {
		return this.nom;
	}

	public String prenom() {
		return this.prenom;
	}

	public int nbVote() {
		return this.nbVote;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public void setCirconscription(String circonscription) {
		this.circonscription = circonscription;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public void setIdBV(int idBV) {
		this.idBV = idBV;
	}

	public void setIdCandidat(int idCandidat) {
		this.idCandidat = idCandidat;
	}

	public void setIdCanton(int idCanton) {
		this.idCanton = idCanton;
	}

	public void setIdCirc(int idCirc) {
		this.idCirc = idCirc;
	}

	public void setIdDept(int idDept) {
		this.idDept = idDept;
	}

	public void setNbVote(int nbVote) {
		this.nbVote = nbVote;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setNomBV(String nomBV) {
		this.nomBV = nomBV;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	
}
