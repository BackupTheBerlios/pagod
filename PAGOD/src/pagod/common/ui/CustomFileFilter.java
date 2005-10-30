/*
 * $Id: CustomFileFilter.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.common.ui;

import java.io.File;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

/**
 * Cette classe permet d'ajouter un filtre à un FileChooser de façon plus simple
 * qu'avec le "FileFilter" de base
 * 
 * @author Benjamin
 */
public class CustomFileFilter extends FileFilter
{
    /**
     * les extensions autorisées pour ce filtre (ex: "tif", "dpe", ...) /!\ de
     * ne pas faire figurer le '.' avant l'extension
     */
    private Vector<String> vecAuthorizedExtensions;

    /**
     * la description à afficher à côté des extensions autorisées par le filtre
     */
    private String sFormatDescription;

    /**
     * @param extensions
     *            chaînes des extensions autorisées (ex : "tif", "dpe", ...)
     * @param desc
     *            description du filtre (ex: "Image TIFF", ...)
     */
    public CustomFileFilter(Vector<String> extensions, String desc)
    {
        this.vecAuthorizedExtensions = extensions;
        this.sFormatDescription = desc;
    }

    /**
     * Renvoie true si f "passe" le filtre
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f)
    {
        // accepter les dossiers...
        if (f.isDirectory())
        {
            return true;
        }

        // ...et les fichiers qui ont un des suffixes autorisés
        String extension = null;
        String fileName = f.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1)
        {
            extension = fileName.substring(i + 1).toLowerCase();
        }

        return fileName != null
                && this.vecAuthorizedExtensions.contains(extension);
    }

    /**
     * Renvoie la description du filtre
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return this.sFormatDescription;
    }
}
