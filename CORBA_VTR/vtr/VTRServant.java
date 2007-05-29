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
			return 7;
		}

		public int getRowCount() {
			return resultVote.length;
		}

		public Object getValueAt(int l, int c) {

			ResultVote ligne = this.resultVote[l];

			switch (c) {
			case 0:
				return ligne.departement();
			case 1:
				return ligne.circonscription();
			case 2:
				return ligne.canton();
			case 3:
				return ligne.nomBV();
			case 4:
				return ligne.nom();
			case 5:
				return ligne.prenom();
			case 6:
				return new Integer(ligne.nbVote());

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
				return "DEPARTEMENT";
			case 1:
				return "CIRCONSCRIPTION";
			case 2:
				return "CANTON";
			case 3:
				return "BV NOM";
			case 4:
				return "CANDIDAT NOM";
			case 5:
				return "CANDIDAT PRENOM";
			case 6:
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
