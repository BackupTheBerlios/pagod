/*
 * Projet PAGOD
 * 
 * $Id: StepListPanel.java,v 1.6 2006/03/02 21:02:05 cyberal82 Exp $
 */
package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import pagod.common.model.Step;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.StepState;

/**
 * Classe permettant d'afficher un panel contenant la liste des étapes 
 * d'une activité. Lorsqu'on double clique sur une étape l'assistant
 * l'affiche.
 * 
 * @author psyko
 * 
 */
public class StepListPanel extends JPanel
{
	private JList			lStepList;

	private List<Request>	lRequest;

	/**
	 * constructeur
	 * 
	 * @param actSched
	 *            l'activity scheduler
	 */
	public StepListPanel (ActivityScheduler actSched)
	{
		super(new BorderLayout());

		// on remplit la JList
		Vector<Request> listData = new Vector<Request>();

		// initialisation de la liste avec les noms des steps de l'activit? en
		// cours maintenant, on remplit cette liste
		for (AbstractActivityState abstrActState : actSched.getStateList())
		{
			if (abstrActState instanceof StepState)
			{
				listData.add(new Request(Request.RequestType.GOTOSTEP,
						abstrActState));
			}
		}

		// on garde les requetes de la JList pour pouvoir plus facilement
		// selectionner une etape
		this.lRequest = new ArrayList<Request>(listData);

		// on initialise la JList ? l aide du modele
		this.lStepList = new JList(listData);

		// on sp?cifie qu un seul ?l?ment sera clicable ? la fois
		this.lStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// on rajoute un listener qui, lors du double clic, va aller directement
		// ? l'?tape
		this.lStepList.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent me)
			{
				if (me.getClickCount() == 2)
				{
					// goToStep();

					ApplicationManager.getInstance().manageRequest(
							(Request) StepListPanel.this.lStepList
									.getSelectedValue());
					// me.consume();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(this.lStepList);

		// ajuste la taille prefererer pour qu'on puisse voir 5 etapes au
		// maximum
		// je mets 6 car si je mets 5 ca en affiche que 4 au max :(
		if (this.lStepList.getModel().getSize() > 6)
		{
			this.lStepList.setVisibleRowCount(6);
		}
		else
		{
			this.lStepList.setVisibleRowCount(this.lStepList.getModel()
					.getSize());
		}
		this.add(scrollPane, BorderLayout.CENTER);
		// this.add(centerPanel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Selectionne l'etape passé en parametre dans le StepListPanel
	 * 
	 * @param step
	 *            l'etape que l'on veut voir selectionne dans le StepListPanel
	 */
	void selectedStep (Step step)
	{
		int index = 0;

		// pour chacune des requetes
		for (Request aRequest : this.lRequest)
		{
			index++;
			try
			{
				// on met ce code entre try catch car il peut y avoir des pb de
				// cast
				StepState aStepState = (StepState) aRequest.getContent();

				// on selectionne l'etape
				// TODO peut etre equals
				if (aStepState.getStep() == step)
				{
					// on selectionne l'etape (on fait -1 car la premiere etape
					// commence a l'indice 0)
					this.lStepList.setSelectedIndex(index - 1);
					return;
				}
			}
			catch (ClassCastException e)
			{
				// on fait rien
			}
		}

	}
}
