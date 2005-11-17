/*
 * Projet PAGOD
 * 
 * $Id: PreConditionCheckerState.java,v 1.10 2005/11/17 12:14:56 yak Exp $
 */
package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

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
        
        this.activityScheduler.resetSplitPane();
        this.activityScheduler.checkBeforeStart();
        this.activityScheduler.fillDirectAccessComboBox();
		
		this.activityScheduler.autoComboSelect(0);

        
        // on affiche le bouton next
        ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
                .setEnabled(true);

        // on affiche le bouton terminate
        ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
                .setEnabled(true);

        // on affiche le bouton previous
        ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
                .setEnabled(false);
        
        // on affiche la combobox
        ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP)
                .setEnabled(true);
        
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#previous()
     */
    public void previous()
    {
      
     
    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#next()
     */
    public void next()
    {
            this.activityScheduler.setActivityState(new ActivityPresentationState(
                this.activityScheduler, this.activity, true));

    }

    /** (non-Javadoc)
     * @see pagod.wizard.control.states.AbstractActivityState#terminate()
     */
    public void terminate()
    {
     }
    
    /**
    /* 
     * @see pagod.wizard.control.states.AbstractActivityState#gotoStep()
     */
    public void gotoStep()
    {
    	boolean i = this.activity.hasInputProducts();
    	boolean o = this.activity.hasOutputProducts();
    	boolean s = this.activity.hasSteps();
    	int GTS = this.getGoToStepInd();
    	int LS = this.getStepList().size();

    	if(i)
    	{
    		if(o)
    		{
    			if(s)
    			{
    				System.out.println(" I et O et S ; GTS " +GTS);
    				switch(GTS)
    				{
    					case 0:
    						break;
    					case 1:
    						this.activityScheduler.setActivityState(new ActivityPresentationState(
    								this.activityScheduler, this.activity, false));
    				        break;
    					case 2:
    						this.activityScheduler.setActivityState(new FirstStepState(
    								this.activityScheduler, this.activity));
    						break;
    					default :
    						if(GTS == LS +2)
    						{
    							this.activityScheduler.setActivityState(new PostConditionCheckerState(
        								this.activityScheduler, this.activity));
    						}
    						else
    						{
    							if (GTS == LS +1)
    							{
    								this.activityScheduler.setActivityState(new LastStepState(
    	    								this.activityScheduler, this.activity));
    							}
    							else
    							{
    								this.activityScheduler.setActivityState(new MiddleStepState(
    	    								this.activityScheduler, this.activity, GTS-1));
    							}
    						}
    						break;
    						
    				}
    			}
    			else
    			{
    				System.out.println(" I et O et !S ");
    				switch(GTS)
    				{
    					case 0:
    						break;
    					case 1:
    						this.activityScheduler.setActivityState(new ActivityPresentationState(
    								this.activityScheduler, this.activity, false));
    				        break;
    					case 2:
    						this.activityScheduler.setActivityState(new PostConditionCheckerState(
    								this.activityScheduler, this.activity));
    						break;			
    				}
    			}
    		}
    		else
    		{
    			if(s)
    			{
    				System.out.println(" I et !O et S ");
    				switch(GTS)
    				{
    					case 0:
    						break;
    					case 1:
    						this.activityScheduler.setActivityState(new ActivityPresentationState(
    								this.activityScheduler, this.activity, false));
    				        break;
    					case 2:
    						this.activityScheduler.setActivityState(new FirstStepState(
    								this.activityScheduler, this.activity));
    						break;
    					default :
    						if (GTS == LS +1)
    						{
    							this.activityScheduler.setActivityState(new LastStepState(
    	    							this.activityScheduler, this.activity));
    						}
    						else
    						{
    							this.activityScheduler.setActivityState(new MiddleStepState(
    	    						this.activityScheduler, this.activity, GTS-1));
    						}
    					break;
    						
    				}
    			}
    			else
    			{
    				System.out.println(" I et !O et !S ");
    				switch(GTS)
    				{
    					case 0:
    						break;
    					case 1:
    						this.activityScheduler.setActivityState(new ActivityPresentationState(
    								this.activityScheduler, this.activity, false));
    				        break;
    				}
    			}
    		}
    	}
    }
 }
