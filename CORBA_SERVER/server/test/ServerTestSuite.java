package server.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ServerTestSuite extends TestCase {
	
	/**
	 * Lance tous les tests
	 * 
	 * @return un test
	 */
	public static Test suite() {
		// construit une suite de test avec toute les methodes de test
		// de la classe ServerTest
		TestSuite suite = new TestSuite(ServerTest.class, "Serveur test suite");
		
		return suite;
	}
	
}
