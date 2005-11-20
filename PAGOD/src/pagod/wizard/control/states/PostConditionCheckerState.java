/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerState.java,v 1.8 2005/11/20 23:26:43 psyko Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

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
   }


	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#terminate()
	 */
	public void terminate ()
	{
	}
	
    
    /**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		return(" Verification des PostConditions ");
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		this.activityScheduler.resetSplitPane();
		this.activityScheduler.checkBeforeEnd();

	}

}
