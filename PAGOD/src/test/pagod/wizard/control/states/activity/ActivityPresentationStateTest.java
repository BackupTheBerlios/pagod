/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationStateTest.java,v 1.3 2006/01/16 15:34:09 cyberal82 Exp $
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
import pagod.wizard.control.actions.NextAction;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;

/**
 * Classe de test qui permet de tester la classe ActivityPresentationState
 * 
 * @author Cyberal
 */
public class ActivityPresentationStateTest extends TestCase
{

	private Activity					activity;
	private ActivityScheduler			activityScheduler;
	private ActivityPresentationState	state;

	/**
	 * Methode appele avant chaque debut de test
	 */
	public void setUp ()
	{
		// création d'une activité vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
						new ArrayList<Activity>()));

		// creation d'un ActivityScheduler
		this.activityScheduler = new ActivityScheduler(this.activity);

		// creation de l'etat ActivityPresentationState
		this.state = new ActivityPresentationState(this.activityScheduler,
				this.activity);

		// on met l'ActivityScheduler dans l'etat ActivityPresentationState
		this.activityScheduler.setActivityState(this.state);
	}

	/**
	 * Methode appele a chaque fin de test
	 */
	public void tearDown ()
	{
		// suppression de l'activity
		this.activity = null;
	}

	/**
	 * Test de la methode manageRequest
	 */
	public void testManageRequest ()
	{
		/** ****** Test sur la requete NEXT ******* */

		// Creation d'une requete NEXT
		Request requestNext = new Request(Request.RequestType.NEXT);

		// l'activité n'as pas d'etape ni de produit en sortie donc il ne doit
		// pas y avoir de changement d'etat
		assertFalse(
				"L'etat ne devrait pas changer car l'activite n'a pas d'etape ni de produit en sortie",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type ActivityPresentationState (il ne devrait pas avoir changer)",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);

		// creation d'une liste d'etape
		List<Step> lStep = new ArrayList<Step>();
		lStep.add(new Step("", "step1", "", null));

		// ajout d'une etape a l'activité
		this.activity.setSteps(lStep);

		assertTrue("L'etat devrait changer car l'activie a une etape",
				this.state.manageRequest(requestNext));

		assertTrue("L'etat du scheduler devrait etre de type StepState",
				this.activityScheduler.getActivityState() instanceof StepState);

		// creation d'une liste de produit en sortie
		List<Product> lProductOutput = new ArrayList<Product>();
		lProductOutput.add(new Product("", "produit1", null, null, null));

		// ajout de produit en sortie de l'activité
		this.activity.setOutputProducts(lProductOutput);

		// on remet l'ActivityScheduler dans l'etat ActivityPresentationState
		// car il vient de changer
		this.activityScheduler.setActivityState(this.state);

		assertTrue(
				"L'etat devrait changer car l'activie a une etape et des produits en sortie",
				this.state.manageRequest(requestNext));

		assertTrue("L'etat du scheduler devrait etre de type StepState",
				this.activityScheduler.getActivityState() instanceof StepState);

		// on modifie l'activite pour qu'elle n'ait plus de step (elle a tjs un
		// produit en sortie)
		this.activity.setSteps(new ArrayList<Step>());

		assertTrue(
				"L'etat devrait changer car l'activite un produit en sortie (elle n'a pas d'etape)",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PostConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);

		/** ****** Test sur la requete PREVIOUS ******* */

		// Creation d'une requete NEXT
		Request requestPrevious = new Request(Request.RequestType.PREVIOUS);

		// on remet l'ActivityScheduler dans l'etat ActivityPresentationState
		// car il vient de changer
		this.activityScheduler.setActivityState(this.state);

		assertTrue(
				"L'etat de l'l'ActivityScheduler devrait etre de type",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

		// l'activite n'a pas de produit en entree donc un manageRequest de
		// PREVIOUS ne devrait pas changer d'etat
		assertFalse(
				"L'etat ne devrait pas changer car l'activite n'a pas de produit en entree",
				this.state.manageRequest(requestPrevious));

		assertTrue(
				"L'etat du scheduler devrait etre de type ActivityPresentationState (il ne devrait pas avoir changer)",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);

		// creation d'une liste de produit
		// creation d'une liste de produit en sortie
		List<Product> lProductInput = new ArrayList<Product>();
		lProductInput.add(new Product("", "produit1", null, null, null));

		// on ajoute a l'activite des produits en entree
		this.activity.setInputProducts(lProductInput);

		assertTrue(
				"L'etat devrait changer car l'activite a pas des produits en entrees",
				this.state.manageRequest(requestPrevious));

		assertTrue(
				"L'etat du scheduler devrait etre de type PreConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);

		/** ******* Test sur une requete GOTOSTEP ********** */

		// creation d'un ArrayList de step avec une etape
		ArrayList<Step> arrStep = new ArrayList<Step>();
		arrStep.add(new Step("Etape 1", null, new ArrayList<Product>()));

		// création d'une activité vide pour les tests
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

		// on test la requet GOTOSTEP
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

		/** ****** Test sur une requete quelconque ******* */

		// on se met dans le bon etat
		// creation de l'etat ActivityState
		this.state = new ActivityPresentationState(this.activityScheduler,
				this.activity);

		// on met l'ActivityScheduler dans l'etat ActivityPresentationState
		this.activityScheduler.setActivityState(this.state);

		// pour toutes les autres requetes l'etat ne devrait pas changer
		for (Request.RequestType aRequest : Request.RequestType.values())
		{

			// si la requete est previous ou next on passe a l'iteration
			// suivante car ces cas la on deja etaient teste
			if (aRequest == Request.RequestType.PREVIOUS
					|| aRequest == Request.RequestType.NEXT
					|| aRequest == Request.RequestType.GOTOSTEP) 
				continue;

			Request request = new Request(aRequest);

			assertTrue(
					"L'etat de l'l'ActivityScheduler devrait etre de type",
					this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

			assertFalse(
					"L'etat ne devrait pas changer car ce type de requete ne fait pas changer lorsqu'on est dans l'etat ActivityPresentationState",
					this.state.manageRequest(request));

			assertTrue(
					"L'etat du scheduler devrait etre de type ActivityPresentationState (il ne devrait pas avoir changer)",
					this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

			assertTrue(
					"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
					this.activityScheduler.getActivityState() == this.state);
		}
	}
}
