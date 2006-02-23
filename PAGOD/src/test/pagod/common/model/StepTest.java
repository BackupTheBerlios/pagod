/*
 * $Id: StepTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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

import junit.framework.TestCase;
import java.util.ArrayList;
import pagod.common.model.*;

/**
 * @author Biniou
 */
public class StepTest extends TestCase
{
    /**
     * Step que l'on teste
     */
    protected Step step;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // création d'un step vide pour les tests
        this.step = new Step("", "", "", null);
    }

    /**
     * test le constructeur de Step
     * 
    */
    public void testStep1()
    {
        final String NAME = "Nom du Step";
        final String COMMENT = "Commentaires";

        Product p = new Product("id", "nom", null, null, "descr p", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p);

        Step st = new Step(NAME, COMMENT, list);

        assertEquals(st.getName(), NAME);
        assertEquals(st.getComment(), COMMENT);
        assertEquals(st.getOutputProducts(), list);
        assertTrue(st.getId() != "" && st.getId() != null);

    }

    /**
     * test du constructeur de Step
     * 
     */
    public void testStep2()
    {
        final String ID = "ID Step";
        final String NAME = "Nom du Step";
        final String COMMENT = "Commentaires";

        Product p = new Product("id", "nom", null, null, "descr p", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p);

        Step st = new Step(ID, NAME, COMMENT, list);

        assertEquals(st.getName(), NAME);
        assertEquals(st.getComment(), COMMENT);
        assertEquals(st.getOutputProducts(), list);
        assertEquals(st.getId(), ID);

    }

    /**
     * Test de la méthode hasOutputProducts
     * 
     * @see Step#hasOutputProducts()
     */
    public void testHasOutputProducts()
    {
        // test quand la collection de product est vide
        assertTrue((this.step.getOutputProducts()).isEmpty());

        // test quand la collection contient un product
        Product p = new Product("id", "nom", null, null, "descr p", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p);
        this.step.setOutputProducts(list);
        assertFalse((this.step.getOutputProducts()).isEmpty());

    }

    /**
     * Test de la méthode getOutputProducts
     *
     * @see Step#getOutputProducts()
     */
    public void testGetOutputProducts()
    {
        // test quand la collection de product est vide
        assertTrue((this.step.getOutputProducts()).size() == 0);

        // test quand la collection contient un product
        Product p = new Product("id", "nom", null, null, "descr p", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p);
        this.step.setOutputProducts(list);
        assertTrue((this.step.getOutputProducts()).contains(p)
                && (this.step.getOutputProducts()).size() == 1);

    }

    /**
     * test de la methode getName de Step
     * 
     * @see Step#getName()
     */
    public void testGetName()
    {
        this.step.setName("name1");
        assertSame(this.step.getName(), "name1");

    }

    /**
     * test de la methode toString de Step
     * 
     * @see Step#toString()
     */
    /*
     * Class under test for String toString()
     */
    public void testToString()
    {
        this.step.setName("name1");
        assertEquals(this.step.toString(), "name1");
    }

    /**
     * test de la methode getComment de Step
     * 
     * @see Step#getComment()
     */
    public void testGetComment()
    {
        final String comment = "Commentaire";
        this.step.setComment(comment);
        assertEquals(this.step.getComment(), comment);

    }

    /**
     * test de la methode setComment de Step
     * 
     * @see Step#setComment(String)
     */
    public void testSetComment()
    {
        final String comment = "Commentaire";
        this.step.setComment(comment);
        assertEquals(this.step.getComment(), comment);

    }

    /**
     * test de la methode removeOutputProduct de Step
     * 
     * @see Step#removeOutputProduct(Product)
     */
    public void testRemoveOutputProduct()
    {
        Product p1 = new Product("id1", "nom1", null, null, "descr prod1", null);
        Product p2 = new Product("id2", "nom2", null, null, "descr prod2", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p1);

        // test en supprimant un product qui n'appartient pas a la liste
        this.step.setOutputProducts(list);
        this.step.removeOutputProduct(p2);
        assertTrue((this.step.getOutputProducts()).contains(p1)
                && (this.step.getOutputProducts()).size() == 1);

        // test en supprimant un product qui fait partie de la liste
        ArrayList<Product> list2 = new ArrayList<Product>();
        list2.add(p1);
        list2.add(p2);

        this.step.setOutputProducts(list2);
        this.step.removeOutputProduct(p2);
        assertTrue((this.step.getOutputProducts()).contains(p1)
                && (this.step.getOutputProducts()).size() == 1);

    }

    /**
     * test de la methode addOutputProduct de Step
     * 
     * @see Step#addOutputProduct(Product)
     */
    public void testAddOutputProduct()
    {
        Product p1 = new Product("id1", "nom1", null, null, "descr prod1", null);
        Product p2 = new Product("id2", "nom2", null, null, "descr prod2", null);
        Product p3 = new Product("id3", "nom3", null, null, "descr prod3", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p1);

        // test en ajoutant un product deja existante
        this.step.setOutputProducts(list);
        this.step.addOutputProduct(p1);
        assertTrue((this.step.getOutputProducts()).contains(p1)
                && (this.step.getOutputProducts()).size() == 1);

        // test en ajoutant 2 product différents
        this.step.setOutputProducts(list);
        this.step.addOutputProduct(p2);
        this.step.addOutputProduct(p3);
        assertTrue((this.step.getOutputProducts()).contains(p1)
                && (this.step.getOutputProducts()).size() == 3
                && (this.step.getOutputProducts()).contains(p2)
                && (this.step.getOutputProducts()).contains(p3));

    }

    /**
     * test de la methode setName de Step
     * 
     * @see Step#setName(String)
     */
    public void testSetName()
    {
        this.step.setName("name1");
        assertSame(this.step.getName(), "name1");

    }

    /**
     * test de la methode getId de Step
     * 
     * @see Step#getId()
     */
    public void testGetId()
    {
        Step step2 = new Step("id", "", "", null);
        assertSame(step2.getId(), "id");
    }

    /**
     * test de la methode setOutputProducts de Step
     * 
    */
    public void testSetOutputProducts()
    {
        Product p = new Product("id", "nom", null, null, "descr p", null);
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(p);
        this.step.setOutputProducts(list);
        assertTrue((this.step.getOutputProducts()).contains(p)
                && (this.step.getOutputProducts()).size() == 1);

    }

}
