/*
 * Projet PAGOD
 * 
 * $Id: CloseProjectAction.java,v 1.5 2006/02/09 19:06:21 cyberal82 Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.common.model.Activity;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.TimeHandler;
import pagod.wizard.control.states.Request;

/**
 * @author m1isi15
 * 
 */
public class CloseProjectAction extends AbstractPagodAction
{
	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public CloseProjectAction ()
			throws LanguagesManager.NotInitializedException, IOException
	{
		// TODO CHANGER ICONE ET KEYSTROKE
		super("closeProject", "OpenIcon.gif", new Request(
				Request.RequestType.CLOSE_PROJECT), KeyStroke.getKeyStroke(
				KeyEvent.VK_C, KeyEvent.CTRL_MASK));
	}

	/** (non-Javadoc)
	 * @see pagod.wizard.control.actions.AbstractPagodAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		if (TimerManager.getInstance().isStarted())
		{
			TimerManager.getInstance().stop();
			Activity aTemp = ApplicationManager.getInstance().getMfPagod()
					.getActivity();
			// on enregistre le temps pour l'activit?
			aTemp.setTime(TimerManager.getInstance().getValueElapsed());
		
		}
		
		// sauvegarde des temps lies au processus
		ApplicationManager.getInstance().saveTime();
		
		ApplicationManager.getInstance().manageRequest(this.request);
		
	}

}
