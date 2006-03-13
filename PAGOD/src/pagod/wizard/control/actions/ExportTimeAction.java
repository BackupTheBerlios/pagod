/*
 * Projet PAGOD
 * 
 * $Id: ExportTimeAction.java,v 1.3 2006/03/13 09:15:28 fabfoot Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

import pagod.common.ui.CustomFileFilter;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.utils.ImagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.TimeHandler;

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

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// exporter les temps pass?s
		// decendre le model sur le dom et ecrire le fichier

		if (TimerManager.getInstance().isStarted())
		{
			TimerManager.getInstance().stop();

		}
		// on ecrit le fichier
		if (ApplicationManager.getInstance().getCurrentProcess() != null)
		{
			TimeHandler th = new TimeHandler();
			th.loadModel(ApplicationManager.getInstance().getCurrentProcess());
			// doc a ecrire reste a savoir ou l'ecrire
			
			JFileChooser chooser = new JFileChooser();// cr?ation dun nouveau
			chooser.setMultiSelectionEnabled(false); // filechosser
			Vector<String> authorizedExtensions = new Vector<String>();

			// xml
			authorizedExtensions.add("xml");
			CustomFileFilter xmlFileFilter = new CustomFileFilter(
					authorizedExtensions, ".xml");
			chooser.addChoosableFileFilter(xmlFileFilter);
			chooser.setApproveButtonText(LanguagesManager.getInstance().getString("Save" )); // intitul? du bouton
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				String path = chooser.getSelectedFile().getAbsolutePath();
				String test = path.substring( path.length()-4,path.length()  );
				//System.out.println(test);
				if (test.equals(".xml" ))
				{
					th.exportXML(path );
				}
				else{
					th.exportXML(path + ".xml");
				}
				
			}

		}
	}
}
