/*
 * $Id: ProductsPanel.java,v 1.7 2006/02/23 18:26:37 psyko Exp $
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

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import pagod.common.model.Activity;
import pagod.common.model.Product;

/**
 * Panneaux pr?sentant une liste de produit et proposant des actions li? ? ces
 * produits (voir guides, cr?e produit)
 * 
 * @author MoOky
 * 
 */
public class ProductsPanel extends JSplitPane
{
	/**
	 * Panneau de la liste des produits
	 */
	private ListProductsPanel	listProductsPanel;

	/**
	 * Panneau des actions a effectu? sur les produits
	 */
	private ActionsPanel		actionsPanel;

	/**
	 * Constructeur
	 * 
	 * @param activity
	 *            l'activite dont on veut presenter les produits passes en
	 *            parametre
	 * @param productsToPresent
	 *            liste de produit ? pr?sent?e
	 */
	public ProductsPanel (Activity activity,
			Collection<Product> productsToPresent)
	{
		// creation du splitpane
		super();
		this.setOneTouchExpandable(true);
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.setResizeWeight(1.0);

		// creation du panneaux des actions positionner sur le premier produit
		// l'AtioPannel affiche les guides disponibles pour l'activite, le role
		// qui realise cette activite
		// ainsi que ceux sur les produits
		this.actionsPanel = new ActionsPanel(activity);

		// creation du panneaux listant les produits
		this.listProductsPanel = new ListProductsPanel(productsToPresent)
		{
			public void onSelection (Product selectedProduct)
			{
				ProductsPanel.this.actionsPanel.setProduct(selectedProduct);
			}
		};

		// remplissage
		// en haut --> le Panneaux listant les produits
		JScrollPane scrollPane = new JScrollPane(this.listProductsPanel);
		this.add(scrollPane, JSplitPane.TOP);
		this.add(this.actionsPanel, JSplitPane.BOTTOM);
		
		// pour definir la taille prefere et minimal du scrollPane
		Dimension dim = new Dimension((int) this.listProductsPanel
				.getMinimumSize().getWidth(), ((int) this.listProductsPanel
				.getMinimumSize().getHeight()) + 7);
		
		
		scrollPane.setMinimumSize(dim);
		scrollPane.setPreferredSize(dim);
	}

	/**
	 * Surcharge : donne le focus a la liste
	 */
	public void requestFocus ()
	{
		this.listProductsPanel.requestFocus();
	}
}
