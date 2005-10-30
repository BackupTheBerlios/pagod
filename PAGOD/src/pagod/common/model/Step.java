/*
 * $Id: Step.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.common.model;

import java.util.ArrayList;
import java.util.List;

import pagod.utils.UniqueIdentifierGenerator;

/**
 * Etape d'une activit� (d�composition d'un activit�)
 * 
 * @author MoOky
 */
public class Step
{
    /**
     * Constructeur avec g�n�ration automatique de l'identifiant
     * 
     * @param name
     *            Nom de l'�tape
     * @param comment
     *            Contenu de l'�tape
     * @param outputProducts
     *            Produits en sortie de l'�tape
     */
    public Step(String name, String comment, List<Product> outputProducts)
    {
        this(null, name, comment, outputProducts);
    }

    /**
     * Constructeur complet
     * 
     * @param id
     *            Identifiant de l'�tape (null ou "" pour g�n�ration
     *            automatique)
     * @param name
     *            Nom de l'�tape
     * @param comment
     *            Contenu de l'�tape
     * @param outputProducts
     *            Produits en sortie de l'�tape
     */
    public Step(String id, String name, String comment,
                List<Product> outputProducts)
    {
        // g�n�ration de l'identifiant de l'�tape si demand�e
        if (id == null || id.length() == 0)
        {
            // r�cup�ration du g�n�rateur
            final UniqueIdentifierGenerator generator = UniqueIdentifierGenerator
                    .getInstance();

            // g�n�ration de l'identifiant
            id = generator.generateUniqueIdentifier("_step_");
        }
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.setOutputProducts(outputProducts);
    }

    /**
     * Nom de l'�tape
     */
    private String name;

    /**
     * Commentaire, conseil pour l�tape
     */
    private String comment;

    /**
     * Identifiant de l'�tape
     */
    private String id;

    /**
     * Liste des produits en sortie
     */
    private List<Product> outputProducts = new ArrayList<Product>();

    /**
     * Pour savoir s'il y a des produits en sortie
     * 
     * @return true si l'activit� a des produits en sortie
     */
    public boolean hasOutputProducts()
    {
        return !this.outputProducts.isEmpty();
    }

    /**
     * @return retourne la liste des produits en sorties
     */
    public List<Product> getOutputProducts()
    {
        return this.outputProducts;
    }

    /**
     * @return retourne le nom de l'�tape
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Redefinition de toString
     * 
     * @return le nom
     */
    public String toString()
    {
        return this.name;
    }

    /**
     * @return Retourne le desctiptif de l'�tape
     */
    public String getComment()
    {
        return this.comment;
    }

    /**
     * @param comment
     *            le descriptif de l'�tape
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Initialise l'attribut outputProducts
     * 
     * @param outputProducts
     *            la valeur a attribuer.
     */
    public void setOutputProducts(List<Product> outputProducts)
    {
        this.outputProducts = (outputProducts != null ? outputProducts
                : new ArrayList<Product>());
    }

    /**
     * @param product
     *            produit � supprimer de la liste de produits en sortie (sans
     *            effet si produit pas dans la liste)
     */
    public void removeOutputProduct(Product product)
    {
        this.outputProducts.remove(product);
    }

    /**
     * @param product
     *            produit � ajouter ds la liste de produits en sortie (sans
     *            effet si produit d�j� pr�sent)
     */
    public void addOutputProduct(Product product)
    {
        if (!this.outputProducts.contains(product))
        {
            this.outputProducts.add(product);
        }
    }

    /**
     * @param name
     *            Valeur � donner � name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Retourne l'identifiant de l'�tape
     * 
     * @return Identifiant de l'�tape
     */
    public String getId()
    {
        return this.id;
    }
}
