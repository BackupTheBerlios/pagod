/*
 * Projet PAGOD
 * 
 * $Id: OpenProjectAction.java,v 1.7 2006/01/25 09:21:23 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.common.model.Activity;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.TimeHandler;
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
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// si le project a pu etre ouvert alors on delegue la requete ?
		// l'application manager
		if (ApplicationManager.getInstance().getMfPagod().openProject())
		{

			// on stop le timer
			if (TimerManager.getInstance().isStarted())
			{
				TimerManager.getInstance().stop();
				Activity aTemp = ApplicationManager.getInstance().getMfPagod()
						.getActivity();
				// on enregistre le temps pour l'activit?
				aTemp.setTime(TimerManager.getInstance().getValue());
			}

			ApplicationManager.getInstance().manageRequest(this.request);

			if (!ApplicationManager.getInstance().getCurrentProject()
					.hasNameDPC())
			{
				// si le processus a pu etre ouvert alors on delegue la requete
				// ? l'application manager
				if (ApplicationManager.getInstance().getMfPagod()
						.associateDPCWithProject())
				{
					ApplicationManager.getInstance().manageRequest(
							new Request(Request.RequestType.OPEN_PROCESS));
					TimeHandler th = new TimeHandler ();
					System.out.println("fabfabafabbba");
					th.loadXML(ApplicationManager.getInstance().getCurrentProject().getName());
					th.fillModel(ApplicationManager.getInstance().getCurrentProcess() );
				}
			}
			else
			{
				// si le projet a un processus d'affecter on declanche l'action
				// openProcess

				File processFile = new File(PreferencesManager.getInstance()
						.getWorkspace()
						+ File.separator
						+ ApplicationManager.getInstance().getCurrentProject()
								.getName()
						+ File.separator
						+ ApplicationManager.getInstance().getCurrentProject()
								.getNameDPC());
				if (ApplicationManager.getInstance().getMfPagod().openProcess(
						processFile))
				{
					ApplicationManager.getInstance().manageRequest(
							new Request(Request.RequestType.OPEN_PROCESS));
					TimeHandler th = new TimeHandler ();
					th.loadXML(ApplicationManager.getInstance().getCurrentProject().getName());
					System.out.println("openprject ");
					th.affiche() ;
					th.fillModel(ApplicationManager.getInstance().getCurrentProcess() );
					ApplicationManager.getInstance().getMfPagod().showProcess();
				}

			}
		}
	}

}
