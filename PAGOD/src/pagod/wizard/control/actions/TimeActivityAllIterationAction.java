/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAllIterationAction.java,v 1.1 2006/02/06 16:19:12 biniou Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

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
		System.out.println("On est sensé afficher les temps depuis le début.");
	}
}
