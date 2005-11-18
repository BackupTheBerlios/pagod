/*
 * $Id: ActivityScheduler.java,v 1.21 2005/11/18 19:15:04 psyko Exp $
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
import pagod.wizard.control.states.ActivityPresentationState;
import pagod.wizard.control.states.AbstractActivityState;
import pagod.wizard.control.states.FirstStepState;
import pagod.wizard.control.states.LastStepState;
import pagod.wizard.control.states.MiddleStepState;
import pagod.wizard.control.states.PostConditionCheckerState;

import pagod.wizard.control.states.PreConditionCheckerState;
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
     * Step utilis? par la combo en cas d acces direct
     */
    private int goToStepInd;
    
    private ArrayList<AbstractActivityState> stateList;
    
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
        // TODO a suppr 
        this.activity = activity;
        this.stepList = new ArrayList<Step>();
        this.stepList = this.activity.getSteps();
        this.state = State.INIT;
        
        this.stateList = new ArrayList<AbstractActivityState>();       
        this.stateList.add(new PreConditionCheckerState(this, this.activity));
        System.out.println("test add statelist OK ");
        this.mfPagod = frame;
        
        // pour pagod
        if (this.activity.hasInputProducts())

        {  	
        	this.activityState = new PreConditionCheckerState (this, activity);
//        	this.initComboBox();
        	System.out.println("Checkbox init OK");
        	this.activityState.display();
        }

        else

        {
        	this.activityState = new ActivityPresentationState(this, activity);
  //      	this.initComboBox();
        	System.out.println("Checkbox init OK");
        	this.activityState.display();
        	
        }

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
	 * @param goToStepInd
	 */
	public void setGoToStepInd (int goToStepInd)
	{
		this.goToStepInd = goToStepInd;
	}
	
	/**
	 * rempli automatiquement la combo box pour acces direct aux steps
	 */
	 public void initComboBox()
	 {
		 
		 //on remove l action listener de la combo pour eviter conflits et boucles infinies
		this.mfPagod.getButtonPanel().getCbDirectAccess().removeActionListener(
					ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
		//on vide la combo
		
		// si has input, on remplit stateList
		if (this.activity.hasInputProducts())
		 {
			 System.out.println(new PreConditionCheckerState (this, this.activity).toString());
			  this.stateList.add(new PreConditionCheckerState (this, this.activity));
			 
			}
		// on remplit statelist avec Activity presentation
		
		 this.stateList.add(new ActivityPresentationState(this, this.activity));
		 
		 // si has steps , meme chose
		 if(this.activity.hasSteps())
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
					 for(int i=0; i< this.stepList.size() -1; i++)
					 {
						 this.stateList.add(new MiddleStepState (this, this.activity, i));
					 }
					 this.stateList.add(new LastStepState(this, this.activity));
					 break;
			 }
			 
		 }
		 
		 // Meme chose pr les post conditions
		 if (this.activity.hasOutputProducts())
		 {
			 this.stateList.add(new PostConditionCheckerState (this, this.activity));
			 
		 }
		 
		 // on remplit notre combo avec les elements de la statelist
		 for(int i=0; i< this.stateList.size(); i++)
		 {
			 this.mfPagod.getButtonPanel().getCbDirectAccess().
 				addItem(this.stateList.get(i));
		 }
				 
			this.mfPagod.getButtonPanel().getCbDirectAccess().addActionListener(
					ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
	        
	 }
	 
	 /**
	 * s?lectionne l'?tape en cours dans la combo en fonction du contenu de l'activit?
	 * @param i 
	 */
	 public void autoComboSelect(int i)
	 {

	/*	 	if (!this.activity.hasInputProducts() && i>=1)
				i--;
		
		 	this.mfPagod.getButtonPanel().getCbDirectAccess().removeActionListener(
					ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
			
			this.mfPagod.getButtonPanel().getCbDirectAccess().setSelectedIndex(i);
			
			this.mfPagod.getButtonPanel().getCbDirectAccess().addActionListener(
					ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));

		if (!this.activity.hasInputProducts())
			i--;
		//desactivation de l'action pour eviter de lever un actionPerformed lors du selectedIndex
		this.mfPagod.getButtonPanel().getCbDirectAccess().removeActionListener(ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
		this.mfPagod.getButtonPanel().getCbDirectAccess().setSelectedIndex(i);
		this.mfPagod.getButtonPanel().getCbDirectAccess().addActionListener(ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP));
*/
	 }


	/**
	 * @return statelists
	 */
	public List<AbstractActivityState> getStateList ()
	{
		return this.stateList;
	}
	
	/**
	 * @param index 
	 */
	public void setState (int index)
	{
		this.activityState = this.stateList.get(index);
	}
	 
}
