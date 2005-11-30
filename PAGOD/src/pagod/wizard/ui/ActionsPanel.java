/*
 * $Id: ActionsPanel.java,v 1.3 2005/11/30 09:38:07 cyberal82 Exp $
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Activity;
import pagod.common.model.Guidance;
import pagod.common.model.Product;
import pagod.wizard.control.adapters.GuidancesTreeModel;

/**
 * Affiche les actions suceptibles d'etre effectuer sur un produit. On a affiche
 * les guides disponibles pour l'activite, le role qui realise cette activite
 * ainsi que ceux sur les produits 
 * 
 * @author MoOky
 */
public class ActionsPanel extends JPanel
{
	/**
	 * Produit en cours
	 */
	private Product	productInProgress;
	
	/**
	 * Guides disponible sous forme de JTree
	 */
	private JTree	treeGuidances;
	
	/**
	 * L'activite en cours d'execution 
	 */
	private Activity activity;

	/**
	 * Constructeur
	 * 
	 * @param activity
	 *            l'activite qui est en cours d'affichage
	 */
	public ActionsPanel (Activity activity)
	{
		super();
		
		this.activity = activity;
	}

	/**
	 * Change le produit sur lequel l'utilisateur souhaite travailler
	 * 
	 * @param productInProgress
	 *            produit sur lequel on travaille
	 */
	public void setProduct (Product productInProgress)
	{
		this.productInProgress = productInProgress;
		this.treeGuidances = new JTree(new GuidancesTreeModel(this.activity,
				this.productInProgress));

		// on enleve ce que peut contenir le panneau
		this.removeAll();

		this.setLayout(new BorderLayout());

		// ajout de la JTree
		JScrollPane scroll = new JScrollPane(this.treeGuidances);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		this.add(scroll, BorderLayout.CENTER);

		// ne pas afficher le noeud racine
		this.treeGuidances.setRootVisible(false);

		// un seul noeud selectionnable a la fois
		this.treeGuidances.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// ajout du renderer qui affiche les icones ad hoc en face des labels
		this.treeGuidances.setCellRenderer(new GuidanceTreeCellRenderer());

		// deplier tous les noeuds
		this.expandAllNodes();

		// ajout de l'ecouteur de selection sur le JTree
		this.treeGuidances.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged (TreeSelectionEvent event)
			{

			}
		});

		this.treeGuidances.addMouseListener(new MouseAdapter()
		{
			/**
			 * Lancement de l'application permettant de visulaliser un guide si
			 * l'utilisateur a effectuer un doucle clique sur un guide
			 */
			public void mousePressed (MouseEvent e)
			{
				int iSelRow = ActionsPanel.this.treeGuidances
						.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = ActionsPanel.this.treeGuidances
						.getPathForLocation(e.getX(), e.getY());
				if (iSelRow != -1)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
							.getLastPathComponent();

					if (e.getClickCount() == 2
							&& node.getUserObject() instanceof Guidance)
					{
						ModelResourcesManager.getInstance().launchContentFile(
								(Guidance) node.getUserObject());
					}
				}
			}

			/**
			 * Change le rendu de la JTree pour qu'aucun guide ne soit en bleu
			 * car la souris ne survole plus la JTree
			 */
			public void mouseExited (MouseEvent e)
			{
				// changement de l'objet de rendu
				ActionsPanel.this.treeGuidances
						.setCellRenderer(new GuidanceTreeCellRenderer());

			}
		});

		// ajout d'un listener permettant de mettre en bleu un guide lorsque la
		// souris le survole
		this.treeGuidances.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseMoved (MouseEvent e)
			{
				int iSelRow = ActionsPanel.this.treeGuidances
						.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = ActionsPanel.this.treeGuidances
						.getPathForLocation(e.getX(), e.getY());

				if (iSelRow != -1)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
							.getLastPathComponent();

					if (node.getUserObject() instanceof Guidance)
					{
						// Changement de la couleur
						ActionsPanel.this.treeGuidances
								.setCellRenderer(new GuidanceTreeCellRenderer(
										(Guidance) node.getUserObject()));

					}
					if (node.getUserObject() instanceof String)
					{
						ActionsPanel.this.treeGuidances
								.setCellRenderer(new GuidanceTreeCellRenderer());
					}
				}
			}
		});

		// on indique combien de ligne de la JTree doivent etre visible
		this.treeGuidances.setVisibleRowCount(productInProgress
				.getGuidanceTypeCount()
				+ productInProgress.getGuidanceCount());
		// on defini la taille minimum de l'ActionPanel comme etant la taille
		// prefere de la JTree
		this.setMinimumSize(this.treeGuidances.getPreferredSize());

		this.revalidate();
	}

	/**
	 * d?plier tous les noeuds du treeGuidances
	 */
	private void expandAllNodes ()
	{
		int row = 0;
		while (row < this.treeGuidances.getRowCount())
		{
			this.treeGuidances.expandRow(row);
			row++;
		}
	}

	/**
	 * replier tous les noeuds du treeGuidances
	 */
	private void collapseAllNodes ()
	{
		int row = this.treeGuidances.getRowCount() - 1;
		while (row >= 0)
		{
			this.treeGuidances.collapseRow(row);
			row--;
		}
	}

	private class GuidanceTreeCellRenderer extends DefaultTreeCellRenderer
	{
		private Guidance	guidanceHover;

		/**
		 * Cree un nouveau rendu ou aucun guide n'est afficher en bleu (car
		 * aucun guide n'est survole par la souris)
		 */
		public GuidanceTreeCellRenderer ()
		{
			super();
			this.guidanceHover = null;
		}

		/**
		 * Cree un nouveau rendu ou le guide guidance est afficher en bleu (car
		 * il est survole par la souris)
		 * 
		 * @param guidance
		 *            est le guide a afficher en bleu car il est survole
		 */
		public GuidanceTreeCellRenderer (Guidance guidance)
		{
			super();

			this.guidanceHover = guidance;
		}

		/**
		 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
		 *      java.lang.Object, boolean, boolean, boolean, int, boolean)
		 */
		public Component getTreeCellRendererComponent (JTree tree,
				Object value, boolean sel, boolean expanded, boolean leaf,
				int row, boolean focus)
		{

			Component c = null;

			c = super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, focus);
			if (value instanceof DefaultMutableTreeNode)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				/*
				 * Color fc =
				 * GraphConstants.getForeground(node.getAttributes()); Color bc =
				 * GraphConstants.getBackground(node.getAttributes()); Font font =
				 * GraphConstants.getFont(node.getAttributes());
				 * setBackgroundNonSelectionColor(bc); setForeground(fc);
				 */

				// c'est un type de guide
				if (node.getUserObject() instanceof String)
				{
					((DefaultTreeCellRenderer) c).setText(((String) node
							.getUserObject()).toUpperCase());
				}
				if (node.getUserObject() instanceof Guidance)
				{
					// r?cup?rer l'ic?ne du guide
					this.setIcon(ModelResourcesManager.getInstance()
							.getSmallIcon((Guidance) node.getUserObject()));
					// Changement de la couleur si necessaire
					Guidance guidance = (Guidance) node.getUserObject();
					if (guidance == this.guidanceHover)
					{
						this.setForeground(Color.blue);
					}
				}
				// c'est le label explicant qu'il n'y a pas de guide
				// disponible
				if (node.getUserObject() instanceof JLabel)
				{
					return ((JLabel) node.getUserObject());
				}
				if (this.selected && leaf)
				{
					Font f = this.getFont();
					Font f2 = f.deriveFont(Font.ITALIC);
					this.setFont(f2);
				}
				else
				{
					Font f = this.getFont();
					Font f2 = f.deriveFont(Font.PLAIN);
					this.setFont(f2);
				}
			}
			return c;
		}
	}
}
