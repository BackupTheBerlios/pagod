/*
 * $Id: ProcessComponent.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
 * Composant ou sous processus
 * 
 * @author MoOky
 */
public class ProcessComponent extends ProcessElement
{

    /**
     * Liste des définitions de travail qui compose ce composant
     */
    private List<WorkDefinition> worksDefinitions = new ArrayList<WorkDefinition>();

    /**
     * Constructeur complet d'un sous-processus
     * 
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     * @param workDefinitions
     */
    public ProcessComponent(String id, String name, URL fileURL, URL iconURL,
            List<WorkDefinition> workDefinitions)
    {
        super(id, name, fileURL, iconURL);
        
        this.setWorksDefinitions(workDefinitions);
    }

    /**
     * Retourne l'attribut workDefinitions
     * 
     * @return worksDefinitions.
     */
    public List<WorkDefinition> getWorksDefinitions()
    {
        return this.worksDefinitions;
    }

    /**
     * Initialise l'attribut workDefinitions
     * 
     * @param worksDefinitions
     *            la valeur a attribuer.
     */
    public void setWorksDefinitions(List<WorkDefinition> worksDefinitions)
    {
        if (this.worksDefinitions == worksDefinitions)
            return;
        
        final List<WorkDefinition> oldWorkDefinitions = this.worksDefinitions;
        
        // blocage de la récursivité
        this.worksDefinitions = new ArrayList<WorkDefinition>();
        
        // suppression des anciennes liaisons
        for (WorkDefinition wd : oldWorkDefinitions)
        {
            wd.setProcessComponent(null);
        }
        
        // création des nouvelles liaisons
        if (worksDefinitions != null)
        {
            this.worksDefinitions = worksDefinitions;
            
            for (WorkDefinition wd : worksDefinitions)
            {
                wd.setProcessComponent(this);
            }
        }
    }

    /**
     * Ajoute la définition de travail spécifiée à ce composant de processus
     * @param workDefinition Définition de travail à ajouter
     */
    public void addWorkDefinition(WorkDefinition workDefinition)
    {
        if (!this.worksDefinitions.contains(workDefinition))
        {
            this.worksDefinitions.add(workDefinition);
            
            workDefinition.setProcessComponent(this);
        }
    }
    
    /**
     * Supprime la définition de travail spécifiée de ce composant de processus
     * @param workDefinition Définition de travail à supprimer
     */
    public void removeWorkDefinition(WorkDefinition workDefinition)
    {
        if (this.worksDefinitions.remove(workDefinition))
        {
            workDefinition.setProcessComponent(null);
        }
    }
}
