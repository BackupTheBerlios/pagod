/*
 * $Id: ExceptionManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

/**
 * Gestionnaire d'exception implémenté comme un singleton Cette classe permet de
 * centraliser le traitement des exceptions non attendu.
 * 
 * @author MoOky
 */
public class ExceptionManager
{
    /**
     * Instance du gestionnaire d'exception
     */
    private static ExceptionManager emInstance = null;

    /**
     * constructeur privé du gestionnaire d'exception (implémentation d'un
     * singleton)
     */
    private ExceptionManager()
    {
    }

    /**
     * retourne l'instance du gestionnaire d'exception
     * 
     * @return instance du gestionnaire d'exception
     */
    public static ExceptionManager getInstance()
    {
        if (ExceptionManager.emInstance == null)
        {
            ExceptionManager.emInstance = new ExceptionManager();
        }
        return emInstance;
    }

    /**
     * Traite l'exception passer en parametre
     * 
     * @param ex
     *            exception à traiter
     */
    public void manage(Exception ex)
    {
        
        //new EM("PAGOD", ex, 1);
         System.out.println("___________________________");
         System.out.println("Gestion de l'exception : ");
         System.out.println("Message : " + ex.getMessage());
         ex.printStackTrace();
         System.out.println("___________________________");
     
    }
    /**
     * Traite l'exception passer en parametre
     * 
     * @param ex
     *            exception à traiter
     */
    public void manageWarning(Exception ex)
    {
        
        //new EM("PAGOD", ex, 4);
        System.out.println("___________________________");
        System.out.println("Gestion de l'exception : ");
        System.out.println("Message : " + ex.getMessage());
        ex.printStackTrace();
        System.out.println("___________________________");
    }
}
