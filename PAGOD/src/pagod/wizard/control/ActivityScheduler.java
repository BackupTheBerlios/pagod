/*
 * $Id: ActivityScheduler.java,v 1.24 2005/11/22 13:27:14 fabfoot Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of PAGOD.
 *
 * PAGOD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * PAGOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PAGOD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.wizard.control;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.utils.ActionManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.FirstStepState;
import pagod.wizard.control.states.activity.LastStepState;
import pagod.wizard.control.states.activity.MiddleStepState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepFactory;
import pagod.wizard.ui.MainFrame;

/**
 * Classe g?rant le d?roulement de l'assitance d'un activit?
 * 
 * @author MoOky
 */
public class ActivityScheduler
{
    /**
     * Etat possible de l'application
     */
    private enum State {
        /**
         * Etat initial
         */
        INIT,
        /**
         * Presentation de l'activit?
         */
        ACTIVITY_PRESENTATION,
        /**
         * Verification des prerequis de l'activit?
         */
        ACTIVITY_CHECKLIST,
        /**
         * Verification des post Condition de l'acitvit?
         */
        ACTIVITY_ENDCHECKLIST,
        /**
         * Presentation de l'?tape
         */
        STEP_PRESENTATION,
        /**
         * Presentation des produits ? r?alis?.
         */
        PRODUCTS_PRESENTATION
    }

    /**
     * Etat du d?roulement de l'activit? pagod
     */
    private State state;

    /**
     * Etat du d?roulement de l'activit? pagod
     */
    private AbstractActivityState activityState;

    /**
     * Activit? ? d?roul?
     */
    private Activity activity = null;

    /**
     * indice de l'?tape
     */
    private int index = 0;

    /**
     * Liste des ?tapes de l'activit?
     */
    private List<Step> stepList;

    /**
     * Etape Courante;
     */
    private Step step;

    /**
     * Fenetre principale de l'application
     */
    private MainFrame mfPagod;
    
    /**
     * currentyActivitState : ?tat courant de l'activit?
     */
    private int currentActivityState;
    
    /**
     * Step utilis? par la combo en cas d acces direct
     */
    //private int goToStepInd;
    
    /**
     * 
     * Liste des ?tats possibles de l'activit?
     */
    private List<AbstractActivityState> stateList = new ArrayList<AbstractActivityState>();
    
    /**
     * Constructeur
     * 
     * @param activity
     *            activit? ? d?rouler
     * @param frame
     */
    public ActivityScheduler(final Activity activity, MainFrame frame)
    {
     	
    	System.out.println("Constr ActivityScheduler ");
        this.activity = activity;
        this.stateList = new ArrayList <AbstractActivityState>();
        this.stepList = this.activity.getSteps();
        
        this.state = State.INIT;
        this.setStateList();
        this.mfPagod = frame;
        this.mfPagod.InitButtonPanel();
    }
    
    /**
     * 
     */
    public void initActivityScheduler()
    {
    	new StepFactory(this, this.activity, this.stateList, 0);
    }
   
    /**
     * D?finit l'?tat de la machine ? ?tat qui represente une activit? lanc?
     * 
     * @param activityState
     *            est l'?tat de la machine a ?tat qui repr?sente une activit?
     *            lanc?
     */
    public void setActivityState(AbstractActivityState activityState)
    {
			this.activityState = activityState;
	}
    
    /**
     * Presente les produits passer en parametre
     * 
     * @param outputProducts
     */
    public void presentProducts(Collection<Product> outputProducts)
    {
        this.mfPagod.presentProducts(outputProducts);
        this.state = State.PRODUCTS_PRESENTATION;
    }

   

    /**
     * Presente l'etape passer en parametre
     * 
     * @param stepToPresent
     * @param rang
     * @param total
     */
    public void presentStep(Step stepToPresent, int rang, int total)
    {
        this.mfPagod.presentStep(stepToPresent, rang + 1, total);
        this.state = State.STEP_PRESENTATION;
    }

    /**
     * lance l'activit?
     * 
     */
    public void presentActivity()
    {
        this.mfPagod.presentActivity(this.activity);
        this.state = State.ACTIVITY_PRESENTATION;
    }
    
    /**
     * 
     *
     */
    public void presentActivityAndProduct()
    {
    	this.mfPagod.presentActivityAndProduct(this.activity);
    }
    
    /**
     * Lance la verification des pr?requis
     * 
     */
    public void checkBeforeStart()
    {
        this.mfPagod.showCheckList(this.activity);
        //this.state = State.ACTIVITY_CHECKLIST;
    }
    
    /**
     * Lance la verification des postconditions
     */
    public void checkBeforeEnd ()
    {
    	this.mfPagod.showEndCheckList(this.activity);
    	//this.state = State.ACTIVITY_ENDCHECKLIST;
    	
    }

