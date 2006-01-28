/*
 * Projet PAGOD
 * 
 * $Id: ActivityLaunchedStateTest.java,v 1.2 2006/01/28 16:34:02 cyberal82 Exp $
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
 * @author yak
 *
 */
public class ActivityLaunchedStateTest extends TestCase
{
	
	/**
	 * l'objet a tester
	 */
	private ActivityLaunchedState	activityLaunchedState;
	/**
	 * l'activité concernée
	 */
	private Activity activity;
	
	
	/**
	 * contructeur du test
	 */
	public ActivityLaunchedStateTest ()
	{
		this.activity = new Activity("", "", null, null, new ArrayList<Step>(),
				new WorkDefinition("", "", null, null,
						new ArrayList<Activity>()), new ArrayList<Product>(),
				new ArrayList<Product>(), new Role("", "", null, null,
						new ArrayList<Activity>()));
		this.activityLaunchedState = new ActivityLaunchedState (ApplicationManager.getInstance(),this.activity);
	}
	/**
	 * Test method for 'pagod.wizard.control.states.application.ActivityLaunchedState.manageRequest(Request)'
	 */
	public void testManageRequest ()
	{
		//creation de la requete initiale
		Request request = new Request(
				Request.RequestType.OPEN_PROJECT);
		assertTrue("La requete close project doit retourner true", this.activityLaunchedState.manageRequest(request));
	
		
		//creation de la requete close project
		request = new Request(
				Request.RequestType.CLOSE_PROJECT);
		assertTrue("L'etat doit avoir change", this.activityLaunchedState
				.manageRequest(request));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type InitState",
				ApplicationManager.getInstance().getState() instanceof InitState);
		
		//creation de la requete
		request = new Request(
				Request.RequestType.TERMINATE_ACTIVITY);
		assertTrue("L'etat doit avoir change", this.activityLaunchedState
				.manageRequest(request));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type ProcessOpenedState",
				ApplicationManager.getInstance().getState() instanceof ProcessOpenedState);
		
		//pour toute les requete next previous on delegue à l'autre machne a etat ;)
		
		
	}


}
