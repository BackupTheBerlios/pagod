/*
 * $Id: ListProductsPanel.java,v 1.7 2006/02/23 18:26:37 psyko Exp $
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Product;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;

/**
 * Presente une liste de produits
 * 
 * @author MoOky
 * 
 */
public abstract class ListProductsPanel extends JPanel
{
	private JList	productsList;

	/**
	 * Produit selectionne
	 */
	private Product	theSelectedProduct;

	/**
	 * label pour pr?venir
	 */
	private JLabel	message;

	/**
	 * Bouton permettant de lancer la creation d'un produit
	 */
	private JButton	bpLauncherProduct;

	/**
	 * Bouton permettant de lancer la modification d'un produit
	 */
	private JButton	bpUpdaterProduct;

	/**
	 * Constructeur
	 * 
	 * @param productsToPresent
	 *            liste de produit ? pr?sent?e
	 */
	public ListProductsPanel (Collection<Product> productsToPresent)
	{
		super();
		
		// * Mise en forme * //
		// mettre le fond du panneaux en blanc
		this.setBackground(Color.WHITE);
		
		// Definir le layout
		BorderLayout bl = new BorderLayout();
		bl.setHgap(7);
		bl.setVgap(7);
		this.setLayout(bl);
		this.add(Box.createGlue(), BorderLayout.NORTH);
		this.add(Box.createGlue(), BorderLayout.WEST);
		this.add(Box.createGlue(), BorderLayout.EAST);
		
		// this.add(Box.createGlue(),BorderLayout.SOUTH);

		// creation et ajout d'un bouton pour lancer la creation du produit
		this.bpLauncherProduct = new JButton(LanguagesManager.getInstance()
				.getString("ListProductsPanelBpLauncherProduct"));
		
		// creation et ajout d'un bouton pour modifier le produit existant
		this.bpUpdaterProduct = new JButton(LanguagesManager.getInstance()
				.getString("ListProductsPanelBpUpdaterProduct"));
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(this.bpLauncherProduct);
		panel.add(this.bpUpdaterProduct);
		panel.setBackground(Color.WHITE);
		
		// creation et ajout du Label
		this.message = new JLabel(LanguagesManager.getInstance().getString(
				"nonDefineTool"));
		panel.add(this.message);
		this.add(panel, BorderLayout.SOUTH);
		
		// mise sur ecoute du bouton pour lancer la creation du produit
		this.bpLauncherProduct.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				ModelResourcesManager.getInstance().launchProductApplication(
						ListProductsPanel.this.theSelectedProduct);
			}
		});

		// mise sur ecoute du bouton pour lancer la modification du produit
		this.bpUpdaterProduct.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				ModelResourcesManager.getInstance()
						.launchUpdateProductApplication(
								ListProductsPanel.this.theSelectedProduct);
			}
		});

		// * Cr?er la liste * //
		// Rremplir le model de la Jlist
		DefaultListModel model = new DefaultListModel();
		for (Product p : productsToPresent)
		{
			model.addElement(p);
		}
		
		// Cr?er la JList
		this.productsList = new JList(model);
				
		// mettre en place un renderer Personnalis?
		this.productsList.setCellRenderer(new ListProductRenderer());
		
		// Associer un ?couteur ? la liste
		this.productsList.addListSelectionListener(new ListProductListener());
		
		// selectionner la premiere liste
		this.productsList.setSelectedIndex(0);

		// Ajouter la JList au panneaux
		this.add(this.productsList, BorderLayout.CENTER);

		// ajuste la taille prefererer pour qu'on puisse voir tout les produits
		// dans la liste
		this.productsList.setVisibleRowCount(productsToPresent.size());

		Dimension minDim = new Dimension(
				(int) this.productsList.getPreferredScrollableViewportSize()
						.getWidth(), this.productsList.getVisibleRowCount() * 18);
		
		// on defini la taille minimal et prefere du productsList
		this.productsList.setMinimumSize(minDim);
		this.productsList.setPreferredSize(minDim);
		
		// on met a jour la taille minumum de ListProductsPanel
		// on fait * 3 car il y a 2 composants (la jlist et un bouton)
		minDim = new Dimension((int) this.getMinimumSize().getWidth(),
				((int) this.productsList.getMinimumSize().getHeight())
						+ 26 + bl.getVgap() * 3);
		
		// on defini la taille minimal et prefere de ListProductsPanel
		this.setMinimumSize(minDim);
		this.setPreferredSize(minDim);
	}

	/**
	 * Methode app?l? lors d'un changement de selection de ligne
	 * 
	 * @param selectedProduct
	 *            produit selectionn?
	 */
	public abstract void onSelection (Product selectedProduct);

	private class ListProductListener implements ListSelectionListener
	{

		/**
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged (ListSelectionEvent e)
		{
			/* debut des modifications -> baloo */
			try
			{
				Properties pProduct = new Properties();
				// recupere le workspace courant
				String sPathProperties = PreferencesManager.getInstance()
						.getWorkspace();
				// recupere le chemin du .properties du projet courant
				sPathProperties += "/"
						+ ApplicationManager.getInstance().getCurrentProject()
								.getName();
				sPathProperties += "/documentation.properties";
				File fileProperties = new File(sPathProperties);
				// charge le fichier
				pProduct.load(new FileInputStream(fileProperties));

				if (!e.getValueIsAdjusting())
				{
					// Product selectedProduct;
					ListProductsPanel.this.theSelectedProduct = (Product) ((JList) e
							.getSource()).getSelectedValue();
					if (ListProductsPanel.this.theSelectedProduct.getEditor() != null)
					{
						// teste si un document est deja cree pour le produit
						if (pProduct
								.getProperty(ListProductsPanel.this.theSelectedProduct
										.getId()) != null)
						{
							ListProductsPanel.this.bpLauncherProduct
									.setVisible(false);
							ListProductsPanel.this.bpUpdaterProduct
									.setVisible(true);
						}
						else
						{
							ListProductsPanel.this.bpLauncherProduct
									.setVisible(true);
							ListProductsPanel.this.bpUpdaterProduct
									.setVisible(false);
						}
						ListProductsPanel.this.message.setVisible(false);
					}
					else
					{
						ListProductsPanel.this.bpLauncherProduct
								.setVisible(false);
						ListProductsPanel.this.bpUpdaterProduct
								.setVisible(false);
						ListProductsPanel.this.message.setVisible(true);
					}
					onSelection(ListProductsPanel.this.theSelectedProduct);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, LanguagesManager
						.getInstance().getString("ErreurDeLancement"),
						LanguagesManager.getInstance().getString(
								"ErreurDeLancementTitle"),
						JOptionPane.WARNING_MESSAGE);
			}
		}
		/* fin des modifications -> baloo */
	}

	private class ListProductRenderer extends DefaultListCellRenderer
	{

		/**
		 * @param list
		 * @param value
		 * @param index
		 * @param isSelected
		 * @param cellHasFocus
		 * @return Composant ? afficher
		 * 
		 */
		public Component getListCellRendererComponent (JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus)
		{
			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			
			label.setIcon(ModelResourcesManager.getInstance().getSmallIcon(
					(Product) value));
			Font LabelFont = label.getFont();
			
			label.setFont(LabelFont.deriveFont(Font.TRUETYPE_FONT));
			
			//TODO : c est ici qu on rajoute le cell renderer pr la JList je pense
			if (value instanceof Product)
			{
				// TODO, qd on aura un process avec des produits à description
				// faudra supprimer l affichage du nom
				String sTTT = ((Product) value).getName();
				if (((Product) value).getDescription() != null)
				{
					sTTT += " : "+((Product) value).getDescription(); 
				}
				this.setToolTipText(sTTT);
			}

			return label;
		}
	}

	/**
	 * Donne le focus a la liste
	 */
	public void requestFocus ()
	{
		this.productsList.requestFocus();
	}

}
