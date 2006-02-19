/*
 * Projet PAGOD
 * 
 * $Id: ProcessOpenedState.java,v 1.1 2006/02/19 15:36:04 yak Exp $
 */
package pagod.configurator.control.states;

import pagod.configurator.control.ApplicationManager;

/**
 * @author yak
 * 
 */
public class ProcessOpenedState extends AbstractApplicationState
{

	/**
	 * @param applicationManager
	 */
	public ProcessOpenedState (ApplicationManager applicationManager)
	{
		super(applicationManager);
		// TODO Corps de constructeur généré automatiquement
	}

	/**
	 * (non-Javadoc)
	 * 
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
