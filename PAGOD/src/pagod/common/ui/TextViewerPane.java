/*
 * $Id: TextViewerPane.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import pagod.utils.ExceptionManager;
import pagod.utils.LanguagesManager;

/**
 * Panneau d'affichage des fichiers text, HTML ou rtf
 * 
 * @author Cédric
 */
public class TextViewerPane extends JScrollPane
{
    private JEditorPane contentPane;

    /**
     * Constructeur du visualisateur de contenu
     * 
//     * @param styleSheet
//     *            Feuille de style à utiliser
     */
//    public TextViewerPane(StyleSheet styleSheet)
    public TextViewerPane()
    {
        super();
        this.contentPane = new JEditorPane();
        this.contentPane.setEditable(false);
        this.add(this.contentPane);
        this.setViewportView(this.contentPane);
//        // définition de la feuille de style
//        HTMLEditorKit kit = (HTMLEditorKit) this.contentPane
//                .getEditorKitForContentType("text/html");
//        kit.setStyleSheet(styleSheet);
    }

    /**
     * Affiche le fichier spécifié si l'on passe null, alors on affiche ""
     * 
     * @param file
     *            Fichier de contenu
     */
    public void showContentFile(URL file)
    {
        if (file != null)
        {
            try
            {
                this.contentPane.setPage(file);
            }
            catch (IOException e)
            {
                this.setMessage(LanguagesManager.getInstance().getString(
                        "cantReadFile"));
            }
        }
        else
        {
            this.setMessage("");
        }
    }

    /**
     * Affiche le texte passer en parametre
     * 
     * @param message
     *            message à afficher
     */
    public void setMessage(String message)
    {
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        HTMLDocument htmlDocument = (HTMLDocument) htmlEditorKit
                .createDefaultDocument();
        try
        {
            htmlDocument.insertString(0, message, null);
        }
        catch (BadLocationException e)
        {
            // TODO arrive jamais comme on insere a la possition 0
            ExceptionManager.getInstance().manage(e);
        }
        this.contentPane.setDocument(htmlDocument);
    }

    /**
     * surcharge pour donné le focus à editorPane
     */
    public void requestFocus()
    {
        this.contentPane.requestFocus();
    }
}
