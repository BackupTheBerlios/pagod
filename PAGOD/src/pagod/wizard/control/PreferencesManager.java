/*
 * $Id: PreferencesManager.java,v 1.6 2006/01/05 15:29:00 biniou Exp $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Observable;
import java.util.Properties;

import javax.swing.JOptionPane;

import pagod.utils.FilesManager;
import pagod.utils.LanguagesManager;

/**
 * PreferencesManager est implant? comme un singleton. Cette classe gere les
 * preferences de l'utilisateur a savoir la lang ainsi que les associations
 * entre extension de fichier et executable.
 * 
 * @author Alexandre Bes
 */
public class PreferencesManager extends Observable
{
    /**
     * Exception lev?e si l'extension n'est pas valide c'est a dire si elle ce
     * termine par un .
     */
    public class InvalidExtensionException extends Exception
    {
    }

    /**
     * Exception lev?e si l'extension le programme n'est pas executable
     */
    public class FileNotExecuteException extends Exception
    {
    }

    /**
     * Attribut contenant les associations cl?-valeur. Les cl? sont les
     * extensions des fichiers sans le "." et les valeurs sont les chemins
     * d'acces ? l'application qui ouvre ce type de fichiers.
     * 
     * Il y une cl? particuliere "lang" qui a pour valeur la langue choisi par
     * l'utilisateur
     */
    private static Properties preferences = null;

    /**
     * Chemin d'acces au fichier de preferences
     */
    private static String sPathFilePreferences;

    /**
     * Nom du fichier de preference
     */
    private static String sNameFilePreferences = "wizard_preferences.properties";

    /**
     * Instance du gestionnaire de preference
     */
    private static PreferencesManager instance = null;

    /**
     * Retourne l'instance du gestionnaire de preference
     * 
     * @return instance du gestionnaire de preference
     */
    public static PreferencesManager getInstance()
    {
        if (instance == null)
            instance = new PreferencesManager();

        return instance;
    }

    /**
     * Retourne la langue choisi par l'utilisateur A defaut la langue de la JVM
     * sera choisi et defini dans les preferences
     * 
     * @return la langue choisi par l'utilisateur
     */
    public String getLanguage()
    {
        String sLang = preferences.getProperty("lang");

        return sLang;
    }

    /**
     * Defini la langue "language" comme etant la langue choisi par
     * l'utilisateur
     * 
     * REMARQUE : cette fonction ne verifie pas que la locale est valide
     * 
     * @param language
     *            est la langue a definir
     */
    public void setLanguage(String language)
    {
        preferences.setProperty("lang", language);
    }
    
    /**
     * Retourne le chemin du workspace choisi par l'utilisateur 
     * 
     * @return le chemin choisi par l'utilisateur
     */
    public String getWorkspace()
    {
        String sPathWorkspace = preferences.getProperty("workspace");

        return sPathWorkspace;
    }

    /**
     * Defini le chemin "pathWorkspace" comme etant le chemin choisi par
     * l'utilisateur
     * 
     * REMARQUE : cette fonction ne verifie pas que la locale est valide
     * 
     * @param pathWorkspace
     *            est le chemin du workspace a definir
     */
    public void setWorkspace(String pathWorkspace)
    {
        preferences.setProperty("workspace", pathWorkspace);
    }

    /**
     * Retourne le chemin d'acces au logiciel capable de visualiser les fichiers
     * portant l'extension "extension"
     * 
     * @param extension
     *            est l'extension du fichier (avec ou sans le point)
     * @return un chemin d'acces a un logiciel ou null si aucun logiciel n'a ete
     *         associe a cette extension
     * @throws InvalidExtensionException
     */
    public String getPreference(String extension)
                                                 throws InvalidExtensionException
    {
        String sTemp;

        // on recupere l'extension avec le .
        int i = extension.lastIndexOf(".");

        if (i <= -1)
            // si extension ne contient pas de .
            sTemp = "." + extension;
        else if (i != extension.length() - 1)
            // si l'extension ne ce termine pas par un .
            sTemp = extension.substring(extension.lastIndexOf("."));
        else
            throw new InvalidExtensionException();

        return preferences.getProperty(sTemp);
    }

