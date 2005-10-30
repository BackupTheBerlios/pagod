/*
 * $Id: MainConfigurator.java,v 1.1 2005/10/30 10:45:00 yak Exp $
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

package pagod.configurator;
 
import pagod.configurator.control.ApplicationManager;

/**
 * Classe principale de PAGOD configurator.
 * 
 * @author MoOky
 */
public class MainConfigurator
{
    /**
     * Méthode Principale de PAGOD configurator
     * 
     * @param args
     *            PAGOD configurator n'utilise pas les paramètres passés en ligne de
     *            commande.
     */
    public static void main(String[] args)
    {
        // lancement de l'application
        ApplicationManager applicationManager = ApplicationManager
                .getInstance();
        applicationManager
                .manageRequest(ApplicationManager.Request.RUN_APPLICATION);
    }
}
