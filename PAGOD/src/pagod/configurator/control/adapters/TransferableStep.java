/*
 * $Id: TransferableStep.java,v 1.2 2006/01/25 13:51:40 cyberal82 Exp $
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
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import pagod.common.model.Step;

/**
 * Classe TransferableStep : permet d'encapsuler une step pour la transf?rer
 * notamment par DnD
 * 
 * @author Benjamin
 */
public class TransferableStep implements Transferable
{
    /**
     * Commentaire pour <code>stepFlavor</code>
     */
    public static DataFlavor stepFlavor = null;
    static
    {
        try
        {
            stepFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + "; class=pagod.common.model.Step", "Step");
        }
        catch (Exception e)
        {
            //TODO UTILISER LE MANAGER!
        	System.err.println(e);
        }
    }

    /**
     * l'?tape que l'on encapsule
     */
    private Step step;

    /**
     * @param s
     *            la step ? encapsuler dans le TransferableStep
     */
    public TransferableStep(Step s)
    {
        this.step = s;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     * @return les DataFlavor support?s par le TransferableStep
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] tab = { stepFlavor };
        return tab;
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     * @return true si flavor est support? par TransferableStep
     */
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor == stepFlavor;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     * @return la Step encapsul?e dans ce Transferable
     */
    public Object getTransferData(DataFlavor flavor)
                                                    throws UnsupportedFlavorException,
                                                    IOException
    {
        return this.step;
    }

}
