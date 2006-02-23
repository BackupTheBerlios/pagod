/*
 * Projet PAGOD
 * 
 * $Id: PreCondictionCheckerStateTest.java,v 1.5 2006/02/23 01:43:15 psyko Exp $
 */
package test.pagod.wizard.control.states.activity;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import pagod.common.model.Activity;
import pagod.common.model.Guidance;
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
 * @author psyko Test unitaire de la classe PreConditionCheckerState
 *         correspondant a un état de l'ActivityScheduler
 */
public class PreCondictionCheckerStateTest extends TestCase
{
	Activity				activity;
	ActivityScheduler		activityScheduler;
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
		lInputProduct.add(new Product("", "produit1", null, null, "descr prod1", null));

		// creation des requetes
		Request reqNext = new Request(Request.RequestType.NEXT);
		Request reqPrevious = new Request(Request.RequestType.PREVIOUS);
		Request reqAleat = new Request(Request.RequestType.CLOSE_PROCESS);

		// ajout des produits en entrée de l'activité
		this.activity.setInputProducts(lInputProduct);

		// on envoie la requete PREVIOUS et on doit passer en
		// ActivityPresentationState
		assertTrue(
				"Requete PREVIOUS envoyée a l'etat : changement d'etat.",
				this.state.manageRequest(reqPrevious));
		assertTrue(
				"L'etat actuel doit etre un ActivityPresentationState",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
		// on se remet dans l'etat PreconditionCheckerState
		this.activityScheduler.setState(this.state);
		
		// on envoi la requete NEXT sans qu'il y est d'etape ou de produit en
		// sortie ni meme un guide de type "Liste de controles"
		assertFalse("Requete NEXT envoyée a l'etat : pas de changement d'etat",
				this.state.manageRequest(reqNext));
		assertTrue(
				"L'etat actuel doit etre un PreConditionChecherState",
				this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);
		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);

		// on ajoute un produits en sortie de l'activite
		List<Product> lOutputProduct = new ArrayList<Product> ();
		lOutputProduct.add(new Product("", "outputProduct", null, null, "descr prod", null));
		this.activity.setOutputProducts(lOutputProduct);
		
		// on envoi la requete NEXT qui doit nous mettre dans l'etat PostConditionCheckerState
		assertTrue(
				"Requete NEXT envoyée a l'etat : changement d'etat.",
				this.state.manageRequest(reqNext));
		assertTrue(
				"L'etat actuel doit etre un PostConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);
		// on se remet dans l'etat PreconditionCheckerState
		this.activityScheduler.setState(this.state);
		
		
		// suppression des produits en sorties et ajout d'une liste de controles a l'activite 
		this.activity.setOutputProducts(new ArrayList<Product>());
		Guidance gCheckist = new Guidance("", "une liste de controles", null, null,
				"Liste de controles");
		this.activity.addGuidance(gCheckist);
		
		//on envoi la requete NEXT qui doit nous mettre dans l'etat PostConditionCheckerState
		assertTrue(
				"Requete NEXT envoyée a l'etat : changement d'etat.",
				this.state.manageRequest(reqNext));
		assertTrue(
				"L'etat actuel doit etre un PostConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);
		// on se remet dans l'etat PreconditionCheckerState
		this.activityScheduler.setState(this.state);
		
		
		// ajout d'une etape
		Step aStep = new Step("step1", "comment", new ArrayList<Product> ());
		this.activity.addStep(aStep);
		// on envoi la requete NEXT qui doit nous mettre dans l'etat StepState
		assertTrue(
				"Requete NEXT envoyée a l'etat : changement d'etat.",
				this.state.manageRequest(reqNext));
		assertTrue(
				"L'etat actuel doit etre un StepState",
				this.activityScheduler.getActivityState() instanceof StepState);
		// on se remet dans l'etat PreconditionCheckerState
		this.activityScheduler.setState(this.state);
		
		// on se remet dans l'etat PreconditionCheckerState
		this.activityScheduler.setState(this.state);
		
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

			assertTrue("L'etat devrait changer", this.activityScheduler
					.ManageRequest(request));
			assertTrue("L'etat devrait etre celui de tab[i]",
					this.activityScheduler.getActivityState() == arrState
							.get(i));
		}

		// avec ou sans steps dans l'activité, on passe ds tous les cas
		// dans un ActityPresentationState gràce au next
		// et le bouton previous est désactivé

		this.activityScheduler.setState(this.state);
		// on envoie un ensemble de requetes et on teste si elles ont changé
		// l'état de la machine
		assertFalse(
				"Requete CLOSE_PROCESS envoyée a l'etat, rien ne doit se passer",
				this.state.manageRequest(reqAleat));
		assertTrue("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);
		reqAleat = new Request(Request.RequestType.CLOSE_PROJECT);

		assertFalse(
				"Requete CLOSE_PROJECT envoyée a l'etat, rien ne doit se passer",
				this.state.manageRequest(reqAleat));
		assertTrue("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);

		reqAleat = new Request(Request.RequestType.TERMINATE_ACTIVITY);
		assertFalse(
				"Requete TERMINATE_ACTIVITY envoyée a l'etat, rien ne doit se passer",
				this.state.manageRequest(reqAleat));
		assertTrue("L'etat actuel doit etre un PreConditionCheckerState",
				this.activityScheduler.getActivityState() == this.state);

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
