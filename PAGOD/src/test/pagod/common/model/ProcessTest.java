/*
 * $Id: ProcessTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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
import pagod.common.model.ProcessComponent;
import pagod.common.model.Product;
import pagod.common.model.Process;
import pagod.common.model.Role;
import pagod.common.model.Tool;
import pagod.common.model.WorkDefinition;

/**
 * @author Biniou
 */
public class ProcessTest extends ProcessElementTest
{
    /**
     * process que l'on teste
     */
    protected Process proc;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // création d'un process vide pour les tests
        this.proc = new Process("", "", null, null);

        // teste ce process en tant que ProcessElement
        this.processElement = this.proc;
    }

    /**
     * test du constructeur Process
     * 
     * @see Process#Process(String, String, URL, URL)
     */
    public void testProcess()
    {
        final String ID = "Process id";
        final String NAME = "Process nom";
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

        Process p = new Process(ID, NAME, FILE_PATH, ICON_PATH);

        assertEquals(p.getId(), ID);
        assertEquals(p.getName(), NAME);
        assertEquals(p.getFileURL(), FILE_PATH);
        assertEquals(p.getIconURL(), ICON_PATH);

    }

    /**
     * test de la methode getRoles de Process
     * 
     * @see Process#getRoles()
     */
    public void testGetRoles()
    {
        // Creation de la liste de Role
        Role r = new Role("id", "nom", null, null, "descr r", null);
        ArrayList<Role> list = new ArrayList<Role>();
        list.add(r);

        // Ajout de la liste de Role dans proc et verification du get
        this.proc.setRoles(list);
        assertSame(this.proc.getRoles(), list);

    }

    /**
     * test de la methode setRoles de Process
    */
    public void testSetRoles()
    {
        // Creation de la liste de Role
        Role r1 = new Role("id", "nom", null, null, "descr r1", null);
        ArrayList<Role> list = new ArrayList<Role>();
        list.add(r1);

        // test si on essaie de rajouter null
        this.proc.setRoles(null);
        assertTrue(this.proc.getRoles().size() == 0);

        // test en rajoutant la liste
        this.proc.setRoles(list);
        assertSame(this.proc.getRoles(), list);

    }

    /**
     * test de la methode addRole de Process
     * 
     * @see Process#addRole(Role)
     */
    public void testAddRole()
    {
        Role r1 = new Role("id", "nom", null, null, "descr r1", null);

        // test si on ajoute une fois r1
        this.proc.addRole(r1);
        assertTrue(this.proc.getRoles().contains(r1)
                && this.proc.getRoles().size() == 1);

        // test si on essaie d'ajouter un role deja present dans la liste
        this.proc.addRole(r1);
        this.proc.addRole(r1);
        assertTrue(this.proc.getRoles().contains(r1)
                && this.proc.getRoles().size() == 1);

    }

    /**
     * test la methode getAllUniqueProcessComponentForRoles de Process
    */
    public void testGetAllUniqueProcessComponentsForRoles()
    {

        // initialisations pour le test
        WorkDefinition wd1 = new WorkDefinition("id", "nom", null, null, null);
        ProcessComponent pc1 = new ProcessComponent("id1", "nom1", null, null,
                null);
        wd1.setProcessComponent(pc1);

        Activity a1 = new Activity("id", "nom", null, null, null, null, null,
                null, null);
        a1.setWorkDefinition(wd1);
        ArrayList<Activity> act1 = new ArrayList<Activity>();
        act1.add(a1);

        Role r1 = new Role("id", "nom", null, null, "descr r1", null);
        Role r3 = new Role("id2", "nom2", null, null, "descr r3", null);

        ArrayList<Role> list_para = new ArrayList<Role>();
        list_para.add(r3);

        r1.setActivities(act1);
        ArrayList<Role> list = new ArrayList<Role>();
        list.add(r1);
        this.proc.setRoles(list);

        // initialisation pour quand 2 meme composant sont presents
        ArrayList<Activity> act2 = new ArrayList<Activity>();
        act2.add(a1);
        act2.add(a1);

        assertTrue(list.contains(r1) && (this.proc.getRoles()).contains(r1));

        // On teste le resultat
        assertTrue(((this.proc.getAllUniqueProcessComponentsForRoles(list))
                .contains(pc1))
                && ((this.proc.getAllUniqueProcessComponentsForRoles(list))
                        .size() == 1));

        // Test quand on a plusieurs fois le meme composant à afficher
        Role r2 = new Role("id", "nom", null, null, "descr r2", act2);
        ArrayList<Role> list2 = new ArrayList<Role>();
        list2.add(r2);
        this.proc.setRoles(list2);

        assertTrue(((this.proc.getAllUniqueProcessComponentsForRoles(list2))
                .contains(pc1))
                && ((this.proc.getAllUniqueProcessComponentsForRoles(list2))
                        .size() == 1));

        // Test quand aucun des roles de la liste passée en parametre
        // n'est present dans le processus
        this.proc.setRoles(list);
        assertTrue((this.proc.getAllUniqueProcessComponentsForRoles(list_para))
                .size() == 0);

    }

    /**
     * test de la methode getAllProducts de Process
     * 
     * @see Process#getAllProducts()
     */
    public void testGetAllProducts()
    {
        Product prod1 = new Product("id1", "nom1", null, null, "descr prod1", null);
        Product prod2 = new Product("id2", "nom2", null, null, "descr prod2", null);

        ArrayList<Product> prod_in = new ArrayList<Product>();
        prod_in.add(prod1);

        ArrayList<Product> prod_out = new ArrayList<Product>();
        prod_out.add(prod2);

        // Test en ayant des produits en entree et sortie differents
        Activity a1 = new Activity("id", "nom", null, null, null, null,
                prod_in, prod_out, null);
        ArrayList<Activity> act1 = new ArrayList<Activity>();
        act1.add(a1);

        Role r1 = new Role("id", "nom", null, null, "descr r1", act1);
        ArrayList<Role> list = new ArrayList<Role>();
        list.add(r1);
        this.proc.setRoles(list);

        assertTrue((this.proc.getAllProducts()).contains(prod1)
                && (this.proc.getAllProducts()).contains(prod2)
                && (this.proc.getAllProducts()).size() == 2);

        // Test si le meme produit est a la fois en entree et en sortie
        // le resultat ne doit le contenir qu'une seule fois

        Activity a2 = new Activity("id", "nom", null, null, null, null,
                prod_in, prod_in, null);
        ArrayList<Activity> act2 = new ArrayList<Activity>();
        act2.add(a2);

        Role r2 = new Role("id", "nom", null, null, "descr r2", act2);
        ArrayList<Role> list2 = new ArrayList<Role>();
        list2.add(r2);
        this.proc.setRoles(list2);

        assertTrue((this.proc.getAllProducts()).contains(prod1)
                && (this.proc.getAllProducts()).size() == 1);

    }

    /**
     * test de la methode getUsedTools de Process
     * 
     * @see Process#getUsedTools()
     */
    public void testGetUsedTools()
    {
        // initialisations
        Product prod1 = new Product("id1", "nom1", null, null, "descr prod1", null);
        ArrayList<Product> prod_in = new ArrayList<Product>();
        prod_in.add(prod1);

        Tool tool1 = new Tool("name", "path", null);
        Product prod2 = new Product("id2", "nom2", null, null, "descr prod2", tool1);
        ArrayList<Product> prod_out = new ArrayList<Product>();
        prod_out.add(prod2);

        // Test quand les tool sont nuls
        Activity a1 = new Activity("id", "nom", null, null, null, null,
                prod_in, null, null);
        ArrayList<Activity> act1 = new ArrayList<Activity>();
        act1.add(a1);

        Role r1 = new Role("id", "nom", null, null, "descr r1", act1);
        ArrayList<Role> liste = new ArrayList<Role>();
        liste.add(r1);
        this.proc.setRoles(liste);

        assertTrue((this.proc.getUsedTools()).size() == 0);

        // test avec un tool a ajouter
        Activity a2 = new Activity("id", "nom", null, null, null, null,
                prod_in, prod_out, null);
        ArrayList<Activity> act2 = new ArrayList<Activity>();
        act2.add(a2);

        Role r2 = new Role("id", "nom", null, null, "descr r2", act2);
        ArrayList<Role> list2 = new ArrayList<Role>();
        list2.add(r2);
        this.proc.setRoles(list2);

        assertTrue((this.proc.getUsedTools()).contains(tool1)
                && (this.proc.getUsedTools()).size() == 1);

        // test quand 2 tool identiques sont presents
        // le resultat ne doit en contenir qu'un

        Activity a3 = new Activity("id", "nom", null, null, null, null,
                prod_out, prod_out, null);
        ArrayList<Activity> act3 = new ArrayList<Activity>();
        act3.add(a3);

        Role r3 = new Role("id", "nom", null, null, "descr r3", act3);
        ArrayList<Role> list3 = new ArrayList<Role>();
        list3.add(r3);
        this.proc.setRoles(list3);

        assertTrue((this.proc.getUsedTools()).contains(tool1)
                && (this.proc.getUsedTools()).size() == 1);

    }

    /**
     * test de la methode getTools de Process
     * 
     * @see Process#getTools()
     */
    public void testGetTools()
    {
        Tool tool1 = new Tool("name1", "path1", null);
        Tool tool2 = new Tool("name2", "path2", null);
        ArrayList<Tool> list = new ArrayList<Tool>();
        list.add(tool1);
        list.add(tool2);

        this.proc.setTools(list);
        assertEquals(this.proc.getTools(), list);
        assertTrue((this.proc.getTools()).contains(tool1)
                && (this.proc.getTools()).contains(tool2)
                && (this.proc.getTools()).size() == 2);

    }

    /**
     * test de la methode setTools de Process
     * 
    */
    public void testSetTools()
    {
        Tool tool1 = new Tool("name1", "path1", null);
        Tool tool2 = new Tool("name2", "path2", null);
        ArrayList<Tool> list = new ArrayList<Tool>();
        list.add(tool1);
        list.add(tool2);

        this.proc.setTools(list);
        assertEquals(this.proc.getTools(), list);
        assertTrue((this.proc.getTools()).contains(tool1)
                && (this.proc.getTools()).contains(tool2)
                && (this.proc.getTools()).size() == 2);

        // test si on essaie de rajouter null
        this.proc.setTools(null);
        assertTrue((this.proc.getTools()).size() == 0);

    }

}
