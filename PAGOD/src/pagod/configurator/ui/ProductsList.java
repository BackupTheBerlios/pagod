/*
 * $Id: ProductsList.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JList;

import pagod.common.model.Product;
import pagod.configurator.control.adapters.ProductsListModel;
import pagod.utils.ExceptionManager;

/**
 * @author pierrot
 * 
 * Liste des produits a associer aux outils dans le ToolsAssociationPanel
 */
public class ProductsList extends JList implements DragGestureListener,
        DragSourceListener, DropTargetListener
{
    private DragSource dragSource = null;

    private boolean isSource = false;

    private Product productDragged;

    /**
     * Constructeur de la liste. Mise en place du drag and drop
     * 
     * @param model
     */
    public ProductsList(ProductsListModel model)
    {
        super(model);
        this.dragSource = new DragSource();
        this.dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    /**
     * Invoquee lorsque un mouvement de drag a ete initie
     * 
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    public void dragGestureRecognized(DragGestureEvent event)
    {
        this.productDragged = (Product) this.getSelectedValue();
        if (this.productDragged != null)
            this.dragSource.startDrag(event, DragSource.DefaultMoveDrop,
                    new TransferableObject(this.productDragged), this);
    }

    /**
     * Invoquee lorsque ue operation de drag s'amorce sur une plateforme de drop
     * 
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent arg0)
    {
    }

    /**
     * Invoquee lorsque le curseur de drag evolue sur une plateforme de drop
     * 
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dsde)
    {
        if (dsde.getDragSourceContext().getDragSource() == this.dragSource)
            this.isSource = true;
        else
            this.isSource = false;
    }

    /**
     * Invoquee lorsque l'utilisateur a modifie les conditions de drop
     * 
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(DragSourceDragEvent arg0)
    {
    }

    /**
     * Invoquee lorsque le curseur sort d'une plateforme de drop
     * 
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dse)
    {
    }

    /**
     * Invoquee lorsque l'operation de dnd est achevee
     * 
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(DragSourceDropEvent event)
    {
    }

    /**
     * Invoque losqu'on dragge hors du site de drop
     * 
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent event)
    {
    }

    /**
     * Invoquee lorsque une operation de drag est en cours
     * 
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(DropTargetDragEvent dtde)
    {
        if (!this.isSource)
        {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        }
        else
        {
            this.isSource = false;
            dtde.rejectDrag();
        }

    }

    /**
     * Invoquee si les parametres de drop sont modifies
     * 
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent arg0)
    {
    }

    /**
     * Invoquee lorsque le curseur se positionne et effectue une operation de drop sur le composant associe
     * 
     * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
     */
    public void dragExit(DropTargetEvent arg0)
    {
    }

    /**
     * Une operation drop est arrivee
     * 
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent event)
    {   
        try
        {
            Transferable transferable = event.getTransferable();

            if (transferable
                    .isDataFlavorSupported(TransferableObject.PRODUCT_FLAVOR))
            {
                this.productDragged = (Product) transferable
                        .getTransferData(TransferableObject.PRODUCT_FLAVOR);
                event.acceptDrop(DnDConstants.ACTION_MOVE);
                this.productDragged.setEditor(null);
                this.getDropTarget().getDropTargetContext().dropComplete(true);
                return;
            }
            event.rejectDrop();
            event.dropComplete(false);
        }
        catch (IOException exception)
        {
            event.rejectDrop();
            event.dropComplete(false);    
            ExceptionManager.getInstance().manage(exception);
        }
        catch (UnsupportedFlavorException e)
        {
            event.rejectDrop();
            event.dropComplete(false);
            ExceptionManager.getInstance().manage(e);
        }

    }
}
