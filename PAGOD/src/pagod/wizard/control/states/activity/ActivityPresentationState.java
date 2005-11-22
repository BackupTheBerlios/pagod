/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */

package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;


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
	
	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#toString()
	 */
	@Override
	public String toString ()
	{
		return(" Presentation de l'activite ");
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		this.activityScheduler.resetSplitPane();
		// on affiche la presentation de l'activit?
		this.activityScheduler.presentActivityAndProduct();
		
	}

	
}
