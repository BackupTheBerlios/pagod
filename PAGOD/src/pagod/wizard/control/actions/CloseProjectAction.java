/*
 * Projet PAGOD
 * 
 * $Id: CloseProjectAction.java,v 1.7 2006/02/19 14:20:04 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
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
			
		}
		
		// sauvegarde des temps lies au processus
		ApplicationManager.getInstance().saveTime();
		
		ApplicationManager.getInstance().manageRequest(this.request);
		
	}

}
