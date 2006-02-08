/*
 * $Id: ProcessElementTest.java,v 1.3 2006/02/08 12:12:52 cyberal82 Exp $
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
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import pagod.common.model.Guidance;
import pagod.common.model.ProcessElement;

/**
 * Classe de test JUnit de ProcessElement
 * 
 * @author m1isi24
 */
public class ProcessElementTest extends TestCase
{
	/** Element ? tester */
	protected ProcessElement	processElement;

	/*
	 * Initialise avant les tests
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp () throws Exception
	{
		super.setUp();

		// cr?ation d'un ?l?ment vide pour les tests
		this.processElement = new ProcessElement("", "", null, null)
		{
		};
	}

	/**
	 * Teste le constructeur de ProcessElement
	 * 
	 * @see ProcessElement#ProcessElement(String, String, URL, URL)
	 */
	public void testProcessElement ()
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

		ProcessElement pe = new ProcessElement(ID, NAME, FILE_PATH, ICON_PATH)
		{
		};

		assertEquals(pe.getId(), ID);
		assertEquals(pe.getName(), NAME);
		assertEquals(pe.getFileURL(), FILE_PATH);
		assertEquals(pe.getIconURL(), ICON_PATH);
	}

	/**
	 * Teste addGuidance de ProcessElement
	 * 
	 * @see ProcessElement#addGuidance(Guidance)
	 */
	public void testAddGuidance ()
	{
		Guidance g = new Guidance("IdGuide", "NomGuide", null, null,
				"typeGuide");

		this.processElement.addGuidance(g);

		assertTrue(this.processElement.getGuidances().contains(g));
	}

	/**
	 * Teste getId de ProcessElement
	 * 
	 * @see ProcessElement#getId()
	 */
	public void testGetId ()
	{
		final String ID = "Id Process Element";

		this.processElement.setId(ID);

		assertEquals(this.processElement.getId(), ID);
	}

	/**
	 * Teste getFilePath de ProcessElement
	 * 
	 * @see ProcessElement#getFileURL()
	 */
	public void testGetFileURL ()
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
	 * 
	 * @see ProcessElement#getGuidances()
	 */
	public void testGetGuidances ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");

		ArrayList<Guidance> array = new ArrayList<Guidance>();
		array.add(g1);
		array.add(g2);

		this.processElement.setGuidances(array);

		assertEquals(this.processElement.getGuidances(), array);
	}

	/**
	 * Teste getGuidancesWithoutType de ProcessElement
	 * 
	 * @see ProcessElement#getGuidanceWithoutType(String)
	 */
	public void testGetGuidancesWithoutType ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");
		Guidance g3 = new Guidance("IdGuide3", "NomGuide3", null, null,
				"typeGuide1");
		Guidance g4 = new Guidance("IdGuide4", "NomGuide4", null, null,
				"typeGuide2");

		List<Guidance> array = new ArrayList<Guidance>();
		array.add(g1);
		array.add(g2);
		array.add(g3);
		array.add(g4);

		this.processElement.setGuidances(array);
		List<Guidance> temp = this.processElement
				.getGuidanceWithoutType("typeGuide2");
		for (Iterator i = temp.iterator(); i.hasNext();)
		{
			assertEquals(((Guidance) i.next()).getType(), "typeGuide1");
		}
	}

	/**
	 * Teste getGuidancesType de ProcessElement
	 * 
	 * @see ProcessElement#getGuidanceType(String)
	 */
	public void testGetGuidancesType ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");
		Guidance g3 = new Guidance("IdGuide3", "NomGuide3", null, null,
				"typeGuide1");
		Guidance g4 = new Guidance("IdGuide4", "NomGuide4", null, null,
				"typeGuide2");

		List<Guidance> array = new ArrayList<Guidance>();
		array.add(g1);
		array.add(g2);
		array.add(g3);
		array.add(g4);

		this.processElement.setGuidances(array);
		List<Guidance> temp = this.processElement.getGuidanceType("typeGuide2");
		for (Iterator i = temp.iterator(); i.hasNext();)
		{
			assertEquals(((Guidance) i.next()).getType(), "typeGuide2");
		}
	}

	/**
	 * Test hasGuidanceWithoutType
	 * 
	 * @see ProcessElement#hasGuidanceWithoutType(String)
	 */
	public void testHasGuidanceWithoutType ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");
		Guidance g3 = new Guidance("IdGuide3", "NomGuide3", null, null,
				"typeGuide3");
		Guidance g4 = new Guidance("IdGuide4", "NomGuide4", null, null,
				"typeGuide3");

		List<Guidance> array = new ArrayList<Guidance>();
		array.add(g1);
		array.add(g2);
		array.add(g3);
		array.add(g4);

		this.processElement.setGuidances(array);

		assertTrue(
				"Il doit y avoir des guides qui ne sont pas du type typeGuide3",
				this.processElement.hasGuidanceWithoutType("typeGuide3"));

		// on supprime les guides de type 1 et 2 pour n'avoir dans l'activity
		// que des guides de type 3
		array.remove(g1);
		array.remove(g2);
		this.processElement.setGuidances(array);

		assertFalse(
				"Il ne doit pas y avoir des guides qui ont du type typeGuide3",
				this.processElement.hasGuidanceWithoutType("typeGuide3"));
	}

	/**
	 * Test hasGuidanceType
	 * 
	 * @see ProcessElement#hasGuidanceType(String)
	 */
	public void testHasGuidanceType ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");
		Guidance g3 = new Guidance("IdGuide3", "NomGuide3", null, null,
				"typeGuide3");
		Guidance g4 = new Guidance("IdGuide4", "NomGuide4", null, null,
				"typeGuide3");

		List<Guidance> array = new ArrayList<Guidance>();
		array.add(g1);
		array.add(g2);
		array.add(g3);
		array.add(g4);

		this.processElement.setGuidances(array);

		assertTrue("Il doit y avoir des guides qui sont du type typeGuide3",
				this.processElement.hasGuidanceType("typeGuide3"));

		assertFalse(
				"Il ne doit pas y avoir des guides qui sont du type typeGuide5",
				this.processElement.hasGuidanceType("typeGuide5"));
	}

	/**
	 * Teste getIconPath de ProcessElement
	 * 
	 * @see ProcessElement#getIconURL()
	 */
	public void testGetIconURL ()
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
	 * 
	 * @see ProcessElement#getName()
	 */
	public void testGetName ()
	{
		final String NAME = "Nom Process Element";

		this.processElement.setName(NAME);

		assertEquals(this.processElement.getName(), NAME);
	}

	/**
	 * Teste setFilePath de ProcessElement
	 * 
	 * @see ProcessElement#setFileURL(URL)
	 */
	public void testSetFileURL ()
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
	public void testSetGuidances ()
	{
		Guidance g1 = new Guidance("IdGuide1", "NomGuide1", null, null,
				"typeGuide1");
		Guidance g2 = new Guidance("IdGuide2", "NomGuide2", null, null,
				"typeGuide2");

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
	 * 
	 * @see ProcessElement#setIconURL(URL)
	 */
	public void testSetIconURL ()
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
	 * 
	 * @see ProcessElement#setName(String)
	 */
	public void testSetName ()
	{
		final String NAME = "Nom Process Element";
		final String NEW_NAME = "NouveauNom";

		this.processElement.setName(NAME);

		assertEquals(this.processElement.getName(), NAME);

		this.processElement.setName(NEW_NAME);

		assertEquals(this.processElement.getName(), NEW_NAME);
	}
}
