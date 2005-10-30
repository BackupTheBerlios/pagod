/*
 * $Id: ToolsProductsAssociationsTreeModel.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import pagod.common.model.Process;
import pagod.common.model.Product;
import pagod.common.model.Tool;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author m1isi28
 * 
 * Classe modele de l'arbre d'associations produits - outils
 */
public class ToolsProductsAssociationsTreeModel implements TreeModel
{
    private Process rootProcess;
    
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Constructeur du modele
     * 
     * @param rootProcess
     */
    public ToolsProductsAssociationsTreeModel(Process rootProcess)
    {
        this.rootProcess = rootProcess;
    }

    /**
     * Retourne la racine de l'arbre, en l'occurence, le processus
     * 
     * @return le processus
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot()
    {
        return this.rootProcess;
    }

    /**
     * Retourne le fils situe a l'index index du parent parent
     * 
     * @param parent
     *            le parent de l'objet cherche
     * @param index
     *            l'index de l'enfant dans la descendance du parent
     * @return l'objet recherche
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index)
    {
        Collection elements;
        if (parent instanceof Process)
            elements = ((Process) parent).getTools();
        else if (parent instanceof Tool)
            elements = ((Tool) parent).getProducts();
        else
            return null;
        int i = 0;
        for (Object object : elements)
        {
            if (i == index)
                return object;
            i++;
        }
        return null;
    }

    /**
     * Retourne le nombre d'elements dans la descendance de l'objet passe en
     * parametre
     * 
     * @param parent
     *            l'objet dont on veut connaitre le nombre de fils
     * @return le nombre de fils de l'objet
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent)
    {
        if (parent instanceof Process)
            return ((Process) parent).getTools().size();
        else if (parent instanceof Tool)
            return ((Tool) parent).getProducts().size();
        else
            return 0;
    }

    /**
     * Determine si l'objet est une feuille
     * 
     * @param node
     *            l'objet
     * @return true si c'est une feuille
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node)
    {
        if (node instanceof Product)
            return true;
        return false;
    }

    /**
     * Retourne l'index de l'objet child dans la descendance directe de l'objet
     * parent
     * 
     * @param parent
     *            le parent
     * @param child
     *            le fils
     * @return l'index
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     *      java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child)
    {
        Collection elements = null;
        if (child instanceof Product)
            elements = ((Tool) parent).getProducts();
        else if (child instanceof Tool)
            elements = ((Process) parent).getTools();
        int i = 0;
        for (Object object : elements)
        {
            if (object == child)
                return i;
            i++;
        }
        return 0;
    }

    /**
     * Ajoute le TreeModelListener passe en parametre au modele
     * 
     * @param l
     *            le listener a ajouter
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l)
    {        
        this.listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Supprime le TreeModelListener passe en parametre du modele
     * 
     * @param l
     *            le listener a supprimer
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l)
    {
        this.listenerList.remove(TreeModelListener.class, l);

    }

    /**
     * Ajout d'un produit dans l'arbre ainsi que mise a jour de la couche metier
     * 
     * @param product
     *            le produit
     * @param tool
     *            l'outil
     */
    public void insertProductNode(Product product, Tool tool)
    {
        product.setEditor(tool);
    }

    /**
     * Ajout d'un outil dans l'arbre
     * 
     * @param toolName
     *            le nom de l'outil a ajouter
     * @return true si l'insertion est effective
     */
    public boolean insertTool(String toolName)
    {
        if (this.existToolName(toolName))
            return false;
        Tool tool = new Tool(toolName, null, new ArrayList<Product>());
        this.rootProcess.getTools().add(tool);
        return true;
    }

    /**
     * Permet de verifier si une chaine de caractere est deja utilisee en tant
     * que nom d'un outil
     * 
     * @param toolName
     * @return true si le nom en parametre est un nom de Tool existant
     */
    private boolean existToolName(String toolName)
    {
        for (Tool tool : this.rootProcess.getTools())
        {
            if (tool.getName().equals(toolName))
                return true;
        }
        return false;
    }

    /**
     * Suppression d'un outil dans l'arbre et repercussion sur la couche metier
     * 
     * @param tool
     *            l'outil a supprimer
     */
    public void deleteTool(Tool tool)
    {
        this.rootProcess.getTools().remove(tool);
        ArrayList<Product> products = new ArrayList<Product>(tool.getProducts());
        for (Product product : products)
            product.setEditor(null);
    }

    /**
     * Modification d'un outil dans l'arbre et repercussion sur la couche metier
     * @param oldToolName l'ancien nom de l'outil
     * @param newToolName le nouveau nom de l'outil
     * @return true si la modification s'est bien realisee
     */
    public boolean modifyTool(String oldToolName, String newToolName)
    {
        if (! existToolName(newToolName))
        {
            for (Tool tool : this.rootProcess.getTools())
            {
                if (tool.getName().equals(oldToolName))
                {
                    tool.setName(newToolName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Suppression d'un produit dans l'arbre et repercussion sur la couche
     * metier
     * 
     * @param product
     */
    public void deleteProductNode(Product product)
    {
        product.setEditor(null);
    }

    /**
     * Repercute sur les listeners un evenement de modification de la structure
     * de l'arbre
     */
    public void fireTreeStructureChanged()
    {
        Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        // Notifie les listeners  concernes par l'evenement du dernier au premier
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (treeModelEvent == null)
                    treeModelEvent = new TreeModelEvent(this.rootProcess,
                            new TreePath(this.rootProcess));
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(treeModelEvent);
            }
        }
    }

    /**
     * Retourne le TreePath de l'objet en parametre
     * 
     * @param object
     *            l'objet dont on veut recuperer le path
     * @return le path de l'objet
     */
    public TreePath getPath(Object object)
    {
        if (object == this.rootProcess)
            return new TreePath(this.rootProcess);
        if (object instanceof Product)
        {
            Object[] path = { this.getRoot(), ((Product) object).getEditor(),
                    object };
            return new TreePath(path);
        }
        if (object instanceof Tool)
        {
            Object[] path = { this.getRoot(), object };
            return new TreePath(path);
        }
        return null;
    }

    /**
     * Permet d'assigner une nouvelle valeur a un path (n'est pas implementee)
     * 
     * @param path
     *            le path a modifier
     * @param newValue
     *            la nouvelle valeur a attribuer
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     *      java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        // implementation inutile dans le contexte PAGOD

    }

}
