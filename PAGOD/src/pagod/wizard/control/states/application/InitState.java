/*
 * Projet PAGOD
 * 
 * $Id: InitState.java,v 1.6 2006/02/19 13:11:05 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ApplicationManager;
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
