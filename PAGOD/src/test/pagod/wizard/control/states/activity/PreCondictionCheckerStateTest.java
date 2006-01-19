/*
 * Projet PAGOD
 * 
 * $Id: PreCondictionCheckerStateTest.java,v 1.2 2006/01/19 19:43:08 psyko Exp $
 */
package test.pagod.wizard.control.states.activity;

import java.util.ArrayList;
import java.util.List;

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
import junit.framework.TestCase;

/**
 * @author psyko
 * Test unitaire de la classe PreConditionCheckerState correspondant
 * a un état de l'ActivityScheduler
 */
public class PreCondictionCheckerStateTest extends TestCase
{
	Activity					activity;
	ActivityScheduler			activityScheduler;
	PreConditionCheckerState	state;

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
		this.state = new PreConditionCheckerState(this.activityScheduler,
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
		// creation d'une liste de produits en entrée
		List<Product> lInputProduct = new ArrayList<Product>();
		lInputProduct.add(new Product("", "produit1", null, null, null));

		// creation des requetes
		Request reqNext = new Request(Request.RequestType.NEXT);
		Request reqPrevious = new Request(Request.RequestType.PREVIOUS);
		Request reqAleat = new Request(Request.RequestType.CLOSE_PROCESS);
		Request reqGotoStep_1 = new Request(Request.RequestType.GOTOSTEP, new PreConditionCheckerState(this.activityScheduler,this.activity));
		Request reqGotoStep_2 = new Request(Request.RequestType.GOTOSTEP, new ActivityPresentationState(this.activityScheduler,this.activity));
		Request reqGotoStep_3 = new Request(Request.RequestType.GOTOSTEP, new PostConditionCheckerState(this.activityScheduler,this.activity));
		
		// ajout des produits en entrée de l'activité
		this.activity.setInputProducts(lInputProduct);
		
		//on envoie la requete next et on ne doit pas changer d'etat
		assertFalse("Requete PREVIOUS envoyée a l'etat. Aucun changement ", this.state.manageRequest(reqPrevious));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState() == this.state);
		//on passe a l'etat suivant et on verifie que c'est un ActivityPresentationState
		assertTrue ("Requete NEXT envoyée a l'etat : changement vers l'etat. ActivityPresentationState",this.state.manageRequest(reqNext));
		assertTrue ("L'etat actuel doit etre un ActivityPresentationState",this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
	
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PreConditionCheckerState
		assertTrue ("Requete GOTOSTEP_1 envoyée a l'etat : changement vers l'état PreConditionCheckerState ",this.state.manageRequest(reqGotoStep_1));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PreConditionCheckerState
		assertTrue ("Requete GOTOSTEP_2 envoyée a l'etat : changement vers l'état ActivityPresentationState ",this.state.manageRequest(reqGotoStep_2));
		assertTrue ("L'etat actuel doit etre un ActivityPresentationState",this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
		// on passe a l'etat preconditionCheckerState et on verifie que c'est un PostConditionCheckerState
		assertTrue ("Requete GOTOSTEP_3 envoyée a l'etat : changement vers l'état PostConditionCheckerState ",this.state.manageRequest(reqGotoStep_3));
		assertTrue ("L'etat actuel doit etre un PostConditionCheckerState",this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);
		
		
		//avec ou sans steps dans l'activité, on passe ds tous les cas  
		//dans un ActiityPresentationState gràce au next
		//et le bouton previous est désactivé
		
		this.activityScheduler.setState(this.state);
		//on envoie un ensemble de requetes et on teste si elles ont changé l'état de la machine
		assertFalse("Requete CLOSE_PROCESS envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState() == this.state);
		reqAleat = new Request(Request.RequestType.CLOSE_PROJECT);
		assertFalse("Requete CLOSE_PROJECT envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState()  == this.state);
		
		reqAleat = new Request(Request.RequestType.TERMINATE_ACTIVITY);
		assertFalse("Requete TERMINATE_ACTIVITY envoyée a l'etat, rien ne doit se passer", this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",this.activityScheduler.getActivityState()  == this.state);
		
		
	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PreConditionCheckerState.terminate()'
	 */
	public void testTerminate ()
	{

	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PreConditionCheckerState.toString()'
	 */
	public void testToString ()
	{

	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.PreConditionCheckerState.display()'
	 */
	public void testDisplay ()
	{

	}
}