	/**
	 * @return Retourne l'attribut mfPagod
	 */
	public MainFrame getMfPagod ()
	{
		return this.mfPagod;
	}
  
  
	/**
	 * V?rifie les post conditions du projet
	 * @return TRUE si on a verifi? les post cond
	 * 			FALSE sinon
	 */
	public boolean checkPostCondition ()
	{
		// on construit le message avec tout le produits
		String sPrecondMessage = new String("");
		String sPrecondTitle = LanguagesManager.getInstance().getString(
				"postCondTitle");
		sPrecondMessage += LanguagesManager.getInstance().getString(
				"postCondMessage")
				+ "\n";
		// pour chaque produit on ajoute le nom a la liste des produits
		for (Product productTemp : this.activity.getOutputProducts())
		{
			// ajout du nom au message
			sPrecondMessage += productTemp.getName() + "\n";
		}

		int iRetour = JOptionPane.showConfirmDialog(this.getMfPagod(),
				sPrecondMessage, sPrecondTitle, JOptionPane.YES_NO_OPTION);
		// en fonction du retour un renvoir le bon booleen
		switch (iRetour)
		{
			case JOptionPane.NO_OPTION:
				return false;
			case JOptionPane.YES_OPTION:
				return true;
			default:
				return false;

		}

	}

	/**
	 * Remet a null le splitPane permettant d'afficher : 
	 * - dans sa partie sup?rieur la pr?sentation d'une activit? ou d'une ?tape 
	 * - dans sa partie inf?rieur les produits a cr?er durant cette ?tape ainsi que les plan type
	 *   s'il y en a
	 * 
	 */
	public void resetSplitPane()
	{
		this.mfPagod.resetSplitPane();
	}


	/**
	 * @return la stepList
	 * utilis? dans le Main Frame, et ds les stepstates 
	 */
	public List<Step> getStepList ()
	{
		return this.stepList;
	}


	/**
	 * @return l'activit?
	 */
	public Activity getActivity ()
	{
		return this.activity;
	}


	/**
	 * @return activity state
	 */
	public AbstractActivityState getActivityState ()
	{
		return this.activityState;
	}
	
	/**
	 * @return statelists
	 */
	public List<AbstractActivityState> getStateList ()
	{
		return this.stateList;
	}
	
	/**
	 * @param indice
	 * @return AbstractActivityState
	 */
	public AbstractActivityState getState(int indice)
	{
		AbstractActivityState abstractActivityState = this.stateList.get(indice);
		return abstractActivityState;
	}
	
	/**
	 * @param indice
	 */
	public void setState (int indice)
	{
		this.activityState = this.getState(indice);
	}
	 
    /**
     * @param etat
     */
    public void addState(AbstractActivityState etat)
    {
        if (!this.stateList.contains(etat))
        {
            this.stateList.add(etat);
        }
    }
    
    /**
     * fonction cr?ant la liste des etats pr?sents dans l'activit?
     * ceux ci serviront ? l'abstract factory
     */
    public void setStateList()
    {
    	if (this.activity.hasInputProducts())
    		this.addState(new PreConditionCheckerState(this,this.activity));
    		
    	this.addState(new ActivityPresentationState(this,this.activity));
    	
    	if (this.activity.hasSteps())
    	{
   			 switch(this.stepList.size())
   			 {
   				 case 1:
   					 this.stateList.add(new LastStepState(this, this.activity));
   					 break;
   				 case 2:
   					 this.stateList.add(new FirstStepState(this, this.activity));
   					 this.stateList.add(new LastStepState(this, this.activity));
   					 break;
   				 default:
   					 this.stateList.add(new FirstStepState(this, this.activity));
   					 for(int i = 1; i< this.stepList.size()-1; i++)
   					 {
   						 this.stateList.add(new MiddleStepState (this, this.activity, i));
   					 }
   					 this.stateList.add(new LastStepState(this, this.activity));
   					 break;
   			 }
    	}
    		
    	if (this.activity.hasOutputProducts())
    		this.addState(new PostConditionCheckerState(this,this.activity));
    		
    	System.out.println("StateList OK : " + this.stateList.size() + " Elts");
    
    }

	/**
	 * init ComboBOx
	 */
	public void initComboBox()
	{
		this.mfPagod.getButtonPanel().getCbDirectAccess().removeActionListener(
				ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
		
		this.mfPagod.getButtonPanel().getCbDirectAccess().removeAll();
		this.mfPagod.getButtonPanel().getCbDirectAccess().removeAllItems();
		
		// on remplit notre comboModel avec les elements de la statelist
		 for(AbstractActivityState states : this.stateList)
		 {
			 this.mfPagod.getButtonPanel().getCbDirectAccess().addItem(states);
		 }

		// en 2 on remplit la combo box
		this.mfPagod.getButtonPanel().getCbDirectAccess().setSelectedIndex(this.currentActivityState);
		this.mfPagod.getButtonPanel().getCbDirectAccess().addActionListener(
				ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
	}

	/**
	 * @return l'etat courant de l'activit?
	 */
	public int getCurrentActivityState ()
	{
		return this.currentActivityState;
	}

	/**
	 * @param currentActivityState
	 */
	public void setCurrentActivityState (int currentActivityState)
	{
		this.currentActivityState = currentActivityState;
	}
    
}
