/*
 * Projet PAGOD
 * 
 * $Id: MiddleStepState.java,v 1.10 2005/11/18 19:15:04 psyko Exp $
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
public class MiddleStepState extends AbstractActivityState
{


    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public MiddleStepState(ActivityScheduler activityScheduler,
                           Activity activity, int iCurrentStep)
    {
        
        super(activityScheduler, activity, iCurrentStep);
        this.index = iCurrentStep;
    }

    /** 
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
        // si l'etape d'avant etait la premiere
        if (this.index == 1)
        {
            System.out.println("MiddleStep: previous() -> firststep") ;
            this.index--;
            this.activityScheduler.setActivityState(new FirstStepState(
                    this.activityScheduler, this.activity));
        }
        
        // si l'etape d'avant etait une middleStep
        if (this.index > 1)
        {
            System.out.println("MiddleStep: previous() -> middlestep") ;
            this.index--;
            this.activityScheduler.setActivityState(new MiddleStepState(
                    this.activityScheduler, this.activity, this.index));
        }

    }


    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
    public void next()
    {
        
        // s'il reste encore des middleStep
        if (this.index < (this.stepList.size() - 2))
        {
            System.out.println("MiddleStep: next() -> middlestep") ;
            this.index++;
            this.activityScheduler.setActivityState(new MiddleStepState(
                    this.activityScheduler, this.activity, this.index));
        }
        
        
        // si la prochaine est la derniere
        else if (this.index == (this.stepList.size() - 2))
        {
            
            System.out.println("MiddleStep: next() -> laststep") ;
            this.activityScheduler.setActivityState(new LastStepState(
                    this.activityScheduler, this.activity));
        }

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de m�thode g�n�r� automatiquement

    }
    
    /**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#toString()
	 */
	@Override
	public String toString ()
	{
		return(" Middle step " + this.index);
	}

	/**
	 *  (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractActivityState#display()
	 */
	public void display ()
	{
		System.out.println("MiddleStep : constructeur") ;
        

        System.out.println(this.index) ;
         
        // on affiche le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
                .setEnabled(true);

        // on affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
                .setEnabled(true);

        // on affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
                .setEnabled(true);
        
        // on affiche la combobox
        ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP)
                .setEnabled(false);
        
		
		//affichage de l'etape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size()) ;
        
        
	}
    
}
