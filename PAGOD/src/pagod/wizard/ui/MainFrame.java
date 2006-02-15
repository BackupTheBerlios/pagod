/*
 * $Id: MainFrame.java,v 1.47 2006/02/15 14:34:29 psyko Exp $
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

package pagod.wizard.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import pagod.common.control.InterfaceManager;
import pagod.common.control.adapters.ProcessTreeModel;
import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.model.Product;
import pagod.common.model.Project;
import pagod.common.model.Step;
import pagod.common.ui.ContentViewerPane;
import pagod.common.ui.NewProjectDialog;
import pagod.common.ui.OpenProjectDialog;
import pagod.common.ui.ProcessFileChooser;
import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.utils.ActionManager.KeyNotFoundException;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.ToolsManager;
import pagod.wizard.control.actions.AbstractPagodAction;
import pagod.wizard.control.states.activity.AbstractActivityState;
import pagod.wizard.control.states.activity.ActivityPresentationState;
import pagod.wizard.control.states.activity.PostConditionCheckerState;
import pagod.wizard.control.states.activity.PreConditionCheckerState;
import pagod.wizard.control.states.activity.StepState;
import pagod.wizard.control.states.application.ActivityLaunchedState;
import pagod.wizard.control.states.application.InitState;
import pagod.wizard.control.states.application.ProcessOpenedState;
import pagod.wizard.control.states.application.ProjectOpenedState;
import pagod.wizard.ui.ButtonPanel.Buttons;

/**
 * Fen?tre principale de l'application PAGOD
 * 
 * @author MoOky
 */
public class MainFrame extends JFrame implements Observer
{
	/**
	 * Panneaux du Nord de la fenetre
	 */
	private JPanel				northPanel;

	/**
	 * Panneaux du centre de la fenetre
	 */
	private JPanel				centerPanel;

	/**
	 * Panneaux du sud de la fenetre
	 */
	private JPanel				southPanel;

	/**
	 * Panneaux de message
	 */
	private MessagePanel		messagePanel		= null;

	/**
	 * Panneaux des arbres
	 */
	private ProcessPanel		processPanel		= null;

	/**
	 * Panneaux de Lancement de l'activit?
	 */
	private JPanel				runActivityPanel	= null;

	/**
	 * Panneaux visualiseur de fichier de contenu
	 */
	private ContentViewerPane	contentViewerPanel	= null;

	/**
	 * Panneaux des boutons
	 */
	private ButtonPanel			buttonPanel			= null;

	/**
	 * un splitPane permettant d'afficher : - dans sa partie sup?rieur la
	 * pr?sentation d'une activit? ou d'une ?tape - dans sa partie inf?rieur les
	 * produits a cr?er durant cette ?tape ainsi que les plan type s'il y en a
	 */
	private JSplitPane			splitPane			= null;

	/**
	 * StepListPanel, qui va contenir la liste de toutes les etapes
	 */
	private StepListPanel		jListPanel			= null;

	/**
	 * panel qui va contenir: le contenu de l'étape et la liste des etapes
	 */
	private JSplitPane			stepPanel			= null;

	private int					dividerLocation		= 300;

	private Dimension			dim;

