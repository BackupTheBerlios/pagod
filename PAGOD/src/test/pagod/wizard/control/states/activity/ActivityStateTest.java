/*
 * Projet PAGOD
 * 
 * $Id: ActivityStateTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
 */
package test.pagod.wizard.control.states.activity;

import java.util.ArrayList;

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
 * @author Cyberal
 * 
 */
public class ActivityStateTest extends TestCase
{
	private Activity			activity;
	private ActivityScheduler	activityScheduler;
	private ActivityState		state;

	/**
	 * Methode appele avant chaque debut de test
	 */
	public void setUp ()
	{
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
		ArrayList<AbstractActivityState> arrState = new ArrayList<AbstractActivityState>();

//		 initialisation de l'ArrayList
		arrState.add(new PreConditionCheckerState(this.activityScheduler,
				this.activity));
		arrState.add(new ActivityPresentationState(this.activityScheduler,
				this.activity));
		arrState.add(new StepState(this.activityScheduler, this.activity, 0));
		arrState.add(new PostConditionCheckerState(this.activityScheduler,
				this.activity));

		// on test la requet GOTOSTEP
		for (int i = 0; i < arrState.size(); i++)
		{
			// on se met dans le bon etat
			// creation de l'etat ActivityState
			this.state = new ActivityState(this.activityScheduler,
					this.activity);

			// on met l'ActivityScheduler dans l'etat ActivityPresentationState
			this.activityScheduler.setActivityState(this.state);

			// Creation d'une requete GOTOSTEP
			Request request = new Request(Request.RequestType.GOTOSTEP,
					arrState.get(i));

			assertTrue("L'etat devrait changer", this.activityScheduler
					.ManageRequest(request));
			assertTrue("L'etat devrait etre celui de tab[i]",
					this.activityScheduler.getActivityState() == arrState.get(i));
		}

		// on se met dans le bon etat
		// creation de l'etat ActivityState
		this.state = new ActivityState(this.activityScheduler, this.activity);

		// on met l'ActivityScheduler dans l'etat ActivityPresentationState
		this.activityScheduler.setActivityState(this.state);

		// pour toutes les autres requetes l'etat ne devrait pas changer
		for (Request.RequestType aRequest : Request.RequestType.values())
		{
			// si la requete est de type GOTOSTEP on passe a l'iteration
			// suivante car on a deja teste ce type de requete
			if (aRequest == Request.RequestType.GOTOSTEP) continue;

			Request request = new Request(aRequest);

			assertFalse(
					"L'etat ne devrait pas changer car ce type de requete ne fait pas changer lorsqu'on est dans l'etat ActivityPresentationState",
					this.state.manageRequest(request));

			assertTrue(
					"L'etat du scheduler devrait etre de type ActivityState (il ne devrait pas avoir changer)",
					this.activityScheduler.getActivityState() instanceof ActivityState);

			assertTrue(
					"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
					this.activityScheduler.getActivityState() == this.state);
		}
	}
}
