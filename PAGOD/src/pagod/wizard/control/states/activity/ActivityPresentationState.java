/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.5 2006/02/08 12:12:52 cyberal82 Exp $
 */

package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.Request;


/**
 * @author Alexandre Bes
 * 
 */
public class ActivityPresentationState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public ActivityPresentationState (ActivityScheduler activityScheduler,
			Activity activity)
	{
		super(activityScheduler, activity);
	}
	
	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		return(LanguagesManager.getInstance().getString("activityPresentation"));	
	}

	
	/** 
	 * @see pagod.wizard.control.states.AbstractState#manageRequest(pagod.wizard.control.states.Request)
	 */
	
	public boolean manageRequest (Request request)
	{
		//AbstractActivityState temporaire
		AbstractActivityState state;
		
		//on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{
			case NEXT:
				if (this.activity.hasSteps())
				{
					state = new StepState(this.activityScheduler,this.activity,0);
				}
				else if (this.activity.hasOutputProducts())
				{
					state = new PostConditionCheckerState (this.activityScheduler,this.activity);
				}
				else
				{
					return false;
				}
				break;	
				
			case PREVIOUS:
				if (this.activity.hasInputProducts())
				{
					state = new PreConditionCheckerState (this.activityScheduler,this.activity);
				}
				else
				{
					return false;
				}
				break;
				
			case GOTOSTEP:
				return super.manageRequest(request);
				
			default:
				return false;
		}
		
		if ( state == null )
		{
			System.err.println("state a null dans le manage request");
			return false;
		}
			
		
		this.activityScheduler.setState (state);
		return true;
		
	}

	
}
