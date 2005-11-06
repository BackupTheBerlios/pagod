/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.2 2005/11/06 14:55:46 fabfoot Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author fabfoot
 *
 */
public class PreConditionCheckerState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public PreConditionCheckerState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur généré automatiquement
        System.out.println("precond");
        // on affiche le bouton next
        
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
                .setEnabled(true);

        // on affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
                .setEnabled(true);

        // on affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
                .setEnabled(false);
    }

    @Override
    public void previous()
    {
        // TODO Corps de méthode généré automatiquement
     
    }

    @Override
    public void next()
    {
        // TODO Corps de méthode généré automatiquement
        this.activityScheduler.setActivityState(new ActivityPresentationState(
                this.activityScheduler, this.activity));
    }

    @Override
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement
        
    }

}
