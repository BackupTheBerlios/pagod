/*
 * Projet PAGOD
 * 
 * $Id: InitState.java,v 1.4 2005/11/30 08:52:44 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.utils.ActionManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * etat Initial de l'application manager
 * 
 * @author fabfoot
 * 
 */
public class InitState extends AbstractApplicationState
{

	/**
	 * @param applicationManager
	 */
	public InitState (ApplicationManager applicationManager)
	{
		super(applicationManager);
		
		System.err.println("on passe en initState");
		
		//on desactive les actions
		ActionManager.getInstance().getAction(
				Constants.ACTION_RUN_ACTIVITY)
				.setEnabled(false);
		ActionManager.getInstance().getAction(
				Constants.ACTION_NEXT)
				.setEnabled(false);
		ActionManager.getInstance().getAction(
				Constants.ACTION_PREVIOUS).setEnabled(
				false);
		ActionManager.getInstance().getAction(
				Constants.ACTION_TERMINATE).setEnabled(
				false);
		ActionManager.getInstance().getAction(
				Constants.ACTION_GOTOSTEP).setEnabled(
				false);
		ActionManager.getInstance().getAction(
				Constants.ACTION_TOOLSSETTINGS)
				.setEnabled(false);

	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{
		// AbstractApplicationState temporaire
		AbstractApplicationState state;

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROJECT:
				state = new ProjectOpenedState(this.applicationManager);
				break;

			default:
				return false;
		}
		this.applicationManager.setState(state);
		return true;

	}

}
