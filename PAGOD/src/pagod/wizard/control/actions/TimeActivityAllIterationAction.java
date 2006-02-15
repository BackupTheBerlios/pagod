/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAllIterationAction.java,v 1.2 2006/02/15 15:50:49 biniou Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.TimeActivityAllIterationsDialog;

/**
 * @author biniou
 *
 */
public class TimeActivityAllIterationAction extends AbstractPagodAction
{
	/**
	 * @throws ImagesManager.NotInitializedException
	 * @throws IOException
	 * @throws LanguagesManager.NotInitializedException
	 */
	public TimeActivityAllIterationAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{
		
		//TODO changer l'icone
		
		super("timeActivityAllIteration", "AboutIcon.gif", null);
	}
	
	/**
	 * Methode appelée lorsque l'action est déclenchée
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		TimeActivityAllIterationsDialog tad = new TimeActivityAllIterationsDialog(ApplicationManager.getInstance().getMfPagod());
		tad.setVisible(true);
	}
}
