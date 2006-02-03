/*
 * Projet PAGOD
 * 
 * $Id: NewProjectAction.java,v 1.6 2006/02/03 13:02:54 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.configurator.control.PreferencesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.TimeHandler;
import pagod.wizard.control.states.Request;

/**
 * @author yak
 * 
 */
public class NewProjectAction extends AbstractPagodAction
{

	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public NewProjectAction () throws LanguagesManager.NotInitializedException,
			IOException
	{
		// TODO CHANGER ICONE ET KEYSTROKE
		super("newProject", "OpenIcon.gif", new Request(
				Request.RequestType.OPEN_PROJECT), KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_MASK));
	}

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// si le project a pu etre ouvert alors on delegue la requete ?
		// l'application manager
		if (ApplicationManager.getInstance().getMfPagod().newProject())
		{
			ApplicationManager.getInstance().manageRequest(this.request);

			// si le processus a pu etre ouvert alors on delegue la requete
			// ? l'application manager
			if (ApplicationManager.getInstance().getMfPagod().associateDPCWithProject())
			{
				ApplicationManager.getInstance().manageRequest(
						new Request(Request.RequestType.OPEN_PROCESS));
			}
			TimeHandler th = new TimeHandler ();
			System.out.println("test fab chargement du fichier");
			//th.loadXML(ApplicationManager.getInstance().getCurrentProject().getName());
			th.loadModel( ApplicationManager.getInstance().getCurrentProcess());
			th.affiche() ;
			th.writeXML(ApplicationManager.getInstance().getCurrentProject().getName()  );
		}
	}

}
