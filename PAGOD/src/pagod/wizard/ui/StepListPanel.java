/*
 * Projet PAGOD
 * 
 * $Id: StepListPanel.java,v 1.1 2006/02/02 23:39:52 psyko Exp $
 */
package pagod.wizard.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.StepState;

/**
 * @author psyko
 *
 */
public class StepListPanel extends JSplitPane
{
	private JList lStepList;
		
	StepListPanel(ActivityScheduler actSched)
	{
		super();
		
		// on initialise notre JList
        this.lStepList = new JList();
        
        // on vide notre JList (elements affichés et Listeners)
        // juste pr etre bien sur que ça soit propre  ... 
		this.lStepList.removeAll();
        
		// on crée  le modele de la JList
        DefaultListModel listModelJList = new DefaultListModel();
		
        // initialisation de la liste avec les noms des steps de l'activité en cours
		// maintenant, on remplit cette liste
		for(AbstractActivityState abstrActState : actSched.getStateList())
		{
			if (abstrActState instanceof StepState)
			{
				listModelJList.addElement(				
						new Request(Request.RequestType.GOTOSTEP, abstrActState));
			}
		}
				
		// on initialise la JList à l aide du modele
		this.lStepList = new JList(listModelJList);
		
		// on spécifie qu un seul élément sera clicable à la fois
		this.lStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// on rajoute un listener qui, lors du double clic, va aller directement à l'étape
        this.lStepList.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                if (me.getClickCount() == 2)
                {
                    goToStep();
                    me.consume();
                }
            }
        });
	}    
		
	//méthode chargée de l'acces direct 
	protected void goToStep()
	{	    
	    // on effectue la requete GOTOSTEP !
	    ApplicationManager.getInstance().manageRequest((Request)this.lStepList.getSelectedValue());
    }
	    
}

