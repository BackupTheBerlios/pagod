/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationStateTest.java,v 1.7 2006/02/23 01:43:15 psyko Exp $
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
		// cr�ation d'une activit� vide pour les tests
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null, "descr role",
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

		// l'activit� n'as pas d'etape ni de produit en sortie ni de guide de type "Liste de controles" donc il ne doit
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

		// l'activit� n'as pas d'etape ni de produit en sortie mais elle a un
		// guide de type "liste de controles" donc il doit y avoir de changement
		// d'etat
		Guidance g = new Guidance("IdGuide1", "NomGuide1", null, null,
				"Liste de controles");
		this.activity.addGuidance(g);

		assertTrue(
				"L'etat devrait changer car l'activite a un guide de type liste de controles",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PostConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);

		// on supprime le guide et on remet l'activityScheduler dans l'etat
		// ActivityPresentationState
		this.activity.removeGuidance(g);
		this.activityScheduler.setActivityState(this.state);
		

		// l'activit� n'as pas d'etape ni de produit en sortie mais le role
		// associe a un guide de type "liste de controles" donc il doit y avoir
		// un changement d'etat
		this.activity.getRole().addGuidance(g);

		assertTrue(
				"L'etat devrait changer car le role associe a l'activite a un guide de type \"liste de controles\"",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PostConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);

		// on supprime le guide et on remet l'activityScheduler dans l'etat
		// ActivityPresentationState
		this.activity.getRole().removeGuidance(g);
		this.activityScheduler.setActivityState(this.state);
		

		// creation d'une liste d'etape
		List<Step> lStep = new ArrayList<Step>();
		lStep.add(new Step("", "step1", "", null));

		// ajout d'une etape a l'activit�
		this.activity.setSteps(lStep);

		assertTrue("L'etat devrait changer car l'activie a une etape",
				this.state.manageRequest(requestNext));

		assertTrue("L'etat du scheduler devrait etre de type StepState",
				this.activityScheduler.getActivityState() instanceof StepState);

		// creation d'une liste de produit en sortie
		List<Product> lProductOutput = new ArrayList<Product>();
		lProductOutput.add(new Product("", "produit1", null, null, "descr prod", null));

		// ajout de produit en sortie de l'activit�
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

		// on remet l'ActivityScheduler dans l'etat ActivityPresentationState
		// car il vient de changer
		this.activityScheduler.setActivityState(this.state);
		
		// on supprime de l'activite les etapes, les produits en sorties et les guides
		this.activity.setSteps(new ArrayList<Step>());
		this.activity.setOutputProducts(new ArrayList<Product>());
		this.activity.setGuidances(new ArrayList<Guidance>());
		
		/*********/
		// l'activite n'a pas de produit en entree et pas de guide d'un type
		// autre que "liste de controles" donc un manageRequest de
		// PREVIOUS ne devrait pas changer d'etat
		assertFalse(
				"L'etat ne devrait pas changer car l'activite n'a pas de produit en entree",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type ActivityPresentationState (il ne devrait pas avoir changer)",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);

		// ajout d'un guide d'un type autre que liste de controle
		Guidance aGuidance = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		this.activity.addGuidance(aGuidance);

		// l'activite n'a pas de produit en entree et un guide d'un type
		// autre que "liste de controles" donc un manageRequest de
		// PREVIOUS devrait changer d'etat
		assertTrue(
				"L'etat devrait changer car l'activite a un guide d'un type autre que liste de controle",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PreConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);

		// on supprime le guide et on remet l'activityScheduler dans l'etat
		// ActivityPresentationState
		this.activity.removeGuidance(aGuidance);
		this.activityScheduler.setActivityState(this.state);

		// creation d'une liste de produit en entree
		List<Product> lProductInput = new ArrayList<Product>();
		lProductInput.add(new Product("", "produit1", null, null, "descr prod", null));

		// on ajoute a l'activite des produits en entree
		this.activity.setInputProducts(lProductInput);

		assertTrue(
				"L'etat devrait changer car l'activite a des produits en entrees",
				this.state.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PreConditionCheckerState",
				this.activityScheduler.getActivityState() instanceof PreConditionCheckerState);
		
		// on remet l'ActivityScheduler dans l'etat ActivityPresentationState
		// car il vient de changer
		this.activityScheduler.setActivityState(this.state);
		
		/** ****** Test sur la requete PREVIOUS ******* */
		// Creation d'une requete PREVIOUS
		Request requestPrevious = new Request(Request.RequestType.PREVIOUS);
		
		// la requete PREVIOUS n'a aucun effet quand on est dans l'etat ActivityPresentationState
		assertFalse(
				"L'etat ne devrait pas changer",
				this.state.manageRequest(requestPrevious));

		assertTrue(
				"L'etat du scheduler devrait etre de type ActivityPresentationState (il ne devrait pas avoir changer)",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);

		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);
		
		
		/** ******* Test sur une requete GOTOSTEP ********** */

		// creation d'un ArrayList de step avec une etape
		ArrayList<Step> arrStep = new ArrayList<Step>();
		arrStep.add(new Step("Etape 1", null, new ArrayList<Product>()));

		// cr�ation d'une activit� vide pour les tests
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
					|| aRequest == Request.RequestType.GOTOSTEP) continue;

			Request request = new Request(aRequest);

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
