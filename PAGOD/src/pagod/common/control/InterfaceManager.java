/*
 * $Id: InterfaceManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of pagod.
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
 * along with pagod; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.common.control; 

import java.awt.Component;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import pagod.common.model.Process;
import pagod.utils.ExceptionManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Gestionnaire des interfaces avec l'exterieur.
 * 
 * @author pierrot
 */
public class InterfaceManager
{
    /** Nom du fichier de configuration */
    private static final String CONFIG_FILENAME = "configuration.xml";

    /**
     * type du document ouvert
     */
    public enum Type {
        /**
         * fichier DPC
         */
        DPC,
        /**
         * fichier pagod
         */
        PAGOD
    }

    /** Type du dernier fichier ouvert */
    private Type openedFileType = null;

    /** Dernier fichier ouvert */
    private File openedFile = null;

    /** Instance de l'InterfaceManager */
    private static InterfaceManager currentInstance = null;

    /**
     * Singleton
     * 
     * @return Instance de l'InterfaceManager
     */
    public static InterfaceManager getInstance()
    {
        if (InterfaceManager.currentInstance == null)
            InterfaceManager.currentInstance = new InterfaceManager();
        return InterfaceManager.currentInstance;
    }

    /**
     * Constructeur du gestionnaire d'interface
     */
    private InterfaceManager()
    {
    }

    /**
     * @return le type du fichier qui a été ouvert
     */
    public Type getType()
    {
        return this.openedFileType;
    }

    /**
     * @return Fichier ouvert
     */
    public File getFile()
    {
        return this.openedFile;
    }

