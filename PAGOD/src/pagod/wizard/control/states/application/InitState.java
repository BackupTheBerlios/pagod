/*
 * Projet PAGOD
 * 
 * $Id: InitState.java,v 1.5 2005/11/30 12:21:17 cyberal82 Exp $
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
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a chang? d'?tat faut sinon
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
