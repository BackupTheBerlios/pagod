/* 
 * $Id: RolesPanel.java,v 1.1 2006/02/16 22:22:22 themorpheus Exp $
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

package pagod.configurator.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pagod.common.model.Process;
import pagod.common.model.Role;
import pagod.wizard.control.ApplicationManager;

/**
 * Panneau de configuration des roles
 * @author baloo
 *
 */
public class RolesPanel extends JPanel
{
	// panel contenant la JTable
	private JPanel	pCentral;

	// Jtable
	private JTable tableRoles;
	
	/**
     * Constructeur du panneaux de configuration des roles
     * 
     * @param process
     *            Processus pour lequel il faut créer des étapes.
     */
    public RolesPanel(Process process)
    {
    	super();
    	
    	// création de la JTable
    	RolesTableModel tm = new RolesTableModel();
		this.tableRoles = new JTable(tm);

		// panel contenant la JTable
		this.pCentral = new JPanel();
		// JScrollPane contenant la JTable
		JScrollPane jspTableRoles = new JScrollPane(this.tableRoles);
		this.pCentral.add(jspTableRoles,BorderLayout.CENTER);

		// positionnement des panels
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		this.add(this.pCentral,BorderLayout.CENTER);
		
    }
    
    private class RolesTableModel extends DefaultTableModel
	{
		// arraylist contenant toutes les roles du modele
		private List<Role> lRoles = new ArrayList<Role>();

		RolesTableModel()
		{
			super();
			
			/*this.lRoles.addAll(ApplicationManager.getInstance()
					.getCurrentProcess().getRoles());	*/		
		}
	}
}
