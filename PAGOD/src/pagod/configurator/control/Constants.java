/*
 * $Id: Constants.java,v 1.3 2006/01/23 17:43:55 yak Exp $
 *
 * PAGOD - Spem Wizard
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

/**
 * Interface contenant l'ensemble de constante lié à l'application.
 * 
 * @author MoOky
 */
public interface Constants
{
    /**
     * Nom court de l'application : PAGOD
     */
    public static final String APPLICATION_SHORT_NAME = "PAGOD configurator";

    /**
     * Nom complet de l'application : Spem Wizard
     */
    public static final String APPLICATION_LONG_NAME = "Spem Wizard configurator";

    /**
     * Version de l'application
     */
    public static final String APPLICATION_VERSION = "1.00 (20050323)";

    /**
     * Chemin du répertoire contenant les ressources de l'application
     */
    public static final String RESOURCES_DIRECTORY = "resources/";

    /**
     * Chemin du repertoire de sortie du fichier de preferences
     */
    public static final String PREFERENCES_DIRECTORY = "preferences"
            + File.separator;

    /**
     * Chemin du répertoire contenant les fichiers langues
     */
    public static final String LANGUAGES_DIRECTORY = RESOURCES_DIRECTORY
            + "languages/configurator/";

    /**
     * Prefixe des fichiers langues
     */
    public static final String LANGUAGES_FILE_PREFIXE = "pagod_configurator";

    /**
     * Chemin du fichier de langue par defaut
     */
    public static final String LANGUAGES_FILE = LANGUAGES_DIRECTORY
            + LANGUAGES_FILE_PREFIXE;

    /**
     * Chemin du répertoire contenant les fichiers langues
     */
    public static final String LANGUAGES_OUTPUT_DIRECTORY = "languages"
            + File.separator;

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
    public static final String ACTION_OPEN = "openAction";

    /**
     * <code>ACTION_SAVE</code>
     */
    public static final String ACTION_SAVE = "saveAction";

    /**
     * <code>ACTION_SAVE_AS</code>
     */
    public static final String ACTION_SAVE_AS = "saveAsAction";

    /**
     * <code>ACTION_ABOUT</code>
     */
    public static final String ACTION_ABOUT = "aboutAction";

    /**
     * <code>ACTION_PREFERENCES</code>
     */
    public static final String ACTION_PREFERENCES = "preferencesAction";

}
