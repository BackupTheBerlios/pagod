/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityDialog.java,v 1.3 2006/01/15 09:21:15 biniou Exp $
 */
package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import pagod.utils.LanguagesManager;

/**
 * @author biniou
 * 
 */
public class TimeActivityDialog extends JDialog implements ActionListener
{

	// panel contenant la JTable
	private JPanel	pCentral	= null;

	// panel qui contiendra les boutons du bas
	private JPanel	pButton		= null;

	// Bouton Valider
	private JButton	pbOk		= null;

	// Bouton Annuler
	private JButton	pbCancel	= null;

	// Jtable
	private JTable	table;

	/**
	 * @param parentFrame
	 */
	public TimeActivityDialog (JFrame parentFrame)
	{
		super(parentFrame);

		// boite de dialogue modale
		this.setModal(true);

		// titre
		this.setTitle(LanguagesManager.getInstance().getString(
				"TimeActivityDialogTitle"));

		// creation de la JTable
		this.table = new JTable();

		// panel qui va contenir la JTable
		// TODO remplir la JTable, définir le model ...
		this.pCentral = new JPanel();
		this.pCentral.add(this.table);

		// panel contenant les boutons
		this.pButton = new JPanel();

		// creation des boutons Ok et annuler
		this.pbOk = new JButton(LanguagesManager.getInstance().getString(
				"OKButtonLabel"));
		this.pbCancel = new JButton(LanguagesManager.getInstance().getString(
				"CancelButtonLabel"));

		// ajout des boutons au panel
		this.pButton.add(this.pbOk);
		this.pButton.add(this.pbCancel);

		// association des listeners aux boutons
		this.pbOk.addActionListener(this);
		this.pbCancel.addActionListener(this);

		// positionnement des panel dans la JDialog
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.pCentral, BorderLayout.CENTER);
		this.getContentPane().add(this.pButton, BorderLayout.SOUTH);

		this.pack();

		// boîte de dialogue centrée par rapport à l'appelant
		this.setLocationRelativeTo(parentFrame);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	public void actionPerformed (ActionEvent e)
	{
		// on recupere le bouton sur lequel on a clique grace a
		// l'ActionEvent e
		JButton b = (JButton) e.getSource();

		// si on a cliqué sur le bouton ok
		if (b == TimeActivityDialog.this.pbOk)
		{
			// on a cliqué sur ok
			// TODO mettre a jour le .xml avec les nouvelles valeurs
			TimeActivityDialog.this.dispose();
		}
		else
		{
			// bouton annuler
			TimeActivityDialog.this.dispose();
		}

	}
}
