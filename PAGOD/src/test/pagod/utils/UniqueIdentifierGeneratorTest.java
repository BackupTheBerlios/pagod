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

        // création du générateur
        this.generator = UniqueIdentifierGenerator.getInstance();
    }

    /**
     * Retourne un nombre d'itérations aléatoires à effectuer
     * 
     * @return Nombre d'itération à effectuer
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

        // récupération des instances
        generator1 = UniqueIdentifierGenerator.getInstance();
        generator2 = UniqueIdentifierGenerator.getInstance();

        // vérifie que le générateur a bien été retourné
        assertNotNull(generator1);
        assertNotNull(generator2);

        // vérifie qu'il s'agit bien du même objet
        assertSame(generator1, generator2);
    }

    /**
     * Teste generateUniqueIdentifier sans paramètre
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier()
     */
    public void testGenerateUniqueIdentifier()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);

        // ajoute tous les identifiants à l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // génération de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier();

            // vérifie que l'identifiant n'y était pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));
        }
    }

    /**
     * Teste generateUniqueIdentifier avec le préfixe
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier(String)
     */
    public void testGenerateUniqueIdentifierString()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);
        final String prefix = "monprefixe_" + Integer.toString(nbIterations);

        // ajoute tous les identifiants à l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // génération de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier(prefix);

            // vérifie que l'identifiant n'y était pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));

            // vérifie que le préfixe est bien présent
            assertTrue(sId.startsWith(prefix));
        }
    }

    /**
     * Teste generateUniqueIdentifier avec le préfixe et le suffixe
     * 
     * @see UniqueIdentifierGenerator#generateUniqueIdentifier(String, String)
     */
    public void testGenerateUniqueIdentifierStringString()
    {
        final int nbIterations = this.nbIterations();
        final HashSet<String> generatedIds = new HashSet<String>(nbIterations);
        final String prefix = "monprefixe_" + Integer.toString(nbIterations);
        final String suffix = "monsuffixe_" + Integer.toString(nbIterations);

        // ajoute tous les identifiants à l'ensemble
        for (int i = 1 ; i <= nbIterations ; i++)
        {
            // génération de l'identifiant
            final String sId = this.generator.generateUniqueIdentifier(prefix,
                    suffix);

            // vérifie que l'identifiant n'y était pas et l'ajoute
            assertTrue("Run " + Integer.toString(i) + ", ID: " + sId,
                    generatedIds.add(sId));

            // vérifie que le préfixe et le suffixe sont bien présents
            assertTrue(sId.startsWith(prefix));
            assertTrue(sId.endsWith(suffix));
        }
    }
}
