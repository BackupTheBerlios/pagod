/*
 * $Id: MainWizard.java,v 1.6 2006/02/19 15:53:40 yak Exp $
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

package pagod.wizard;

import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.MainFrame;

/**
 * Classe principale de PAGOD.
 * 
 * @author MoOky
 */
public class MainWizard
{
	/**
	 * Méthode Principale de pagod
	 * 
	 * @param args
	 *            PAGOD n'utilise pas les paramètres passés en ligne de
	 *            commande.
	 */
	public static void main (String[] args)
	{
		// lancement de l'application
		ApplicationManager applicationManager = ApplicationManager
				.getInstance();
	
		// creation de la fenetre principale
		MainFrame mfPagod = new MainFrame();
		
		applicationManager.setMfPagod(mfPagod);

	}
}
