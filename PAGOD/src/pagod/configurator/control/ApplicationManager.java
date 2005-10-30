/*
 * $Id: ApplicationManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pagod.utils.ActionManager;
import pagod.utils.ExceptionManager;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.configurator.control.Constants;
import pagod.configurator.control.PreferencesManager;
import pagod.common.control.InterfaceManager;
import pagod.common.model.Process;
import pagod.common.ui.AboutDialog;
import pagod.common.ui.ProcessFileChooser;
import pagod.configurator.control.actions.*;
import pagod.configurator.ui.MainFrame;
import pagod.configurator.ui.PreferencesDialog;
import pagod.configurator.ui.ProcessOutputFileChooser;

/**
 * Gestionnaire de l'application implémenté comme un singleton Cette classe gère
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
     * Requette suceptible d'etre soumis à l'application manager
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
         * enregistrer un processus
         */
        SAVE_PROCESS,
        /**
         * enregistrer sous un processus
         */
        SAVE_AS_PROCESS,
        /**
         * Afficher le a propos
         */
        SHOW_ABOUT,
        /**
         * Afficher la fenetre de configuration des preferences
         */
        PREFERENCES

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
         * Etat Processus ouvert : choix de l'activité à lancer
         */
        PROCESS_OPENED,
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
     * Processus en cours
     */
    private Process currentProcess = null;

    /**
     * Constructeur privé du gestionnaire d'application (implémentation d'un
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
                // on crée le repertoire
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
            am.registerAction(Constants.ACTION_ABOUT, new AboutAction());
            am.registerAction(Constants.ACTION_PREFERENCES,
                    new PreferencesAction());
            // creation de la fenetre principale
            this.mfPagod = new MainFrame();
            // mettre a jour l'etat de l'application
            this.state = State.LOADED;
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
    public static ApplicationManager getInstance()
    {
        if (ApplicationManager.amInstance == null)
        {
            ApplicationManager.amInstance = new ApplicationManager();
        }
        return (ApplicationManager.amInstance);
    }

    /**
     * Retourne la fenêtre principale
     * 
     * @return Fenêtre principale
     */
    public Frame getMainFrame()
    {
        return this.mfPagod;
    }

    /**
     * Gère les requetes
     * 
     * @param request
     *            requete à traiter
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
            else if (request == Request.PREFERENCES)
            {
                this.showPreferencesDialog();
            }
            else
            {
                // cas de requete dépendante d'un état
                switch (this.state)
                {
                    case LOADED:
                        switch (request)
                        {
                            case RUN_APPLICATION:
                                this.run();
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_SAVE)
                                        .setEnabled(false);
                                ActionManager.getInstance().getAction(
                                        Constants.ACTION_SAVE_AS).setEnabled(
                                        false);
                                break;
                        }
                        break;
                    case INIT:
                        switch (request)
                        {
                            case OPEN_PROCESS:
                                if (this.openProcess(false))
                                {
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_SAVE).setEnabled(
                                            true);
                                    ActionManager.getInstance().getAction(
                                            Constants.ACTION_SAVE_AS)
                                            .setEnabled(true);
                                }
                                break;
                            case QUIT_APPLICATION:
                                this.quit(false);
                                break;
                        }
                        break;
                    case PROCESS_OPENED:
                        switch (request)
                        {
                            case OPEN_PROCESS:
                                this.openProcess(true);
                                break;
                            case SAVE_PROCESS:
                                this.saveProcess();
                                break;
                            case SAVE_AS_PROCESS:
                                this.saveAsProcess();
                                break;

                            case QUIT_APPLICATION:
                                this.quit(true);
                                break;
                        }
                        break;
                }
            }
        }
        catch (Exception ex)
        {
            ExceptionManager.getInstance().manage(ex);
        }

    }

    /**
     * Lance l'application
     */
    private void run()
    {
        // rends la fenetre visible et la maximise
        final Dimension screenSize = Toolkit.getDefaultToolkit()
                .getScreenSize();
        this.mfPagod.setPreferredSize(new Dimension((int) (screenSize
                .getWidth() * 3.0 / 4.0),
                (int) (screenSize.getHeight() * 3.0 / 4.0)));
        this.mfPagod.pack();
        this.mfPagod.setVisible(true);
        this.mfPagod.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.state = State.INIT;
    }

    /**
     * Ferme l'application
     * 
     * @param saveMessage
     *            vrai si on veut on message de confirmation de sauvegarde
     */
    private void quit(boolean saveMessage)
    {
        boolean quitAgreement = true;
        // si on souhaite un message de sauvegarde
        if (saveMessage)
        {
            final int choice = JOptionPane.showConfirmDialog(this.mfPagod,
                    LanguagesManager.getInstance().getString(
                            "saveConfirmationTitle"),
                    LanguagesManager.getInstance().getString(
                            "saveConfirmationMessage"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            // si il veut rie nfaire
            if (choice == JOptionPane.CANCEL_OPTION)
            {
                quitAgreement = false;
            }
            // si il veux savegarder alors
            else if (choice == JOptionPane.YES_OPTION)
            {
                // on sauvegarde mais si la sauvegarde c'est mal passer
                // alors on arrete tout
                if (!this.saveProcess())
                    quitAgreement = false;
            }
            else
            {
                // si il ne veux pas sauvegarder alors on arrete tout
                quitAgreement = true;
            }
        }
        if (quitAgreement)
        {
            // on sauvegarde les preferences que l'utilisateur a pu
            // modifier
            PreferencesManager.getInstance().storeExtensions();
            System.exit(0);
        }
    }

    /**
     * Gère l'ouverture d'un processus
     * 
     * @param saveMessage
     *            vrai si on veut proposer une sauvegarde
     * @return true si le processus est ouvert
     */
    private boolean openProcess(boolean saveMessage)
    {
        boolean open = false;
        boolean openAgreement = true;
        // on demande de sauvegarder si c'est souhaiter
        if (saveMessage)
        {
            final int choice = JOptionPane.showConfirmDialog(this.mfPagod,
                    LanguagesManager.getInstance().getString(
                            "saveConfirmationTitle"),
                    LanguagesManager.getInstance().getString(
                            "saveConfirmationMessage"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.CANCEL_OPTION)
            {
                openAgreement = false;
            }
            // si il veux savegarder alors
            else if (choice == JOptionPane.YES_OPTION)
            {
                // on sauvegarde mais si la sauvegarde c'est mal passer
                // alors on arrete tout
                if (!this.saveProcess())
                    openAgreement = false;
            }
            else
            {
                // si il ne veux pas sauvegarder alors on arrete tout
                openAgreement = true;
            }
        }
        if (openAgreement)
        {
            ProcessFileChooser fileChooser = new ProcessFileChooser();
            if (fileChooser.showOpenDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION)
            {
                // Remplir le modèle metier
                InterfaceManager im = InterfaceManager.getInstance();
                File choosenFile = fileChooser.getSelectedFile();
                Process aProcess = im.importModel(
                        choosenFile.getAbsolutePath(), this.mfPagod, true);
                // si l'ouverture c'est bien passer
                if (aProcess != null)
                {
                    // recuperer les Roles choisis
                    // creer le TreeModel nécessaire au JTree de la fenetre
                    // presenter a l'utilisateur le processus
                    this.mfPagod
                            .presentProcess(aProcess, choosenFile.getName());
                    // mettre a jour le processus en cours
                    this.currentProcess = aProcess;
                    // mettre a jour l etat
                    this.state = State.PROCESS_OPENED;
                    return true;
                }
            }
        }
        return open;
    }

    /**
     * Enregistre
     * 
     * @return vrai si le fichier a ete enregistrer
     */
    private boolean saveProcess()
    {
        InterfaceManager im = InterfaceManager.getInstance();
        if (im.getType() == InterfaceManager.Type.PAGOD)
            return im.exportProcess(null, this.currentProcess, this.mfPagod);
        else
            return this.saveAsProcess();
    }

    /**
     * Enregistrer sous
     * 
     * @return vrai si le fichier a ete enregistrer
     */
    private boolean saveAsProcess()
    {
        ProcessOutputFileChooser fileChooser = new ProcessOutputFileChooser();
        String savePath = null;
        // Chemin de sauvegarde
        do
        {
            if (fileChooser.showSaveDialog(this.mfPagod) == JFileChooser.APPROVE_OPTION)
            {
                // on verifie si le fichier existe deja
                if (fileChooser.getSelectedFile().exists())
                {
                    // si il existe on demande confirmation
                    if (JOptionPane.showConfirmDialog(this.mfPagod,
                            LanguagesManager.getInstance().getString(
                                    "eraseFileConfirmationMessage"),
                            LanguagesManager.getInstance().getString(
                                    "eraseFileConfirmationTitle"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
                    {
                        // si on souhaite ecrase
                        savePath = fileChooser.getSelectedFile().getPath();
                    }
                }
                // si le fichier n'existe pas
                else
                {
                    savePath = fileChooser.getSelectedFile().getPath();
                }
            }
            // si on selectionne annuler
            else
            {
                // on arrete tout
                return false;
            }
        } while (savePath == null);
        // on enregistre

        return InterfaceManager.getInstance().exportProcess(savePath,
                this.currentProcess, this.mfPagod);
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
     * lance la fenetre de configuration des preferences
     */
    private void showPreferencesDialog()
    {
        PreferencesDialog pd = new PreferencesDialog(this.mfPagod);
        pd.setVisible(true);
    }
}
