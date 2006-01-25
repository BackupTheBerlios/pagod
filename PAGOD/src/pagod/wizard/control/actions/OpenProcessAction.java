/*
 * $Id: OpenProcessAction.java,v 1.5 2006/01/25 09:21:23 fabfoot Exp $
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
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.KeyStroke;

import pagod.common.model.Activity;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.TimeHandler;
import pagod.wizard.control.states.Request;

/**
 * Action pour ouvrir un processus
 * 
 * @author MoOky
 */
public class OpenProcessAction extends AbstractPagodAction
{
	/**
	 * @throws LanguagesManager.NotInitializedException
	 * @throws IOException
	 * @throws ImagesManager.NotInitializedException
	 */
	public OpenProcessAction ()
			throws LanguagesManager.NotInitializedException, IOException,
			ImagesManager.NotInitializedException
	{
		super("openProcess", "OpenIcon.gif", new Request(
				Request.RequestType.OPEN_PROCESS), KeyStroke.getKeyStroke(
				KeyEvent.VK_O, KeyEvent.CTRL_MASK));
	}

	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
	public void actionPerformed (ActionEvent actionEvent)
	{

		File processFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator
				+ ApplicationManager.getInstance().getCurrentProject()
						.getName()
				+ File.separator
				+ ApplicationManager.getInstance().getCurrentProject()
						.getNameDPC());
		if (processFile.delete())
		{
			// si le processus a pu etre ouvert alors on delegue la requete ?
			// l'application manager
			// on essaye d'effacer l'ancien dpc
			// si le fichier a ete effacer ;) on l'associe au projet
			if (ApplicationManager.getInstance().getMfPagod()
					.associateDPCWithProject())
			{
				// on stop le timer
				if (TimerManager.getInstance().isStarted())
				{
					TimerManager.getInstance().stop();
					Activity aTemp = ApplicationManager.getInstance()
							.getMfPagod().getActivity();
					// on enregistre le temps pour l'activit?
					aTemp.setTime(TimerManager.getInstance().getValue());
				}
				ApplicationManager.getInstance().manageRequest(this.request);
				 
				//initialiser le document time 
				TimeHandler th = new TimeHandler ();
				th.loadXML( ApplicationManager.getInstance().getCurrentProject().getName());
				System.out.println("apres le load xml pasing");
				th.affiche(); 
				th.fillModel(ApplicationManager.getInstance().getCurrentProcess() );
				System.out.println("apres la mont du model");
				th.affiche();
			}
			
		}

	}
}
