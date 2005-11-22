/*
 * Projet PAGOD
 * 
 * $Id: MiddleStepState.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */
package pagod.wizard.control.states.activity;

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
		System.out.println("stateListSize " + this.activityScheduler.getStateList().size());
		//affichage de l'etape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;

  }
    
}
