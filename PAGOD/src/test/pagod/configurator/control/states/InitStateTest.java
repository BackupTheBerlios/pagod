/*
 * Projet PAGOD
 * 
 * $Id: InitStateTest.java,v 1.1 2006/02/19 15:37:49 yak Exp $
 */
package test.pagod.configurator.control.states;

import junit.framework.TestCase;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.states.InitState;
import pagod.configurator.control.states.ProcessOpenedState;
import pagod.configurator.control.states.Request;

/**
 * @author yak test de la classe InitState
 */
public class InitStateTest extends TestCase
{
	private InitState	initState;

	/**
	 * test du constructeur
	 */
	public InitStateTest ()
	{
		 this.initState = new InitState (ApplicationManager.getInstance());
	}

	/**
	 * Test method for
	 * 'pagod.configurator.control.states.InitState.manageRequest(Request)'
	 */
	public void testManageRequest ()
	{
		// Creation d'une requete CLOSE_PROJECT
		Request requestOpenProcess = new Request(
				Request.RequestType.OPEN_PROCESS);

		// il ne doit aps y avoir de changement détat lorsque l'on recoit la
		// requete
		assertTrue("L'etat doit avoir change", this.initState
				.manageRequest(requestOpenProcess));

		// le nouvelle etat doit etre de type processOpenedState
		assertTrue("L'etat doit avoir change", ApplicationManager.getInstance()
				.getState() instanceof ProcessOpenedState);
	}

}
