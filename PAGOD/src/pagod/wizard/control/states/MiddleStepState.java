/*
 * Projet PAGOD
 * 
 * $Id: MiddleStepState.java,v 1.11 2005/11/20 23:26:43 psyko Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;


import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */
public class MiddleStepState extends AbstractStepState
{


    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public MiddleStepState(ActivityScheduler activityScheduler,
                           Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity, iCurrentStep);
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

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
		//affichage de l'etape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;

  }
    
}