    /**
     * Defini le chemin d'acces au logiciel a utiliser pour une extension donne
     * 
     * @param extension
     *            l'extension des fichiers
     * @param path
     *            le chemin d'acces au logiciel
     * @throws InvalidExtensionException
     *             exception leve lorsque l'extension n'est pas valide (elle ce
     *             termine par un .)
     * @throws FileNotFoundException
     *             lorsque le fichier (path) n'existe pas
     * @throws FileNotExecuteException
     *             lorsque le fichier n'est pas executable
     */
    public void setPreference(String extension, String path)
                                                            throws InvalidExtensionException,
                                                            FileNotFoundException,
                                                            FileNotExecuteException
    {
        String sTemp = "";

        // on recupere l'extension avec le .
        int i = extension.lastIndexOf(".");

        if (i <= -1)
            // si extension ne contient pas de .
            sTemp = "." + extension;
        else if (i != extension.length() - 1)
            // si l'extension ne se termine pas par un .
            sTemp = extension.substring(extension.lastIndexOf("."));
        else
            throw new InvalidExtensionException();

        // on test si le fichier est executable
        File file = new File(path);

        // si le fichier n'existe pas on leve une exception si on utilise pas l'extension par default
        if(!path.equals("default"))
        	if (!file.exists())
        		throw new FileNotFoundException();

        /*
         * // si le fichier n'est pas executable on leve une exception
         * FilePermission fileP = new FilePermission(path, "read"); if
         * (fileP.implies(new Permission ("execute"))) { throw new
         * FileNotExecuteException () ; }
         */

        // on ajoute l'association dans les preferences avec les extensions en
        // minuscules
        preferences.setProperty(sTemp.toLowerCase(), path);

        // notifie tous les observateurs du PreferencesManager qu'une preference
        // a chang?
        this.setChanged();
        this.notifyObservers(extension);
    }
    
    

    /**
     * Permet de tester l'existance d'une association extension-programme
     * 
     * @param extension
     *            extension dont on veut savoir si un programme a ete defini
     *            pour visualiser les fichiers de ce type
     * @return true s'il existe une association extension-programme dans les
     *         preferences sinon false
     */
    public boolean containPreference(String extension)
    {
        String sTemp = "";

        // on recupere l'extension avec le .
        int i = extension.lastIndexOf(".");

        if (i <= -1)
            // si extension ne contient pas de .
            sTemp = "." + extension;
        else if (i != extension.length() - 1)
            // si l'extension ne ce termine pas par un .
            sTemp = extension.substring(extension.lastIndexOf("."));
        else
            // throw new InvalidExtensionException();
            return false;

        return preferences.containsKey(sTemp);
    }
    
    /**
     * Permet de tester l'existence d'un chemin par défaut pour le workspace
     * 
     * @return true s'il existe un chemin dans les preferences sinon false
     */
    public boolean containWorkspace()
    {
        return preferences.containsKey("workspace");
    }
    
    /**
     * supprime le workspace
     */
    public void removeWorkspace()
    {
    	if (this.containWorkspace())
    	{
    		preferences.remove("workspace");
    	}
    	
    }

    /**
     * Supprime l'association entre l'extension de fichier pass? en parametre et
     * le programme qui permet de visualiser ce type de fichier.
     * 
     * @param extension
     *            l'extension du fichier
     */
    public void removePreference(String extension)
    {
        String sTemp = null;

        // on recupere l'extension avec le .
        int i = extension.lastIndexOf(".");

        if (i <= -1)
            // si extension ne contient pas de .
            sTemp = "." + extension;
        else if (i != extension.length() - 1)
            // si l'extension ne ce termine pas par un .
            sTemp = extension.substring(extension.lastIndexOf("."));

        if (sTemp != null)
        {
            preferences.remove(sTemp.toLowerCase());

            // notifie tous les observateurs du PreferencesManager qu'une
            // preference a chang?
            this.setChanged();
            this.notifyObservers(extension);
        }
    }

