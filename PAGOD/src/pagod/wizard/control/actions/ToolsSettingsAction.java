/*
 * $Id: ToolsSettingsAction.java,v 1.3 2006/01/20 13:49:21 yak Exp $
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

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.common.control.actions.CustomAction;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.ToolsSettingsDialog;

/**
 * Action de lancement de la configuration des outils
 * 
 * @author MoOky
 */ 
public class ToolsSettingsAction extends CustomAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public ToolsSettingsAction()
                                throws LanguagesManager.NotInitializedException,
                                IOException,
                                ImagesManager.NotInitializedException
    {
        super("toolsSettings", "ToolsSettingsIcon.gif");
    }

	public void actionPerformed (ActionEvent e)
	{
		ToolsSettingsDialog tsd = new ToolsSettingsDialog(ApplicationManager.getInstance().getMfPagod(),
				ApplicationManager.getInstance().getCurrentProcess().getRoles());
		tsd.setVisible(true);
	}
}
