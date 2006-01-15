/*
 * Projet PAGOD
 * 
 * $Id: ActivityState.java,v 1.1 2006/01/15 15:38:40 cyberal82 Exp $
 */
package test.pagod.wizard.control.states.activity;

import pagod.common.model.Activity;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.activity.AbstractActivityState;

/**
 * Cette classe a ete cree uniquement pour pouvoir tester la methode manageRequest
 * de la classe AbstractActivityState qui gere uniquement les requetes de type GOTOSTEP
 * 
 * @author Cyberal
 *
 */
public class ActivityState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 */
	public ActivityState (ActivityScheduler activityScheduler, Activity activity)
	{
		super(activityScheduler, activity);
		// TODO Corps de constructeur généré automatiquement
	}

	/* (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#terminate()
	 */
	public void terminate ()
	{
		// TODO Corps de méthode généré automatiquement

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		// TODO Corps de méthode généré automatiquement
		return null;
	}

	/* (non-Javadoc)
	 * @see pagod.wizard.control.states.activity.AbstractActivityState#display()
	 */
	public void display ()
	{
		// TODO Corps de méthode généré automatiquement

	}

}
