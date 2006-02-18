/*
 * Projet PAGOD
 * 
 * $Id: StepState.java,v 1.6 2006/02/18 16:35:53 cyberal82 Exp $
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
public class StepState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 * @param stepInd
	 * 
	 */
	public StepState (ActivityScheduler activityScheduler, Activity activity,
			int stepInd)
	{
		super(activityScheduler, activity);
		this.index = stepInd;
		this.step = this.stepList.get(this.index);
	}

	/**
	 * @return le nom du step sous forme de chaine
	 * 
	 */
	public String toString ()
	{
		return ("- " + LanguagesManager.getInstance().getString("step") + " : " + this
				.getStepList().get(this.index).getName());
	}

	/**
	 * (non-Javadoc)
	 * 
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
				// si on est sur la derni?re ?tape
				if (this.index == this.stepList.size() - 1)
				{
					if (this.activity.hasOutputProducts()
							|| this.activity
									.hasGuidanceType("Liste de controles")
							|| this.activity.getRole().hasGuidanceType(
									"Liste de controles"))
					{
						state = new PostConditionCheckerState(
								this.activityScheduler, this.activity);
					}
					else
						return false;
				}
				// sinon on passe au step suivant
				else
				{
					state = new StepState(this.activityScheduler,
							this.activity, this.index + 1);
				}
				break;

			case PREVIOUS:
				// si on est sur la premi?re ?tape
				if (this.index == 0)
				{
					// si l'activite a des produits en entree ou qu'il y a des guides 
					// (associe au role ou a l'activite) d'un type autre 
					// que liste de controle on va dans l'etat PreConditionCheckerState
					if (this.activity.hasInputProducts()
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
						// on va dans l'etat ActivityPresentationState
						state = new ActivityPresentationState(
							this.activityScheduler, this.activity);
					}
				}
				else
				{
					state = new StepState(this.activityScheduler,
							this.activity, this.index - 1);
				}

				break;

			case GOTOSTEP:
				return super.manageRequest(request);

			default:
				return false;
		}
		this.activityScheduler.setState(state);
		return true;

	}

}
