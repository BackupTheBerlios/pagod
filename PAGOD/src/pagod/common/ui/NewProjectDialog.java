/*
 * Projet PAGOD
 * 
 * $Id: NewProjectDialog.java,v 1.5 2005/12/04 17:54:15 yak Exp $
 */
package pagod.common.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pagod.common.model.Project;
import pagod.utils.LanguagesManager;

/**
 * TODO cette fenetre n'est jamais disposée, c'est l'appelant de faire le dispose()
 * @author biniou
 * 
 */
public class NewProjectDialog extends JDialog
{
	// JLabel qui contient l'extension si on la modifie
	private JTextField	txtProjectName	= new JTextField(30);

	/* Attribut contenant les boutons */
	private JButton		btCreate		= null;

	private JButton		btCancel;

	private Project		createdProject = null;

	/**
	 * constructeur de la newprojectdialog
	 * 
	 * @param parent
	 */
	public NewProjectDialog (JFrame parent)
	{
		// appel de la methode parente
		super(parent, LanguagesManager.getInstance().getString(
				"NewProjectTitre"), true);

		this.setFocusable(true);

		// on spécifie les boutons avec leur titre
		this.btCreate = new JButton(LanguagesManager.getInstance().getString(
				"NewProjectBtCreate"));
		this.btCancel = new JButton(LanguagesManager.getInstance().getString(
				"NewProjectBtCancel"));

		JLabel lblProjectName = new JLabel(LanguagesManager.getInstance()
				.getString("NewProjectLabel"));

		/* panneau qui va contenir le JLabel et le JTextField */
		JPanel centralPanel = new JPanel();

		/*
		 * on met un GridLayout qui sera un tableau a 1 lignes et 2 colonnes
		 */
		GridBagLayout gridBag = new GridBagLayout();
		centralPanel.setLayout(gridBag);

		// contraintes pour l'ajout des composants
		GridBagConstraints constraints = new GridBagConstraints();

		// on y ajoute le JLabel et le JTextField

		// on ajoute le label
		// on definit les contraintes pour le label
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;

		// on lie les contraintes au label
		gridBag.setConstraints(lblProjectName, constraints);
		centralPanel.add(lblProjectName);

		// on ajoute le textField
		// on definit les contraintes pour le textField
		constraints.gridx = 1;
		constraints.gridy = 0;

		// on lie les contraintes au textField
		gridBag.setConstraints(this.txtProjectName, constraints);
		centralPanel.add(this.txtProjectName);

		// panneau qui va contenir les boutons
		JPanel bottomPanel = new JPanel();

		// on ajoute les boutons
		bottomPanel.add(this.btCreate);
		bottomPanel.add(this.btCancel);

		// positionnement des deux panels
		this.getContentPane().add(centralPanel, BorderLayout.CENTER);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		/*
		 * on ajoute une inner class anonyme (classe interne) faisant en sorte
		 * que la croix en haut a droite ferme bien la fenetre
		 */
		this.addWindowListener(new WindowAdapter()
		{
			// on redefinit la methode windowsClosing heritee de WindowAdapter
			void windowsClosing (WindowEvent e)
			{
				/*
				 * on ferme la fenetre
				 */
				NewProjectDialog.this.dispose();
			}
		});

		// parametrage des boutons
		this.btCreate.addActionListener(new ButtonListener());
		this.btCancel.addActionListener(new ButtonListener());

		// ajout de listener sur le texte field
		this.txtProjectName.setFocusable(true);
		this.txtProjectName.addCaretListener(new ListenerTextField());

		// et on desactive le bouton
		NewProjectDialog.this.btCreate.setEnabled(false);

		this.pack();
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}

	private class ListenerTextField implements CaretListener
	{
		/**
		 * @param arg0
		 */
		public void caretUpdate (CaretEvent arg0)
		{
			if (!(NewProjectDialog.this.txtProjectName.getText().equals("")))

			{
				if (NewProjectDialog.this.btCreate != null) NewProjectDialog.this.btCreate
						.setEnabled(true);
			}
			else
			{
				if (NewProjectDialog.this.btCreate != null) NewProjectDialog.this.btCreate
						.setEnabled(false);
			}
		}
	}

	private class ButtonListener implements ActionListener
	{
		/**
		 * @param e
		 */
		public void actionPerformed (ActionEvent e)
		{
			// on recupere le bouton sur lequel on a clique grace a
			// l'ActionEvent e
			JButton b = (JButton) e.getSource();

			/*
			 * si on a cliqué sur le bouton create
			 */

			if (b == NewProjectDialog.this.btCreate)
			{
				// on supprime les espaces du nom du projet au cas ou
				// et on les remplace par des _
				String sProjectName = NewProjectDialog.this.txtProjectName
						.getText().replaceAll(" ", "_");

				// on va verifier que le champs est bien rempli
				Project newProject = new Project(sProjectName);

				try
				{
					if (newProject.createProject(sProjectName))
					{
						// tout s'est bien déroulé, on designe le projet comme
						// etant le projet courant et on ferme la fenetre
						NewProjectDialog.this.createdProject = newProject;
						NewProjectDialog.this.setVisible(false);
					}
					else
					{
						// erreur
						JOptionPane.showMessageDialog(NewProjectDialog.this,
								LanguagesManager.getInstance().getString(
										"NewProjectErrorCreateException"),
								LanguagesManager.getInstance().getString(
										"NewProjectErrorTitle"),
								JOptionPane.ERROR_MESSAGE);
					}
				}
				catch (HeadlessException e1)
				{
					// TODO Bloc de traitement des exceptions généré
					// automatiquement
					e1.printStackTrace();
				}
				catch (IOException e1)
				{
					// TODO Bloc de traitement des exceptions généré
					// automatiquement
					e1.printStackTrace();
				}

				// on revient à la fenetre
				NewProjectDialog.this.txtProjectName.selectAll();

			}
			else
			{
				// bouton cancel : on ferme la fenetre
				NewProjectDialog.this.setVisible(false);
			}
		}
	}

	/**
	 * @return Retourne l'attribut newProject
	 */
	public Project getCreatedProject ()
	{
		
		return this.createdProject;
	}
}
