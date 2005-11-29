/*
 * Projet PAGOD
 * 
 * $Id: ActivityLaunchedState.java,v 1.3 2005/11/29 18:11:15 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

public class ActivityLaunchedState extends AbstractApplicationState 
{

	/**
	 * @param applicationManager
	 */
	public ActivityLaunchedState (ApplicationManager applicationManager)
	{
		super(applicationManager );
		// TODO Corps de constructeur g?n?r? automatiquement
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{
		
		return false;
	}

}
