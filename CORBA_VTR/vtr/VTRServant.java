package vtr;

import javax.swing.table.AbstractTableModel;

import MAV.ResultVote;
import MAV.SRV;
import MAV._VTRImplBase;
import MAV.SRVPackage.InternalErrorException;

public class VTRServant extends _VTRImplBase {

	/**
	 * le serveur sur lequel est connecté le VTR TODO faudra le supprimer des
	 * qu'on passera a la version 2 du observer / observable
	 */
	private SRV srv;

	private ResultVoteTableModel tableModel;

	public void notifieVTR() {
		System.out.println(" *** notifieVTR ");
		try {
			this.tableModel.setResultVote(srv.listeResultat());
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setResultVote(ResultVote[] resultVote) {
		this.tableModel = new ResultVoteTableModel(resultVote);
	}

	public SRV getSrv() {
		return srv;
	}

	public void setSrv(SRV srv) {
		this.srv = srv;
	}

	public ResultVoteTableModel getResultVoteTableModel() {
		return tableModel;
	}

	private class ResultVoteTableModel extends AbstractTableModel {

		/**
		 * les resultats qu'a collecté le BV sur lequel est connecté le VTR
		 */
		private ResultVote[] resultVote;

		public ResultVoteTableModel(ResultVote[] resultVote) {
			this.resultVote = resultVote;
		}

		public int getColumnCount() {
			return 12;
		}

		public int getRowCount() {
			return resultVote.length;
		}

		public Object getValueAt(int l, int c) {

			ResultVote ligne = this.resultVote[l];

			switch (c) {
			case 0:
				return ligne.idBV();
			case 1:
				return ligne.nomBV();
			case 2:
				return ligne.idCanton();
			case 3:
				return ligne.canton();
			case 4:
				return ligne.idCirc();
			case 5:
				return ligne.circonscription();
			case 6:
				return ligne.idDept();
			case 7:
				return ligne.departement();
			case 8:
				return ligne.idCandidat();
			case 9:
				return ligne.nom();
			case 10:
				return ligne.prenom();
			case 11:
				return ligne.nbVote();

			default:
				return null;

			}

			// TODO A SUPPRIMER
			//			 
			// readonly attribute long idBV;
			// readonly attribute string nomBV;
			// readonly attribute long idCanton;
			// readonly attribute string canton;
			// readonly attribute long idCirc;
			// readonly attribute string circonscription;
			// readonly attribute long idDept;
			// readonly attribute string departement;
			//			
			//			
			// readonly attribute long idCandidat;
			// readonly attribute string nom;
			// readonly attribute string prenom;
			// readonly attribute long nbVote;

		}

		public String getColumnName(int c) {

			switch (c) {
			case 0:
				return "BV ID";
			case 1:
				return "BV NOM";
			case 2:
				return "CANTON ID";
			case 3:
				return "CANTON";
			case 4:
				return "CIRC NOM";
			case 5:
				return "CIRCONSCRIPTION";
			case 6:
				return "DEPARTEMENT ID";
			case 7:
				return "DEPARTEMENT";
			case 8:
				return "CANDIDAT ID";
			case 9:
				return "CANDIDAT NOM";
			case 10:
				return "CANDIDAT PRENOM";
			case 11:
				return "NB VOTES";

			default:
				return null;

			}

		}

		public ResultVote[] getResultVote() {
			return resultVote;
		}

		public void setResultVote(ResultVote[] resultVote) {
			this.resultVote = resultVote;
			
			// on notifie que les données de la table ont changé
			this.fireTableDataChanged();
		}
	}

}
