/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerState.java,v 1.7 2006/02/04 22:42:06 yak Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.Request;

/**
 * @author yak
 *
 */
public class PostConditionCheckerState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public PostConditionCheckerState (ActivityScheduler activityScheduler,Activity activity)
	{
		super(activityScheduler, activity);
   }


    
   
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		// TODO rajouter un message dans l'internationalisation
		return(LanguagesManager.getInstance().getString("postConditions"));
	}


	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.AbstractState#manageRequest(pagod.wizard.control.states.Request)
	 */
	public boolean manageRequest (Request request)
	{
		//AbstractActivityState temporaire
		AbstractActivityState state;
		
		//on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{
				
			case PREVIOUS:
				if (this.activity.hasSteps())
				{
					// on se positionne sur la derni?re ?tape
					state = new StepState(this.activityScheduler,this.activity,this.stepList.size()-1);
				}
				else
				{
					state = new ActivityPresentationState(this.activityScheduler,this.activity);
					
				}
				break;
				
			case GOTOSTEP:
				return super.manageRequest(request);
				
			default:
				return false;
		}
		this.activityScheduler.setState (state);
		return true;
		
	}

}
