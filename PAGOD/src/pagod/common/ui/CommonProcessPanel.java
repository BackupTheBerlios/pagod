/*
 * $Id: CommonProcessPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Object;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pagod.common.control.ModelResourcesManager;
import pagod.common.control.adapters.ProcessTreeModel;
import pagod.common.model.Activity;
import pagod.common.model.ProcessElement;
import pagod.common.model.Role;

/**
 * Panneaux affichant le processus
 * 
 * @author MoOky
 */
public abstract class CommonProcessPanel extends JSplitPane
{
    /**
     * Arbre du processus
     */
    private JTree processTree;

    /**
     * Panneaux visualiseur de fichier de contenu
     */
    private ContentViewerPane viewPanel;

    /**
     * Constructeur du panneaux
     * 
     * @param processTreeModel
     *            modele de l'arbre dans le panneaux
     */
    public CommonProcessPanel(ProcessTreeModel processTreeModel)
    {
        // creation du splitpane
        super();
        this.setOneTouchExpandable(true);
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        // creation du l'arbre
        this.processTree = new JTree(processTreeModel);
        // ne pas afficher le noeud racine
        this.processTree.setRootVisible(false);
        // ajout de l'écouteur de sélection sur le JTree
        this.processTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent event)
            {
                // dans tous les cas : mettre à jour le fichier de contenu
                // affiché dans le
                // viewPanel
                ProcessElement selectedElement = (ProcessElement) ((DefaultMutableTreeNode) event
                        .getPath().getLastPathComponent()).getUserObject();

                CommonProcessPanel.this.viewPanel
                        .setProcessElement(selectedElement);
                // si le dernier noeud correspond à une activité
                if (((DefaultMutableTreeNode) event.getPath()
                        .getLastPathComponent()).getUserObject() instanceof Activity)
                {
                    // lancer la méthode associé a la selection d'une activité
                    // try
                    CommonProcessPanel.this.onActivitySelection();
                }
                else
                {
                    // lancer la méthode associé a la selection d'un élément
                    // autre qu'une activité
                    CommonProcessPanel.this.onNoActivitySelection();
                }
            }
        });
        // ajout de l'écouteur de double click sur le JTree
        this.processTree.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // si c'est un double click
                if (e.getClickCount() == 2)
                {
                    TreePath clickedElementpath = CommonProcessPanel.this.processTree
                            .getPathForLocation(e.getX(), e.getY());
                    // si on click sur une élement de l'arbre et que c'est une
                    // activité
                    if (clickedElementpath != null
                            && ((DefaultMutableTreeNode) clickedElementpath
                                    .getLastPathComponent()).getUserObject() instanceof Activity)
                    {

                        CommonProcessPanel.this.onActivityDoubleClick();

                    }
                }
            }
        });
        // un seul noeud sélectionnable à la fois
        this.processTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        // ajout du renderer qui affiche les icones ad hoc en face des label
        this.processTree.setCellRenderer(new PAGODTreeCellRenderer());
        // déplier tout l'arbre
        this.expandAllNodes();
        // On active les ToolTip
        this.processTree.setToolTipText("");

        // ************************************
        // remplissage du panel (JSplitPane):
        // ************************************
        // en haut --> l'arbre des ProcessComponent/WorkDef/Activity
        this.add(new JScrollPane(this.processTree), JSplitPane.TOP);
        // en bas --> un panneau avec au centre le visualiseur de fichier HTML
        // creation de panneaux de visualisation des fichiers de contenu
        this.viewPanel = new ContentViewerPane();

        JPanel pnlVisualiseur = new JPanel(new BorderLayout());
        pnlVisualiseur.add(this.viewPanel, BorderLayout.CENTER);
        this.add(pnlVisualiseur, JSplitPane.BOTTOM);
    }

    /**
     * Retourne l'activité selectionnée dans l'arbre ou null (si rien n'est
     * selectionner ou si ce n'est pas une activité)
     * 
     * @return Activité selectioné.
     */
    public Activity getSelectedActivity()
    {
        if (this.processTree.getSelectionPath().getLastPathComponent() != null
                && ((DefaultMutableTreeNode) this.processTree
                        .getSelectionPath().getLastPathComponent())
                        .getUserObject() instanceof Activity)
        {
            return (Activity) ((DefaultMutableTreeNode) this.processTree
                    .getSelectionPath().getLastPathComponent()).getUserObject();
        }
        else
        {
            return null;
        }
    }

    private class PAGODTreeCellRenderer extends DefaultTreeCellRenderer
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
                if ((node.getUserObject() instanceof ProcessElement))
                {
                    this
                            .setIcon(ModelResourcesManager.getInstance()
                                    .getSmallIcon(
                                            (ProcessElement) node
                                                    .getUserObject()));

                }
                if ((node.getUserObject() instanceof Activity))
                {
                    // Affichage du role de l'activité en ToolTip
                    Activity a = null;
                    a = (Activity) node.getUserObject();
                    Role r = null;
                    r = a.getRole();
                    this.setToolTipText(r.getName());

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

    /**
     * déplier tous les noeuds du processTree
     */
    private void expandAllNodes()
    {
        int row = 0;
        while (row < this.processTree.getRowCount())
        {
            this.processTree.expandRow(row);
            row++;
        }
    }

    /**
     * replier tous les noeuds du processTree
     */
    private void collapseAllNodes()
    {
        int row = this.processTree.getRowCount() - 1;
        while (row >= 0)
        {
            this.processTree.collapseRow(row);
            row--;
        }
    }

    /**
     * Surcharge donne le focus a l'arbre
     */
    public void requestFocus()
    {
        this.processTree.requestFocus();
    }

    /**
     * 
     */
    protected abstract void onActivitySelection();

    /**
     * 
     */
    protected abstract void onNoActivitySelection();

    /**
     * 
     */
    protected abstract void onActivityDoubleClick();
}
