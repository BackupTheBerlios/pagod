/*
 * $Id: ProductsListModel.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import pagod.common.model.Process;
import pagod.common.model.Product;

/**
 * Classe modele de la liste de produits du ToolsAssociationsPanel
 * @author pierrot
 */
public class ProductsListModel extends AbstractListModel
{

    private Process process;    
    
    /**
     * Constructeur du ProductListModel
     * @param process
     */
    public ProductsListModel(Process process)
    {
        super();
        this.process = process;
    }
    
    /**
     * Retourne la taille de la liste
     * @return la taille de la liste
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {
        try
        {
            return this.process.getAllProducts().size();
        }
        catch (NullPointerException e)
        {
            return 0;
        }
    }

    /**
     * Retourne l'element a l'indice designe par le parametre
     * @param index l'indice du parametre
     * @return l'objet recherche
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index)
    {
        Collection<Product> colProducts = this.process.getAllProducts();
        Iterator iterator = colProducts.iterator();
        Product product = null;
        for (int i = 0; i <= index && iterator.hasNext(); i++)
            product = (Product)iterator.next();
        return product;
    }
    
    /**
     * Retourne l'index de l'element product dans la liste
     * @param product le produit dont on veut l'index
     * @return l'index de l'element recherche
     */
    public int getIndex(Product product)
    {
        Collection<Product> colProducts = this.process.getAllProducts();
        Iterator iterator = colProducts.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            if (product == (Product)iterator.next())
                return i;
        }
        return 0;
    }

    /**
     * Notifie au modele une modification de la structure de la liste
     * @param source l'objet a l'origine de la modification (this le plus souvent)
     * @param index0 l'index de depart de la modification
     * @param index1 l'index de fin de la modification
     * @see javax.swing.AbstractListModel#fireContentsChanged(java.lang.Object, int, int)
     */
    public void fireContentsChanged(Object source, int index0, int index1)
    {
        super.fireContentsChanged(source, index0, index1);
    }
}
