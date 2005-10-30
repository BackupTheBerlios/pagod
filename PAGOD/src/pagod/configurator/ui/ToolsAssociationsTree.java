/*
 * $Id: ToolsAssociationsTree.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.Point;
import java.awt.datatransfer.Transferable;
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
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import pagod.common.model.Product;
import pagod.common.model.Tool;
import pagod.configurator.control.adapters.ToolsProductsAssociationsTreeModel;
import pagod.utils.ExceptionManager;

/**
 * @author pierrot
 * 
 * Arbre permettant de realiser les associations entre les outils et les
 * produits
 */
public class ToolsAssociationsTree extends JTree implements
        DragGestureListener, DragSourceListener, DropTargetListener
{

    private DragSource dragSource = null;
    private Product draggedObject;
    private boolean isDropTarget = false;

    
    
    /**
     * Constructeur de l'arbre. Mise en place du drag and drop
     * @param model
     */
    public ToolsAssociationsTree(TreeModel model)
    {
        super(model);
        this.dragSource = new DragSource();
        this.dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
    }
    
    /**
     * Invoquee lorsque un mouvement de drag a ete initie
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    public void dragGestureRecognized(DragGestureEvent dge)
    {
        TreePath path = this.getSelectionPath(); 
        if (path != null) {
            if (path.getLastPathComponent() instanceof Product)
            {
                this.draggedObject = (Product) path.getLastPathComponent();
                this.dragSource.startDrag(dge, DragSource.DefaultMoveDrop , null, new Point(0,0), new TransferableObject(this.draggedObject), this);
            }
        }

    }

    /**
     * Invoquee lorsque ue operation de drag s'amorce sur une plateforme de drop
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dsde)
    {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
    }

    /**
     * Invoquee lorsque le curseur de drag evolue sur une plateforme de drop
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dsde)
    {  
        this.isDropTarget = false;
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);   
    }

    /**
     * Invoquee lorsque l'utilisateur a modifie les conditions de drop
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(DragSourceDragEvent dsde)
    {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
    }

    /**
     * Invoquee lorsque le curseur sort d'une plateforme de drop
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dse)
    {
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    /**
     * Invoquee lorsque l'operation de dnd est achevee
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        if (dsde.getDropSuccess()) {
            if (!this.isDropTarget)
            {
                //on rafraichit l'affichage en conservant les noeuds etendus
                ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
                for (int i = 0; i < ((ToolsProductsAssociationsTreeModel) this.getModel()).getChildCount(this.getModel().getRoot()); i++)
                {
                    Tool tool = (Tool) this.getModel().getChild(this.getModel().getRoot(), i);
                    TreePath treePath = ((ToolsProductsAssociationsTreeModel) this.getModel()).getPath(tool);
                    if (this.isExpanded(treePath))
                    {
                        expandedPaths.add(treePath);
                    }
                }
                ((ToolsProductsAssociationsTreeModel)this.getModel()).fireTreeStructureChanged();
                for (TreePath treePath : expandedPaths)
                {
                    this.scrollPathToVisible(treePath);
                    this.expandPath(treePath);
                }
            }
            else
                this.isDropTarget = false;
        }
    }


    /**
     * Invoque losqu'on dragge hors du site de drop
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent dtde)
    {        
        if (canPerformAction(this.draggedObject)) {
            dtde.acceptDrag(DnDConstants.ACTION_MOVE);            
        }
        else {
            dtde.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
        }
        

    }

    /**
     * Invoquee lorsque une operation de drag est en cours
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(DropTargetDragEvent dtde)
    {
        if (canPerformAction(this.draggedObject)) {
            dtde.acceptDrag(DnDConstants.ACTION_MOVE);            
        }
        else {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        }
    }

    /**
     * Invoquee si les parametres de drop sont modifies
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
        if (canPerformAction(this.draggedObject)) {
            dtde.acceptDrag(DnDConstants.ACTION_MOVE);            
        }
        else {
            dtde.rejectDrag();
        }
    }

    /**
     * Invoquee lorsque le curseur se positionne et effectue une operation de drop sur le composant associe
     * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
     */
    public void dragExit(DropTargetEvent arg0)
    {
    }

    /**
     * 
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent dtde)
    {
        this.isDropTarget = true;
        try {
            Transferable transferable = dtde.getTransferable();
            Point pt = dtde.getLocation();     
            if (transferable.isDataFlavorSupported(TransferableObject.PRODUCT_FLAVOR))
            {
                TreePath pathTarget = this.getPathForLocation(pt.x, pt.y);
                this.draggedObject = (Product) transferable.getTransferData(TransferableObject.PRODUCT_FLAVOR);
                if (pathTarget != null)
                {
                    Object object =pathTarget.getLastPathComponent();
                    Tool newToolAssociated = null;
                
                    if (object instanceof Tool)
                        newToolAssociated = (Tool) object;
                    else if (object instanceof Product)
                        newToolAssociated = ((Product)object).getEditor();
                    if (executeDrop(this.draggedObject, newToolAssociated)) {
                        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                        dtde.getDropTargetContext().dropComplete(true);
                        this.getDropTarget().getDropTargetContext().dropComplete(true);
                        //on rafraichit l'affichage en conservant les noeuds etendus
                        ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
                        for (int i = 0; i < ((ToolsProductsAssociationsTreeModel) this.getModel()).getChildCount(this.getModel().getRoot()); i++)
                        {
                            Tool tool = (Tool) this.getModel().getChild(this.getModel().getRoot(), i);
                            TreePath treePath = ((ToolsProductsAssociationsTreeModel) this.getModel()).getPath(tool);
                            if (this.isExpanded(treePath))
                                expandedPaths.add(treePath);
                        }
                        ((ToolsProductsAssociationsTreeModel)this.getModel()).fireTreeStructureChanged();
                        for (TreePath treePath : expandedPaths)
                        {
                            this.scrollPathToVisible(treePath);
                            this.expandPath(treePath);
                        }
                        this.setSelectionPath(((ToolsProductsAssociationsTreeModel) this.getModel()).getPath(this.draggedObject));
                        return;                 
                    }
                }
            }
            dtde.rejectDrop();
            dtde.dropComplete(false);
        }       
        catch (Exception e) {   
            dtde.rejectDrop();
            dtde.dropComplete(false);
            ExceptionManager.getInstance().manage(e);
        } 
    }
    
    /**
     * Verifie que l'objet dragge soit bien un produit
     * @param theDraggedObject l'objet dragge
     * @return true si c'est un produit
     */
    public boolean canPerformAction(Object theDraggedObject) {
        if (theDraggedObject instanceof Product)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Appelee lors du drop
     * @param theDraggedProduct l'objet dragge
     * @param newToolAssociated le nouveau pere dans l'arbre
     * @return true si le drop s'est bien realise
     */
    public boolean executeDrop(Object theDraggedProduct, Tool newToolAssociated) { 
        
        if (theDraggedProduct instanceof Product)
        {
            ((ToolsProductsAssociationsTreeModel)this.getModel()).insertProductNode((Product)theDraggedProduct,newToolAssociated);
        }
        
        TreePath treePath = ((ToolsProductsAssociationsTreeModel) this.getModel()).getPath(theDraggedProduct);
        this.scrollPathToVisible(treePath);
        this.setSelectionPath(treePath);
        return(true);
    }
    
    /**
     * Permet d'etendre tous les noeuds de l'arbre
     *
     */
    private void expandAllNodes()
    {
        int row = 0;
        while (row < this.getRowCount())
        {
            this.expandRow(row);
            row++;
        }
    }
}
