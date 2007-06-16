package server.test;

import java.util.Date;

import junit.framework.TestCase;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import server.Server;
import MAV.Candidat;
import MAV.ResultVote;
import MAV.SRV;
import MAV.SRVHelper;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.ElectionFinishedException;
import MAV.SRVPackage.IncorrectBVPersonException;
import MAV.SRVPackage.InternalErrorException;
import constants.Constants;

/**
 * Classe qui permet de tester le serveur.
 * 
 * Remarques: 
 * - la méthode testLaunchSRV doit être la premiere méthode de test dans la classe car 
 * elle permet de lancer les 2 SRV et d'initialiser des attributs static pour les tests
 * -  le srv 1 doit avoir une date
 * de fin d'election suffisament loin pour que les tests unitaires ait le temps
 * de se faire 
 * - le srv 2 doit avoir une date de fin d'election déjà passée
 * - la méthode testStopSRV arrete les deux threads SRV lancé pour les besoins des tests
 * 
 * @author bes
 * 
 */
public class ServerTest extends TestCase {

	/**
	 * Le thread qui fait tourner le SRV 1
	 */
	private static SRVThread threadSRV1;

	/**
	 * Le thread qui fait tourner le SRV 2
	 */
	private static SRVThread threadSRV2;

	/**
	 * la reference vers le srv1
	 */
	private static SRV srv1;

	/**
	 * la reference vers le srv2
	 */
	private static SRV srv2;


	/**
	 * Cette méthode permet de lancer 2 thread qui font tourner les 2 SRV
	 * necessaires aux tests unitaire. Elle initialise srv1 et srv2.
	 * 
	 * Il est très important que cela soit la premiere dans le test afin qu'elle
	 * soit appelé en premier lors du lancement des tests unitaires.
	 * 
	 */
	public void testLaunchSRV() {
		// construit du thread pour le SRV1, la date de fin d'election arrivera
		// dans 4 minutes ce qui laissera largement le temps au test de se
		// terminer
		Date endDateElection = new Date(
				System.currentTimeMillis() + 1000 * 60 * 4);
		String sEndDateElection = Server.DATE_FORMATTER.format(endDateElection);
		ServerTest.threadSRV1 = new SRVThread("1", sEndDateElection);

		// construit le thread pour le SRV2, la date de fin d'election est déjà
		// passé (date courante - 10 ms)
		endDateElection = new Date(System.currentTimeMillis() - 10);
		sEndDateElection = Server.DATE_FORMATTER.format(endDateElection);
		ServerTest.threadSRV2 = new SRVThread("2", sEndDateElection);

		// lance le thread pour le SRV1
		ServerTest.threadSRV1.start();

		// on attend 2 secondes que le serveur aient bien eu le temps de
		// s'enregistrer auprès du service de nommage
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Problème lors du lancement du serveur SRV1");
		}

		// lance le thread pour le SRV2
		ServerTest.threadSRV2.start();

