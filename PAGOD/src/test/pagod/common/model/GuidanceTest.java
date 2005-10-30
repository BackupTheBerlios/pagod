/*
 * $Id: GuidanceTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import pagod.common.model.Guidance;

/**
 * @author Biniou
 */
public class GuidanceTest extends ProcessElementTest
{
    /**
     * produit que l'on teste
     */
    protected Guidance guid ;
    

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        // création d'un guide vide pour les tests
        this.guid = new Guidance("","",null,null,"");
        
        // teste ce guide en tant que ProcessElement
        this.processElement = this.guid;
    } 

    /**
     * test du constructeur Guidance de la classe Guidance
     * @see Guidance#Guidance(String, String, URL, URL, String)
     */
    public void testGuidance()
    {
        final String ID = "Guidance ID";
        final String NAME = "Guidance name";
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
        
        final String TYPE = "Guidance type" ;
        
        Guidance g = new Guidance(ID,NAME,FILE_PATH,ICON_PATH,TYPE);
        
        assertEquals(g.getId(), ID);
        assertEquals(g.getName(), NAME);
        assertEquals(g.getFileURL(), FILE_PATH);
        assertEquals(g.getIconURL(), ICON_PATH);
        assertEquals(g.getType(),TYPE);
        
    }

    /**
     * test de la methode getType de Guidance
     * @see Guidance#getType()
     */
    public void testGetType()
    {
        String t1 = "type1";
        Guidance g = new Guidance("id","name",null,null,t1);
        assertEquals(g.getType(),t1);
    }

}
