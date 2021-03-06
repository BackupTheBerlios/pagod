/*
 * $Id: Role.java,v 1.4 2006/02/23 18:26:37 psyko Exp $
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

package pagod.common.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Role ...
 * 
 * @author MoOky
 */
public class Role extends ProcessElement
{
	/**
	 * Liste des activ�s que doit r�alis� le role
	 */
	private List<Activity>	activities	= new ArrayList<Activity>();

	/**
	 * Permet de savoir si ce role est actif ou non (cet notion est parametrable
	 * dans le configurateur)
	 */
	private boolean			isActivate	= true;
	
	private String			description = "";

	/**
	 * Constructeur complet d'un role
	 * 
	 * @param id
	 * @param name
	 * @param fileURL
	 * @param iconURL
	 * @param description 
	 * @param activities
	 */
	public Role (String id, String name, URL fileURL, URL iconURL, String description,
			List<Activity> activities)
	{
		super(id, name, fileURL, iconURL);
		this.description = description;

		this.setActivities(activities);
	}

	/**
	 * Retourne l'attribut activities
	 * 
	 * @return activities.
	 */
	public List<Activity> getActivities ()
	{
		return this.activities;
	}

	/**
	 * Initialise l'attribut activities
	 * 
	 * @param activities
	 *            la valeur a attribuer.
	 */
	public void setActivities (List<Activity> activities)
	{
		if (this.activities == activities) return;

		final List<Activity> oldActivities = this.activities;

		// blocage de la r�cursivit�
		this.activities = new ArrayList<Activity>();

		// suppression des anciennes liaisons
		for (Activity a : oldActivities)
		{
			a.setRole(null);
		}

		// cr�ation des nouvelles liaisons
		if (activities != null)
		{
			this.activities = activities;

			for (Activity a : activities)
			{
				a.setRole(this);
			}
		}
	}

	/**
	 * Ajoute l'activit� sp�cifi�e � ce r�le
	 * 
	 * @param activity
	 *            Activit� � ajouter
	 */
	public void addActivity (Activity activity)
	{
		if (!this.activities.contains(activity))
		{
			this.activities.add(activity);

			activity.setRole(this);
		}
	}

	/**
	 * Supprime l'activit� sp�cifi�e de ce r�le
	 * 
	 * @param activity
	 *            Activit� � supprimer
	 */
	public void removeActivity (Activity activity)
	{
		if (this.activities.remove(activity))
		{
			activity.setRole(null);
		}
	}
	
	/**
	 * Permet de savoir si un role est active ou non
	 * 
	 * @return true si le role est active sinon false
	 */
	public boolean isActivate()
	{
		return this.isActivate;
	}
	
	
	/**
	 * Permet de metre d'activer/desactiver le role
	 * 
	 * @param b true si on veut activer le role sinon false
	 */
	public void setActivate(boolean b)
	{
		this.isActivate = b;
	}
	
	/**
     * @return String
     */
    public String getDescription()
    {
    	return this.description;
    }
}
