package server;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import server.corba.SRVServant;
import server.db.DBHelper;
import constants.Constants;

/**
 * @author bes
 * 
 */
public class Server {

	/**
	 * Le format des dates
	 */
	private static String DATE_PATTERN = "dd-MM-yyyy_HH-mm";

	public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			Server.DATE_PATTERN);

	/**
	 * @param args -
	 *            args[0] est l'identifiant du bureau de vote args[1] est la
	 *            date de fin de l'election au format : jj-mm-aaaa_hh-mm
	 */
	public static void main(String[] args) {

		// recupère le premier argument correspondant a l'identifiant du BV
		if (args.length < 2) {
			System.out.println("ERREUR pas assez d'arg");
			return;
		}

		// on recupere l'identifiant du bureau de vote
		String idBV = args[0];

		try {
			// on recupere la date de fin des elections
			String sEndDateElection = args[1];
			// la date de fin des elections attention la methode parse peut
			// lever une exception
			Date endDateElection = DATE_FORMATTER.parse(sEndDateElection);
			
			// TODO a suppr
			System.out.println("endDateElection : " + DATE_FORMATTER.format(endDateElection));

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);
			System.out.println(orb.getClass().getName());

			// create servant and register it with the ORB
			SRVServant SRVRef = new SRVServant(endDateElection);
			orb.connect(SRVRef);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// bind the Object Reference in Naming
			NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME
					+ idBV, Constants.SRV_KIND);
			NameComponent path[] = { nc };
			ncRef.rebind(path, SRVRef);
			// wait for invocations from clients
			java.lang.Object sync = new java.lang.Object();
			System.out.println("service launched");

			System.out.println("Create schema");
			// creation du schema
			DBHelper.getInstance().createConnection(idBV);
			DBHelper.getInstance().createSchema();

			// initialisation de la base
			System.out.println("Initialisation de la base");
			DBHelper.getInstance().initDB();

			System.out.println("SRV pret");

			synchronized (sync) {
				sync.wait();
			}

		} catch (SQLException e) {
			System.err.println("ERROR SQL: " + e);
			e.printStackTrace(System.out);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: impossible de charger le driver");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out
					.println("ERROR: impossible de mettre le serveur en attente");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out
					.println("ERROR: la date de fin d'election n'est pas au bon format. Le format est '"
							+ Server.DATE_PATTERN + "'");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
