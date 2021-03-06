/*
 * Projet PAGOD
 * 
 * $Id: NextIterationAction.java,v 1.11 2006/03/02 21:43:19 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JOptionPane;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;

/**
 * @author biniou
 * 
 */
public class NextIterationAction extends AbstractPagodAction
{
	/**
	 * @throws ImagesManager.NotInitializedException
	 * @throws IOException
	 * @throws LanguagesManager.NotInitializedException
	 */
	public NextIterationAction ()
			throws LanguagesManager.NotInitializedException, IOException,
			ImagesManager.NotInitializedException
	{

		// TODO changer l'icone

		super("nextIteration", "AboutIcon.gif", null);
	}

	/**
	 * Methode appel?e lorsque l'action est d?clench?e
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		int ret = JOptionPane.showConfirmDialog(ApplicationManager
				.getInstance().getMfPagod(), LanguagesManager.getInstance().getString("ChangeIteration" ) ,LanguagesManager.getInstance().getString("ChangeIterationTitle" ) ,JOptionPane.YES_NO_OPTION);
		if (ret == JOptionPane.YES_OPTION)
		{
			int itcour = ApplicationManager.getInstance().getCurrentProject()
					.getItCurrent();
			// mise a zero de tous les temps remaining de chaque activite de
			// l'etape
			// termine

			TimeCouple tc1 = null;
			itcour++;
			TimeCouple tc = new TimeCouple(0, 0);
			ApplicationManager.getInstance().getCurrentProject().setItCurrent(
					itcour);
			Collection<Activity> cAc = ApplicationManager.getInstance()
					.getCurrentProcess().getAllActivities();
			for (Activity a : cAc)
			{
				a.sethmTime(itcour, tc);
				// pour chaque activit mettre a 0 le temps remainig
				tc1 = a.gethmTime(itcour - 1);
				tc1.setTimeRemaining(0);
				a.setDone(false);

			}

			// on affiche un message comme quoi l'iteration a chang?

			JOptionPane.showMessageDialog(ApplicationManager.getInstance()
					.getMfPagod(), LanguagesManager.getInstance().getString(
					"NewIteration")+ itcour, LanguagesManager.getInstance().getString(
					"NewIterationTitle")  , JOptionPane.INFORMATION_MESSAGE);
		}
		else if (ret == JOptionPane.NO_OPTION)
		{
			
		}

	}
}
