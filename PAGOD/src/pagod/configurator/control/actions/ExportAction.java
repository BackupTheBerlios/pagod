/*
 * $Id: ExportAction.java,v 1.1 2006/03/02 22:25:53 themorpheus Exp $
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
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.ui.RolesDialog;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * Action pour sauvegarder un processus
 * 
 * @author MoOky
 */
public class ExportAction extends CustomAction
{
	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public ExportAction () throws LanguagesManager.NotInitializedException,
			IOException, ImagesManager.NotInitializedException
	{
		super("export", "SaveAsHtmlStepIcon.gif");
	}

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{
		// on ouvre un nouvelle fenetre
		RolesDialog rd = new RolesDialog(ApplicationManager.getInstance()
				.getMfPagod(), ApplicationManager.getInstance()
				.getCurrentProcess());
		rd.setVisible(true);

	}
}
