/*
 * $Id: ProcessElementTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import pagod.common.model.Guidance; 
import pagod.common.model.ProcessElement;
import junit.framework.TestCase;

/**
 * Classe de test JUnit de ProcessElement
 * 
 * @author m1isi24
 */
public class ProcessElementTest extends TestCase
{
    /** Element à tester */
    protected ProcessElement processElement;
    
    /*
     * Initialise avant les tests
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        // création d'un élément vide pour les tests
        this.processElement = new ProcessElement("", "", null, null){};
    }

    /**
     * Teste le constructeur de ProcessElement
     * @see ProcessElement#ProcessElement(String, String, URL, URL)
     */
    public void testProcessElement()
    {
        final String ID = "Id Process Element";
        final String NAME = "Nom Process Element";
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
        
        ProcessElement pe = new ProcessElement(ID, NAME, FILE_PATH, ICON_PATH){};
        
        assertEquals(pe.getId(), ID);
        assertEquals(pe.getName(), NAME);
        assertEquals(pe.getFileURL(), FILE_PATH);
        assertEquals(pe.getIconURL(), ICON_PATH);
    }

    /**
     * Teste addGuidance de ProcessElement
     * @see ProcessElement#addGuidance(Guidance)
     */
    public void testAddGuidance()
    {
        Guidance g = new Guidance("IdGuide", "NomGuide", null,
                null, "typeGuide");
        
        this.processElement.addGuidance(g);
        
        assertTrue(this.processElement.getGuidances().contains(g));
    }

    /**
     * Teste getId de ProcessElement
     * @see ProcessElement#getId()
     */
    public void testGetId()
    {
        final String ID = "Id Process Element";
        
        this.processElement.setId(ID);
        
        assertEquals(this.processElement.getId(), ID);
    }

    /**
     * Teste getFilePath de ProcessElement
     * @see ProcessElement#getFileURL()
     */
    public void testGetFileURL()
    {
        try
        {
            final URL FILE_PATH = new URL("file://");
        
            this.processElement.setFileURL(FILE_PATH);
        
            assertEquals(this.processElement.getFileURL(), FILE_PATH);
        }
        catch (MalformedURLException mue)
        {
            fail("URL de test invalide: " + mue);
        }
    }

    /**
     * Teste getGuidances de ProcessElement
     * @see ProcessElement#getGuidances()
     */
    public void testGetGuidances()
    {
        Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null,
                null, "typeGuide1");
        Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null,
                null, "typeGuide2");
        
        ArrayList<Guidance> array = new ArrayList<Guidance>();
        array.add(g1);
        array.add(g2);
        
        this.processElement.setGuidances(array);
        
        assertEquals(this.processElement.getGuidances(), array);
    }

    /**
     * Teste getIconPath de ProcessElement
     * @see ProcessElement#getIconURL()
     */
    public void testGetIconURL()
    {
        try
        {
            final URL ICON_PATH = new URL("file://");
            
            this.processElement.setIconURL(ICON_PATH);
            
            assertEquals(this.processElement.getIconURL(), ICON_PATH);
        }
        catch (MalformedURLException mue)
        {
            fail("URL de test invalide: " + mue);
        }
    }

    /**
     * Teste getName de ProcessElement
     * @see ProcessElement#getName()
     */
    public void testGetName()
    {
        final String NAME = "Nom Process Element";
        
        this.processElement.setName(NAME);
        
        assertEquals(this.processElement.getName(), NAME);
    }

    /**
     * Teste setFilePath de ProcessElement
     * @see ProcessElement#setFileURL(URL)
     */
    public void testSetFileURL()
    {
        try
        {
            final URL FILE_PATH = new URL("file://");
        
            this.processElement.setFileURL(null);
        
            assertNull(this.processElement.getFileURL());
        
            this.processElement.setFileURL(FILE_PATH);

            assertEquals(this.processElement.getFileURL(), FILE_PATH);
        }
        catch (MalformedURLException mue)
        {
            fail("URL de test invalide: " + mue);
        }
    }

    /**
     * Teste setGuidances de ProcessElement
     */
    public void testSetGuidances()
    {
        Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null,
                null, "typeGuide1");
        Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null,
                null, "typeGuide2");
        
        ArrayList<Guidance> array1 = new ArrayList<Guidance>();
        ArrayList<Guidance> array2 = new ArrayList<Guidance>();
        array2.add(g1);
        array2.add(g2);
        
        this.processElement.setGuidances(array1);
        
        assertEquals(this.processElement.getGuidances(), array1);
        
        this.processElement.setGuidances(array2);
        
        assertEquals(this.processElement.getGuidances(), array2);
    }

    /**
     * Teste setIconPath de ProcessElement
     * @see ProcessElement#setIconURL(URL)
     */
    public void testSetIconURL()
    {
        try
        {
            final URL ICON_PATH = new URL("file://");
            
            this.processElement.setIconURL(null);
            
            assertNull(this.processElement.getIconURL());
            
            this.processElement.setIconURL(ICON_PATH);
            
            assertEquals(this.processElement.getIconURL(), ICON_PATH);
        }
        catch (MalformedURLException mue)
        {
            fail("URL de test invalide: " + mue);
        }
    }

    /**
     * Teste setName de ProcessElement
     * @see ProcessElement#setName(String)
     */
    public void testSetName()
    {
        final String NAME = "Nom Process Element";
        final String NEW_NAME = "NouveauNom";
        
        this.processElement.setName(NAME);
        
        assertEquals(this.processElement.getName(), NAME);
        
        this.processElement.setName(NEW_NAME);
        
        assertEquals(this.processElement.getName(), NEW_NAME);
    }
}
