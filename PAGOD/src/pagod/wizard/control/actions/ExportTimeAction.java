/*
 * Projet PAGOD
 * 
 * $Id: ExportTimeAction.java,v 1.1 2006/03/12 02:11:23 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JComboBox;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.utils.ImagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.TimeHandler;
import pagod.wizard.control.states.Request;

/**
 * @author fabfoot
 * 
 */
public class ExportTimeAction extends AbstractPagodAction
{
	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws NotInitializedException
	 * @throws IOException
	 * @throws NotInitializedException
	 */
	public ExportTimeAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{

		// TODO changer l'icone

		super("exportTime", "AboutIcon.gif", null);
	}

	/* (non-Javadoc)
	 * @see pagod.wizard.control.actions.AbstractPagodAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		//exporter les temps passés
		//decendre le model sur le dom et ecrire le fichier
		
		
		if (TimerManager.getInstance().isStarted())
		{
			TimerManager.getInstance().stop();

		}
		// on ecrit le fichier
		if (ApplicationManager.getInstance().getCurrentProcess() != null)
		{
			TimeHandler th = new TimeHandler();
			th.loadModel(ApplicationManager.getInstance().getCurrentProcess());
			System.out.println("ExportTimeAction : ou faut il exporter");
			//doc a ecrire reste a savoir ou l'ecrire
			//th.writeXML(ApplicationManager.getInstance().getCurrentProject()
				//	.getName());
		}
	}
}
