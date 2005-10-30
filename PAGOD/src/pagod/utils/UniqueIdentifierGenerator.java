/*
 * $Id: UniqueIdentifierGenerator.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of PAGOD.
 *
 * PAGOD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * PAGOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PAGOD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Singleton de génération d'identifiants uniques
 * 
 * @author m1isi24
 */
public class UniqueIdentifierGenerator
{
    /** Instance courante du générateur */
    private static UniqueIdentifierGenerator currentInstance = null;

    /**
     * Retourne l'instance du générateur aléatoire
     * 
     * @return Générateur d'identifiant aléatoire
     */
    public static UniqueIdentifierGenerator getInstance()
    {
        if (UniqueIdentifierGenerator.currentInstance == null)
            UniqueIdentifierGenerator.currentInstance = new UniqueIdentifierGenerator();

        return UniqueIdentifierGenerator.currentInstance;
    }

    /** Générateur sécurisé */
    private SecureRandom secureRandom;

    /** Générateur non sécurisé */
    private Random random;

    /** Algorithme sécuriser à utiliser */
    private static String ALGORITHM = "SHA1PRNG";

    /** Nombre d'octets aléatoires */
    private static int SIZE = 20;

    /** Base souhaitée pour la conversion */
    private static int RADIX = 36;

    /** Base utilisée pour la conversion */
    private int radix;

    /**
     * Initialise le générateur
     */
    private UniqueIdentifierGenerator()
    {
        // création du générateur sécurisé
        try
        {
            this.secureRandom = SecureRandom
                    .getInstance(UniqueIdentifierGenerator.ALGORITHM);
            this.random = null;
        }
        catch (NoSuchAlgorithmException nsae)
        {
            this.secureRandom = null;

            // création du générateur non sécurisé
            this.random = new Random();
        }

        // choix de la base de conversion
        if (UniqueIdentifierGenerator.RADIX > Character.MAX_RADIX)
            this.radix = Character.MAX_RADIX;
        else
            this.radix = UniqueIdentifierGenerator.RADIX;
    }

    /**
     * Génère un identifiant unique
     * 
     * @return Identifiant aléatoire généré
     */
    public String generateUniqueIdentifier()
    {
        return this.generateUniqueIdentifier("", "");
    }

    /**
     * Génère un identifiant unique préfixé par la valeur spécifiée
     * 
     * @param prefix
     *            Préfixe de l'identifiant unique
     * @return Identifiant aléatoire généré préfixé
     */
    public String generateUniqueIdentifier(String prefix)
    {
        return this.generateUniqueIdentifier(prefix, "");
    }

    /**
     * Génère un identifiant unique préfixé et suffixé par les valeurs
     * spécifiées
     * 
     * @param prefix
     *            Préfixe de l'identifiant unique
     * @param suffix
     *            Suffixe de l'identifiant unique
     * @return Identifiant aléatoire généré pré et suffixé
     */
    public String generateUniqueIdentifier(String prefix, String suffix)
    {
        byte[] buffer = new byte[UniqueIdentifierGenerator.SIZE];

        if (this.secureRandom != null)
        {
            // création des octets aléatoires
            this.secureRandom.nextBytes(buffer);
        }
        else
        {
            // création des octets aléatoires
            this.random.nextBytes(buffer);
        }

        // création d'un entier long
        BigInteger bigInteger = new BigInteger(1, buffer);

        // génération de la chaine dans la base choisie
        return (prefix + bigInteger.toString(this.radix) + suffix);
    }
}
