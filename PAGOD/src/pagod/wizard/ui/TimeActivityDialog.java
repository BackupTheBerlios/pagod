/*
 * Projet PAGOD
 * 
 * $Id: TimeActivityDialog.java,v 1.12 2006/03/04 15:54:32 biniou Exp $
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
 * @author ppmaxynoob et biniou
 */
public class TimeActivityDialog extends JDialog implements ActionListener
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
		//this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		
//		 panel qui va contenir la JTable
		this.pCentral = new JScrollPane(this.table);
		this.pCentral.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.pCentral.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		

		
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
		add(this.pCentral, BorderLayout.CENTER);
		add(this.pButton, BorderLayout.SOUTH);

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
			alAct.addAll(ApplicationManager.getInstance().getCurrentProcess()
					.getAllActivities());
			ArrayList<HashMap<Integer,TimeCouple>> alH = null;
			alH = ((MaTableModel) TimeActivityDialog.this.table.getModel())
					.getAlTime();
			
			int iteration = ApplicationManager.getInstance().getCurrentProject().getItCurrent();
			int i = 0;
			for (i = 0; i < alH.size(); i++)
			{
				// recuperation du timecouple de la hm provisoire
				TimeCouple tc = (alH.get(i)).get(iteration);			
				alAct.get(i).sethmTime(iteration,tc);
				
				//alAct.get(i).setTime(alT.get(i));
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
		private ArrayList<HashMap<Integer,TimeCouple>>	alTime	= new ArrayList<HashMap<Integer,TimeCouple>>();

		MaTableModel ()
		{
			super();
			int i = 0;
			this.alActivities.addAll(ApplicationManager.getInstance()
					.getCurrentProcess().getAllActivities());

			// initialisation de la liste des temps
			for (Activity currentActivity : this.alActivities)
			{
				// recuperation du temps de l'activité
				HashMap<Integer,TimeCouple> hmTime = new HashMap<Integer,TimeCouple>();
				hmTime = currentActivity.getHM();
				this.alTime.add(i, hmTime);
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
			return 3;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt (int rowIndex, int columnIndex)
		{
			String cellValue = null;
			// iteration courante
			int it = ApplicationManager.getInstance().getCurrentProject()
					.getItCurrent();

			// on recupere les noms des activites et les temps associes
			if (columnIndex == 0)
			{
				cellValue = (this.alActivities.get(rowIndex)).getName();
			}
			else if (columnIndex == 1)
			{
				// affichage du tps passé sous la forme h:m:s

				// on recupere la hashmap de l'activité
				HashMap hmTime = new HashMap();
				hmTime = this.alTime.get(rowIndex);

				// en fonction de l'ité courante, on recupere le temps passé
				TimeCouple tc = (TimeCouple) hmTime.get(it);
				cellValue = TimerManager.stringFromTime(tc.getTimeElapsed());
				cellValue = TimerManager.displayTimeWithoutSeconds(cellValue) ;
			}
			else
			{
				// affichage du tps prévu sous la forme h:m:s

				// on recupere la hashmap de l'activité
				HashMap hmTime = this.alTime.get(rowIndex);

				// en fonction de l'ité courante, on recupere le temps passé
				TimeCouple tc = (TimeCouple) hmTime.get(it);
				cellValue = TimerManager.stringFromTime(tc.getTimeRemaining());
				cellValue = TimerManager.displayTimeWithoutSeconds(cellValue) ;
			}

			return (cellValue);
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
						"TimeActivityColumnNameActivity");
			}
			else if (iColumnIndex == 1)
			{
				return LanguagesManager.getInstance().getString(
						"TimeActivityColumnNameTimeElapsed");
			}
			else
			{
				return LanguagesManager.getInstance().getString(
				"TimeActivityColumnNameTimeRemaining");
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
			// verification que la chaine rentrée soit de la bonne forme

			boolean isValid = false;
			String val = String.valueOf(value);
			isValid = val.matches("[0-9]+:[0-5]?[0-9]");

			// sauver les modifs dans l'arraylist
			if (isValid)
			{
				int time = TimerManager.timeFromStringWithoutSeconds(String.valueOf(value));
				HashMap<Integer,TimeCouple> hm = new HashMap<Integer,TimeCouple>();
				int it = ApplicationManager.getInstance().getCurrentProject()
				.getItCurrent();
				
				if (columnIndex ==1)
				{
					// temps passé
					// on recupere le tps prévu pour ne pas le changer
					TimeCouple tc = (this.alTime.get(rowIndex)).get(it);
					int remainingTime = tc.getTimeRemaining();
					
					// on crée un nouveau couple avec l'ancien temps restant et
					// le nouveau temps passé
					TimeCouple tcEdit = new TimeCouple(time, remainingTime);
					hm.put(it,tcEdit);
					this.alTime.set(rowIndex,hm);
					
				}
				else
				{
					// temps prévu
					// on recupere le tps passé pour ne pas le changer
					TimeCouple tc = (this.alTime.get(rowIndex)).get(it);
					int elapsedTime = tc.getTimeElapsed();
					
					// on crée un nouveau couple avec l'ancien temps restant et
					// le nouveau temps passé
					TimeCouple tcEdit = new TimeCouple(elapsedTime, time);
					hm.put(it,tcEdit);
					this.alTime.set(rowIndex,hm);
				}
				
				//this.alTime.set(rowIndex, i);
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
		public ArrayList<HashMap<Integer,TimeCouple>> getAlTime ()
		{
			// getteur
			return (this.alTime);
		}

	}
}
