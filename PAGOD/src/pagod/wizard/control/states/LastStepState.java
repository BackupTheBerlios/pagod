/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.13 2005/11/21 19:15:50 psyko Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */

public class LastStepState extends AbstractStepState
{
    /**
     * @param activityScheduler
     * @param activity
     */
    public LastStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        this.index = this.stepList.size() -1;
        System.out.println("Constructeur de LastStepState");
        
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
	@Override
	public String toString ()
	{
		return this.getStepList().get(this.index).getName();
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		this.activityScheduler.resetSplitPane();
        // affichage de l'?tape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
	}
	
 }
