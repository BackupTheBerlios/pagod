/*
 * $Id: GuidancesTreeModel.java,v 1.3 2006/02/02 13:49:43 cyberal82 Exp $
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

package pagod.wizard.control.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pagod.common.model.Activity;
import pagod.common.model.Guidance;
import pagod.common.model.Product;
import pagod.utils.LanguagesManager;

/**
 * Classe permettant d'adapter les guides d'un produit donn? pour en faire le
 * modele d'un JTree
 * 
 * @author Alexandre Bes
 */
public class GuidancesTreeModel extends DefaultTreeModel
{

	/**
	 * Produit dont on veux adapter les guides pour en faire un JTree
	 */
	private Product		product;

	/**
	 * 
	 */
	private Activity	activity;

	/**
	 * Constructeur GuidancesTreeModel a partir d'un produit donne
	 * 
	 * @param activity
	 *            l'activite qui est en cours d'execution
	 * 
	 * @param product
	 *            produit dont on veux adapter les guides pour en faire un JTree
	 */
	public GuidancesTreeModel (Activity activity, Product product)
	{
		super(null);

		this.product = product;
		this.activity = activity;

		this.initGuidancesTreeModel();
	}

	/**
	 * 
	 * 
	 */
	private void initGuidancesTreeModel ()
	{
		this.root = new DefaultMutableTreeNode("racine");

		// on recupere tous les guides disponibles pour ce produit
		Collection<Guidance> arrGuidances = new ArrayList<Guidance> (this.product.getGuidances());

		// s'il l'activite possede des guides on les ajoute a la liste des
		// guides a afficher
		if (this.activity.getGuidanceCount() > 0) arrGuidances
				.addAll(this.activity.getGuidances());

		// s'il le role associe a l'activite possede des guides on les ajoute a la liste des
		// guides a afficher
		if (this.activity.getRole().getGuidanceCount() > 0) arrGuidances
				.addAll(this.activity.getRole().getGuidances());

		// pour tous les guides
		for (Guidance guidance : arrGuidances)
		{
			String sTypeGuidance = guidance.getType();

			Enumeration e = this.root.children();
			boolean isAdded = false;

			// tant que le guide n'a pas ete ajoute et qu'il reste des types de
			// guide possible dans le model
			while (!isAdded && e.hasMoreElements())
			{
				DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode) e
						.nextElement();

				// si ce type de guide existe deja
				if (((String) typeNode.getUserObject()).equals(sTypeGuidance))
				{
					// creation et ajout du guide dans le type de guide
					// correspondant
					typeNode.add(new DefaultMutableTreeNode(guidance));

					isAdded = true;
				}
			}

			// si ce type de guide n'existe pas
			if (!isAdded)
			{
				// creation du nouveau type de guide
				DefaultMutableTreeNode newTypeGuidance = new DefaultMutableTreeNode(
						sTypeGuidance);

				// ajout du guide dans les guides de meme type
				newTypeGuidance.add(new DefaultMutableTreeNode(guidance));

				// ajout du nouveau type de guide a la racine
				((DefaultMutableTreeNode) this.root).add(newTypeGuidance);
			}
		}

		// s'il n'y a pas de guide defini pour ce produit
		if (this.root.getChildCount() == 0)
		{
			// ajout d'un JLabel explicant qu'aucun guide n'a ete defini
			// pour ce produit
			((DefaultMutableTreeNode) this.root)
					.add(new DefaultMutableTreeNode(new JLabel(LanguagesManager
							.getInstance().getString(
									"ActionsPanelLabelNoGuideAvailable"))));
		}

	}
}
