/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.5 2005/11/08 17:31:24 yak Exp $
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
public class PreConditionCheckerState extends AbstractActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public PreConditionCheckerState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur g?n?r? automatiquement
        System.out.println("precond");
        this.activityScheduler.checkBeforeStart();
        
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

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
        // TODO Corps de m?thode g?n?r? automatiquement
     
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
    public void next()
    {
        // TODO Corps de m?thode g?n?r? automatiquement
        this.activityScheduler.setActivityState(new ActivityPresentationState(
                this.activityScheduler, this.activity));
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de m?thode g?n?r? automatiquement
        
    }

}
