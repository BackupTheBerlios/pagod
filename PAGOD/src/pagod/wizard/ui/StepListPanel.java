/*
 * Projet PAGOD
 * 
 * $Id: StepListPanel.java,v 1.3 2006/02/05 17:02:09 psyko Exp $
 */
package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

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
		super(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// on initialise notre JList
        this.lStepList = new JList();
        
        // on vide notre JList (elements affich?s et Listeners)
        // juste pr etre bien sur que ?a soit propre  ... 
		this.lStepList.removeAll();
		
		centerPanel.add(this.lStepList);
		centerPanel.setBackground(Color.WHITE);
		this.add(centerPanel, BorderLayout.CENTER);
		
	}
	
	/**
	 * @param actSched
	 * methode qui initialise la JList
	 */
	public void initJList(ActivityScheduler actSched)
	{    
		Vector<Request> listData = new Vector<Request>();
               
        // initialisation de la liste avec les noms des steps de l'activit? en cours
		// maintenant, on remplit cette liste
		for(AbstractActivityState abstrActState : actSched.getStateList())
		{
			if (abstrActState instanceof StepState)
			{
				listData.add(				
						new Request(Request.RequestType.GOTOSTEP, abstrActState));				
			}
		}
		
		// on initialise la JList ? l aide du modele
		this.lStepList.setListData(listData);
		
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
        
        this.setMaximumSize(this.lStepList.getSize());
        this.setMinimumSize(this.lStepList.getSize());
        this.setPreferredSize(this.lStepList.getSize());
	}    
		
	//m?thode charg?e de l'acces direct 
	protected void goToStep()
	{	    
	    // on effectue la requete GOTOSTEP !
	    ApplicationManager.getInstance().manageRequest((Request)this.lStepList.getSelectedValue());
    }
}

