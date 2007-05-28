package server.model;

import MAV._CandidatImplBase;

public class CandidatImpl extends _CandidatImplBase {

	/**
	 * id
	 */
	private int id;

	/**
	 * nom du candidat
	 */
	private String nom;

	/**
	 * prenom du candidat
	 */
	private String prenom;

	/**
	 * description du candidat
	 */
	private String description;

	public int id() {
		return id;
	}

	public String nom() {
		return this.nom;
	}

	public String prenom() {
		return this.prenom;
	}

	public String description() {
		return this.description;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
