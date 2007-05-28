/**
 * 
 */
package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.model.CandidatImpl;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.IncorrectBVPersonException;
import constants.Constants;

/**
 * @author breton
 * 
 */
public class DBHelper {

	/**
	 * instance de l'objet singleton
	 * 
	 */
	private static DBHelper instance = null;

	/**
	 * la connexion � la bd utilis�e
	 */
	private Connection connection;

	/**
	 * constructeur priv� du singleton
	 * 
	 */
	private DBHelper() {

	}

	/**
	 * cr�e une connection � la bd et si elle n'existe pas cr�e la base
	 * 
	 * @param name
	 *            le nom de la base de donn�es
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createConnection(String name) throws ClassNotFoundException,
			SQLException {

		Class.forName("org.hsqldb.jdbcDriver");
		this.connection = DriverManager.getConnection("jdbc:hsqldb:file:"
				+ Constants.DB_PATH + "BD_" + name, Constants.DB_USER,
				Constants.DB_PASSWORD);

		// on indique que l'on gere les transactions de facon manuelle
		this.connection.setAutoCommit(false);
	}

	/**
	 * M�thode appel�e lors de la destruction de l'objet qui permet de fermer la
	 * connexion
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		Statement st = this.connection.createStatement();
		st.execute("SHUTDOWN");
	}

	/**
	 * cr�er le sch�ma de la base
	 */
	public void createSchema() {

		try {
			Statement st = this.connection.createStatement();

			// create tables
			String createTableCandidate = "CREATE TABLE CANDIDATS(ID INTEGER, NOM VARCHAR(50),PRENOM VARCHAR(50),DESCRIPTIF VARCHAR(500), PRIMARY KEY (ID))";
			st.executeUpdate(createTableCandidate);

			String createTableDepartement = "CREATE TABLE DEPARTEMENTS (ID INTEGER, NOM VARCHAR(50),PRIMARY KEY (ID))";
			st.executeUpdate(createTableDepartement);

			String createTableCirconscription = "CREATE TABLE CIRCONSCRIPTIONS (ID INTEGER, NOM VARCHAR(50),DEPT_ID INTEGER ,PRIMARY KEY (ID), FOREIGN KEY (DEPT_ID) REFERENCES DEPARTEMENTS(ID))";
			st.executeUpdate(createTableCirconscription);

			String createTableCanton = "CREATE TABLE CANTONS (ID INTEGER, NOM VARCHAR(50),CIRC_ID INTEGER , PRIMARY KEY (ID), FOREIGN KEY (CIRC_ID) REFERENCES CIRCONSCRIPTIONS(ID))";
			st.executeUpdate(createTableCanton);

			String createTableBV = "CREATE TABLE BV(ID INTEGER, NOM VARCHAR(50),CANTON_ID INTEGER, PRIMARY KEY (ID),FOREIGN KEY (CANTON_ID) REFERENCES CANTONS(ID))";
			st.executeUpdate(createTableBV);

			String createTableMAV = "CREATE TABLE MAV(ID INTEGER,BV_ID INTEGER, PRIMARY KEY (ID), FOREIGN KEY (BV_ID) REFERENCES BV(ID))";
			st.executeUpdate(createTableMAV);

			String createTablePersonne = "CREATE TABLE PERSONNES(NUMINSEE INTEGER, NOM VARCHAR(50),PRENOM VARCHAR(50),MDP VARCHAR(50), AVOTE NUMERIC(1),BV_ID INTEGER, PRIMARY KEY (NUMINSEE), FOREIGN KEY (BV_ID) REFERENCES BV (ID))";
			st.executeUpdate(createTablePersonne);

			String createTableBVCandidat = "CREATE TABLE BV_CANDIDAT(NBVOTE INTEGER, BV_ID INTEGER, CANDIDAT_ID INTEGER, PRIMARY KEY (BV_ID, CANDIDAT_ID), FOREIGN KEY (CANDIDAT_ID) REFERENCES CANDIDATS (ID), FOREIGN KEY (BV_ID) REFERENCES BV(ID)  )";
			st.executeUpdate(createTableBVCandidat);

			this.connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * fonction qui initialise la base de donn�es avec les donn�es de
	 * l'application
	 */
	public void initDB() {

		try {
			Statement st = this.connection.createStatement();

			// creation des candidats
			String SQL = "INSERT INTO CANDIDATS VALUES (1,'SARKOZY','NICOLAS','Description  sarkozy ')";
			st.execute(SQL);
			SQL = "INSERT INTO CANDIDATS VALUES (2,'ROYAL','SEGOLENE','Description royal')";
			st.execute(SQL);
			SQL = "INSERT INTO CANDIDATS VALUES (3,'BAYROU','FRANCOIS','Description BAYROU')";
			st.execute(SQL);

			// creation des DEPARTEMENTS
			SQL = "INSERT INTO DEPARTEMENTS VALUES (31,'Haute-garonne')";
			st.execute(SQL);
			SQL = "INSERT INTO DEPARTEMENTS VALUES (64,'Pyr�n�es-Atlantique')";
			st.execute(SQL);
			// SQL = "INSERT INTO DEPARTEMENTS VALUES (63,'Puy de d�me')";
			// st.execute(SQL);

			// creation des circonscription
			SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (1,'CIRC31_1',31)";
			st.execute(SQL);
			SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (2,'CIRC31_2',31)";
			st.execute(SQL);
			SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (3,'CIRC64_1',64)";
			st.execute(SQL);
			// SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (4,'CIRC64_2',64)";
			// st.execute(SQL);
			// SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (5,'CIRC63_1',63)";
			// st.execute(SQL);
			// SQL = "INSERT INTO CIRCONSCRIPTIONS VALUES (6,'CIRC63_2',63)";
			// st.execute(SQL);

			// creation des cantons
			SQL = "INSERT INTO CANTONS VALUES (1,'CANTON1',1)";
			st.execute(SQL);
			SQL = "INSERT INTO CANTONS VALUES (2,'CANTON2',1)";
			st.execute(SQL);
			SQL = "INSERT INTO CANTONS VALUES (3,'CANTON3',2)";
			st.execute(SQL);
			SQL = "INSERT INTO CANTONS VALUES (4,'CANTON4',3)";
			st.execute(SQL);
			// SQL = "INSERT INTO CANTONS VALUES (5,'CANTON5',3)";
			// st.execute(SQL);
			// SQL = "INSERT INTO CANTONS VALUES (6,'CANTON2',3)";
			// st.execute(SQL);

			// bureau de vote
			SQL = "INSERT INTO BV VALUES (1,'BUREAU 1',1)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (2,'BUREAU 2',1)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (3,'BUREAU 3',2)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (4,'BUREAU 4',2)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (5,'BUREAU 5',3)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (6,'BUREAU 6',3)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (7,'BUREAU 7',4)";
			st.execute(SQL);
			SQL = "INSERT INTO BV VALUES (8,'BUREAU 8',4)";
			st.execute(SQL);

			// creation des personnes
			SQL = "INSERT INTO PERSONNES VALUES(1,'BES','ALEXANDRE', '1234', 0,1)";
			st.execute(SQL);

			SQL = "INSERT INTO PERSONNES VALUES(2,'BRETON','GUILLAUME', '1234', 0,2)";
			st.execute(SQL);

			SQL = "INSERT INTO PERSONNES VALUES(3,'CANAYE','KURVIN', '1234', 0,4)";
			st.execute(SQL);

			// creation des MAV
			SQL = "INSERT INTO MAV VALUES (1,1)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (2,1)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (3,2)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (4,2)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (5,3)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (6,3)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (7,4)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (8,4)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (9,5)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (10,5)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (11,6)";
			st.execute(SQL);
			SQL = "INSERT INTO MAV VALUES (12,6)";
			st.execute(SQL);

			this.connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param MAV
	 *            l'identifiant de la machine a voter
	 * @param BV
	 *            l'identifiant du bureau de vote
	 * 
	 * @return true Vrai si le MAV appartient au BV
	 * 
	 * @throws SQLException
	 *             si l'authentification ne peut avoir lieu TODO Faire en sorte
	 *             que l'on ne puisse authentifier une machine qu'une seule fois
	 */
	public boolean authMAV(int MAV, int BV) throws SQLException {

		// creation requete
		String SQL = "SELECT COUNT(*) FROM MAV WHERE ID = ? AND BV_ID = ?";
		PreparedStatement st;
		st = this.connection.prepareStatement(SQL);
		st.setInt(1, MAV);
		st.setInt(2, BV);

		// lance la requete
		ResultSet result = st.executeQuery();
		result.next();

		return result.getInt(1) > 0;
	}

	/**
	 * TODO METTRE EN PLACE UNE VERIFICATION DU PASSWORD ET DU BV ET UN RETOUR
	 * (EXCEPTION...) => A PRIORI FAIT
	 * 
	 * TODO faudrait pas passer l'identiifant de la machine a voter
	 * 
	 * @param numInsee
	 *            le num�ro insee
	 * @param password
	 *            le mot de passe
	 * @param idBV
	 *            l'identifiant du bureau de vote
	 * 
	 * @throws SQLException
	 */
	public void authPersonne(int numInsee, String password, int idBV)
			throws SQLException, BadAuthentificationException,
			AlreadyVoteException, IncorrectBVPersonException {
		// creation requete
		String SQL = "SELECT AVOTE, BV_ID FROM PERSONNES WHERE NUMINSEE = ? AND MDP = ?";
		PreparedStatement st;
		st = this.connection.prepareStatement(SQL);
		st.setInt(1, numInsee);
		st.setString(2, password);

		// lance la requete
		ResultSet result = st.executeQuery();

		// si la personne existe
		if (result.next()) {
			// si la personne a pas deja vot�
			if (result.getInt(1) == 1) {
				throw new AlreadyVoteException("La personne " + numInsee
						+ " a d�j� vot�");
			}

			// si la personne vote dans le bureau de vote
			if (result.getInt(2) != idBV) {
				throw new IncorrectBVPersonException("La personne " + numInsee
						+ " doit voter dans le bureau de vote "
						+ result.getInt(2)
						+ " et non pas dans le bureau de vote " + idBV);
			}

		} else {
			throw new BadAuthentificationException("La personne " + numInsee
					+ " n'existe pas");
		}
	}

	/**
	 * 
	 * @return Retourne la liste des candidats.
	 * 
	 * @throws SQLException
	 */
	public List listeCandidat() throws SQLException {
		// creation requete
		String SQL = "SELECT * FROM CANDIDATS";
		Statement st = this.connection.createStatement();
		ResultSet result = st.executeQuery(SQL);
		List returnedList = new ArrayList();
		CandidatImpl candidat;
		while (result.next()) {
			candidat = new CandidatImpl();
			candidat.setId(result.getInt(1));
			candidat.setNom(result.getString(2));
			candidat.setPrenom(result.getString(3));
			candidat.setDescription(result.getString(4));
			returnedList.add(candidat);
		}
		return returnedList;
	}

	/**
	 * Methode qui retourne les resultats des votes
	 * 
	 * @return le resultSet contenant tous les resultats tri�s
	 * @throws SQLException
	 */
	public ResultSet listeResultat() throws SQLException {
		String SQL = "SELECT BV.ID,CANDIDATS.ID,CANDIDATS.NOM, CANDIDATS.PRENOM,BV_CANDIDAT.NBVOTE, ";
		SQL += "BV.NOM , DEPARTEMENTS.ID, DEPARTEMENTS.NOM, CIRCONSCRIPTIONS.ID, CIRCONSCRIPTIONS.NOM, CANTONS.ID, CANTONS.NOM, BV_CANDIDAT.NBVOTE ";
		SQL += "FROM BV_CANDIDAT, BV, CANDIDATS, DEPARTEMENTS, CIRCONSCRIPTIONS, CANTONS ";
		SQL += "WHERE BV_CANDIDAT.CANDIDAT_ID = CANDIDATS.ID AND ";
		SQL += "BV_CANDIDAT.BV_ID = BV.ID AND ";
		SQL += "BV.CANTON_ID = CANTONS.ID AND ";
		SQL += "CANTONS.CIRC_ID = CIRCONSCRIPTIONS.ID AND ";
		SQL += "CIRCONSCRIPTIONS.DEPT_ID = DEPARTEMENTS.ID ";
		SQL += "ORDER BY DEPARTEMENTS.ID, CIRCONSCRIPTIONS.ID, CANTONS.ID,BV.ID ";

		System.out.println("REQ sql : " + SQL);

		Statement st = this.connection.createStatement();
		ResultSet result = st.executeQuery(SQL);

		return result;
	}

	/**
	 * Fonction qui permet � une personne de voter dans un bureau de vote si
	 * elle n'a pas d�j� vot�.
	 * 
	 * TODO FAUDRA VOIR POUR LES EXCEPTIONS DU GENRE LA PERSONNE N EST PAS
	 * PRESENTE ... CF authPers => FAIT
	 * 
	 * TODO JE PENSE QU IL EST PLUS JUDICIEX LORSQU'ON VOTE DE PASSER EN
	 * PARAMETRE LE PASSWORD COMME CELA LA METHODE VOTE POURRA UTILISER LA
	 * METHODE AUTHPERSONNE => FAIT
	 * 
	 * @param idPersonne
	 *            l'identifiant de la personne
	 * @param password
	 *            le mot de la passe de la personne
	 * @param idCandidat
	 *            l'identifiant du candidat
	 * @param idBV
	 *            l'identifiant du bureau de vote
	 * 
	 * @return true si la vote a �t� effectu� sinon false
	 * @throws SQLException
	 */
	public boolean vote(int numInsee, String password, int idCandidat, int idBV)
			throws SQLException, BadAuthentificationException,
			AlreadyVoteException, IncorrectBVPersonException {

		System.out.println("DBHelper.vote");

		this.authPersonne(numInsee, password, idBV);

		String SQL;
		PreparedStatement st;
		ResultSet result;
		// on indique que la personne a vot�
		SQL = "UPDATE PERSONNES SET AVOTE = 1 WHERE NUMINSEE = ?";
		st = this.connection.prepareStatement(SQL);
		st.setInt(1, numInsee);

		int res = st.executeUpdate();

		System.out.println("res modif de avote" + res);

		if (res == 0) {
			return false;
		} else {
			// il faut ajouter la voix de la personne au candidat

			// si le candidat n'existe pas
			if (!candidatExiste(idCandidat)) {
				System.out.println("Le candidat n'existe pas");
				this.connection.rollback();
				return false;
			}

			System.out.println("Le candidat existe");

			// on regarde si le candidat a d�j� eu des votes
			SQL = "SELECT * FROM BV_CANDIDAT WHERE CANDIDAT_ID = ? AND BV_ID = ?";
			st = this.connection.prepareStatement(SQL);
			st.setInt(1, idCandidat);
			st.setInt(2, idBV);

			// on execute la requ�te
			result = st.executeQuery();

			// si personne n'a encore vot� pour le candidat il faut cr�e
			// une ligne correspondant
			// au candidat dans la table BV_CANDIDAT
			if (!result.next()) {
				System.out.println("creation d'un ligne bv_candidat");

				SQL = "INSERT INTO BV_CANDIDAT VALUES (?, ?, ?)";
				st = this.connection.prepareStatement(SQL);
				st.setInt(1, 0);
				st.setInt(2, idBV);
				st.setInt(3, idCandidat);

				res = st.executeUpdate();

				if (res == 0) {
					this.connection.rollback();
					return false;
				}

				System.out.println("==> bv candidat ajout�");
			}

			// le candidat existe il faut donc lui ajouter une voix
			SQL = "UPDATE BV_CANDIDAT SET NBVOTE = NBVOTE + 1 WHERE CANDIDAT_ID = ? AND BV_ID = ?";
			st = this.connection.prepareStatement(SQL);
			st.setInt(1, idCandidat);
			st.setInt(2, idBV);

			// on execute la requ�te
			res = st.executeUpdate();

			System.out.println("modif nbVote " + res);

			if (res == 0) {
				// on n'a pas put ajouter une voix au candidat pour qui
				// la personne a vot�
				// on annule les modificiation prec�dente
				this.connection.rollback();

				return false;
			}

		}
		this.connection.commit();
		return true;
	}

	/**
	 * @return Retourne true si le candidat dont l'identifiant pass� en
	 *         param�tre existe sinon false.
	 * @throws SQLException
	 *             exception sql
	 */
	private boolean candidatExiste(int idCandidat) throws SQLException {
		String SQL = "SELECT COUNT(*) FROM CANDIDATS WHERE ID = ?";
		PreparedStatement st = this.connection.prepareStatement(SQL);

		st.setInt(1, idCandidat);

		// execution de la requete
		ResultSet resultSet = st.executeQuery();

		resultSet.next();

		return resultSet.getInt(1) != 0;
	}

	/**
	 * 
	 * @return Retourne l'unique instance du DBHelper
	 */
	public static DBHelper getInstance() {
		if (DBHelper.instance == null) {
			DBHelper.instance = new DBHelper();
		}
		return instance;
	}

}
