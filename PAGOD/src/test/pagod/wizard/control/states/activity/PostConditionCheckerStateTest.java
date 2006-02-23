/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerStateTest.java,v 1.6 2006/02/23 01:43:15 psyko Exp $
 */
package test.pagod.wizard.control.states.activity;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Step;
import pagod.common.model.WorkDefinition;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;

/**
 * Test unitaire de la classe PostConditionCheckerState correspondant a un état
 * pour l'ActivityScheduler
 * 
 * @author yak
 * 
 */
public class PostConditionCheckerStateTest extends TestCase
{
	Activity					activity;
	ActivityScheduler			activityScheduler;
	AbstractActivityState	state;

	protected void setUp () throws Exception
	{
		super.setUp();
		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null, "descr role",
						new ArrayList<Activity>()));

		// creation d'un ActivityScheduler
		this.activityScheduler = new ActivityScheduler(this.activity);

		// creation de l'etat ActivityPresentationState
		this.state = new PostConditionCheckerState(this.activityScheduler,
				this.activity);

		// on met l'ActivityScheduler dans l'etat ActivityPresentationState
		this.activityScheduler.setActivityState(this.state);
	}

	protected void tearDown () throws Exception
	{
		super.tearDown();
		this.activity = null;
	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PostConditionCheckerState.manageRequest(Request)'
	 */
	public void testManageRequest ()
	{
		// creation des requetes

		Request reqNext = new Request(Request.RequestType.NEXT);
		Request reqPrevious = new Request(Request.RequestType.PREVIOUS);
		Request reqAleat = new Request(Request.RequestType.CLOSE_PROCESS);
				
		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null, "descr role",
						new ArrayList<Activity>()));
		
		// creation d'une liste de produit en sortie
		List<Product> lProductOutput = new ArrayList<Product>();
		lProductOutput.add(new Product("", "produit1", null, null, "descr prod", null));
		// ajout de produit en sortie de l'activité
		this.activity.setOutputProducts(lProductOutput);
		
		//on envoie la requete next et on ne doit pas changer d'etat
		assertFalse("Requete NEXT envoyé a l'etat aucun changement ", this.state.manageRequest(reqNext));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState() == this.state);
		//on passe a l'etat precedent et on verifie que c'est un ActivityPresentationState
		assertTrue ("Requete Previous envoyé a l'etat : changement vers l'etat ActivityPresentationState",this.state.manageRequest(reqPrevious));
		assertTrue ("L'etat actuel doit etre un ActivityPresentationState",this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
		
		// creation d'une liste d'etape
		List<Step> lStep = new ArrayList<Step>();
		lStep.add(new Step("", "step1", "", null));
		lStep.add(new Step("", "step2", "", null));
		// ajout d'une etape a l'activité
		this.activity.setSteps(lStep);

		//on revient au debut
		this.state = new PostConditionCheckerState(this.activityScheduler,
				this.activity);
		this.activityScheduler.setState(this.state);
		
		//on passe a l'etat precedent et on verifie que c'est un stepstate
		assertTrue ("Requete Previous envoyé a l'etat : changement vers l'etat StepState",this.state.manageRequest(reqPrevious));
		
		//onreviens au debut
		
		this.activityScheduler.setState(this.state);
		//on envoie un ensemble de requete et on test si elle ont changé l'etat de la machine
		assertFalse("Requete CLOSE_PROCESS envoyée a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);
		
		reqAleat = new Request(Request.RequestType.CLOSE_PROJECT);
		assertFalse("Requete CLOSE_PROJECT envoyée a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",
				this.activityScheduler.getActivityState()  == this.state);
		
		reqAleat = new Request(Request.RequestType.TERMINATE_ACTIVITY);
		assertFalse("Requete TERMINATE_ACTIVITY envoyée a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",
				this.activityScheduler.getActivityState()  == this.state);
		
		
		// Test de GOTOSTEP
		// creation d'un ArrayList de step avec une etape
		ArrayList<Step> arrStep = new ArrayList<Step>();
		arrStep.add(new Step("Etape 1", null, new ArrayList<Product>()));

		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, arrStep,
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null, "descr role",
						new ArrayList<Activity>()));

		// creation d'un ActivityScheduler
		this.activityScheduler = new ActivityScheduler(this.activity);

		ArrayList<AbstractActivityState> arrState = new ArrayList<AbstractActivityState>();

		// initialisation de l'ArrayList
		arrState.add(new PreConditionCheckerState(this.activityScheduler,
				this.activity));
		arrState.add(new StepState(this.activityScheduler, this.activity, 0));
		arrState.add(new PostConditionCheckerState(this.activityScheduler,
				this.activity));

		// on teste la requete GOTOSTEP
		for (int i = 0; i < arrState.size(); i++)
		{
			// on se met dans le bon etat
			// creation de l'etat ActivityState
			this.state = new ActivityPresentationState(this.activityScheduler,
					this.activity);

			// on met l'ActivityScheduler dans l'etat ActivityPresentationState
			this.activityScheduler.setActivityState(this.state);

			// Creation d'une requete GOTOSTEP
			Request request = new Request(Request.RequestType.GOTOSTEP,
					arrState.get(i));

			assertTrue("L'etat devrait changer", 
					this.activityScheduler.ManageRequest(request));
			assertTrue("L'etat devrait etre celui de tab[i]",
					this.activityScheduler.getActivityState() == arrState.get(i));
		}
		
		
	}


	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PostConditionCheckerState.toString()'
	 */
	public void testToString ()
	{

	}

	

}
