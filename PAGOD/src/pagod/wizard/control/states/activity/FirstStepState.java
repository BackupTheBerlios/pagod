/*
 * Projet PAGOD
 * 
 * $Id: FirstStepState.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */
public class FirstStepState extends AbstractActivityState
{
	
	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public FirstStepState(ActivityScheduler activityScheduler, Activity activity)
	{
		super(activityScheduler, activity);
		
		// initialisation de l'index
		this.index = 0;
	}
	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#terminate()
	 */
	public void terminate()
	{
		// TODO Corps de m?thode g?n?r? automatiquement
		
	}

    /**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		return this.getStepList().get(0).getName();
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		this.activityScheduler.resetSplitPane();
		//affichage de l'etape
		this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;
	}
}

