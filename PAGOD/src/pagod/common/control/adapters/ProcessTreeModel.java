/* 
 * $Id: ProcessTreeModel.java,v 1.2 2006/02/04 22:42:06 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of SPWIZ.
 *
 * SPWIZ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SPWIZ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SPWIZ; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.common.control.adapters;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.model.ProcessComponent;
import pagod.common.model.Role;
import pagod.common.model.WorkDefinition;

/**
 * Classe permettant d'adapter un processus pour en faire le modele d'un JTree
 * 
 * @author Benjamin
 */
public class ProcessTreeModel extends DefaultTreeModel
{
    /**
     * le processus à adapter
     */
    private Process process;

    /**
     * les roles "utiles" que l'utilisateur a choisi d'endosser sur le processus
     * process
     */
    private Collection<Role> arrRoles;

    /**
     * Constructeur d'un ProcessTreeModel
     * 
     * @param process
     *            Processus à adapter
     * @param arrRoles
     *            Roles utiles
     */
    public ProcessTreeModel(Process process, Collection<Role> arrRoles)
    {
        super(null);
        // le processus et les roles "utiles" passés en paramètres doivent être
        // "sauvegardés"
        this.process = process;
        this.arrRoles = arrRoles;
        if (this.arrRoles == null) {
            this.arrRoles = new ArrayList<Role>() ;
        }

        this.root = new DefaultMutableTreeNode("racine");

        Collection<ProcessComponent> arrProcessComponents = this.process
                .getAllUniqueProcessComponentsForRoles(arrRoles);

        for (ProcessComponent pc : arrProcessComponents)
        {
            DefaultMutableTreeNode processComponentNode = new DefaultMutableTreeNode(
                    pc);
            // ajouter sous le process component les work definitions
            for (WorkDefinition wd : pc.getWorksDefinitions())
            {
                DefaultMutableTreeNode workDefinitionNode = new DefaultMutableTreeNode(
                        wd);
                
                // ajouter sous la work definition les activités (vérifier
                // qu'elles sont bien pour un role autorisés
                for (Activity a : wd.getActivities())
                {
                    
                	if (this.arrRoles.contains(a.getRole()))
                    {
                        workDefinitionNode.add(new DefaultMutableTreeNode(a));
                    }
                }
                // s'il y a au moins une activité, ajouter le noeud "WD"
                if (workDefinitionNode.getChildCount() != 0)
                {
                    processComponentNode.add(workDefinitionNode);
                }

            }
            // s'il y a au moins une "WD", ajouter le noeud "PC", ajouter le
            // process component sous la racine
            if (processComponentNode.getChildCount() != 0)
            {
                ((DefaultMutableTreeNode) this.root).add(processComponentNode);
            }
        }

    }

}
