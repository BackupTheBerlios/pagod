/*
 * $Id: ProcessElement.java,v 1.7 2006/02/22 16:32:10 cyberal82 Exp $
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
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

/**
 * @author Benjamin
 */
public abstract class ProcessElement extends Observable
{
	/**
	 * Identifiant de l'?l?ment de processus
	 */
	private String			id;

	/**
	 * URL du fichier de contenu associ? ? l'?l?ment de processus
	 */
	private URL				fileURL;

	/**
	 * Liste des guides associ? ? cet ?l?ment
	 */
	private List<Guidance>	guidances;

	/**
	 * URL de l'icone de l'?l?ment de processus
	 */
	private URL				iconURL;

	/**
	 * Nom de l'?l?ment de processus
	 */
	private String			name;

	// /**
	// * Feuille de style ? utiliser pour afficher le fichier de contenu
	// */
	// private static StyleSheet styleSheet = new StyleSheet();
	//
	// /**
	// * D?finit la feuille de style ? utiliser pour les fichiers de contenu
	// *
	// * @param styleSheet
	// * Feuille de style ? utiliser
	// */
	// public static void setStyleSheet(StyleSheet styleSheet)
	// {
	// ProcessElement.styleSheet = (styleSheet != null ? styleSheet
	// : new StyleSheet());
	// }
	//
	// /**
	// * Retourne la feuille de style ? utiliser pour les fichiers de contenu
	// *
	// * @return Feuille de style ? utiliser
	// */
	// public static StyleSheet getStyleSheet()
	// {
	// return ProcessElement.styleSheet;
	// }

	/**
	 * @param id
	 * @param name
	 * @param fileURL
	 * @param iconURL
	 */
	public ProcessElement (String id, String name, URL fileURL, URL iconURL)
	{
		// initialiser attributs ? partir des param?tres du constructeur
		this.id = id;
		this.name = name;
		this.fileURL = fileURL;
		this.iconURL = iconURL;
		// initialiser liste vide
		this.guidances = new ArrayList<Guidance>();
	}

	/**
	 * ajoute un "guidance" ? la liste des guidances de ce processElement
	 * 
	 * @param g
	 *            le "guidance" ? ajouter
	 */
	public void addGuidance (Guidance g)
	{
		if (!this.guidances.contains(g))
		{
			this.guidances.add(g);
		}
	}
	
	/**
	 * Supprime la premiere occurence du guide passe en parametre
	 * 
	 * @param g le guide que l'on veut supprimer
	 * 
	 * @return true si le guide a ete supprimer sinon faux
	 */
	public boolean removeGuidance (Guidance g)
	{
		return this.guidances.remove(g);
	}

	/**
	 * Retourne l'identifiant de l'?l?ment de processus
	 * 
	 * @return Identifiant de l'?l?ment de processus
	 */
	public String getId ()
	{
		return this.id;
	}

	/**
	 * Sp?cifie l'identifiant de l'?l?ment de processus
	 * 
	 * @param id
	 *            Identifiant de l'?l?ment de processus
	 */
	public void setId (String id)
	{
		this.id = id;
	}

	/**
	 * @return Returns the fileURL.
	 */
	public URL getFileURL ()
	{
		return this.fileURL;
	}

	/**
	 * @return Returns the guidances.
	 */
	public List<Guidance> getGuidances ()
	{
		return this.guidances;
	}

	/**
	 * Retourne le nombre de guide associe au ProcessElement
	 * 
	 * @return le nombre de guide associe au ProcessElement
	 */
	public int getGuidanceCount ()
	{
		return this.guidances.size();
	}

	/**
	 * Retourne le nombre de type guide different que possede ce ProcessElement
	 * 
	 * @return le nombre de type guide different que possede ce ProcessElement
	 */
	public int getGuidanceTypeCount ()
	{
		// un HashSet est un ensemble, c'est a dire qu'il ne contient pas
		// de doublon
		HashSet<String> set = new HashSet<String>();

		for (Guidance guidance : this.guidances)
			set.add(guidance.getType().toUpperCase());

		return set.size();
	}

	/**
	 * Retourne tous les guides qui ne sont pas du type passe en parametre
	 * (sType)
	 * 
	 * @param sType
	 *            le type de guide qu'on ne veut pas recuperer
	 * 
	 * @return tous les guides qui ne sont pas du type passe en parametre
	 *         (sType)
	 */
	public List<Guidance> getGuidanceWithoutType (String sType)
	{
		List<Guidance> lGuidance = new ArrayList<Guidance>();

		for (Guidance g : this.guidances)
		{
			if (!g.getType().equalsIgnoreCase(sType)) 
				lGuidance.add(g);
		}

		return lGuidance;
	}
	
	/**
	 * Retourne vrai s'il existe des guides qui ne sont pas du type specifie en
	 * parametre
	 * 
	 * @param sType
	 *            le type des guides qui ne doivent pas etre prit en compte
	 * 
	 * @return vrai s'il existe des guides qui ne sont pas du type specifie en
	 *         parametre sinon faux
	 */
	public boolean hasGuidanceWithoutType (String sType)
	{
		for ( Guidance g : this.guidances)
		{
			if ( ! g.getType().equalsIgnoreCase(sType))
				return true;
		}
		return false;
	}

	
	/**
	 * Retourne vrai s'il existe des guides qui sont du type specifie en
	 * parametre
	 * 
	 * @param sType
	 *            le type des guides qui doivent etre prit en compte
	 * 
	 * @return vrai s'il existe des guides qui sont du type specifie en
	 *         parametre sinon faux
	 */
	public boolean hasGuidanceType (String sType)
	{
		for ( Guidance g : this.guidances)
		{
			if (g.getType().equalsIgnoreCase(sType))
				return true;
		}
		return false;
	}
	
	/**
	 * Retourne tous les guides du type specifie en parametre
	 * 
	 * @param sType
	 *            le type de guide que l'on veut recuperer
	 * 
	 * @return tous les guides du type specifie en parametre
	 */
	public List<Guidance> getGuidanceType (String sType)
	{
		List<Guidance> lGuidance = new ArrayList<Guidance>();

		for (Guidance g : this.guidances)
		{
			if (g.getType().equalsIgnoreCase(sType)) 
				lGuidance.add(g);
		}

		return lGuidance;
	}

	/**
	 * @return Returns the iconURL.
	 */
	public URL getIconURL ()
	{
		return this.iconURL;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName ()
	{
		return this.name;
	}

	/**
	 * @param fileURL
	 *            The fileURL to set.
	 */
	public void setFileURL (URL fileURL)
	{
		this.fileURL = fileURL;
	}

	/**
	 * @param guidances
	 *            The guidances to set.
	 */
	public void setGuidances (List<Guidance> guidances)
	{
		this.guidances = (guidances != null ? guidances
				: new ArrayList<Guidance>());
	}

	/**
	 * @param iconURL
	 *            The iconURL to set.
	 */
	public void setIconURL (URL iconURL)
	{
		this.iconURL = iconURL;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName (String name)
	{
		this.name = name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return this.name;
	}
}
