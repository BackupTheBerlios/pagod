

package pagod.wizard.control.actions;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;

/**
 * Demande la suite
 * 
 * @author MoOky
 */
public class NextAction extends AbstractPagodAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public NextAction() throws LanguagesManager.NotInitializedException,
                       IOException, ImagesManager.NotInitializedException
    {
        super("next", "NextIcon.gif", ApplicationManager.Request.NEXT,
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK));
    }
}