	/**
	 * Met un component dans la partie sup?rieur du splitPane.
	 * 
	 * Remarque 1 : le splitPane permet d'afficher : - dans sa partie sup?rieur
	 * la pr?sentation d'une activit? ou d'une ?tape - dans sa partie inf?rieur
	 * les produits a cr?er durant cette ?tape ainsi que les plan type s'il y en
	 * a
	 * 
	 * Remarque 2 : si le splitPane n'existe pas il sera cree
	 * 
	 * Remarque 3 : - si obj est de type Activity on affiche dans la partie
	 * inferieur la liste de tous les produits - si obj est de type Step on
	 * affiche dans la partie inferieur la liste des produits en sortie de
	 * l'etape
	 * 
	 * @param component
	 *            est le composant que l'on veut voir apparaitre dans la partie
	 *            sup?rieur du JSPlitPane
	 * 
	 * @param obj
	 *            soit un objet de type Step ou Activity
	 * 
	 */
	private void setComponentInJSplitPane (JComponent component, Object obj)
	{
		// si le splitPane n'existe pas on le cree
		if (this.splitPane == null)
		{
			// on nettoye le panneau
			this.centerPanel.removeAll();

			this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

			// on indique que lorsqu'il y a de l'espace suppl?mentaire dans le
			// splitpane
			// le composant contenu en bas ou a droite ne prendra rien de
			// l'espace suppl?mentaire
			this.splitPane.setResizeWeight(1.0);
		}

		// ajout du panneau permettant d'afficher les produits a creer
		if (obj instanceof Activity)
		{
			// ajout du panneau permettant d'afficher les produits a creer
			this.splitPane.setRightComponent(new ProductsPanel(this
					.getActivity(), this.getActivity().getOutputProducts()));
			this.centerPanel.add(this.splitPane);

			this.splitPane.setOneTouchExpandable(true);

			this.splitPane.setLeftComponent(component);

		}
		else if (obj instanceof Step)
		{
			Step aStep = (Step) obj;

			if (aStep.hasOutputProducts())
			{
				// ajout du panneau permettant d'afficher les produits a creer
				this.splitPane.setRightComponent(new ProductsPanel(this
						.getActivity(), aStep.getOutputProducts()));
				this.centerPanel.add(this.splitPane);

				this.splitPane.setOneTouchExpandable(true);
			}
			else
			{
				// s'il n'y a pas de produit en sortie de l'etape on vide le
				// splitPane inferieur
				// TODO a regarder de plus pres
				this.splitPane.setRightComponent(new ProductsPanel(this
						.getActivity(), aStep.getOutputProducts()));
				this.splitPane.getRightComponent().setVisible(false);
				this.centerPanel.add(this.splitPane);

				this.splitPane.setOneTouchExpandable(false);
				this.splitPane.setLeftComponent(component);
			}

			this.stepPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

			this.stepPanel.setLeftComponent(component);
			this.stepPanel.setRightComponent(this.jListPanel);

			this.stepPanel.setOneTouchExpandable(true);
			this.stepPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
			this.stepPanel.setResizeWeight(0.8);

			this.splitPane.setLeftComponent(this.stepPanel);

		}

	}

