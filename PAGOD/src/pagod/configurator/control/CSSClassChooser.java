/*
 * $Id: CSSClassChooser.java,v 1.2 2006/02/19 15:36:04 yak Exp $
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

package pagod.configurator.control;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pagod.common.control.InterfaceManager;
import pagod.utils.LanguagesManager;

/**
 * Singleton de choix des classes des CSS pour l'autodétection des étapes
 * 
 * @author m1isi24
 */
public class CSSClassChooser
{
    /** Instance courante */
    private static CSSClassChooser currentInstance = null;

    /**
     * Retourne l'instance du sélecteur
     * 
     * @return Instance du sélecteur
     */
    public static CSSClassChooser getInstance()
    {
        if (CSSClassChooser.currentInstance == null)
            CSSClassChooser.currentInstance = new CSSClassChooser();
        return CSSClassChooser.currentInstance;
    }

    private CSSClassChooser()
    {
    }

    /** Liste des classes */
    private SortedSet<String> classes = new TreeSet<String>();

    /** Classe du titre */
    private String titleClass = "";

    /** Class du corps */
    private String bodyClass = "";

    /**
     * Retourne la classe pour les titres
     * 
     * @return Classe pour les titres
     */
    public String getTitleClass()
    {
        return this.titleClass;
    }

    /**
     * Retourne la classe pour les contenus
     * 
     * @return Classe pour les contenus
     */
    public String getBodyClass()
    {
        return this.bodyClass;
    }

    /**
     * Affiche la fenêtre de sélection
     * 
     * @return VRAI si le choix a été effectué, FAUX sinon
     */
    public boolean choose()
    {
        // extraction des classes
        this.extractClasses();

        // affichage de la fenêtre de choix
        final CSSClassChooserDialog dlg = new CSSClassChooserDialog(
                this.classes.toArray(), this.titleClass, this.bodyClass);
        dlg.setLocationByPlatform(true);
        dlg.setVisible(true);
        return dlg.wasValidate();
    }

    /**
     * Extrait les classes du fichier ouvert
     */
    private void extractClasses()
    {
        // création des expressions régulières
        final Pattern p1 = Pattern.compile("\\s*([^{]+)\\s*\\{[^}]*\\}");
        final Pattern p2 = Pattern.compile("(?:\\.([^,.:#\\s]+))");

        // création de l'ensemble
        this.classes = new TreeSet<String>();

        // lecture des classes
        final Matcher m = p1.matcher(InterfaceManager.getInstance()
                .getCSSFileContent());
        while (m.find())
        {
            final Matcher m1 = p2.matcher(m.group(1));
            while (m1.find())
            {
                this.classes.add(m1.group(1));
            }
        }
    }

    private class CSSClassChooserDialog extends JDialog
    {
        private final JComboBox lstTitle;

        private final JComboBox lstBody;

        private boolean validate;

        /**
         * Crée une fenêtre de choix de classe
         * 
         * @param values
         *            Valeurs contenues dans la combobox
         * @param currentTitle
         *            Classe des titres actuelle
         * @param currentBody
         *            Classe des corps actuelle
         */
        public CSSClassChooserDialog(Object[] values, String currentTitle,
                                     String currentBody)
        {
            super(ApplicationManager.getInstance().getMfPagod(),
                    LanguagesManager.getInstance().getString(
                            "CSSClassChooser_dialogTitle"), true);

            // bouton ok
            final JButton btOk = new JButton(LanguagesManager.getInstance()
                    .getString("OKButtonLabel"));
            btOk.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    CSSClassChooser.this.titleClass = (String) CSSClassChooserDialog.this.lstTitle
                            .getSelectedItem();
                    CSSClassChooser.this.bodyClass = (String) CSSClassChooserDialog.this.lstBody
                            .getSelectedItem();
                    CSSClassChooserDialog.this.validate = true;
                    CSSClassChooserDialog.this.dispose();
                }
            });

            // bouton annuler
            final JButton btCancel = new JButton(LanguagesManager.getInstance()
                    .getString("CancelButtonLabel"));
            btCancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    CSSClassChooserDialog.this.validate = false;
                    CSSClassChooserDialog.this.dispose();
                }
            });

            // panneau des boutons
            final JPanel pnlButtons = new JPanel();
            pnlButtons.add(btOk);
            pnlButtons.add(btCancel);

            // panneau des listes
            GridBagConstraints gridBagConstraints;
            final JPanel pnlLists = new JPanel(new GridBagLayout());

            final JLabel lblGeneral = new JLabel(LanguagesManager.getInstance()
                    .getString("CSSClassChooser_description"));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(0, 0, 5, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            pnlLists.add(lblGeneral, gridBagConstraints);

            // liste pour le titre
            this.lstTitle = new JComboBox(values);
            this.lstTitle.setEditable(true);
            this.lstTitle.setSelectedItem(currentTitle);

            final JLabel lblTitle = new JLabel(LanguagesManager.getInstance()
                    .getString("CSSClassChooser_titleClass"),
                    SwingConstants.RIGHT);
            lblTitle.setLabelFor(this.lstTitle);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlLists.add(lblTitle, gridBagConstraints);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            pnlLists.add(this.lstTitle, gridBagConstraints);

            // liste pour le corps
            this.lstBody = new JComboBox(values);
            this.lstBody.setEditable(true);
            this.lstBody.setSelectedItem(currentBody);

            final JLabel lblBody = new JLabel(LanguagesManager.getInstance()
                    .getString("CSSClassChooser_bodyClass"),
                    SwingConstants.RIGHT);
            lblBody.setLabelFor(this.lstBody);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlLists.add(lblBody, gridBagConstraints);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            pnlLists.add(this.lstBody, gridBagConstraints);

            // ajout des panneaux à la fenêtre
            final JPanel pnlGlobal = new JPanel(new BorderLayout());
            pnlGlobal.setBorder(new EmptyBorder(new Insets(10, 10, 5, 10)));
            pnlGlobal.add(pnlButtons, BorderLayout.SOUTH);
            pnlGlobal.add(pnlLists, BorderLayout.CENTER);
            this.getContentPane().add(pnlGlobal, BorderLayout.CENTER);

            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.pack();
        }

        /**
         * Retourne le choix de l'utilisateur
         * 
         * @return VRAI si la fenêtre a été validée, FAUX sinon
         */
        public boolean wasValidate()
        {
            return this.validate;
        }
    }
}
