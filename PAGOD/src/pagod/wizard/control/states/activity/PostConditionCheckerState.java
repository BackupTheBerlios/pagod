/*
 * Projet PAGOD
 * 
 * $Id: PostConditionCheckerState.java,v 1.8 2006/02/18 16:35:53 cyberal82 Exp $
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
				// si l'activite a des produits en entree ou qu'il y a des guides 
				// (associe au role ou a l'activite) d'un type autre 
				// que liste de controle on va dans l'etat PreConditionCheckerState
				else if (this.activity.hasInputProducts()
							|| this.activity.hasGuidanceWithoutType(
									"Liste de controles")
							|| this.activity.getRole()
									.hasGuidanceWithoutType("Liste de controles"))
				{
					state = new PreConditionCheckerState(
								this.activityScheduler, this.activity);
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
