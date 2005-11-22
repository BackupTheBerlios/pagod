/*
 * Projet PAGOD
 * 
 * $Id: StepFactory.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */
package pagod.wizard.control.states.activity;

import java.util.List;
import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author psyko
 *
 */
public class StepFactory
{
	/**
	 * @param activityScheduler
	 * @param activity
	 * @param stateList
	 * @param index
	 * factory qui va g?n?rer automatiquement l'?tat dans lequel on se trouve
	 */
	public StepFactory(ActivityScheduler activityScheduler, Activity activity, 
			List<AbstractActivityState> stateList, int index)
	{
		activityScheduler.setCurrentActivityState(index);
		activityScheduler.setActivityState(stateList.get(index));
		activityScheduler.getActivityState().display();
		activityScheduler.initComboBox();
		
		//selon l'index, on affiche ou non les boutons next et previous, mais dans tous les cas
		// le bouton terminate et la combo
		
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(true);
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(true);
        if(activityScheduler.getCurrentActivityState()==0)
        	ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(false);
        if(activityScheduler.getCurrentActivityState()== stateList.size()-1)
        	ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(false);
        
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE).setEnabled(true);
        ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP).setEnabled(true);  
		
		
	}
	
}
