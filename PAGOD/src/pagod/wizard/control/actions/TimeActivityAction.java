/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAction.java,v 1.1 2006/01/13 14:28:43 biniou Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.common.ui.AboutDialog;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * @author m1isi13
 *
 */
public class TimeActivityAction extends AbstractPagodAction
{
	/**
	 * @throws ImagesManager.NotInitializedException
	 * @throws IOException
	 * @throws LanguagesManager.NotInitializedException
	 */
	public TimeActivityAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{
		// TODO remplacer par la bonne action (timeActivity)
		//TODO rajouter valeurs dans le bundle
		super("about", "AboutIcon.gif", new Request(
				Request.RequestType.SHOW_ABOUT));
	}
	
	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// TODO enlever les commentaires quand on aura code
		// la fenetre
		/*AboutDialog ad = new TimeActivityDialog(ApplicationManager.getInstance().getMfPagod(),
				Constants.APPLICATION_SHORT_NAME + " "
						+ Constants.APPLICATION_VERSION);
		ad.setVisible(true);*/
	}

}
