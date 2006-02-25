/*
 * $Id: AboutAction.java,v 1.3 2006/02/25 13:23:45 cyberal82 Exp $
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
import java.io.IOException;

import pagod.common.control.actions.CustomAction;
import pagod.common.ui.AboutDialog;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.Constants;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;


/**
 * Action pour offrir un processus
 * 
 * @author MoOky
 */
public class AboutAction extends CustomAction
{
    /**
     * @throws ImagesManager.NotInitializedException
     * @throws IOException
     * @throws LanguagesManager.NotInitializedException
     */
    public AboutAction() throws LanguagesManager.NotInitializedException,
                        IOException, ImagesManager.NotInitializedException
    {
        super("about", "AboutIcon.gif");
    }
    

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		AboutDialog ad = new AboutDialog(ApplicationManager.getInstance().getMfPagod(),
				Constants.APPLICATION_SHORT_NAME + " "
						+ Constants.APPLICATION_VERSION);
		ad.setVisible(true);
	}
}
