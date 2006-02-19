/*
 * Projet PAGOD
 * 
 * $Id: ActivityState.java,v 1.2 2006/02/19 15:36:43 yak Exp $
 */
package test.pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.activity.AbstractActivityState;

/**
 * Cette classe a ete cree uniquement pour pouvoir tester la methode manageRequest
 * de la classe AbstractActivityState qui gere uniquement les requetes de type GOTOSTEP
 * 
 * @author Cyberal
 *
 */
public class ActivityState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public ActivityState (ActivityScheduler activityScheduler, Activity activity)
	{
		super(activityScheduler, activity);
		
	}


	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		
		return null;
	}


}
