/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.3 2006/01/18 00:13:22 psyko Exp $
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
		//TODO a supprimer
    	System.err.println(this.toString());

	}

	
	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#terminate()
	 */
	public void terminate ()
	{
		// TODO Corps de m?thode g?n?r? automatiquement

	}
	
	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#toString()
	 */
	public String toString ()
	{
		//TODO rajouter un message dans l'internationalisation
		return(LanguagesManager.getInstance().getString("activityPresentation"));	
	}

	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#display()
	 */
	public void display ()
	{
		/*
		this.activityScheduler.resetSplitPane();
		// on affiche la presentation de l'activit?
		this.activityScheduler.presentActivityAndProduct();
		*/
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
