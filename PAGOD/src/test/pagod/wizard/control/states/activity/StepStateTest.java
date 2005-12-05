/*
 * Projet PAGOD
 * 
 * $Id: StepStateTest.java,v 1.1 2005/12/05 14:10:20 biniou Exp $
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
import pagod.wizard.control.states.activity.StepState;
import junit.framework.TestCase;

/**
 * Classe de test qui permet de tester la classe StepState
 * 
 * @author biniou
 * 
 */
public class StepStateTest extends TestCase
{
	Activity			activity;
	ActivityScheduler	activityScheduler;
	StepState			state;

	/**
	 * Methode appelée avant chaque debut de test
	 * 
	 * @throws Exception
	 */
	protected void setUp () throws Exception
	{
		// creation d'une liste d'etape
		List<Step> lStep = new ArrayList<Step>();
		lStep.add(new Step("", "step1", "", null));

		// création d'une activité à une etape(minimum pour rencontrer
		// cet état) pour les tests
		this.activity = new Activity("", "", null, null, lStep,
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
						new ArrayList<Activity>()));

		// creation d'un ActivityScheduler
		this.activityScheduler = new ActivityScheduler(this.activity);

		// creation de l'etat StepState indicé a 0
		this.state = new StepState(this.activityScheduler, this.activity, 0);

		// on met l'ActivityScheduler dans l'etat ActivityPresentationState
		this.activityScheduler.setActivityState(this.state);
	}

	/**
	 * Methode appele a chaque fin de test
	 * 
	 * @throws Exception
	 */
	protected void tearDown () throws Exception
	{
		// suppression de l'activity
		this.activity = null;
	}

	/**
	 * Test method for
	 * 'pagod.wizard.control.states.activity.StepState.manageRequest(Request)'
	 */
	public void testManageRequest ()
	{
		/** ******* Test sur la requete NEXT ******** */
		// Creation d'une requete NEXT
		Request requestNext = new Request(Request.RequestType.NEXT);
		// l'etat chargé ne doit pas changé
		assertFalse("L'etat ne doit pas changer : pas de postcond", this.state
				.manageRequest(requestNext));
		assertTrue(
				"L'etat du scheduler devrait etre de type StepState (il ne devrait pas avoir changé)",
				this.activityScheduler.getActivityState() instanceof StepState);
		assertTrue(
				"L'etat du scheduler devrait etre celui contenu dans this.state (le meme objet)",
				this.activityScheduler.getActivityState() == this.state);

		// on refait un next() qui doit renvoyer sur l'etat postconditions
		// creation d'une liste de produit en sortie
		List<Product> lProductOutput = new ArrayList<Product>();
		lProductOutput.add(new Product("", "produit1", null, null, null));

		// ajout de produit en sortie de l'activité
		this.activity.setOutputProducts(lProductOutput);
		// l'etat chargé doit etre le postcond
		// puisque l'etape n'a qu'une seule activité
		assertTrue("L'etat doit changer : postcond", this.state
				.manageRequest(requestNext));

		assertTrue(
				"L'etat du scheduler devrait etre de type PostCond",
				this.activityScheduler.getActivityState() instanceof PostConditionCheckerState);

		// on rajoute 1 etape
		this.activityScheduler.setState(this.state);
		this.activity.addStep(new Step("", "step 2", "", null));
		assertTrue("L'etat ne doit pas changer : il y a encore une etape",
				this.state.manageRequest(requestNext));
		assertTrue(
				"L'etat du scheduler devrait etre de type StepState (il ne devrait pas avoir changé)",
				this.activityScheduler.getActivityState() instanceof StepState);
		// verification de l'index
		assertEquals(this.activityScheduler.getActivityState().getIndex(), 1);
		
		// on met l'etat a jour (pour l'index)
		this.state = (StepState) this.activityScheduler.getActivityState();

		/** ******* Test sur la requete PREVIOUS ******** */
		// Creation d'une requete NEXT
		Request requestPrevious = new Request(Request.RequestType.PREVIOUS);

		// l'etat ne doit pas changé mais l'index doit passé à 0
		assertTrue("L'etat ne doit pas changer : il y a une étape avant",
				this.state.manageRequest(requestPrevious));
		
//		 verification de l'index
		assertEquals(this.activityScheduler.getActivityState().getIndex(), 0);
		
		assertTrue(
				"L'etat du scheduler devrait etre de type StepState (il ne devrait pas avoir changé)",
				this.activityScheduler.getActivityState() instanceof StepState);
		
		
//		 on met l'etat a jour (pour l'index)
		this.state = (StepState) this.activityScheduler.getActivityState();
		
		// encore un previous : on doit passer a la presentation de l'activité
		assertTrue("L'etat doit changer : presentation",
				this.state.manageRequest(requestPrevious));
		
		assertTrue(
				"L'etat du scheduler devrait etre de type StepState (il ne devrait pas avoir changé)",
				this.activityScheduler.getActivityState() instanceof ActivityPresentationState);
		
	}

}
