/*
 * Projet PAGOD
 * 
 * $Id: StepListPanel.java,v 1.2 2006/02/03 14:41:02 coincoin Exp $
 */
package pagod.wizard.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
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
public class StepListPanel extends JPanel
{
	private JList lStepList;
		
	/**
	 * constructeur
	 */
	public StepListPanel()
	{
		super();
		
		// on initialise notre JList
        this.lStepList = new JList();
        
        // on vide notre JList (elements affich?s et Listeners)
        // juste pr etre bien sur que ?a soit propre  ... 
		this.lStepList.removeAll();
		this.add(this.lStepList);
	}
	
	/**
	 * @param actSched
	 * methode qui initialise la JList
	 */
	public void initJList(ActivityScheduler actSched)
	{    
		// on cr?e  le modele de la JList
        DefaultListModel listModelJList = new DefaultListModel();
		
        // initialisation de la liste avec les noms des steps de l'activit? en cours
		// maintenant, on remplit cette liste
		for(AbstractActivityState abstrActState : actSched.getStateList())
		{
			if (abstrActState instanceof StepState)
			{
				listModelJList.addElement(				
						new Request(Request.RequestType.GOTOSTEP, abstrActState));
			}
		}
				
		// on initialise la JList ? l aide du modele
		this.lStepList = new JList(listModelJList);
		
		// on sp?cifie qu un seul ?l?ment sera clicable ? la fois
		this.lStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// on rajoute un listener qui, lors du double clic, va aller directement ? l'?tape
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
		
	//m?thode charg?e de l'acces direct 
	protected void goToStep()
	{	    
	    // on effectue la requete GOTOSTEP !
	    ApplicationManager.getInstance().manageRequest((Request)this.lStepList.getSelectedValue());
    }
	    
	
}

