/*
 * $Id: ToolsTableModel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.util.Collections;
import java.util.Comparator;
import java.util.MissingResourceException;

import javax.swing.table.AbstractTableModel;

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Tool;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Classe permettant d'adapter les Outils d'un processus pour en faire le modele
 * d'une JTable
 * 
 * @author Fabien Jouanne
 */
public class ToolsTableModel extends AbstractTableModel
{
    private ArrayList<Tool> arrTools = new ArrayList<Tool>();

    /**
     * @param roles
     * 
     */
    public ToolsTableModel(Collection<Role> roles)
    {
        super();
        
        for (Role role : roles)
        {
            final Collection<Activity> activity = role.getActivities();
            for (Activity acty : activity)
            {
                // récupération uniquement des Tools des outputProducts
                final Collection<Product> outputProducts = acty.getOutputProducts();
                for (Product oProduct : outputProducts)
                {
                    final Tool tool = oProduct.getEditor();

                    if (tool != null)
                    {
                        if (!this.arrTools.contains(tool))
                        {
                            this.arrTools.add(tool);
                        }
                    }
                }
            }
        }

        // tri la liste
        Collections.sort(this.arrTools, new ToolComparator());
    }

    /**
     * retourne le nombre de colonne du modele
     * 
     * @return int
     */
    public int getRowCount()
    {
        return this.arrTools.size();
    }

    /**
     * retourne le nombre de colonne du modele
     * 
     * @return int
     */
    public int getColumnCount()
    {
        return 2;
    }

    /**
     * retourne le nombre de colonne du modele
     * 
     * @param arg0
     * @param arg1
     * @return String
     */
    public Object getValueAt(int arg0, int arg1)
    {
        Tool key = this.arrTools.get(arg0);

        if (arg1 == 0)
            return key;
        else
            return key.getPath();
    }

    /**
     * fonction qui renvoie le nom des colonnes
     * 
     * @param iColumnIndex
     * @return String
     * 
     */
    public String getColumnName(int iColumnIndex)
    {
        if (iColumnIndex == 0)
        {
            try
            {
                return LanguagesManager.getInstance().getString(
                        "ToolsSettingsTool");
            }
            catch (MissingResourceException e)
            {
            }
            catch (NotInitializedException e)
            {
            }
        }
        else
        {
            try
            {
                return LanguagesManager.getInstance().getString(
                        "ToolsSettingsPath");
            }
            catch (MissingResourceException e1)
            {
            }
            catch (NotInitializedException e1)
            {
            }
        }
        return "";
    }


    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     * @param columnIndex Colonne dont on souhaite connaître la classe
     * @return Classe de la colonne spécifiée
     */
    public Class<? extends Object> getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
            return Tool.class;
        else
            return String.class;
    }

    
    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex == 1);
    }
    
    
    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (columnIndex == 1 && aValue instanceof String)
        {
            this.arrTools.get(rowIndex).setPath((String)aValue);
            this.fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    private class ToolComparator implements Comparator<Tool>
    {
        /**
         * Compare les 2 outils spécifié
         * @param t1 premier outil
         * @param t2 deuxième outil
         * @return Résultat de la comparaison
         */
        public int compare(Tool t1, Tool t2)
        {
            return t1.getName().compareTo(t2.getName());
        }
    }
}
