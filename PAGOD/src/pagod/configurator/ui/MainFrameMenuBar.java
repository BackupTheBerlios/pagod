/*
 * $Id: MainFrameMenuBar.java,v 1.2 2006/03/02 22:25:53 themorpheus Exp $
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

import java.util.Locale;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import pagod.common.ui.MnemonicMenu;
import pagod.configurator.control.Constants;
import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.ActionManager.KeyNotFoundException;

/**
 * Barre de Menu de la Fenetre principale de l'application
 * 
 * @author MoOky
 */
public class MainFrameMenuBar extends JMenuBar
{
    /**
     * Constructeur de la barre de Menu de la fenetre principale
     * 
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws KeyNotFoundException 
     *             Si il n'y a pas de chain correspondante à la clé donné au
     *             LanguagesManager.
     * @throws ImagesManager.NotInitializedException
     *             Si l'iconManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     * @see ImagesManager#setImagePath(String)
     */
    public MainFrameMenuBar()
    {
        super();
        // Menu Fichier
        MnemonicMenu mFile = new MnemonicMenu(LanguagesManager.getInstance()
                .getString("file"));

        JMenuItem miOpen = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_OPEN));
        mFile.add(miOpen);
        
        JMenuItem miSave = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_SAVE));
        mFile.add(miSave);
        
        JMenuItem miSaveAs = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_SAVE_AS));
        mFile.add(miSaveAs);
        
        JMenuItem miExport = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_EXPORT));
        mFile.add(miExport);
        
        JMenuItem miQuit = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_QUIT));
        mFile.add(miQuit);

        this.add(mFile);

        // Menu Aide
        MnemonicMenu mHelp = new MnemonicMenu(LanguagesManager.getInstance().getString("?"));

        JMenuItem miPreferences = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_PREFERENCES));
        mHelp.add(miPreferences);

        JMenuItem miAbout = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_ABOUT));

        mHelp.add(miAbout);

        this.add(mHelp);
    }
}
