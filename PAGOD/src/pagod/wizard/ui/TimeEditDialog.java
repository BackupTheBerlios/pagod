/*
 * Projet PAGOD
 * 
 * $Id: TimeEditDialog.java,v 1.1 2006/02/02 15:05:16 psyko Exp $
 */

package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pagod.common.model.Activity;
import pagod.utils.LanguagesManager;


/**
 * @author psyko
 * JDialog permettant l'edition des temps
 * (effectu� et � faire) pour une activit�
 * Charg�e lors du lancement d'une activit�
 * ou de la terminaison d'une activit�
 * 
 * R�cup�re les temps d'une HashMap d'Activity
 * => constructeur param�tr�
 */
public class TimeEditDialog extends JDialog implements ActionListener
{
	//attributs de la classe ... 
	// deux boutons, (valider et annuler)
	private JButton pbValidate;
	private JButton pbCancel;
	
	//deux zones de texte
	private JLabel lEstimatedTime;
	private JLabel lPassedTime;
	
	// et deux champs de saisie
	private JTextField tfEstimatedTime;
	private JTextField tfPassedTime;
	
	// panneaux qui contiendront les composants de la JDialog
	private JPanel pButton;
	private JPanel pEast;
	private JPanel pWest;
	
	// activit� dont on configure les temps
	private Activity activity;
	
	/**
	 * constructeur prenant en param�tre le temps effectu�
	 * et le temps estim�... 
	 * @param dParent 
	 * @param activity 
	 */
	public TimeEditDialog(JFrame dParent, Activity activity)
	{
		super(dParent,
                LanguagesManager.getInstance().getString("TimeEditDialogTitle")+
                activity.getName(), 
                true);
		
		// initialisation de notre attribut activity
		this.activity = activity;
		
		// creation de nos 2 boutons
		this.pbValidate = new JButton(
				LanguagesManager.getInstance().getString("OKButtonLabel"));
		this.pbCancel = new JButton(
				LanguagesManager.getInstance().getString("CancelButtonLabel"));
		
		// creation de nos 2 JTextField
		this.tfEstimatedTime = new JTextField("00:00:00");
		this.tfPassedTime = new JTextField("00:00:00");
		
		// creation de nos 2 JLabel
		this.lEstimatedTime = new JLabel(
				LanguagesManager.getInstance().getString("EstimatedTimeLabel")+" :");
		this.lPassedTime = new JLabel(
				LanguagesManager.getInstance().getString("PassedTimeLabel")+" :");
				
		// panel contenant les boutons
		this.pButton = new JPanel();
		this.pButton.add(this.pbValidate);
		this.pButton.add(this.pbCancel);

		// association des listeners aux boutons
		this.pbValidate.addActionListener(this);
		this.pbCancel.addActionListener(this);
		
		// cr�ation du panel contenant les JLabel
		this.pWest = new JPanel();
		this.pWest.setLayout(new BorderLayout());
		this.pWest.add(this.lEstimatedTime,BorderLayout.NORTH);
		this.pWest.add(this.lPassedTime,BorderLayout.SOUTH);
		
		//creation du panel contenant les JTextField
		this.pEast = new JPanel();
		this.pEast.setLayout(new BorderLayout());
		this.pEast.add(this.tfEstimatedTime,BorderLayout.NORTH);
		this.pEast.add(this.tfPassedTime,BorderLayout.SOUTH);
		
		// positionnement des panel dans la JDialog
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.pButton, BorderLayout.SOUTH);
		this.getContentPane().add(this.pWest,BorderLayout.WEST);
		this.getContentPane().add(this.pEast,BorderLayout.EAST);
		
        this.addWindowListener(
        new WindowAdapter()
        {
            // on redefinit la methode windowsClosing heritee de WindowAdapter
            void windowsClosing(WindowEvent e)
            {
                /*
                 * on ferme la fenetre une inner class peut acceder aux
                 * attributs de la classe qui la contient en donnant sont nom
                 * suivit de .this puis le nom de l'attribut ou de la methode
                 */
                TimeEditDialog.this.dispose();

            }
        });
		
		this.pack();

		// bo?te de dialogue centree par rapport a l'appelant
		this.setLocationRelativeTo(dParent);
		this.setVisible(true);
	}

	/** 
	 * m�thode de validation des temps
	 * enregistre dans la HashMap de l'activit� les temps
	 * pass� et � faire
	 */
	public void validate()
	{
		// TODO : le corps de la m�thode !! 
		
		
	}
	
	/** 
	 * @param e : actionEvent 
	 *  
	 */
	public void actionPerformed (ActionEvent e)
	{
		// on recupere le bouton sur lequel on a clique grace a
		// l'ActionEvent e
		JButton pb = (JButton) e.getSource();

		// si on a clique sur le bouton ok
		if (pb == TimeEditDialog.this.pbValidate)
		{
			// on a cliqu� sur ok			
			this.validate();
			
			// on ferme la fenetre
			TimeEditDialog.this.dispose();
		}
		
		// bouton annuler
		else
		{
			// on ne prend aucun changement en compte, on ferme la fenetre
			TimeEditDialog.this.dispose();
		}
	}
	

}