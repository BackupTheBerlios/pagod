/*
 * $Id: MainFrameMenuBar.java,v 1.3 2005/11/30 12:21:17 cyberal82 Exp $
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

package pagod.wizard.ui;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import pagod.common.ui.MnemonicMenu;
import pagod.utils.ActionManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.Constants;

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
     */
    public MainFrameMenuBar()
    {
        super();
        // Menu Fichier
        MnemonicMenu mFile = new MnemonicMenu(LanguagesManager.getInstance()
                .getString("file"));
        
        JMenuItem miOpenProject= new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_OPENPROJECT));
        mFile.add(miOpenProject);
        
        JMenuItem miOpenProcess = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_OPENPROCESS));
        mFile.add(miOpenProcess);
        JMenuItem miCloseProcess = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_CLOSEPROCESS));
        mFile.add(miCloseProcess);

        JMenuItem miToolsSettings = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_TOOLSSETTINGS));
        mFile.add(miToolsSettings);

        JMenuItem miQuit = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_QUIT));
        mFile.add(miQuit);

        this.add(mFile);

        // Menu Activit?
        MnemonicMenu mActivity = new MnemonicMenu(LanguagesManager.getInstance().getString(
                "activity"));

        JMenuItem miRun = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_RUN_ACTIVITY));
        mActivity.add(miRun);

        JMenuItem miNext = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_NEXT));
        mActivity.add(miNext);

        JMenuItem miPrevious = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_PREVIOUS));
        mActivity.add(miPrevious);

        JMenuItem miTerminate = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_TERMINATE));
        mActivity.add(miTerminate);

        this.add(mActivity);

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
