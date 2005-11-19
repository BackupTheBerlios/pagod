/*
 * $Id: ApplicationManager.java,v 1.6 2005/11/19 16:15:00 biniou Exp $
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

import javax.swing.JFileChooser;

import pagod.utils.ActionManager;
import pagod.utils.ExceptionManager;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.PreferencesManager.FileNotExecuteException;
import pagod.wizard.control.PreferencesManager.InvalidExtensionException;
import pagod.wizard.control.actions.*;
import pagod.common.control.InterfaceManager;
import pagod.common.control.adapters.ProcessTreeModel;
import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.ui.AboutDialog;
import pagod.common.ui.ProcessFileChooser;

// test bibi

import pagod.common.ui.WorkspaceFileChooser;
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
public class ApplicationManager
{
    /**
     * Instance du gestionnaire d'application
     */
    private static ApplicationManager amInstance = null;

    /**
     * Requete suceptible d'etre soumis ? l'application manager
     */
    public enum Request {
        /**
         * Lancer l'application
         */
        RUN_APPLICATION,
        /**
         * Quitter l'application
         */
        QUIT_APPLICATION,
        /**
         * Ouvrir un processus
         */
        OPEN_PROCESS,
        /**
         * Afficher le a propos
         */
        SHOW_ABOUT,
        /**
         * Afficher la fenetre de configuration des preferences
         */
        PREFERENCES,
        /**
         * Afficher la fenetre de configuration des outils
         */
        SET_TOOLS,
        /**
         * Lancer une activit?
         */
        RUN_ACTIVITY,
        /**
         * Demande la suite
         */
        NEXT,
        /**
         * Demande le retour
         */
        PREVIOUS,
        /**
         * Terminer l'activit?
         */
        TERMINATE_ACTIVITY,
        /**
         * aller à une étape direct en utilisant la comboBox
         */
        GOTOSTEP
    }

    /**
     * Etat possible de l'application
     */
    private enum State {
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
    private State state;

    /**
     * Fenetre principale de l'application
     */
    private MainFrame mfPagod;

    /**
     * D?rouleur d'activit?
     */
    private ActivityScheduler activityScheduler;

    /**
     * Processus en cours
     */
    private Process currentProcess = null;

    /**
     * Constructeur priv? du gestionnaire d'application (impl?mentation d'un
     * singleton)
     */
    private ApplicationManager()
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
            am.registerAction(Constants.ACTION_ABOUT, new AboutAction());
            am.registerAction(Constants.ACTION_RUN_ACTIVITY,
                    new RunActivityAction());
            am.registerAction(Constants.ACTION_NEXT, new NextAction());
            am.registerAction(Constants.ACTION_PREVIOUS, new PreviousAction());
            am.registerAction(Constants.ACTION_TERMINATE,new TerminateAction());
            am.registerAction(Constants.ACTION_GOTOSTEP,new DirectAccessAction());
            
            am.registerAction(Constants.ACTION_PREFERENCES,
                    new PreferencesAction());
            am.registerAction(Constants.ACTION_TOOLSSETTINGS,
                    new ToolsSettingsAction());
            // creation de la fenetre principale
            this.mfPagod = new MainFrame();
            // mettre a jour l'etat de l'application
            this.state = State.LOADED;
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
    public static ApplicationManager getInstance()
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
    public void manageRequest(Request request)
    {
        try
        {
            // cas de requete traiter quelque soit etat
            if (request == Request.SHOW_ABOUT)
            {
                this.showAboutDialog();
            }
            else if (request == Request.QUIT_APPLICATION)
            {
                this.quit();
            }
            else if (request == Request.PREFERENCES)
            {
                this.showPreferencesDialog();
            }
            else
            {
                // cas de requete d?pendante d'un ?tat
                switch (this.state)
                {
                    case LOADED:
                        switch (request)
                        {
                            case RUN_APPLICATION:

                                this.run();
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_RUN_ACTIVITY)
                                        .setEnabled(false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_NEXT)
                                        .setEnabled(false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_PREVIOUS).setEnabled(
                                        false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_TERMINATE).setEnabled(
                                        false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_GOTOSTEP).setEnabled(
                                        false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_TOOLSSETTINGS)
                                        .setEnabled(false);
                                break;
                        }
                        break;
                    case INIT:
                        switch (request)
                        {
                            case OPEN_PROCESS:
                                if (this.openProcess())
                                {
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TOOLSSETTINGS)
                                            .setEnabled(true);
                                }
                                break;
                        }
                        break;
                    case PROCESS_OPENED:
                        switch (request)
                        {
                            case OPEN_PROCESS:
                                if (this.openProcess())
                                {
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_RUN_ACTIVITY)
                                            .setEnabled(false);
                                }
                                if (this.state == State.INIT)
                                {
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_RUN_ACTIVITY)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_NEXT).setEnabled(
                                            false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_PREVIOUS)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_GOTOSTEP)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TERMINATE)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TOOLSSETTINGS)
                                            .setEnabled(false);
                                }
                                break;
                            case RUN_ACTIVITY:
                                this.runActivity();
                                
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_RUN_ACTIVITY)
                                        .setEnabled(false);
                                
                                /*ActionManager.getInstance().getAction(
                                        Constants.ACTION_TERMINATE).setEnabled(
                                        true); */
                                break;
                            case SET_TOOLS:
                                this.showToolsSettingsDialog();
                                break;
                        }
                        break;
                    case ACTIVITY_LAUNCHED:
                        switch (request)
                        {
                            case OPEN_PROCESS:
                                if (this.openProcess())
                                {
                                    this.activityScheduler = null;
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_RUN_ACTIVITY)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TERMINATE)
                                            .setEnabled(false);
                                }
                                if (this.state == State.INIT)
                                {
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_RUN_ACTIVITY)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_NEXT).setEnabled(
                                            false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_PREVIOUS)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_GOTOSTEP)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TERMINATE)
                                            .setEnabled(false);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_TOOLSSETTINGS)
                                            .setEnabled(false);
                                }
                                break;
                            case TERMINATE_ACTIVITY:
                                this.mfPagod.showProcess();
                                this.activityScheduler = null;
                                this.state = State.PROCESS_OPENED;
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_RUN_ACTIVITY)
                                        .setEnabled(true);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_NEXT)
                                        .setEnabled(false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_PREVIOUS).setEnabled(
                                        false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_GOTOSTEP)
                                        .setEnabled(false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_TERMINATE).setEnabled(
                                        false);
                                break;
                            case SET_TOOLS:
                                this.showToolsSettingsDialog();
                                break;
                                
                            case NEXT:
                            	ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP).setEnabled(false);
                            	System.out.println(request);
                                this.activityScheduler.getActivityState().next();
                                this.activityScheduler.getActivityState().display();
                                break;
                            case PREVIOUS:
                            	ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP).setEnabled(false);
                            	System.out.println(request);
                                this.activityScheduler.getActivityState().previous();
                                this.activityScheduler.getActivityState().display();
                                break;
                            case GOTOSTEP:
                            	ActionManager.getInstance().getAction(Constants.ACTION_NEXT).setEnabled(false);
                            	ActionManager.getInstance().getAction(Constants.ACTION_PREVIOUS).setEnabled(false);
                            	this.activityScheduler.getActivityState().setState(
                            			this.mfPagod.getButtonPanel().getCbDirectAccess().getSelectedIndex());
                               	this.activityScheduler.getActivityState().display();
                            	break;
                        }
                        break;

                }
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
     * @throws FileNotExecuteException 
     * @throws InvalidExtensionException 
     * @throws FileNotFoundException 
     */
    private void run() throws FileNotFoundException, InvalidExtensionException, FileNotExecuteException
    {
        // rends la fenetre visible et la maximise
        this.mfPagod.setVisible(true);
        this.mfPagod.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        // lancement de la fenetre de choix de workspace si besoin
        
        // test si la valeur de la clé workspace est définie ou pas
        if (!PreferencesManager.getInstance().containPreference("workspace"))
        {
        	WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();
        
        		if (workspaceChooser.showOpenDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION) 
        		{
        			File file = workspaceChooser.getSelectedFile();
        			System.out.println(file.getPath());
        			
        			// mettre le path dans le fichier preferences a la clé "workspace"
        			PreferencesManager.getInstance().setPreference("workspace",file.getPath());
            
        		}
        }
        
        // fin de choix de workspace
        
        this.state = State.INIT;
    }

    /**
     * Ferme l'application
     */
    private void quit()
    {
        // on sauvegarde l'association Tool/chemin d'acces
        if (this.currentProcess != null)
            this.closeProcess();
        // on sauvegarde les preferences que l'utilisateur a pu
        // modifier
        PreferencesManager.getInstance().storeExtensions();
        System.exit(0);
    }

    /**
     * G?re l'ouverture d'un processus
     * 
     * @return true si le processus est ouvert
     */
    private boolean openProcess()
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
                if (this.currentProcess != null)
                    this.closeProcess();
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
                    // mettre a jour l etat
                    this.state = State.PROCESS_OPENED;
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
                    // mettre a jour l etat
                    this.state = State.INIT;
                    open = false;
                }
            }
        }
        return open;
    }

    /**
     * Lance la fenetre de dialogue a propos
     */
    private void showAboutDialog()
    {
        AboutDialog ad = new AboutDialog(this.mfPagod,
                Constants.APPLICATION_SHORT_NAME + " "
                        + Constants.APPLICATION_VERSION);
        ad.setVisible(true);
    }

    /**
     * Lance une activit?
     */
    private void runActivity()
    {
        Activity activity = this.mfPagod.getActivity();
        this.activityScheduler = new ActivityScheduler(activity, this.mfPagod);
        this.state = State.ACTIVITY_LAUNCHED;
    }

    /**
     * lance la fenetre de configuration des preferences
     */
    private void showPreferencesDialog()
    {
        PreferencesDialog pd = new PreferencesDialog(this.mfPagod);
        pd.setVisible(true);
    }

    /**
     * lance la fenetre de configuration des preferences
     */
    private void showToolsSettingsDialog()
    {
        ToolsSettingsDialog tsd = new ToolsSettingsDialog(this.mfPagod,
                this.currentProcess.getRoles());
        tsd.setVisible(true);
    }

    /**
     * Ferme le processus en cours
     */
    private void closeProcess()
    {
        ToolsManager.getInstance().storeToolsAssociation();
    }
}
