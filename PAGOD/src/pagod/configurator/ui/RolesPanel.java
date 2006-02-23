/* 
 * $Id: RolesPanel.java,v 1.3 2006/02/23 01:43:15 psyko Exp $
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
import javax.swing.table.AbstractTableModel;

import pagod.common.model.Process;
import pagod.common.model.Role;

/**
 * Panneau de configuration des roles
 * 
 * @author baloo
 * 
 */
public class RolesPanel extends JPanel
{
	// panel contenant la JTable
	private JPanel	pCentral;

	// Jtable
	private JTable	tableRoles;

	/**
	 * Constructeur du panneaux de configuration des roles
	 * 
	 * @param process
	 *            Processus pour lequel il faut cr?er des ?tapes.
	 */
	public RolesPanel (Process process)
	{
		super();

		// cr?ation de la JTable
		RolesTableModel tm = new RolesTableModel(process);
		this.tableRoles = new JTable(tm);

		// panel contenant la JTable
		this.pCentral = new JPanel();
		// JScrollPane contenant la JTable
		JScrollPane jspTableRoles = new JScrollPane(this.tableRoles);
		this.pCentral.add(jspTableRoles, BorderLayout.CENTER);

		// positionnement des panels
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		this.add(this.pCentral, BorderLayout.CENTER);

	}

	private class RolesTableModel extends AbstractTableModel
	{
		// arraylist contenant toutes les roles du modele
		private List<Role>	lRoles	= new ArrayList<Role>();
		
		/**
	     * classe des colonnes (plus facile ? maintenir en passant par un tableau de
	     * la sorte)
	     */
	    private Class[] columnClasses = { String.class, Boolean.class };

		RolesTableModel (Process process)
		{
			super();

			this.lRoles.addAll(process.getRoles());
		}

		/**
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount ()
		{
			return this.lRoles.size();
		}

		/**
		 * @return int
		 * 
		 */
		public int getColumnCount ()
		{
			return 2;
		}

		/**
		 * @param rowIndex 
		 * @param columnIndex 
		 * @return Object
		 * 
		 */
		public Object getValueAt (int rowIndex, int columnIndex)
		{

			if (columnIndex == 0)
			{
				return this.lRoles.get(rowIndex).getName();
			}
			else
			{
				return (new Boolean(this.lRoles.get(rowIndex).isActivate()));
			}
		}
		
		/**
		 * @param aValue 
		 * @param rowIndex 
		 * @param columnIndex 
		 * 
		 */		 
		public void setValueAt (Object aValue, int rowIndex, int columnIndex)
		{

			if (columnIndex == 1)
			{
				Boolean bActivate = (Boolean)aValue;
				this.lRoles.get(rowIndex).setActivate(bActivate.booleanValue());
			}
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable (int row, int col)
		{
			// temps editables
			if (col == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		/**
		 * @param columnIndex 
		 * @return  Class
		*/
		public Class getColumnClass (int columnIndex)
		{
			return this.columnClasses[columnIndex];
		}
	}
}
