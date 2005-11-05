/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.3 2005/11/05 15:01:50 cyberal82 Exp $
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
        // TODO Corps de constructeur g�n�r� automatiquement
    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        // TODO Corps de m�thode g�n�r� automatiquement

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
        // TODO Corps de m�thode g�n�r� automatiquement
        System.out.println("LastStepState : next()");
    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de m�thode g�n�r� automatiquement

    }

}
