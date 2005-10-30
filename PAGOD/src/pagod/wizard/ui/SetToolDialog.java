/*
 * $Id: SetToolDialog.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import pagod.common.model.Tool;
import pagod.utils.LanguagesManager;

/**
 * Cette classe permet de cr?e une fenetre permettant de configurer un Tool
 * (c'est a dire indiquer le path du Tool)
 * 
 * @author Alexandre Bes
 */
public class SetToolDialog extends JDialog
{

    /* attributs contenant l'extension et le chemin qu'aura saisi l'utilisateur */
    private JLabel lTool = new JLabel();

    private JTextField tfPath = new JTextField(20);

    /* Attribut contenant les boutons */

    private JButton bpCancel;

    private JButton bpBrowse;

    private JButton bpUpdate = null;

    /**
     * Tool a configurer
     */
    Tool tool;

    /**
     * Constructeur d'un SetToolDialog, cette boite de dialogue permet de
     * modifier le chemin d'acces a l'executable correspondant au Tool.
     * 
     * @param dParent
     *            fenetre de dialog parente a celle la
     * @param tool
     *            tool que l'on veut configurer
     */
    public SetToolDialog(JFrame dParent, Tool tool)
    {
        // appel de la methode parente
        super(dParent,
                LanguagesManager.getInstance().getString("SetToolTitre"), true);
        this.setModal(true);

        this.tool = tool;

        // on sp?cifie les bouton avec leur titre
        this.bpCancel = new JButton(LanguagesManager.getInstance().getString(
                "SetToolBtCancel"));
        this.bpBrowse = new JButton(LanguagesManager.getInstance().getString(
                "SetToolBtBrowse"));
        this.bpUpdate = new JButton(LanguagesManager.getInstance().getString(
                "SetToolBtUpdate"));
        JLabel lblTool = new JLabel(LanguagesManager.getInstance().getString(
                "SetToolLabelTool"));
        JLabel lblPath = new JLabel(LanguagesManager.getInstance().getString(
                "SetToolLabelPath"));

        // panneau qui va contenir les JLabel et les JTextField */
        JPanel centralPanel = new JPanel();

        // on met un GridLayout qui sera un tableau a 2 lignes et 3 colonnes
        GridBagLayout gridBag = new GridBagLayout();
        centralPanel.setLayout(gridBag);

        // contraintes pour l'ajout des composants
        GridBagConstraints c = new GridBagConstraints();

        // on y ajoute les JLabel et les JTextField

        // on ajoute le label extension
        // on definit les contraintes pour le label extension
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au label extension
        gridBag.setConstraints(lblTool, c);
        centralPanel.add(lblTool);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 0;
        this.lTool.setText(tool.getName());

        // on lie les contraintes au label Tool
        gridBag.setConstraints(this.lTool, c);
        centralPanel.add(this.lTool);

        // on ajoute le label path
        // on definit les contraintes pour le label path
        c.gridx = 0;
        c.gridy = 1;

        // on lie les contraintes au label path
        gridBag.setConstraints(lblPath, c);
        centralPanel.add(lblPath);

        // on ajoute le textField path
        // on definit les contraintes pour le textField path
        c.gridx = 1;
        c.gridy = 1;

        // s'il y a deja un path on l'affiche
        if (tool.getPath() != null)
            this.tfPath.setText(tool.getPath());

        // on lie les contraintes au textField path
        gridBag.setConstraints(this.tfPath, c);
        centralPanel.add(this.tfPath);

        // on ajoute le bouton parcourir
        // on definit les contraintes pour le bouton
        c.gridx = 2;
        c.gridy = 1;

        // on lie les contraintes au bouton parcourir
        gridBag.setConstraints(this.bpBrowse, c);
        centralPanel.add(this.bpBrowse);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.bpUpdate);
        bottomPanel.add(this.bpCancel);

