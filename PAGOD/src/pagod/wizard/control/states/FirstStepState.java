/*
 * Projet PAGOD
 * 
 * $Id: FirstStepState.java,v 1.1 2005/11/04 18:10:13 cyberal82 Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */
public class FirstStepState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public FirstStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur généré automatiquement
    }

    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public FirstStepState(ActivityScheduler activityScheduler,
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

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

    }

}
