/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAction.java,v 1.2 2006/01/15 09:20:18 biniou Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;
import pagod.wizard.ui.TimeActivityDialog;

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
		// => trouver un moyen pour créer une action sans requete
		// cf modifier encore la machine a etat?
		
		//TODO changer l'icone
		
		super("timeActivity", "AboutIcon.gif", new Request(
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
		TimeActivityDialog tad = new TimeActivityDialog(ApplicationManager.getInstance().getMfPagod());
		tad.setVisible(true);
	}

}
