/*
 * Projet PAGOD
 * 
 * $Id: NextIterationAction.java,v 1.1 2006/02/06 16:19:12 biniou Exp $
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
public class NextIterationAction extends AbstractPagodAction
{
	/**
	 * @throws ImagesManager.NotInitializedException
	 * @throws IOException
	 * @throws LanguagesManager.NotInitializedException
	 */
	public NextIterationAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{
		
		//TODO changer l'icone
		
		super("nextIteration", "AboutIcon.gif", null);
	}
	
	/**
	 * Methode appelée lorsque l'action est déclenchée
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		System.out.println("On est sensé passer à l'itération suivante.");
	}
}
