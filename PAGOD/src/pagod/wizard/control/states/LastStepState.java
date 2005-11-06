/*
 * Projet PAGOD
 * 
 * $Id: LastStepState.java,v 1.5 2005/11/06 15:53:44 yak Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;

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
        
        System.out.println("Constructeur de LastStepState");
        
        // initialisation de l'index
        this.index = this.stepList.size() - 1;
        System.out.println(this.index);
        
        // On affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(true);
        
        // On affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE).setEnabled(true);
                
        // on d�sactive le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(true);
        
        // affichage de l'�tape
        this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
        
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        switch(this.stepList.size())
        {
            // S' il n'y avait qu'une �tape, on renvoie � la pr�sentation de l'activit� 
            case 1: System.out.println("Methode PREVIOUS () du lastStep => ActivityPresentation");
                    this.activityScheduler.setActivityState(new ActivityPresentationState(this.activityScheduler, this.activity));
                    break;
          
            // S'il y avait deux �tapes, on revient � la firstStep
            case 2: System.out.println("Methode PREVIOUS () du lastStep => FirstStep");
                    this.activityScheduler.setActivityState(new FirstStepState(this.activityScheduler, this.activity));
                    break;
            // Les autres cas, on remonte d'une �tape    
            default:   
                    System.out.println("M�thode PREVIOUS() du lastStep-> MiddleStep");
                    this.activityScheduler.setActivityState(new MiddleStepState(this.activityScheduler, this.activity, this.index-1));
                    break;
        }
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
    	if (this.activityScheduler.checkPostCondition())
		{
			// les pr�conditions sont v�rifi�es, on peu revinir a la
			// pr�sentation
			// on change d'�tat pour null
			this.activityScheduler.setActivityState(null);
			// on effectue un terminante sur l'application manager
			ApplicationManager.getInstance().manageRequest(
					ApplicationManager.Request.TERMINATE_ACTIVITY);
		}

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // TODO Corps de m�thode g�n�r� automatiquement

    }

}