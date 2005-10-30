/*
 * $Id: PreferencesManagerTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package test.pagod.wizard.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.PreferencesManager.FileNotExecuteException;
import pagod.wizard.control.PreferencesManager.InvalidExtensionException;

/**
 * @author Alexandre Bes
 * 
 */
public class PreferencesManagerTest extends TestCase
{

    /**
     * Permet de remettre a vide le PreferencesManager, il faut faire cela car
     * c'est une classe implemente comme un singleton
     */
    public void setUp()
    {

        for (String sExtension : PreferencesManager.getInstance().preferences())
            PreferencesManager.getInstance().removePreference(sExtension);

            PreferencesManager.getInstance().storePreferences();

    }

    /**
     * Fonction permettant de creer un fichier temporaire (pour etre sur qu'il
     * existe)
     * 
     * @return un fichier temporaire
     */
    private File createTempFile()
    {
        // on cree un fichier temp pour etre sur de son existance
        File f = null;
        try
        {
            f = File.createTempFile("tempFile", "");
        }
        catch (IOException e1)
        {
            // TODO Bloc de traitement des exceptions g?n?r? automatiquement
            e1.printStackTrace();
        }
        return f;
    }

    /**
     * fonction permettant d'afficher les preferences *
     */
    private void printPreferences()
    {

        for (String sPref : PreferencesManager.getInstance().preferences())
            System.out.println(sPref);

    }

    /**
     * Test la methode getLanguage
     * 
     */
    public void testGetLanguage()
    {
        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        // on met la langue "tt"
        PreferencesManager.getInstance().setLanguage("tt");
        try
        {
            // vu que la langue correspond a la cle "lang" on essaye de voir si
            // on peut modifier la langue en passant par les setPreferences
            PreferencesManager.getInstance().setPreference("lang",
                    f.getAbsolutePath());
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre invalide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }

        // la langue devrait tjs etre egual a "tt" et non pas a
        // f.getAbsolutePath()
        assertTrue(PreferencesManager.getInstance().getLanguage().equals("tt"));
    }

    /**
     * Test de la methode SetLanguage()
     * 
     */
    public void testSetLanguage()
    {
        PreferencesManager.getInstance().loadPreferences();

        PreferencesManager.getInstance().setLanguage("yy");
        assertTrue(PreferencesManager.getInstance().getLanguage().equals("yy"));
    }

    /**
     * Test de la methode getPreference
     * 
     */
    public void testGetPreference()
    {
        PreferencesManager.getInstance().loadPreferences();

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            PreferencesManager.getInstance().setPreference(".pps",
                    f.getAbsolutePath());

            assertTrue(PreferencesManager.getInstance().getPreference(".pps")
                    .equals(f.getAbsolutePath()));
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre valide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
    }

    /**
     * Test de la methode setPreference()
     * 
     */
    public void testSetPreference()
    {

        boolean invalidExtensionException = false;
        boolean fileNotFoundException = false;

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            // on ajoute une extension qui ne doit lever aucune exception
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());

