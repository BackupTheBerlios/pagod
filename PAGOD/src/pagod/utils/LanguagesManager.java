/*
 * $Id: LanguagesManager.java,v 1.2 2006/03/04 12:56:18 garwind111 Exp $
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

package pagod.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Gestionnaire de langues implémenté comme un singleton Cette Classe permet
 * gérer la langue de l'application
 * 
 * @author MoOky
 */
public class LanguagesManager
{
    /**
     * Exception levée si le gestionnaire de langues n'est pas initialisé
     */
    public class NotInitializedException extends RuntimeException
    {
    }

    /**
     * Instance du gestionnaire de langues
     */
    private static LanguagesManager lmInstance = null;

    /**
     * Resource Bundle encapsuler
     */
    private ResourceBundle resourceBundle = null;

    /**
     * Constructeur privé du gestionnaire de langues (implémentation d'un
     * singleton)
     */
    private LanguagesManager()
    {
    }

    /**
     * retourne l'instance du gestionnaire de langues
     * 
     * @return instance du gestionnaire de langues
     */
    public static LanguagesManager getInstance()
    {
        if (LanguagesManager.lmInstance == null)
        {
            LanguagesManager.lmInstance = new LanguagesManager();
        }
        return LanguagesManager.lmInstance;
    }

    /**
     * Parametre la locale de l'application
     * 
     * @param resourceFile
     *            chemin d'acces au fichier de ressource
     * @param locale
     *            locale désiré
     * @param loader
     *            class loader utilisé pour retouvé les fichiers langues
     * @throws MissingResourceException
     *             Si aucun fichier langue n'a été trouvé pour la locale choisi.
     */
    public void setResourceFile(String resourceFile, Locale locale,
                                ClassLoader loader)                                        
    {
        this.resourceBundle = ResourceBundle.getBundle(resourceFile, locale,loader);
    }

    /**
     * Parametre la locale de l'application
     * 
     * @param resourceFile
     *            chemin d'acces au fichier de ressource
     * @param locale
     *            locale désiré
     * @throws MissingResourceException
     *             Si aucun fichier langue n'a été trouvé pour la locale choisi.
     */
    public void setResourceFile(String resourceFile, Locale locale)
    {
        this.resourceBundle = ResourceBundle.getBundle(resourceFile, locale);
    }

    /**
     * Retourne la chaine associée à la clé passer en parametre
     * 
     * @param key
     *            la clé de la chaine désiré
     * @return la chaine associé à la clé passer en parametre
     * @throws NotInitializedException
     *             Si le gestionnaire n'est pas initialisé.
     * @throws MissingResourceException
     *             Si la cle n'existe pas dans le fichier de langue.
     */
    public String getString(String key)
    {
        if (this.resourceBundle != null)
        {
            return this.resourceBundle.getString(key);
        }
        else
        {
            throw new NotInitializedException();
        }
    }
    
    /**
     * 
     * @return
     */
    public ResourceBundle getResourceBundle(){
    	return this.resourceBundle;
    }
}