/*
 * Projet PAGOD
 * 
 * $Id: ProcessOpenedStateTest.java,v 1.1 2006/02/19 15:37:49 yak Exp $
 */
package test.pagod.configurator.control.states;

import junit.framework.TestCase;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.states.ProcessOpenedState;
import pagod.configurator.control.states.Request;

/**
 * @author yak test de la class ProcessOpenedState
 */
public class ProcessOpenedStateTest extends TestCase
{
	private ProcessOpenedState	processOpenedState;

	/**
	 * test du constructeur
	 */
	public ProcessOpenedStateTest ()
	{
		this.processOpenedState = new ProcessOpenedState(ApplicationManager
				.getInstance());
	}

	/**
	 * Test method for
	 * 'pagod.configurator.control.states.ProcessOpenedState.manageRequest(Request)'
	 */
	public void testManageRequest ()
	{
		// Creation d'une requete CLOSE_PROJECT
		Request requestOpenProcess = new Request(
				Request.RequestType.OPEN_PROCESS);

		// il ne doit aps y avoir de changement détat lorsque l'on recoit la
		// requete
		assertTrue("L'etat doit avoir change", this.processOpenedState
				.manageRequest(requestOpenProcess));
		// le nouvelle etat doit etre de type processOpenedState
		assertTrue("L'etat doit avoir change", ApplicationManager.getInstance()
				.getState() instanceof ProcessOpenedState);
	}

}
