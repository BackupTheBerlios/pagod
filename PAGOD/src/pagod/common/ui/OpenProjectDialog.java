/*
 * Projet PAGOD
 * 
 * $Id: OpenProjectDialog.java,v 1.1 2005/12/01 17:50:04 biniou Exp $
 */
package pagod.common.ui;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pagod.common.model.Project;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;

/**
 * @author biniou
 * 
 */
public class OpenProjectDialog extends JDialog
{

	/* combobox qui contiendra la liste des projets */
	private JComboBox	cbProject	= null;

	/* Attribut contenant les boutons */
	private JButton		btOpen		= null;

	private JButton		btCancel;

	/**
	 * constructeur de la newprojectdialog
	 * 
	 * @param parent
	 */
	public OpenProjectDialog (JFrame parent)
	{
		// appel de la methode parente
		super(parent, LanguagesManager.getInstance().getString(
				"OpenProjectTitre"), true);

		this.setFocusable(true);

			// on spécifie les boutons avec leur titre
			// TODO ressources
			this.btOpen = new JButton(LanguagesManager.getInstance().getString(
					"OpenProjectBtOpen"));
			this.btCancel = new JButton(LanguagesManager.getInstance()
					.getString("OpenProjectBtCancel"));

			/* panneau qui va contenir la combobox */
			JPanel centralPanel = new JPanel();
			
			if (initializeComboBox())
			{
				centralPanel.add(this.cbProject);
			}
			else
			{
				centralPanel.add(new JComboBox());
				JOptionPane.showMessageDialog(OpenProjectDialog.this,
						LanguagesManager.getInstance().getString(
								"OpenProjectErrorOpenException"),
						LanguagesManager.getInstance().getString(
								"OpenProjectErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
				
			}
			// panneau qui va contenir les boutons
			JPanel bottomPanel = new JPanel();

			// on ajoute les boutons
			bottomPanel.add(this.btOpen);
			bottomPanel.add(this.btCancel);

			// positionnement des deux panels
			this.getContentPane().add(centralPanel, BorderLayout.CENTER);
			this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

			/*
			 * on ajoute une inner class anonyme (classe interne) faisant en
			 * sorte que la croix en haut a droite ferme bien la fenetre
			 */
			this.addWindowListener(new WindowAdapter()
			{
				// on redefinit la methode windowsClosing heritee de
				// WindowAdapter
				void windowsClosing (WindowEvent e)
				{
					/*
					 * on ferme la fenetre
					 */
					OpenProjectDialog.this.dispose();
				}
			});

			// parametrage des boutons
			this.btOpen.addActionListener(new ButtonListener());
			this.btCancel.addActionListener(new ButtonListener());

			this.pack();
			this.setLocationRelativeTo(parent);		
	}

	/**
	 * remplit la combobox
	 * @return vrai si la combobox est remplie, faux sinon
	 */
	public boolean initializeComboBox ()
	{
		boolean resultat = false;

		List<File> directoryList = new ArrayList<File>();
		List<File> validList = new ArrayList<File>();

		// on recupere tous les repertoires qui sont dans le workspace
		File workspaceDirectory = new File(PreferencesManager.getInstance()
				.getWorkspace());
		File listTemp[] = workspaceDirectory.listFiles();

		for (File currentFile : listTemp)
		{
			if (currentFile.isDirectory())
			{
				directoryList.add(currentFile);
			}
		}

		// une fois qu'on a tous les repertoires on vérifie
		// qu'ils soient des projects valides
		// il existe le documentation.properties et le repertoire doc

		if (directoryList.size() != 0)
		{
			for (File currentFile : directoryList)
			{
				File tempPropertiesFile = new File(currentFile
						.getAbsolutePath()
						+ File.separator + "documentation.properties");
				File tempDocsFile = new File(currentFile.getAbsolutePath()
						+ File.separator + Project.DOCS_DIRECTORY);

				if (tempPropertiesFile.exists() && tempDocsFile.exists())
				{
					validList.add(currentFile);
				}

			}

			if (validList.size() != 0)
			{
				// on transforme cette liste en liste de string
				this.cbProject = new JComboBox();

				for (File currentFile : validList)
				{
					this.cbProject.addItem(currentFile.getName());	
				}
				resultat = true;		
			}
		}
		return (resultat);

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
			 * si on a cliqué sur le bouton open
			 */

			if (b == OpenProjectDialog.this.btOpen)
			{
				String sProjectName = (String) OpenProjectDialog.this.cbProject
						.getSelectedItem();
				// on crée une instance du projet a ouvrir et on l'associe
				// a l'applicationmanager
				Project choosenProject = new Project(sProjectName);

				ApplicationManager.getInstance().setCurrentProject(
						choosenProject);
				
				// on ferme la fenetre, l'application manager se chargera
				// d'afficher le processus et de charger le projet
				OpenProjectDialog.this.dispose();

			}
			else
			{
				// bouton cancel : on ferme la fenetre
				OpenProjectDialog.this.dispose();
			}
		}
	}

}
