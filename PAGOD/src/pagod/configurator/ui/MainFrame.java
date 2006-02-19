/*
 * $Id: MainFrame.java,v 1.4 2006/02/19 15:36:04 yak Exp $
 *
 * SPWIZ - Spem Wizard
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * PAGOD- Personal assistant for group of development
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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import pagod.common.control.InterfaceManager;
import pagod.common.control.actions.CustomAction;
import pagod.common.model.Process;
import pagod.common.ui.AboutDialog;
import pagod.common.ui.ProcessFileChooser;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.Constants;
import pagod.configurator.control.PreferencesManager;
import pagod.configurator.control.actions.AbstractPagodAction;
import pagod.configurator.control.states.InitState;
import pagod.configurator.control.states.ProcessOpenedState;
import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.ActionManager.KeyNotFoundException;

/**
 * Fen?tre principale de l'application PAGOD configurator
 * 
 * @author MoOky
 */
public class MainFrame extends JFrame implements Observer
{
	/**
	 * Panneaux de cr?ation des ?tapes
	 */
	private StepsPanel				stepsPanel;

	/**
	 * Panneaux associations des produits
	 */
	private ToolsAssociationPanel	toolsAssociationPanel;

	/**
	 * Panneau d'activation/d?sactivation de r?les
	 */
	private RolesPanel				rolesPanel;

	/**
	 * Les onglets
	 */
	private JTabbedPane				tabs;

