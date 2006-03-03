/* 
 * $Id: RolesDialog.java,v 1.2 2006/03/03 16:01:17 themorpheus Exp $
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pagod.common.control.InterfaceManager;
import pagod.common.model.Process;
import pagod.common.model.Role;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.PreferencesManager;
import pagod.utils.LanguagesManager;

/**
 * Panneau de configuration des roles
 * 
 * @author baloo
 * 
 */
public class RolesDialog extends JDialog
{
	// panel contenant la JTable
	private JPanel		pCentral;

	// Jtable
	private JTable		tableRoles;

	// panneau contenant les boutons
	private JPanel		pButton		= new JPanel();
	private Process		process		= null;

	private JButton		bpExport	= null;
	private JButton		bpCancel	= null;

	/**
	 * Constructeur du panneaux de configuration des roles
	 * 
	 * @param parentFrame
	 * 
	 * @param process
	 *            Processus pour lequel il faut cr?er des ?tapes.
	 * 
	 */
	public RolesDialog (MainFrame parentFrame, Process process)
	{
		super(parentFrame);
		this.process = process;

		// bo?te de dialogue modale et centr?e par rapport ? l'appelant
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
				"RolesExportButtonLabel"));
		this.bpCancel = new JButton(LanguagesManager.getInstance().getString(
				"RolesCancelButtonLabel"));

		// ajout des boutons
		this.pButton.add(this.bpExport);
		this.pButton.add(this.bpCancel);

		this.bpExport.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (RolesDialog.this
						.exportProcessAction(RolesDialog.this.process))
				{
					// TODO debug alex
					System.out.println("role du model baloo : ");
					for (Role r : RolesDialog.this.process.getRoles())
					{
						System.out.println(r.getName() + " : " + r.isActivate());
					}
					
					System.out.println("role du du dpc : ");
					for (Role r : ApplicationManager.getInstance().getCurrentProcess().getRoles())
					{
						System.out.println(r.getName() + " : " + r.isActivate());
					}
					
					
					
					RolesDialog.this.dispose();
				}
			}
		});
		this.bpCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				RolesDialog.this.dispose();
			}
		});
		// positionnement des panels
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		this.add(this.pCentral, BorderLayout.CENTER);
		this.add(this.pButton, BorderLayout.SOUTH);

		// on sauvegarde les preferences pour ne pas perdre celle qui ont put
		// etre modifer ailleur qu'en passant par cette fenetre
		PreferencesManager.getInstance().storePreferences();

		this.pack();

		// bo?te de dialogue centr?e par rapport ? l'appelant
		this.setLocationRelativeTo(parentFrame);

	}

	/**
	 * Export d'un processus dont les roles ont ete redefini le modele
	 * 
	 * @param p
	 * @return vrai si le fichier a bien ete enregistre
	 */
	public boolean exportProcessAction (Process p)
	{

		ProcessOutputFileChooser fileChooser = new ProcessOutputFileChooser();
		String savePath = null;
		// Chemin de sauvegarde
		do
		{
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				// on verifie si le fichier existe deja
				if (fileChooser.getSelectedFile().exists())
				{
					// si il existe on demande confirmation
					if (JOptionPane.showConfirmDialog(this, LanguagesManager
							.getInstance().getString(
									"eraseFileConfirmationMessage"),
							LanguagesManager.getInstance().getString(
									"eraseFileConfirmationTitle"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
					{
						// si on souhaite ecrase
						savePath = fileChooser.getSelectedFile().getPath();
					}
				}
				// si le fichier n'existe pas
				else
				{
					savePath = fileChooser.getSelectedFile().getPath();
				}
			}
			// si l'utilisateur appuie sur Annuler
			else
			{
				// on arrete tout
				return false;
			}
		} while (savePath == null);
		return InterfaceManager.getInstance().exportProcess(savePath, p, this);
	}
	private class RolesTableModel extends AbstractTableModel
	{
		// arraylist contenant toutes les roles du modele
		private List<Role>	lRoles			= new ArrayList<Role>();

		/**
		 * classe des colonnes (plus facile ? maintenir en passant par un
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
		public Class<?> getColumnClass (int columnIndex)
		{
			return this.columnClasses[columnIndex];
		}
	}
}
