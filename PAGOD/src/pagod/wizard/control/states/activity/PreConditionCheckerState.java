/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.2 2005/11/27 20:37:52 yak Exp $
 */
package pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.Request;

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
    	//TODO a supprimer
    	System.err.println(this.toString());
           
    }

  
    /** (non-Javadoc)
     * @see pagod.wizard.control.states.activity.AbstractActivityState#terminate()
     */
    public void terminate()
    {
     }
    
    
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#toString()
	 */
	public String toString ()
	{
//		TODO rajouter un message dans l'internationalisation
		return(" Verification des PreConditions ");
	}

	
	/** (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#display()
	 */
	public void display ()
	{
		/*
		this.activityScheduler.resetSplitPane();
        this.activityScheduler.checkBeforeStart();
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
