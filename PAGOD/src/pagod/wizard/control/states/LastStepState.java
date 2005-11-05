/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.2 2005/11/05 12:45:03 cyberal82 Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */
public class LastStepState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public LastStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur généré automatiquement
    }

    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public LastStepState(ActivityScheduler activityScheduler,
                         Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity, iCurrentStep);
        // TODO Corps de constructeur généré automatiquement
    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        // TODO Corps de méthode généré automatiquement

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
        // TODO Corps de méthode généré automatiquement
        System.out.println("LastStepState : next()");
    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

    }

}
