/*
 * Projet PAGOD
 * 
 * $Id: AbstractApplicationState.java,v 1.1 2005/11/22 13:27:14 fabfoot Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ActivityScheduler;

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
     * @param activityScheduler
     */
    public AbstractApplicationState(ActivityScheduler activityScheduler)
    {
    	super();
    	
    }
    /**
     * 
     */
    public void showAbout()
    {}
    /**
     * 
     */
    public void quitApplication()
    {}
    /**
     * 
     */
    public void Preferences()
    {}
    /**
     * 
     */
    public abstract void createNewProject();
    /**
     * 
     */
    public abstract void openProject();
    /**
     * 
     */
    public abstract void openProcess();
    /**
     * 
     */
    public abstract void runActivity();
    /**
     * 
     */
    public abstract void setTools();
    /**
     * 
     */
    public abstract void closeProject();
    
}
