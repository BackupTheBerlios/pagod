/*
 * Projet PAGOD
 * 
 * $Id: ProcessOpenedState.java,v 1.10 2006/02/12 17:02:27 cyberal82 Exp $
 */
package pagod.wizard.control.states.application;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

/**
 * Etat de l'ApplicationManager lrosque un project est ouvert et que le
 * processus est ouvert aussi
 * 
 * @author yak & bob contre attaque le retour
 */
public class ProcessOpenedState extends AbstractApplicationState
{

	/**
	 * @param applicationManager
	 */
	public ProcessOpenedState (ApplicationManager applicationManager)
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

		AbstractApplicationState state;
		System.err.println("ProcessOpened.manageRequest :" + request);
		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{

			case OPEN_PROJECT:
				state = new ProjectOpenedState(this.applicationManager);
				break;

			case CLOSE_PROJECT:
				// TODO il y a ptetre des trucs a r?init
				state = new InitState(this.applicationManager);
				break;

			case OPEN_PROCESS:
				// qd on est dans l'etat ProcessOpenedState la requete
				// OPEN_PROCESS ne change pas l'etat
				return false;

			case RUN_ACTIVITY:
				// s'il y a un processus
				if (ApplicationManager.getInstance().getCurrentProject() != null)
				{
					// on creer un nouvelle etat activit? en passant en
					// parametre
					// l'actvit? r?cup?r? a partir
					// de la requete
					// cf RunActivityAction.actionPerformed
					// on lance le timer
					// on recupere le num?ro de l'it
					Activity aTemp = (Activity) request.getContent();
					int iCurrentIt = ApplicationManager.getInstance()
							.getCurrentProject().getItCurrent();
					TimeCouple tcTemp = aTemp.gethmTime(iCurrentIt);

					// on demarre le manager
					TimerManager.getInstance().start(tcTemp.getTimeElapsed(),
							tcTemp.getTimeRemaining());
				}
				state = new ActivityLaunchedState(this.applicationManager,
						(Activity) request.getContent());
				break;

			default:
				return false;
		}
		this.applicationManager.setState(state);
		return true;
	}

}
