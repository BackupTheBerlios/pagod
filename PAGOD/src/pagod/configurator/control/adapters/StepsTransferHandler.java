/*
 * $Id: StepsTransferHandler.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import pagod.common.model.Step;
import pagod.configurator.ui.StepsTable;

/**
 * StepsTransferHandler : gère le Drag ET le Drop d'une TransferableStep
 * 
 * @author Benjamin
 */
public class StepsTransferHandler extends TransferHandler
{
    // The data type exported.
    String mimeType = DataFlavor.javaJVMLocalObjectMimeType
            + ";class=pagod.common.model.Step";

    DataFlavor stepFlavor;

    private boolean changesForegroundColor = true;

    /**
     * créé un StepsTransferHandler
     */
    public StepsTransferHandler()
    {
        // Try to create a DataFlavor for step.
        try
        {
            this.stepFlavor = new DataFlavor(this.mimeType);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gére le drop d'une TransferableStep
     * 
     * @param c
     *            Composant où la TransferableStep a été lâchée (pour nous, une
     *            StepsTable)
     * @param t
     *            Le Transferable lâche (pour nous, une TransferableStep)
     * @return vrai si le Drop a été effectué
     */
    public boolean importData(JComponent c, Transferable t)
    {
        if (hasStepFlavor(t.getTransferDataFlavors()))
        {
            try
            {
                StepsTable target = (StepsTable) c;
                int ligneLachage = target.getSelectedRow();
                Step s = (Step) t.getTransferData(this.stepFlavor);
                ((ActivityStepsTableModel) target.getModel()).getActivity()
                        .removeStep(s);
                ((ActivityStepsTableModel) target.getModel())
                        .fireTableDataChanged();
                ((ActivityStepsTableModel) target.getModel()).getActivity()
                        .addStep(s, ligneLachage);
                // ((ActivityStepsTableModel)
                // target.getModel()).getActivity().removeStep(s);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Does the flavor list have a Step flavor?
     * 
     * @param flavors
     * @return TRUE if the list have a Step flavor, FALSE otherwise
     */
    protected boolean hasStepFlavor(DataFlavor[] flavors)
    {
        if (this.stepFlavor == null)
        {
            return false;
        }

        for (int i = 0 ; i < flavors.length ; i++)
        {
            if (this.stepFlavor.equals(flavors[i]))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Overridden to include a check for a step flavor.
     * 
     * @param c
     * @param flavors
     * @return TRUE if import is possible, FALSE otherwise
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors)
    {
        return this.hasStepFlavor(flavors);
    }

    /**
     * lors du Drag --> renvoyer un TransferableStep à partir de la ligne
     * sélectionnée dans <code>c</code> qui est en fait la StepsTable
     * 
     * @param c
     *            Composant source du drag (pour nous, une StepsTable)
     * @return le TransferableStep de la ligne sélectionnée dans la StepsTable c
     */
    protected Transferable createTransferable(JComponent c)
    {
        StepsTable source = (StepsTable) c;
        Step s = ((ActivityStepsTableModel) source.getModel())
                .getStepByRow(source.getSelectedRow());
        return new TransferableStep(s);
    }

    /**
     * renvoie le type d'actions autorisées (move, copy, ...)
     * 
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    public int getSourceActions(JComponent c)
    {
        return MOVE;
    }

    /**
     * renvoie l'icône affichée durant le drag TODO voir si on met autre chose
     * que le truc par défaut
     * 
     * @see javax.swing.TransferHandler#getVisualRepresentation(java.awt.datatransfer.Transferable)
     */
    public Icon getVisualRepresentation(Transferable t)
    {
        return super.getVisualRepresentation(t);
    }
}