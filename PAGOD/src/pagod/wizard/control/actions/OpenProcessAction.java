/*
 * $Id: OpenProcessAction.java,v 1.12 2006/02/16 17:07:32 cyberal82 Exp $
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

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
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

		// on recupere le nom du dpc(processus) qui sera a supprimer si
		// l'association du
		// nouveau processus ce passe bien
		String sNameDPC = ApplicationManager.getInstance().getCurrentProject()
				.getNameDPC();

		// sauvegarde des temps lies au processus
		ApplicationManager.getInstance().saveTime();

		// si le processus a pu etre ouvert et associe
		if (ApplicationManager.getInstance().getMfPagod()
				.associateDPCWithProject())
		{

			// s'il y avait un dpc affecter precedemment
			if (sNameDPC != null)
			{
				File processFile = new File(PreferencesManager.getInstance()
						.getWorkspace()
						+ File.separator
						+ ApplicationManager.getInstance().getCurrentProject()
								.getName() + File.separator + sNameDPC);

				// si on a pas reussit a supprimer l'ancien dpc on affiche un
				// message a l'utilisateur
				if (!processFile.delete())
				{

					JOptionPane.showMessageDialog(ApplicationManager
							.getInstance().getMfPagod(), LanguagesManager
							.getInstance().getString(
									"openProcessActionMsgErreurBody1")
							+ " \""
							+ processFile.getAbsolutePath()
							+ "\" \n"
							+ LanguagesManager.getInstance().getString(
									"openProcessActionMsgErreurBody2"),
							LanguagesManager.getInstance().getString(
									"openProcessActionMsgErreurTitle"),
							JOptionPane.ERROR_MESSAGE);
				}
			}

			// on delegue la requete a l'ApplicatioManager
			ApplicationManager.getInstance().manageRequest(this.request);
			
			// s'il y avait un dpc affecter precedemment on charge le fichier tps
			if (sNameDPC != null)
			{
				// initialiser le document time
				TimeHandler th = new TimeHandler();
				th.loadXML(ApplicationManager.getInstance().getCurrentProject()
						.getName());
				th.fillModel(ApplicationManager.getInstance()
						.getCurrentProcess());
			}
		}

	}
}
