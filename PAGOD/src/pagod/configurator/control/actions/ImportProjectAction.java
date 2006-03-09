/*
 * Projet PAGOD
 * 
 * $Id: ImportProjectAction.java,v 1.2 2006/03/09 00:40:47 psyko Exp $
 */

package pagod.configurator.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.common.control.actions.CustomAction;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * 
 * @author l3isi31
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
		super("preferences", "PreferencesIcon.gif");
	}

	/**
	 * Methode appele lorsque l'action est declanchee
	 * @param e 
	 * 
	 */
	public void actionPerformed (ActionEvent e)
	{
		// TODO Corps de m?thode g?n?r? automatiquement
		
	}
}