    /**
     * Recupere dans le fichier dont le chemin est passe en parametre toutes les
     * informations du processus sur lequel l'assistance doit s'appliquer.
     * Initialise ensuite le processus et retourne son instance.
     * 
     * @param path
     *            chemin vers le fichier à parser
     * @param parent
     *            Elément graphique parent
     * @param createTempFile
     *            TRUE pour demander la création du fichier temporaire, FALSE
     *            sinon
     * @return instance du processus à assister
     * @throws NotInitializedException
     * @throws MissingResourceException
     */
    public Process importModel(String path, Component parent,
                               boolean createTempFile)
                                                      throws MissingResourceException,
                                                      NotInitializedException
    {
        final File fileToOpen = new File(path);
        Type fileToOpenType = null;
        Process process = null;

        // changement du curseur
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try
        {
            final DPCHandler h = new DPCHandler(new URL("jar:"
                    + fileToOpen.toURL() + "!/"));

            try
            {
                if (h.loadDPC())
                    fileToOpenType = Type.PAGOD;
                else
                    fileToOpenType = Type.DPC;

                process = h.getProcess();
            }
            catch (DPCHandler.InvalidFileException ife)
            {
                JOptionPane.showMessageDialog(parent, LanguagesManager
                        .getInstance().getString(
                                "InterfaceManager_invalidProcessFileMessage"),
                        LanguagesManager.getInstance().getString(
                                "InterfaceManager_invalidProcessFileTitle"),
                        JOptionPane.ERROR_MESSAGE);
                // TODO supprimer l'affichage de la pile
                ExceptionManager.getInstance().manageWarning(ife);
            }
            catch (DPCHandler.InvalidConfigurationFile icf)
            {
                JOptionPane
                        .showMessageDialog(
                                parent,
                                LanguagesManager
                                        .getInstance()
                                        .getString(
                                                "InterfaceManager_invalidConfigurationFileMessage"),
                                LanguagesManager
                                        .getInstance()
                                        .getString(
                                                "InterfaceManager_invalidConfigurationFileTitle"),
                                JOptionPane.INFORMATION_MESSAGE);
                // TODO supprimer l'affichage de la pile
                ExceptionManager.getInstance().manageWarning(icf);

                process = h.getProcess();
                fileToOpenType = Type.DPC;
            }
        }
        catch (MalformedURLException mue)
        {
            JOptionPane.showMessageDialog(parent, LanguagesManager
                    .getInstance()
                    .getString("InterfaceManager_urlErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_urlErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(mue);
        }

        // changement du curseur
        parent.setCursor(Cursor.getDefaultCursor());

        if (process != null)
        {
            this.openedFile = fileToOpen;
            this.openedFileType = fileToOpenType;
        }
        return process;
    }

    /**
     * Ecrit le fichier pagod à l'emplacement spécifié
     * 
     * @param path
     *            Chemin du fichier à créer
     * @param process
     *            Processus configuré à sauvegarder
     * @param parent
     *            Elément graphique parent
     * @return vrai si le process a été correctement enregistrer
     * @throws NotInitializedException
     * @throws MissingResourceException
     */
    public boolean exportProcess(String path, Process process, Component parent)
                                                                                throws MissingResourceException,
                                                                                NotInitializedException
    {
        boolean result = false;

        // changement du curseur
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // création du pagod
        try
        {
            // création du transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // convertisseur du processus
            final DPCHandler h = new DPCHandler(process);
            final Source domSrc = new DOMSource(h.createConfig());

            final File outputFile;
            if (path == null)
                outputFile = this.openedFile;
            else
                outputFile = new File(path);

            // ouverture d'un flux sur le fichier actuellement ouvert
            InputStream inputStream = new FileInputStream(this.openedFile);

            // si écriture dans le fichier ouvert
            if (outputFile.equals(this.openedFile))
            {
                // stockage du fichier source en mémoire
                final byte[] inputBuffer = new byte[(int) this.openedFile
                        .length()];
                inputStream.read(inputBuffer);
                inputStream.close();

                // utilisation de l'image pour la lecture
                inputStream = new ByteArrayInputStream(inputBuffer);
            }
            // sinon utilisation directe du fichier ouvert

            final ZipInputStream src = new ZipInputStream(inputStream);
            final ZipOutputStream dest = new ZipOutputStream(
                    new FileOutputStream(outputFile));

            final byte[] buffer = new byte[1024];

            // copie du fichier existant
            ZipEntry entry;
            while ((entry = src.getNextEntry()) != null)
            {
                int n;

                // si un fichier pagod, pas de copie du fichier de configuration
                if (this.openedFileType == Type.PAGOD
                        && entry.getName().equals(CONFIG_FILENAME))
                    continue;

                // création de l'entrée
                dest.putNextEntry(new ZipEntry(entry.getName()));

                // copie des données
                while ((n = src.read(buffer)) != -1)
                    dest.write(buffer, 0, n);

                // fermeture de l'entrée courante
                src.closeEntry();
                dest.closeEntry();
            }

            // ajout du fichier de configuration
            entry = new ZipEntry(CONFIG_FILENAME);
            dest.putNextEntry(entry);
            transformer.transform(domSrc, new StreamResult(dest));

            // fermeture des flux
            src.close();
            dest.close();

            // le fichier destination est le nouveau fichier ouvert
            this.openedFile = outputFile;
            this.openedFileType = Type.PAGOD;

            result = true;
        }
        catch (FileNotFoundException fnfe)
        {
            JOptionPane.showMessageDialog(parent, LanguagesManager
                    .getInstance().getString(
                            "InterfaceManager_errorOriginalFileMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_errorOriginalFileTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(fnfe);
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(parent,
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_ioErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_ioErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(ioe);
        }
        catch (DPCHandler.XMLGeneratorException xge)
        {
            JOptionPane.showMessageDialog(parent, LanguagesManager
                    .getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(xge);
        }
        catch (TransformerConfigurationException tce)
        {
            JOptionPane.showMessageDialog(parent, LanguagesManager
                    .getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(tce);
        }
        catch (TransformerException te)
        {
            JOptionPane.showMessageDialog(parent, LanguagesManager
                    .getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "InterfaceManager_xmlGenerationErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            // TODO supprimer l'affichage de la pile
            ExceptionManager.getInstance().manageWarning(te);
        }

        // changement du curseur
        parent.setCursor(Cursor.getDefaultCursor());

        return result;
    }

    /**
     * Extrait le contenu du fichier CSS
     * 
     * @return Contenu du fichier CSS
     */
    public StringBuffer getCSSFileContent()
    {
        StringBuffer result = new StringBuffer();

        try
        {
            final FileInputStream in = new FileInputStream(this.openedFile);
            final ZipInputStream src = new ZipInputStream(in);

            // recherche de l'entrée
            ZipEntry entry;
            while ((entry = src.getNextEntry()) != null)
            {
                final String name = entry.getName();

                if (name.startsWith("Site/styles/") && name.endsWith(".css"))
                {
                    char[] buf = new char[256];
                    int n;

                    BufferedReader r = new BufferedReader(
                            new InputStreamReader(src));
                    while ((n = r.read(buf)) != -1)
                        result.append(buf, 0, n);
                    result.append("\n");
                }

                src.closeEntry();
            }

            // fermeture des flux
            src.close();
            in.close();
        }
        catch (IOException ioe)
        {
        }

        return result;
    }
}
