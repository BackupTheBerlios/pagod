/*
 * Projet PAGOD
 * 
 * $Id: ProcessOpenedStateTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
 */
package test.pagod.wizard.control.states.application;

import java.util.ArrayList;

import junit.framework.TestCase;
import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Step;
import pagod.common.model.WorkDefinition;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.application.ActivityLaunchedState;
import pagod.wizard.control.states.application.InitState;
import pagod.wizard.control.states.application.ProcessOpenedState;

/**
 * Classe permettant de tester la classe ProcessOpenedState.
 * 
 * @author Alexandre Bes
 */
public class ProcessOpenedStateTest extends TestCase
{
	/**
	 * l'objet a tester
	 */
	private ProcessOpenedState	processOpenedState;

	/**
	 * Constructeur de la classe de test
	 * 
	 */
	public ProcessOpenedStateTest ()
	{
		this.processOpenedState = new ProcessOpenedState(ApplicationManager
				.getInstance());
	}

	/**
	 * Methode permettant de tester la methode manageRequest
	 * 
	 */
	public void testManageRequest ()
	{
		/************* GESTION D'UNE REQUETE CLOSE_PROJECT *************/ 
		
		// Creation d'une requete CLOSE_PROJECT
		Request requestCloseProject = new Request(
				Request.RequestType.CLOSE_PROJECT);

		assertTrue("L'etat doit avoir change", this.processOpenedState
				.manageRequest(requestCloseProject));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type InitState",
				ApplicationManager.getInstance().getState() instanceof InitState);

		
		/************* GESTION D'UNE REQUETE RUN_ACTIVITY *************/
		// on remet l'applicationManager dans l'etat ProcessOpenedState
		ApplicationManager.getInstance().setState(this.processOpenedState);
		assertTrue(ApplicationManager.getInstance().getState() instanceof ProcessOpenedState);
		
		Request requestRunActivity = new Request(
				Request.RequestType.RUN_ACTIVITY);
		
		// creation d'une activite
		Activity activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null, "descr role",
						new ArrayList<Activity>()));
		
		// on passe en parametre de la requete l'activite que l'on veut lancer
		requestRunActivity.setContent(activity);

		assertTrue("L'etat doit avoir change", this.processOpenedState
				.manageRequest(requestRunActivity));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type ActivityLaunchedState",
				ApplicationManager.getInstance().getState() instanceof ActivityLaunchedState);
		
	}

}
