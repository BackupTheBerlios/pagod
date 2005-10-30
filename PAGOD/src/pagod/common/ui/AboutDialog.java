/*
 * $Id: AboutDialog.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 *PAGOD- Personal assistant for group of development
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

package pagod.common.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Benjamin Fenêtre "A propos"
 */
public class AboutDialog extends JDialog implements ActionListener
{
    /** Bouton OK */
    private JButton pbOk = null;

    /**
     * Constructeur d'un AboutDialog, ayant pour fenêtre appelante parentFrame
     * 
     * @param parentFrame
     *            fenêtre appelante du AboutDialog
     * @param sVersion
     *            Version de l'application sous forme de chaîne
     */
    public AboutDialog(JFrame parentFrame, String sVersion)
    {
        // construire boite de dialogue, de titre à récupérer dans la locale
        super(parentFrame, LanguagesManager.getInstance().getString(
                "aboutFrameTitle"), true);

        this.pbOk = new JButton("OK");

        GridBagConstraints gridBagConstraints;

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        // Boutton par défaut : bouton "OK" (=> touche ENTER = bouton OK)
        this.pbOk.setDefaultCapable(true);
        this.getRootPane().setDefaultButton(this.pbOk);

        this.pbOk.addActionListener(this);

        southPanel.add(this.pbOk);

        // contenu du about
        JLabel lbl;

        lbl = new JLabel(ImagesManager.getInstance().getIcon("logoPAGOD.png"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogWhatIsPagod"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogLicence"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogAuthors"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogUsedComponents"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(sVersion);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(lbl, gridBagConstraints);

        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        // boîte de dialogue modale et centrée par rapport à l'appelant

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parentFrame);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        // clic sur OK --> fermer la fenêtre
        if (e.getSource() == this.pbOk)
            dispose();
    }

}