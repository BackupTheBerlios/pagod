/*
 * $Id: ViewerPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pagod.configurator.control.PreferencesManager;
import pagod.configurator.control.PreferencesManager.InvalidExtensionException;
import pagod.utils.ExceptionManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.configurator.ui.AddExtensionDialog;

/**
 * @author yak
 * 
 */
public class ViewerPanel extends JPanel implements Observer
{

    // model contenant les données
    private TableModel tModel;

    // Jtable
    private JTable table;

    // bouton ajouter
    private JButton ajoutButton;

    // bouton supprimer
    private JButton supButton;

    // bouton modifier
    private JButton modButton;

    // fenetre parente du panel
    private JFrame parent;

    /**
     * 
     * @param parentFrame
     * @throws NotInitializedException
     * @throws MissingResourceException
     */
    public ViewerPanel(JFrame parentFrame) throws MissingResourceException,
                                          NotInitializedException
    {
        super();
        this.parent = parentFrame;
        this.table = new JTable();
        this.table.setFocusable(true);
        this.table.addKeyListener(new ListenerKey());
        this.table.addMouseListener(new ListenerMouse());
        // ajout d'un observer sur le preferences manager
        // afinde pouvoir voir les changements

        PreferencesManager.getInstance().addObserver(this);

        // panel gauche
        JPanel panelButton = new JPanel();
        // creation du gridbaglayout

        GridBagLayout panelButtonLayout = new GridBagLayout();
        panelButton.setLayout(panelButtonLayout);
        // création des bouton
        this.ajoutButton = new JButton(LanguagesManager.getInstance()
                .getString("ViewerAddbt"));

        this.supButton = new JButton(LanguagesManager.getInstance().getString(
                "ViewerDelbt"));
        this.modButton = new JButton(LanguagesManager.getInstance().getString(
                "ViewerModbt"));

        // creation de la contrainte pour le bouton ajouter

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.anchor = GridBagConstraints.NORTH;
        // ajout de la contrainte sur le bouton ajout

        panelButtonLayout.setConstraints(this.ajoutButton, constraint);
        // ajout du bouton sur le panel
        panelButton.add(this.ajoutButton);
        constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.anchor = GridBagConstraints.NORTH;
        panelButtonLayout.setConstraints(this.modButton, constraint);
        panelButton.add(this.modButton);

        constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.anchor = GridBagConstraints.NORTH;
        panelButtonLayout.setConstraints(this.supButton, constraint);
        panelButton.add(this.supButton);

        // ajout des bouton sur le layout

        // creation du modele (contenant les données)
        this.tModel = new TableModel();
        // association de la table et du model
        this.table.setModel(this.tModel);
        // on interdit la selection multiple

        this.table.setSelectionMode(0);
        // on spécifie le border layout pour le panel
        setLayout(new BorderLayout());

        JScrollPane scrollpane = new JScrollPane(this.table);
        // on ajoute la grille
        add(scrollpane, BorderLayout.CENTER);
        // on ajoute les boutons
        add(panelButton, BorderLayout.EAST);
        this.ajoutButton.addActionListener(new ListenerBoutons());
        this.supButton.addActionListener(new ListenerBoutons());
        this.modButton.addActionListener(new ListenerBoutons());

        // si il n'ya pas de donnée on désactive les boutton modifier et
        // supprimer
        ViewerPanel.this.supButton.setEnabled(false);
        ViewerPanel.this.modButton.setEnabled(false);

    }

    /**
     * 
     * @param o
     * @param arg
     * 
     */
    public void update(Observable o, Object arg)
    {
        this.tModel.fireTableStructureChanged();
    }

