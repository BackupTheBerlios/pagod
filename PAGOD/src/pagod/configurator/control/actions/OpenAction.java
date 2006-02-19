/*
 * $Id: OpenAction.java,v 1.2 2006/02/19 15:36:04 yak Exp $
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

package pagod.configurator.control.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.states.InitState;
import pagod.configurator.control.states.Request;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;


/**
 * Action pour offrir un processus
 * 
 * @author MoOky
 */
public class OpenAction extends AbstractPagodAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public OpenAction() throws LanguagesManager.NotInitializedException,
                       IOException, ImagesManager.NotInitializedException
    {
        super("open", "OpenIcon.gif", new Request(Request.RequestType.OPEN_PROCESS),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
    }
    /**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		//TODO verfier si le message est a afficher ou pas (param booleen de open process
		if (ApplicationManager.getInstance().getMfPagod().openProcess(!(ApplicationManager.getInstance().getState() instanceof InitState)))
		{
			ApplicationManager.getInstance().manageRequest(this.request);
		}
	}
}
