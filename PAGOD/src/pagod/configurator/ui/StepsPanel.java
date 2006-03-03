/*
 * $Id: StepsPanel.java,v 1.7 2006/03/03 15:48:26 garwind111 Exp $
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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.common.ui.CommonProcessPanel;
import pagod.common.control.adapters.ProcessTreeModel;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.control.HtmlStepsAutodetector;
import pagod.configurator.control.HtmlStepsExporter;
import pagod.configurator.control.adapters.ActivityStepsTableModel;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.Utilities;
import pagod.utils.editor.Ekit;

/**
 * Panneaux de configuration des étapes.
 * 
 * @author MoOky
 */
public class StepsPanel extends JPanel 
{

    /**
     * action listener du bouton "autodetect des etapes de ttes les activités"
     * 
     * @author Benjamin
     */
    private class DetectAllStepsActionListener implements ActionListener
    {

        private Process process;

        /**
         * @param process
         */
        public DetectAllStepsActionListener(Process process)
        {
            this.process = process;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            ApplicationManager.getInstance().getMfPagod().setCursor(
                    new Cursor(Cursor.WAIT_CURSOR));
            HtmlStepsAutodetector.FillSteps(this.process);
            ((AbstractTableModel) StepsPanel.this.pStepsConfigurationPanel.tEtapes
                    .getModel()).fireTableDataChanged();
            ApplicationManager.getInstance().getMfPagod().setCursor(
                    new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * gestion des boutons "up", "down", "last", "first"
     */
    private class OrganizingStepsButtonActionListener implements ActionListener
    {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            JTable tEtapes = StepsPanel.this.pStepsConfigurationPanel.tEtapes;
            StepsManagementPanel pEtapes = StepsPanel.this.pStepsConfigurationPanel;
            JButton pbClique = (JButton) e.getSource();
            if (tEtapes.getSelectedRow() != -1)
            {
                // annuler modif éventuellement en cours sur la table
                if (StepsPanel.this.pStepsConfigurationPanel.tEtapes
                        .getCellEditor() != null)
                    StepsPanel.this.pStepsConfigurationPanel.tEtapes
                            .getCellEditor().cancelCellEditing();

                if (pbClique == pEtapes.pbFirst && tEtapes.getSelectedRow() > 0)
                {
                    // remonter l'étape actuellement sélectionnée tout en haut
                    ((ActivityStepsTableModel) tEtapes.getModel())
                            .moveFirst(tEtapes.getSelectedRow());
                    // faire suivre le focus de sélection
                    tEtapes.setRowSelectionInterval(0, 0);
                }

                if (pbClique == pEtapes.pbLast
                        && tEtapes.getSelectedRow() < tEtapes.getRowCount() - 1)
                {
                    // descendre l'étape actuellement sélectionnée tout en bas
                    ((ActivityStepsTableModel) tEtapes.getModel())
                            .moveLast(tEtapes.getSelectedRow());
                    // faire suivre le focus de sélection
                    tEtapes.setRowSelectionInterval(tEtapes.getRowCount() - 1,
                            tEtapes.getRowCount() - 1);
                }

                if (pbClique == pEtapes.pbUp && tEtapes.getSelectedRow() > 0)
                {
                    // remonter l'étape actuellement sélectionnée d'un cran
                    ((ActivityStepsTableModel) tEtapes.getModel())
                            .moveUp(tEtapes.getSelectedRow());
                    // faire suivre le focus de sélection
                    tEtapes.setRowSelectionInterval(
                            tEtapes.getSelectedRow() - 1, tEtapes
                                    .getSelectedRow() - 1);
                }

                if (pbClique == pEtapes.pbDown
                        && tEtapes.getSelectedRow() < tEtapes.getRowCount() - 1)
                {
                    // descendre l'étape actuellement sélectionnée d'un cran
                    ((ActivityStepsTableModel) tEtapes.getModel())
                            .moveDown(tEtapes.getSelectedRow());
                    // faire suivre le focus de sélection
                    tEtapes.setRowSelectionInterval(
                            tEtapes.getSelectedRow() + 1, tEtapes
                                    .getSelectedRow() + 1);
                }

                // // notifier la modif
                // ((ActivityStepsTableModel) tEtapes.getModel())
                // .fireTableDataChanged();
            }
        }
    }

    private class StepsManagementPanel extends JPanel
    {

        private Box boxBoutonsDeplacement = new Box(BoxLayout.Y_AXIS);

        private JButton pbAdd = null;

        private JButton pbDown = null;

        private JButton pbFirst = null;

        private JButton pbLast = null;

        private JButton pbAutodetectSteps = null;

        private JButton pbSaveStepAsHtmlFile = null;

        private JPanel pBoutonsActions = new JPanel(new FlowLayout());

        private JButton pbRemove = null;

        private JButton pbUp = null;

        private JPanel pExterne = new JPanel(new BorderLayout());

        private JPanel pTableEtBoutons = new JPanel(new BorderLayout());
        
        // arno bouton d'aperçu de step
        private JButton pbStepOverview = null;
        
        // arno bouton d'aperçu de step
        private JButton pbPagodEditor = null;

        protected StepsTable tEtapes = new StepsTable(
                new ActivityStepsTableModel());

        /**
         * constructeur
         */
        public StepsManagementPanel()
        {
            super();
            this.setLayout(new BorderLayout());
            // initialisation de tous les boutons (label ou icones)
            this.pbFirst = new JButton(ImagesManager.getInstance().getIcon(
                    "FirstIcon.gif"));
            this.pbUp = new JButton(ImagesManager.getInstance().getIcon(
                    "UpIcon.gif"));
            this.pbDown = new JButton(ImagesManager.getInstance().getIcon(
                    "DownIcon.gif"));
            this.pbLast = new JButton(ImagesManager.getInstance().getIcon(
                    "LastIcon.gif"));

            this.pbAdd = new JButton(ImagesManager.getInstance().getIcon(
                    "AddIcon.gif"));
            this.pbRemove = new JButton(ImagesManager.getInstance().getIcon(
                    "RemoveIcon.gif"));

            this.pbAutodetectSteps = new JButton(ImagesManager.getInstance()
                    .getIcon("AutofillStepsIcon.gif"));

            this.pbSaveStepAsHtmlFile = new JButton(ImagesManager.getInstance()
                    .getIcon("SaveAsHtmlStepIcon.gif"));
            
            // bouton aperçu de step
            this.pbStepOverview = new JButton(ImagesManager.getInstance()
                    .getIcon("Overview.gif"));
            // this.pbStepOverview.setSize(10, 10);
            this.pbStepOverview.setEnabled(false);
            
            // bouton pagod editor
            this.pbPagodEditor = new JButton(ImagesManager.getInstance()
                    .getIcon("Overview.gif"));
            // this.pbStepOverview.setSize(10, 10);
            this.pbPagodEditor.setEnabled(false);
            
            
            this.pbFirst
                    .addActionListener(new OrganizingStepsButtonActionListener());
            this.boxBoutonsDeplacement.add(this.pbFirst);

            this.pbUp
                    .addActionListener(new OrganizingStepsButtonActionListener());
            this.boxBoutonsDeplacement.add(this.pbUp);

            this.pbDown
                    .addActionListener(new OrganizingStepsButtonActionListener());
            this.boxBoutonsDeplacement.add(this.pbDown);

            this.pbLast
                    .addActionListener(new OrganizingStepsButtonActionListener());
            this.boxBoutonsDeplacement.add(this.pbLast);

            this.boxBoutonsDeplacement.add(new Box.Filler(
                    new Dimension(10, 10), new Dimension(10, 10),
                    new Dimension(10, 10)));

            this.pbAdd.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;
                    // annuler modif en cours sur la table
                    if (stepsTable.getCellEditor() != null)
                        stepsTable.getCellEditor().cancelCellEditing();
                    // ajouter une étape à l'activité contenue dans le modèle de
                    // la table (identifiant généré automatiquement
                    ((ActivityStepsTableModel) stepsTable.getModel())
                            .getActivity().addStep(
                                    new Step("", "", "",
                                            new ArrayList<Product>()));
                    // notification de l'insertion
                    ((ActivityStepsTableModel) stepsTable.getModel())
                            .fireTableRowsInserted(
                                    stepsTable.getRowCount() - 1, stepsTable
                                            .getRowCount() - 1);
                    // entrer en édition dans le nom de l'étape nouvellement
                    // ajoutée
                    if (stepsTable.editCellAt(stepsTable.getRowCount() - 1, 1))
                    {
                        stepsTable.getEditorComponent().requestFocusInWindow();
                    }
                    // selection ligne nouvellement ajoutée
                    stepsTable.setRowSelectionInterval(
                            stepsTable.getRowCount() - 1, stepsTable
                                    .getRowCount() - 1);
                }
            });
            this.boxBoutonsDeplacement.add(this.pbAdd);

