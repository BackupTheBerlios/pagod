/*
 * $Id: Constants.java,v 1.17 2006/03/12 02:11:23 fabfoot Exp $
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

/**
 * Interface contenant l'ensemble de constante li? ? l'application.
 * 
 * @author MoOky
 */
public interface Constants
{
    /**
     * Nom court de l'application : PAGOD
     */
    public static final String APPLICATION_SHORT_NAME = "PAGOD";

    /**
     * Nom complet de l'application : Personal Assistant for Group Of Development
     */
    public static final String APPLICATION_LONG_NAME = "Personal Assistant for Group Of Development";

    /**
     * Version de l'application
     */
    public static final String APPLICATION_VERSION = "1.00 (20050323)";

    /**
     * Chemin du r?pertoire ou vont etre stocker les fichiers de sauvegarde des outils
     */
    public static final String TOOLS_DIRECTORY = "tools" + File.separator;
    
    /**
     * Chemin du r?pertoire contenant les ressources de l'application
     */
    public static final String RESOURCES_DIRECTORY = "resources/";

    	
    	
    
    /**
     * Chemin du repertoire de sortie du fichier de preferences
     */
    public static final String PREFERENCES_DIRECTORY = "preferences" + File.separator;
    
    /**
     * Chemin du r?pertoire contenant les fichiers langues
     */
    public static final String LANGUAGES_DIRECTORY = RESOURCES_DIRECTORY
        + "languages/wizard/" ;
    
    /**
     * Prefixe des fichiers langues
     */
    public static final String LANGUAGES_FILE_PREFIXE = "pagod_wizard";
    
    /**
     * Chemin du fichier de langue par defaut
     */
    public static final String LANGUAGES_FILE = LANGUAGES_DIRECTORY
            + LANGUAGES_FILE_PREFIXE;
    /**
     * Chemin du r?pertoire contenant les fichiers langues
     */
    public static final String LANGUAGES_OUTPUT_DIRECTORY = "languages" + File.separator ;
    
    /**
     * Chemin du fichier de langue par defaut
     */
    public static final String LANGUAGES_OUTPUT_FILE = LANGUAGES_OUTPUT_DIRECTORY
            + LANGUAGES_FILE_PREFIXE;
    /**
     * Chemin du repertoire contenant les images de l'application
     */
    public static final String IMAGES_DIRECTORY = RESOURCES_DIRECTORY
            + "images/";

    // Cle des actions de l'application
    /**
     * <code>ACTION_QUIT</code>
     */
    public static final String ACTION_QUIT = "quitAction";
    

    /**
     * <code>ACTION_OPENPROCESS</code>
     */
    public static final String ACTION_OPENPROCESS = "openProcessAction";
    /**
     * <code>ACTION_NEWPROJECT</code>
     */
    public static final String ACTION_NEWPROJECT = "newProjectAction";

    /**
     * <code>ACTION_OPENPROJECT</code>
     */
    public static final String ACTION_OPENPROJECT = "openProjectAction";
    
    /**
     * <code>ACTION_CLOSEPROJECT</code>
     */
    public static final String ACTION_CLOSEPROJECT = "closeProjectAction";
    
    /**
     * <code>ACTION_ABOUT</code>
     */
    public static final String ACTION_ABOUT = "aboutAction";

    /**
     * <code>ACTION_NEXT_ITERATION</code>
     */
    public static final String ACTION_NEXT_ITERATION = "goToNextIteration";
   
    /**
     * <code>EXPORT_TIME</code>
     */
    public static final String ACTION_EXPORT_TIME = "exportTime";
    
    /**
     * <code>ACTION_TIME_CURRENT_ITERATION</code>
     */
    public static final String ACTION_TIME_CURRENT_ITERATION = "consultTimeCurrentIteration";
    
    /**
     * <code>ACTION_TIME_ALL_ITERATIONS</code>
     */
    public static final String ACTION_TIME_ALL_ITERATIONS = "consultTimeAllIterations";
    
    
    /**
     * <code>ACTION_RUN_ACTIVITY</code>
     */
    public static final String ACTION_RUN_ACTIVITY = "runActivityAction";

    /**
     * <code>ACTION_NEXT</code>
     */
    public static final String ACTION_NEXT = "nextAction";

    /**
     * <code>ACTION_PREVIOUS</code>
     */
    public static final String ACTION_PREVIOUS = "previousAction";

    /**
     * <code>ACTION_TERMINATE</code>
     */
    public static final String ACTION_TERMINATE = "terminateAction";
    /**
     * <code>ACTION_SUSPEND</code>
     */
    public static final String ACTION_SUSPEND = "supendAction";
    /**
     * <code>ACTION_GOTOSTEP</code>
     */
    public static final String ACTION_GOTOSTEP = "gotostep";

    /**
     * <code>ACTION_PREFERENCES</code>
     */
    public static final String ACTION_PREFERENCES = "preferencesAction";

    /**
     * <code>ACTION_TOOLSSETTINGS</code>
     */
    public static final String ACTION_TOOLSSETTINGS = "toolsSettingsAction";
    
    /**
     * <code>NAME_FILE_TIME</code>
     */
    public static final String NAME_FILE_TIME = "time.xml";
    
    /**
	 * <code>DOCS_DIRECTORY</code>
	 */
	public static final String	DOCS_DIRECTORY	= "produits" + File.separator;
	
    /**
     * <code>ACTION_IMPORT_PROJECT</code>
     */
    public static final String ACTION_IMPORT_PROJECT = "importProjectAction";
    
}
