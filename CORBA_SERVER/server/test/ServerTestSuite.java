package server.test;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import server.Server;

public class ServerTestSuite extends TestCase {

	
	private static SRVThread threadSRV1;
	private static SRVThread threadSRV2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		System.out.println("SetUp");
	}
	
	/**
	 * Lance tous les tests
	 * 
	 * @return un test
	 */
	public static Test suite() {

		// construit du thread pour le SRV1, la date de fin d'election arrivera
		// dans 4 minutes ce qui laissera largement le temps au test de se
		// terminer
		Date endDateElection = new Date(
				System.currentTimeMillis() + 1000 * 60 * 4);
		String sEndDateElection = Server.DATE_FORMATTER.format(endDateElection);
		threadSRV1 = new SRVThread("1", sEndDateElection);

		// construit le thread pour le SRV2, la date de fin d'election est déjà
		// passé (date courante - 10 ms)
		endDateElection = new Date(System.currentTimeMillis() - 10);
		sEndDateElection = Server.DATE_FORMATTER.format(endDateElection);
		threadSRV2 = new SRVThread("2", sEndDateElection);

		// lance les threads pour le SRV1 et SRV2
//		threadSRV1.start();
//		threadSRV2.start();

		// on attend 5 secondes que les serveurs aient bien eu le temps de
		// s'enregistrer auprès du service de nommage
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Problème lors du lancement des serveurs SRV1 et SRV2");
		}

		// construit une suite de test avec toute les methodes de test
		// de la classe ServerTest
		TestSuite suite = new TestSuite(ServerTest.class, "Serveur test suite");

		System.out.println("TestSuite");
		
		return suite;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		System.out.println("TEARDWON");
	}
}
