/*
 * $Id: WorkDefinition.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
 * D�finition de travail (d�composition d'un sous procesus ou composant)
 * 
 * @author MoOky
 */
public class WorkDefinition extends ProcessElement
{

    /**
     * Liste des activit�s qui composent la d�finition de travail
     */
    private List<Activity> activities = new ArrayList<Activity>();

    /**
     * Composant associ� � la d�finition de travail
     */
    private ProcessComponent processComponent = null;

    /**
     * Constructeur complet d'une definition de travail
     * 
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     * @param activities
     */
    public WorkDefinition(String id, String name, URL fileURL, URL iconURL,
            List<Activity> activities) 
    {
        super(id, name, fileURL, iconURL);
        
        this.setActivities(activities);
    }

    /**
     * Retourne l'attribut activities
     * 
     * @return activities.
     */
    public List<Activity> getActivities()
    {
        return this.activities;
    }

    /**
     * Initialise l'attribut activities
     * 
     * @param activities
     *            la valeur a attribuer.
     */
    public void setActivities(List<Activity> activities)
    {
        if (this.activities == activities)
            return;
        
        final List<Activity> oldActivities = this.activities;
        
        // blocage de la r�cursivit�
        this.activities = new ArrayList<Activity>();
        
        // suppression des anciennes liaisons
        for (Activity a : oldActivities)
        {
            a.setWorkDefinition(null);
        }
        
        // cr�ation des nouvelles liaisons
        if (activities != null)
        {
            this.activities = activities;
            
            for (Activity a : activities)
            {
                a.setWorkDefinition(this);
            }
        }
    }

    /**
     * Ajoute l'activit� sp�cifi�e � cette d�finition de travail
     * @param activity Activit� � ajouter
     */
    public void addActivity(Activity activity)
    {
        if (!this.activities.contains(activity))
        {
            this.activities.add(activity);
            
            activity.setWorkDefinition(this);
        }
    }
    
    /**
     * Supprime l'activit� sp�cifi�e de cette d�finition de travail
     * @param activity Activit� � supprimer
     */
    public void removeActivity(Activity activity)
    {
        if (this.activities.remove(activity))
        {
            activity.setWorkDefinition(null);
        }
    }

    /**
     * @return composant de processus associ�
     */
    public ProcessComponent getProcessComponent()
    {
        return this.processComponent;
    }

    /**
     * @param processComponent
     *            The processComponent to set.
     */
    public void setProcessComponent(ProcessComponent processComponent)
    {
        if (this.processComponent == processComponent)
            return;
        
        final ProcessComponent oldProcessComponent = this.processComponent;
        
        // blocage de la r�cursivit�
        this.processComponent = null;
        
        // suppression de l'ancienne liaison si existante
        if (oldProcessComponent != null)
            oldProcessComponent.removeWorkDefinition(this);
        
        // cr�ation de la nouvelle liaison
        if (processComponent != null)
        {
            this.processComponent = processComponent;
            processComponent.addWorkDefinition(this);
        }
    }
}
