/*
 * $Id: ProductsPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.util.Collection;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import pagod.common.model.Product;


/**
 * Panneaux pr?sentant une liste de produit et proposant des actions li? ? ces produits
 * (voir guides, cr?e produit)
 * @author MoOky
 *
 */
public class ProductsPanel extends JSplitPane
{
    /**
     * Panneau de la liste des produits
     */
    private ListProductsPanel listProductsPanel;
    
    /**
     * Panneau des actions a effectu? sur les produits
     */
    private ActionsPanel actionsPanel;
    
    /**
     * Constructeur
     * @param productsToPresent liste de produit ? pr?sent?e
     */
    public ProductsPanel(Collection<Product> productsToPresent)
    {
        // creation du splitpane
        super();
        this.setOneTouchExpandable(true);
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.setResizeWeight(0.5);
        // creation du panneaux des actions positionner sur le premier produit
        this.actionsPanel = new ActionsPanel();
        // creation du panneaux listant les produits
        this.listProductsPanel = new ListProductsPanel(productsToPresent){

            public void onSelection(Product selectedProduct)
            {
                ProductsPanel.this.actionsPanel.setProduct(selectedProduct);
            }};
        // remplissage
        // en haut --> le Panneaux listant les produits
        this.add(new JScrollPane(this.listProductsPanel), JSplitPane.TOP);
        this.add(this.actionsPanel, JSplitPane.BOTTOM);
    }
    
    
    /** 
     * Surcharge : donne le focus a la liste
     */
    public void requestFocus()
    {
        this.listProductsPanel.requestFocus();
    }
}
