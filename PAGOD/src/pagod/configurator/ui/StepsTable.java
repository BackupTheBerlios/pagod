/*
 * $Id: StepsTable.java,v 1.4 2006/03/04 18:00:55 garwind111 Exp $
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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.adapters.ActivityStepsTableModel;
import pagod.configurator.control.adapters.OutputProductsTableModel;
import pagod.configurator.control.adapters.StepsTransferHandler;
import pagod.utils.LanguagesManager;

/**
 * @author Benjamin
 * @author Arno
 */
public class StepsTable extends JTable 
{
	
    /**
     * Renderer d'une String en HTML (EditorPane)
     */
    private class HTMLRenderer extends DefaultTableCellRenderer
    {
        private JEditorPane jEditorPane = new JEditorPane("text/html", "");

        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column)
        {
            JLabel labelParDefaut = (JLabel) super
                    .getTableCellRendererComponent(table, value, hasFocus,
                            hasFocus, row, column);
            // récupérer la bordure du renderer par défaut
            this.jEditorPane.setBorder(labelParDefaut.getBorder());

            String sContenu = "";
            if (value != null)
                sContenu = value.toString();
            this.jEditorPane.setText(sContenu);
            // si la ligne est sélectionnée, changer le background
            if (isSelected)
                this.jEditorPane.setBackground(table.getSelectionBackground());
            else if (row % 2 != 0)
                this.jEditorPane
                        .setBackground(StepsTable.this.couleurLignesImpaires);
            else
                this.jEditorPane
                        .setBackground(StepsTable.this.couleurLignesPaires);
            // TODO tooltip en HTML
            this.jEditorPane.setToolTipText(sContenu);
            // renvoi du composant de rendu*/
            return this.jEditorPane;
        }
    }
    /**
     * 
     * @author yak
     *
     */
    
    //clase permettant d'obtenir un editeur avec un text area ala place d'un jtextfield
    private class StepEditor extends AbstractCellEditor implements TableCellEditor
    {
        private JTextArea txtAreaEditor = new JTextArea();

     
    //constructeur vide de l'editor
    /**
     * 
     */
    public StepEditor()
       {
       }
   
    /**
     * informe si la cellule est editable lors de l'eveneme
     * @param evt evenement
     * @return boolean True si la cellule est editable, false sinon
     * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject evt)
       {
    		if (evt instanceof MouseEvent)
           {
               // éditabilité sur un double-clic
               return ((MouseEvent) evt).getClickCount() >= 2;
           }
           return true;
       }
       /**
        * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
        */
       public boolean shouldSelectCell(EventObject anEvent)
       {
    	   this.txtAreaEditor.repaint();
    	   return true;
       }
       
        /**
         * 
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue()
        {
            //on renvoie la valeur du txtArea
            return this.txtAreaEditor.getText();
        }
        /**
         * renvoie une jscrollpane contenant un textearea avec la valeur a éditer
         * 
         * @param arg0 JTable d'origine
         * @param arg1 Valeur a editée
         * @param arg2 
         * @param arg3 
         * @param arg4 
         * @return component le composant d'edition
         * 
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4)
        {
          
            //on crée un textarea avec la valeur de la celulle dedans
            this.txtAreaEditor.setText((String)arg1);
            //on renvoir un jscroll pane avec le text area dedans
           return new JScrollPane(this.txtAreaEditor);
        }
        
    }
    /**
     * Composant d'édition des produits en sortie d'une Step
     */
    private class OutputProductsEditingTable extends AbstractCellEditor
            implements TableCellEditor
    {
        private int i, j;

        private JTable tableEdition = new JTable();

        /**
         *
         */
        public OutputProductsEditingTable()
        {
        }

        /**
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue()
        {
            return ((OutputProductsTableModel) this.tableEdition.getModel())
                    .getStep();
        }

        /**
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row, int col)
        {
            // sauvegarde coordonnées du composant de rendu
            this.i = row;
            this.j = col;

            this.tableEdition.setModel(new OutputProductsTableModel(
                    ((ActivityStepsTableModel) table.getModel()).getActivity(),
                    ((ActivityStepsTableModel) table.getModel())
                            .getStepByRow(row)));
            /*
             * if (isSelected)
             * this.tableEdition.setBackground(table.getSelectionBackground());
             * else if (row % 2 != 0) this.tableEdition
             * .setBackground(StepsTable.this.couleurLignesImpaires); else
             * this.tableEdition
             * .setBackground(StepsTable.this.couleurLignesPaires);
             */
            
            this.tableEdition.getColumnModel().getColumn(0).setPreferredWidth(
                    15);
            this.tableEdition.getColumnModel().getColumn(0).setMaxWidth(15);
            
            // TODO donner correctement le focus à la première ligne de la table
            // (pour édition tout au clavier)
            /*
             * if (this.tableEdition.getRowCount() > 0)
             * this.tableEdition.setRowSelectionInterval(0, 0);
             * this.tableEdition.requestFocus();
             */
            return this.tableEdition;

        }

        /**
         * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
         */
        public boolean isCellEditable(EventObject evt)
        {
            if (evt instanceof MouseEvent)
            {
                // éditabilité sur un double-clic
                return ((MouseEvent) evt).getClickCount() >= 2;
            }
            return true;
        }

        /**
         * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
         */
        public boolean shouldSelectCell(EventObject anEvent)
        {
            return true;
        }
    }

    /**
     * Renderer d'une Step en un Editorpane de liste de produits en sortie
     */
    private class OutputProductsRenderer extends DefaultTableCellRenderer
    {
        private JPanel panelRendu = new JPanel();

        /**
         * 
         */
        public OutputProductsRenderer()
        {
            super();
            this.panelRendu.setLayout(new BoxLayout(this.panelRendu,
                    BoxLayout.Y_AXIS));
        }

        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column)
        {
            JLabel labelParDefaut = (JLabel) super
                    .getTableCellRendererComponent(table, value, hasFocus,
                            hasFocus, row, column);
            // récupérer la bordure du renderer par défaut
            this.panelRendu.setBorder(labelParDefaut.getBorder());

            // value est une step --> parcourir l'arrayList de products (les
            // produits en sortie de la step
            this.panelRendu.removeAll();
            ArrayList<Product> arrProducts = new ArrayList<Product>();
            if (((Step) value).hasOutputProducts())
            {
                arrProducts = (ArrayList<Product>) ((Step) value)
                        .getOutputProducts();
                // ajouter autant de JLabel que de produits
                for (Product p : arrProducts)
                {
                    try
                    {
                        this.panelRendu.add(new JLabel(p.getName(),
                                ModelResourcesManager.getInstance().getIcon(p),
                                JLabel.LEFT));
                    }
                    catch (Exception e)
                    {
                        this.panelRendu
                                .add(new JLabel(p.getName(), JLabel.LEFT));
                    }
                }
            }
            else
            {
                // pas de produits en sortie
                try
                {
                    this.panelRendu.add(new JLabel(LanguagesManager
                            .getInstance().getString(
                                    "StepsPanelNoProductForThisStep")));
                }
                catch (Exception e)
                {
                }
            }
            // si la ligne est sélectionnée, changer le background
            if (isSelected)
                this.panelRendu.setBackground(table.getSelectionBackground());
            else
            {
                if (row % 2 != 0)
                    this.panelRendu
                            .setBackground(StepsTable.this.couleurLignesImpaires);
                else
                    this.panelRendu
                            .setBackground(StepsTable.this.couleurLignesPaires);
            }

            // renvoi du composant de rendu
            return this.panelRendu;
        }
    }
    
    protected final Color couleurLignesImpaires = new Color(0xFBFFEB);

    protected final Color couleurLignesPaires = new Color(0xEEFFEB);

    /**
     * @author Benjamin
     * @param dm
     */
    public StepsTable(ActivityStepsTableModel dm)
    {
        super(dm);
        this.setAutoCreateColumnsFromModel(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        HTMLRenderer htmlRenderer = new HTMLRenderer();
        this.getColumnModel().getColumn(0).setCellRenderer(htmlRenderer);
        this.getColumnModel().getColumn(1).setCellRenderer(htmlRenderer);
        this.getColumnModel().getColumn(2).setCellRenderer(htmlRenderer);
        this.getColumnModel().getColumn(3).setCellRenderer(
                new OutputProductsRenderer());
        this.getColumnModel().getColumn(2).setCellEditor(new StepEditor());
        this.getColumnModel().getColumn(3).setCellEditor(
                new OutputProductsEditingTable());
        this.getColumnModel().getColumn(3).setCellEditor(
                new OutputProductsEditingTable());
        this.getColumnModel().getColumn(0).setMaxWidth(10);
        this.getColumnModel().getColumn(1).setMinWidth(150);
        this.setRowHeight(80);
        this.setDragEnabled(true);
        this.setTransferHandler(new StepsTransferHandler());
    }
}
