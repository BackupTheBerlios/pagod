/*
 * $Id: Activity.java,v 1.2 2005/11/14 23:37:22 psyko Exp $
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
import java.util.Collections;
import java.util.List;

/**
 * Activité d'un processus
 * 
 * @author MoOky
 */
public class Activity extends ProcessElement
{
    /**
     * Liste des étapes de l'activités
     */
    private List<Step> steps = new ArrayList<Step>();

    /**
     * Définition de travail qui contient l'activité
     */
    private WorkDefinition workDefinition = null;

    /**
     * Les produits en entrée de l'activité
     */
    private List<Product> inputProducts = new ArrayList<Product>();

    /**
     * Les produits en sortie de l'activité
     */
    private List<Product> outputProducts = new ArrayList<Product>();

    /**
     * Role qui doit réaliser l'activité
     */
    private Role role = null;

    /**
     * Constructeur complet d'une activite
     * 
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     * @param steps
     * @param workDefinition
     * @param inputProducts
     * @param outputProducts
     * @param role
     */
    public Activity(String id, String name, URL fileURL, URL iconURL,
                    List<Step> steps, WorkDefinition workDefinition,
                    List<Product> inputProducts,
                    List<Product> outputProducts, Role role)
    {
        super(id, name, fileURL, iconURL);

        this.setSteps(steps);
        this.setWorkDefinition(workDefinition);
        this.setInputProducts(inputProducts);
        this.setOutputProducts(outputProducts);
        this.setRole(role);
    }

    /**
     * Retourne l'attribut inputProducts
     * 
     * @return inputProducts.
     */
    public List<Product> getInputProducts()
    {
        return this.inputProducts;
    }

    /**
     * Initialise l'attribut inputProducts
     * 
     * @param inputProducts
     *            la valeur a attribuer.
     */
    public void setInputProducts(List<Product> inputProducts)
    {
        this.inputProducts = (inputProducts != null ? inputProducts
                : new ArrayList<Product>());
    }

    /**
     * Retourne l'attribut outputProducts
     * 
     * @return outputProducts.
     */
    public List<Product> getOutputProducts()
    {
        return this.outputProducts;
    }

    /**
     * Initialise l'attribut outputProducts
     * 
     * @param outputProducts
     *            la valeur a attribuer.
     */
    public void setOutputProducts(List<Product> outputProducts)
    {
        this.outputProducts = (outputProducts != null ? outputProducts
                : new ArrayList<Product>());
    }

    /**
     * Retourne l'attribut role
     * 
     * @return role.
     */
    public Role getRole()
    {
        return this.role;
    }

    /**
     * Initialise l'attribut role
     * 
     * @param role
     *            la valeur a attribuer.
     */
    public void setRole(Role role)
    {
        if (this.role == role)
            return;

        final Role oldRole = this.role;

        // blocage de la récursivité
        this.role = null;

        // suppression de l'ancienne liaison si existante
        if (oldRole != null)
            oldRole.removeActivity(this);

        // création de la nouvelle liaison
        if (role != null)
        {
            this.role = role;
            role.addActivity(this);
        }
    }

    /**
     * Retourne l'attribut steps
     * 
     * @return steps.
     */
    public List<Step> getSteps()
    {
        return this.steps;
    }

    /**
     * Initialise l'attribut steps
     * 
     * @param steps
     *            la valeur a attribuer.
     */
    public void setSteps(List<Step> steps)
    {
        this.steps = (steps != null ? steps : new ArrayList<Step>());
    }

    /**
     * Retourne l'attribut workDefinition
     * 
     * @return workDefinition.
     */
    public WorkDefinition getWorkDefinition()
    {
        return this.workDefinition;
    }

    /**
     * Initialise l'attribut workDefinition
     * 
     * @param workDefinition
     *            la valeur a attribuer.
     */
    public void setWorkDefinition(WorkDefinition workDefinition)
    {
        if (this.workDefinition == workDefinition)
            return;

        final WorkDefinition oldWorkDefinition = this.workDefinition;

        // blocage de la récursivité
        this.workDefinition = null;

        // suppression de l'ancienne liaison si existante
        if (oldWorkDefinition != null)
            oldWorkDefinition.removeActivity(this);

        // création de la nouvelle liaison
        if (workDefinition != null)
        {
            this.workDefinition = workDefinition;
            workDefinition.addActivity(this);
        }
    }

    /**
     * Pour savoir s'il y a des produits en entrées
     * 
     * @return true si l'activité a des produits en entrée
     */
    public boolean hasInputProducts()
    {
        return (!this.inputProducts.isEmpty());
    }

    /**
     * Pour savoir s'il y a des produits en sortie
     * 
     * @return true si l'activité a des produits en sortie
     */
    public boolean hasOutputProducts()
    {
        return (!this.outputProducts.isEmpty());
    }

    /**
     * Pour savoir si l'activité a besoin d'outils ou pas
     * 
     * @return true si l'activité nécessite l'utilisation outil particulier
     */
    public boolean needsTools()
    {
        if (!this.hasOutputProducts())
            return false;
        else
            for (Product t : this.outputProducts)
            {
                if (t.getEditor() != null)
                    return true;
            }
        return false;
    }

    /**
     * Pour savoir si l'activité est décomposé en étapes ou pas
     * 
     * @return vrai si l'activité a des étapes associées
     */
    public boolean hasSteps()
    {
        return (!this.steps.isEmpty());
    }

    /**
     * @param step
     *            étape à ajouter à l'activité (sans effet si cette étape est
     *            déjà une étape de l'activité)
     */
    public void addStep(Step step)
    {
        if (!this.steps.contains(step))
        {
            this.steps.add(step);
        }
    }
    
    /**
     * @param step
     * @param index
     */
    public void addStep(Step step, int index)
    {
        if (!this.steps.contains(step))
        {
            this.steps.add(index,step) ;
        }
    }
    /**
     * @param step
     *            étape à supprimer de l'activité (sans effet si step n'est pas
     *            une étape de l'activité)
     */
    public void removeStep(Step step)
    {
        if (this.steps.contains(step))
        {
            this.steps.remove(step);
        }
    }

    /**
     * Permute l'ordre de step et step2
     * 
     * @param step
     * @param step2
     */
    public void swapSteps(Step step, Step step2)
    {
        Collections.swap(this.steps, this.steps.lastIndexOf(step), this.steps
                .lastIndexOf(step2));
    }

    /**
     * @param step 
     *            étape à ajouter à l'activité (sans effet si cette étape est
     *            déjà une étape de l'activité)
     * @param index
     *          rang où ajouter l'étape 
     */

}
