/*
 * $Id: ButtonPanel.java,v 1.8 2005/11/29 18:11:15 yak Exp $
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

package pagod.wizard.ui;

import static pagod.wizard.control.Constants.ACTION_GOTOSTEP;
import static pagod.wizard.control.Constants.ACTION_NEXT;
import static pagod.wizard.control.Constants.ACTION_PREVIOUS;
import static pagod.wizard.control.Constants.ACTION_TERMINATE;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pagod.utils.ActionManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;

/**
 * @author Cédric Panneau de boutons de navigation
 */
public class ButtonPanel extends JPanel
{
	/** Bouton "Suivant" */
	private JButton		pbNext;

	/** Bouton "Précédent" */
	private JButton		pbPrevious;

	/** Bouton "Terminer" */
	private JButton		pbTerminate;

	/** Combo box pour acces direct aux steps* */
	private JComboBox	cbDirectAccess;

	/**
	 * Identificateurs des boutons
	 */
	public enum Buttons
	{
		/** Bouton suivant */
		PB_NEXT,
		/** Bouton précédent */
		PB_PREVIOUS,
		/** Bouton terminer */
		PB_TERMINATE,
		/** Combo pour acces direct aux étapes */
		CB_GOTOSTEP

	}

	/**
	 * Constructeur du panneau de boutons
	 * 
	 */
	public ButtonPanel ()
	{
		super(new BorderLayout());

		// Récupération de l'instance du gestionnaire d'actions
		ActionManager am = ActionManager.getInstance();

		// Création des boutons
		this.pbNext = new JButton(am.getAction(ACTION_NEXT));
		this.pbNext.setHorizontalTextPosition(SwingConstants.LEADING);
		this.pbPrevious = new JButton(am.getAction(ACTION_PREVIOUS));
		this.pbTerminate = new JButton(am.getAction(ACTION_TERMINATE));

		this.cbDirectAccess = new JComboBox();
		this.cbDirectAccess.addActionListener(am.getAction(ACTION_GOTOSTEP));

		// Création de panneaux intermédiaires pour la taille des boutons
		JPanel pnlGauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pnlCentre = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pnlDroite = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel pnlBas = new JPanel(new FlowLayout(FlowLayout.CENTER));

		this.add(pnlGauche, BorderLayout.WEST);
		this.add(pnlCentre, BorderLayout.CENTER);
		this.add(pnlDroite, BorderLayout.EAST);
		this.add(pnlBas, BorderLayout.SOUTH);

		// Ajout des boutons aux panneaux
		pnlGauche.add(this.pbPrevious);
		pnlCentre.add(this.pbTerminate);
		pnlDroite.add(this.pbNext);
		// TODO rajouter les entrées de Aller à : -> Go to
		// dans le properties ...
		// ça devrait permettre d'utiliser un mnémonique

		pnlBas.add(this.cbDirectAccess);
	}

	/**
	 * Affiche les boutons spécifiés
	 * 
	 * @param buttons
	 *            Boutons à afficher
	 */
	public void showButtons (Buttons... buttons)
	{
		this.setButtonsVisible(buttons, true);
	}

	/**
	 * Masque les boutons spécifiés
	 * 
	 * @param buttons
	 *            Boutons à masquer
	 */
	public void hideButtons (Buttons... buttons)
	{
		this.setButtonsVisible(buttons, false);
	}

	/**
	 * Cache tous les boutons
	 */
	public void hideAllButtons ()
	{
		this.setAllButtonsVisible(false);
	}

	/**
	 * Affiche tous les boutons
	 */
	public void showAllButtons ()
	{
		this.setAllButtonsVisible(true);
	}

	/**
	 * Change la visibilité des boutons spécifiés
	 * 
	 * @param buttons
	 *            Boutons à modifier
	 * @param visible
	 *            VRAI pour afficher les boutons, FAUX pour les masquer
	 */
	private void setButtonsVisible (Buttons[] buttons, boolean visible)
	{
		for (int i = 0; i < buttons.length; i++)
		{
			switch (buttons[i])
			{
				case PB_NEXT:
					this.pbNext.setVisible(visible);
					break;
				case PB_PREVIOUS:
					this.pbPrevious.setVisible(visible);
					break;
				case PB_TERMINATE:
					this.pbTerminate.setVisible(visible);
					break;
			}
		}
	}

