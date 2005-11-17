/*
 * Projet PAGOD
 * 
 * $Id: ActivityPresentationState.java,v 1.9 2005/11/17 01:12:51 psyko Exp $
 */

package pagod.wizard.control.states;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.Constants;

/**
 * @author Alexandre Bes
 * 
 */
public class ActivityPresentationState extends AbstractActivityState
{

	/**
	 * @param activityScheduler
	 * @param activity
	 * @param updateRequired 
	 */
	public ActivityPresentationState (ActivityScheduler activityScheduler,
			Activity activity, boolean updateRequired)
	{
		super(activityScheduler, activity);

		System.out.println("Activity presentation ");
		// on affiche la presentation de l'activit?
		this.activityScheduler.presentActivityAndProduct();
		this.activityScheduler.fillDirectAccessComboBox();
		if(!updateRequired)
		{	
			ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP).setEnabled(false);
		}
		if(this.activity.hasInputProducts())
			this.activityScheduler.autoComboSelect(1);
		else
			this.activityScheduler.autoComboSelect(0);
		
		
		// TODO a supprimer
		// test la presentation des produits a creer
		// this.activityScheduler.presentProducts(this.activity.getOutputProducts());
		// this.activityScheduler.checkBeforeEnd();
		
		// s'il y a des produits en sorties
		if (this.activity.hasOutputProducts())
		{
			// on affiche le bouton next
			ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
					.setEnabled(true);
		}
		else
		{
			// on n'affiche pas le bouton next
			ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
					.setEnabled(false);
		}
		
		// on affiche le bouton terminate
		ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
				.setEnabled(true);

        // on affiche la combobox
        ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP)
                .setEnabled(true);
        
		if (this.activity.hasInputProducts())
		{
			// on active le bouton previous
			ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
					.setEnabled(true);
		}
		else
		{
			// on grise le bouton previous
			ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS)
					.setEnabled(false);
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.AbstractActivityState#previous()
	 */
	public void previous ()
	{
		// s'il y a des produits en entree on revient au panneau permettant de
		// voir les preconditions
		if (this.activity.hasInputProducts()) this.activityScheduler
				.setActivityState(new PreConditionCheckerState(this.activityScheduler, this.activity,true));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.AbstractActivityState#next()
	 */
	public void next ()
	{

		System.out.println("ActivityPresentationState : next() nbStep = "
				+ this.stepList.size());

		switch (this.stepList.size())
		{
			// s'il n'y a pas d'etape on passe directement au dernier etat
			case 0:

				if ( ! this.activity.hasOutputProducts())
				{
					// TODO il faut creer une exception
					System.err.println("ActivityPresentationStep : on ne devrait pas pouvoir fre de next qd il n'y a pas de produit en sortie");
					return;
				}
					
				this.activityScheduler.setActivityState(new PostConditionCheckerState(this.activityScheduler,this.activity));
					 
				
				break;

			// s'il y a une seule etape, elle doit avoir le comportement de la
			// derniere
			case 1:
				this.activityScheduler.setActivityState(new LastStepState(
						this.activityScheduler, this.activity));
				break;

			default:
				this.activityScheduler.setActivityState(new FirstStepState(
						this.activityScheduler, this.activity));
				break;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see pagod.wizard.control.states.AbstractActivityState#terminate()
	 */
	public void terminate ()
	{
		// TODO Corps de m?thode g?n?r? automatiquement

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
    						this.previous ();
    						break;
    					case 1:
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
    			}
    		}
    		else
    		{
    			if(s)
    			{
    				System.out.println(" I et !O et S ");
    			}
    			else
    			{
    				System.out.println(" I et !O et !S ");
    			}
    		}
    	}
    	else
    	{
    		if(o)
    		{
    			if(s)
    			{
    				System.out.println(" !I et O et S ");
    			}
    			else
    			{
    				System.out.println(" !I et O et !S ");
    			}
    		}
    		else
    		{
    			if(s)
    			{
    				System.out.println(" !I et !O et S ");
    			}
    			else
    			{
    				System.out.println(" !I et !O et !S ");
    			}
    		}
    	}
    }
    	

	
}
