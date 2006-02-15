/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAction.java,v 1.5 2006/02/15 15:50:49 biniou Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.TimeActivityDialog;

/**
 * @author ppmaxynoob et biniou
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
		// => trouver un moyen pour cr?er une action sans requete
		// cf modifier encore la machine a etat?
		
		//TODO changer l'icone
		
		super("timeActivity", "AboutIcon.gif", null);
	}
	
	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		TimeActivityDialog tad = new TimeActivityDialog(ApplicationManager.getInstance().getMfPagod());
		tad.setVisible(true);
	}

}
