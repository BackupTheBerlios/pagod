/*
 * $Id: MainFrameMenuBar.java,v 1.9 2006/03/18 11:33:06 biniou Exp $
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
        
        JMenuItem miNewProject= new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_NEWPROJECT));
        mFile.add(miNewProject);
        
        JMenuItem miOpenProject= new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_OPENPROJECT));
        mFile.add(miOpenProject);
        
        JMenuItem miOpenProcess = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_OPENPROCESS));
        mFile.add(miOpenProcess);
        mFile.addSeparator();
        JMenuItem miCloseProject = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_CLOSEPROJECT));
        mFile.add(miCloseProject);
        mFile.addSeparator();
        
        JMenuItem miDeleteProject = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_DELETEPROJECT));
        mFile.add(miDeleteProject);
        mFile.addSeparator();
        
        JMenuItem miToolsSettings = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_TOOLSSETTINGS));
        mFile.add(miToolsSettings);
        
        // ajout d'une option du menu pour l'import de projet
        /* TODO ALEX A FAIRE AVEC FLO 
        mFile.addSeparator();
        JMenuItem miImportProject = new JMenuItem(ActionManager.getInstance()
                .getAction(Constants.ACTION_IMPORT_PROJECT));
        mFile.add(miImportProject);
        */
        mFile.addSeparator();
        JMenuItem miQuit = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_QUIT));
        mFile.add(miQuit);

        this.add(mFile);
        
        // Menu Itération
        MnemonicMenu mIteration = new MnemonicMenu(LanguagesManager.getInstance().getString(
        "iteration"));
        
        //export du temps
        JMenuItem miExport = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_EXPORT_TIME));
        mIteration.add(miExport);
        
        // passer a l'ité suivante
        JMenuItem miNextIteration = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_NEXT_ITERATION));
        mIteration.add(miNextIteration);
        
        // consulter les temps de l'ité courante
        JMenuItem miTimeCurrentIteration = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_TIME_CURRENT_ITERATION));
        mIteration.add(miTimeCurrentIteration);
        
        // consulter les temps pour toutes les ités
        JMenuItem miTimeAllIterations = new JMenuItem(ActionManager.getInstance().getAction(
                Constants.ACTION_TIME_ALL_ITERATIONS));
        mIteration.add(miTimeAllIterations);
        
        this.add(mIteration);
        
        // Menu Activité
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
