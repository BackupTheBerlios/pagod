/*
 * $Id: MainFrame.java,v 1.2 2006/02/16 22:22:22 themorpheus Exp $
 *
 * SPWIZ - Spem Wizard
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * PAGOD- Personal assistant for group of development
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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.ActionManager.KeyNotFoundException;
import pagod.configurator.control.actions.AbstractPagodAction;
import pagod.configurator.control.Constants;
import pagod.common.model.Process;

/**
 * Fenêtre principale de l'application PAGOD configurator
 * 
 * @author MoOky
 */
public class MainFrame extends JFrame
{
    /**
     * Panneaux de création des étapes
     */
    private StepsPanel stepsPanel; 

    /**
     * Panneaux associations des produits
     */
    private ToolsAssociationPanel toolsAssociationPanel;

    /**
     * Panneau d'activation/désactivation de rôles
     */
    private RolesPanel rolesPanel;
    
    /**
     * Les onglets
     */
    private JTabbedPane tabs;

    /**
     * Construit la Fenêtre principale de PAGOD configurator initialement
     * invisible
     * 
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws KeyNotFoundException
     *             Si il n'y a pas de chain correspondante à la clé donné au
     *             LanguagesManager.
     * @throws ImagesManager.NotInitializedException
     *             Si l'iconManager n'a pas été initialisé.
     * @see java.awt.GraphicsEnvironment#isHeadless()
     * @see java.awt.Component#setVisible(boolean)
     * @see pagod.utils.LanguagesManager#setResourceFile(String, Locale)
     * @see pagod.utils.ImagesManager#setImagePath(String)
     */
    public MainFrame() 
    {
        super();
        // Definir le titre de la fenêtre
        this.setTitle(Constants.APPLICATION_SHORT_NAME);
        // Définir l'icône de la fenêtre
        this.setIconImage(ImagesManager.getInstance().getImageResource(
                "iconConfigurator.png"));
        // Faire un traitement particulier lors de la fermeture de l'application
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);     
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent arg0)
            {
                try
                {
                    ActionManager.getInstance()
                            .getAction(Constants.ACTION_QUIT).actionPerformed(
                                    null);
                }
                catch (KeyNotFoundException e)
                {
                }
            }
        });
        // Creation du Menu
        this.setJMenuBar(new MainFrameMenuBar());
        // Definir les Raccourcis Claviers pour soit sensible du moment que la
        // fenetre est active
        // actions Menu Fichier
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_OPEN)).configureRootPane(this.getRootPane(),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_QUIT)).configureRootPane(this.getRootPane(),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Presente le processus à utilisateur
     * 
     * @param process
     *            processus passer en parametre
     * @param fileName
     *            nom du fichier du processus
     */
    public void presentProcess(Process process, String fileName)
    {
        // mettre le titre a jour
        String title = Constants.APPLICATION_SHORT_NAME + " - " + fileName;
        if (process.getName() != null)
            title += " (" + process.getName() + ") ";
        this.setTitle(title);
        // creer les panneaux
        this.stepsPanel = new StepsPanel(process);
        this.toolsAssociationPanel = new ToolsAssociationPanel(process);
        this.rolesPanel = new RolesPanel(process);
        this.tabs = new JTabbedPane();
        this.tabs.addTab(LanguagesManager.getInstance().getString("stepsTab"),
                this.stepsPanel);
        this.tabs.addTab(LanguagesManager.getInstance().getString("toolsTab"),
                this.toolsAssociationPanel);
        this.tabs.addTab(LanguagesManager.getInstance().getString("rolesTab"),
                this.rolesPanel);
        this.getContentPane().removeAll();
        this.getContentPane().add(this.tabs);
        this.setVisible(true);
    }

}
