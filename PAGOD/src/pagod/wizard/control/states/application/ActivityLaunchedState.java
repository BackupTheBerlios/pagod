/*
 * Projet PAGOD
 * 
 * $Id: ActivityLaunchedState.java,v 1.4 2005/11/30 08:52:44 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

/**
 * @author yak
 * 
 */
public class ActivityLaunchedState extends AbstractApplicationState
{

	/**
	 * D?rouleur d'activit?
	 */
	private ActivityScheduler	activityScheduler;

	/**
	 * @param applicationManager
	 * @param activity 
	 */
	public ActivityLaunchedState (ApplicationManager applicationManager,
			Activity activity)
	{
		super(applicationManager);

		this.activityScheduler = new ActivityScheduler(activity);

		// on passe a la MainFrame l'ActivityScheduler pour qu'elle
		// puisse s'enregistrer comme observer de l'ActivityScheduler
		//TODO a changer
		applicationManager.notifyMainFrame(this.activityScheduler);
		
		// TODO solution temporaire
		this.activityScheduler.setState(this.activityScheduler.getState(0));

	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{

		System.out.println("ActivityLaunchedState.manageRequest :" + request);
		AbstractApplicationState state;

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROJECT:
				// TODO A faire
				return false;

			case CLOSE_PROJECT:
				// TODO il y a ptetre des trucs a réinit
				state = new InitState(this.applicationManager);
				break;

			case OPEN_PROCESS:
				// TODO voir si on ne reviens pas en arriere a ce moment la
				return false;
				
			case TERMINATE_ACTIVITY:
				this.activityScheduler = null;
				state = new ProcessOpenedState (this.applicationManager);
				break;

			// si la requet request est inconnue on la délegue a l'activity
			// scheduler qui saura peut etre la traiter
			default:
				
				boolean bReturn = this.activityScheduler.ManageRequest(request);
				this.applicationManager.setState(this);
				return bReturn;

		}
		this.applicationManager.setState(state);
		return true;
	}

}
