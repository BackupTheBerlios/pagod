package server.corba;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.db.DBHelper;
import server.model.ResultVoteImpl;
import MAV.Candidat;
import MAV.ResultVote;
import MAV._SRVImplBase;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.InternalErrorException;
import MAV.SRVPackage.IncorrectBVPersonException;

/**
 * 
 * @author breton
 * 
 */
public class SRVServant extends _SRVImplBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6704221871768262118L;

	public ResultVote [] listeResultat() throws InternalErrorException {
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
			resultVote.setNom(result.getString(2));
			resultVote.setNomBV(result.getString(6));
			resultVote.setPrenom(result.getString(2));
			resultList.add(resultVote);
			
		}

		return (ResultVote[]) resultList.toArray(new ResultVote[0]);
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException("Une erreur interne c'est produite");
		}
	}

	public Candidat[] listeCandidat() throws InternalErrorException {
		try {
			// on recupere la liste des candidats
			List candidats = DBHelper.getInstance().listeCandidat();

			return (Candidat[]) candidats.toArray(new Candidat[0]);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException("Une erreur interne c'est produite");
		}
	}

	public boolean authMAV(int idMAV, int idBV) throws InternalErrorException {

		try {
			return DBHelper.getInstance().authMAV(idMAV, idBV);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException("Une erreur interne c'est produite");
		}
	}

	public void authPersonne(int idPersonne, String password, int idBV)
			throws InternalErrorException, BadAuthentificationException,
			AlreadyVoteException, IncorrectBVPersonException {
		System.out.println("appel de SRVServant.authPersonne");
		try {
			DBHelper.getInstance().authPersonne(idPersonne, password,
					idBV);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException("Une erreur interne c'est produite");
		}

	}

	public boolean vote(int idPersonne, String password, int idCandidat, int idBV)
			throws InternalErrorException, BadAuthentificationException,
			AlreadyVoteException, IncorrectBVPersonException {
		System.out.println("appel de SRVServant.vote");
		try {
			return DBHelper.getInstance().vote(idPersonne, password, idCandidat, idBV);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException("Une erreur interne c'est produite");
		}
	}

}
