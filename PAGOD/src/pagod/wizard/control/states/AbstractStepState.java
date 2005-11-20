/*
 * Projet PAGOD
 * 
 * $Id: AbstractStepState.java,v 1.1 2005/11/20 23:26:43 psyko Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author psyko
 *
 */
public abstract class AbstractStepState extends AbstractActivityState
{
    /**
     * 
     * @param activityScheduler
     * @param activity
     */
    public AbstractStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);                
    }
	
    /**
     * 
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public AbstractStepState(ActivityScheduler activityScheduler, Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity);
        this.step = null;
        this.index = iCurrentStep;
    }

	/**
	 *  
	 * @return String , intitulé de la classe 
	 */
    public abstract String toString();
    
   
}
