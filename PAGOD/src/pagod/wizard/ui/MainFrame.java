/*
 * $Id: MainFrame.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pagod.common.control.adapters.ProcessTreeModel;
import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.common.ui.ContentViewerPane;
import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.ActionManager.KeyNotFoundException;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.Constants;
import pagod.wizard.control.actions.AbstractPagodAction;

/**
 * Fen�tre principale de l'application PAGOD
 * 
 * @author MoOky
 */
public class MainFrame extends JFrame
{
    /**
     * Panneaux du Nord de la fenetre
     */
    private JPanel northPanel;

    /**
     * Panneaux du centre de la fenetre
     */
    private JPanel centerPanel;

    /**
     * Panneaux du sud de la fenetre
     */
    private JPanel southPanel;

    /**
     * Panneaux de message
     */
    private MessagePanel messagePanel = null;

    /**
     * Panneaux des arbres
     */
    private ProcessPanel processPanel = null;

    /**
     * Panneaux de Lancement de l'activit�
     */
    private JPanel runActivityPanel = null;

    /**
     * Panneaux visualiseur de fichier de contenu
     */
    private ContentViewerPane ContentViewerPanel = null;

    /**
     * Panneaux des boutons
     */
    private ButtonPanel buttonPanel = null;

    /**
     * Construit la Fen�tre principale de PAGOD initialement invisible
     * 
     */
    public MainFrame()
    {
        super();
        // Definir le titre de la fen�tre
        this.setTitle(Constants.APPLICATION_SHORT_NAME);
        // D�finir l'ic�ne de la fen�tre
        this.setIconImage(ImagesManager.getInstance().getImageResource(
                "iconWizard.png"));
        // Positionner la fen�tre dans le coin en haut � gauche
        this.setLocation(0, 0);
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
        // D�finir la taille de la fen�tre et la taille de la fenetre maximise
        Rectangle screenSize = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int iWidth = screenSize.width / 3;
        int iHeight = screenSize.height;
        this.setSize(screenSize.width / 3, screenSize.height);
        this.setMaximizedBounds(new Rectangle(iWidth, iHeight));
        // Creation du Menu
        this.setJMenuBar(new MainFrameMenuBar());
        // creation des Panneaux
        this.northPanel = new JPanel();
        this.northPanel.setLayout(new BorderLayout());
        this.getContentPane().add(this.northPanel, BorderLayout.NORTH);
        this.centerPanel = new JPanel();
        this.centerPanel.setLayout(new BorderLayout());
        this.getContentPane().add(this.centerPanel, BorderLayout.CENTER);
        this.southPanel = new JPanel();
        this.southPanel.setLayout(new BorderLayout());
        this.getContentPane().add(this.southPanel, BorderLayout.SOUTH);

        // creation et initialisation du Panneaux de message
        this.messagePanel = new MessagePanel();
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "welcomeMessage"));
        this.northPanel.add(this.messagePanel);
        this.runActivityPanel = new JPanel(new BorderLayout());
        JPanel innerPane = new JPanel(new FlowLayout());
        innerPane.add(new JButton(ActionManager.getInstance().getAction(
                Constants.ACTION_RUN_ACTIVITY)));
        this.runActivityPanel.add(innerPane, BorderLayout.EAST);
        // Definir les Raccourcis Claviers pour soit sensible du moment que la
        // fenetre est active
        // actions Menu Fichier
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_OPEN)).configureRootPane(this.getRootPane(),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_QUIT)).configureRootPane(this.getRootPane(),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        // actions Menu Activit�
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_PREVIOUS)).configureRootPane(this
                .getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_NEXT)).configureRootPane(this.getRootPane(),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        ((AbstractPagodAction) ActionManager.getInstance().getAction(
                Constants.ACTION_TERMINATE)).configureRootPane(this
                .getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    /**
     * Presente le processus � utilisateur
     * 
     * @param process
     *            processus passer en parametre
     * @param fileName
     *            nom du fichier du processus
     * @param processName
     *            nom du processus
     */
    public void showProcess(ProcessTreeModel process, String fileName,
                            String processName)
    {
        // mettre le titre a jour
        String title = Constants.APPLICATION_SHORT_NAME + " - " + fileName;
        if (processName != null)
            title += " (" + processName + ") ";
        this.setTitle(title);
        // creer le treePanel
        this.processPanel = new ProcessPanel(process);
        this.showProcess();
    }

    /**
     * Presente le processus en cours � utilisateur
     * 
     */
    public void showProcess()
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        // mettre a jour le message
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "openedProcessMessage"));
        // on remplie les panneaux
        // au nord
        this.northPanel.setVisible(false);
        this.northPanel.add(this.messagePanel);
        this.northPanel.setVisible(true);
        // au centre
        this.centerPanel.setVisible(false);
        this.centerPanel.add(this.processPanel);
        this.centerPanel.setVisible(true);
        // au sud
        this.southPanel.setVisible(false);
        this.southPanel.add(this.runActivityPanel);
        this.southPanel.setVisible(true);

        this.setVisible(true);
        this.processPanel.requestFocus();
    }

    /**
     * Retourne l'activit� selectionn�
     * 
     * @return activit� selectionn�
     */
    public Activity getActivity()
    {
        return this.processPanel.getSelectedActivity();
    }

    /**
     * @param activityToPresent
     */
    public void presentActivity(Activity activityToPresent)
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        this.southPanel.removeAll();
        // mettre a jour le message
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "activityPresentationMessage"));
        // cr�er les panneaux
        this.ContentViewerPanel = new ContentViewerPane(activityToPresent);
        this.centerPanel.add(this.ContentViewerPanel);
        this.buttonPanel = new ButtonPanel();
        this.southPanel.add(this.buttonPanel);
        this.setVisible(true);
        this.ContentViewerPanel.requestFocus();
    }

    /**
     * @param activity
     * @throws NotInitializedException
     *             Si le gestionnaire de langues n'est pas initialis�
     * @throws MissingResourceException
     *             Si une cl� n'existe pas dans le fichier de langues
     * @throws pagod.utils.ImagesManager.NotInitializedException
     */
    public void showCheckList(Activity activity)
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        // mettre a jour le message
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "activityCheckListMessage"));
        // cr�er les panneaux
        this.centerPanel.add(new CheckPane(activity));
        this.setVisible(true); 
    }

    /**
     * @param stepToPresent
     * @param total
     * @param rang
     */
    public void presentStep(Step stepToPresent, int rang, int total)
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        // mettre a jour le message
        if (!stepToPresent.hasOutputProducts())
            this.messagePanel.setMessage(LanguagesManager.getInstance()
                    .getString("presentStepMessage"));
        else
            // on ajoute une phrase explicant qu'il faut cliquer sur suivant
            // pour cr�er les produits de cette �tape
            this.messagePanel.setMessage(LanguagesManager.getInstance()
                    .getString("presentStepMessage")
                    + " "
                    + LanguagesManager.getInstance().getString(
                            "infoOutputProduct"));

        // cr�er les panneaux
        this.centerPanel.add(new StepPanel(stepToPresent, rang, total));
        this.setVisible(true);
    }

    /**
     * @param ProductsToPresent
     */
    public void presentProducts(Collection<Product> ProductsToPresent)
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        // mettre a jour le message
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "presentProductsMessage"));
        // cr�er les panneaux
        ProductsPanel productsPanel = new ProductsPanel(ProductsToPresent);
        this.centerPanel.add(productsPanel);
        this.setVisible(true);
        // demande le focus
        productsPanel.requestFocus();
    }

    /**
     * reinitialise la fenetre
     * 
     */
    public void reinitialize()
    {
        // on netoye les panneaux
        this.centerPanel.removeAll();
        this.southPanel.removeAll();
        // mettre a jour le message
        this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
                "welcomeMessage"));
        this.setTitle(Constants.APPLICATION_SHORT_NAME);
        this.setVisible(true);
    }
}
