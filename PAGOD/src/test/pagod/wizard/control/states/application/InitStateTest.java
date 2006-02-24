 /*
 * Projet PAGOD
 * 
 * $Id: InitStateTest.java,v 1.3 2006/02/24 16:37:43 psyko Exp $
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
		
		/** ****** Test sur une requete quelconque ******* */

		// on se met dans le bon etat
		// creation de l'etat InitState
		this.initState  = new InitState (ApplicationManager.getInstance());

		// pour toutes les autres requetes l'etat ne devrait pas changer
		for (Request.RequestType aRequest : Request.RequestType.values())
		{

			// si la requete est OPEN_PROJECT
			// suivante car ces cas la on deja etaient teste
			if (aRequest == Request.RequestType.OPEN_PROJECT) continue;

			Request request = new Request(aRequest);

			assertFalse(
					"L'etat ne devrait pas changer car ce type de requete ne fait pas changer lorsqu'on est dans l'etat ActivityPresentationState",
					this.initState.manageRequest(request));

			
			
		}
	}
}
