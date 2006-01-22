/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityDialog.java,v 1.6 2006/01/22 08:23:23 biniou Exp $
 */
package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pagod.common.model.Activity;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;

/**
 * @author pp et biniou
 */
public class TimeActivityDialog extends JDialog implements ActionListener
{

	// panel contenant la JTable
	private JPanel	pCentral	= null;

	// panel qui contiendra les boutons du bas
	private JPanel	pButton		= null;

	// Bouton Valider
	private JButton	pbOk		= null;

	// Bouton Annuler
	private JButton	pbCancel	= null;

	// Jtable
	private JTable	table;
	

	/**
	 * @param parentFrame
	 */
	public TimeActivityDialog (JFrame parentFrame)
	{
		super(parentFrame);

		// boite de dialogue modale
		this.setModal(true);

		// titre
		this.setTitle(LanguagesManager.getInstance().getString(
				"TimeActivityDialogTitle"));

		// creation de la JTable

		MaTableModel tm = new MaTableModel();
		this.table = new JTable(tm);

		// panel qui va contenir la JTable
		this.pCentral = new JPanel();
		// jspTable.add(this.table);
		JScrollPane jspTable = new JScrollPane(this.table);
		this.pCentral.add(jspTable, BorderLayout.CENTER);
		// on ajoute la grille

		// panel contenant les boutons
		this.pButton = new JPanel();

		// creation des boutons Ok et annuler
		this.pbOk = new JButton(LanguagesManager.getInstance().getString(
				"OKButtonLabel"));
		this.pbCancel = new JButton(LanguagesManager.getInstance().getString(
				"CancelButtonLabel"));

		// ajout des boutons au panel
		this.pButton.add(this.pbOk);
		this.pButton.add(this.pbCancel);

		// association des listeners aux boutons
		this.pbOk.addActionListener(this);
		this.pbCancel.addActionListener(this);

		// positionnement des panel dans la JDialog
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.pCentral, BorderLayout.CENTER);
		this.getContentPane().add(this.pButton, BorderLayout.SOUTH);

		this.pack();

		// bo?te de dialogue centr?e par rapport ? l'appelant
		this.setLocationRelativeTo(parentFrame);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	public void actionPerformed (ActionEvent e)
	{
		// on recupere le bouton sur lequel on a clique grace a
		// l'ActionEvent e
		JButton b = (JButton) e.getSource();

		// si on a cliqu? sur le bouton ok
		if (b == TimeActivityDialog.this.pbOk)
		{
			// on a cliqu? sur ok			
			// on recupere la liste de temps du tablemodel 
			// et on stocke tous les temps dans les activites
			ArrayList<Activity> alAct = new ArrayList<Activity>();
			alAct.addAll(ApplicationManager.getInstance().getCurrentProcess().getAllActivities());
			ArrayList<Integer> alT = null;
			alT = ((MaTableModel)TimeActivityDialog.this.table.getModel()).getAlTime();
			int i = 0;
			for (i =0; i<alT.size(); i++)
			{
				alAct.get(i).setTime(alT.get(i));				
			}
			
			// on ferme la fenetre
			TimeActivityDialog.this.dispose();
		}
		else
		{
			// bouton annuler
			// on ne prend aucun changement en compte
			TimeActivityDialog.this.dispose();
		}

	}

	private class MaTableModel extends AbstractTableModel
	{
		// arraylist contenant toutes les activites du modele
		private ArrayList<Activity>	alActivities	= new ArrayList<Activity>();
		
		// arraylist contenant les temps associes aux activits
		private ArrayList<Integer> alTime = new ArrayList<Integer>();
		
		MaTableModel ()
		{
			super();
			int i =0;
			this.alActivities.addAll(ApplicationManager.getInstance()
					.getCurrentProcess().getAllActivities());
			
			// initialisation de la liste des temps
			for (Activity currentActivity : this.alActivities)
			{
				this.alTime.add(i,currentActivity.getTime());
				i++;
			}			
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount ()
		{
			// nombre de lignes
			return (this.alActivities.size());

		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount ()
		{
			// nombre de colonnes
			return 2;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt (int rowIndex, int columnIndex)
		{
			String cellValue = null;
			
			// on recupere les noms des activites et les temps associes
			if (columnIndex == 0)
			{
				cellValue = (this.alActivities.get(rowIndex)).getName();
			}
			else
			{
				cellValue = Integer.toString((this.alTime.get(rowIndex)));
			}

			return cellValue;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		public String getColumnName (int iColumnIndex)
		{
			// nom des colonnes
			if (iColumnIndex == 0) return LanguagesManager.getInstance().getString(
			"TimeActivityColumnNameActivity");
			else
				return LanguagesManager.getInstance().getString(
				"TimeActivityColumnNameTime");
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable (int row, int col)
		{
			// temps editables
			if (col == 0) return false;
			else
				return true;
		}

		/**
		 * @param value
		 * @param rowIndex
		 * @param columnIndex
		 */
		public void setValueAt (Object value, int rowIndex, int columnIndex)
		{
			// TODO ajouter un formatter pour n'accepter que les int
			
			// sauver les modifs dans l'arraylist
			int i = Integer.parseInt(String.valueOf(value));

			this.alTime.set(rowIndex,i);
		}
		
		/**
		 * @return arraylist des temps
		 */
		public ArrayList<Integer> getAlTime()
		{
			// getteur
			return(this.alTime);
		}

	}
}
