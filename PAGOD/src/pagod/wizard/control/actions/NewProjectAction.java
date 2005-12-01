/*
 * Projet PAGOD
 * 
 * $Id: NewProjectAction.java,v 1.1 2005/12/01 14:32:16 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

/**
 * @author yak
 * 
 */
public class NewProjectAction extends AbstractPagodAction
{

	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public NewProjectAction () throws LanguagesManager.NotInitializedException,
			IOException
	{
		// TODO CHANGER ICONE ET KEYSTROKE
		super("newProject", "OpenIcon.gif", new Request(
				Request.RequestType.OPEN_PROJECT), KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_MASK));
	}
	
	 
    /**
     * Methode appélée lorsque l'action est déclenché
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
    	//si le project a pu etre ouvert alors on delegue la requete à l'application manager
    	if (ApplicationManager.getInstance().getMfPagod().newProject())
    		ApplicationManager.getInstance().manageRequest(this.request);
    }

}
