/*
 * $Id: Tool.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
 * Outils (logiciel)
 * 
 * @author MoOky
 */
public class Tool
{
    /**
     * Id de l'outil
     */
    private String id;

    /**
     * nom de l'outil
     */
    private String name;

    /**
     * Chemin d'accès à l'outil
     */
    private String path;

    /**
     * Les produits utilisant cet outil
     */
    private List<Product> products = new ArrayList<Product>();

    /**
     * Constructeur d'un outil avec génération automatique d'identifiant
     * 
     * @param name
     *            Nom de l'outil
     * @param path
     *            Chemin de l'outil
     * @param products
     *            Produits utilisant cet outil
     */
    public Tool(String name, String path, List<Product> products)
    {
        this(null, name, path, products);
    }

    /**
     * Constructeur complet d'un outil
     * 
     * @param id
     *            Identifiant de l'outil (null ou "" pour génération
     *            automatique)
     * @param name
     *            Nom de l'outil
     * @param path
     *            Chemin de l'outil
     * @param products
     *            Produits utilisant cet outil
     */
    public Tool(String id, String name, String path,
                List<Product> products)
    {
        // génération de l'identifiant de l'outil si demandé
        if (id == null || id.length() == 0)
        {
            // récupération du générateur aléatoire
            final UniqueIdentifierGenerator generator = UniqueIdentifierGenerator
                    .getInstance();

            id = generator.generateUniqueIdentifier("_tool_");
        }
        this.id = id;
        this.name = name;
        this.path = path;
        this.setProducts(products);
    }

    /**
     * Retourne l'attribut name
     * 
     * @return name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Initialise l'attribut name
     * 
     * @param name
     *            la valeur a attribuer.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Retourne l'attribut path
     * 
     * @return path.
     */
    public String getPath()
    {
        return this.path;
    }

    /**
     * Initialise l'attribut path
     * 
     * @param path
     *            la valeur a attribuer.
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Redefinition de la methode toString
     * 
     * @return le nom
     */
    public String toString()
    {
        return this.name;
    }

    /**
     * Retourne l'identifiant de l'outil
     * 
     * @return identifiant de l'outil
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return Les produits utilisant cet outil
     */
    public List<Product> getProducts()
    {
        return this.products;
    }

    /**
     * Définit la liste des produits utilisant cet outil
     * 
     * @param products
     *            Liste des produits utilisant cet outil
     */
    public void setProducts(List<Product> products)
    {
        if (this.products == products)
            return;

        final List<Product> oldProducts = this.products;

        // blocage de la récursivité
        this.products = new ArrayList<Product>();

        // suppression des anciennes liaisons
        for (Product p : oldProducts)
        {
            p.setEditor(null);
        }

        // création des nouvelles liaisons
        if (products != null)
        {
            this.products = products;

            for (Product p : products)
            {
                p.setEditor(this);
            }
        }
    }

    /**
     * Ajoute le produit spécifiée à cet outil
     * 
     * @param product
     *            Produit à ajouter
     */
    public void addProduct(Product product)
    {
        if (!this.products.contains(product))
        {
            this.products.add(product);

            product.setEditor(this);
        }
    }

    /**
     * Supprime le produit de cet outil
     * 
     * @param product
     *            Produit à supprimer
     */
    public void removeProduct(Product product)
    {
        if (this.products.remove(product))
        {
            product.setEditor(null);
        }
    }
}
