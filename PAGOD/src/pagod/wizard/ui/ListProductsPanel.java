/*
 * $Id: ListProductsPanel.java,v 1.2 2005/11/13 18:10:42 cyberal82 Exp $
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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Product;
import pagod.utils.LanguagesManager;

/**
 * Presente une liste de produits
 * 
 * @author MoOky
 * 
 */
public abstract class ListProductsPanel extends JPanel
{
    private JList productsList;

    /**
     * Produit selectionne
     */
    private Product theSelectedProduct;

    /**
     * label pour prévenir
     */
    private JLabel message;

    /**
     * Bouton permettant de lancer la creation d'un produit
     */
    private JButton bpLauncherProduct;

    /**
     * Constructeur
     * 
     * @param productsToPresent
     *            liste de produit à présentée
     */
    public ListProductsPanel(Collection<Product> productsToPresent)
    {
        super();
        // * Mise en forme * //
        // mettre le fonc du panneaux en blanc
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

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(this.bpLauncherProduct);
        panel.setBackground(Color.WHITE);
        // creation et ajout du Label
        this.message = new JLabel(LanguagesManager.getInstance()
                .getString("nonDefineTool"));
        panel.add(this.message);
        this.add(panel, BorderLayout.SOUTH);
        // mise sur ecoute du bouton pour lancer la creation du produit
        this.bpLauncherProduct.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                ModelResourcesManager.getInstance().launchProductApplication(
                        ListProductsPanel.this.theSelectedProduct);
            }
        });

        // * Créer la liste * //
        // Rremplir le model de la Jlist
        DefaultListModel model = new DefaultListModel();
        for (Product p : productsToPresent)
        {
            model.addElement(p);
        }
        // Créer la JList
        this.productsList = new JList(model);
        // mettre en place un renderer Personnalisé
        this.productsList.setCellRenderer(new ListProductRenderer());
        // Associer un écouteur à la liste
        this.productsList.addListSelectionListener(new ListProductListener());
        // selectionner la premiere liste
        this.productsList.setSelectedIndex(0);
        // Ajouter la JList au panneaux
        this.add(this.productsList, BorderLayout.CENTER);
    }

    /**
     * Methode appélé lors d'un changement de selection de ligne
     * 
     * @param selectedProduct
     *            produit selectionné
     */
    public abstract void onSelection(Product selectedProduct);

    private class ListProductListener implements ListSelectionListener
    {

        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e)
        {
            if (!e.getValueIsAdjusting())
            {
                // Product selectedProduct;
                ListProductsPanel.this.theSelectedProduct = (Product) ((JList) e
                        .getSource()).getSelectedValue();
                if (ListProductsPanel.this.theSelectedProduct.getEditor() != null)
                {
                    ListProductsPanel.this.bpLauncherProduct.setVisible(true);
                    ListProductsPanel.this.message.setVisible(false);
                }
                else
                {
                    ListProductsPanel.this.bpLauncherProduct.setVisible(false);
                    ListProductsPanel.this.message.setVisible(true);
                }
                onSelection(ListProductsPanel.this.theSelectedProduct);
            }
        }

    }

    private class ListProductRenderer extends DefaultListCellRenderer
    {

        /**
         * @param list
         * @param value
         * @param index
         * @param isSelected
         * @param cellHasFocus
         * @return Composant à afficher
         * 
         */
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            JLabel label = (JLabel) super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
            label.setIcon(ModelResourcesManager.getInstance().getSmallIcon(
                        (Product) value));
            Font LabelFont = label.getFont();
            label.setFont(LabelFont.deriveFont(Font.TRUETYPE_FONT));
            return label;
        }
    }

    /**
     * Donne le focus a la liste
     */
    public void requestFocus()
    {
        this.productsList.requestFocus();
    }

}
