/*
 * $Id: OutputProductsTableModel.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

package pagod.configurator.control.adapters;

import javax.swing.table.AbstractTableModel;

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Step;

/**
 * @author Benjamin
 */
public class OutputProductsTableModel extends AbstractTableModel
{
    private Activity activity;

    private Step step;

    /**
     * @param a
     *            activité de la step à adapter
     * @param s
     *            step à adapter
     */
    public OutputProductsTableModel(Activity a, Step s)
    {
        super();
        this.step = s;
        this.activity = a;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return this.activity.getOutputProducts().size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 2;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                // colonne 1 : on renvoie le Boolean true si step a le produit
                // en sortie,
                // faux
                // sinon
                if (this.step.getOutputProducts() != null
                        && this.activity.getOutputProducts() != null)
                    return new Boolean(
                            this.step.getOutputProducts()
                                    .contains(
                                            this.activity.getOutputProducts()
                                                    .toArray()[rowIndex]));
                else
                    return false;
            case 1:
                // colonne 2 : le nom du produit
                return ((Product) this.activity.getOutputProducts().toArray()[rowIndex])
                        .getName();
            default:
                return null;
        }

    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class< ? > getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            default:
                // erreur
                return null;
        }
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        // coche/décoche --> ajouter/supprimer le product à la Step
        Boolean value = (Boolean) aValue;
        if (value)
        {
            this.step.addOutputProduct((Product) this.activity
                    .getOutputProducts().toArray()[rowIndex]);
        }
        else
        {
            this.step.removeOutputProduct((Product) this.activity
                    .getOutputProducts().toArray()[rowIndex]);
        }
        // fireTableCellUpdated(rowIndex, columnIndex);

    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        // on ne peut éditer que la colonne "checkbox"
        return (columnIndex == 0);
    }

    /**
     * @return Retourne l'attribut step
     */
    public Step getStep()
    {
        return this.step;
    }
}