        /*
         * maintenant que les panneaux sont cr?es il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * SetToolDialog c'est une methode herite de JDialog
         */
        this.getContentPane().add(centralPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        /*
         * on ajoute une inner class anonyme (classe interne) faisant en sorte
         * que la croix en haut a droite ferme bien la fenetre
         */
        this.addWindowListener(new WindowAdapter()
        {
            // on redefinit la methode windowsClosing heritee de WindowAdapter
            void windowsClosing(WindowEvent e)
            {
                /*
                 * on ferme la fenetre une inner class peut acceder aux
                 * attributs de la classe qui la contient en Donnant sont nom
                 * suivit de .this puis le nom de l'attribut ou de la methode
                 */
                SetToolDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Modifier et Annuler
         */
        this.bpUpdate.addActionListener(new ListenerBoutons());
        this.bpCancel.addActionListener(new ListenerBoutons());
        this.bpBrowse.addActionListener(new ListenerBoutons());
        // on ajoute des listener sur les boutons

        this.tfPath.setFocusable(true);
        this.tfPath.addCaretListener(new ListenerTextField());

        // on met le bouton btUpdate grise si y a rien dans txtPath
        if (this.tfPath.getText().equals(""))
            this.bpUpdate.setEnabled(false);

        /*
         * on demande au gestionnaire d'adapter la taille de la fenetre en
         * fonction des composants qui sont dedans
         */
        this.pack();
        this.setLocationRelativeTo(dParent);

        // les fenetres par defaut sont invisible donc on la rend visible
        this.setVisible(true);
    }

    private class ListenerTextField implements CaretListener
    {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
         */
        /**
         * @param arg0
         */
        public void caretUpdate(CaretEvent arg0)
        {

            if (!(SetToolDialog.this.tfPath.getText().equals("")))
            {
                if (SetToolDialog.this.bpUpdate != null)
                    SetToolDialog.this.bpUpdate.setEnabled(true);

            }
            else
            {
                if (SetToolDialog.this.bpUpdate != null)
                    SetToolDialog.this.bpUpdate.setEnabled(false);

            }
        }

    }

    private class ListenerBoutons implements ActionListener
    {
        /**
         * 
         * 
         * @param e
         */
        public void actionPerformed(ActionEvent e)
        {

            // on recupere le bouton sur lequel on a clique grace a
            // l'ActionEvent e
            JButton b = (JButton) e.getSource();

            /*
             * maintenant il faut savoir si c sur le bouton Modifier, Parcourir
             * ou sur le bouton Annuler qu'on a cliqu? c'est pour cette raison
             * qu'on a mis les 3 boutons en attributs
             */

            if (b == SetToolDialog.this.bpUpdate)
            {
                // on a cliqu? sur le bpUpdate

                // on va v?rifier que le champs path est bien
                // remplis

                // on test si le fichier existe
                File file = new File(SetToolDialog.this.tfPath.getText());

                // si le fichier n'existe pas on leve une exception
                if (!file.exists())
                {
                        // on affiche un message explicant que le programme
                        // n'existe pas
                        JOptionPane.showMessageDialog(SetToolDialog.this,
                                LanguagesManager.getInstance().getString(
                                        "SetToolErrorFileNotFoundException"),
                                LanguagesManager.getInstance().getString(
                                        "AddExtErrorTitle"),
                                JOptionPane.ERROR_MESSAGE);
                }

                // modification du path du Tool
                SetToolDialog.this.tool.setPath(SetToolDialog.this.tfPath
                        .getText());

                // TODO il faut notifier qu'il y a eu un changement

                SetToolDialog.this.dispose();

            }
            else if (b == SetToolDialog.this.bpBrowse)
            {

                JFileChooser homeChooser = new JFileChooser();

                int returnVal = homeChooser.showOpenDialog(null);

                // une fois qu'on a choisi une application
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {

                    // Recupere le path du fichier choisi
                    File file = homeChooser.getSelectedFile();
                    String filePath = file.getAbsoluteFile().getAbsolutePath();

                    // Met lengthPath dans le txtfield
                    SetToolDialog.this.tfPath.setText(filePath);

                }

            }
            else
            {
                /*
                 * vu qu'il y a que 3 boutons sur ecoute si on a pas clique sur
                 * btUpdate ou sur btBrowse c'est qu'on a clique sur btCancel
                 */

                // on ferme la fenetre
                SetToolDialog.this.dispose();

            }

        }
    }

}
