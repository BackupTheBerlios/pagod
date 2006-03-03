/*
 * $Id: ApplicationManager.java,v 1.3 2006/03/03 12:02:39 themorpheus Exp $
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

package pagod.configurator.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Observable;

import pagod.common.model.Process;
import pagod.configurator.control.actions.AboutAction;
import pagod.configurator.control.actions.ExportAction;
import pagod.configurator.control.actions.OpenAction;
import pagod.configurator.control.actions.PreferencesAction;
import pagod.configurator.control.actions.QuitAction;
import pagod.configurator.control.actions.SaveAction;
import pagod.configurator.control.actions.SaveAsAction;
import pagod.configurator.control.states.AbstractApplicationState;
import pagod.configurator.control.states.Request;
import pagod.configurator.ui.MainFrame;
import pagod.utils.ActionManager;
import pagod.utils.ExceptionManager;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * Gestionnaire de l'application impl?ment? comme un singleton Cette classe g?re
 * la logique applicative
 * 
 * @author MoOky
 */
public class ApplicationManager extends Observable
{
	/**
	 * Instance du gestionnaire d'application
	 */
	private static ApplicationManager	amInstance	= null;

	/**
	 * Etat possible de l'application
	 */
	private enum State
	{
		/**
		 * Application est charger
		 */
		LOADED,
		/**
		 * Etat initiale de l'application
		 */
		INIT,
		/**
		 * Etat Processus ouvert : choix de l'activit? ? lancer
		 */
		PROCESS_OPENED,
	}

	/**
	 * Etat de l'application
	 */
	private AbstractApplicationState	applicationState;

	/**
	 * Fenetre principale de l'application
	 */
	private MainFrame					mfPagod;

	/**
	 * Processus en cours
	 */
	private Process						currentProcess	= null;

	/**
	 * Constructeur priv? du gestionnaire d'application (impl?mentation d'un
	 * singleton)
	 */
	private ApplicationManager ()
	{
		try
		{
			String applicationPath = FilesManager.getInstance().getRootPath();
			// si le repertoire des Langues n'existe pas
			File languagesDirectory = new File(applicationPath
					+ Constants.LANGUAGES_OUTPUT_DIRECTORY);
			if (!languagesDirectory.exists())
			{
				// on cr?e le repertoire
				languagesDirectory.mkdir();
			}
			// on extrait le fichier de langue par defaut
			File defaultLanguageFile = new File(applicationPath
					+ Constants.LANGUAGES_OUTPUT_FILE + ".properties");
			InputStream defaultInputStream = ClassLoader
					.getSystemResourceAsStream(Constants.LANGUAGES_FILE
							+ ".properties");
			OutputStream defaultOutputStream = new FileOutputStream(
					defaultLanguageFile);
			FilesManager.getInstance().copyFile(defaultInputStream,
					defaultOutputStream);
			// on extrait le fichier de langue fr
			File frLanguageFile = new File(applicationPath
					+ Constants.LANGUAGES_OUTPUT_FILE + "_fr.properties");
			InputStream frInputStream = ClassLoader
					.getSystemResourceAsStream(Constants.LANGUAGES_FILE
							+ "_fr.properties");
			OutputStream frOutputStream = new FileOutputStream(frLanguageFile);
			FilesManager.getInstance().copyFile(frInputStream, frOutputStream);
			// on extrait le fichier de langue en
			File enLanguageFile = new File(applicationPath
					+ Constants.LANGUAGES_OUTPUT_FILE + "_en.properties");
			InputStream enInputStream = ClassLoader
					.getSystemResourceAsStream(Constants.LANGUAGES_FILE
							+ "_en.properties");
			OutputStream enOutputStream = new FileOutputStream(enLanguageFile);
			FilesManager.getInstance().copyFile(enInputStream, enOutputStream);
			// Definition de la locale
			URL urls[] = { languagesDirectory.toURL() };
			LanguagesManager.getInstance().setResourceFile(
					Constants.LANGUAGES_FILE_PREFIXE,
					new Locale(PreferencesManager.getInstance().getLanguage()),
					new URLClassLoader(urls));
			// Definition du Chemin contenant les Icones de l'applications
			ImagesManager.getInstance()
					.setImagePath(Constants.IMAGES_DIRECTORY);
			// Creation et enregistrement des actions de l'application
			ActionManager am = ActionManager.getInstance();
			am.registerAction(Constants.ACTION_QUIT, new QuitAction());
			am.registerAction(Constants.ACTION_OPEN, new OpenAction());
			am.registerAction(Constants.ACTION_SAVE, new SaveAction());
			am.registerAction(Constants.ACTION_SAVE_AS, new SaveAsAction());
			am.registerAction(Constants.ACTION_EXPORT, new ExportAction());
			am.registerAction(Constants.ACTION_ABOUT, new AboutAction());
			am.registerAction(Constants.ACTION_PREFERENCES,
				new PreferencesAction());

		}
		catch (Exception ex)
		{
			ExceptionManager.getInstance().manage(ex);
			System.exit(0);
		}
	}

	/**
	 * retourne l'instance du gestionnaire d'application
	 * 
	 * @return instance du gestionnaire d'application
	 */
	public static ApplicationManager getInstance ()
	{
		if (ApplicationManager.amInstance == null)
		{
			ApplicationManager.amInstance = new ApplicationManager();
		}
		return (ApplicationManager.amInstance);
	}

	/**
	 * G?re les requetes
	 * 
	 * @param request
	 *            requete ? traiter
	 * @return vrai si mle chnagment d'etat a etait fait, faut sinon
	 */
	public boolean manageRequest (Request request)
	{
		try
		{

			return this.applicationState.manageRequest(request);
			

		}
		catch (Exception ex)
		{
			ExceptionManager.getInstance().manage(ex);
			System.exit(0);
		}
		return false;

	}

	/**
	 * @return Retourne l'attribut state
	 */
	public AbstractApplicationState getState ()
	{
		return this.applicationState;
	}

	/**
	 * @param state
	 *            Valeur ? donner ? state
	 */
	public void setState (AbstractApplicationState state)
	{
		this.applicationState = state;
		// on indique aux observers que l'etat a change
		this.setChanged();
		this.notifyObservers(this.applicationState);

		// TODO pour debug
		System.err.println(this.applicationState);
	}

	/**
	 * 
	 * @param mainFrame
	 *            est la fenetre principale de l'application
	 */
	public void setMfPagod (MainFrame mainFrame)
	{
		this.mfPagod = mainFrame;

		// on met la main frame sur ecoute de l'application manager et de ces
		// etats
		this.addObserver(this.mfPagod);

	}

	/**
	 * @return Retourne l'attribut mfPagod
	 */
	public MainFrame getMfPagod ()
	{
		return this.mfPagod;
	}

	/**
	 * @return Retourne l'attribut currentProcess
	 */
	public Process getCurrentProcess ()
	{
		return this.currentProcess;
	}

	/**
	 * @param currentProcess
	 *            Valeur ? donner ? currentProcess
	 */
	public void setCurrentProcess (Process currentProcess)
	{
		this.currentProcess = currentProcess;
	}
}
