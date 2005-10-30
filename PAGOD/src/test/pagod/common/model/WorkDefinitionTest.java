/*
 * $Id: WorkDefinitionTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
 * @author Biniou
 */
public class WorkDefinitionTest extends ProcessElementTest
{
    /**
     * wd que l'on teste
     */
    protected WorkDefinition wd;

    /*
     * @see ProcessElementTest#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // création d'un produit vide pour les tests
        this.wd = new WorkDefinition("", "", null, null, null);

        // teste ce produit en tant que ProcessElement
        this.processElement = this.wd;
    }

    /**
     * test du constructeur WorkDefinition
     * 
     */
    public void testWorkDefinition()
    {
        final String ID = "Id WD";
        final String NAME = "Nom WD";
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

        Activity a = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l = new ArrayList<Activity>();
        l.add(a);

        WorkDefinition workdef = new WorkDefinition(ID, NAME, FILE_PATH,
                ICON_PATH, l);

        assertEquals(workdef.getId(), ID);
        assertEquals(workdef.getName(), NAME);
        assertEquals(workdef.getFileURL(), FILE_PATH);
        assertEquals(workdef.getIconURL(), ICON_PATH);
        assertSame(workdef.getActivities(), l);

    }

    /**
     * test de la methode getActivities de WorkDefinition
     * 
     * @see WorkDefinition#getActivities()
     */
    public void testGetActivities()
    {
        Activity a = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l = new ArrayList<Activity>();
        l.add(a);

        this.wd.setActivities(l);
        assertSame(this.wd.getActivities(), l);

    }

    /**
     * test de la methode setActivities de WorkDefinition
     * 
     */
    public void testSetActivities()
    {
        Activity a = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l = new ArrayList<Activity>();
        l.add(a);

        Activity a2 = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l2 = new ArrayList<Activity>();
        l2.add(a2);

        // test en faisant le set a partir d'une wd qui ne contient
        // que null
        this.wd.setActivities(l);
        assertSame(this.wd.getActivities(), l);

        // test en faisant le set a partir d'une wd contenant
        // deja une liste d'activités
        this.wd.setActivities(l);
        this.wd.setActivities(l2);
        assertSame(this.wd.getActivities(), l2);

        // test en faisant un set de null a partir d'une wd
        // contenant deja une liste d'activités
        this.wd.setActivities(l);
        this.wd.setActivities(null);
        assertTrue((this.wd.getActivities()).size() == 0);

    }

    /**
     * test de la methode addActivity de WorkDefinition
     * 
     * @see WorkDefinition#addActivity(Activity)
     */
    public void testAddActivity()
    {
        Activity a = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l = new ArrayList<Activity>();
        l.add(a);

        Activity a2 = new Activity("", "", null, null, null, null, null, null,
                null);

        // test en ajoutant une activité deja existante
        this.wd.setActivities(l);
        this.wd.addActivity(a);
        assertTrue((this.wd.getActivities()).size() == 1);

        // test en ajoutant 2 activités différentes
        this.wd.addActivity(a);
        this.wd.addActivity(a2);
        assertTrue((this.wd.getActivities()).size() == 2
                && (this.wd.getActivities()).contains(a)
                && (this.wd.getActivities()).contains(a2));

    }

    /**
     * test de la methode removeActivity de WorkDefinition
     * 
     * @see WorkDefinition#removeActivity(Activity)
     */
    public void testRemoveActivity()
    {
        Activity a = new Activity("", "", null, null, null, null, null, null,
                null);
        ArrayList<Activity> l = new ArrayList<Activity>();
        l.add(a);

        Activity a2 = new Activity("", "", null, null, null, null, null, null,
                null);

        // test en supprimant une activité qui n'appartient
        // pas a la liste
        this.wd.setActivities(l);
        this.wd.removeActivity(a2);
        assertTrue((this.wd.getActivities()).size() == 1
                && (this.wd.getActivities()).contains(a));

        // test en supprimant une activité qui fait partie de la liste
        this.wd.setActivities(l);
        this.wd.removeActivity(a);
        assertTrue((this.wd.getActivities()).size() == 0);

    }

    /**
     * test de la methode getProcessComponent de WorkDefinition
     * 
     * @see WorkDefinition#getProcessComponent()
     */
    public void testGetProcessComponent()
    {
        ProcessComponent pc = new ProcessComponent("", "", null, null, null);

        this.wd.setProcessComponent(pc);
        assertSame(this.wd.getProcessComponent(), pc);

    }

    /**
     * test de la methode setProcessComponent de WorkDefinition
     * 
     * @see WorkDefinition#setProcessComponent(ProcessComponent)
     */
    public void testSetProcessComponent()
    {
        ProcessComponent pc1 = new ProcessComponent("id1", "nom1", null, null,
                null);
        ProcessComponent pc2 = new ProcessComponent("id2", "nom2", null, null,
                null);

        // test en faisant le set a partir d'une wd qui ne contient
        // que null
        this.wd.setProcessComponent(pc1);
        assertSame(this.wd.getProcessComponent(), pc1);

        // test en faisant le set a partir d'une wd contenant
        // deja un ProcessComponent
        this.wd.setProcessComponent(pc1);
        this.wd.setProcessComponent(pc2);
        assertSame(this.wd.getProcessComponent(), pc2);

    }

}
