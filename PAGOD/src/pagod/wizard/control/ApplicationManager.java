/*
 * $Id: ApplicationManager.java,v 1.13 2005/11/30 12:21:52 cyberal82 Exp $
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

package pagod.wizard.control;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Observable;

import javax.swing.JFileChooser;

import pagod.common.control.InterfaceManager;
import pagod.common.control.adapters.ProcessTreeModel;
import pagod.common.model.Process;
import pagod.common.model.Project;
import pagod.common.ui.AboutDialog;
import pagod.common.ui.NewProjectDialog;
import pagod.common.ui.ProcessFileChooser;
import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.ActionManager;
import pagod.utils.ExceptionManager;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.PreferencesManager.FileNotExecuteException;
import pagod.wizard.control.PreferencesManager.InvalidExtensionException;
import pagod.wizard.control.actions.AboutAction;
import pagod.wizard.control.actions.CloseProcessAction;
import pagod.wizard.control.actions.GotoAction;
import pagod.wizard.control.actions.NextAction;
import pagod.wizard.control.actions.OpenProcessAction;
import pagod.wizard.control.actions.OpenProjectAction;
import pagod.wizard.control.actions.PreferencesAction;
import pagod.wizard.control.actions.PreviousAction;
import pagod.wizard.control.actions.QuitAction;
import pagod.wizard.control.actions.RunActivityAction;
import pagod.wizard.control.actions.TerminateAction;
import pagod.wizard.control.actions.ToolsSettingsAction;
import pagod.wizard.control.states.Request;
import pagod.wizard.control.states.application.AbstractApplicationState;
import pagod.wizard.control.states.application.InitState;
import pagod.wizard.ui.MainFrame;
import pagod.wizard.ui.PreferencesDialog;
import pagod.wizard.ui.RolesChooserDialog;
import pagod.wizard.ui.ToolsSettingsDialog;

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
		/**
		 * Activit? en cours de d?roulement
		 */
		ACTIVITY_LAUNCHED
	}

	/**
	 * Etat de l'application
	 */
	//private State						state2;

	/**
	 * Etat de l'application
	 */
	private AbstractApplicationState	applicationState;
	
	/**
	 * Fenetre principale de l'application
	 */
	private MainFrame					mfPagod;

	/**
	 * D?rouleur d'activit?
	 */
	private ActivityScheduler			activityScheduler;

	/**
	 * Processus en cours
	 */
	private Process						currentProcess	= null;

	/**
	 * Projet en cours
	 */
	private Project						currentProject	= null;

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
			am.registerAction(Constants.ACTION_OPENPROCESS, new OpenProcessAction());
			am.registerAction(Constants.ACTION_CLOSEPROCESS, new CloseProcessAction());
			am.registerAction(Constants.ACTION_OPENPROJECT, new OpenProjectAction());
			am.registerAction(Constants.ACTION_ABOUT, new AboutAction());
			am.registerAction(Constants.ACTION_RUN_ACTIVITY,
					new RunActivityAction());
			am.registerAction(Constants.ACTION_NEXT, new NextAction());
			am.registerAction(Constants.ACTION_PREVIOUS, new PreviousAction());
			am
					.registerAction(Constants.ACTION_TERMINATE,
							new TerminateAction());
			am.registerAction(Constants.ACTION_GOTOSTEP,
					new GotoAction());

			am.registerAction(Constants.ACTION_PREFERENCES,
					new PreferencesAction());
			am.registerAction(Constants.ACTION_TOOLSSETTINGS,
					new ToolsSettingsAction());
			// creation de la fenetre principale
			this.mfPagod = new MainFrame();
			// on met la main frame sur ecoute de l'application manager et de ces etats
			this.addObserver(this.mfPagod);
			//on passe dans l'?tat init
			this.setState(new InitState(this));
			
		}
		catch (Exception ex)
		{
			ExceptionManager.getInstance().manage(ex);
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
	 */
	public void manageRequest (Request request)
	{
		try
		{
			// cas de requete traiter quelque soit etat
			if (request.getCurrentRequest() == Request.RequestType.SHOW_ABOUT)
			{
				this.showAboutDialog();
			}
			else if (request.getCurrentRequest() == Request.RequestType.QUIT_APPLICATION)
			{
				this.quit();
			}
			else if (request.getCurrentRequest() == Request.RequestType.PREFERENCES)
			{
				this.showPreferencesDialog();
			}
			else
			{
				this.applicationState.manageRequest(request);
				
				
			}
		}
		catch (Exception ex)
		{
			ExceptionManager.getInstance().manage(ex);
			System.exit(0);
		}

	}

	/**
	 * Lance l'application
	 * 
	 * @throws FileNotExecuteException
	 * @throws InvalidExtensionException
	 * @throws FileNotFoundException
	 */
	private void run () throws FileNotFoundException,
			InvalidExtensionException, FileNotExecuteException
	{
		// rends la fenetre visible et la maximise
		this.mfPagod.setVisible(true);
		this.mfPagod.setExtendedState(Frame.MAXIMIZED_BOTH);

		// lancement de la fenetre de choix de workspace si besoin

//		 test si la valeur de la cl? workspace est d?finie ou pas
		if (!PreferencesManager.getInstance().containWorkspace())
		{
			WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();

			if (workspaceChooser.showOpenDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION)
			{
				File file = workspaceChooser.getSelectedFile();
				System.out.println(file.getPath());

				// mettre le path dans le fichier preferences a la cl?
				// "workspace"
				PreferencesManager.getInstance().setWorkspace(file.getPath());
			}
		}

		// fin de choix de workspace

		// on enregistre la MainFrame comme observer de l'ApplicationManager
		this.addObserver(this.mfPagod);

	}

	/**
	 * Ferme l'application
	 */
	private void quit ()
	{
		// on sauvegarde l'association Tool/chemin d'acces
		if (this.currentProcess != null) this.closeProcess();
		// on sauvegarde les preferences que l'utilisateur a pu
		// modifier
		PreferencesManager.getInstance().storeExtensions();
		System.exit(0);
	}

	/**
	 * Gere la creation d'un projet
	 * 
	 */
	private void createNewProject ()
	{
		// on verifie que le workspace est bien cr?? sinon on force
		// l'utilisateur a le choisir

		// test si la valeur de la cl? workspace est d?finie ou pas
		if (!PreferencesManager.getInstance().containWorkspace())
		{
			WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();

			if (workspaceChooser.showOpenDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION)
			{
				File file = workspaceChooser.getSelectedFile();
				System.out.println(file.getPath());

				// mettre le path dans le fichier preferences a la cl?
				// "workspace"
				PreferencesManager.getInstance().setWorkspace(file.getPath());

				// on affiche la fenetre qui permet de saisir le nom du projet
				NewProjectDialog testDialog = new NewProjectDialog(this.mfPagod);
				testDialog.setVisible(true);
			}
			// si l'utilisateur ne choisit pas, on ne cree rien
			else
			{
				System.err.println("Le workspace n'est pas d?fini.");
			}
		}
		else
		{
			// on affiche la fenetre qui permet de saisir le nom du projet
			NewProjectDialog testDialog = new NewProjectDialog(this.mfPagod);
			testDialog.setVisible(true);
		}
	}

	/**
	 * G?re l'ouverture d'un processus
	 * 
	 * @return true si le processus est ouvert
	 */
	private boolean openProcess ()
	{
		boolean open = false;
		

		ProcessFileChooser fileChooser = new ProcessFileChooser();
		if (fileChooser.showOpenDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION)
		{

			// Remplir le mod?le metier
			File choosenfile = fileChooser.getSelectedFile();
			Process aProcess = InterfaceManager.getInstance().importModel(
					choosenfile.getAbsolutePath(), this.mfPagod, false);
			if (aProcess != null)
			{
				if (this.currentProcess != null) this.closeProcess();
				// Afficher la fenetre de choix des roles
				RolesChooserDialog rolesChooser = new RolesChooserDialog(
						this.mfPagod, aProcess.getRoles());
				if (rolesChooser.showDialog() == RolesChooserDialog.APPROVE_OPTION)
				{
					// recuperer les Roles choisis
					// creer le TreeModel n?cessaire au JTree de la fenetre
					// presenter a l'utilisateur le processus
					String fileName = choosenfile.getName();
					this.mfPagod.showProcess(new ProcessTreeModel(aProcess,
							rolesChooser.getChosenRoles()), fileName, aProcess
							.getName());
					// mettre a jour le processus en cours
					this.currentProcess = aProcess;
					// on ouvre les fichiers d'outils
					ToolsManager.getInstance().initialise(this.currentProcess);
					ToolsManager.getInstance().loadToolsAssociation();
					open = true;
				}
				else
				{
					// recuperer les Roles choisis
					// creer le TreeModel n?cessaire au JTree de la fenetre
					// presenter a l'utilisateur le processus
					this.mfPagod.reinitialize();
					// mettre a jour le processus en cours
					this.currentProcess = null;
					open = false;
				}
			}
		}
		return open;
	}
	
	
	/**
	 * Lance la fenetre de dialogue a propos
	 */
	private void showAboutDialog ()
	{
		AboutDialog ad = new AboutDialog(this.mfPagod,
				Constants.APPLICATION_SHORT_NAME + " "
						+ Constants.APPLICATION_VERSION);
		ad.setVisible(true);
	}

	/**
	 * Lance une activit?
	 * TODO a suppr d?plac?e dans activity launched
	 */
	/*private void runActivity ()
	{
		Activity activity = this.mfPagod.getActivity();
		this.activityScheduler = new ActivityScheduler(activity);
		// TODO A SUPPR
		// this.activityScheduler.initActivityScheduler();
		this.state2 = State.ACTIVITY_LAUNCHED;

		// on notifie la MainFrame qu'on a lanc? une activit?
		this.setChanged();

		// on passe a la MainFrame l'ActivityScheduler pour qu'elle
		// puisse s'enregistrer comme observer de l'ActivityScheduler
		this.notifyObservers(this.activityScheduler);

		// TODO solution temporaire
		this.activityScheduler.setState(this.activityScheduler.getState(0));

	}*/

	/**
	 * lance la fenetre de configuration des preferences
	 */
	private void showPreferencesDialog ()
	{
		PreferencesDialog pd = new PreferencesDialog(this.mfPagod);
		pd.setVisible(true);
	}

	/**
	 * lance la fenetre de configuration des preferences
	 */
	private void showToolsSettingsDialog ()
	{
		ToolsSettingsDialog tsd = new ToolsSettingsDialog(this.mfPagod,
				this.currentProcess.getRoles());
		tsd.setVisible(true);
	}

	/**
	 * Ferme le processus en cours
	 */
	public void closeProcess ()
	{
		ToolsManager.getInstance().storeToolsAssociation();
	}

	/**
	 * @return Retourne l'attribut currentProject
	 */
	public Project getCurrentProject ()
	{
		return this.currentProject;
	}

	/**
	 * @param currentProject
	 *            Valeur ? donner ? currentProject
	 */
	public void setCurrentProject (Project currentProject)
	{
		this.currentProject = currentProject;
	}

	/**
	 * @return Retourne l'attribut state
	 */
	public AbstractApplicationState getState ()
	{
		return this.applicationState;
	}

	/**
	 * @param state Valeur ? donner ? state
	 */
	public void setState (AbstractApplicationState state)
	{
		this.applicationState = state;
		//on indique aux observers que l'etat a change
		this.setChanged();
		this.notifyObservers(this.applicationState);
		
		// TODO pour debug
		System.err.println(this.applicationState);
	}

	/**
	 * @return Retourne l'attribut currentProcess
	 */
	public Process getCurrentProcess ()
	{
		return this.currentProcess;
	}

	/**
	 * @param currentProcess Valeur ? donner ? currentProcess
	 */
	public void setCurrentProcess (Process currentProcess)
	{
		this.currentProcess = currentProcess;
	}

	/**
	 * @return Retourne l'attribut mfPagod
	 */
	public MainFrame getMfPagod ()
	{
		return this.mfPagod;
	}
	//TODO A changer methode temporaire
	public void notifyMainFrame(ActivityScheduler activityScheduler)
	{
		//notifie les observer (par ex la mainframe)
		this.setChanged();
		this.notifyObservers(activityScheduler);
	}
}
