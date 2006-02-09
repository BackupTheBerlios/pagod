/*
 * Projet PAGOD
 * 
 * $Id: TimeEditDialog.java,v 1.5 2006/02/09 17:52:17 biniou Exp $
 */

package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;

/**
 * @author psyko JDialog permettant l'edition des temps (effectué et à faire)
 *         pour une activité Chargée lors du lancement d'une activité ou de la
 *         terminaison d'une activité
 * 
 * Récupère les temps d'une HashMap d'Activity => constructeur paramétré
 */
public class TimeEditDialog extends JDialog
{
	// attributs de la classe ...
	// deux boutons, (valider et annuler)
	private JButton		pbValidate;
	private JButton		pbCancel;

	// deux zones de texte
	private JLabel		lEstimatedTime;
	private JLabel		lPassedTime;

	// et deux champs de saisie
	private JTextField	tfEstimatedTime;
	private JTextField	tfPassedTime;

	// panneaux qui contiendront les composants de la JDialog
	private JPanel		pButton;
	private JPanel		pEast;
	private JPanel		pWest;

	// activité dont on configure les temps
	private Activity	activity;

	/**
	 * constructeur prenant en paramètre le temps effectué et le temps estimé...
	 * 
	 * @param dParent
	 * @param activity
	 */
	public TimeEditDialog (JFrame dParent, Activity activity)
	{
		super(dParent, LanguagesManager.getInstance().getString(
				"TimeEditDialogTitle")
				+ activity.getName(), true);

		// initialisation de notre attribut activity
		this.activity = activity;

		// creation de nos 2 boutons
		this.pbValidate = new JButton(LanguagesManager.getInstance().getString(
				"OKButtonLabel"));
		this.pbCancel = new JButton(LanguagesManager.getInstance().getString(
				"CancelButtonLabel"));

		// Récuperation de l'ité courante
		int iCurrentIt = ApplicationManager.getInstance().getCurrentProject()
				.getItCurrent();

		// creation de nos 2 JTextField
		// initialisés avec les valeurs de la HMap de Activity
		this.tfEstimatedTime = new JTextField(TimerManager
				.stringFromTime(this.activity.gethmTime(iCurrentIt)
						.getTimeRemaining()),8);

		this.tfPassedTime = new JTextField(TimerManager
				.stringFromTime(this.activity.gethmTime(iCurrentIt)
						.getTimeElapsed()),8);

		// creation de nos 2 JLabel
		this.lEstimatedTime = new JLabel(LanguagesManager.getInstance()
				.getString("EstimatedTimeLabel")
				+ " :");
		this.lPassedTime = new JLabel(LanguagesManager.getInstance().getString(
				"PassedTimeLabel")
				+ " :");

		// panel contenant les boutons
		this.pButton = new JPanel();
		this.pButton.add(this.pbValidate);
		this.pButton.add(this.pbCancel);

		// association des listeners aux boutons
		this.pbValidate.addActionListener(new ListenerBoutons());
		this.pbCancel.addActionListener(new ListenerBoutons());

		// création du panel contenant les JLabel
		this.pWest = new JPanel();
		this.pWest.setLayout(new BorderLayout());
		this.pWest.add(this.lEstimatedTime, BorderLayout.NORTH);
		this.pWest.add(this.lPassedTime, BorderLayout.SOUTH);

		// creation du panel contenant les JTextField
		this.pEast = new JPanel();
		this.pEast.setLayout(new BorderLayout());
		this.pEast.add(this.tfEstimatedTime, BorderLayout.NORTH);
		this.pEast.add(this.tfPassedTime, BorderLayout.SOUTH);

		// positionnement des panel dans la JDialog
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.pButton, BorderLayout.SOUTH);
		this.getContentPane().add(this.pWest, BorderLayout.WEST);
		this.getContentPane().add(this.pEast, BorderLayout.EAST);

		this.addWindowListener(new WindowAdapter()
		{
			// on redefinit la methode windowsClosing heritee de WindowAdapter
			void windowsClosing (WindowEvent e)
			{
				// on ferme la fenetre une inner class peut acceder aux
				// attributs de la classe qui la contient en donnant sont nom
				// suivit de .this puis le nom de l'attribut ou de la methode
				TimeEditDialog.this.dispose();

			}
		});

		this.pack();

		// bo?te de dialogue centree par rapport a l'appelant
		this.setLocationRelativeTo(dParent);
		this.setVisible(true);

	}

	/**
	 * méthode de validation des temps enregistre dans la HashMap de l'activité
	 * les temps passé et à faire
	 * 
	 * @return true si les temps sont au bon format et qu'on a bien changé
	 *         qqchose false sinon
	 */
	public boolean validateDialog ()
	{
		// verifie que les valeurs sont au bon format
		boolean isValid = false;
		String sElapsedTime = this.tfPassedTime.getText();
		String sEstimatedTime = this.tfEstimatedTime.getText();

		isValid = (sElapsedTime.matches("[0-9]+:[0-5]?[0-9]:[0-5]?[0-9]"))
				&& (sEstimatedTime.matches("[0-9]+:[0-5]?[0-9]:[0-5]?[0-9]"));

		if (isValid)
		{
			// 1°)récupérer les valeurs ds les champs texte
			// 2°)faire conversion en int
			int iTimeElapsed = TimerManager.timeFromString(this.tfPassedTime
					.getText());
			int iTimeRemaining = TimerManager
					.timeFromString(this.tfEstimatedTime.getText());
			int iCurrentIt = ApplicationManager.getInstance()
					.getCurrentProject().getItCurrent();

			this.activity.sethmTime(iCurrentIt, new TimeCouple(iTimeElapsed,
					iTimeRemaining));
		}

		return (isValid);

	}

	private class ListenerBoutons implements ActionListener
	{
		/**
		 * @param e :
		 *            actionEvent
		 * 
		 */
		public void actionPerformed (ActionEvent e)
		{
			// on recupere le bouton sur lequel on a clique grace a
			// l'ActionEvent e
			JButton pb = (JButton) e.getSource();

			// si on a clique sur le bouton ok
			if (pb == TimeEditDialog.this.pbValidate)
			{
				// on a cliqué sur ok
				// si les temps sont au bon format on ferme sinon on ne fait
				// rien
				if (TimeEditDialog.this.validateDialog())
				{
					// on ferme la fenetre
					TimeEditDialog.this.dispose();
				}
				else
				{
					// message d'erreur
					JOptionPane.showMessageDialog(ApplicationManager
							.getInstance().getMfPagod(), LanguagesManager
							.getInstance().getString("EditTimeException"),
							LanguagesManager.getInstance().getString(
									"EditTimeErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
				}
			}

			// bouton annuler
			else
			{
				// on ne prend aucun changement en compte, on ferme la fenetre
				TimeEditDialog.this.dispose();
			}
		}
	}
}
