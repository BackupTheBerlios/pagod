/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAction.java,v 1.3 2006/01/20 15:38:00 biniou Exp $
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
		// TODO enlever les commentaires quand on aura code
		// la fenetre
		TimeActivityDialog tad = new TimeActivityDialog(ApplicationManager.getInstance().getMfPagod());
		tad.setVisible(true);
	}

}