            this.pbRemove.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;

                    if (stepsTable.getSelectedRow() != -1)
                    {
                        // annuler modif en cours sur la table
                        if (stepsTable.getCellEditor() != null)
                            stepsTable.getCellEditor().cancelCellEditing();
                        // demander confirmation
                        try
                        {
                            String nomEtapeASupprimer = ((ActivityStepsTableModel) stepsTable
                                    .getModel()).getStepByRow(
                                    stepsTable.getSelectedRow()).getName();
                            if (nomEtapeASupprimer == null)
                                nomEtapeASupprimer = "";
                            if (JOptionPane
                                    .showConfirmDialog(
                                            StepsPanel.this.getParent(),
                                            String
                                                    .format(
                                                            LanguagesManager
                                                                    .getInstance()
                                                                    .getString(
                                                                            "StepsPanelStepDeleteConfirmationMessage"),
                                                            nomEtapeASupprimer),
                                            LanguagesManager
                                                    .getInstance()
                                                    .getString(
                                                            "StepsPanelStepDeleteConfirmationTitle"),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
                            {
                                int numLigneASupprimer = stepsTable
                                        .getSelectedRow();
                                ((ActivityStepsTableModel) stepsTable
                                        .getModel())
                                        .RemoveStep(numLigneASupprimer);
                                ((ActivityStepsTableModel) stepsTable
                                        .getModel()).fireTableRowsDeleted(
                                        numLigneASupprimer, numLigneASupprimer);
                                // resélectionner la ligne "supprimée"
                                if (stepsTable.getRowCount() > 0)
                                    stepsTable.setRowSelectionInterval(Math
                                            .min(stepsTable.getRowCount() - 1,
                                                    numLigneASupprimer), Math
                                            .min(stepsTable.getRowCount() - 1,
                                                    numLigneASupprimer));
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            this.boxBoutonsDeplacement.add(this.pbRemove);
            
            // ajout bouton aperçu
            // pbStepOverview
            this.pbStepOverview.addActionListener(new ActionListener(){
            		// Suppression du warning JoptionPane
            		@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent e)
                    {
            			StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;

                        if (stepsTable.getSelectedRow() != -1)
                        {
                            // annuler modif en cours sur la table
                            if (stepsTable.getCellEditor() != null)
                                stepsTable.getCellEditor().cancelCellEditing();
                            try
                            {
                                String stepname = ((ActivityStepsTableModel) stepsTable
									.getModel()).getStepByRow(
									stepsTable.getSelectedRow()).getName();
							if (stepname == null) stepname = "";
							
							int linetoview = stepsTable.getSelectedRow();
							Step steptosee = ((ActivityStepsTableModel) stepsTable.getModel()).getStepByRow(linetoview);
							if (!stepname.equals("")){
								new StepOverviewFrame(steptosee).setVisible(true);
							}
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
					{
						new JOptionPane().showMessageDialog(null,
								LanguagesManager.getInstance().getString(
										"StepsPanelOverview_WarningMessageTitle"),
								LanguagesManager.getInstance().getString(
										"StepsPanelOverview_WarningMessage"),
								JOptionPane.WARNING_MESSAGE);
					}
                    }
            });
            this.boxBoutonsDeplacement.add(this.pbStepOverview);
            this.pbStepOverview.setToolTipText(LanguagesManager
                    .getInstance()
                    .getString("StepsPanelOverview"));
            
//          ajout bouton aperçu
            // pbStepOverview
            this.pbPagodEditor.addActionListener(new ActionListener(){
            		// Suppression du warning JoptionPane
            		@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent e)
                    {
            			StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;

                        if (stepsTable.getSelectedRow() != -1)
                        {
                            // annuler modif en cours sur la table
                            if (stepsTable.getCellEditor() != null)
                                stepsTable.getCellEditor().cancelCellEditing();
                            try
                            {
                                String stepname = ((ActivityStepsTableModel) stepsTable
									.getModel()).getStepByRow(
									stepsTable.getSelectedRow()).getName();
							if (stepname == null) stepname = "";
							
							int linetoview = stepsTable.getSelectedRow();
							Step steptosee = ((ActivityStepsTableModel) stepsTable.getModel()).getStepByRow(linetoview);
							if (!stepname.equals("")){
								// Appel de Pagod Editor
								new Ekit(steptosee);
							}
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
					{
						new JOptionPane().showMessageDialog(null,
								LanguagesManager.getInstance().getString(
										"StepsPanelOverview_WarningMessageTitle"),
								LanguagesManager.getInstance().getString(
										"StepsPanelOverview_WarningMessage"),
								JOptionPane.WARNING_MESSAGE);
					}
                    }
            });
            this.boxBoutonsDeplacement.add(this.pbPagodEditor);
            this.pbPagodEditor.setToolTipText(LanguagesManager
                    .getInstance()
                    .getString("StepsPanelOverview"));
            
            this.pbAutodetectSteps.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;
                    // remplir les étapes
                    if (HtmlStepsAutodetector
                            .FillSteps(((ActivityStepsTableModel) stepsTable
                                    .getModel()).getActivity()))
                    {
                        // des étapes ont été détéctées, mettre à jour la table
                        ((ActivityStepsTableModel) stepsTable.getModel())
                                .fireTableDataChanged();
                    }
                    else
                    {
                        String msg = "";
                        try
                        {
                            msg = LanguagesManager.getInstance().getString(
                                    "StepsPanelNoStepsDetected");
                        }
                        catch (Exception ex)
                        {
                        }
                        // pas d'étapes détectées
                        JOptionPane.showMessageDialog(null, msg, msg,
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            this.pbAutodetectSteps.setToolTipText(LanguagesManager
                    .getInstance()
                    .getString("StepsPanelAutodetectStepsTooltip"));
            
            
            
            this.boxBoutonsDeplacement.add(this.pbAutodetectSteps);

            this.pbSaveStepAsHtmlFile.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    // recuperer la table (pour ne pas faire appel 10000 fois à
                    // StepsPanel.machin.truc
                    StepsTable stepsTable = StepsPanel.this.pStepsConfigurationPanel.tEtapes;
                    // recuperer l'activité actuellement affichee
                    Activity activityToExport = ((ActivityStepsTableModel) stepsTable
                            .getModel()).getActivity();
                    // ouverture d'un jfilechooser de choix de fichier à sauver
                    HtmlOutputFileChooser fileChooser = new HtmlOutputFileChooser();
                    String savePath = null;
                    // Chemin de sauvegarde
                    do
                    {
                        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                        {
                            // on verifie si le fichier existe deja
                            if (fileChooser.getSelectedFile().exists())
                            {
                                // si il existe on demande confirmation
                                if (JOptionPane
                                        .showConfirmDialog(
                                                null,
                                                LanguagesManager
                                                        .getInstance()
                                                        .getString(
                                                                "eraseFileConfirmationMessage"),
                                                LanguagesManager
                                                        .getInstance()
                                                        .getString(
                                                                "eraseFileConfirmationTitle"),
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION)
                                {
                                    // si on souhaite ecraser
                                    savePath = fileChooser.getSelectedFile()
                                            .getPath();
                                }
                            }
                            // si le fichier n'existe pas
                            else
                            {
                                savePath = fileChooser.getSelectedFile()
                                        .getPath();
                            }
                        }
                        // si on selectionne annuler
                        else
                        {
                            // on arrete tout
                            return;
                        }
                    } while (savePath == null);

                    if (HtmlStepsExporter.exportActivityToFile(
                            activityToExport, savePath))
                    {
                        // export vers "savePath" OK , afficher boite de
                        // dialogue correspondante
                        JOptionPane.showMessageDialog(ApplicationManager
                                .getInstance().getMfPagod(), String.format(
                                LanguagesManager.getInstance().getString(
                                        "StepsPanelExportStepsOkMessage"),
                                savePath), LanguagesManager.getInstance()
                                .getString("StepsPanelExportStepsDialogTitle"),
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    { // export vers "savePath" KO , afficher boite de
                        // dialogue correspondante
                        JOptionPane.showMessageDialog(ApplicationManager
                                .getInstance().getMfPagod(), String.format(
                                LanguagesManager.getInstance().getString(
                                        "StepsPanelExportStepsFailureMessage"),
                                savePath), LanguagesManager.getInstance()
                                .getString("StepsPanelExportStepsDialogTitle"),
                                JOptionPane.ERROR);
                    }
                }
            });

            this.pbSaveStepAsHtmlFile.setToolTipText(LanguagesManager
                    .getInstance().getString(
                            "StepsPanelExportStepsAsHtmlTooltip"));

            this.boxBoutonsDeplacement.add(this.pbSaveStepAsHtmlFile);

            this.pExterne.add(new JScrollPane(this.tEtapes),
                    BorderLayout.CENTER);
            this.pExterne.add(this.boxBoutonsDeplacement, BorderLayout.EAST);

            // this.pExterne.add(this.pTableEtBoutons, BorderLayout.CENTER);

            this.add(this.pExterne);

            // griser le StepsManagementPanel à l'ouverture :
            Utilities.setEnabledContainer(this, false);

        }
    }

    private CommonProcessPanel pProcessPanel;

    /**
     * 
     */
    private StepsManagementPanel pStepsConfigurationPanel = null;

    /**
     * Constructeur du panneaux de configuration des étapes
     * 
     * @param process
     *            Processus pour lequel il faut créer des étapes.
     */
    public StepsPanel(Process process)
    {
        this.setLayout(new BorderLayout());
        // ajout de l'arbre de processus (pas de filtre par rôle)
        this.pProcessPanel = new CommonProcessPanel(new ProcessTreeModel(
                process, process.getRoles()))
        {

            protected void onActivitySelection()
            {
                // mise à jour de la liste
                StepsPanel.this.pStepsConfigurationPanel.tEtapes
                        .setModel(new ActivityStepsTableModel(this
                                .getSelectedActivity()));
                // activer le StepsManagementPanel
                Utilities.setEnabledContainer(
                        StepsPanel.this.pStepsConfigurationPanel, true);
                // annuler modif en cours sur la table
                if (StepsPanel.this.pStepsConfigurationPanel.tEtapes
                        .getCellEditor() != null)
                    StepsPanel.this.pStepsConfigurationPanel.tEtapes
                            .getCellEditor().cancelCellEditing();
            }

            protected void onNoActivitySelection()
            {
                // annuler édition éventuellement en cours sur la table
                if (StepsPanel.this.pStepsConfigurationPanel.tEtapes
                        .getCellEditor() != null)
                    StepsPanel.this.pStepsConfigurationPanel.tEtapes
                            .getCellEditor().cancelCellEditing();
                // vider la liste des étapes
                StepsPanel.this.clearSteps();
                // griser le StepsManagementPanel
                Utilities.setEnabledContainer(
                        StepsPanel.this.pStepsConfigurationPanel, false);
            }

            protected void onActivityDoubleClick()
            {
            }
        };
        this.pProcessPanel.setMaximumSize(new Dimension(350, 350));
        this.pProcessPanel.setPreferredSize(new Dimension(350, 350));
        this.pProcessPanel.setMinimumSize(new Dimension(350, 350));
        // création d'un JSplitPane (à gauche : arbre avec, au-dessus de lui, un
        // bouton "autodétect d'étapes multiple", stepsManagementPanel à
        // droite)
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setOneTouchExpandable(true);
        JPanel pGauche = new JPanel(new BorderLayout());
        pGauche.add(this.pProcessPanel, BorderLayout.CENTER);
        JButton btDetectAllSteps = new JButton(ImagesManager.getInstance()
                .getSmallIcon("AutofillStepsIcon.gif"));
        btDetectAllSteps.setToolTipText(LanguagesManager.getInstance()
                .getString("StepsPanelAutodetectAllStepsTooltip"));
        btDetectAllSteps.setContentAreaFilled(false);
        btDetectAllSteps.setBorderPainted(false);
        btDetectAllSteps.addActionListener(new DetectAllStepsActionListener(
                process));
        JPanel pHtGauche = new JPanel(new BorderLayout());
        pHtGauche.add(btDetectAllSteps, BorderLayout.WEST);
        pGauche.add(pHtGauche, BorderLayout.NORTH);
        jsp.add(pGauche, JSplitPane.LEFT);
        // ajout du panneau de configuration des étapes
        this.pStepsConfigurationPanel = new StepsManagementPanel();
        jsp.add(this.pStepsConfigurationPanel, JSplitPane.RIGHT);
        this.add(jsp);
    }

    /**
     * vider la liste des étapes affichées
     */
    protected void clearSteps()
    {
        ((ActivityStepsTableModel) this.pStepsConfigurationPanel.tEtapes
                .getModel()).clear();
    }
}
