/*
 * $Id: QuitAction.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of PAGOD.
 *
 * PAGOD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * PAGOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PAGOD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.wizard.control.actions;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;

/**
 * Action pour quitter l'application
 * 
 * @author MoOky
 */
public class QuitAction extends AbstractPagodAction
{
    /**
     * @throws ImagesManager.NotInitializedException
     * @throws IOException
     * @throws LanguagesManager.NotInitializedException
     */
    public QuitAction() throws ImagesManager.NotInitializedException,
                       IOException, LanguagesManager.NotInitializedException
    {
        super("quit", "QuitIcon.gif",
                ApplicationManager.Request.QUIT_APPLICATION, KeyStroke
                .getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
    }
}
