 /*
 * Projet PAGOD
 * 
 * $Id: InitStateTest.java,v 1.1 2006/02/21 12:12:20 fabfoot Exp $
 */
package test.pagod.wizard.control.states.application;

import junit.framework.TestCase;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.application.InitState;
import pagod.wizard.control.states.application.ProjectOpenedState;

/**
 * @author fabfoot
 * 
 */
public class InitStateTest extends TestCase
{
	/**
	 * l'objet a tester
	 */
	private InitState	initState;

	/**
	 * Constructeur de la classe de test
	 * 
	 */
	public InitStateTest ()
	{
		this.initState = new InitState(ApplicationManager.getInstance());
	}

	/**
	 * Methode permettant de tester la methode manageRequest
	 * 
	 */
	public void testManageRequest ()
	{

		/** *********** GESTION D'UNE REQUETE OPEN_PROJECT ************ */
//		 Creation d'une requete OPEN_PROCESS
		Request requestOpenProject = new Request(
				Request.RequestType.OPEN_PROJECT);

		assertTrue("L'etat doit avoir change", this.initState
				.manageRequest(requestOpenProject));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type ProjectOpenedState",
				ApplicationManager.getInstance().getState() instanceof ProjectOpenedState );
	}
}
