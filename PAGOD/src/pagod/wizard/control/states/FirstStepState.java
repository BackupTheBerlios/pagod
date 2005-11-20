/*
 * Projet PAGOD
 * 
 * $Id: FirstStepState.java,v 1.13 2005/11/20 23:26:43 psyko Exp $
 */
package pagod.wizard.control.states;

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
		//affichage de l'etape
		this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;
	}
}

