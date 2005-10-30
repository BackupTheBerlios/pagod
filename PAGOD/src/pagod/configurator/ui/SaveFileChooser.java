/*
 * $Id: SaveFileChooser.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.configurator.ui;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

import pagod.common.ui.CustomFileFilter;


/**
 * Fenetre de sauvegarde de fichier
 * 
 * @author MoOky
 */
public class SaveFileChooser extends JFileChooser
{

    private  String extensionWithoutPoint;

    private  String extension;

    /**
     * Constructeur de la fenetre de selection de fichier pour la sauvegarde
     * @param extensionWithoutPoint 
     * @param description 
     * 
     */
    public SaveFileChooser(String extensionWithoutPoint, String description )
    {
        super();
        // initialisation des extension
        this.extensionWithoutPoint = extensionWithoutPoint;
        this.extension = "." + this.extensionWithoutPoint;
        // interdit la selection multiple
        this.setMultiSelectionEnabled(false);        
         // mettre en place le filtre
        Vector<String> authorizedExtensions = new Vector<String>();
        authorizedExtensions = new Vector<String>();
        authorizedExtensions.add(this.extensionWithoutPoint);
        CustomFileFilter filter = new CustomFileFilter(
                authorizedExtensions, description);
        this.addChoosableFileFilter(filter);
    }

    /**
     * @see javax.swing.JFileChooser#getSelectedFile()
     */
    public File getSelectedFile()
    {
        File selectedFile = super.getSelectedFile();
        if (selectedFile != null)
        {
            // si le fichier selectionner n'existe pas
            if (!selectedFile.exists())
            {

                int extensionSize = this.extension.length();
                // recupere le nom du fichier
                String fileName = selectedFile.getName();
                if (fileName.length() <= extensionSize)
                    selectedFile = new File(selectedFile.getAbsolutePath()
                            + this.extension);
                else
                {
                    // recuperer les x derniers caracteres (x etant la taille de extension)
                    String lastcharacters = fileName.substring(fileName
                            .length()
                            - extensionSize, fileName.length());
                    // si ce n'est pas egal a l'extension alors on le rajoute
                    if (!lastcharacters.toLowerCase().equals(
                            this.extension))
                    {
                        selectedFile = new File(selectedFile.getAbsolutePath()
                                + this.extension);
                    }
                    else
                    {
                        // si c'est egal alors on le mets en minuscule
                        String path = selectedFile.getAbsolutePath();
                        String beforeExtension = path.substring(0, path
                                .length()
                                - extensionSize);
                        selectedFile = new File(beforeExtension
                                + this.extension);
                    }
                }
            }
        }
        return selectedFile;
    }
}

