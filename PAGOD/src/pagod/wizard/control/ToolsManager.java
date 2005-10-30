/*
 * $Id: ToolsManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.util.Enumeration;
import java.util.Observable;
import java.util.Properties;

import javax.swing.JOptionPane;

import pagod.utils.FilesManager;
import pagod.utils.LanguagesManager;
import pagod.common.model.Process;
import pagod.common.model.Tool;

/**
 * ToolsManager est implanté comme un singleton. Cette classe permet de sauvegarder les Tools
 * defini dans le model metier pour un processus c'est a dire de conserver les chemin d'acces aux outils que
 * l'utilisateur a put définir.
 * Permet egalement de charger les Tools defini dans le model metier pour un processus.
 * 
 * @author Alexandre Bes
 */
public class ToolsManager extends Observable
{
    /**
     * Exception levée si l'extension n'est pas valide c'est a dire si elle ce
     * termine par un .
     */
    public class InvalidExtensionException extends Exception
    {
    }

    /**
     * Exception levée si l'extension le programme n'est pas executable
     */
    public class FileNotExecuteException extends Exception
    {
    }

    /**
     * Attribut contenant les associations clé-valeur. Les clé sont les
     * les nom des outils et les valeurs sont les chemins
     * d'acces à l'application.
     */
    private Properties preferences = null;

    /**
     * Chemin d'acces au fichier de configuration des outils
     */
    private  String sToolsFilePath;
    
    /**
     * Chemin d'acces au repertoire contenant les fichiers de configuration des outils
     */
    private  String sDirectoryPath;
    
    /**
     * Extension du fichier de configuration des outils
     */
    private  String sToolsFileExtension = ".properties";

    /**
     * Instance du gestionnaire d'outil
     */
    private static ToolsManager instance = null;
    
    /**
     * processus dont il faut stocker les outils
     */
    private Process process;

    /**
     * Retourne l'instance du gestionnaire de preference
     * 
     * @return instance du gestionnaire de preference
     */
    public static ToolsManager getInstance()
    {
        if (instance== null)
            instance = new ToolsManager();
            
        return instance;
    }
    /**
     * Initialise le ToolsManager
     * @param processToManage process dont il faut gerer les outils
     */
    public void initialise(Process processToManage)
    {
        this.process = processToManage;
        this.sToolsFilePath =this.sDirectoryPath
        + this.process.getName()+this.sToolsFileExtension;
        // on charge les preferences
        this.loadToolsAssociation();
    }
  
    /**
     * Retourne le chemin d'acces au logiciel associé à l'outil
     * 
     * @param toolname nom de l'outil
     * @return un chemin d'acces a un logiciel ou null si aucun logiciel n'a ete
     *         associe a cette extension
     */
    private String getToolPath(String toolname)
    {
        return this.preferences.getProperty(toolname);
    }

    /**
     * Defini le chemin d'acces au logiciel associé à l'outil
     * 
     * @param toolname nom de l'outil
     * @param path
     *            le chemin d'acces au logiciel
     *             termine par un .)
     * @throws FileNotFoundException
     *             lorsque le fichier (path) n'existe pas
     */
    private void setToolPath(String toolname, String path)
                                                            throws FileNotFoundException
    {
        // on test si le fichier est executable
        File file = new File(path);

        // si le fichier n'existe pas on leve une exception
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
        this.preferences.setProperty(toolname, path);
    }

    /**
     * Permet de tester l'existance d'une association Tool-programme
     * 
     * @param toolname
     *            nom de l'outil
     * @return true s'il existe une association extension-programme dans les
     *         preferences sinon false
     */
    public boolean containTool(String toolname)
    {
        return this.preferences.containsKey(toolname);
    }

    /**
     * Supprime l'association entre le nom de l'outil passé en parametre et
     * le programme associé.
     * 
     * @param toolname
     *            nom de l'outil
     */
    public void removeTool(String toolname)
    {
         this.preferences.remove(toolname.toLowerCase());
    }

    /**
     * charge les assosiation outils-chemin d'acces
     * 
     */
    public void storeToolsAssociation() 
    {
        // on met a jour le modele
        for (Tool t : this.process.getTools())
        {
            String toolName =  t.getName();
            if (toolName != null)
            {
                String toolPath  = t.getPath();
                if (toolPath != null)
                {
                    try {
						this.setToolPath(toolName,toolPath);
					} 
                    catch (FileNotFoundException e)
                    {
					}
                }
            }
        }
        
        File fileDirectory = new File(this.sDirectoryPath);
        if (!fileDirectory.exists())
            fileDirectory.mkdir();

        File fileTools = new File(this.sToolsFilePath);

        // on ecrit le fichier sur disque a l'emplacement path
        String comments = "Fichier contenant toutes les chemin d'acces aux outils pour le processus"+this.process.getName()+".";
        try {
			this.preferences.store(new FileOutputStream(fileTools), comments);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
                    LanguagesManager.getInstance().getString(
                            "storeToolsErrorMessage"),
                    LanguagesManager.getInstance().getString(
                            "storeToolsErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
		}
    }
    
    /**
     * Charge les association definies par l'utilisateur.
     * 
     */
    public void loadToolsAssociation()
    {
        String path = this.sToolsFilePath;

        File toolFile = new File(path);

        // on test l'existance du fichier de outil-path
        if (toolFile.exists())
        {
            // on charge le fichier des association outil-path
            try
            {
                this.preferences.load(new FileInputStream(toolFile));
            }
            catch (IOException exception)
            {
                JOptionPane.showMessageDialog(null,
                        LanguagesManager.getInstance().getString(
                                "loadToolsErrorMessage"),
                        LanguagesManager.getInstance().getString(
                                "loadToolsErrorTitle"),
                        JOptionPane.ERROR_MESSAGE);
            }

            // enleve les associations si le programme n'existe plus
            checkAssociation();
            // on met a jour le modele
            for (Tool t : this.process.getTools())
            {
                String toolName =  t.getName();
                if (toolName != null)
                {
                    String toolPath  = this.getToolPath(toolName);
                    if (toolPath != null)
                    {
                        t.setPath(toolPath);
                    }
                }
            }
        }
    }

    /**
     * Constructeur privé du gestionnaire de preference (implantation d'un
     * singleton)
     * 
     * 
     * Remarque : le constructeur met a jour sPathFilePreferences
     */
    private ToolsManager()
    {
        this.preferences = new Properties();
        
        this.sDirectoryPath = FilesManager.getInstance().getRootPath()
                + Constants.TOOLS_DIRECTORY;
    }

    /**
     * Verifie que les executables associés soit toujours present
     * sur la machine, si ce n'est pas le cas l'association est supprimé.
     */
    private void checkAssociation()
    {
        File file;
        /*
         * pour toutes les associations entre outil et chemin dacces on verifi
         * que le chemin dacces est correct
         */
        for (Enumeration keys = this.preferences.keys() ; keys.hasMoreElements() ;)
        {
            String toolName = keys.nextElement().toString();
            file = new File(this.preferences.getProperty(toolName));
            // si le fichier n'existe pas on supprime l'association
            if (!file.exists())
            {
                this.preferences.remove(toolName);
            }
        }
    }
}