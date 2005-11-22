  /*
 * Projet PAGOD
 * 
 * $Id: AbstractActivityState.java,v 1.1 2005/11/22 13:27:13 fabfoot Exp $
 */
package pagod.wizard.control.states.activity;

import java.util.List;

import pagod.common.model.Activity;
import pagod.common.model.Step;
import pagod.wizard.control.ActivityScheduler;

/**
 * @author Cyberal
 *
 */
public abstract class AbstractActivityState
{
    /**
     * on stocke l'activityScheduler car un etat doit pouvoir changer l'etat
     * de l'ActivityScheduler (invocation de la methode setState())
     */
    protected ActivityScheduler activityScheduler;
    
    protected Activity activity = null;
    
    /**
     * indice de l'etape
     */
    protected int index = 0;
    
    protected List<Step> stepList;

    /**
     * Etape Courante;
     */
    protected Step step;
    
    /**
     * Step utilis? par la combo en cas d acces direct
     */
    private int goToStepInd;
    

    /**
     * 
     * @param activityScheduler
     * @param activity
     */
    public AbstractActivityState(ActivityScheduler activityScheduler, Activity activity)
    {
        super();
        // TODO Corps de constructeur g?n?r? automatiquement
        
        this.activityScheduler = activityScheduler;
        this.activity = activity;
        this.stepList = activity.getSteps();
        this.step = null;

    }
    
    /**
     * 
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
  
    
    /**
     * 
     */
    public abstract void terminate();
    

	/**
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
	
	/**
	 * 
	 */
	public abstract void display();
    
	/**
	 * @param goToStepInd
	 */
	public void setGoToStepInd (int goToStepInd)
	{
		this.goToStepInd = goToStepInd;
	}

	/**
	 * @return goToStepInd
	 */
	public int getGoToStepInd ()
	{
		return this.goToStepInd;
	}

	/**
	 * @return liste
	 */
	public List<Step> getStepList ()
	{
		return this.stepList;
	}

    /**
     *  (non-Javadoc)
     * @param index 
     * @see pagod.wizard.control.states.AbstractActivityState
     */
    public void setState(int index)
    {
    	this.activityScheduler.setState(
    			this.activityScheduler.getMfPagod().getButtonPanel().
    			getCbDirectAccess().getSelectedIndex());
    }
    
    
	
}