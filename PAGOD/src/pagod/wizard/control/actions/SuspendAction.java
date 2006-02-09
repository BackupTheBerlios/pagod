/*
 * Projet PAGOD
 * 
 * $Id: SuspendAction.java,v 1.6 2006/02/09 17:50:36 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.ActionManager;
import pagod.utils.TimerManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;
import pagod.wizard.ui.TimeEditDialog;

/**
 * @author yak
 * 
 */
public class SuspendAction extends AbstractPagodAction
{

	/**
	 * @throws NotInitializedException
	 * @throws IOException
	 * @throws NotInitializedException
	 * @throws pagod.utils.ImagesManager.NotInitializedException
	 */
	public SuspendAction () throws NotInitializedException, IOException,
			pagod.utils.ImagesManager.NotInitializedException
	{
		super("suspend", "TerminateIcon.gif", new Request(
				Request.RequestType.SUSPEND_ACTIVITY), KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0));
	}

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		TimerManager.getInstance().stop();
		if (ApplicationManager.getInstance().manageRequest(this.request))
		{
			// on stop le timer

			Activity aTemp = ApplicationManager.getInstance().getMfPagod()
					.getActivity();
			// Modif Flotueur : on appelle la bo?te de dialogue TimeEditDialog
			// qui va servir ?
			// connaitre et ? modifier le temps pass? sur une activit?
		
			
			
			System.out.println(" TimerManager.getInstance().isStarted() -> "+TimerManager.getInstance().isStarted());
			// Fin modif Flotueur
			// on enregistre le temps
			int iCurrentIt = ApplicationManager.getInstance()
					.getCurrentProject().getItCurrent();

			aTemp.sethmTime(iCurrentIt, new TimeCouple(TimerManager
					.getInstance().getValueElapsed(), TimerManager
					.getInstance().getValueRemaining()));
			TimeEditDialog ted2 = new TimeEditDialog(ApplicationManager
					.getInstance().getMfPagod(), aTemp);
			ActionManager.getInstance()
					.getAction(Constants.ACTION_RUN_ACTIVITY).setEnabled(true);
		}
		else
		{
			// si ca n'a pas marcher on redemarre le timer
			TimerManager.getInstance().start();

		}

	}
}
