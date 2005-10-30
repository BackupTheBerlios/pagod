/*
 * $Id: ToolTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * SPWIZ - Spem Wizard
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of SPWIZ.
 *
 * SPWIZ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SPWIZ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SPWIZ; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package test.pagod.common.model; 

import java.util.ArrayList;

import pagod.common.model.Product;
import pagod.common.model.Tool;
import junit.framework.TestCase;

/**
 * @author Benjamin
 */
public class ToolTest extends TestCase
{

    /**
     * tool que l'on teste
     */
    protected Tool tool;

    /*
     * @see TestCase#setUp()
     */

    protected void setUp() throws Exception
    {
        super.setUp();
        // création d'un tool vide pour les tests
        this.tool = new Tool("", "", "", new ArrayList<Product>());
    }

    /**
     * teste le constructeur de Tool
     */
    public void testTool()
    {
       final String id1 = "Tool ID";
       final String name1 = "Tool name" ;
       final String path1 = "Tool path" ;
       
       ArrayList<Product> p1 = new ArrayList<Product>();
       p1.add(new Product("Id1 Product", "Nom1 Product", null,
               null, new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
       
       Tool t1 = new Tool(id1,name1,path1,p1) ;
        
        assertEquals(t1.getId(),id1) ;
        assertEquals(t1.getName(),name1) ;
        assertEquals(t1.getPath(),path1) ;
        assertEquals(t1.getProducts(),p1) ;
        
    }

    /**
     * test de la methode getName de Tool
     * @see Tool#getName()
     */
    public void testGetName()
    {
        
        String n1 = "name1";
                
        this.tool.setName(n1);
        assertEquals(this.tool.getName(),n1);
        
    }

    /**
     * test de la methode setName de Tool
     * @see Tool#setName(String)
     */
    public void testSetName()
    {
        String n1 = "name";
        this.tool.setName(n1);
        assertEquals(this.tool.getName(),n1);
        
    }

    /**
     * test de la methode getPath de Tool
     * @see Tool#getPath()
     */
    public void testGetPath()
    {
        String path1 = "path";
        this.tool.setPath(path1);
        assertEquals(this.tool.getPath(),path1);
        
    }

    /**
     * test de la methode setPath de Tool
     * @see Tool#setPath(String)
     */
    public void testSetPath()
    {
        String path1 = "path";
        this.tool.setPath(path1);
        assertEquals(this.tool.getPath(),path1);
        
    }

    /*
     * Class under test for String toString()
     */
    /**
     * test de la methode toString de Tool
     * @see Tool#toString()
     */
    public void testToString()
    {
        String n1 = "name1";
        
        this.tool.setName(n1);
        assertEquals(this.tool.getName(),n1);
        
    }

    /**
     * test de la methode getId de Tool
     * @see Tool#getId()
     */
    public void testGetId()
    {
    }

    /**
     * test de la methode getProducts de Tool
     * @see Tool#getProducts()
     */
    public void testGetProducts()
    {
        /**
         *  creation des listes de product pour le test
         */
        ArrayList<Product> p1 = new ArrayList<Product>();
        p1.add(new Product("Id1 Product", "Nom1 Product", null,
                null, new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        p1.add(new Product("Id2 Product", "Nom2 Product", null,
                null, new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));

        this.tool.setProducts(p1);
        assertSame(this.tool.getProducts(),p1) ;
        
    }

    /**
     * teste la methode setProduct de Tool
    */
    public void testSetProducts()
    {
        ArrayList<Product> p1 = new ArrayList<Product>();
        p1.add(new Product("Id1 Product", "Nom1 Product", null,
                null, new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        p1.add(new Product("Id2 Product", "Nom2 Product", null,
                null, new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));
        
        /**
         * test de l'ajout d'une liste de produit a partir d'un tool possedant une 
         * liste vide de product
         */
        
        this.tool.setProducts(p1);
        assertSame(this.tool.getProducts(),p1) ;
        
        /**
         * test si on met la liste de product a null
         */
        this.tool.setProducts(null);
        assertTrue(this.tool.getProducts().size()==0) ;
        
    }

    /**
     * test de la methode addProduct de Tool
     * @see Tool#addProduct(Product)
     */
    public void testAddProduct()
    {
        
        
        // produit qu'on va rajouter a la liste
        Product prod = new Product("Id1 Product", "Nom1 Product", null,
                null, new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null));
        
        
        
        // teste l'ajout d'un produit qui n'existe pas deja dans la liste
        this.tool.addProduct(prod) ;
      
        
        assertTrue(this.tool.getProducts().contains(prod) && this.tool.getProducts().size()==1);
        
        
        // teste l'ajout d'un produit existant deja dans la liste en ajoutant 
        // 2 fois le meme produit
        
        this.tool.addProduct(prod);
        this.tool.addProduct(prod);
        assertTrue(this.tool.getProducts().contains(prod) && this.tool.getProducts().size()==1);
               
    }

    /**
     * test de la methode removeProduct de Tool
     * @see Tool#removeProduct(Product)
     */
    public void testRemoveProduct()
    {
        // creation de l'arraylist pour faire le set
        // tout en gardant le product seul pour pouvoir se servir de remove
        ArrayList<Product> p1 = new ArrayList<Product>();
        Product prod = new Product("Id1 Product", "Nom1 Product", null,
                null, new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null));
        p1.add(prod);
        
        // on ajoute un product
        this.tool.setProducts(p1);
        
        // on le supprime
        this.tool.removeProduct(prod);
              
        assertTrue(this.tool.getProducts().size()==0);
        
    }

}
