/*
 * $Id: PreferencesDialog.java,v 1.2 2006/01/20 15:43:38 coincoin Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pagod.utils.LanguagesManager;
import pagod.wizard.control.PreferencesManager;

/**
 * Fenetre des Pr?f?rences de l'application.
 * 
 * @author MoOky
 */
public class PreferencesDialog extends JDialog implements ActionListener
{
    /**
     * Bouton Valider
     */
    private JButton pbOk = null;

    /**
     * Bouton Annuler
     */
    private JButton pbCancel = null;

    private LanguageChooserPanel pLanguage = null;

    private ViewerPanel pViewer = null;

    /**
     * Constructeur d'une PreferencesDialog, ayant pour fen?tre appelante
     * parentFrame
     * 
     * @param parentFrame
     *            fen?tre appelante du AboutDialog Si
     *            GraphicsEnvironment.isHeadless() retourne vrai.
     */
    public PreferencesDialog(JFrame parentFrame)
    {
        super(parentFrame);
        // bo?te de dialogue modale et centr?e par rapport ? l'appelant
        this.setModal(true);

        // on met le titre
        this.setTitle(LanguagesManager.getInstance().getString(
                "PreferencesDialogTitle"));

        // on cree le panneau a onglet
        JTabbedPane tpPreferences = new JTabbedPane();

        // ajout de l'onglet permettant de changer la langue
        this.pLanguage = new LanguageChooserPanel();
        tpPreferences.addTab(LanguagesManager.getInstance().getString(
                "LanguageTabTitle"), this.pLanguage);

        // ajout de l'onglet permettant de gerer les associations
        this.pViewer = new ViewerPanel(parentFrame);
        tpPreferences.addTab(LanguagesManager.getInstance().getString(
                "ExtensionTabTitle"), this.pViewer);

        // panneau contenant les boutons
        JPanel pButton = new JPanel();

        // creation des boutons Ok et annuler
        this.pbOk = new JButton(LanguagesManager.getInstance().getString(
                "OKButtonLabel"));
        this.pbCancel = new JButton(LanguagesManager.getInstance().getString(
                "CancelButtonLabel"));

        // ajout des boutons
        pButton.add(this.pbOk);
        pButton.add(this.pbCancel);

        // mise sur ecoute des boutons
        this.pbOk.addActionListener(this);
        this.pbCancel.addActionListener(this);

        // ajout des panneaux d'onglet et de bouton dans la JDialog
        this.setLayout(new BorderLayout());
        this.getContentPane().add(tpPreferences, BorderLayout.CENTER);
        this.getContentPane().add(pButton, BorderLayout.SOUTH);

        // on sauvegarde les preferences pour ne pas perdre celle qui ont put
        // etre modifer ailleur qu'en passant par cette fenetre
        PreferencesManager.getInstance().storePreferences();
        this.pack();

        // bo?te de dialogue centr?e par rapport ? l'appelant
        this.setLocationRelativeTo(parentFrame);
    }

    /**
     * 
     * @param e
     *            evenement
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == this.pbOk)
        {
            // on a clique sur le bouton Ok

            // on recupere la langue selectionne et la precedente
            String sLang = this.pLanguage.getSelectedLanguage();
            String sPrecLang = PreferencesManager.getInstance().getLanguage();

            PreferencesManager.getInstance().setLanguage(sLang);
            /*Modif Coin coin*/
            PreferencesManager.getInstance().setWorkspace(this.pLanguage.getWorkspace());
            /*Fin Modif Coin coin*/
            PreferencesManager.getInstance().storePreferences();
            PreferencesManager.getInstance().setLanguage(sPrecLang);

            if (!sLang.equals(sPrecLang))
            {
                // on affiche un message comme quoi la langue a change
                JOptionPane.showMessageDialog(
                        PreferencesDialog.this.getOwner(), LanguagesManager
                                .getInstance().getString("infoChangeLanguage"),
                        LanguagesManager.getInstance().getString(
                                "titleInfoChangeLanguage"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            
            
            
            
            this.dispose();
        }
        else
        {
            // on a cliquer sur le bouton annuler
            // on charge les preferences avant les modifications
            PreferencesManager.getInstance().loadPreferences();
            this.dispose();
        }
    }
}
