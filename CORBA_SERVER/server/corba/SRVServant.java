package server.corba;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import server.Server;
import server.db.DBHelper;
import server.model.ResultVoteImpl;
import MAV.Candidat;
import MAV.ResultVote;
import MAV.VTR;
import MAV._SRVImplBase;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.IncorrectBVPersonException;
import MAV.SRVPackage.InternalErrorException;
import MAV.SRVPackage.ElectionFinishedException;

/**
 * 
 * @author breton
 * 
 */
public class SRVServant extends _SRVImplBase {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 6704221871768262118L;

	/**
	 * liste de tous les vtr qui sont en train de consulter en temps r�el les
	 * donn�es sur ce bureau de vote
	 */
	private List vtrList;
	
	/**
	 * represente la date de fin de l'election
	 */
	private Date endDateElection;

	/**
	 * Construit un SRV.
	 *
	 */
	public SRVServant(Date endDateElection) {
		this.vtrList = new ArrayList();
		this.endDateElection = endDateElection;		
	}

	public ResultVote[] listeResultat() throws InternalErrorException {
		try {
			ResultSet result = DBHelper.getInstance().listeResultat();
			List resultList = new ArrayList();
			ResultVoteImpl resultVote;
			while (result.next()) {
				resultVote = new ResultVoteImpl();

				resultVote.setCanton(result.getString(12));
				resultVote.setCirconscription(result.getString(10));
				resultVote.setDepartement(result.getString(8));
				resultVote.setIdBV(result.getInt(1));
				resultVote.setIdCandidat(result.getInt(2));
				resultVote.setIdCanton(result.getInt(11));
				resultVote.setIdCirc(result.getInt(9));
				resultVote.setIdDept(result.getInt(7));
				resultVote.setNbVote(result.getInt(13));
				resultVote.setNom(result.getString(3));
				resultVote.setNomBV(result.getString(6));
				resultVote.setPrenom(result.getString(4));
				resultList.add(resultVote);

			}

			return (ResultVote[]) resultList.toArray(new ResultVote[0]);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException(
					"Une erreur interne c'est produite");
		}
	}

	public Candidat[] listeCandidat() throws InternalErrorException {
		try {
			// on recupere la liste des candidats
			List candidats = DBHelper.getInstance().listeCandidat();

			return (Candidat[]) candidats.toArray(new Candidat[0]);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException(
					"Une erreur interne c'est produite");
		}
	}

	public boolean authMAV(int idMAV, int idBV) throws InternalErrorException, ElectionFinishedException {

		if (isFinishedElection())
		{
			throw new ElectionFinishedException("Election termin�, authentification impossible.");
		}
		
		try {
			return DBHelper.getInstance().authMAV(idMAV, idBV);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException(
					"Une erreur interne c'est produite");
		}
	}

	public void authPersonne(int idPersonne, String password, int idBV)
			throws InternalErrorException, BadAuthentificationException,
			AlreadyVoteException, IncorrectBVPersonException, ElectionFinishedException {
		System.out.println("appel de SRVServant.authPersonne");
		
		if (isFinishedElection())
		{
			throw new ElectionFinishedException("Election termin�, vous ne pouvez plus vous authentifier");
		}
		
		try {
			DBHelper.getInstance().authPersonne(idPersonne, password, idBV);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException(
					"Une erreur interne c'est produite");
		}

	}

	public boolean vote(int idPersonne, String password, int idCandidat,
			int idBV) throws InternalErrorException,
			BadAuthentificationException, AlreadyVoteException,
			IncorrectBVPersonException, ElectionFinishedException {
		System.out.println("appel de SRVServant.vote");
		
		if (isFinishedElection())
		{
			throw new ElectionFinishedException("Election termin�, vous ne pouvez plus voter");
		}
		
		try {
			boolean result = DBHelper.getInstance().vote(idPersonne, password,
					idCandidat, idBV);
			
			// si le vote a r�ussit
			if (result)
			{
				// on notifie tous les VTR qu'il y a eu des changement
				for (Iterator it = this.vtrList.iterator() ; it.hasNext() ;)
				{
					VTR vtr = (VTR) it.next();
					
					vtr.notifieVTR(idCandidat, idBV);
				}
			}
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException(
					"Une erreur interne c'est produite");
		}
	}

	/**
	 * Enregistre un vtr. Un vtr enregistr� sera notifi� lorsqu'il
	 */
	public void enregistrerVTR(VTR vtr) {
		this.vtrList.add(vtr);
	}

	/**
	 * Desenregistre un vtr afin qu'il ne soit plus notifi� des changements.
	 */
	public void desenristrerVTR(VTR vtr) {
		this.vtrList.remove(vtr);
	}
	
	/**
	 * Retourne true si l'�lection est termin� sinon false.
	 * 
	 * @return Retourne true si l'�lection est termin� sinon false. 
	 */
	private boolean isFinishedElection()
	{
		// on recupere la date courante
		Date currentDate = new Date(System.currentTimeMillis());
		
		return this.endDateElection.compareTo(currentDate) <= 0;
		
	}
}
