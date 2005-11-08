/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.5 2005/11/08 17:31:25 yak Exp $
 */

package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author Alexandre Bes
 * 
 */
public class ActivityPresentationState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public ActivityPresentationState (ActivityScheduler activityScheduler,
			Activity activity)
	{
		super(activityScheduler, activity);

		// on affiche la presentation de l'activit?
		this.activityScheduler.presentActivity();

		// on affiche le bouton next
		ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
				.setEnabled(true);

		// on affiche le bouton terminate
		ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
				.setEnabled(true);

		if (this.activity.hasInputProducts())
		{
			// on active le bouton previous
			ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
					.setEnabled(true);
		}
		else
		{
			// on grise le bouton previous
			ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
					.setEnabled(false);
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.AbstractActivityState#previous()
	 */
	public void previous ()
	{
		// s'il y a des produits en entree on revient au panneau permettant de
		// voir les preconditions
		if (this.activity.hasInputProducts()) this.activityScheduler
				.setActivityState(new PreConditionCheckerState(
						this.activityScheduler, this.activity));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.AbstractActivityState#next()
	 */
	public void next ()
	{

		System.out.println("ActivityPresentationState : next() nbStep = "
				+ this.stepList.size());

		switch (this.stepList.size())
		{
			// s'il n'y a pas d'etape on passe directement au dernier etat
			case 0:

				
					this.activityScheduler.setActivityState(new PostConditionCheckerState(this.activityScheduler,this.activity));
					 
				
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
	 * @see pagod.wizard.control.states.AbstractActivityState#terminate()
	 */
	public void terminate ()
	{
		// TODO Corps de m?thode g?n?r? automatiquement

	}

}
