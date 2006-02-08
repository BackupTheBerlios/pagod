/*
 * $Id: ActivityScheduler.java,v 1.33 2006/02/08 16:48:21 cyberal82 Exp $
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
import java.util.List;
import java.util.Observable;

import pagod.common.model.Activity;
import pagod.common.model.Step;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;

/**
 * Classe g?rant le d?roulement de l'assitance d'un activit?
 * 
 * @author MoOky
 */
public class ActivityScheduler extends Observable
{
	/**
	 * Etat du d?roulement de l'activit? pagod
	 */
	private AbstractActivityState		activityState;

	/**
	 * Activit? ? d?roul?
	 */
	private Activity					activity	= null;

	/**
	 * indice de l'?tape
	 */
	private int							index		= 0;

	/**
	 * Liste des ?tapes de l'activit?
	 */
	private List<Step>					stepList;

	/**
	 * Etape Courante;
	 */
	private Step						step;

	/**
	 * 
	 * Liste des ?tats possibles de l'activit?
	 */
	private List<AbstractActivityState>	stateList;

	/**
	 * Constructeur
	 * 
	 * @param activity
	 *            activit? ? d?rouler
	 */
	public ActivityScheduler (final Activity activity)
	{
		this.activity = activity;
		this.stateList = new ArrayList<AbstractActivityState>();
		this.stepList = this.activity.getSteps();
		this.setStateList();
	}

	/**
	 * D?finit l'?tat de la machine ? ?tat qui represente une activit? lanc?
	 * 
	 * @param activityState
	 *            est l'?tat de la machine a ?tat qui repr?sente une activit?
	 *            lanc?
	 */
	public void setActivityState (AbstractActivityState activityState)
	{
		this.activityState = activityState;
	}

	/**
	 * @return la stepList utilis? dans le Main Frame, et ds les stepstates
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
	public AbstractActivityState getState (int indice)
	{
		AbstractActivityState abstractActivityState = this.stateList
				.get(indice);
		return abstractActivityState;
	}

	/**
	 * met a jour l'etat de l'ActivityScheduler
	 * 
	 * @param activityState
	 *            l'etat de l'ActivityScheduler a mettre en place
	 */
	public void setState (AbstractActivityState activityState)
	{
		this.activityState = activityState;
		if (this.activityState == null)
		{
			System.err
					.println("ActivityState a null dans l'activity scheduler");
			return;
		}

		// on indique aux observers que l'etat a change
		this.setChanged();
		this.notifyObservers(this.activityState);
	}

	/**
	 * @param etat
	 */
	public void addState (AbstractActivityState etat)
	{
		if (!this.stateList.contains(etat))
		{
			this.stateList.add(etat);
		}
	}

	/**
	 * fonction cr?ant la liste des etats pr?sents dans l'activit? ceux ci
	 * serviront ? l'abstract factory
	 */

	public void setStateList ()
	{

		// si l'activite a des produit en entree ou qu'elle a des guides
		// qui ne sont pas de type "liste de controles"
		// ou que le role associe a l'activite a des guides qui ne sont
		// pas de type "liste de controles"
		if (this.activity.hasInputProducts()
				|| this.activity.hasGuidanceWithoutType("Liste de controles")
				|| this.activity.getRole().hasGuidanceWithoutType(
						"Liste de controles"))
		{
			this.addState(new PreConditionCheckerState(this, this.activity));
		}

		this.addState(new ActivityPresentationState(this, this.activity));

		if (this.activity.hasSteps())
		{

			for (int i = 0; i < this.stepList.size(); i++)
			{
				this.stateList.add(new StepState(this, this.activity, i));
			}

		}

		// si l'activite a des produit en sortie ou qu'elle a des guides
		// qui sont de type "liste de controles"
		// ou que le role associe a l'activite a des guides qui sont
		// de type "liste de controles"
		if (this.activity.hasOutputProducts()
				|| this.activity.hasGuidanceType("Liste de controles")
				|| this.activity.getRole()
						.hasGuidanceType("Liste de controles"))
		{
			this.addState(new PostConditionCheckerState(this, this.activity));
		}
	}

	/**
	 * @param request
	 * @return un bool
	 */
	public boolean ManageRequest (Request request)
	{
		return this.activityState.manageRequest(request);
	}

}
