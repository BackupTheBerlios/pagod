/*
 * Projet PAGOD
 * 
 * $Id: AbstractActivityState.java,v 1.1 2005/11/08 17:31:25 yak Exp $
 */
package pagod.wizard.control.states;

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
     * 
     * @param activityScheduler
     * @param activity
     */
    public AbstractActivityState(ActivityScheduler activityScheduler, Activity activity)
    {
        super();
        // TODO Corps de constructeur généré automatiquement
        
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
    public AbstractActivityState(ActivityScheduler activityScheduler, Activity activity, int iCurrentStep)
    {
        super();
        // TODO Corps de constructeur généré automatiquement
        
        this.activityScheduler = activityScheduler;
        this.activity = activity;
        this.index = iCurrentStep;
        this.stepList = activity.getSteps();
        this.step = null;
    }

    /**
     * 
     *
     */
    public abstract void previous();
    
    /**
     * 
     */
    public abstract void next();
    
    /**
     * 
     */
    public abstract void terminate();
}