            assertTrue(PreferencesManager.getInstance().getPreference("toto")
                    .equals(f.getAbsolutePath()));
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre valide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }

        try
        {
            // l'ajout de cette extension doit lever une
            // InvalidExtensionException
            PreferencesManager.getInstance().setPreference("aa.",
                    f.getAbsolutePath());
        }
        catch (InvalidExtensionException e)
        {
            invalidExtensionException = true;
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
        assertTrue(invalidExtensionException);

        try
        {
            // l'ajout de cette extension doit lever une fileNotFoundException
            PreferencesManager.getInstance().setPreference("a", "hsdfsdhf");
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre valide");
        }
        catch (FileNotFoundException e)
        {
            fileNotFoundException = true;
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
        assertTrue(fileNotFoundException);

        // TODO il faudrait tester si setPreference leve bien une
        // FileNotExecuteException
        // pour le moment ce n'est pas le cas

    }

    /**
     * Test de la methode ContainPreference()
     * 
     */
    public void testContainPreference()
    {

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            // ajout d'un extension qui ne doit pas lever d'exception
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());

            assertTrue(PreferencesManager.getInstance().containPreference(
                    "toto"));
            assertTrue(PreferencesManager.getInstance().containPreference(
                    ".toto"));

        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre valide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }

        try
        {
            // doit lever une InvalidExtensionException
            PreferencesManager.getInstance().setPreference("a.",
                    f.getAbsolutePath());

        }
        catch (InvalidExtensionException e)
        {
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }

        // il ne devrait pas y avoir d'extension a de defini dans les
        // preferences
        assertFalse(PreferencesManager.getInstance().containPreference("a."));
        assertFalse(PreferencesManager.getInstance().containPreference(".a"));
        assertFalse(PreferencesManager.getInstance().containPreference("a"));
    }

    /**
     * Test de la methode removePreference
     * 
     */
    public void testRemovePreference()
    {

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            // ajout de l'extension toto
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());

            // verification de l'existance de toto dans les preferences
            assertTrue(PreferencesManager.getInstance().getPreference("toto")
                    .equals(f.getAbsolutePath()));

            PreferencesManager.getInstance().removePreference("toto");

            // l'extension toto ne devrait pas exister
            assertNull(PreferencesManager.getInstance().getPreference("toto"));
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre invalide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
    }

    /**
     * Test de la methode storePreferences
     * 
     */
    public void testStorePreferences()
    {

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            // ajout de l'extension toto
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());

            // sauvegarde de l'extension toto
            PreferencesManager.getInstance().storePreferences();

            // suppression de l'extenion dans la representation memoire des
            // preferences
            PreferencesManager.getInstance().removePreference("toto");

            // verification de l'existance de l'extension toto
            assertNull(PreferencesManager.getInstance().getPreference("toto"));

            // chargement des preferences a partir du fichier, l'extension toto
            // doit exister
            PreferencesManager.getInstance().loadPreferences();
            assertTrue(PreferencesManager.getInstance().getPreference("toto")
                    .equals(f.getAbsolutePath()));
        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre invalide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
    }

    /**
     * Test de la methode storeExtension
     * 
     */
    public void testStoreExtensions()
    {
        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            // ajout des extensions toto, word et de la langue fr
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("word",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setLanguage("fr");

            // sauvegarde des preferences
            PreferencesManager.getInstance().storePreferences();

            // changement de la langue
            PreferencesManager.getInstance().setLanguage("en");

            assertEquals(PreferencesManager.getInstance().getLanguage(), "en");

            PreferencesManager.getInstance().storeExtensions();

            PreferencesManager.getInstance().loadPreferences();

            // la langue doit etre fr car storeExtension ne modifie pas la
            // langue
            assertEquals(PreferencesManager.getInstance().getLanguage(), "fr");
        }
        catch (FileNotFoundException e)
        {
            // TODO Bloc de traitement des exceptions g?n?r? automatiquement
            e.printStackTrace();
        }
        catch (InvalidExtensionException e)
        {
            // TODO Bloc de traitement des exceptions g?n?r? automatiquement
            e.printStackTrace();
        }
        catch (FileNotExecuteException e)
        {
            // TODO Bloc de traitement des exceptions g?n?r? automatiquement
            e.printStackTrace();
        }
    }

    /**
     * Test de la methode Preferences
     * 
     */
    public void testPreferences()
    {

        boolean invalidExtensionException = false;
        boolean fileNotFoundException = false;
        boolean fileNotExecuteException = false;

        // on cree un fichier temp pour etre sur de son existance
        File f = null;
        try
        {
            f = File.createTempFile("tempFile", "");
        }
        catch (IOException e1)
        {
            // TODO Bloc de traitement des exceptions g?n?r? automatiquement
            e1.printStackTrace();
        }

        try
        {

            PreferencesManager.getInstance().setPreference("..a",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("lang",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("toto.e",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("d",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("c",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().setPreference("b",
                    f.getAbsolutePath());

        }
        catch (InvalidExtensionException e)
        {
            fail("L'extension aurait du etre valide");
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }

        assertFalse(invalidExtensionException);
        assertFalse(fileNotFoundException);
        assertFalse(fileNotExecuteException);

        try
        {
            PreferencesManager.getInstance().setPreference("aa.",
                    f.getAbsolutePath());
        }
        catch (InvalidExtensionException e)
        {
            invalidExtensionException = true;
        }
        catch (FileNotFoundException e)
        {
            fail("Le fichier aurait du exister");
        }
        catch (FileNotExecuteException e)
        {
            fail("Le fichier aurait du etre executable");
        }
        assertTrue(invalidExtensionException);
        assertFalse(fileNotFoundException);
        assertFalse(fileNotExecuteException);

        // TODO il faudrait tester si la methode leve bien une exception
        // FileNotExecuteException

        ArrayList<String> res = PreferencesManager.getInstance().preferences();

        ArrayList<String> temp = new ArrayList<String>();
        temp.add(".a");
        temp.add(".b");
        temp.add(".c");
        temp.add(".d");
        temp.add(".e");
        temp.add(".lang");

        assertTrue(res.equals(temp));
        assertFalse(res.contains("lang"));
    }

    /**
     * Test de la methode numberExtensions
     * 
     */
    public void testNumberExtensions()
    {

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            assertEquals("Le nombre de preferences defini est faux", 0,
                    PreferencesManager.getInstance().numberExtensions());

            PreferencesManager.getInstance().setPreference("ext1",
                    f.getAbsolutePath());

            PreferencesManager.getInstance().setPreference("ext2",
                    f.getAbsolutePath());

            assertEquals("Le nombre de preferences defini est faux", 2,
                    PreferencesManager.getInstance().numberExtensions());

            PreferencesManager.getInstance().removePreference("ext1");
            assertEquals("Le nombre de preferences defini est faux", 1,
                    PreferencesManager.getInstance().numberExtensions());

            assertEquals("Le nombre de preferences defini est faux", 1,
                    PreferencesManager.getInstance().numberExtensions());
        }
        catch (Exception e)
        {
            System.out.println("Ne devrait pas arriver");
            e.printStackTrace();
        }
    }

    /**
     * Test de la methode loadPreferences()
     * 
     */
    public void testLoadPreferences()
    {

        // on cree un fichier temp pour etre sur de son existance
        File f = createTempFile();

        try
        {
            PreferencesManager.getInstance().setPreference("toto",
                    f.getAbsolutePath());
            PreferencesManager.getInstance().storePreferences();
            PreferencesManager.getInstance().removePreference("toto");
            assertNull(PreferencesManager.getInstance().getPreference("toto"));
            PreferencesManager.getInstance().loadPreferences();
            assertTrue(PreferencesManager.getInstance().getPreference("toto")
                    .equals(f.getAbsolutePath()));
        }
        catch (Exception e)
        {
            System.out.println("Ne devrait pas arriver");
            e.printStackTrace();
        }
    }

    /**
     * Methode appele a chaque fin de test
     */
    public void tearDown()
    {
        PreferencesManager.getInstance().setLanguage("fr");
        PreferencesManager.getInstance().storePreferences();
    }

}
