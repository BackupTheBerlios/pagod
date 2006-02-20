/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityAllIterationsDialog.java,v 1.3 2006/02/20 09:04:44 yak Exp $
 */
package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;

/**
 * @author biniou
 * 
 */
public class TimeActivityAllIterationsDialog extends JDialog implements
		ActionListener
{
	// panel contenant la JTable
	private JScrollPane	pCentral	= null;

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
	public TimeActivityAllIterationsDialog (JFrame parentFrame)
	{
		super(parentFrame);

		// boite de dialogue modale
		this.setModal(true);

		// titre
		this.setTitle(LanguagesManager.getInstance().getString(
				"TimeActivityAllIterationsDialogTitle"));

		// creation de la JTable

		MaTableModel tm = new MaTableModel();
		this.table = new JTable();
		this.table.setModel(tm);

		// panel qui va contenir la JTable
	

		this.pCentral  = new JScrollPane(this.table);
		this.pCentral.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.pCentral.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//this.pCentral.add(jspTable, BorderLayout.CENTER);

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
		this.getContentPane().add(this.pCentral , BorderLayout.CENTER);
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
		if (b == TimeActivityAllIterationsDialog.this.pbOk)
		{
			// on a cliqu? sur ok
			// on recupere la liste de temps du tablemodel
			// et on stocke tous les temps dans les activites
			ArrayList<Activity> alAct = new ArrayList<Activity>();
			alAct.addAll(ApplicationManager.getInstance().getCurrentProcess()
					.getAllActivities());
			ArrayList<HashMap<Integer, TimeCouple>> alTime = null;
			alTime = ((MaTableModel) TimeActivityAllIterationsDialog.this.table
					.getModel()).getAlTime();

			int i = 0;
			for (i = 0; i < alTime.size(); i++)
			{
				// recuperation du timecouple de la hm provisoire
				alAct.get(i).setHM(alTime.get(i));

				// alAct.get(i).setTime(alT.get(i));
			}

			// on ferme la fenetre
			TimeActivityAllIterationsDialog.this.dispose();
		}
		else
		{
			// bouton annuler
			// on ne prend aucun changement en compte
			TimeActivityAllIterationsDialog.this.dispose();
		}

	}

	private class MaTableModel extends AbstractTableModel
	{
		// arraylist contenant toutes les activites du modele
		private ArrayList<Activity>						alActivities	= new ArrayList<Activity>();

		// arraylist contenant les temps associes aux activits
		private ArrayList<HashMap<Integer, TimeCouple>>	alTime			= new ArrayList<HashMap<Integer, TimeCouple>>();

		@SuppressWarnings("unchecked") MaTableModel ()
		{
			super();
			int i = 0;
			this.alActivities.addAll(ApplicationManager.getInstance()
					.getCurrentProcess().getAllActivities());

			// initialisation de la liste des temps
			for (Activity currentActivity : this.alActivities)
			{
				// recuperation du temps de l'activit?
				HashMap<Integer, TimeCouple> hmTime = (HashMap<Integer, TimeCouple>)(currentActivity.getHM().clone());
				
				this.alTime.add(i, hmTime);
				i++;
			}
		}

		/**
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount ()
		{
			// nombre de lignes
			return (this.alActivities.size());

		}

		/**
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount ()
		{
			// nombre de colonnes egal au nombre d'it?rations +1
			return (ApplicationManager.getInstance().getCurrentProject()
					.getItCurrent() + 1);
		}

		/**
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
				// affichage du tps pass? sous la forme h:m:s
				// on recupere la hashmap de l'activit?
				HashMap hmTime = new HashMap();
				hmTime = this.alTime.get(rowIndex);

				// en fonction de la colonne, on recupere le temps
				// pass?
				TimeCouple tc = (TimeCouple) hmTime.get(columnIndex);
				cellValue = TimerManager.stringFromTime(tc.getTimeElapsed());

			}

			return cellValue;
		}

		/**
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		public String getColumnName (int iColumnIndex)
		{
			// nom des colonnes
			if (iColumnIndex == 0)
			{
				return LanguagesManager.getInstance().getString(
						"TimeActivityColumnNameActivity");
			}
			else
			{
				return (LanguagesManager.getInstance().getString(
						"TimeActivityColumnNameTimeElapsedIterations") + iColumnIndex);
			}
		}

		/**
		 * 
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable (int row, int col)
		{
			// temps editables
			if (col == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		/**
		 * @param value
		 * @param rowIndex
		 * @param columnIndex
		 */
		public void setValueAt (Object value, int rowIndex, int columnIndex)
		{
			// verification que la chaine rentr?e soit de la bonne forme

			boolean isValid = false;
			String val = String.valueOf(value);
			isValid = val.matches("[0-9]+:[0-5]?[0-9]:[0-5]?[0-9]");

			// sauver les modifs dans l'arraylist
			if (isValid)
			{
				int time = TimerManager.timeFromString(String.valueOf(value));
				HashMap<Integer, TimeCouple> hm = this.alTime.get(rowIndex);
				int it = ApplicationManager.getInstance().getCurrentProject()
						.getItCurrent();

				if (columnIndex == it)
				{
					// temps pass?
					// on recupere le tps pr?vu pour ne pas le changer
					// puisqu'on est dans l'it?ration courante
					TimeCouple tc = (this.alTime.get(rowIndex)).get(it);
					int remainingTime = tc.getTimeRemaining();

					// on cr?e un nouveau couple avec l'ancien temps restant et
					// le nouveau temps pass?
					TimeCouple tcEdit = new TimeCouple(time, remainingTime);
					hm.put(columnIndex, tcEdit);
					this.alTime.set(rowIndex, hm);
				}
				else
				{
					// on cr?e un nouveau couple avec 0 en temps restant et
					// le nouveau temps pass?
					TimeCouple tcEdit = new TimeCouple(time, 0);
					hm.put(columnIndex, tcEdit);
					this.alTime.set(rowIndex, hm);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(ApplicationManager.getInstance()
						.getMfPagod(), LanguagesManager.getInstance()
						.getString("EditTimeException"), LanguagesManager
						.getInstance().getString("EditTimeErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * @return arraylist des temps
		 */
		public ArrayList<HashMap<Integer, TimeCouple>> getAlTime ()
		{
			// getteur
			return (this.alTime);
		}

	}
}
