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
    public static Test suite()
    {
    	TestSuite suite = new TestSuite("Serveur test suite");
    	
    	suite.addTest(new ServerTest());
    	
    	return suite;
    }
}
