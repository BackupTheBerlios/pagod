/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerState.java,v 1.2 2005/11/13 15:45:56 cyberal82 Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author yak
 *
 */
public class PostConditionCheckerState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public PostConditionCheckerState (ActivityScheduler activityScheduler,
			Activity activity)
	{
		super(activityScheduler, activity);
		
		System.out.println("PostconditionCheckerState : constructeur");
		this.activityScheduler.resetSplitPane();
		this.activityScheduler.checkBeforeEnd();
		// on masque le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
                .setEnabled(false);

        // on affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
                .setEnabled(true);

        //on affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
                .setEnabled(true);
	}


	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#previous()
	 */
	@Override
	public void previous ()
	{
		System.out.println("PostConditionCheckerState-> Previous");
		if (this.stepList.size() == 0)
		{
			this.activityScheduler.setActivityState(new ActivityPresentationState(this.activityScheduler,this.activity));
		}
		else
		{

			this.activityScheduler.setActivityState(new LastStepState(this.activityScheduler,this.activity));
		}
			

	}

	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#next()
	 */
	@Override
	public void next ()
	{
		

	}

	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#terminate()
	 */
	@Override
	public void terminate ()
	{
	}

}