/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.3 2005/11/06 15:53:44 yak Exp $
 */

package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;

/**
 * @author Alexandre Bes
 * 
 */
public class ActivityPresentationState extends ActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public ActivityPresentationState (ActivityScheduler activityScheduler,
			Activity activity)
	{
		super(activityScheduler, activity);
		
		// on affiche la presentation de l'activité
		this.activityScheduler.presentActivity();

		// on affiche le bouton next
		ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
				.setEnabled(true);

		// on affiche le bouton terminate
		ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
				.setEnabled(true);

		// on grise le bouton previous
		ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
				.setEnabled(false);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.ActivityState#previous()
	 */
	public void previous ()
	{
		// ne devrait jamais arriver
		System.err
				.println("ActivityPresentationState : normalement pas de previous possible");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.ActivityState#next()
	 */
	public void next ()
	{

		System.out.println("ActivityPresentationState : next() nbStep = "
				+ this.stepList.size());

		switch (this.stepList.size())
		{
			// s'il n'y a pas d'etape on passe directement au dernier etat
			case 0:

				if (this.activityScheduler.checkPostCondition())
				{
					// les préconditions sont vérifiées, on peu revinir a la
					// présentation
					// on change d'état pour null
					this.activityScheduler.setActivityState(null);
					// on effectue un terminante sur l'application manager
					ApplicationManager.getInstance().manageRequest(
							ApplicationManager.Request.TERMINATE_ACTIVITY);
				}
				break;

			// s'il y a une seule etape, elle doit avoir le comportement de la
			// derniere
			case 1:
				this.activityScheduler.setActivityState(new LastStepState(
						this.activityScheduler, this.activity));
				break;

			default:
				this.activityScheduler.setActivityState(new FirstStepState(
						this.activityScheduler, this.activity));
				break;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.ActivityState#terminate()
	 */
	public void terminate ()
	{
		// TODO Corps de méthode généré automatiquement

	}

}