		// on attend 2 secondes que le serveur aient bien eu le temps de
		// s'enregistrer auprès du service de nommage
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Problème lors du lancement du serveur SRV2");
		}

		// initilisation des 2 references vers les SRV

		String[] args = { "" };

		// create and initialize the ORB
		ORB orb = ORB.init(args, null);

		try {
			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// on recupere le srv1
			NameComponent nc = new NameComponent(
					Constants.SRV_SERVANT_NAME + 1, Constants.SRV_KIND);
			NameComponent path[] = { nc };
			ServerTest.srv1 = SRVHelper.narrow(ncRef.resolve(path));
			assertNotNull(ServerTest.srv1);

			// on recupere le srv2
			nc = new NameComponent(Constants.SRV_SERVANT_NAME + 2,
					Constants.SRV_KIND);
			path[0] = nc;
			ServerTest.srv2 = SRVHelper.narrow(ncRef.resolve(path));
			assertNotNull(ServerTest.srv2);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Erreur lors de l'initialisation des 2 references");
		}
	}

	public void testAuthMAV() {

		// test d'une authentification qui doit reussir
		boolean authentificationOk = true;
		boolean retour = false;
		try {
			retour = ServerTest.srv1.authMAV(1, 1);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			authentificationOk = false;

		} catch (ElectionFinishedException e) {
			authentificationOk = false;
		}
		assertTrue(authentificationOk && retour);

		// test d'une authentification qui doit echouer
		// car la mav 100 n'existe pas
		boolean exceptionLeve = false;
		try {
			retour = ServerTest.srv1.authMAV(100, 1);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			exceptionLeve = true;

		} catch (ElectionFinishedException e) {
			exceptionLeve = true;
		}
		assertTrue(!exceptionLeve && !retour);

		// test d'une authentification qui doit echouer
		// car la bv 100 n'existe pas
		exceptionLeve = false;
		try {
			retour = ServerTest.srv1.authMAV(1, 100);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			exceptionLeve = true;

		} catch (ElectionFinishedException e) {
			exceptionLeve = true;
		}
		assertTrue(!exceptionLeve && !retour);

		// test d'une authentification qui doit echouer
		// car l'election est fini
		boolean ElectionFinishedExceptionLeve = false;
		try {
			ServerTest.srv2.authMAV(3, 2);
		} catch (ElectionFinishedException e) {
			ElectionFinishedExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ElectionFinishedExceptionLeve);
	}

	public void testAuthPersonne() {
		// test d'une authentification qui doit reussir
		boolean authentificationOk = true;
		try {
			ServerTest.srv1.authPersonne(1, "1234", 1);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			authentificationOk = false;

		} catch (Exception e) {
			authentificationOk = false;
		}
		assertTrue(authentificationOk);

		// test d'une authentification qui doit echouer
		// car le mot de passe n'est pas le bon
		boolean badAuthentificationExceptionLeve = false;
		try {
			ServerTest.srv1.authPersonne(1, "1", 1);
		} catch (BadAuthentificationException e) {
			badAuthentificationExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(badAuthentificationExceptionLeve);

		// test d'une authentification qui doit echouer
		// car la personne 100 n'existe pas
		badAuthentificationExceptionLeve = false;
		try {
			ServerTest.srv1.authPersonne(100, "1234", 1);
		} catch (BadAuthentificationException e) {
			badAuthentificationExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(badAuthentificationExceptionLeve);

		// test d'une authentification qui doit echouer
		// car la personne 1 doit voter dans le bv 1 et non pas 2
		boolean incorrectBVPersonExceptionLeve = false;
		try {
			ServerTest.srv1.authPersonne(1, "1234", 2);
		} catch (IncorrectBVPersonException e) {
			incorrectBVPersonExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(incorrectBVPersonExceptionLeve);

		// Remarque : le test sur l'exception AlreadyVoteException est fait dans
		// vote

		// test d'une authentification qui doit echouer
		// car l'election est fini
		boolean ElectionFinishedExceptionLeve = false;
		try {
			ServerTest.srv2.authPersonne(2, "1234", 2);
		} catch (ElectionFinishedException e) {
			ElectionFinishedExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ElectionFinishedExceptionLeve);

	}
	
	public void testListeCandidat()
	{
		
		try {
			Candidat[] candidats = ServerTest.srv1.listeCandidat();
			
			assertEquals(candidats.length, 3);
			
			// parcours de la liste des candidats
			for (int i = 0 ; i < candidats.length ; i++)
			{
				Candidat currentCandidat = candidats[i]; 
				switch (currentCandidat.id()) {
				case 1:
					assertEquals("SARKOZY", currentCandidat.nom());
					assertEquals("NICOLAS", currentCandidat.prenom());
					assertEquals("Description  sarkozy ", currentCandidat.description());
					
					break;
					
				case 2:
					assertEquals("ROYAL", currentCandidat.nom());
					assertEquals("SEGOLENE", currentCandidat.prenom());
					assertEquals("Description  royal ", currentCandidat.description());
					
					break;
				case 3:
					assertEquals("BAYROU", currentCandidat.nom());
					assertEquals("FRANCOIS", currentCandidat.prenom());
					assertEquals("Description  BAYROU ", currentCandidat.description());
					
					break;

				default:
						fail();
					break;
				}
			}
			
			
			candidats = ServerTest.srv2.listeCandidat();
			
			assertEquals(candidats.length, 3);
			
			// parcours de la liste des candidats
			for (int i = 0 ; i < candidats.length ; i++)
			{
				Candidat currentCandidat = candidats[i]; 
				switch (currentCandidat.id()) {
				case 1:
					assertEquals("SARKOZY", currentCandidat.nom());
					assertEquals("NICOLAS", currentCandidat.prenom());
					assertEquals("Description  sarkozy ", currentCandidat.description());
					
					break;
					
				case 2:
					assertEquals("ROYAL", currentCandidat.nom());
					assertEquals("SEGOLENE", currentCandidat.prenom());
					assertEquals("Description  royal ", currentCandidat.description());
					
					break;
				case 3:
					assertEquals("BAYROU", currentCandidat.nom());
					assertEquals("FRANCOIS", currentCandidat.prenom());
					assertEquals("Description  BAYROU ", currentCandidat.description());
					
					break;

				default:
						fail();
					break;
				}
			}
			
		} catch (InternalErrorException e) {
			e.printStackTrace();
			fail();
		}
		
		
	}

	public void testVote() {
		// test d'un vote qui doit echouer
		// car le mot de passe n'est pas le bon
		boolean badAuthentificationExceptionLeve = false;
		try {
			ServerTest.srv1.vote(1, "1", 1, 1);
		} catch (BadAuthentificationException e) {
			badAuthentificationExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(badAuthentificationExceptionLeve);

		// test d'un vote qui doit echouer
		// car la personne 100 n'existe pas
		badAuthentificationExceptionLeve = false;
		try {
			ServerTest.srv1.vote(100, "1234", 1, 1);
		} catch (BadAuthentificationException e) {
			badAuthentificationExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(badAuthentificationExceptionLeve);

		// test d'un vote qui doit echouer
		// car la personne 1 doit voter dans le bv 1 et non pas 2
		boolean incorrectBVPersonExceptionLeve = false;
		try {
			ServerTest.srv1.authPersonne(1, "1234", 2);
		} catch (IncorrectBVPersonException e) {
			incorrectBVPersonExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(incorrectBVPersonExceptionLeve);

		// test d'un vote qui doit réussir

		// parcours des resultats pour recuperer le nb de vote pour le candidat
		// dans le bv
		int idBv = 1;
		int idCandidat = 1;
		int nbVotesAvant = 0;

		try {
			ResultVote[] resultats = ServerTest.srv1.listeResultat();

			// parcours des resultats pour voir si le vote a été pris en compte
			boolean trouve = false;
			int i = 0;
			while (!trouve && i < resultats.length) {
				ResultVote unResultat = resultats[i];
				if (unResultat.idBV() == idBv
						&& unResultat.idCandidat() == idCandidat) {
					nbVotesAvant = unResultat.nbVote();
					trouve = true;
				}
				i++;
			}

		} catch (InternalErrorException e1) {
			e1.printStackTrace();
			fail();
		}

		boolean aVote = false;
		try {
			aVote = ServerTest.srv1.vote(1, "1234", 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(aVote);
		// on verifie que le vote a bien été pris en compte
		try {
			ResultVote[] resultats = ServerTest.srv1.listeResultat();

			int nbVotesApres = 0;
			// parcours des resultats pour voir si le vote a été pris en compte
			boolean trouve = false;
			int i = 0;
			while (!trouve && i < resultats.length) {
				ResultVote unResultat = resultats[i];
				if (unResultat.idBV() == 1 && unResultat.idCandidat() == 1) {
					trouve = true;
					nbVotesApres = unResultat.nbVote();
				}
				i++;
			}

			assertTrue(trouve);
			assertEquals(nbVotesAvant + 1, nbVotesApres);

		} catch (InternalErrorException e1) {
			e1.printStackTrace();
			fail();
		}

		// test d'un vote qui ne doit pas reussir car la personne a déjà voté
		boolean alreadyVoteExceptionLeve = false;
		try {
			ServerTest.srv1.vote(1, "1234", 1, 1);
		} catch (AlreadyVoteException e) {
			alreadyVoteExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(alreadyVoteExceptionLeve);

		// test d'une exception qui ne doit pas reussir car la personne a déjà
		// voté
		alreadyVoteExceptionLeve = false;
		try {
			ServerTest.srv1.authPersonne(1, "1234", 1);
		} catch (AlreadyVoteException e) {
			alreadyVoteExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(alreadyVoteExceptionLeve);

		// test d'un vote qui doit echouer
		// car l'election est fini
		boolean ElectionFinishedExceptionLeve = false;
		try {
			ServerTest.srv2.vote(2, "1234", 1, 2);
		} catch (ElectionFinishedException e) {
			ElectionFinishedExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ElectionFinishedExceptionLeve);
	}

	/**
	 * Cette fonction permet d'arreter les 2 threads qui font tourné les 2 SRV.
	 * 
	 */
	public void testStopSRV() {
		ServerTest.threadSRV1.stop();
		ServerTest.threadSRV2.stop();
	}
}