	/**
	 * Construit la Fen?tre principale de PAGOD initialement invisible
	 * 
	 */
	public MainFrame ()
	{
		super();
		// Definir le titre de la fen?tre
		this.setTitle(Constants.APPLICATION_SHORT_NAME);
		// D?finir l'ic?ne de la fen?tre
		this.setIconImage(ImagesManager.getInstance().getImageResource(
				"iconWizard.png"));
		// Positionner la fen?tre dans le coin en haut ? gauche
		this.setLocation(0, 0);
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
		// D?finir la taille de la fen?tre et la taille de la fenetre maximise
		Rectangle screenSize = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int iWidth = screenSize.width / 3;
		int iHeight = screenSize.height;
		this.setSize(screenSize.width / 3, screenSize.height);
		this.setMaximizedBounds(new Rectangle(iWidth, iHeight));
		// Creation du Menu
		this.setJMenuBar(new MainFrameMenuBar());
		// creation des Panneaux
		this.northPanel = new JPanel();
		this.northPanel.setLayout(new BorderLayout());
		this.getContentPane().add(this.northPanel, BorderLayout.NORTH);
		this.centerPanel = new JPanel();
		this.centerPanel.setLayout(new BorderLayout());
		this.getContentPane().add(this.centerPanel, BorderLayout.CENTER);
		this.southPanel = new JPanel();
		this.southPanel.setLayout(new BorderLayout());
		this.getContentPane().add(this.southPanel, BorderLayout.SOUTH);

		// creation et initialisation du Panneaux de message
		this.messagePanel = new MessagePanel();
		// on ajoute de panneau de message comme observer du timer
		TimerManager.getInstance().addObserver(this.messagePanel);
		TimerManager.getInstance().addObserver(this);

		this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
				"welcomeMessage"));
		this.northPanel.add(this.messagePanel);
		this.runActivityPanel = new JPanel(new BorderLayout());
		JPanel innerPane = new JPanel(new FlowLayout());
		innerPane.add(new JButton(ActionManager.getInstance().getAction(
				Constants.ACTION_RUN_ACTIVITY)));
		this.runActivityPanel.add(innerPane, BorderLayout.EAST);
		// Definir les Raccourcis Claviers pour soit sensible du moment que la
		// fenetre est active
		// actions Menu Fichier
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_OPENPROCESS)).configureRootPane(this
				.getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_QUIT)).configureRootPane(this.getRootPane(),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		// actions Menu Activit?
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_PREVIOUS)).configureRootPane(this
				.getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_NEXT)).configureRootPane(this.getRootPane(),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_TERMINATE)).configureRootPane(this
				.getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		((AbstractPagodAction) ActionManager.getInstance().getAction(
				Constants.ACTION_SUSPEND)).configureRootPane(
				this.getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW);

		// TODO il faudra fre cela qd le pb de la MainFrame sera regle
		// on enregistre la MainFrame comme observer de l'ApplicationManager
		// ApplicationManager.getInstance().addObserver(this);

		// TODO mn?monique pour acc?der ? la combo box, et mettre le focus
		// dedans
		// pr access clavier aux steps ...
	}

	/**
	 * Presente le processus ? utilisateur
	 * 
	 * @param process
	 *            processus passer en parametre
	 * @param fileName
	 *            nom du fichier du processus
	 * @param processName
	 *            nom du processus
	 */
	public void showProcess (ProcessTreeModel process, String fileName,
			String processName)
	{

		// on recupere le nom du projet en cours
		String nameProject = ApplicationManager.getInstance()
				.getCurrentProject().getName();

		// mettre le titre a jour
		String title = Constants.APPLICATION_SHORT_NAME + " - " + nameProject
				+ " - " + fileName;
		if (processName != null) title += " (" + processName + ") ";
		this.setTitle(title);
		// creer le treePanel
		this.processPanel = new ProcessPanel(process);
		this.showProcess();
	}

	/**
	 * Presente le processus en cours ? utilisateur
	 * 
	 */
	public void showProcess ()
	{
		// on netoye les panneaux
		this.centerPanel.removeAll();
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		// mettre a jour le message
		this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
				"openedProcessMessage"));
		// on remplie les panneaux
		// au nord
		this.northPanel.setVisible(false);
		this.northPanel.add(this.messagePanel);
		this.northPanel.setVisible(true);
		// au centre
		this.centerPanel.setVisible(false);
		this.centerPanel.add(this.processPanel);
		this.centerPanel.setVisible(true);
		// au sud
		this.southPanel.setVisible(false);
		this.southPanel.add(this.runActivityPanel);
		this.southPanel.setVisible(true);

		this.setVisible(true);
		this.processPanel.requestFocus();
	}

	/**
	 * Retourne l'activit? selectionn?
	 * 
	 * @return activit? selectionn?
	 */
	public Activity getActivity ()
	{
		return this.processPanel.getSelectedActivity();
	}

	/**
	 * @param activity
	 * @throws NotInitializedException
	 *             Si le gestionnaire de langues n'est pas initialis?
	 * @throws MissingResourceException
	 *             Si une cl? n'existe pas dans le fichier de langues
	 * @throws pagod.utils.ImagesManager.NotInitializedException
	 */
	public void showCheckList (Activity activity)
	{
		// on netoye les panneaux
		this.centerPanel.removeAll();
		this.southPanel.removeAll();

		// cr?er les panneaux
		this.centerPanel.add(new CheckPane(activity));

		this.southPanel.add(this.buttonPanel);
		this.setVisible(true);
	}

	/**
	 * Fonction qui affiche les produits n?cessaire pour termin? l'activit?
	 * 
	 * @param activity
	 */
	public void showEndCheckList (Activity activity)
	{
		// on netoye les panneaux
		this.centerPanel.removeAll();
		this.southPanel.removeAll();
		// on cr?er les panneau
		this.centerPanel.add(new EndCheckPanel(activity));

		this.southPanel.add(this.buttonPanel);
		this.setVisible(true);

	}

	/**
	 * @param stepToPresent
	 * @param total
	 * @param rang
	 */
	public void presentStep (Step stepToPresent, int rang, int total)
	{
		// s'il y a des produits en sorties de l'activite lance
		if (this.getActivity().hasOutputProducts())
		{
			// on affiche un jsplitPane qui affiche en haut la presentation de
			// l'etape
			// et en bas les produits en sorties
			// cr?er les panneaux

			this.setComponentInJSplitPane(new StepPanel(stepToPresent, rang,
					total), stepToPresent);
			// this.dividerLocation = this.splitPane.getLastDividerLocation();
			this.splitPane.setDividerLocation(-1);
		}
		else
		{
			// on nettoye le panneaux
			this.centerPanel.removeAll();

			// on affiche uniquement la presentation des activites
			this.centerPanel.add(new StepPanel(stepToPresent, rang, total));
		}

		this.setVisible(true);
	}

	/**
	 * @param activity
	 * @param ProductsToPresent
	 */
	public void presentProducts (Activity activity,
			Collection<Product> ProductsToPresent)
	{
		// on netoye les panneaux
		this.centerPanel.removeAll();
		// mettre a jour le message
		this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
				"presentProductsMessage"));
		// cr?er les panneaux
		ProductsPanel productsPanel = new ProductsPanel(activity,
				ProductsToPresent);
		this.centerPanel.add(productsPanel);
		this.setVisible(true);

		// demande le focus
		productsPanel.requestFocus();
	}

	/**
	 * 
	 * @param activityToPresent
	 */
	public void presentActivity (Activity activityToPresent)
	{
		// on netoye le panneau
		this.southPanel.removeAll();

		// cr?er les panneaux
		this.contentViewerPanel = new ContentViewerPane(activityToPresent);

		// s'il y a des produits en sorties de cette activite
		if (activityToPresent.hasOutputProducts())
		{
			// on affiche un jsplitPane qui affiche en haut la presentation de
			// l'activite
			// et en bas les produits en sorties

			this.setComponentInJSplitPane(this.contentViewerPanel,
					activityToPresent);
		}
		else
		{

			// on nettoye le panneaux
			this.centerPanel.removeAll();

			// on affiche uniquement la presentation des activites
			this.centerPanel.add(this.contentViewerPanel);

		}

		this.southPanel.add(this.buttonPanel);

		this.setVisible(true);
		// demande le focus
		this.contentViewerPanel.requestFocus();
	}

	/**
	 * G?re l'ouverture d'un processus
	 * 
	 * @return TODO a faire
	 */
	public boolean chooseAndOpenProcess ()
	{
		ProcessFileChooser fileChooser = new ProcessFileChooser();
		boolean opened = false;
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{

			// Remplir le mod?le metier
			File choosenfile = fileChooser.getSelectedFile();

			opened = this.openProcess(choosenfile);

		}
		return opened;
	}

	/**
	 * Permet d'ouvrir et de charger le model m?tier avec un dpc a partir du
	 * fichier en entr?e
	 * 
	 * @param processFile
	 *            le fichier en entr?e (le dpc)
	 * @return vrai si le dpc a ete ouvert
	 */
	public boolean openProcess (File processFile)
	{
		boolean opened = false;
		Process aProcess = InterfaceManager.getInstance().importModel(
				processFile.getAbsolutePath(), this, false);
		if (aProcess != null)
		{
			if (ApplicationManager.getInstance().getCurrentProcess() != null) ApplicationManager
					.getInstance().closeProcess();
			// Afficher la fenetre de choix des roles
			RolesChooserDialog rolesChooser = new RolesChooserDialog(this,
					aProcess.getRoles());
			if (rolesChooser.showDialog() == RolesChooserDialog.APPROVE_OPTION)
			{
				// recuperer les Roles choisis
				// creer le TreeModel n?cessaire au JTree de la fenetre
				// presenter a l'utilisateur le processus
				String fileName = processFile.getName();

				this.showProcess(new ProcessTreeModel(aProcess, rolesChooser
						.getChosenRoles()), fileName, aProcess.getName());

				// mettre a jour le processus en cours
				ApplicationManager.getInstance().setCurrentProcess(aProcess);
				ApplicationManager.getInstance().getCurrentProject()
						.setCurrentProcess(aProcess);
				// on ouvre les fichiers d'outils
				ToolsManager.getInstance().initialise(
						ApplicationManager.getInstance().getCurrentProcess());
				ToolsManager.getInstance().loadToolsAssociation();
				opened = true;
			}
			else
			{
				opened = false;
			}
		}
		return opened;
	}

	/**
	 * @return vrai si le projet est bien ouvert
	 * 
	 * 
	 */
	public boolean openProject ()
	{
		boolean opened = false;

		OpenProjectDialog opDialog = new OpenProjectDialog(this);
		opDialog.showDialog();
		Project projectTemp = null;
		if (opDialog.hasProject())
		{
			projectTemp = opDialog.getOpenedProject();
			ApplicationManager.getInstance().setCurrentProject(projectTemp);
			this.reinitialize();
		}
		else
		{
			return opened;
		}
		opDialog.dispose();

		if (ApplicationManager.getInstance().getCurrentProject() != null)
		{

			System.out.println("On a a un project et on essaye de l'ouvrir");
			File validTab[];
			File dpc = null;
			File directory = new File(PreferencesManager.getInstance()
					.getWorkspace()
					+ File.separator
					+ ApplicationManager.getInstance().getCurrentProject()
							.getName());
			validTab = directory.listFiles();

			for (File currentFile : validTab)
			{

				if (currentFile.getName().endsWith(".dpc")
						|| currentFile.getName().endsWith(".pagod"))
				{
					dpc = currentFile;
					System.out.println(currentFile.getName());
				}
			}
			if (dpc != null)
			{
				ApplicationManager.getInstance().getCurrentProject()
						.setNameDPC(dpc.getName());
				System.out.println(dpc.getName());
				opened = true;
			}
			else
			{
				opened = true;
			}
		}
		return (opened);
	}

	/**
	 * M?thode permettant de creer un nouveau projet et d'ouvrir le dialogue
	 * pour la creation
	 * 
	 * @return vrai si le projet a pu ?tre creer sinon faux
	 */
	public boolean newProject ()
	{
		NewProjectDialog newProjectDialog = new NewProjectDialog(this);
		Project p = newProjectDialog.getCreatedProject();
		newProjectDialog.dispose();
		if (p == null)
		{
			System.err.println("PAS de project cr??");
			return false;
		}
		System.err.println("Project cr??");
		ApplicationManager.getInstance().setCurrentProject(p);
		return true;
	}

	/**
	 * Permet d'associer un dpc a un projet et charge ce meme dpc dans le modele
	 * metier
	 * 
	 * @return vrai si le dpc a pu etre associ? sinon faux
	 */
	public boolean associateDPCWithProject ()
	{
		// on demande a l'utilisateur de choisir un fichier processus
		ProcessFileChooser fileChooser = new ProcessFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			// on associe le dpc/pagod au projet en cours
			try
			{
				ApplicationManager.getInstance().getCurrentProject().changeDPC(
						fileChooser.getSelectedFile());
			}
			catch (IOException e)
			{
				// TODO Bloc de traitement des exceptions g?n?r? automatiquement
				e.printStackTrace();
			}

			// Remplir le mod?le metier
			File choosenFile = fileChooser.getSelectedFile();
			this.openProcess(choosenFile);

			return ApplicationManager.getInstance().getCurrentProject()
					.hasCurrentProcess();

		}
		return false;
	}

	/**
	 * reinitialise la fenetre
	 * 
	 */
	public void reinitialize ()
	{
		// on netoye les panneaux
		this.centerPanel.removeAll();
		this.southPanel.removeAll();
		// mettre a jour le message
		this.messagePanel.setMessage(LanguagesManager.getInstance().getString(
				"welcomeMessage"));
		this.setTitle(Constants.APPLICATION_SHORT_NAME);
		this.setVisible(true);
	}

	/**
	 * Remet a null le splitPane permettant d'afficher : - dans sa partie
	 * sup?rieur la pr?sentation d'une activit? ou d'une ?tape - dans sa partie
	 * inf?rieur les produits a cr?er durant cette ?tape ainsi que les plan type
	 * s'il y en a
	 * 
	 */
	public void resetSplitPane ()
	{
		this.splitPane = null;
	}

	/**
	 * @return le button panel utile pour l acces direct aux ?tapes
	 */
	public ButtonPanel getButtonPanel ()
	{
		return this.buttonPanel;
	}

	/**
	 * initialise le buttonPanel
	 */
	public void InitButtonPanel ()
	{
		this.buttonPanel = new ButtonPanel();
	}

	/**
	 * TODO ptetre deplacer cette methode dans le mesasge panel pour eviter la
	 * redondance
	 * 
	 * @param state
	 *            methode qui va mettre à jour le message en fonction de l'
	 *            etape ds laquelle on se trouve
	 */
	protected void setMessagePanel (AbstractActivityState state)
	{
		String message;

		message = LanguagesManager.getInstance().getString("activityRole")
				+ " : " + this.getActivity().getRole() + "<BR>"
				+ LanguagesManager.getInstance().getString("activityActivity")
				+ " : " + this.getActivity() + "<BR>" + state;
		
		if (ApplicationManager.getInstance().getCurrentProject() != null)
		{
			message = message + "<BR>" + 
			LanguagesManager.getInstance().getString("activityIteration")
			+ " : " + ApplicationManager.getInstance().getCurrentProject().getItCurrent();
		}

		this.messagePanel.setMessage(message);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update (Observable obs, Object obj)
	{
		// s'il y a eu un changement d'etat dans ActivityScheduler
		// (changement d'etat qd une activite est lanc?)
		if (obs instanceof ActivityScheduler)
		{
			ActionManager.getInstance()
					.getAction(Constants.ACTION_RUN_ACTIVITY).setEnabled(false);
			System.err.println("notification actictivity sched");

			// on recupere l'etat de l'application
			AbstractActivityState state = (AbstractActivityState) obj;

			// dans n'importe quel etat on peut toujours faire terminate et
			// gotostep
			ActionManager.getInstance().getAction(Constants.ACTION_SUSPEND)
					.setEnabled(true);
			ActionManager.getInstance().getAction(Constants.ACTION_GOTOSTEP)
					.setEnabled(true);

			if (state instanceof PreConditionCheckerState)
			{
				// on rafraichit la MainFrame
				this.resetSplitPane();
				this.showCheckList(state.getActivity());

				// on active et desactive les actions
				ActionManager.getInstance()
						.getAction(Constants.ACTION_PREVIOUS).setEnabled(false);

				ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
						.setEnabled(true);
				if (state.getActivity().hasOutputProducts())
				{

					this.activateSuspend();
				}
				else
				{
					this.activateTerminate();

				}

			}
			else if (state instanceof ActivityPresentationState)
			{
				// on rafraichit la MainFrame
				this.resetSplitPane();

				this.presentActivity(state.getActivity());

				// s'il y a des produits en entrees ou des guides d'un type
				// autre que "liste de controles" associe a l'activite (ou au
				// role de cette activite) on active previous
				if (state.getActivity().hasInputProducts()
						|| state.getActivity().hasGuidanceWithoutType(
								"Liste de controles")
						|| state.getActivity().getRole()
								.hasGuidanceWithoutType("Liste de controles"))
				{
					ActionManager.getInstance().getAction(
							Constants.ACTION_PREVIOUS).setEnabled(true);
				}
				else
					ActionManager.getInstance().getAction(
							Constants.ACTION_PREVIOUS).setEnabled(false);

				// s'il y a des etapes ou des produits en sorties on active le
				// next
				if (state.getActivity().hasSteps()
						|| state.getActivity().hasOutputProducts()
						|| state.getActivity().hasGuidanceType(
								"Liste de controles")
						|| state.getActivity().getRole().hasGuidanceType(
								"Liste de controles"))
				{
					ActionManager.getInstance()
							.getAction(Constants.ACTION_NEXT).setEnabled(true);
				}
				else
					ActionManager.getInstance()
							.getAction(Constants.ACTION_NEXT).setEnabled(false);
				if (state.getActivity().hasOutputProducts())
				{

					this.activateSuspend();
				}
				else
				{
					this.activateTerminate();

				}

			}
			else if (state instanceof StepState)
			{
				System.err
						.println("MainFrame.update().state instanceof StepState");

				// on rafraichit la MainFrame
				this.resetSplitPane();
				this.presentStep(state.getStep(), state.getIndex() + 1, state
						.getStepList().size());

				// on peut toujours faire previous car au minimum on reviendra
				// en ActivityPresentation
				ActionManager.getInstance()
						.getAction(Constants.ACTION_PREVIOUS).setEnabled(true);

				
				// si on est sur la derniere etape et qu'il n'y a pas : 
				// - des produits en sortie
				// - des guides de type liste de controles sur le role ou l'activite
				// on ne peut pas faire next
				if (state.getIndex() == state.getStepList().size() - 1
						&& (!state.getActivity().hasOutputProducts()
								&& !state.getActivity().hasGuidanceType(
										"Liste de controles") 
								&& !state.getActivity().getRole().hasGuidanceType(
										"Liste de controles")))
				{
					ActionManager.getInstance()
							.getAction(Constants.ACTION_NEXT).setEnabled(false);
				}
				else
				{
					ActionManager.getInstance()
							.getAction(Constants.ACTION_NEXT).setEnabled(true);
				}
				
				if (state.getActivity().hasOutputProducts())
				{

					this.activateSuspend();
				}
				else
				{
					this.activateTerminate();

				}

			}
			else if (state instanceof PostConditionCheckerState)
			{
				// on rafraichit la MainFrame
				this.resetSplitPane();
				this.showEndCheckList(state.getActivity());
				this.activateTerminate();

				// on peut toujours faire previous car au minimum on reviendra
				// en ActivityPresentation
				ActionManager.getInstance()
						.getAction(Constants.ACTION_PREVIOUS).setEnabled(true);

				// on ne peut jamais faire next quand on est en
				// PostConditionCheckerState
				ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
						.setEnabled(false);

			}
			else
			{
				System.err
						.println("ActivityScheduler est dans un etat inconnu !!!");
			}

			// on position la combo sur le bon item

			// on initialise la comboBox du ButtonPanel

			this.setMessagePanel(state);
			this.buttonPanel.setSelectedIndex(state);
			return;
		}

		// s'il y a eu un changement d'etat dans l'ApplicationManager
		if (obs instanceof ApplicationManager)
		{
			// s'il l'objet passe est de type ActivityScheduler
			// la MainFrame s'enregistre comme observer aupres de
			// l'ActivityScheduler
			if (obj instanceof ActivityScheduler)
			{

				ActivityScheduler activityScheduler = (ActivityScheduler) obj;
				activityScheduler.addObserver(this);
				// a l'entr?e dans un activit? on cr?e un nouveau panel de
				// boutton que l'on pourra ajouter
				// ensuite en bas
				// on peut faire cela car obj est de type ActivityScheduler
				// lorsque l'on vient de lancer une activit?
				this.buttonPanel = new ButtonPanel();
				// on initialise la combo box
				this.buttonPanel.initComboBox(activityScheduler.getStateList());

				this.jListPanel = new StepListPanel();
				this.jListPanel.initJList(activityScheduler);

			}
			else if (obj instanceof InitState)
			{
				// on desactive les actions
				ActionManager.getInstance().getAction(
						Constants.ACTION_RUN_ACTIVITY).setEnabled(false);
				ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
						.setEnabled(false);
				ActionManager.getInstance()
						.getAction(Constants.ACTION_PREVIOUS).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_TERMINATE).setEnabled(false);
				ActionManager.getInstance().getAction(Constants.ACTION_SUSPEND)
						.setEnabled(false);
				ActionManager.getInstance()
						.getAction(Constants.ACTION_GOTOSTEP).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_TOOLSSETTINGS).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_OPENPROCESS).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_CLOSEPROJECT).setEnabled(false);

				// on affiche la fen?tre
				this.setVisible(true);
				this.setExtendedState(Frame.MAXIMIZED_BOTH);

				// si aucun projet n'a ete creer on demande a l'utilisateur de
				// le definir
				// test si la valeur de la cl? workspace est d?finie ou pas

				boolean validWorkspace = false;
				boolean cancelChoice = false;

				if (!PreferencesManager.getInstance().containWorkspace())
				{
					WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();

					while (!validWorkspace && !cancelChoice)
					{
						if (workspaceChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
						{
							File file = workspaceChooser.getSelectedFile();
							System.out.println(file.getPath());

							// on verifie que le workspace choisi existe
							// mettre le path dans le fichier preferences a la
							// cl?
							// "workspace"
							if (file.exists())
							{
								PreferencesManager.getInstance().setWorkspace(
										file.getPath());
								validWorkspace = true;
							}
							else
							{
								// affichage d'un message d'erreur si le
								// workspace est invalide
								JOptionPane
										.showMessageDialog(
												this,
												LanguagesManager
														.getInstance()
														.getString(
																"WorkspaceException"),
												LanguagesManager
														.getInstance()
														.getString(
																"WorkspaceErrorTitle"),
												JOptionPane.ERROR_MESSAGE);
							}
						}
						else
						{
							cancelChoice = true;
						}
					}
				}
				else
				{
					// le workspace existe deja
					validWorkspace = true;
				}
				// si le workspace n'est pas choisi on affiche un message
				// d'erreur
				if (!validWorkspace)
				{
					ActionManager.getInstance().getAction(
							Constants.ACTION_NEWPROJECT).setEnabled(false);
					ActionManager.getInstance().getAction(
							Constants.ACTION_OPENPROJECT).setEnabled(false);
					/*
					 * ActionManager.getInstance().getAction(
					 * Constants.ACTION_TIMEACTIVITY).setEnabled(false);
					 */
					ActionManager.getInstance().getAction(
							Constants.ACTION_PREFERENCES).setEnabled(false);

					// affichage d'un message d'erreur si le workspace n'est pas
					// d?fini ou invalide
					JOptionPane.showMessageDialog(this, LanguagesManager
							.getInstance().getString("WorkspaceException"),
							LanguagesManager.getInstance().getString(
									"WorkspaceErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
				}
				// si le workspace est valide on degrise les actions
				else
				{
					ActionManager.getInstance().getAction(
							Constants.ACTION_NEWPROJECT).setEnabled(true);
					ActionManager.getInstance().getAction(
							Constants.ACTION_OPENPROJECT).setEnabled(true);
					/*
					 * ActionManager.getInstance().getAction(
					 * Constants.ACTION_TIMEACTIVITY).setEnabled(true);
					 */
				}
				this.reinitialize();

			}
			else if (obj instanceof ProjectOpenedState)
			{
				// on desactive run activity
				ActionManager.getInstance().getAction(
						Constants.ACTION_RUN_ACTIVITY).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_TOOLSSETTINGS).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_OPENPROCESS).setEnabled(true);
				ActionManager.getInstance().getAction(
						Constants.ACTION_CLOSEPROJECT).setEnabled(true);

			}
			else if (obj instanceof ProcessOpenedState)
			{
				this.showProcess();
				ActionManager.getInstance().getAction(
						Constants.ACTION_RUN_ACTIVITY).setEnabled(false);

				ActionManager.getInstance().getAction(
						Constants.ACTION_TOOLSSETTINGS).setEnabled(true);
				ActionManager.getInstance().getAction(Constants.ACTION_NEXT)
						.setEnabled(false);
				ActionManager.getInstance()
						.getAction(Constants.ACTION_PREVIOUS).setEnabled(false);
				ActionManager.getInstance()
						.getAction(Constants.ACTION_GOTOSTEP).setEnabled(false);
				ActionManager.getInstance().getAction(
						Constants.ACTION_TERMINATE).setEnabled(false);

				// TODO fab pour tester le timeHandler
				// TimeHandler th = new TimeHandler ();
				// th.loadXML(
				// ApplicationManager.getInstance().getCurrentProject().getName());
				// th.affiche() ;
				// th.fillModel(ApplicationManager.getInstance().getCurrentProcess()
				// );
				// th.loadModel(ApplicationManager.getInstance().getCurrentProcess()
				// );
				// System.err.println("fab");
				// th.affiche();
				// th.writeXML(ApplicationManager.getInstance().getCurrentProject().getName());
				/*
				 * final Collection <Activity > cactivity =
				 * ApplicationManager.getInstance().getCurrentProcess().getAllActivities
				 * (); for (Activity acty : cactivity) {
				 * System.err.println(acty.getTime() ); }
				 */
			}
			else if (obj instanceof ActivityLaunchedState)
			{

			}

		}

	}

	/**
	 * methode pour concentrer les desactvation de boutons
	 */
	private void activateSuspend ()
	{

		ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
				.setEnabled(false);
		ActionManager.getInstance().getAction(Constants.ACTION_SUSPEND)
				.setEnabled(true);
		this.buttonPanel.hideButtons(Buttons.PB_TERMINATE);
		this.buttonPanel.showButtons(Buttons.PB_SUSPEND);
	}

	private void activateTerminate ()
	{

		ActionManager.getInstance().getAction(Constants.ACTION_TERMINATE)
				.setEnabled(true);
		ActionManager.getInstance().getAction(Constants.ACTION_SUSPEND)
				.setEnabled(false);
		this.buttonPanel.hideButtons(Buttons.PB_SUSPEND);
		this.buttonPanel.showButtons(Buttons.PB_TERMINATE);
	}
}
