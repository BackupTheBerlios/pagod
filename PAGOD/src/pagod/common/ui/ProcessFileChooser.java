/*
 * $Id: ProcessFileChooser.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.JFileChooser;

import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Fenetre de selection de fichier de processus
 * 
 * @author MoOky
 */
public class ProcessFileChooser extends JFileChooser
{
    /**
     * Constructeur de la fenetre de selection de fichier de processus
     * 
     * @throws NotInitializedException
     *             Si le gestionnaire de langues n'est pas initialisé
     * @throws MissingResourceException
     *             Si une clé n'existe pas dans le fichier de langues
     */
    public ProcessFileChooser()
    {
        super();
        // initialisation du titre
        this.setDialogTitle(LanguagesManager.getInstance().getString(
                "processFileChooserTitle"));
        // interdit la selection multiple
        this.setMultiSelectionEnabled(false);
        // mettre en place le filtre (dpe)
        Vector<String> authorizedExtensions = new Vector<String>();
        
        // processus dpc
        authorizedExtensions.add("dpc");
        CustomFileFilter dpcOrPagodFilter = new CustomFileFilter(authorizedExtensions,
                LanguagesManager.getInstance().getString(
                        "processFileChooserFilterDPC"));
        this.addChoosableFileFilter(dpcOrPagodFilter);
        
        // processus PAGOD
        authorizedExtensions = new Vector<String>();
        authorizedExtensions.add("pagod");
        CustomFileFilter pagodFilter = new CustomFileFilter(authorizedExtensions,
                LanguagesManager.getInstance().getString(
                        "processFileChooserFilterPAGOD"));
        this.addChoosableFileFilter(pagodFilter);

        // processus dpc ou PAGOD
        authorizedExtensions = new Vector<String>();
        authorizedExtensions.add("dpc");
        authorizedExtensions.add("pagod");
        CustomFileFilter dpcFilter = new CustomFileFilter(authorizedExtensions,
                LanguagesManager.getInstance().getString(
                        "processFileChooserFilterDPCorPAGOD"));
        this.addChoosableFileFilter(dpcFilter);
    }
}
