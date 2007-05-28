package server;

import java.sql.SQLException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import server.corba.SRVServant;
import server.db.DBHelper;
import constants.Constants;

/**
 * @author breton
 * 
 */
public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// recupère le premier argument correspondant a l'identifiant du BV
		if (args.length < 1) {
			System.out.println("ERREUR pas assez d'arg");
			return;
		}

		String idBV = args[0];
		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// create servant and register it with the ORB
			SRVServant SRVRef = new SRVServant();
			orb.connect(SRVRef);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// bind the Object Reference in Naming
			NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME + idBV
					, "");
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
			System.out.println("ERROR: impossible de mettre le serveur en attente");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
