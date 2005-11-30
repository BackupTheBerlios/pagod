/*
 * Projet PAGOD
 * 
 * $Id: ProcessOpenedState.java,v 1.4 2005/11/30 08:52:44 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * Etat de l'ApplicationManager lrosque un project est ouvert et que le processus est ouvert aussi
 * @author yak & bob contre attaque le retour 
 */
public class ProcessOpenedState extends AbstractApplicationState 
{

	/**
	 * @param applicationManager
	 */
	public ProcessOpenedState (ApplicationManager  applicationManager)
	{
		super(applicationManager );
		
		ActionManager.getInstance().getAction(
				Constants.ACTION_RUN_ACTIVITY)
				.setEnabled(false);
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{
		
		AbstractApplicationState state;
		System.err.println("ProcessOpened.manageRequest :"+request);
		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROJECT:
				// TODO A faire
				return false;

			case CLOSE_PROJECT:
				//TODO il y a ptetre des trucs a réinit
				state = new InitState(this.applicationManager);
				break;
				
			case OPEN_PROCESS:
				//TODO voir si on ne reviens pas en arriere a ce moment la
				return false;
			
			case RUN_ACTIVITY:
				System.err.println("ici");
				//on creer un nouvelle etat activité en passant en parametre l'actvité récupéré a partir 
				// de la requete
				//cf RunActivityAction.actionPerformed
				state = new ActivityLaunchedState(this.applicationManager,(Activity)request.getContent());
				break;
				
			default:
				return false;
		}
		this.applicationManager.setState(state);
		return true;
	}

}