	/**
	 * Construit la Fen?tre principale de PAGOD configurator initialement
	 * invisible
	 * 
	 * @throws LanguagesManager.NotInitializedException
	 *             Si le LanguagesManager n'a pas ?t? initialis?.
	 * @throws KeyNotFoundException
	 *             Si il n'y a pas de chain correspondante ? la cl? donn? au
	 *             LanguagesManager.
	 * @throws ImagesManager.NotInitializedException
	 *             Si l'iconManager n'a pas ?t? initialis?.
	 * @see java.awt.GraphicsEnvironment#isHeadless()
	 * @see java.awt.Component#setVisible(boolean)
	 * @see pagod.utils.LanguagesManager#setResourceFile(String, Locale)
	 * @see pagod.utils.ImagesManager#setImagePath(String)
	 */
	public MainFrame ()
	{
		super();
		// Definir le titre de la fen?tre
		this.setTitle(Constants.APPLICATION_SHORT_NAME);
		// D?finir l'ic?ne de la fen?tre
		this.setIconImage(ImagesManager.getInstance().getImageResource(
				"iconConfigurator.png"));
		// Faire un traitement particulier lors de la fermeture de l'application
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent arg0)
			{
				try
				{
					ActionManager.getInstance()
							.getAction(Constants.ACTION_QUIT).actionPerformed(
									null);
				}
				catch (KeyNotFoundException e)
				{
				}
			}
		});
		// Creation du Menu
		this.setJMenuBar(new MainFrameMenuBar());
		// Definir les Raccourcis Claviers pour qu'ils soit sensible du moment
		// que la
		// fenetre est active
		// actions Menu Fichier
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_OPEN)).configureRootPane(this.getRootPane(),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((CustomAction) ActionManager.getInstance().getAction(
				Constants.ACTION_QUIT)).configureRootPane(this.getRootPane(),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// mise en place de la fenetre
		// rends la fenetre visible et la maximise
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		this.setPreferredSize(new Dimension(
				(int) (screenSize.getWidth() * 3.0 / 4.0), (int) (screenSize
						.getHeight() * 3.0 / 4.0)));
		this.pack();
		this.setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

	}

	/**
	 * Presente le processus ? utilisateur
	 * 
	 * @param process
	 *            processus passer en parametre
	 * @param fileName
	 *            nom du fichier du processus
	 */
	public void presentProcess (Process process, String fileName)
	{
		// mettre le titre a jour
		String title = Constants.APPLICATION_SHORT_NAME + " - " + fileName;
		if (process.getName() != null) title += " (" + process.getName() + ") ";
		this.setTitle(title);
		// creer les panneaux
		this.stepsPanel = new StepsPanel(process);
		this.toolsAssociationPanel = new ToolsAssociationPanel(process);
		this.rolesPanel = new RolesPanel(process);
		this.tabs = new JTabbedPane();
		this.tabs.addTab(LanguagesManager.getInstance().getString("stepsTab"),
				this.stepsPanel);
		this.tabs.addTab(LanguagesManager.getInstance().getString("toolsTab"),
				this.toolsAssociationPanel);
		/*
		 * this.tabs.addTab(LanguagesManager.getInstance().getString("rolesTab"),
		 * this.rolesPanel);
		 */
		this.getContentPane().removeAll();
		this.getContentPane().add(this.tabs);
		this.setVisible(true);
	}

	/**
	 * Gère l'ouverture d'un processus
	 * 
	 * @param saveMessage
	 *            vrai si on veut proposer une sauvegarde
	 * @return true si le processus est ouvert
	 */
	public boolean openProcess (boolean saveMessage)
	{
		boolean open = false;
		boolean openAgreement = true;
		// on demande de sauvegarder si c'est souhaiter
		if (saveMessage)
		{
			final int choice = JOptionPane.showConfirmDialog(this,
					LanguagesManager.getInstance().getString(
							"saveConfirmationTitle"),
					LanguagesManager.getInstance().getString(
							"saveConfirmationMessage"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (choice == JOptionPane.CANCEL_OPTION)
			{
				openAgreement = false;
			}
			// si il veux savegarder alors
			else if (choice == JOptionPane.YES_OPTION)
			{
				// on sauvegarde mais si la sauvegarde c'est mal passer
				// alors on arrete tout
				if (!this.saveProcess()) openAgreement = false;
			}
			else
			{
				// si il ne veux pas sauvegarder alors on arrete tout
				openAgreement = true;
			}
		}
		if (openAgreement)
		{
			ProcessFileChooser fileChooser = new ProcessFileChooser();
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				// Remplir le modèle metier
				InterfaceManager im = InterfaceManager.getInstance();
				File choosenFile = fileChooser.getSelectedFile();
				Process aProcess = im.importModel(
						choosenFile.getAbsolutePath(), this, true);
				// si l'ouverture c'est bien passer
				if (aProcess != null)
				{
					// recuperer les Roles choisis
					// creer le TreeModel nécessaire au JTree de la fenetre
					// presenter a l'utilisateur le processus
					this.presentProcess(aProcess, choosenFile.getName());
					// mettre a jour le processus en cours
					ApplicationManager.getInstance()
							.setCurrentProcess(aProcess);

					return true;
				}
			}
		}
		return open;
	}

	/**
	 * Enregistre
	 * 
	 * @return vrai si le fichier a ete enregistrer
	 */
	public boolean saveProcess ()
	{
		InterfaceManager im = InterfaceManager.getInstance();
		if (im.getType() == InterfaceManager.Type.PAGOD) return im
				.exportProcess(null, ApplicationManager.getInstance()
						.getCurrentProcess(), this);
		else
			return this.saveAsProcess();
	}

	/**
	 * Enregistrer sous
	 * 
	 * @return vrai si le fichier a ete enregistrer
	 */
	public boolean saveAsProcess ()
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
			// si on selectionne annuler
			else
			{
				// on arrete tout
				return false;
			}
		} while (savePath == null);
		// on enregistre

		return InterfaceManager.getInstance().exportProcess(savePath,
				ApplicationManager.getInstance().getCurrentProcess(), this);
	}

	/**
	 * Ferme l'application
	 * 
	 * @param saveMessage
	 *            vrai si on veut on message de confirmation de sauvegarde
	 */
	public void quit (boolean saveMessage)
	{
		boolean quitAgreement = true;
		// si on souhaite un message de sauvegarde
		if (saveMessage)
		{
			final int choice = JOptionPane.showConfirmDialog(this,
					LanguagesManager.getInstance().getString(
							"saveConfirmationTitle"),
					LanguagesManager.getInstance().getString(
							"saveConfirmationMessage"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			// si il veut rie nfaire
			if (choice == JOptionPane.CANCEL_OPTION)
			{
				quitAgreement = false;
			}
			// si il veux savegarder alors
			else if (choice == JOptionPane.YES_OPTION)
			{
				// on sauvegarde mais si la sauvegarde c'est mal passer
				// alors on arrete tout
				if (!this.saveProcess()) quitAgreement = false;
			}
			else
			{
				// si il ne veux pas sauvegarder alors on arrete tout
				quitAgreement = true;
			}
		}
		if (quitAgreement)
		{
			// on sauvegarde les preferences que l'utilisateur a pu
			// modifier
			PreferencesManager.getInstance().storeExtensions();
			System.exit(0);
		}
	}

	/**
	 * Lance la fenetre de dialogue a propos
	 */
	public void showAboutDialog ()
	{
		AboutDialog ad = new AboutDialog(this, Constants.APPLICATION_SHORT_NAME
				+ " " + Constants.APPLICATION_VERSION);
		ad.setVisible(true);
	}

	/**
	 * lance la fenetre de configuration des preferences
	 */
	public void showPreferencesDialog ()
	{
		PreferencesDialog pd = new PreferencesDialog(this);
		pd.setVisible(true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update (Observable obs, Object obj)
	{
		if (obs instanceof ApplicationManager)
		{
			if (obj instanceof InitState)
			{
				// on desactive les actions
				ActionManager.getInstance().getAction(Constants.ACTION_SAVE)
						.setEnabled(false);
				ActionManager.getInstance().getAction(Constants.ACTION_SAVE_AS)
						.setEnabled(false);
			}
			else if (obj instanceof ProcessOpenedState)
			{
				// on active les actions
				ActionManager.getInstance().getAction(Constants.ACTION_SAVE)
						.setEnabled(true);
				ActionManager.getInstance().getAction(Constants.ACTION_SAVE_AS)
						.setEnabled(true);
			}
		}

	}
}
