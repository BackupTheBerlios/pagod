/*
 * Projet PAGOD
 * 
 * $Id: MiddleStepState.java,v 1.2 2005/11/05 14:28:16 biniou Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
//import pagod.common.model.Step;
//import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
//import pagod.wizard.control.Constants;
//import java.util.List;
//import java.util.ArrayList;

/**
 * @author Cyberal
 *
 */
public class MiddleStepState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public MiddleStepState(ActivityScheduler activityScheduler,
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
    public MiddleStepState(ActivityScheduler activityScheduler,
                           Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity, iCurrentStep);
        // TODO Corps de constructeur généré automatiquement
        
        
        /*List<Step> test = new ArrayList<Step> (); 
        test = this.activity.getSteps();*/
    }

    /* 
     * @param : aucun
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        // si l'etape d'avant etait la premiere
        if (this.index == 1)
        {
            this.index--;
            this.activityScheduler.setActivityState(new FirstStepState(
                    this.activityScheduler, this.activity, this.index));
        }
        
        // si l'etape d'avant etait une middleStep
        if (this.index > 1)
        {
            this.index--;
            this.activityScheduler.setActivityState(new MiddleStepState(
                    this.activityScheduler, this.activity, this.index));
        }

    }


    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
        
        // s'il reste encore des middleStep
        if (this.index < this.stepList.size() - 2)
        {
            this.index++;
            this.activityScheduler.setActivityState(new MiddleStepState(
                    this.activityScheduler, this.activity, this.index));
        }
        
        // si la prochaine est la derniere
        if (this.index == this.stepList.size() - 1)
        {
            this.index++;
            this.activityScheduler.setActivityState(new LastStepState(
                    this.activityScheduler, this.activity, this.index));
        }

    }

    /* (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

    }

}
