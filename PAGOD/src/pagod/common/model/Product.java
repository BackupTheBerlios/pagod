/*
 * $Id: Product.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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

package pagod.common.model;

import java.net.URL;

/**
 * Produit générer ou utilisé dans le processus
 * 
 * @author MoOky
 */
public class Product extends ProcessElement
{
    /**
     * Outil utilisé pour éditer le produit
     */
    private Tool 		editor = null;
    
    private String 		description = "";

    /**
     * Constructeur complet d'un produit
     * 
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     * @param description 
     * @param editor
     */
    public Product(String id, String name, URL fileURL, URL iconURL, String description, Tool editor)
    {
        super(id, name, fileURL, iconURL);
        this.description = description;
        this.setEditor(editor);
    }

    /**
     * Retourne l'attribut editor
     * 
     * @return editor.
     */
    public Tool getEditor()
    {
        return this.editor;
    }

    /**
     * Initialise l'attribut editor
     * 
     * @param editor
     *            la valeur a attribuer.
     */
    public void setEditor(Tool editor)
    {
        if (this.editor == editor)
            return;

        final Tool oldEditor = this.editor;

        // blocage de la récursivité
        this.editor = null;

        // suppression de l'ancienne liaison si existante
        if (oldEditor != null)
            oldEditor.removeProduct(this);

        // création de la nouvelle liaison
        if (editor != null)
        {
            this.editor = editor;
            editor.addProduct(this);
        }
    }
}
