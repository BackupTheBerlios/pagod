/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.7 2005/11/14 23:59:20 psyko Exp $
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

public class LastStepState extends AbstractActivityState
{
    /**
     * @param activityScheduler
     * @param activity
     */
    public LastStepState(ActivityScheduler activityScheduler, Activity activity)
    {
        super(activityScheduler, activity);
        
        System.out.println("Constructeur de LastStepState");
        
        // initialisation de l'index
        this.index = this.stepList.size() - 1;
        System.out.println(this.index);
        this.activityScheduler.fillDirectAccessComboBox();
        this.activityScheduler.autoComboSelect(this.index+2);
        
        // On affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(true);
        
        // On affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE).setEnabled(true);
                
        // on désactive le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(true);
        
        // affichage de l'étape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
        
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
        switch(this.stepList.size())
        {
            // S' il n'y avait qu'une étape, on renvoie à la présentation de l'activité 
            case 1: System.out.println("Methode PREVIOUS () du lastStep => ActivityPresentation");
                    this.activityScheduler.setActivityState(new ActivityPresentationState(this.activityScheduler, this.activity));
                    break;
          
            // S'il y avait deux étapes, on revient à la firstStep
            case 2: System.out.println("Methode PREVIOUS () du lastStep => FirstStep");
                    this.activityScheduler.setActivityState(new FirstStepState(this.activityScheduler, this.activity));
                    break;
            // Les autres cas, on remonte d'une étape    
            default:   
                    System.out.println("Méthode PREVIOUS() du lastStep-> MiddleStep");
                    this.activityScheduler.setActivityState(new MiddleStepState(this.activityScheduler, this.activity, this.index-1));
                    break;
        }
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
    public void next()
    {
    	this.activityScheduler.setActivityState(new PostConditionCheckerState(this.activityScheduler,this.activity));
		

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de méthode généré automatiquement

    }

}
