/*
 * Projet PAGOD
 * 
 * $Id: SuspendAction.java,v 1.1 2006/02/04 16:30:28 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.common.model.Activity;
import pagod.utils.ActionManager;
import pagod.utils.TimerManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

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
	public SuspendAction ()
			throws NotInitializedException, IOException,
			pagod.utils.ImagesManager.NotInitializedException
	{
		 super("suspend", "TerminateIcon.gif",
	        		new Request(Request.RequestType.SUSPEND_ACTIVITY), KeyStroke
	                .getKeyStroke(KeyEvent.VK_ESCAPE, 0));
	}

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
    public void actionPerformed (ActionEvent actionEvent)
	{
    	if ( ApplicationManager.getInstance().manageRequest(this.request))
    	{
    		//on stop le timer
    		TimerManager.getInstance().stop();
    		Activity aTemp = ApplicationManager.getInstance().getMfPagod().getActivity();
    		//on enregistre le temps
    		aTemp.setTime(TimerManager.getInstance().getValue());
    		ActionManager.getInstance().getAction(Constants.ACTION_RUN_ACTIVITY).setEnabled(true);
    	}
	
	}
}
