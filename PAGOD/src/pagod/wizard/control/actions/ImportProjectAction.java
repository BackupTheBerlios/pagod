/*
 * Projet PAGOD
 * 
 * $Id: ImportProjectAction.java,v 1.1 2006/03/09 19:17:41 flotueur Exp $
 */

package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import pagod.common.control.actions.CustomAction;
import pagod.common.model.Project;
import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
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
	public ImportProjectAction() throws LanguagesManager.NotInitializedException,
    										IOException,
    										ImagesManager.NotInitializedException
	{
    	// TODO faire l'icone appropriee
		super("importproject", "importproject.gif");
	}

	/**
	 * Methode appele lorsque l'action est declanchee
	 * @param e 
	 * 			Evenement survenue
	 * 
	 */
	public void actionPerformed (ActionEvent e)
	{
        // Recuperation du workspace
        File currentWorkspace = new File(PreferencesManager.getInstance().getWorkspace());
        
        // creation et affichage de l'arborescence
        // a partir du poste de travail
        WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();
       
        // une fois qu'on a choisi une application
        if (workspaceChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {

            // Recupere le path du fichier choisi
            File selectedPath = workspaceChooser.getSelectedFile();
            
    		//Nous sommes dans un repertoire, on liste donc le contenu
            File[] list = selectedPath.listFiles();
            
            for ( int i = 0; i < list.length; i++) {
            	// Pour chaque répertoire dans le répertoire passé en paramètre
            	// si le répertoire est un répertoire projet, il est recopié dans le workspace
            	if (Project.isValidProjectDirectory(list[i])){
            		// On créé le répertoire projet de destination
            		File targetdir = new File(currentWorkspace.getAbsolutePath()+File.separator+list[i].getName());
            		// On recopie le répertoire
            		FilesManager.copyDirectory(list[i],targetdir,targetdir);
            	}

            }
        }

	}
}
