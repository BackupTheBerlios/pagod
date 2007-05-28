package mav;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import MAV.Candidat;
import MAV.ResultVote;
import MAV.SRV;
import MAV.SRVHelper;
import MAV.SRVPackage.AlreadyVoteException;
import constants.Constants;

public class Client {
	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("erreur pas assez d'arg");
			return;
		}

		// récupération des identifiants de la machine
		int idMav = Integer.parseInt(args[0]);
		int idBV = Integer.parseInt(args[1]);
		
		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME + idBV, "");
			NameComponent path[] = { nc };
			SRV srvRef = SRVHelper.narrow(ncRef.resolve(path));

			System.out.println("" + srvRef.authMAV(idMav, idBV));

			
			
			srvRef.authPersonne(1, "1234", 1);
			
			System.out.println("Liste candidat: ");
			Candidat[] tabCandidat = srvRef.listeCandidat();
			Candidat candidat;
			for (int i = 0; i < tabCandidat.length; i++) {
				candidat = tabCandidat[i];
				System.out.println(candidat.id() + " - " + candidat.nom()
						+ " - " + candidat.prenom() + " - "
						+ candidat.description());
			}

			// on vote
			boolean voteReussi = srvRef.vote(1, "1234", 1, 1);
			System.out.println("vote reussi (true): " + voteReussi);
			
			// la personne essaye de voter a nouveau
//			voteReussi = srvRef.vote(1, "1234", 1, 1);
//			System.out.println("vote reussi (false): " + voteReussi);
			
			// la personne a deja voté
			try 
			{
				srvRef.authPersonne(1, "1234", 1);
				System.out.println("Peut etre authentifier: " + voteReussi);
			}
			catch (AlreadyVoteException e)
			{
				System.out.println("La personne a deja voté");
			}
			
			ResultVote[] result = srvRef.listeResultat();
			
			System.out.println("result.length :" + result.length);
			
			System.out.println("avant que la personne 2 vote pour une mauvaise personne");
			
			try
			{
				// la personne 2 doit voter dans le BV 2 et non pas 1
				// on test de faire voter une personne dans un mauvais bureau de vote
				voteReussi = srvRef.vote(2, "1234", 1, 1);
				
			}
			catch (Exception e) {
				System.out.println("PB dhfdgsk");
			}
			
			
			
			
		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
	}
}
