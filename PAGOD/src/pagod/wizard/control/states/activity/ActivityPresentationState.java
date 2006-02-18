/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.7 2006/02/18 16:35:53 cyberal82 Exp $
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

	/**
	 * 
	 * @return une chaine de caractere
	 */
	public String toString ()
	{
		return (LanguagesManager.getInstance()
				.getString("activityPresentation"));
	}

	/**
	 * @see pagod.wizard.control.states.AbstractState#manageRequest(pagod.wizard.control.states.Request)
	 */

	public boolean manageRequest (Request request)
	{
		// AbstractActivityState temporaire
		AbstractActivityState state;

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{
			case NEXT:
				// s'il y a des produits en entrees ou des guides d'un type
				// autre que "liste de controles" associe a l'activite (ou au
				// role de cette activite) on passe dans l'etat PreConditionCheckerState
				if (this.activity.hasInputProducts()
						|| this.activity.hasGuidanceWithoutType(
								"Liste de controles")
						|| this.activity.getRole()
								.hasGuidanceWithoutType("Liste de controles"))
				{
					state = new PreConditionCheckerState(
							this.activityScheduler, this.activity);
				}
				else if (this.activity.hasSteps())
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

			case GOTOSTEP:
				return super.manageRequest(request);

			default:
				return false;
		}

		if (state == null)
		{
			System.err.println("state a null dans le manage request");
			return false;
		}

		this.activityScheduler.setState(state);
		return true;

	}

}
