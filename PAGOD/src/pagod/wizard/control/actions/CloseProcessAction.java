/*
 * Projet PAGOD
 * 
 * $Id: CloseProcessAction.java,v 1.1 2005/11/30 12:21:52 cyberal82 Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * @author m1isi15
 *
 */
public class CloseProcessAction extends AbstractPagodAction
{
	  /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public CloseProcessAction() throws LanguagesManager.NotInitializedException,
                       IOException
    {
//    	TODO CHANGER ICONE ET KEYSTROKE
    	super("closeProcess","OpenIcon.gif", new Request(Request.RequestType.CLOSE_PROCESS),
                KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
    }
    
    /**
     * Methode app?l?e lorsque l'action est d?clench?
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
    	
    	
		
    }
}
