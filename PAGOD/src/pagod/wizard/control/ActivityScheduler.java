/*
 * $Id: ActivityScheduler.java,v 1.16 2005/11/14 01:10:04 psyko Exp $
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

import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.states.ActivityPresentationState;
import pagod.wizard.control.states.AbstractActivityState;
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
     * Constructeur
     * 
     * @param activity
     *            activit? ? d?rouler
     * @param frame
     */
    public ActivityScheduler(Activity activity, MainFrame frame)
    {
        // TODO a suppr 
        this.activity = activity;
        this.state = State.INIT;
        
        this.mfPagod = frame;
        
        // pour pagod

        
        if (this.activity.hasInputProducts())
        	// si il y a des produits en entree 
        	this.activityState = new PreConditionCheckerState (this, activity);
        else
        	this.activityState = new ActivityPresentationState(this, activity);

     
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
     * G?re les requetes soumis au d?rouleur d'activit?
     * 
     * @param request
     */
    public void manageRequest(ApplicationManager.Request request)
    {
        // TODO test state
        System.out.println(this.state + " -> " + request);
        
        switch (request)
        {
            case NEXT:
                this.activityState.next();
                break;
               
            case PREVIOUS:
                this.activityState.previous();
                break;
        }
        
        /*
        switch (this.state)
        {
            case INIT:
                switch (request)
                {
                    case RUN_ACTIVITY:
                        this.presentActivity();
                        ActionManager.getInstance().getAction(
                                Constants.ACTION_NEXT).setEnabled(true);
                        break;
                }
                break;
            case ACTIVITY_PRESENTATION:
                switch (request)
                {
                    case NEXT:
                        // pagod 
                        // this.activityState.next();
                        
                        // TODO avant le design state
                        // dans tout les cas on active l'action Previous
                        ActionManager.getInstance().getAction(
                                Constants.ACTION_PREVIOUS).setEnabled(true);
                        // si on a des produit en entree ou des outils
                        if (this.activity.hasInputProducts()
                                || this.activity.needsTools())
                            // on presente la checklist
                            this.checkBeforeStart();
                        // sinon si il y a des etapes
                        else if (this.activity.hasSteps())
                        {
                            // on pr?sente la premiere ?tape
                            this.stepList = this.activity.getSteps();
                            this.step = this.stepList.get(this.index);
                            this.presentStep(this.step, this.index,
                                    this.stepList.size());
                        }
                        // sinon s'il y a des produits en sortie activit?s
                        else if (this.activity.hasOutputProducts())
                            // on presente ces produits
                            this.presentProducts(this.activity
                                    .getOutputProducts());
                        // sinon l'activit? est terminer
                        else
                            ApplicationManager
                                    .getInstance()
                                    .manageRequest(
                                            ApplicationManager.Request.TERMINATE_ACTIVITY);
                        
                        break;
                }
                break;
            case ACTIVITY_CHECKLIST:
                switch (request)
                {
                    case NEXT:
                        // si on il y a des etapes
                        if (this.activity.hasSteps())
                        {
                            // on pr?sente la premiere ?tape
                            this.stepList = this.activity.getSteps();
                            this.step = this.stepList.get(this.index);
                            this.presentStep(this.step, this.index,
                                    this.stepList.size());
                        }
                        // sinon si l'activit? a des produits
                        else if (this.activity.hasOutputProducts())
                            // on presente ces produits
                            this.presentProducts(this.activity
                                    .getOutputProducts());
                        // sinon l'activit? est terminer
                        else
                            ApplicationManager
                                    .getInstance()
                                    .manageRequest(
                                            ApplicationManager.Request.TERMINATE_ACTIVITY);
                        break;
                    case PREVIOUS:
                        this.presentActivity();
                        ActionManager.getInstance().getAction(
                                Constants.ACTION_PREVIOUS).setEnabled(false);
                        break;
                }
                break;
            case STEP_PRESENTATION:
                switch (request)
                {
                    case NEXT:
                        // si l'etape a des produits
                        if (this.step.hasOutputProducts())
                        {
                            List<Product> lstOutputProduct = this.step
                                    .getOutputProducts();

                            // s'il n'y a qu'un produit on affiche un
                            // JOptionPane lui permettant de choisir
                            // s'il veut creer ou non le produit
                            int optionSelected;
                            if (lstOutputProduct.size() == 1)
                            {
                                optionSelected = JOptionPane
                                        .showConfirmDialog(
                                                this.mfPagod,
                                                LanguagesManager
                                                        .getInstance()
                                                        .getString(
                                                                "createProductmsg")
                                                        + " "
                                                        + lstOutputProduct.get(
                                                                0).getName()
                                                        + " ? ",
                                                LanguagesManager
                                                        .getInstance()
                                                        .getString(
                                                                "createProductTitle"),
                                                JOptionPane.YES_NO_OPTION);

                                // si l'utilisateur a choisi l'option yes on
                                // lance la cr?ation du produit
                                Product outputProduct = lstOutputProduct.get(0);
                                if (optionSelected == JOptionPane.YES_OPTION
                                        && outputProduct.getEditor() != null)
                                    ModelResourcesManager.getInstance()
                                            .launchProductApplication(
                                                    outputProduct);
                            }
                            else
                            {
                                // il y a plusieurs produit a cree

                            }

                            // on presente ces produits
                            this.presentProducts(this.step.getOutputProducts());
                        }
                        // sinon si il reste des etapes ? presenter
                        else if (this.index < this.stepList.size() - 1)
                        {
                            // on presente l'etape suivante
                            this.index++;
                            this.step = this.stepList.get(this.index);
                            this.presentStep(this.step, this.index,
                                    this.stepList.size());
                        }
                        else
                            ApplicationManager
                                    .getInstance()
                                    .manageRequest(
                                            ApplicationManager.Request.TERMINATE_ACTIVITY);
                        break;
                    case PREVIOUS:
                        // si c'est la premiere ?tape
                        if (this.index == 0)
                        {
                            this.step = null;
                            this.stepList = null;
                            this.checkBeforeStart();
                        }
                        // si ce n'est pas la premiere etape
                        else
                        {
                            // on presente l'etape suivante
                            this.index--;
                            this.step = this.stepList.get(this.index);
                            // si l'?tape a des produit en sortie
                            if (this.step.hasOutputProducts())
                                this.presentProducts(this.step
                                        .getOutputProducts());
                            // sinon
                            else
                                this.presentStep(this.step, this.index,
                                        this.stepList.size());
                        }
                        break;
                }
                break;
            case PRODUCTS_PRESENTATION:
                switch (request)
                {
                    case NEXT:
                        // si activit? a des ?tapes il reste des etapes ?
                        // presenter
                        if (this.step != null
                                && this.index < this.stepList.size() - 1)
                        {
                            // on presente l'etape suivante
                            this.index++;
                            this.step = this.stepList.get(this.index);
                            this.presentStep(this.step, this.index,
                                    this.stepList.size());
                        }
                        else
                            ApplicationManager
                                    .getInstance()
                                    .manageRequest(
                                            ApplicationManager.Request.TERMINATE_ACTIVITY);
                        break;
                    case PREVIOUS:
                        // si activit? a des ?tapes
                        if (this.step != null)
                            this.presentStep(this.step, this.index,
                                    this.stepList.size());
                        // sinon si l'activit? a des produit en entree ou
                        // des outils
                        else if (this.activity.hasInputProducts()
                                || this.activity.needsTools())
                            // on presente la checklist
                            this.checkBeforeStart();
                        // sinon
                        else
                        {
                            // on affiche la presentation de l'activit?
                            this.presentActivity();
                            ActionManager.getInstance().getAction(
                                    Constants.ACTION_PREVIOUS)
                                    .setEnabled(false);
                        }
                        break;
                }
                break;
        }
        */
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
        this.state = State.ACTIVITY_CHECKLIST;
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
	 * - dans sa partie supérieur la présentation d'une activité ou d'une étape 
	 * - dans sa partie inférieur les produits a créer durant cette étape ainsi que les plan type
	 *   s'il y en a
	 * 
	 */
	public void resetSplitPane()
	{
		this.mfPagod.resetSplitPane();
	}
}
