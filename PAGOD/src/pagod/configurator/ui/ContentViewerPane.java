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

package pagod.configurator.ui;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Panneau d'affichage des fichiers de contenus HTML
 * 
 * @author Cédric
 */
public class ContentViewerPane extends JScrollPane
{
    JEditorPane contentPane; 

    /**
     * Constructeur du visualisateur de contenu
     */
    public ContentViewerPane()
    {
        super();
        this.contentPane = new JEditorPane();
        this.contentPane.setEditable(false);
        this.add(this.contentPane);
        this.setViewportView(this.contentPane);
    }

    /**
     * Constructeur du visualisateur de contenu prenant le fichier à afficher en
     * paramètre
     * 
     * @param file
     *            Fichier de contenu
     * @throws IOException
     *             Fichier non trouvé ou pb E/S sur le fichier
     */
    public ContentViewerPane(URL file) throws IOException
    {
        super();

        this.contentPane = new JEditorPane(file);
        this.contentPane.setEditable(false);

        this.add(this.contentPane);
        this.setViewportView(this.contentPane);
    }

    /**
     * Affiche le fichier spécifié si l'on passe null, alors on affiche "pas de
     * fichier de contenu"
     * 
     * @param file
     *            Fichier de contenu
     * @throws IOException
     *             file n'a pas été trouvé ou n'est pas accessible
     * @throws NotInitializedException
     * @throws BadLocationException
     * @throws MissingResourceException
     */
    public void showContentFile(URL file) throws IOException,
                                            MissingResourceException,
                                            BadLocationException,
                                            NotInitializedException
    {
        if (file != null)
        {
            this.contentPane.setPage(file);
        }
        else
        {
            HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
            HTMLDocument htmlDocument = (HTMLDocument) htmlEditorKit
                    .createDefaultDocument();
            htmlDocument.insertString(0, LanguagesManager.getInstance()
                    .getString("noContentFile"), null);
            this.contentPane.setDocument(htmlDocument);
        }
    }
}
