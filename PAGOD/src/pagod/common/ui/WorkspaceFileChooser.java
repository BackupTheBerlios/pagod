/*
 * Projet PAGOD
 * 
 * $Id: WorkspaceFileChooser.java,v 1.1 2005/11/19 16:15:00 biniou Exp $
 */
package pagod.common.ui;

import java.util.MissingResourceException;

import javax.swing.JFileChooser;

import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * @author biniou
 *
 */
public class WorkspaceFileChooser extends JFileChooser
{
	 /**
     * Constructeur de la fenetre de selection du workspace pour le
     * 1er lancement de pagod
     * 
     * @throws NotInitializedException
     *             Si le gestionnaire de langues n'est pas initialisé
     * @throws MissingResourceException
     *             Si une clé n'existe pas dans le fichier de langues
     */
	
	public WorkspaceFileChooser()
    {
		super();
        // initialisation du titre
        this.setDialogTitle(LanguagesManager.getInstance().getString(
                "workspaceFileChooserTitle"));
        
        // on n'affiche que les répertoires        
        this.setFileSelectionMode(DIRECTORIES_ONLY);
 
		
    }
	
	
}