    /**
     * 
     * @author yak
     * 
     */
    private class ListenerKey extends KeyAdapter
    {
        /**
         * @param e
         */
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyChar() == KeyEvent.VK_DELETE)
            {
                ViewerPanel.this.supButton.doClick();
            }
            else if (e.getKeyChar() == KeyEvent.VK_ENTER)
            {
                ViewerPanel.this.modButton.doClick();
            }

        }
    }

    private class ListenerMouse extends MouseAdapter
    {
        /**
         * @param e
         */
        public void mouseClicked(MouseEvent e)
        {
            ViewerPanel.this.supButton.setEnabled(true);
            ViewerPanel.this.modButton.setEnabled(true);
        }
    }

    /**
     * @author yak listener pour les boutons
     */

    private class ListenerBoutons implements ActionListener
    {
        /**
         * 
         * 
         * @param e
         * @throws MissingResourceException
         */
        public void actionPerformed(ActionEvent e)
        {
            // on recupere la source de l'evenement
            JButton b = (JButton) e.getSource();
            // on test la source de l'evenement
            if (b == ViewerPanel.this.ajoutButton)
            {
                new AddExtensionDialog(ViewerPanel.this.parent);
                // désactivé

                ViewerPanel.this.table.changeSelection(0, 0, false, false);
                if (ViewerPanel.this.tModel.getRowCount() > 0)
                {
                    ViewerPanel.this.supButton.setEnabled(true);
                    ViewerPanel.this.modButton.setEnabled(true);
                }

            }
            else if (b == ViewerPanel.this.supButton)
            {

                int iRow = ViewerPanel.this.table.getSelectedRow();
                // on demande confirmation
                int iResponse;
                iResponse = JOptionPane.showOptionDialog(null, LanguagesManager
                        .getInstance().getString("ViewerConfirm"),
                        "confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, null, null);
                if (iResponse != 1)
                {
                    String str = ViewerPanel.this.tModel.getValueAt(iRow, 0);
                    PreferencesManager.getInstance().removePreference(str);

                    // on garde un selection sur la ligne precedente
                    // sauf si on a sélectionné la première ligne dans ce cas la
                    // on
                    // selectionne la première
                    if (iRow != 0)
                    {
                        ViewerPanel.this.table.changeSelection(iRow - 1, 0,
                                false, false);
                    }
                    else
                    {
                        ViewerPanel.this.table.changeSelection(0, 0, false,
                                false);
                    }
                    if (ViewerPanel.this.tModel.getRowCount() <= 0)
                    {
                        // si il n'ya pas de donnée on désactive les boutton
                        // modifier et supprimer
                        ViewerPanel.this.supButton.setEnabled(false);
                        ViewerPanel.this.modButton.setEnabled(false);
                    }
                }

            }
            else if (b == ViewerPanel.this.modButton)
            {

                String sKey = "";
                String sValue = "";
                int iRow = ViewerPanel.this.table.getSelectedRow();
                sKey = ViewerPanel.this.tModel.getValueAt(iRow, 0);
                sValue = ViewerPanel.this.tModel.getValueAt(iRow, 1);
                new AddExtensionDialog(ViewerPanel.this.parent, sKey, sValue);
                ViewerPanel.this.table.changeSelection(iRow, 0, false, false);

            }

        }
    }

    private class TableModel extends AbstractTableModel
    {
        /**
         * fonction qui retourne le nombre de ligne du model
         * 
         * 
         * @return int
         */
        public int getRowCount()
        {
            return PreferencesManager.getInstance().numberExtensions();
        }

        /**
         * retourne le nombre de colonne du modele
         * 
         * @return int
         */
        public int getColumnCount()
        {
            return 2;
        }

        /**
         * 
         * 
         * @param iRowIndex
         * @param iColumnIndex
         * @return String
         */
        public String getValueAt(int iRowIndex, int iColumnIndex)
        {
            ArrayList<String> keyPref = PreferencesManager.getInstance()
                    .preferences();

            String key = keyPref.get(iRowIndex);
            if (iColumnIndex == 0)
                return key;
            else
                try
                {
                    return PreferencesManager.getInstance().getPreference(key);
                }
                catch (InvalidExtensionException e)
                {
                    // TODO Bloc de traitement des exceptions généré
                    ExceptionManager.getInstance().manage(e);
                    return "";
                }
        }

        /**
         * // fonction qui renvoie le nom des colonnes
         * 
         * @param iColumnIndex
         * @return String
         * 
         */
        public String getColumnName(int iColumnIndex)
        {
            if (iColumnIndex == 0)
            {
                return LanguagesManager.getInstance().getString(
                        "ColumnNameExtension");
            }
            else
            {
                return LanguagesManager.getInstance().getString(
                        "ColumnNamePath");
            }
        }

    }
}
