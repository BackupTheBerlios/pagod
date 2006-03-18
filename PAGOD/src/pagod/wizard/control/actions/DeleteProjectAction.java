/*
 * Projet PAGOD
 * 
 * $Id$
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.DeleteProjectDialog;

/**
 * @author biniou
 *
 */
public class DeleteProjectAction extends AbstractPagodAction
{
	/**
	 * @throws ImagesManager.NotInitializedException
	 * @throws IOException
	 * @throws LanguagesManager.NotInitializedException
	 */
	public DeleteProjectAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{
		//TODO changer l'icone
		
		super("deleteProject", "OpenIcon.gif", null);
	}
	
	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		DeleteProjectDialog dpd = new DeleteProjectDialog(ApplicationManager.getInstance().getMfPagod());
		dpd.setVisible(true);
	}
}
