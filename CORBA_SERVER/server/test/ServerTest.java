package server.test;

import junit.framework.TestCase;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

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
 * Prerequis : - avoir lancé le srv 1 et le srv 2 - le srv 1 doit avoir une date
 * de fin d'election suffisament loin pour que les tests unitaires ait le temps
 * de se faire - le srv 2 doit avoir une date de fin d'election déjà passée
 * 
 * 
 * @author bes
 * 
 */
public class ServerTest extends TestCase {

	private SRV srv1;

	private SRV srv2;

	// TODO methode a tester puis a supprimer
	// ResultSeq listeResultat () raises (InternalErrorException);
	// CandidatSeq listeCandidat() raises (InternalErrorException);

	public void setUp() throws Exception {
		String[] args = { "" };

		// create and initialize the ORB
		ORB orb = ORB.init(args, null);

		// get the root naming context
		org.omg.CORBA.Object objRef = orb
				.resolve_initial_references("NameService");
		NamingContext ncRef = NamingContextHelper.narrow(objRef);

		// on recupere le srv1
		NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME + 1,
				Constants.SRV_KIND);
		NameComponent path[] = { nc };
		this.srv1 = SRVHelper.narrow(ncRef.resolve(path));

		// on recupere le srv2
		nc = new NameComponent(Constants.SRV_SERVANT_NAME + 2,
				Constants.SRV_KIND);
		path[0] = nc;
		this.srv2 = SRVHelper.narrow(ncRef.resolve(path));
	}

	public void testAuthMAV() {

		// test d'une authentification qui doit reussir
		boolean authentificationOk = true;
		boolean retour = false;
		try {
			retour = this.srv1.authMAV(1, 1);
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
			retour = this.srv1.authMAV(100, 1);
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
			retour = this.srv1.authMAV(1, 100);
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
			this.srv2.authMAV(3, 2);
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
			this.srv1.authPersonne(1, "1234", 1);
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
			this.srv1.authPersonne(1, "1", 1);
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
			this.srv1.authPersonne(100, "1234", 1);
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
			this.srv1.authPersonne(1, "1234", 2);
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
			this.srv2.authPersonne(2, "1234", 2);
		} catch (ElectionFinishedException e) {
			ElectionFinishedExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ElectionFinishedExceptionLeve);

	}

	public void testVote() {
		// test d'un vote qui doit echouer
		// car le mot de passe n'est pas le bon
		boolean badAuthentificationExceptionLeve = false;
		try {
			this.srv1.vote(1, "1", 1, 1);
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
			this.srv1.vote(100, "1234", 1, 1);
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
			this.srv1.authPersonne(1, "1234", 2);
		} catch (IncorrectBVPersonException e) {
			incorrectBVPersonExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(incorrectBVPersonExceptionLeve);

		// test d'un vote qui doit réussir

		// parcours des resultats pour recuperer le nb de vote pour le candidat dans le bv
		int idBv = 1;
		int idCandidat = 1;
		int nbVotesAvant = 0;
		
		try {
			ResultVote[] resultats = this.srv1.listeResultat();

			// parcours des resultats pour voir si le vote a été pris en compte
			boolean trouve = false;
			int i = 0;
			while (!trouve && i < resultats.length) {
				ResultVote unResultat = resultats[i];
				if (unResultat.idBV() == idBv && unResultat.idCandidat() == idCandidat) {
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
			aVote = this.srv1.vote(1, "1234", 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(aVote);
		// on verifie que le vote a bien été pris en compte
		try {
			ResultVote[] resultats = this.srv1.listeResultat();

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
			this.srv1.vote(1, "1234", 1, 1);
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
			this.srv1.authPersonne(1, "1234", 1);
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
			this.srv2.vote(2, "1234", 1, 2);
		} catch (ElectionFinishedException e) {
			ElectionFinishedExceptionLeve = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ElectionFinishedExceptionLeve);
	}

}
