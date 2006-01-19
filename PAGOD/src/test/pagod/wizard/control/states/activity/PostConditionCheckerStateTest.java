/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerStateTest.java,v 1.3 2006/01/19 21:48:22 psyko Exp $
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
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;

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
	PostConditionCheckerState	state;

	protected void setUp () throws Exception
	{
		super.setUp();
		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
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
		Request reqGotoStep_1 = new Request(Request.RequestType.GOTOSTEP, new PreConditionCheckerState(this.activityScheduler,this.activity));
		Request reqGotoStep_2 = new Request(Request.RequestType.GOTOSTEP, new ActivityPresentationState(this.activityScheduler,this.activity));
		Request reqGotoStep_3 = new Request(Request.RequestType.GOTOSTEP, new PostConditionCheckerState(this.activityScheduler,this.activity));
		
		
		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
						new ArrayList<Activity>()));
		
		// creation d'une liste de produit en sortie
		List<Product> lProductOutput = new ArrayList<Product>();
		lProductOutput.add(new Product("", "produit1", null, null, null));
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
		assertFalse("Requete CLOSE_PROCESS envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState() == this.state);
		reqAleat = new Request(Request.RequestType.CLOSE_PROJECT);
		assertFalse("Requete CLOSE_PROJECT envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState()  == this.state);
		reqAleat = new Request(Request.RequestType.TERMINATE_ACTIVITY);
		assertFalse("Requete TERMINATE_ACTIVITY envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState()  == this.state);
		
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PreConditionCheckerState
		assertTrue ("Requete GOTOSTEP_1 envoyée a l'etat : changement vers l'état PreConditionCheckerState ",this.state.manageRequest(reqGotoStep_1));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PreConditionCheckerState
		assertTrue ("Requete GOTOSTEP_2 envoyée a l'etat : changement vers l'état ActivityPresentationState ",this.state.manageRequest(reqGotoStep_2));
		assertTrue ("L'etat actuel doit etre un ActivityPresentationState",this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PostConditionCheckerState
		assertTrue ("Requete GOTOSTEP_3 envoyée a l'etat : changement vers l'état PostConditionCheckerState ",this.state.manageRequest(reqGotoStep_3));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);
		
		
	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PostConditionCheckerState.terminate()'
	 */
	public void testTerminate ()
	{

	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PostConditionCheckerState.toString()'
	 */
	public void testToString ()
	{

	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PostConditionCheckerState.display()'
	 */
	public void testDisplay ()
	{

	}

}
