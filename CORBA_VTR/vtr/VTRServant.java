package vtr;

import javax.swing.table.AbstractTableModel;

import MAV.ResultVote;
import MAV.SRV;
import MAV._VTRImplBase;

public class VTRServant extends _VTRImplBase {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -6193959437095814709L;

	/**
	 * le serveur sur lequel est connect� le VTR TODO faudra le supprimer des
	 * qu'on passera a la version 2 du observer / observable
	 */
	private SRV srv;

	private ResultVoteTableModel tableModel;

	/**
	 * fonction qui permet d'indiquer au VTR qu'il y a eu un vote suppl�mentaire
	 * dans le bureau de vote pass� en parametre pour le candidat pass� en
	 * param�tre.
	 */
	public void notifieVTR(int idCandidat, int idBV) {
		System.out.println(" *** notifieVTR ");

		// on recherche la ligne de r�sultat qui correspond au bureau de vote et
		// au candidat pass� en param�tre
		boolean finded = false;
		int i = 0;

		// tant qu'on a pas trouv� et qu'il reste des lignes de resultat a
		// explorer la ligne courante
		ResultVote aLine = null;
		while (!finded && i < this.tableModel.getRowCount()) {
			aLine = this.tableModel.getLine(i);

			// si l'identifiant du candidat ainsi que l'identifiant du bv
			// correspondent a ceux pass� en param�tre
			if (aLine.idCandidat() == idCandidat && aLine.idBV() == idBV) {
				// on a trouv� la ligne qui vient de changer
				finded = true;
			} else {
				// on passe a la ligne suivante
				i++;
			}
		}

		// si on a trouv� la ligne qui vient de changer
		if (finded) {
			// on augmente de 1 le nombre de vote pour ce candidat dans ce
			// bureau de vote
			aLine.nbVote(aLine.nbVote() + 1);
			
			// on indique que la cellule a chang�
			// la colonne 6 correspond au nb de vote
			this.tableModel.fireTableCellUpdated(i, 6);
		} else {
			// ne devrait jamais arriv�
			System.err
					.println("VTRServant.notifieVTR() : impossible de trouver la ligne qui a chang� (ceci ne devrait jamais arriver)");
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
		 * serial version id
		 */
		private static final long serialVersionUID = 1617825674845171851L;
		/**
		 * les resultats qu'a collect� le BV sur lequel est connect� le VTR
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

			// on notifie que les donn�es de la table ont chang�
			this.fireTableDataChanged();
		}

		/**
		 * Fonction qui permet de r�cuperer la ligne i du table model
		 * 
		 * @param i
		 *            est le num�ro de la ligne que l'on souhaite r�cuperer
		 * 
		 * Remarque : cette m�thode a �t� cr�� pour am�liorer la lisibilit� de
		 * la m�thode qui notifie un vtr
		 */
		public ResultVote getLine(int i) {
			return this.resultVote[i];
		}
	}

}
