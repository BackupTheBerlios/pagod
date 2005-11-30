/*
 * Projet PAGOD
 * 
 * $Id: ProjectOpenedState.java,v 1.4 2005/11/30 08:52:44 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

/**
 * 
 * @author yak & bob (contre attaque)
 * 
 */
public class ProjectOpenedState extends AbstractApplicationState
{

	/**
	 * @param applicationManager
	 */
	public ProjectOpenedState (ApplicationManager applicationManager)
	{
		super(applicationManager);
		// TODO Corps de constructeur g?n?r? automatiquement
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{

		AbstractApplicationState state;

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROJECT:
				// TODO A faire
				return false;

			case CLOSE_PROJECT:
				state = new InitState(this.applicationManager);
				break;
				
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
