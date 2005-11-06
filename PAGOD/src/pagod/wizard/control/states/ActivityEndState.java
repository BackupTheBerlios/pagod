/*
 * Projet PAGOD
 * 
 * $Id: ActivityEndState.java,v 1.2 2005/11/06 13:24:50 yak Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;

/**
 * @author yak
 *
 */
public class ActivityEndState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public ActivityEndState(ActivityScheduler activityScheduler,
                            Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur généré automatiquement
    }

    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public ActivityEndState(ActivityScheduler activityScheduler,
                            Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity, iCurrentStep);
        //on test les postconditions
        //this.activityScheduler.postCondTest)
        if (true)
        {

            this.terminate();
            
        }
        else
        {
            this.previous();
        }
        
        
        
    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        //on teste le nombre step
        switch (this.stepList.size())
        {
            //si on est il n'y a pas de step on revient a l'acitivité presentation
            case 0: 
                this.activityScheduler.setActivityState(new ActivityPresentationState(
                        this.activityScheduler, this.activity));
                break;
            //sinon on revient au dernier step
            default:
                this.activityScheduler.setActivityState(new LastStepState(
                        this.activityScheduler, this.activity));
                break;
               
        
        }

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
        // TODO Supprimer ceci est un test
        System.err
        .println("ActivityEndState : pas de next possible sur cet etat");

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        //les préconditions sont vérifiées, on peu revinir a la présentation
       //on change d'état pour null
        this.activityScheduler.setActivityState(null);
        //on effectue un terminante sur l'application manager
        ApplicationManager.getInstance().manageRequest( ApplicationManager.Request.TERMINATE_ACTIVITY);
    }

}
