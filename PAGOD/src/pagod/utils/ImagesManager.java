/*
 * $Id: ImagesManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Gestionnaire d'images implémenté comme un singleton Cette Classe permet
 * d'accéder facilement à une image à partir de son nom de fichier
 * 
 * @author MoOky
 */
public class ImagesManager
{
    /**
     * Exception levée si le gestionnaire d'images n'est pas initialisé
     */
    public class NotInitializedException extends RuntimeException
    {
    }

    /**
     * Instance du gestionnaire d'images
     */
    private static ImagesManager imInstance = null;

    /**
     * Chemin du repertoire contenant les images à gérer
     */
    private String iconsPath = null;

    /**
     * Cache des Images
     */
    private HashMap<String, Image> imagesMap = new HashMap<String, Image>();

    /**
     * Constructeur privé du gestionnaire d'images (implémentation d'un
     * singleton)
     */
    private ImagesManager()
    {
    }

    /**
     * retourne l'instance du gestionnaire d'images
     * 
     * @return instance du gestionnaire d'images
     */
    static public ImagesManager getInstance()
    {
        if (ImagesManager.imInstance == null)
        {
            ImagesManager.imInstance = new ImagesManager();
        }
        return ImagesManager.imInstance;
    }

    /**
     * Initialise le chemin ou sont stockés les images
     * 
     * @param path
     */
    public void setImagePath(String path)
    {
        if (path.endsWith("/"))
        {
            this.iconsPath = path;
        }
        else
        {
            this.iconsPath = path + "/";
        }
    }

    /**
     * Retourne l'icone de taille 16x16 dont le nom du fichier est passé en
     * paramettre
     * 
     * @param resourceName
     *            le nom du fichier contenant l'icone
     * @return l'icone
     * @throws NotInitializedException
     *             Si le chemin des Images n'a pas été définis.
     * @see ImagesManager#setImagePath
     */
    public Icon getSmallIcon(String resourceName)
    {
        if (this.iconsPath == null)
        {
            throw new NotInitializedException();
        }
        return new ImageIcon(this.getImageResource(resourceName)
                .getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }

    /**
     * Retourne l'icone dont le nom du fichier est passé en paramettre
     * 
     * @param resourceName
     *            le nom du fichier contenant l'icone
     * @return l'icone
     * @throws NotInitializedException
     *             Si le chemin des Images n'a pas été définis.
     */
    public Icon getIcon(String resourceName)
    {
        if (this.iconsPath == null)
        {
            throw new NotInitializedException();
        }
        return new ImageIcon(this.getImageResource(resourceName));
    }

    /**
     * Retourne l'image dont le nom du fichier est passé en paramettre
     * 
     * @param resourceName
     *            le nom de fichier contenant l'image
     * @return l'image

     */
    public Image getImageResource(String resourceName)
    {
        String fileName = resourceName;
        // si le nom de fichier commence par / alors on supprime le /
        if (fileName.startsWith("/"))
        {
            fileName = fileName.substring(1);
        }
        // on recherche si il est dans le cache
        Image img = null;
        if ((img = this.imagesMap.get(fileName)) == null)
        {
            // si il n'est pas dans le cache
            // ouvrir l'image
            InputStream image;
            try
            {
                if ((image = ClassLoader.getSystemResourceAsStream(this.iconsPath
                        + fileName)) == null)
                    throw new FileNotFoundException();
                img = ImageIO.read(image);
                image.close();
            }
            catch(IOException e)
            {
                img = new BufferedImage(1, 1,BufferedImage.TYPE_BYTE_BINARY);
            }
            // l'ajouter dans le cache
            this.imagesMap.put(fileName, img);
        }
        return img;
    }
}
