/*
 * $Id: RoleTest.java,v 1.2 2006/02/23 01:43:15 psyko Exp $
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
public class RoleTest extends ProcessElementTest
{
    /**
     * role que l'on teste
     */
    protected Role role;

    /*
     * @see ProcessElementTest#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // création d'un produit vide pour les tests
        this.role = new Role("", "", null, null, "descr role", null);

        // teste ce produit en tant que ProcessElement
        this.processElement = this.role;
    }

    /**
     * test du constructeur Role
      */
    public void testRole()
    {
        final String ID = "Id Role";
        final String NAME = "Nom Role";
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

        Activity a = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l =new ArrayList<Activity>();
        l.add(a);

        Role r = new Role(ID, NAME, FILE_PATH, ICON_PATH, "descr role",l);

        assertEquals(r.getId(), ID);
        assertEquals(r.getName(), NAME);
        assertEquals(r.getFileURL(), FILE_PATH);
        assertEquals(r.getIconURL(), ICON_PATH);
        assertSame(r.getActivities(), l);   
        
    }

    /**
     * test de la methode getActivities de Role
     * @see Role#getActivities()
     */
    public void testGetActivities()
    {
        Activity a = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l =new ArrayList<Activity>();
        l.add(a);
        
        this.role.setActivities(l);
        assertSame(this.role.getActivities(),l);
        
        
    }

    /**
     * test de la methode setActivities de Role
    */
    public void testSetActivities()
    {
        Activity a = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l =new ArrayList<Activity>();
        l.add(a);
        
        Activity a2 = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l2 =new ArrayList<Activity>();
        l2.add(a2);
        
        // test en faisant le set a partir d'un role qui ne contient
        // que null
        this.role.setActivities(l);
        assertSame(this.role.getActivities(),l);
        
        // test en faisant le set a partir d'un role contenant
        // deja une liste d'activités
        this.role.setActivities(l);
        this.role.setActivities(l2);
        assertSame(this.role.getActivities(),l2);
        
        // test en faisant un set de null a partir d'un role
        // contenant deja une liste d'activités
        this.role.setActivities(l);
        this.role.setActivities(null);
        assertTrue((this.role.getActivities()).size()==0);
        
    }

    /**
     * test de la methode addActivity de Role
     * @see Role#addActivity(Activity)
     */
    public void testAddActivity()
    {
        Activity a = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l =new ArrayList<Activity>();
        l.add(a);
        
        Activity a2 = new Activity("","",null,null,null,null,null,null,null);
        
        // test en ajoutant une activité deja existante
        this.role.setActivities(l);
        this.role.addActivity(a);
        assertTrue((this.role.getActivities()).size()==1);
        
        // test en ajoutant 2 activités différentes
        this.role.addActivity(a);
        this.role.addActivity(a2);
        assertTrue((this.role.getActivities()).size()==2 && (this.role.getActivities()).contains(a) && (this.role.getActivities()).contains(a2)) ;
        
    }

    /**
     * test de la methode removeActivity de Role
     * @see Role#removeActivity(Activity)
     */
    public void testRemoveActivity()
    {
        Activity a = new Activity("","",null,null,null,null,null,null,null);
        ArrayList<Activity> l =new ArrayList<Activity>();
        l.add(a);
        
        Activity a2 = new Activity("","",null,null,null,null,null,null,null);
        
        // test en supprimant une activité qui n'appartient
        // pas a la liste
        this.role.setActivities(l);
        this.role.removeActivity(a2);
        assertTrue((this.role.getActivities()).size()==1 && (this.role.getActivities()).contains(a));
        
        // test en supprimant une activité qui fait partie de la liste
        this.role.setActivities(l);
        this.role.removeActivity(a);
        assertTrue((this.role.getActivities()).size()==0);
        
    }

}
