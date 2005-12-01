/*
 * Projet PAGOD
 * 
 * $Id: CloseProjectAction.java,v 1.1 2005/12/01 14:32:16 yak Exp $
 */
package pagod.wizard.control.actions;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
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
    public CloseProjectAction() throws LanguagesManager.NotInitializedException,
                       IOException
    {
//    	TODO CHANGER ICONE ET KEYSTROKE
    	super("closeProject","OpenIcon.gif", new Request(Request.RequestType.CLOSE_PROJECT),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
    }
    
   
}
