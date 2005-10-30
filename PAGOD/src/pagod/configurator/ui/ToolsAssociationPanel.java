/*
 * $Id: ToolsAssociationPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Process;
import pagod.common.model.Product;
import pagod.common.model.Tool;
import pagod.configurator.control.adapters.ProductsListModel;
import pagod.configurator.control.adapters.ToolsProductsAssociationsTreeModel;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * Panneaux de configuration des outils.
 * 
 * @author MoOky
 * 
 */
public class ToolsAssociationPanel extends JPanel 
{
    private ToolsAssociationsTree associationsTree;

    private PagodTreeModelListener listener;

    private ProductsList lstProducts;

    private JButton pbNewTool;

    private JButton pbEditTool;

    private JButton pbRemoveTool;

    /**
     * Constructeur du panneaux de configuration des outils
     * 
     * @param process
     */
    public ToolsAssociationPanel(Process process)
    {
        Rectangle screenSize = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int iWidth = screenSize.width;
        int iHeight = screenSize.height;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // partie de la liste
        JPanel pProducts = new JPanel();
        JLabel lTitleProducts = new JLabel(LanguagesManager.getInstance()
                .getString("ToolsPanelProductsTitle"));
        ProductsListModel listModelLstProducts = new ProductsListModel(process);
        /*
         * // initialisation de la liste avec les produits for (Product product :
         * process.getAllProducts()) { listModelLstProducts.addElement(product); }
         */
        this.lstProducts = new ProductsList(listModelLstProducts);
        this.lstProducts.setCellRenderer(new PagodListCellRenderer());
        // this.lstProducts.setDragEnabled(true);
        pProducts.setLayout(new BoxLayout(pProducts, BoxLayout.Y_AXIS));
        pProducts.add(lTitleProducts);
        JScrollPane scrolledListProducts = new JScrollPane(this.lstProducts);
        pProducts.add(scrolledListProducts);
        pProducts.setBackground(Color.WHITE);
        pProducts.setMaximumSize(new Dimension(iWidth / 3, iHeight));

        // partie de l'arbre
        JPanel pTools = new JPanel();
        JLabel lTitleAssociations = new JLabel(LanguagesManager.getInstance()
                .getString("ToolsPanelAssociationsTitle"));
        pTools.setLayout(new BoxLayout(pTools, BoxLayout.Y_AXIS));
        ToolsProductsAssociationsTreeModel associationsTreeModel = new ToolsProductsAssociationsTreeModel(
                process);
        this.listener = new PagodTreeModelListener(this.lstProducts);
        associationsTreeModel.addTreeModelListener(this.listener);
        this.associationsTree = new ToolsAssociationsTree(associationsTreeModel);
        this.associationsTree.setCellRenderer(new PagodTreeCellRenderer());
        this.associationsTree.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                Object t = ToolsAssociationPanel.this.associationsTree
                        .getLastSelectedPathComponent();
                if (t instanceof Tool)
                {
                    ToolsAssociationPanel.this.pbEditTool.setEnabled(true);
                    ToolsAssociationPanel.this.pbRemoveTool.setEnabled(true);
                }
                else
                {
                    ToolsAssociationPanel.this.pbEditTool.setEnabled(false);
                    ToolsAssociationPanel.this.pbRemoveTool.setEnabled(false);
                }
            }

        });
        // ne pas afficher le noeud racine
        this.associationsTree.setRootVisible(false);
        JScrollPane scrolledTreeAssociations = new JScrollPane(
                this.associationsTree);
        pTools.add(lTitleAssociations);
        pTools.add(scrolledTreeAssociations);
        pTools.setBackground(Color.WHITE);

        // section management des outils, implementer les methodes des boutons
        JPanel panToolsManagement = new JPanel();
        this.pbNewTool = new JButton(LanguagesManager.getInstance().getString(
                "ToolsPanelNewToolButtonLabel"));
        this.pbNewTool.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                    // recuperation des path etendus de l'arbre
                    ToolsProductsAssociationsTreeModel model = (ToolsProductsAssociationsTreeModel) ToolsAssociationPanel.this.associationsTree
                            .getModel();
                    ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
                    for (int i = 0 ; i < model.getChildCount(model.getRoot()) ; i++)
                    {
                        Tool tool = (Tool) model.getChild(model.getRoot(), i);
                        TreePath treePath = model.getPath(tool);
                        if (ToolsAssociationPanel.this.associationsTree
                                .isExpanded(treePath))
                        {
                            expandedPaths.add(treePath);
                        }
                    }
                    // creation de la fenetre d'ajout de tool
                    new AddToolDialog(null, model);
                    // la fenetre rend la main, on repercute les modifications
                    // effectuees
                    model.fireTreeStructureChanged();
                    // on etend les path etendus avant la modification
                    for (TreePath treePath : expandedPaths)
                    {
                        ToolsAssociationPanel.this.associationsTree
                                .scrollPathToVisible(treePath);
                        ToolsAssociationPanel.this.associationsTree
                                .expandPath(treePath);
                    }

            }
        });
        this.pbEditTool = new JButton(LanguagesManager.getInstance().getString(
                "ToolsPanelEditToolButtonLabel"));
        this.pbEditTool.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                // recuperation des path etendus de l'arbre
                ToolsProductsAssociationsTreeModel model = (ToolsProductsAssociationsTreeModel) ToolsAssociationPanel.this.associationsTree
                        .getModel();
                ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
                for (int i = 0 ; i < model.getChildCount(model.getRoot()) ; i++)
                {
                    Tool tool = (Tool) model.getChild(model.getRoot(), i);
                    TreePath treePath = model.getPath(tool);
                    if (ToolsAssociationPanel.this.associationsTree
                            .isExpanded(treePath))
                    {
                        expandedPaths.add(treePath);
                    }
                }
                // on recupere l'objet selectionne et on le modifie si c'est un
                // tool
                Object object = ToolsAssociationPanel.this.associationsTree
                        .getLastSelectedPathComponent();
                if (object instanceof Tool)
                    new AddToolDialog(null, (Tool) object, model);
                // on repercute les modifications effectuees
                model.fireTreeStructureChanged();
                // on etend les path etendus avant la modification
                for (TreePath treePath : expandedPaths)
                {
                    ToolsAssociationPanel.this.associationsTree
                            .scrollPathToVisible(treePath);
                    ToolsAssociationPanel.this.associationsTree
                            .expandPath(treePath);
                }

            }
        });
        this.pbRemoveTool = new JButton(LanguagesManager.getInstance()
                .getString("ToolsPanelDeleteToolButtonLabel"));
        this.pbRemoveTool.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                int iResponse;
                iResponse = JOptionPane.showOptionDialog(null, LanguagesManager
                        .getInstance().getString("SupToolConfirm"),
                        LanguagesManager.getInstance()
                                .getString("SupToolTitle"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, null, null);
                if (iResponse != 1)
                {
                    // recuperation des path etendus de l'arbre
                    ToolsProductsAssociationsTreeModel model = (ToolsProductsAssociationsTreeModel) ToolsAssociationPanel.this.associationsTree
                            .getModel();
                    ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
                    for (int i = 0 ; i < model.getChildCount(model.getRoot()) ; i++)
                    {
                        Tool tool = (Tool) model.getChild(model.getRoot(), i);
                        TreePath treePath = model.getPath(tool);
                        if (ToolsAssociationPanel.this.associationsTree
                                .isExpanded(treePath))
                        {
                            expandedPaths.add(treePath);
                        }
                    }
                    // on recupere l'objet selectionne et on le supprime si
                    // c'est un
                    // tool
                    Object object = ToolsAssociationPanel.this.associationsTree
                            .getLastSelectedPathComponent();
                    if (object instanceof Tool)
                        model.deleteTool((Tool) object);
                    // on repercute les modifications effectuees
                    model.fireTreeStructureChanged();
                    // on etend les path etendus avant la modification
                    for (TreePath treePath : expandedPaths)
                    {
                        ToolsAssociationPanel.this.associationsTree
                                .scrollPathToVisible(treePath);
                        ToolsAssociationPanel.this.associationsTree
                                .expandPath(treePath);
                    }
                }

            }
        });

        // on ajoute un grid bag layout et le placement des bouton
        GridBagLayout gLayout = new GridBagLayout();

        panToolsManagement.setLayout(gLayout);

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        gLayout.setConstraints(this.pbNewTool, constraint);
        panToolsManagement.add(this.pbNewTool);

        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        gLayout.setConstraints(this.pbEditTool, constraint);
        panToolsManagement.add(this.pbEditTool);

        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        gLayout.setConstraints(this.pbRemoveTool, constraint);
        panToolsManagement.add(this.pbRemoveTool);
        panToolsManagement.setBackground(Color.WHITE);
        panToolsManagement.setMaximumSize(new Dimension(0, iHeight));
        // mise en place des cibles de drop
        DropTarget listDropTarget = new DropTarget(this.lstProducts,
                this.lstProducts);

        DropTarget treeDropTarget = new DropTarget(this.associationsTree,
                this.associationsTree);
        this.associationsTree.setDropTarget(treeDropTarget);
        this.lstProducts.setDropTarget(listDropTarget);

        this.add(pProducts);
        this.add(pTools);
        this.add(panToolsManagement);

        this.pbEditTool.setEnabled(false);
        this.pbRemoveTool.setEnabled(false);

        this.expandAllNodes();

    }

    /**
     * Renderer des cellules des listes
     */
    class PagodListCellRenderer extends DefaultListCellRenderer
    {
        /**
         * Methode permettant de remplir le composant d'affichage avec le nom et
         * l'icone des roles
         * 
         * @param list
         *            la liste sur laquelle s'applique le renderer
         * @param value
         *            l'element de la liste sur lequel s'applique le renderer
         * @param index
         *            l'index de l'element dans la liste
         * @param isSelected
         *            determine si l'element est selectionne ou non
         * @param cellHasFocus
         *            determine si l'element a le focus ou non
         * @return le composant a afficher
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *      java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            String s = value.toString();
            // rempli le composant avec le libelle du produit
            if (((Product) value).getEditor() != null)
            {
                s = s + " - " + ((Product) value).getEditor().toString();
            }
            this.setText(s);
            // ajoute l'image du produit au composant
            this.setIcon(ModelResourcesManager.getInstance().getSmallIcon(
                    (Product) value));
            // gere le changement de couleur de l'item selectionne
            if (isSelected)
            {
                this.setBackground(list.getSelectionBackground());
                this.setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            return this;
        }
    }

    private class PagodTreeCellRenderer extends DefaultTreeCellRenderer
    {
        /**
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         *      java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean focus)
        {
            Component c = null;

            c = super.getTreeCellRendererComponent(tree, value, sel, expanded,
                    leaf, row, focus);

            if (value instanceof Tool)
            {
                this.setIcon(ImagesManager.getInstance().getSmallIcon(
                        "ToolsSettingsIcon.gif"));

            }
            else if (value instanceof Product)
            {
                this.setIcon(ModelResourcesManager.getInstance().getIcon(
                        (Product) value));
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

            return c;
        }

    }

    private class PagodTreeModelListener implements TreeModelListener
    {
        private ProductsList productsList;

        /**
         * @param productsList
         */
        public PagodTreeModelListener(ProductsList productsList)
        {
            this.productsList = productsList;
        }

        /**
         * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeStructureChanged(TreeModelEvent e)
        {
            ((ProductsListModel) this.productsList.getModel())
                    .fireContentsChanged(this.productsList.getModel(), 0,
                            ((ProductsListModel) this.productsList.getModel())
                                    .getSize());
        }

        /**
         * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesChanged(TreeModelEvent e)
        {
            // TODO Auto-generated method stub
        }

        /**
         * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesInserted(TreeModelEvent e)
        {
            // TODO Auto-generated method stub
        }

        /**
         * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesRemoved(TreeModelEvent e)
        {
            // TODO Auto-generated method stub
        }
    }

    /**
     * deplier tous les noeuds du tree
     */
    private void expandAllNodes()
    {
        int row = 0;
        while (row < this.associationsTree.getRowCount())
        {
            this.associationsTree.expandRow(row);
            row++;
        }
    }

    /**
     * replier tous les noeuds du tree
     */
    private void collapseAllNodes()
    {
        int row = this.associationsTree.getRowCount() - 1;
        while (row >= 0)
        {
            this.associationsTree.collapseRow(row);
            row--;
        }
    }

}
