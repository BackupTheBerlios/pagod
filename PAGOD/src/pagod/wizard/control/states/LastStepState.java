/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.11 2005/11/18 19:15:04 psyko Exp $
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
        
        
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
        switch(this.stepList.size())
        {
            // S' il n'y avait qu'une ?tape, on renvoie ? la pr?sentation de l'activit? 
            case 1: System.out.println("Methode PREVIOUS () du lastStep => ActivityPresentation");

                    this.activityScheduler.setActivityState(
                    		new ActivityPresentationState(this.activityScheduler, this.activity));

                    this.activityScheduler.setActivityState(new ActivityPresentationState(this.activityScheduler, this.activity));

                    break;
          
            // S'il y avait deux ?tapes, on revient ? la firstStep
            case 2: System.out.println("Methode PREVIOUS () du lastStep => FirstStep");
                    this.activityScheduler.setActivityState(new FirstStepState(this.activityScheduler, this.activity));
                    break;
            // Les autres cas, on remonte d'une ?tape    
            default:   
                    System.out.println("M?thode PREVIOUS() du lastStep-> MiddleStep");
                    this.activityScheduler.setActivityState(new MiddleStepState(this.activityScheduler, this.activity, this.index-1));
                    break;
        }
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
    public void next()
    {
    	this.activityScheduler.setActivityState(new PostConditionCheckerState(
    			this.activityScheduler,this.activity));
		

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
		return(" Last step ");
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		System.out.println(this.index);
        
        // On affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(true);
        
        // On affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE).setEnabled(true);
                
        // on d?sactive le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(true);
        
        // on affiche la combobox
        ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP)
                .setEnabled(true);        

        
        // affichage de l'?tape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
//		 affichage de l'étape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
	}
	
 }
