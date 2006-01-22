/*
 * Projet PAGOD
 * 
 * $Id: ProjectOpenedStateTest.java,v 1.1 2006/01/22 08:36:52 biniou Exp $
 */
package test.pagod.wizard.control.states.application;


import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.application.InitState;
import pagod.wizard.control.states.application.ProcessOpenedState;
import pagod.wizard.control.states.application.ProjectOpenedState;
import junit.framework.TestCase;

/**
 * Classe permettant de tester la classe ProjectOpenedState
 * 
 * @author biniou
 */
public class ProjectOpenedStateTest extends TestCase
{

	/**
	 * l'objet a tester
	 */
	private ProjectOpenedState	projectOpenedState;
	
	/**
	 * Constructeur de la classe de test
	 * 
	 */
	public ProjectOpenedStateTest ()
	{
		this.projectOpenedState = new ProjectOpenedState(ApplicationManager
				.getInstance());
	}
	
	
	
	/**
	 * Methode permettant de tester la methode manageRequest
	 * Test method for 'pagod.wizard.control.states.application.ProjectOpenedState.manageRequest(Request)'
	 */

	public void testManageRequest ()
	{
		/************* GESTION D'UNE REQUETE CLOSE_PROJECT *************/ 
		
		// Creation d'une requete CLOSE_PROJECT
		Request requestCloseProject = new Request(
				Request.RequestType.CLOSE_PROJECT);

		assertTrue("L'etat doit avoir change", this.projectOpenedState
				.manageRequest(requestCloseProject));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type InitState",
				ApplicationManager.getInstance().getState() instanceof InitState);
		
//		 Creation d'une requete OPEN_PROCESS
		Request requestOpenProcess = new Request(
				Request.RequestType.OPEN_PROCESS);

		assertTrue("L'etat doit avoir change", this.projectOpenedState
				.manageRequest(requestOpenProcess));
		assertTrue(
				"L'etat de l'applicationManager doit etre de type ProcessOpenedState",
				ApplicationManager.getInstance().getState() instanceof ProcessOpenedState);
	}



}
