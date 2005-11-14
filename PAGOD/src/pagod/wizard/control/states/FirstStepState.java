/*
 * Projet PAGOD
 * 
 * $Id: FirstStepState.java,v 1.8 2005/11/14 23:59:20 psyko Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author Cyberal
 *
 */
public class FirstStepState extends AbstractActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public FirstStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        
        // initialisation de l'index
        this.index = 0;
        this.activityScheduler.fillDirectAccessComboBox();
        this.activityScheduler.autoComboSelect(this.index+2);
        System.out.println("FirstStepState");
        
        // on affiche le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
                .setEnabled(true);

        // on affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
                .setEnabled(true);

        // on affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
                .setEnabled(true);
        
        // affichage de l'etape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
        // on renvoie a la presentation d'activite
        this.activityScheduler.setActivityState(new ActivityPresentationState(
                this.activityScheduler, this.activity));

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
      public void next()
    {
        // on affiche la prochaine etape
        // s'il n'y en a que 2 on affiche la derniere, si plus une du milieu
        // sinon c'est une erreur
        
        switch (this.stepList.size() )
        {
            case 1: System.err.println("FirstStepState : Le cas d'une seule etape est gere par LastStepState.");
                    break;
            
            case 2: System.out.println("Methode NEXT() du firstStep => LastStep");
                    this.activityScheduler.setActivityState(new LastStepState(
                    this.activityScheduler, this.activity));
                    break;
            
            default:   
                    System.out.println("FirstStep; next() -> MiddleStep");
                    this.activityScheduler.setActivityState(new MiddleStepState(
                    this.activityScheduler, this.activity, this.index+1));
                    break;
        }

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

    }

}
