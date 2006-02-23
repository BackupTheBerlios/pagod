/*
 * $Id: ProductTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pagod.common.model.*;

/**
 * @author Benjamin
 * 
 */
public class ProductTest extends ProcessElementTest
{
    /**
     * produit que l'on teste
     */
    protected Product product;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // création d'un produit vide pour les tests
        this.product = new Product("", "", null, null, "descr r1", new Tool("", "", "",
                new ArrayList<Product>()));

        // teste ce produit en tant que ProcessElement
        this.processElement = this.product;
    }

    /**
     * test du constructeur Product
     */
    public void testProduct()
    {
        final String ID = "Id Product";
        final String NAME = "Nom Product";
        URL FILE_PATH = null;
        URL ICON_PATH = null;
        try
        {
            FILE_PATH = new URL("file://");
            ICON_PATH = new URL("http://java.sun.com/");
        }
        catch (MalformedURLException mue)
        {
            fail("URL de test invalide: " + mue);
        }

        Tool t = new Tool("id2", "name2", "path2", new ArrayList<Product>());

        Product p = new Product(ID, NAME, FILE_PATH, ICON_PATH, "descr p", t);

        assertEquals(p.getId(), ID);
        assertEquals(p.getName(), NAME);
        assertEquals(p.getFileURL(), FILE_PATH);
        assertEquals(p.getIconURL(), ICON_PATH);
        assertSame(p.getEditor(), t);
    }

    /**
     * test de la méthode getEditor
     */
    public void testGetEditor()
    {
        Tool t = new Tool("", "", "", null);
        this.product.setEditor(t);
        assertSame(this.product.getEditor(), t);

        Tool test = new Tool("test", "test", "test", null);
        this.product.setEditor(test);
        assertSame(this.product.getEditor(), test);
    }

    /**
     * test de la méthode setEditor
     */
    public void testSetEditor()
    {
        Tool t1 = new Tool("id1", "name1", "path1", null);
        Tool t2 = new Tool("id2", "name2", "path2", null);

        // test en initialisant le tool
        this.product.setEditor(t1);
        assertSame(this.product.getEditor(), t1);

        // test en essayant de rajouter null
        this.product.setEditor(null);
        assertSame(this.product.getEditor(), null);

        // test en modifiant un tool deja existant
        this.product.setEditor(t1);
        this.product.setEditor(t2);
        assertSame(this.product.getEditor(), t2);

    }

}
