/* 
 * $Id: RolesDialog.java,v 1.1 2006/03/02 22:25:53 themorpheus Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import pagod.common.model.Process;
import pagod.common.model.Role;
import pagod.common.ui.WorkspaceFileChooser;
import pagod.configurator.control.PreferencesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.ui.LanguageChooserPanel;
import pagod.wizard.ui.ViewerPanel;

/**
 * Panneau de configuration des roles
 * 
 * @author baloo
 * 
 */
public class RolesDialog extends JDialog implements ActionListener
{
	// panel contenant la JTable
	private JPanel	pCentral;

	// Jtable
	private JTable	tableRoles;

	// panneau contenant les boutons
	private JPanel	pButton		= new JPanel();
	private JFrame parentFrame = null;

	private JButton	bpExport	= null;
	private JButton	bpCancel	= null;

	/**
	 * Constructeur du panneaux de configuration des roles
	 * 
	 * @param process
	 *            Processus pour lequel il faut cr?er des ?tapes.
	 */
	public RolesDialog (JFrame parentFrame, Process process)
	{
		super(parentFrame);
		this.parentFrame = parentFrame;
		// bo�te de dialogue modale et centr�e par rapport � l'appelant
		this.setModal(true);

		// on met le titre
		this.setTitle(LanguagesManager.getInstance().getString(
				"RolesDialogTitle"));

		// cr?ation de la JTable
		RolesTableModel tm = new RolesTableModel(process);
		this.tableRoles = new JTable(tm);

		// panel contenant la JTable
		this.pCentral = new JPanel();
		// JScrollPane contenant la JTable
		JScrollPane jspTableRoles = new JScrollPane(this.tableRoles);
		this.pCentral.add(jspTableRoles, BorderLayout.CENTER);

		// creation des boutons Exporter et Quitter
		this.bpExport = new JButton(LanguagesManager.getInstance().getString(
				"ExportButtonLabel"));
		this.bpCancel = new JButton(LanguagesManager.getInstance().getString(
				"CancelButtonLabel"));

		// ajout des boutons
		this.pButton.add(this.bpExport);
		this.pButton.add(this.bpCancel);

		// positionnement des panels
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		this.add(this.pCentral, BorderLayout.CENTER);
		this.add(this.pButton, BorderLayout.SOUTH);

		// on sauvegarde les preferences pour ne pas perdre celle qui ont put
		// etre modifer ailleur qu'en passant par cette fenetre
		PreferencesManager.getInstance().storePreferences();

		this.pack();

		// bo�te de dialogue centr�e par rapport � l'appelant
		this.setLocationRelativeTo(parentFrame);

	}

	/**
	 * 
	 * @param e
	 *            evenement
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == this.bpExport)
		{
			((MainFrame) this.parentFrame).saveAsProcess();
		}
		else
		{
			this.dispose();
		}
	}

	private class RolesTableModel extends AbstractTableModel
	{
		// arraylist contenant toutes les roles du modele
		private List<Role>	lRoles			= new ArrayList<Role>();

		/**
		 * classe des colonnes (plus facile � maintenir en passant par un
		 * tableau de la sorte)
		 */
		private Class[]		columnClasses	= { String.class, Boolean.class };

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
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		public String getColumnName (int iColumnIndex)
		{
			// nom des colonnes
			if (iColumnIndex == 0)
			{
				return LanguagesManager.getInstance().getString(
						"RolesPanelColumnRoleName");
			}
			else
			{
				return LanguagesManager.getInstance().getString(
						"RolesPanelColumnRoleState");
			}
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
				Boolean bActivate = (Boolean) aValue;
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
		 * @return Class
		 */
		public Class getColumnClass (int columnIndex)
		{
			return this.columnClasses[columnIndex];
		}
	}
}
