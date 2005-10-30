/*
 * $Id: Process.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.util.Collection;
import java.util.HashSet;

/**
 * Processus
 * 
 * @author MoOky
 */
public class Process extends ProcessElement
{
    /**
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     */
    public Process(String id, String name, URL fileURL, URL iconURL)
    {
        super(id, name, fileURL, iconURL);
    }

    /**
     * Liste des rôles liés à ce processus
     */
    private Collection<Role> roles = new ArrayList<Role>();

    /**
     * Liste des outils configures
     */
    private Collection<Tool> tools = new ArrayList<Tool>();

    /**
     * Retourne l'attribut roles
     * 
     * @return roles
     */
    public Collection<Role> getRoles()
    {
        return this.roles;
    }

    /**
     * Initialise l'attribut roles
     * 
     * @param aRole
     *            la valeur a attribuer.
     */
    public void setRoles(Collection<Role> aRole)
    {
        this.roles = (aRole != null ? aRole : new ArrayList<Role>());
    }

    /**
     * Ajoute le rôle spécifié à la liste des rôles de ce processus
     * 
     * @param role
     *            Rôle à ajouter au processus
     */
    public void addRole(Role role)
    {
        if (!this.roles.contains(role))
            this.roles.add(role);
    }

    /**
     * @param arrRoles
     *            liste des r?les pour lesquels on veut faire le listing
     * @return tous les composants de processus (sans doublon) du processus,
     *         pour les r?les donn?s
     */
    public Collection<ProcessComponent> getAllUniqueProcessComponentsForRoles(
                                                                              Collection<Role> arrRoles)
    {
        // variable HashSet, simplement pour g?rer plus facilement les doublons
        // !
        HashSet<ProcessComponent> hsProcessComponents = new HashSet<ProcessComponent>();
        for (Role role : this.getRoles())
        {
            // si c'est un des r?les "autoris?s"
            if (arrRoles.contains(role))
            {
                for (Activity activity : role.getActivities())
                {
                    hsProcessComponents.add(activity.getWorkDefinition()
                            .getProcessComponent());
                }
            }
        }
        // convertir la hashset en arraylist pour le retour de la fonction
        return hsProcessComponents;
    }

    /**
     * Permet de recuperer tous les produits du processus sans les doublons
     * 
     * @return arraylist des produits
     */
    public Collection<Product> getAllProducts()
    {
        ArrayList<Product> arrProducts = new ArrayList<Product>();
        for (Role role : this.getRoles())
        {
            for (Activity activity : role.getActivities())
            {
                for (Product product : activity.getInputProducts())
                {
                    if (!arrProducts.contains(product))
                    {
                        arrProducts.add(product);
                    }
                }
                for (Product product : activity.getOutputProducts())
                {
                    if (!arrProducts.contains(product))
                    {
                        arrProducts.add(product);
                    }
                }
            }
        }
        return arrProducts;
    }

    /**
     * Retourne tous les outils utilises par le processus
     * 
     * @return arraylist des outils
     */
    public Collection<Tool> getUsedTools()
    {
        ArrayList<Tool> arrTools = new ArrayList<Tool>();
        Collection<Product> arrProduct = this.getAllProducts();
        for (Product product : arrProduct)
        {
            Tool tool = product.getEditor();
            if (tool != null && !arrTools.contains(tool))
            {
                arrTools.add(tool);
            }
        }
        return arrTools;

    }

    /**
     * Retourne les outils configures dans le processus
     * 
     * @return arraylist des outils
     */
    public Collection<Tool> getTools()
    {
        return this.tools;
    }

    /**
     * Initialise l'attribut tools
     * 
     * @param tools
     *            la valeur a attribuer.
     */
    public void setTools(Collection<Tool> tools)
    {
        this.tools = (tools != null ? tools : new ArrayList<Tool>());
    }

    /**
     * @return collection de toutes les activités du processus
     */
    public Collection<Activity> getAllActivities()
    {
        ArrayList<Activity> arrAllActivities = new ArrayList<Activity>();
        for (Role r : this.getRoles())
        {
            for (Activity a : r.getActivities())
            {
                if (!arrAllActivities.contains(a))
                {
                    arrAllActivities.add(a);
                }
            }
        }
        return arrAllActivities;
    }
}
