/*
 * Projet PAGOD
 * 
 * $Id: ImportProjectAction.java,v 1.3 2006/03/19 18:21:47 cyberal82 Exp $
 */

package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pagod.common.control.actions.CustomAction;
import pagod.common.model.Project;
import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;

/**
 * Action pour importer des projets d'un workspace vers un autre
 * 
 * @author Flotueur
 * 
 */
public class ImportProjectAction extends CustomAction
{
	/**
	 * 
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public ImportProjectAction ()
			throws LanguagesManager.NotInitializedException, IOException,
			ImagesManager.NotInitializedException
	{
		// TODO faire l'icone appropriee
		super("importproject", "OpenIcon.gif");
	}

	/**
	 * Methode appele lorsque l'action est declanchee
	 * 
	 * @param e
	 *            Evenement survenue
	 * 
	 */
	public void actionPerformed (ActionEvent e)
	{
		// Recuperation du workspace
		File currentWorkspace = new File(PreferencesManager.getInstance()
				.getWorkspace());

		// creation et affichage de l'arborescence
		// a partir du poste de travail
		WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();

		// une fois qu'on a choisi une application
		if (workspaceChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{

			// Recupere le path du fichier choisi
			File selectedPath = workspaceChooser.getSelectedFile();

			// On teste si le répertoire est un projet valide
			if (Project.isValidProjectDirectory(selectedPath))
			{
				// On créé le répertoire projet de destination
				File targetdir = new File(currentWorkspace.getAbsolutePath()
						+ File.separator + selectedPath.getName());
				
				// TODO A SUPPR targetdir.mkdir();
				
				// On recopie le répertoire
				if (targetdir.mkdir() /*&& targetdir.exists()*/
						&& !selectedPath.equals(targetdir)
						&& FilesManager.copyDirectory(selectedPath, targetdir,
								targetdir))
				{
					// Le répertoire a bien été copié, on affiche un message à
					// l'utilisateur
					JOptionPane.showMessageDialog(ApplicationManager
							.getInstance().getMfPagod(), LanguagesManager
							.getInstance().getString("infoImportProject"),
							LanguagesManager.getInstance().getString(
									"titleInfoImportProject"),
							JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					// Il y a eu une erreur durant la copie et l'import ne s'est
					// pas fait, on avertit l'utilisateur
					JOptionPane.showMessageDialog(ApplicationManager
							.getInstance().getMfPagod(), LanguagesManager
							.getInstance().getString("ImportProjectException"),
							LanguagesManager.getInstance().getString(
									"ImportProjectErrorTitle"),
							JOptionPane.ERROR_MESSAGE);

				}
			}
			else
			{
				// le répertoire n'est pas un projet valide, on prévient
				// l'utilisateur
				JOptionPane.showMessageDialog(ApplicationManager.getInstance()
						.getMfPagod(), LanguagesManager.getInstance()
						.getString("ImportProjectNull"), LanguagesManager
						.getInstance().getString("ImportProjectErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}
}
