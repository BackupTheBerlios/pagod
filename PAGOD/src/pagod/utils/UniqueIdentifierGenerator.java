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
 * Singleton de g�n�ration d'identifiants uniques
 * 
 * @author m1isi24
 */
public class UniqueIdentifierGenerator
{
    /** Instance courante du g�n�rateur */
    private static UniqueIdentifierGenerator currentInstance = null;

    /**
     * Retourne l'instance du g�n�rateur al�atoire
     * 
     * @return G�n�rateur d'identifiant al�atoire
     */
    public static UniqueIdentifierGenerator getInstance()
    {
        if (UniqueIdentifierGenerator.currentInstance == null)
            UniqueIdentifierGenerator.currentInstance = new UniqueIdentifierGenerator();

        return UniqueIdentifierGenerator.currentInstance;
    }

    /** G�n�rateur s�curis� */
    private SecureRandom secureRandom;

    /** G�n�rateur non s�curis� */
    private Random random;

    /** Algorithme s�curiser � utiliser */
    private static String ALGORITHM = "SHA1PRNG";

    /** Nombre d'octets al�atoires */
    private static int SIZE = 20;

    /** Base souhait�e pour la conversion */
    private static int RADIX = 36;

    /** Base utilis�e pour la conversion */
    private int radix;

    /**
     * Initialise le g�n�rateur
     */
    private UniqueIdentifierGenerator()
    {
        // cr�ation du g�n�rateur s�curis�
        try
        {
            this.secureRandom = SecureRandom
                    .getInstance(UniqueIdentifierGenerator.ALGORITHM);
            this.random = null;
        }
        catch (NoSuchAlgorithmException nsae)
        {
            this.secureRandom = null;

            // cr�ation du g�n�rateur non s�curis�
            this.random = new Random();
        }

        // choix de la base de conversion
        if (UniqueIdentifierGenerator.RADIX > Character.MAX_RADIX)
            this.radix = Character.MAX_RADIX;
        else
            this.radix = UniqueIdentifierGenerator.RADIX;
    }

    /**
     * G�n�re un identifiant unique
     * 
     * @return Identifiant al�atoire g�n�r�
     */
    public String generateUniqueIdentifier()
    {
        return this.generateUniqueIdentifier("", "");
    }

    /**
     * G�n�re un identifiant unique pr�fix� par la valeur sp�cifi�e
     * 
     * @param prefix
     *            Pr�fixe de l'identifiant unique
     * @return Identifiant al�atoire g�n�r� pr�fix�
     */
    public String generateUniqueIdentifier(String prefix)
    {
        return this.generateUniqueIdentifier(prefix, "");
    }

    /**
     * G�n�re un identifiant unique pr�fix� et suffix� par les valeurs
     * sp�cifi�es
     * 
     * @param prefix
     *            Pr�fixe de l'identifiant unique
     * @param suffix
     *            Suffixe de l'identifiant unique
     * @return Identifiant al�atoire g�n�r� pr� et suffix�
     */
    public String generateUniqueIdentifier(String prefix, String suffix)
    {
        byte[] buffer = new byte[UniqueIdentifierGenerator.SIZE];

        if (this.secureRandom != null)
        {
            // cr�ation des octets al�atoires
            this.secureRandom.nextBytes(buffer);
        }
        else
        {
            // cr�ation des octets al�atoires
            this.random.nextBytes(buffer);
        }

        // cr�ation d'un entier long
        BigInteger bigInteger = new BigInteger(1, buffer);

        // g�n�ration de la chaine dans la base choisie
        return (prefix + bigInteger.toString(this.radix) + suffix);
    }
}
