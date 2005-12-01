/*
 * Projet PAGOD
 * 
 * $Id: OpenProjectAction.java,v 1.2 2005/12/01 16:22:00 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * @author yak
 * 
 */
public class OpenProjectAction extends AbstractPagodAction
{

	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public OpenProjectAction ()
			throws LanguagesManager.NotInitializedException, IOException,
			ImagesManager.NotInitializedException
	{
		// TODO CHANGER ICONE ET KEYSTROKE
		super("openProject", "OpenIcon.gif", new Request(
				Request.RequestType.OPEN_PROJECT), KeyStroke.getKeyStroke(
				KeyEvent.VK_P, KeyEvent.CTRL_MASK));
	}

	/**
	 * Methode appélée lorsque l'action est déclenché
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// si le project a pu etre ouvert alors on delegue la requete à
		// l'application manager
		if (ApplicationManager.getInstance().getMfPagod().openProject())
		{
			ApplicationManager.getInstance().manageRequest(this.request);

			// si le projet a un processus d'affecter on declanche l'action
			// openProcess
			if (!ApplicationManager.getInstance().getCurrentProject()
					.hasCurrentProcess())
			{
				// si le processus a pu etre ouvert alors on delegue la requete
				// à l'application manager
				if (ApplicationManager.getInstance().getMfPagod().openProcess())
				{
					ApplicationManager.getInstance()
							.manageRequest(new Request(Request.RequestType.OPEN_PROCESS));
				}
			}
			else
			{
				// il y a un processus d'assicie donc on l'affiche
				ApplicationManager.getInstance().getMfPagod().showProcess();
			}
		}
	}

}
