/*
 * Projet PAGOD
 * 
 * $Id: NextIterationAction.java,v 1.2 2006/02/09 18:09:18 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;

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
		//System.out.println("On est sensé passer à l'itération suivante.");
		int itcour = ApplicationManager.getInstance().getCurrentProject().getItCurrent() ;
		itcour ++;
		TimeCouple tc = new TimeCouple (0,0);
		ApplicationManager.getInstance().getCurrentProject().setItCurrent(itcour);   
		Collection <Activity> cAc =	ApplicationManager.getInstance().getCurrentProcess().getAllActivities();     
		for (Activity a : cAc)
		{
			a.sethmTime(itcour, tc);
		}
	}
}
