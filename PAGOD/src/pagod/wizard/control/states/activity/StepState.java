/*
 * Projet PAGOD
 * 
 * $Id: StepState.java,v 1.1 2005/11/27 20:37:52 yak Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
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

		// TODO a supprimer
		System.err.println(this.toString());
	}

	@Override
	public void terminate ()
	{
		// TODO Corps de méthode généré automatiquement

	}

	@Override
	public String toString ()
	{
		return this.getStepList().get(this.index).getName();
	}

	@Override
	public void display ()
	{
		/*
		 * this.activityScheduler.resetSplitPane(); //TODO a supprimer
		 * System.out.println("stateListSize " +
		 * this.activityScheduler.getStateList().size()); //affichage de l'etape
		 * this.activityScheduler.presentStep(this.stepList.get(this.index),this.index,this.stepList.size());
		 */
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
				// si on est sur la dernière étape
				if (this.index == this.stepList.size() - 1)
				{
					if (this.activity.hasOutputProducts()) state = new PostConditionCheckerState(
							this.activityScheduler, this.activity);
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
				// si on est sur la première étape on revient sur
				// l'activityPresentation
				if (this.index == 0)
				{
					state = new ActivityPresentationState(
							this.activityScheduler, this.activity);
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
