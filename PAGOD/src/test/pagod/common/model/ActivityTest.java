/*
 * $Id: ActivityTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Step;
import pagod.common.model.Tool;
import pagod.common.model.WorkDefinition;

/**
 * Classe de test JUnit de Activity
 * 
 * @author m1isi24
 */
public class ActivityTest extends ProcessElementTest
{
    /** Activité à tester */
    protected Activity activity;
    

    /*
     * Initialise avant les tests
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        // création d'une activité vide pour les tests
        this.activity = new Activity("", "", null, null,
                new ArrayList<Step>(), new WorkDefinition("", "", null, null, 
                        new ArrayList<Activity>()), 
                new ArrayList<Product>(), new ArrayList<Product>(),
                new Role("", "", null, null, "", new ArrayList<Activity>()));
        
        // teste cette activité en tant que ProcessElement
        this.processElement = this.activity;
    }

    /**
     * Teste le constructeur de Activity
     */
    public void testActivity()
    {
        final String ID = "Id Activity";
        final String NAME = "Nom Activity";
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
        
        ArrayList<Step> steps = new ArrayList<Step>();
        steps.add(new Step("Id1 Step", "Nom1 Step", "Contenu1 Step", null));
        steps.add(new Step("Id2 Step", "Nom2 Step", "Contenu2 Step", null));
        
        WorkDefinition wd = new WorkDefinition("Id WorkDefinition", 
                "Nom WorkDefinition", null, null, new ArrayList<Activity>());
        
        ArrayList<Product> pIn = new ArrayList<Product>();
        pIn.add(new Product("Id1 Product", "Nom1 Product", null,
                null, "", new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        pIn.add(new Product("Id2 Product", "Nom2 Product", null,
                null, "", new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));
        
        ArrayList<Product> pOut = new ArrayList<Product>();
        pOut.add(new Product("Id3 Product", "Nom3 Product", null,
                null, "descr prod3", new Tool("Id3 Tool", "Nom3 Tool", "Chemin3 Tool", null)));
        pOut.add(new Product("Id4 Product", "Nom4 Product", null,
                null, "descr prod4", new Tool("Id4 Tool", "Nom4 Tool", "Chemin4 Tool", null)));
        
        Role r = new Role("Id Role", "Nom Role", null, null, "descr role", new ArrayList<Activity>());
        
        Activity a = new Activity(ID, NAME, FILE_PATH, ICON_PATH, steps, wd, 
                pIn, pOut, r);
        
        assertEquals(a.getId(), ID);
        assertEquals(a.getName(), NAME);
        assertEquals(a.getFileURL(), FILE_PATH);
        assertEquals(a.getIconURL(), ICON_PATH);
        
        assertEquals(a.getSteps(), steps);
        assertSame(a.getWorkDefinition(), wd);
        assertEquals(a.getInputProducts(), pIn);
        assertEquals(a.getOutputProducts(), pOut);
        assertSame(a.getRole(), r);
    }

    /**
     * Teste getInputProducts de Activity
     * @see Activity#getInputProducts()
     */
    public void testGetInputProducts()
    {
        ArrayList<Product> pIn = new ArrayList<Product>();
        pIn.add(new Product("Id1 Product", "Nom1 Product", null,
                null, "descr prod1", new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        pIn.add(new Product("Id2 Product", "Nom2 Product", null,
                null, "descr prod2", new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));
        
        this.activity.setInputProducts(pIn);
        assertEquals(this.activity.getInputProducts(), pIn);
    }

    /**
     * Teste setInputProducts de Activity
     */
    public void testSetInputProducts()
    {
        ArrayList<Product> pIn1 = new ArrayList<Product>();
        ArrayList<Product> pIn2 = new ArrayList<Product>();
        pIn2.add(new Product("Id1 Product", "Nom1 Product", null,
                null, "descr prod1", new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        pIn2.add(new Product("Id2 Product", "Nom2 Product", null,
                null, "descr prod2", new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));
        
        this.activity.setInputProducts(pIn1);
        assertEquals(this.activity.getInputProducts(), pIn1);

        this.activity.setInputProducts(pIn2);
        assertEquals(this.activity.getInputProducts(), pIn2);
    }

    /**
     * Teste getOutputProducts de Activity
     * @see Activity#getOutputProducts()
     */
    public void testGetOutputProducts()
    {
        ArrayList<Product> pOut = new ArrayList<Product>();
        pOut.add(new Product("Id3 Product", "Nom3 Product", null,
                null, "descr prod3", new Tool("Id3 Tool", "Nom3 Tool", "Chemin3 Tool", null)));
        pOut.add(new Product("Id4 Product", "Nom4 Product", null,
                null, "descr prod4", new Tool("Id4 Tool", "Nom4 Tool", "Chemin4 Tool", null)));
        
        this.activity.setOutputProducts(pOut);
        assertEquals(this.activity.getOutputProducts(), pOut);
    }

    /**
     * Teste setOutputProducts de Activity
    */
    public void testSetOutputProducts()
    {
        ArrayList<Product> pOut1 = new ArrayList<Product>();
        ArrayList<Product> pOut2 = new ArrayList<Product>();
        pOut2.add(new Product("Id3 Product", "Nom3 Product", null,
                null, "descr prod3", new Tool("Id3 Tool", "Nom3 Tool", "Chemin3 Tool", null)));
        pOut2.add(new Product("Id4 Product", "Nom4 Product", null,
                null, "descr prod4", new Tool("Id4 Tool", "Nom4 Tool", "Chemin4 Tool", null)));
        
        this.activity.setOutputProducts(pOut1);
        assertEquals(this.activity.getOutputProducts(), pOut1);
        
        this.activity.setOutputProducts(pOut2);
        assertEquals(this.activity.getOutputProducts(), pOut2);
    }

    /**
     * Teste getRole de Activity
     * @see Activity#getRole()
     */
    public void testGetRole()
    {
        Role r = new Role("Id Role", "Nom Role", null, 
                null, "descr role", new ArrayList<Activity>());
        
        this.activity.setRole(r);
        assertSame(this.activity.getRole(), r);
    }

    /**
     * Teste setRole de Activity
     * @see Activity#setRole(Role)
     */
    public void testSetRole()
    {
        Role r1 = new Role("", "", null, null, "descr role1", new ArrayList<Activity>());
        Role r2 = new Role("Id Role", "Nom Role", null, 
                null, "descr role2", new ArrayList<Activity>());
        
        this.activity.setRole(r1);
        assertSame(this.activity.getRole(), r1);

        this.activity.setRole(r2);
        assertSame(this.activity.getRole(), r2);
    }

    /**
     * Teste getSteps de Activity
     * @see Activity#getSteps()
     */
    public void testGetSteps()
    {
        ArrayList<Step> steps = new ArrayList<Step>();
        steps.add(new Step("Id1 Step", "Nom1 Step", "Contenu1 Step", null));
        steps.add(new Step("Id2 Step", "Nom2 Step", "Contenu2 Step", null));
        
        this.activity.setSteps(steps);
        assertEquals(this.activity.getSteps(), steps);
    }

    /**
     * Teste setSteps de Activity
     */
    public void testSetSteps()
    {
        ArrayList<Step> steps1 = new ArrayList<Step>();
        ArrayList<Step> steps2 = new ArrayList<Step>();
        steps2.add(new Step("Id1 Step", "Nom1 Step", "Contenu1 Step", null));
        steps2.add(new Step("Id2 Step", "Nom2 Step", "Contenu2 Step", null));
        
        this.activity.setSteps(steps1);
        assertEquals(this.activity.getSteps(), steps1);
        
        this.activity.setSteps(steps2);
        assertEquals(this.activity.getSteps(), steps2);
    }

    /**
     * Teste getWorkDefinition de Activity
     * @see Activity#getWorkDefinition()
     */
    public void testGetWorkDefinition()
    {
        WorkDefinition wd = new WorkDefinition("Id WorkDefinition", 
                "Nom WorkDefinition", null, null,
                new ArrayList<Activity>());
        
        this.activity.setWorkDefinition(wd);
        assertSame(this.activity.getWorkDefinition(), wd);
    }

    /**
     * Teste setWorkDefinition de Activity
     * @see Activity#setWorkDefinition(WorkDefinition)
     */
    public void testSetWorkDefinition()
    {
        WorkDefinition wd1 = new WorkDefinition("", "", null, null,
                new ArrayList<Activity>());
        WorkDefinition wd2 = new WorkDefinition("Id workDefinition", 
                "Nom WorkDefinition", null, null,
                new ArrayList<Activity>());
        
        this.activity.setWorkDefinition(wd1);
        assertSame(this.activity.getWorkDefinition(), wd1);
        
        this.activity.setWorkDefinition(wd2);
        assertSame(this.activity.getWorkDefinition(), wd2);
    }

    /**
     * Teste hasInputProducts de Activity
     * @see Activity#hasInputProducts()
     */
    public void testHasInputProducts()
    {
        ArrayList<Product> pIn1 = new ArrayList<Product>();
        ArrayList<Product> pIn2 = new ArrayList<Product>();
        pIn2.add(new Product("Id1 Product", "Nom1 Product", null,
                null, "descr prod1", new Tool("Id1 Tool", "Nom1 Tool", "Chemin1 Tool", null)));
        pIn2.add(new Product("Id2 Product", "Nom2 Product", null,
                null, "descr prod2", new Tool("Id2 Tool", "Nom2 Tool", "Chemin2 Tool", null)));
        
        this.activity.setInputProducts(pIn1);
        assertFalse(this.activity.hasInputProducts());
        
        this.activity.setInputProducts(pIn2);
        assertTrue(this.activity.hasInputProducts());
    }

    /**
     * Teste hasOutputProducts de Activity
     * @see Activity#hasOutputProducts()
     */
    public void testHasOutputProducts()
    {
        ArrayList<Product> pOut1 = new ArrayList<Product>();
        ArrayList<Product> pOut2 = new ArrayList<Product>();
        pOut2.add(new Product("Id3 Product", "Nom3 Product", null,
                null, "descr prod3", new Tool("Id3 Tool", "Nom3 Tool", "Chemin3 Tool", null)));
        pOut2.add(new Product("Id4 Product", "Nom4 Product", null,
                null, "descr prod4", new Tool("Id4 Tool", "Nom4 Tool", "Chemin4 Tool", null)));
        
        this.activity.setOutputProducts(pOut1);
        assertFalse(this.activity.hasOutputProducts());
        
        this.activity.setOutputProducts(pOut2);
        assertTrue(this.activity.hasOutputProducts());
    }

    /**
     * Teste needsTools de Activity
     * @see Activity#needsTools()
     */
    public void testNeedsTools()
    {
        ArrayList<Product> pOut1 = new ArrayList<Product>();
        ArrayList<Product> pOut2 = new ArrayList<Product>();
        pOut2.add(new Product("Id3 Product", "Nom3 Product", null,
                null, "descr prod3", null));
        ArrayList<Product> pOut3 = new ArrayList<Product>();
        pOut3.add(new Product("Id4 Product", "Nom4 Product", null,
                null, "descr prod4", new Tool("Id Tool", "Nom Tool", "Chemin Tool", null)));
        
        this.activity.setOutputProducts(pOut1);
        assertFalse(this.activity.needsTools());
        
        this.activity.setOutputProducts(pOut2);
        assertFalse(this.activity.needsTools());

        this.activity.setOutputProducts(pOut3);
        assertTrue(this.activity.needsTools());
    }

    /**
     * Teste hasSteps de Activity
     * @see Activity#hasSteps()
     */
    public void testHasSteps()
    {
        ArrayList<Step> steps1 = new ArrayList<Step>();
        ArrayList<Step> steps2 = new ArrayList<Step>();
        steps2.add(new Step("Id1 Step", "Nom1 Step", "Contenu1 Step", null));
        steps2.add(new Step("Id2 Step", "Nom2 Step", "Contenu2 Step", null));
        
        this.activity.setSteps(steps1);
        assertFalse(this.activity.hasSteps());
        
        this.activity.setSteps(steps2);
        assertTrue(this.activity.hasSteps());
    }
}
