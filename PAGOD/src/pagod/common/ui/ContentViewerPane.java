/*
 * $Id: ContentViewerPane.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Activity;
import pagod.common.model.ProcessComponent;
import pagod.common.model.ProcessElement;
import pagod.common.model.WorkDefinition;
import pagod.utils.LanguagesManager;

/**
 * Panneau d'affichage des fichiers de contenus HTML
 * 
 * @author Cédric
 */
public class ContentViewerPane extends JPanel
{
    /**
     * Bouton de lancement du fichier de contenu
     */
    private JButton viewerLauncherButton;

    /**
     * Panneaux contenant le bouton
     */
    private JScrollPane buttonScrollPane;

    /**
     * panneaux de presentation des fichier html/txt
     */
    private TextViewerPane contentPane;

    /**
     * JComponent courant
     */
    private JComponent currentComponent;

    /**
     * Element a présenter
     */
    private ProcessElement elementToPresent;

    /**
     * Constructeur du visualisateur de contenu
     */
    public ContentViewerPane()
    {
        super();
        this.setLayout(new BorderLayout());
//        this.contentPane = new TextViewerPane(ProcessElement.getStyleSheet());
        this.contentPane = new TextViewerPane();
        this.viewerLauncherButton = new JButton(LanguagesManager.getInstance()
                .getString("seeFileOfContents"));
        this.viewerLauncherButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent arg0)
            {
                ModelResourcesManager.getInstance().launchContentFile(
                        ContentViewerPane.this.elementToPresent);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(this.viewerLauncherButton);
        this.buttonScrollPane = new JScrollPane();
        this.buttonScrollPane.add(buttonPanel);
        this.buttonScrollPane.setViewportView(buttonPanel);
    }

    /**
     * Constructeur du visualisateur de contenu
     * 
     * @param elementToPresent
     *            element de processus à présenté
     */
    public ContentViewerPane(ProcessElement elementToPresent)
    {
        this();
        this.setProcessElement(elementToPresent);
    }

    /**
     * Met a jout l'élément de processus à presenter
     * 
     * @param elementToPresent
     *            element de processus a presenter
     */
    public void setProcessElement(ProcessElement elementToPresent)
    {
        this.elementToPresent = elementToPresent;
        URL fileToPresent = this.elementToPresent.getFileURL();
        if (fileToPresent == null)
        {
            if (this.elementToPresent instanceof Activity)
                this.contentPane.setMessage(LanguagesManager.getInstance()
                        .getString("noContentActivity"));
            else if (this.elementToPresent instanceof ProcessComponent)
                this.contentPane.setMessage(LanguagesManager.getInstance()
                        .getString("noContentComponent"));
            else if (this.elementToPresent instanceof WorkDefinition)
                this.contentPane.setMessage(LanguagesManager.getInstance()
                        .getString("noContentWorkDef"));
            else
                this.contentPane.setMessage("");
            this.currentComponent = this.contentPane;
        }
        else
        {
            String path = fileToPresent.toString();
            if (path.endsWith(".html") || path.endsWith(".htm")
                    || path.endsWith(".txt"))
            {
                this.contentPane.showContentFile(fileToPresent);
                this.currentComponent = this.contentPane;
            }
            else
            {
                this.currentComponent = this.buttonScrollPane;
            }
        }
        this.removeAll();
        this.add(this.currentComponent);
        this.setVisible(false);
        this.setVisible(true);
    }

    /**
     * surcharge pour donné le focus au composant courant
     */
    public void requestFocus()
    {
        if (this.currentComponent == this.buttonScrollPane)
            this.viewerLauncherButton.requestFocus();
        else
            this.currentComponent.requestFocus();
    }
}
