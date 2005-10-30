/*
 * $Id: ProcessComponentTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
public class ProcessComponentTest extends ProcessElementTest
{
    /**
     * processcomponent que l'on teste
     */
    protected ProcessComponent pc;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        // création d'un processComponent vide pour les tests
        this.pc = new ProcessComponent("", "", null, null, null);

        // teste ce processcomponent en tant que ProcessElement
        this.processElement = this.pc;
    }

    /**
     * test du constructeur ProcessComponent
     * 
     */
    public void testProcessComponent()
    {
        final String ID = "Id PC";
        final String NAME = "Nom PC";
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

        WorkDefinition wd = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list = new ArrayList<WorkDefinition>();
        list.add(wd);

        ProcessComponent procc = new ProcessComponent(ID, NAME, FILE_PATH,
                ICON_PATH, list);

        assertEquals(procc.getId(), ID);
        assertEquals(procc.getName(), NAME);
        assertEquals(procc.getFileURL(), FILE_PATH);
        assertEquals(procc.getIconURL(), ICON_PATH);
        assertSame(procc.getWorksDefinitions(), list);
    }

    /**
     * test de la methode getWorksDefinitions de ProcessComponent
     * 
     * @see ProcessComponent#getWorksDefinitions()
     */
    public void testGetWorksDefinitions()
    {
        // Creation de la liste de wd
        WorkDefinition wd = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list = new ArrayList<WorkDefinition>();
        list.add(wd);

        this.pc.setWorksDefinitions(list);
        assertSame(this.pc.getWorksDefinitions(), list);

    }

    /**
     * test de la methode setWorksDefinitions de ProcessComponent
     * 
     */
    public void testSetWorksDefinitions()
    {
        // Creation de la liste de wd
        WorkDefinition wd1 = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list1 = new ArrayList<WorkDefinition>();
        list1.add(wd1);

        WorkDefinition wd2 = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list2 = new ArrayList<WorkDefinition>();
        list2.add(wd2);

        // test en initialisant wd
        this.pc.setWorksDefinitions(list1);
        assertSame(this.pc.getWorksDefinitions(), list1);

        // test de changement de valeur des wd
        this.pc.setWorksDefinitions(list1);
        this.pc.setWorksDefinitions(list2);
        assertSame(this.pc.getWorksDefinitions(), list2);

        // test en essayant de remplacer les wd par null
        this.pc.setWorksDefinitions(list1);
        this.pc.setWorksDefinitions(null);
        assertTrue((this.pc.getWorksDefinitions()).size() == 0);

    }

    /**
     * test de la methode addWorkDefinition de ProcessComponent
     * 
     * @see ProcessComponent#addWorkDefinition(WorkDefinition)
     */
    public void testAddWorkDefinition()
    {
        // Creation de la liste de wd
        WorkDefinition wd1 = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list1 = new ArrayList<WorkDefinition>();
        list1.add(wd1);

        WorkDefinition wd2 = new WorkDefinition("id2", "name2", null, null,
                null);

        // Test en ajoutant une wd qui est deja dans la liste
        this.pc.setWorksDefinitions(list1);
        this.pc.addWorkDefinition(wd1);
        assertTrue((this.pc.getWorksDefinitions()).contains(wd1)
                && (this.pc.getWorksDefinitions()).size() == 1);

        // test en ajoutant une nouvelle wd
        this.pc.setWorksDefinitions(list1);
        this.pc.addWorkDefinition(wd2);
        assertTrue((this.pc.getWorksDefinitions()).contains(wd1)
                && (this.pc.getWorksDefinitions()).contains(wd2)
                && (this.pc.getWorksDefinitions()).size() == 2);

    }

    /**
     * test de la methode removeWorkDefinition de ProcessComponent
     * 
     * @see ProcessComponent#removeWorkDefinition(WorkDefinition)
     */
    public void testRemoveWorkDefinition()
    {
        // Creation de la liste de wd
        WorkDefinition wd1 = new WorkDefinition("id", "name", null, null, null);
        ArrayList<WorkDefinition> list1 = new ArrayList<WorkDefinition>();
        list1.add(wd1);

        WorkDefinition wd2 = new WorkDefinition("id2", "name2", null, null,
                null);

        // test en essayant de supprimer une wd qui n'appartient pas a la liste
        this.pc.setWorksDefinitions(list1);
        this.pc.removeWorkDefinition(wd2);
        assertTrue((this.pc.getWorksDefinitions()).size() == 1
                && (this.pc.getWorksDefinitions()).contains(wd1));

        // test en supprimant une wd qui appartient a la liste
        this.pc.setWorksDefinitions(list1);
        this.pc.removeWorkDefinition(wd1);
        assertTrue((this.pc.getWorksDefinitions()).size() == 0);

    }

}
