/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author fabfoot
 *
 */
public class PreConditionCheckerState extends AbstractActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public PreConditionCheckerState(ActivityScheduler activityScheduler, Activity activity)
    {
    	super(activityScheduler, activity);
           
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
     }
    
    /**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#toString()
	 */
	@Override
	public String toString ()
	{
		return(" Verification des PreConditions ");
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		this.activityScheduler.resetSplitPane();
        this.activityScheduler.checkBeforeStart();

	}

 }
