/*
 * $Id: ProcessOutputFileChooser.java,v 1.3 2006/03/03 16:01:17 themorpheus Exp $
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

import pagod.configurator.control.PreferencesManager;
import pagod.utils.LanguagesManager;


/**
 * Fenetre de selection de fichier de processus
 * 
 * @author MoOky
 */
public class ProcessOutputFileChooser extends SaveFileChooser
{

    /**
     * Constructeur de la fenetre de selection de fichier de processus
     * 
     */
    public ProcessOutputFileChooser()
    {
        super("pagod",LanguagesManager.getInstance().getString(
        "processFileChooserFilterPAGOD"));
        // initialisation du titre
        this.setDialogTitle(LanguagesManager.getInstance().getString(
                "processOutputFileChooserTitle"));
        // se pr?positionner dans le cheminInitial
        String initialPath = PreferencesManager.getInstance().getOutputDirectory();
        if (initialPath != null)
            this.setCurrentDirectory(new File(initialPath));
    }
}
