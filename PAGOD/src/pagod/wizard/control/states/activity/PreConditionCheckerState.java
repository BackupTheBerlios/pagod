/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.6 2006/02/18 16:35:53 cyberal82 Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.Request;
import static pagod.wizard.control.states.Request.RequestType.GOTOSTEP;
import static pagod.wizard.control.states.Request.RequestType.NEXT;
import static pagod.wizard.control.states.Request.RequestType.PREVIOUS;

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
    }

	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		return(LanguagesManager.getInstance().getString("preConditions"));
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
			case NEXT:
				if (this.activity.hasSteps())
				{
					state = new StepState(this.activityScheduler,
							this.activity, 0);
				}
				else if (this.activity.hasOutputProducts()
						|| this.activity.hasGuidanceType("Liste de controles")
						|| this.activity.getRole().hasGuidanceType(
								"Liste de controles"))
				{
					state = new PostConditionCheckerState(
							this.activityScheduler, this.activity);
				}
				else
				{
					return false;
				}
				break;
				
			case PREVIOUS :
				state = new ActivityPresentationState(this.activityScheduler,this.activity);
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
