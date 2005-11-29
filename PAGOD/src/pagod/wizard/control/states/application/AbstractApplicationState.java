/*
 * Projet PAGOD
 * 
 * $Id: AbstractApplicationState.java,v 1.3 2005/11/29 18:11:15 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

/**
 * @author m1isi26
 *
 */
public abstract class AbstractApplicationState
{
	 /**
     * on stocke l'activityScheduler car un etat doit pouvoir changer l'etat
     * de l'ActivityScheduler (invocation de la methode setState())
     */
    protected ActivityScheduler activityScheduler;
    /**
     * @param applicationManager 
     * 
     */
    public AbstractApplicationState(ApplicationManager applicationManager)
    {
    	super();
    	
    }
    
    /**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public abstract boolean manageRequest (Request request);
}
