/*
 * $Id: UniqueIdentifierGeneratorTest.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package test.pagod.utils;

import java.util.HashSet;
import java.util.Random;

import pagod.utils.UniqueIdentifierGenerator;
import junit.framework.TestCase;

/**
 * @author m1isi24
 * 
 */
public class UniqueIdentifierGeneratorTest extends TestCase
{
    private UniqueIdentifierGenerator generator;

    private static final int MIN_ITERATION = 100;

    private static final int MAX_ITERATION = 10000;

    private final Random random = new Random();

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        // cr�ation du g�n�rateur
        this.generator = UniqueIdentifierGenerator.getInstance();
    }

    /**
     * Retourne un nombre d'it�rations al�atoires � effectuer
     * 
     * @return Nombre d'it�ration � effectuer
     */
    private int nbIterations()
    {
        return this.random.nextInt(MAX_ITERATION + 1) + MIN_ITERATION;
    }

    /**
     * Teste le singleton
     * 
     * @see UniqueIdentifierGenerator#getInstance()
     */
    public void testGetInstance()
    {
        UniqueIdentifierGenerator generator1 = null;
        UniqueIdentifierGenerator generator2 = null;

        // r�cup�ration des instances
        generator1 = UniqueIdentifierGenerator.getInstance();
        generator2 = UniqueIdentifierGenerator.getInstance();

        // v�rifie que le g�n�rateur a bien �t� retourn�
        assertNotNull(generator1);
        assertNotNull(generator2);

        // v�rifie qu'il s'agit bien du m�me objet
        assertSame(generator1, generator2);
    }

    /**
     * Teste generateUniqueIdentifier sans param�tre
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier()
     */
    public void testGenerateUniqueIdentifier()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);

        // ajoute tous les identifiants � l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // g�n�ration de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier();

            // v�rifie que l'identifiant n'y �tait pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));
        }
    }

    /**
     * Teste generateUniqueIdentifier avec le pr�fixe
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier(String)
     */
    public void testGenerateUniqueIdentifierString()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);
        final String prefix = "monprefixe_" + Integer.toString(nbIterations);

        // ajoute tous les identifiants � l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // g�n�ration de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier(prefix);

            // v�rifie que l'identifiant n'y �tait pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));

            // v�rifie que le pr�fixe est bien pr�sent
            assertTrue(sId.startsWith(prefix));
        }
    }

    /**
     * Teste generateUniqueIdentifier avec le pr�fixe et le suffixe
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier(String, String)
     */
    public void testGenerateUniqueIdentifierStringString()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);
        final String prefix = "monprefixe_" + Integer.toString(nbIterations);
        final String suffix = "monsuffixe_" + Integer.toString(nbIterations);

        // ajoute tous les identifiants � l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // g�n�ration de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier(prefix,
                    suffix);

            // v�rifie que l'identifiant n'y �tait pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));

            // v�rifie que le pr�fixe et le suffixe sont bien pr�sents
            assertTrue(sId.startsWith(prefix));
            assertTrue(sId.endsWith(suffix));
        }
    }
}
