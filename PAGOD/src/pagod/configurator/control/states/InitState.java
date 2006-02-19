/*
 * Projet PAGOD
 * 
 * $Id: InitState.java,v 1.1 2006/02/19 15:36:04 yak Exp $
 */
package pagod.configurator.control.states;

import pagod.configurator.control.ApplicationManager;


/**
 * @author yak
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
	 * (non-Javadoc)
	 *  methode quigere la requete envoye a l'etat
	 * @param request
	 * @return retourne vrai si le changement d'etat a etait fait faux sinon
	 * @see pagod.configurator.control.states.AbstractApplicationState#manageRequest(pagod.configurator.control.states.Request)
	 */
	public boolean manageRequest (Request request)
	{
		// AbstractApplicationState temporaire
		AbstractApplicationState state;

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROCESS:
				state = new ProcessOpenedState(this.applicationManager);
				break;

			default:
				return false;
		}
		this.applicationManager.setState(state);
		return true;

	}

}
