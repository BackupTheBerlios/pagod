/*
 * Projet PAGOD
 * 
 * $Id: PreCondictionCheckerStateTest.java,v 1.3 2006/01/23 14:13:21 psyko Exp $
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
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;
import junit.framework.TestCase;

/**
 * @author psyko
 * Test unitaire de la classe PreConditionCheckerState correspondant
 * a un �tat de l'ActivityScheduler
 */
public class PreCondictionCheckerStateTest extends TestCase
{
	Activity					activity;
	ActivityScheduler			activityScheduler;
	AbstractActivityState		state;

	protected void setUp () throws Exception
	{
		super.setUp();
		// cr�ation d'une activit� vide pour les tests
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
		// creation d'une liste de produits en entr�e
		List<Product> lInputProduct = new ArrayList<Product>();
		lInputProduct.add(new Product("", "produit1", null, null, null));

		// creation des requetes
		Request reqNext = new Request(Request.RequestType.NEXT);
		Request reqPrevious = new Request(Request.RequestType.PREVIOUS);
		Request reqAleat = new Request(Request.RequestType.CLOSE_PROCESS);
		
		// ajout des produits en entr�e de l'activit�
		this.activity.setInputProducts(lInputProduct);
		
		//on envoie la requete next et on ne doit pas changer d'etat
		assertFalse("Requete PREVIOUS envoy�e a l'etat. Aucun changement ", 
				this.state.manageRequest(reqPrevious));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);
		//on passe a l'etat suivant et on verifie que c'est un ActivityPresentationState
		assertTrue ("Requete NEXT envoy�e a l'etat : changement vers l'etat. ActivityPresentationState",
				this.state.manageRequest(reqNext));
		assertTrue ("L'etat actuel doit etre un ActivityPresentationState",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
	
		// Test de GOTOSTEP
		// creation d'un ArrayList de step avec une etape
		ArrayList<Step> arrStep = new ArrayList<Step>();
		arrStep.add(new Step("Etape 1", null, new ArrayList<Product>()));

		// cr�ation d'une activit� vide pour les tests
		this.activity = new Activity("", "", null, null, arrStep,
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
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
		
		
		//avec ou sans steps dans l'activit�, on passe ds tous les cas  
		//dans un ActiityPresentationState gr�ce au next
		//et le bouton previous est d�sactiv�
		
		this.activityScheduler.setState(this.state);
		//on envoie un ensemble de requetes et on teste si elles ont chang� l'�tat de la machine
		assertFalse("Requete CLOSE_PROCESS envoy�e a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);
		reqAleat = new Request(Request.RequestType.CLOSE_PROJECT);
		
		assertFalse("Requete CLOSE_PROJECT envoy�e a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState()  == this.state);
		
		reqAleat = new Request(Request.RequestType.TERMINATE_ACTIVITY);
		assertFalse("Requete TERMINATE_ACTIVITY envoy�e a l'etat, rien ne doit se passer", 
				this.state.manageRequest(reqAleat));
		assertTrue ("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState()  == this.state);
		
		
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