    /**
     * Stock les preferences definies par l'utilisateur dans le fichier
     * "wizard_preferences.properties" (cf attribut static sNameFilePreferences
     * de cette classe) a l'emplacement design? par le parametre
     * sPathFilePreferences
     * 
     * @return vrai si les preferences ont été sauvegardé
     * 
     */
    public boolean storePreferences()
    {
        File fileDirectory = new File(sPathFilePreferences);
        if (!fileDirectory.exists())
            fileDirectory.mkdir();

        File filePreferences = new File(sPathFilePreferences
                + sNameFilePreferences);

        // on ecrit le fichier sur disque a l'emplacement path
        String comments = "Fichier contenant toutes les preferences definies par l'utilisateur.";
        try
        {
            preferences.store(new FileOutputStream(filePreferences), comments);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, LanguagesManager.getInstance()
                    .getString("storePreferencesErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "storePreferencesErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Stock les extensions definies par l'utilisateur (mais pas la langue) dans
     * le fichier "wizard_preferences.properties" (cf attribut static
     * sNameFilePreferences de cette classe) a l'emplacement design? par le
     * parametre sPathFilePreferences
     * @return vrai si les extensions on bien été enregistrés
     * 
     */
    public boolean storeExtensions()
    {
        String precLanguage = null;
        String currentLanguage = null;
        Properties properties = new Properties();

        File filePreferences = new File(sPathFilePreferences
                + sNameFilePreferences);

        File fileDirectory = new File(sPathFilePreferences);
        if (!fileDirectory.exists())
            fileDirectory.mkdir();
        else
        {
            // on test l'existance du fichier de preferences
            if (filePreferences.exists())
            {
                // chargement des preferences du fichier
                // on charge le fichier des preferences
                try
                {
                    properties.load(new FileInputStream(filePreferences));

                    // recuperation de la langue precedement
                    precLanguage = properties.getProperty("lang");
                }
                catch (IOException exception)
                {
                    JOptionPane.showMessageDialog(null, LanguagesManager
                            .getInstance().getString(
                                    "loadPreferencesErrorMessage"),
                            LanguagesManager.getInstance().getString(
                                    "loadPreferencesErrorTitle"),
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        }

        if (precLanguage != null)
        {
            currentLanguage = this.getLanguage();
            boolean storeStatus;
            // on change la langue avant de sauver le fichier
            this.setLanguage(precLanguage);
            
            storeStatus = this.storePreferences();
            /*
             * //on ecrit le fichier sur disque a l'emplacement path String
             * comments = "Fichier contenant toutes les preferences definies par
             * l'utilisateur."; preferences.store(new
             * FileOutputStream(filePreferences), comments);
             */

            // on remet la langue courante
            this.setLanguage(currentLanguage);
            return storeStatus;
        }
        else
        {
            
            return this.storePreferences();
            /*
             * //on ecrit le fichier sur disque a l'emplacement path String
             * comments = "Fichier contenant toutes les preferences definies par
             * l'utilisateur."; preferences.store(new
             * FileOutputStream(filePreferences), comments);
             */

        }
    }

    /**
     * Retourne un ArrayList de String contenant les preferences (sans la
     * langue) definies par l'utilisateur. Les cl?s sont tri? par ordre
     * alphab?tique.
     * 
     * @return un ArrayList de String contenant les preferences (sans la langue
     *  et le workspace) tri? par ordre alphab?tique
     */
    public ArrayList<String> preferences()
    {
        ArrayList<String> extensions = new ArrayList<String>();
        String sExtension;

        // on rempli l'ArrayList
        for (Enumeration keys = preferences.keys() ; keys.hasMoreElements() ;)
        {
            sExtension = keys.nextElement().toString();

            if (!sExtension.equals("lang") && !sExtension.equals("workspace"))
                extensions.add(sExtension);
        }

        // on trie l'ArrayList
        Collections.sort(extensions);

        return extensions;
    }

    /**
     * Retourne le nombre d'associations (Extension - application) definie par
     * l'utilisateur.
     * 
     * @return le nombre d'associations defini par l'utilsateur
     */
    public int numberExtensions()
    {
    	int numberExt = 0;
    	String sExtension;
    	for (Enumeration keys = preferences.keys() ; keys.hasMoreElements() ;)
        {
    		sExtension = keys.nextElement().toString();
    		if (sExtension.lastIndexOf(".") > -1)
    		{
    			numberExt ++;    			
    		}
    		
        }
    	
        // on met - 1 car il ne faut pas compter la cl? "lang"
        return numberExt;
    }

    /**
     * Charge les preferencse definies par l'utilisateur.
     * @return vrai si les preferences on put être charger
     * 
     */
    public boolean loadPreferences()
    {
        String path = sPathFilePreferences + sNameFilePreferences;

        File filePreferences = new File(path);

        // on test l'existance du fichier de preferences
        if (filePreferences.exists())
        {
            // on charge le fichier des preferences
            try
            {
                preferences.load(new FileInputStream(filePreferences));
                System.out.println(preferences.toString());
                // si la cle "lang" n'a pas put etre trouve on defini le
                // francais
                // par defaut et on met la JVM en francais
                String sLang = preferences.getProperty("lang");
                if (sLang == null)
                {
                    Locale.setDefault(Locale.FRENCH);
                    sLang = Locale.getDefault().getLanguage();
                    preferences.setProperty("lang", sLang);
                }
                else
                {
                    // on met la JVM dans la langue choisi par l'utilisateur
                    Locale tempLocale = new Locale(this.getLanguage());
                    Locale.setDefault(tempLocale);
                }

                // enleve les associations si le programme n'existe plus
                checkPreferences();
            }
            catch (IOException exception)
            {              
                // defini le francais comme langue choisi par l'utilisateur
                Locale.setDefault(Locale.FRENCH);
                String sLang = Locale.getDefault().getLanguage();
                preferences.setProperty("lang", sLang);
                
                // On previent l'utilisateur
                JOptionPane.showMessageDialog(null,
                        LanguagesManager.getInstance().getString(
                                "loadPreferencesErrorMessage"),
                        LanguagesManager.getInstance().getString(
                                "loadPreferencesErrorTitle"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        else
        {
            // le fichier de preference n'existe pas donc on le cree et on
            // defini le francais comme langue choisi par l'utilisateur
            Locale.setDefault(Locale.FRENCH);
            String sLang = Locale.getDefault().getLanguage();
            preferences.setProperty("lang", sLang);
            // on cree le fichier
            this.storePreferences();
        }
        return true;
    }

    /**
     * Constructeur priv? du gestionnaire de preference (implantation d'un
     * singleton)
     * 
     * 
     * Remarque : le constructeur met a jour sPathFilePreferences
     */
    private PreferencesManager()
    {
        preferences = new Properties();

        sPathFilePreferences = FilesManager.getInstance().getRootPath()
                + Constants.PREFERENCES_DIRECTORY;
        // on charge les preferences
        this.loadPreferences();
    }

    /**
     * Verifie que les executables associ?s aux extensions soit toujours present
     * sur la machine, si ce n'est pas le cas l'association entre l'extension et
     * l'executable est supprim? des preferences.
     */
    private void checkPreferences()
    {
        File file;

        /*
         * pour toutes les associations entre extension et programme on verifi
         * que les programmes existent toujours
         */
        for (Enumeration keys = preferences.keys() ; keys.hasMoreElements() ;)
        {
            String extension = keys.nextElement().toString();
            //TODO a améliorer
            if (!extension.equals("lang") && !extension.equals("workspace") )
            {
                file = new File(preferences.getProperty(extension));
               
                // si le fichier n'existe pas on supprime l'association des
                // preferences
                if (!file.exists()&&!file.getName().equals("default"))
                {
                   System.out.println("effacemment "+file.getName());
                	preferences.remove(extension);
                }
            }
        }
    }
}