	/**
	 * Change la visibilité de tous les boutons
	 * 
	 * @param visible
	 *            VRAI pour afficher tous les boutons, FAUX pour les masquer
	 */
	private void setAllButtonsVisible (boolean visible)
	{
		this.pbNext.setVisible(visible);
		this.pbPrevious.setVisible(visible);
		this.pbTerminate.setVisible(visible);
	}

	/**
	 * @return la combo poru l acces direct, utilisé dans le MainFrame
	 */
	public JComboBox getCbDirectAccess ()
	{
		return (this.cbDirectAccess);
	}

	/**
	 * initialise la JComboBox avec les etapes récupérés en paramètre
	 * 
	 * @param stateList
	 *            la liste des états
	 */
	public void initComboBox (List<AbstractActivityState> stateList)
	{

		this.cbDirectAccess.removeActionListener(ActionManager.getInstance()
				.getAction(Constants.ACTION_GOTOSTEP));
		// on remplit notre comboModel avec les elements de la statelist
		for (AbstractActivityState states : stateList)
		{
			// this.mfPagod.getButtonPanel().getCbDirectAccess().addItem(states);
			this.cbDirectAccess.addItem(new Request(
					Request.RequestType.GOTOSTEP, states));
		}

		// en 2 on remplit la combo box
		/*
		 * this.cbDirectAccess.setSelectedIndex( this.currentActivityState);
		 */
		this.cbDirectAccess.addActionListener(ActionManager.getInstance()
				.getAction(Constants.ACTION_GOTOSTEP));

	}

	/**
	 * positionne la JCombobox sur l'etape passée en parametre
	 * 
	 * @param state
	 *            l'état sur lequel on est
	 */
	public void setSelectedIndex (AbstractActivityState state)
	{
		System.out.println("ButtonPanel.setSelectedIndex : " + state);
		// on desactive le listener sur le comboBox
		this.cbDirectAccess.removeActionListener(ActionManager.getInstance()
				.getAction(Constants.ACTION_GOTOSTEP));

		int i = 0;
		boolean trouve = false;
		while (i < this.cbDirectAccess.getItemCount() && !trouve)
		{
			/*
			 * TODO il faut changer cela en redefinissant equals dans tous les
			 * classes qui interviennent, probablement toute les classes metiers
			 */

			// l'etat passe en parametre est le meme que celui de la combo s'il
			// est de meme type pour les classes PreConditionCheckerState,
			// ActivityPresentationState, PostConditionCheckerState
			// car le JComboBox contient une seule instance de ces classes ou si
			// l'etat passe en parametre est le meme que celui de la combo
			// et que les etapes sont les memes (meme index)
			AbstractActivityState activityState = (AbstractActivityState) ((Request) this.cbDirectAccess
					.getItemAt(i)).getContent();
			if ((state instanceof PreConditionCheckerState && activityState instanceof PreConditionCheckerState)
					|| (state instanceof ActivityPresentationState && activityState instanceof ActivityPresentationState)
					|| (state instanceof StepState
							&& activityState instanceof StepState && state
							.getIndex() == activityState.getIndex())
					|| (state instanceof PostConditionCheckerState && activityState instanceof PostConditionCheckerState))
			{
				trouve = true;
				
				System.err.println("ON A TROUVE");
			}
			else
				i++;
		}
		
		// si on a trouve l'etat on positionne selectionne cet elt dans la combo
		if (trouve)
		{
			this.cbDirectAccess.setSelectedIndex(i); // regarde setSelectedItem
		}
		// on reactive le listener sur le comboBox
		this.cbDirectAccess.addActionListener(ActionManager.getInstance()
				.getAction(Constants.ACTION_GOTOSTEP));
	}
}
