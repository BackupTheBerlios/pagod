/*
 * $Id: ToolsSettingsDialog.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Collection;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import pagod.common.model.Role;
import pagod.common.model.Tool;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.adapters.ToolsTableModel;

/**
 * Fenetre de Configuration des outils d'un processus donn�. La configuration
 * consiste � pr�ciser le chemin d'acc�s pour chaque outils
 * 
 * @author MoOky
 */
public class ToolsSettingsDialog extends JDialog
{
    // couleur arri�re plan ligne paires
    private static final Color COLOR_ODD = new Color(0xFBFFEB);

    // couleur arri�re plan ligne impaires
    private static final Color COLOR_EVEN = new Color(0xEEFFEB);

    // couleur arri�re plan ligne erron�es
    private static final Color COLOR_ERROR = Color.RED;

    // model contenant les donn�es
    private ToolsTableModel tModel;

    // Jtable
    private JTable toolsTable;

    // bouton Fermer
    private JButton closeButton;

    /**
     * Constructeur d'une ToolsSettingsDialog, ayant pour fen�tre appelante
     * parentFrame
     * 
     * @param parentFrame
     *            fen�tre appelante
     * @param roles
     *            Liste des roles dont il faut configurer les outils
     *             languageManager pas initialis�
     */
    public ToolsSettingsDialog(JFrame parentFrame, Collection<Role> roles)
    {
        super(parentFrame, LanguagesManager.getInstance().getString(
                "ToolsSettingsTitle"), true);

        this.tModel = new ToolsTableModel(roles);

        this.toolsTable = new JTable(this.tModel);
        this.toolsTable.setFocusable(true);
        this.toolsTable.setSurrendersFocusOnKeystroke(true);
        this.toolsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.toolsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        this.toolsTable.getColumnModel().getColumn(1).setCellEditor(
                new CellPathEditor());
        final CellPathRenderer cellPathRenderer = new CellPathRenderer();
        this.toolsTable.getColumnModel().getColumn(0).setCellRenderer(
                cellPathRenderer);
        this.toolsTable.getColumnModel().getColumn(1).setCellRenderer(
                cellPathRenderer);
        this.toolsTable.setCellSelectionEnabled(false);
        this.toolsTable.setColumnSelectionAllowed(false);
        this.toolsTable.setRowSelectionAllowed(false);

        this.closeButton = new JButton(LanguagesManager.getInstance()
                .getString("ToolsSettingsClose"));
        // demande la fermeture de la fen�tre en cas de clic sur le bouton
        // fermer
        this.closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // on ferme la fenetre
                ToolsSettingsDialog.this.dispose();
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.closeButton);

        final JScrollPane tablePanel = new JScrollPane(this.toolsTable);

        this.add(tablePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // boite de dialogue modale et centr�e par rapport � l'appelant

        this.pack();
        this.setLocationRelativeTo(parentFrame);
    }

    /**
     * Rendereur de la table
     */
    private class CellPathRenderer extends DefaultTableCellRenderer
    {

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column)
        {
            String path;
            if (column == 0)
            {
                path = ((Tool) value).getPath();
                this.setToolTipText(((Tool) value).getName());
            }
            else
            {
                path = (String) value;
                this.setToolTipText((String) value);
            }
            // cas o� le getPath du Tool ou bien le chemin d'appli seraient �
            // null
            if (path == null)
            {
                path = "";
            }
            final File f = new File(path);

            // d�finition de la couleur d'arri�re plan
            if (!f.isFile())
                this.setBackground(ToolsSettingsDialog.COLOR_ERROR);
            else if ((row & 1) != 0)
                this.setBackground(ToolsSettingsDialog.COLOR_ODD);
            else
                this.setBackground(ToolsSettingsDialog.COLOR_EVEN);

            return super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }

    }

    /**
     * Editeur de chemin int�gr� dans la table
     */
    private class CellPathEditor extends AbstractCellEditor implements
            TableCellEditor
    {
        /** Chemin actuel */
        private String sCurrentPath;

        /** Champ de saisie du chemin */
        private JTextField tfPath;

        /** Listeners du model */
        private EventListenerList listenerList = new EventListenerList();

        /**
         * Retourne le composant d'�dition, ne fait aucun test, ne doit �tre
         * utilis� que pour des chemins
         * 
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            // met � jour avec la nouvelle valeur
            this.sCurrentPath = (String) value;

            // cr�ation du panneau
            final JPanel pEditor = new JPanel(new BorderLayout());
            this.tfPath = new JTextField(this.sCurrentPath);
            final JButton btBrowser = new JButton("...");
            btBrowser.addActionListener(new BrowseAction());
            pEditor.add(this.tfPath, BorderLayout.CENTER);
            pEditor.add(btBrowser, BorderLayout.EAST);

            // passe le focus au textfield lorsque l'�diteur le re�oit
            pEditor.setFocusable(true);
            pEditor.addFocusListener(new FocusListener()
            {
                public void focusGained(FocusEvent e)
                {
                    CellPathEditor.this.tfPath.requestFocusInWindow();
                }

                public void focusLost(FocusEvent e)
                {
                }
            });

            // retourne le panneau
            return pEditor;
        }

        /**
         * Retourne la valeur courante
         * 
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue()
        {
            return this.sCurrentPath;
        }

        /**
         * Arr�te l'�dition et signale la modification
         * 
         * @see javax.swing.CellEditor#stopCellEditing()
         */
        public boolean stopCellEditing()
        {
            // r�cup�re le nouveau chemin
            this.sCurrentPath = this.tfPath.getText();
            // alerte les listeners
            this.fireEditingStopped();
            // accepte l'�dition
            return true;
        }

        /**
         * Annule l'�dition et signale l'annulation
         * 
         * @see javax.swing.CellEditor#cancelCellEditing()
         */
        public void cancelCellEditing()
        {
            // restaure la valeur du textfield (surement inutile)
            this.tfPath.setText(this.sCurrentPath);
            // alerte les listeners
            this.fireEditingCanceled();
        }

        /**
         * Traite le clic sur le bouton Parcourir
         * 
         * @author m1isi24
         */
        private class BrowseAction implements ActionListener
        {
            /**
             * Affiche la fen�tre de s�lection de l'ex�cutable et modifie le
             * champs lors de la validation
             * 
             * @param e
             *            Ev�nement re�u
             */
            public void actionPerformed(ActionEvent e)
            {
                // r�cup�ration du chemin actuel
                final String sPath = CellPathEditor.this.tfPath.getText();
                final File fPath = new File(sPath);

                // cr�ation du s�lecteur de fichier
                final JFileChooser dFileChooser = new JFileChooser();

                if (fPath.exists())
                {
                    if (fPath.isDirectory())
                        dFileChooser.setCurrentDirectory(fPath);
                    else
                        dFileChooser.setSelectedFile(fPath);
                }

                // traitement lors de la validation
                if (dFileChooser.showOpenDialog(ToolsSettingsDialog.this) == JFileChooser.APPROVE_OPTION)
                {
                    CellPathEditor.this.tfPath.setText(dFileChooser
                            .getSelectedFile().getAbsolutePath());
                    CellPathEditor.this.stopCellEditing();
                }
            }
        }
    }
}
