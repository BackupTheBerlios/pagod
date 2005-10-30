/*
 * $Id: ActivityStepsTableModel.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

import java.util.MissingResourceException;

import javax.swing.table.AbstractTableModel;

import pagod.common.model.*;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Classe permettant d'adapter une activité pour récupérer la liste de ses
 * activités
 * 
 * @author Benjamin
 */
public class ActivityStepsTableModel  extends AbstractTableModel 
{
    /**
     * l'activité à adapter
     */
    private Activity activity;

    /**
     * en-têtes de colonnes
     */
    private String[] columnNames ;

    /**
     * classe des colonnes (plus facile à maintenir en passant par un tableau de
     * la sorte)
     */
    private Class[] columnClasses = { Integer.class, String.class,
            String.class, Step.class };

    /**
     * constructeur vide
     * @throws NotInitializedException 
     * @throws MissingResourceException 
     */
    public ActivityStepsTableModel() throws MissingResourceException, NotInitializedException
    {
           this.columnNames = new String[] { LanguagesManager.getInstance().getString("StepsPanelTableCol1") ,
            LanguagesManager.getInstance().getString("StepsPanelTableCol2") ,
            LanguagesManager.getInstance().getString("StepsPanelTableCol3") ,
            LanguagesManager.getInstance().getString("StepsPanelTableCol4") } ;
    }

    /**
     * @param a
     */
    public ActivityStepsTableModel(Activity a)
    {
        super();
        // sauvegarde de l'activité sur laquelle le modèle repose
        this.activity = a;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        if (this.activity == null)
            return 0;
        else
            return this.activity.getSteps().size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 4;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                // première colonne : renvoyer le rang de l'étape (= ligne)
                return new Integer(rowIndex + 1);
            case 1:
                // deuxième colonne : nom étape
                return this.activity.getSteps().get(rowIndex).getName();
            case 2:
                // troisième colonne : contenu étape
                return this.activity.getSteps().get(rowIndex).getComment();
            case 3:
                // quatrième colonne : renvoyer l'étape (à
                // mettre en forme avec renderer ad hoc pour afficher les
                // outputproducts
                return this.activity.getSteps().get(rowIndex);
            default:
                // erreur
                return "";
        }
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col)
    {
        return this.columnNames[col];
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class< ? > getColumnClass(int columnIndex)
    {
        return this.columnClasses[columnIndex];
    }

    /**
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col)
    {
        // toutes les colonnes sont éditables sauf la première (numéro d'étape)
        if (col == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * @param row
     * @return l'étape à la ligne row
     */
    public Step getStepByRow(int row)
    {
        return this.activity.getSteps().get(row);
    }

    /**
     * @return l'activité adaptée dans le modèle
     */
    public Activity getActivity()
    {
        return this.activity;
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Step stepAModifier = this.getStepByRow(rowIndex);
        switch (columnIndex)
        {
            case 1:
                stepAModifier.setName((String) aValue);
                break;
            case 2:
                stepAModifier.setComment((String) aValue);
                break;
            case 3:
                // modif gérée automatiquement par l'éditeur lui-même
                break;

        }
//        this.fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * @param numRow
     *            enlève l'étape numRow de l'activité
     */
    public void RemoveStep(int numRow)
    {
        this.activity.removeStep(this.activity.getSteps().get(numRow));

    }

    /**
     * @param numRow
     *            ligne à passer en tête
     */
    public void moveFirst(int numRow)
    {
        if (this.activity.getSteps().size() > 1)
            this.activity.swapSteps(this.activity.getSteps().get(numRow),
                    this.activity.getSteps().get(0));

    }

    /**
     * @param numRow
     *            ligne à remonter d'un cran
     */
    public void moveUp(int numRow)
    {
        if (this.activity.getSteps().size() > 1 && numRow >= 1)
        {
            this.activity.swapSteps(this.activity.getSteps().get(numRow),
                    this.activity.getSteps().get(numRow - 1));
        }

    }

    /**
     * @param numRow
     *            ligne à descendre d'un cran
     */
    public void moveDown(int numRow)
    {
        if (this.activity.getSteps().size() > 1
                && numRow < this.getRowCount() - 1)
        {
            this.activity.swapSteps(this.activity.getSteps().get(numRow),
                    this.activity.getSteps().get(numRow + 1));
        }
    }

    /**
     * @param numRow
     *            ligne à descendre tout en bas
     */
    public void moveLast(int numRow)
    {
        if (this.activity.getSteps().size() > 1)
        {
            this.activity.swapSteps(this.activity.getSteps().get(numRow),
                    this.activity.getSteps().get(
                            this.activity.getSteps().size() - 1));
        }
    }

    /**
     * faire que le modèle n'adapte plus aucune étape
     */
    public void clear()
    {
        this.activity = null;
        this.fireTableDataChanged() ;
    }
}