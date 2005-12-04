/*
 * $Id: AddExtensionDialog.java,v 1.2 2005/12/04 18:20:06 yak Exp $
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.MissingResourceException;

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
import javax.swing.filechooser.FileSystemView;

import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.PreferencesManager.FileNotExecuteException;
import pagod.wizard.control.PreferencesManager.InvalidExtensionException;

/**
 * 
 * @author Biniou & psyko
 */

public class AddExtensionDialog extends JDialog
{

    /* attributs contenant l'extension et le chemin qu'aura saisi l'utilisateur */
    private JTextField txtExtension = new JTextField(20);

    // JLabel qui contient l'extension si on lq modifie

    private JTextField txtPath = new JTextField(20);

    /* Attribut contenant les boutons */
    private JButton btAdd = null;

    private JButton btCancel;

    private JButton btBrowse;
    
    private JButton btDefault;

    private JButton btUpdate = null;

    private String oldKey;

    /**
     * Constructeur de la AddExtensionDialog
     * 
     * @param parent
     */
    public AddExtensionDialog(JFrame parent)
    {

        // appel de la methode parente
        super(parent, LanguagesManager.getInstance()
                .getString("AddExtTitreAdd"), true);
        this.setFocusable(true);
        // on sp?cifie les bouton avec leur titre
        this.btAdd = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtAdd"));
        this.btCancel = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtCancel"));
        this.btBrowse = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtBrowse"));
        this.btDefault = new JButton (LanguagesManager.getInstance().getString(
                "AddExtBtDefault"));

        JLabel lblExtension = new JLabel(LanguagesManager.getInstance()
                .getString("AddExtLabelExt"));
        JLabel lblPath = new JLabel(LanguagesManager.getInstance().getString(
                "AddExtLabelPath"));

        /* panneau qui va contenir les JLabel et les JTextField */
        JPanel centralPanel = new JPanel();

        /*
         * 
         * on met un GridLayout qui sera un tableau a 2 lignes et 3 colonnes
         */
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
        gridBag.setConstraints(lblExtension, c);
        centralPanel.add(lblExtension);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 0;

        // on lie les contraintes au textField extension
        gridBag.setConstraints(this.txtExtension, c);
        centralPanel.add(this.txtExtension);

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

        // on lie les contraintes au textField path
        gridBag.setConstraints(this.txtPath, c);
        centralPanel.add(this.txtPath);

        // on ajoute le bouton parcourir
        // on definit les contraintes pour le bouton
        c.gridx = 2;
        c.gridy = 1;

        // on lie les contraintes au bouton parcourir
        gridBag.setConstraints(this.btBrowse, c);
        centralPanel.add(this.btBrowse);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.btAdd);
        bottomPanel.add(this.btDefault);
        bottomPanel.add(this.btCancel);

        /*
         * maintenant que les panneaux sont cr?es il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * AddExtensionDialog c'est une methode herite de JDialog
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
                AddExtensionDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Ajout et Annuler
         */
        this.btAdd.addActionListener(new ListenerBoutons());
        this.btCancel.addActionListener(new ListenerBoutons());
        this.btDefault.addActionListener(new ListenerBoutons());
        this.btBrowse.addActionListener(new ListenerBoutons());
        // ajout de listener sur les deux texte field
        this.txtExtension.setFocusable(true);
        this.txtExtension.addCaretListener(new ListenerTextField());
        this.txtPath.setFocusable(true);
        this.txtPath.addCaretListener(new ListenerTextField());
        // et on desactive le bouton
        AddExtensionDialog.this.btAdd.setEnabled(false);
        AddExtensionDialog.this.btDefault.setEnabled(false);
        /*
         * on demande au gestionnaire d'adapter la taille de la fenetre en
         * fonction des composants qui sont dedans
         */
        this.addKeyListener(new ListenerKey());
        this.pack();
        this.setLocationRelativeTo(parent);
        // les fenetres par defaut sont invisible donc on la rend visible
        this.setVisible(true);

    }

    /***************************************************************************
     * Constructeurs de la AddExtensionDialog avec un param?tre indiquant l
     * extension et un param?tre indiquant le chemin
     * 
     * @param parent
     * @param extension
     * @param path
     * @throws NotInitializedException
     * @throws MissingResourceException
     **************************************************************************/

    public AddExtensionDialog(JFrame parent, String extension, String path)
    {
        // appel de la methode parente
        super(parent, LanguagesManager.getInstance().getString(
                "AddExtTitreUpdate"), true);
        this.setFocusable(true);
        // on enregistre l'ancienne valeur
        this.oldKey = extension;
        // on sp?cifie les bouton avec leur titre

        this.btCancel = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtCancel"));
        this.btBrowse = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtBrowse"));
        this.btDefault = new JButton (LanguagesManager.getInstance().getString(
        "AddExtBtDefault"));
        this.btUpdate = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtUpdate"));
        JLabel lblExtension = new JLabel(LanguagesManager.getInstance()
                .getString("AddExtLabelExt"));
        JLabel lblPath = new JLabel(LanguagesManager.getInstance().getString(
                "AddExtLabelPath"));

        // panneau qui va contenir les JLabel et les JTextField
        JPanel centralPanel = new JPanel();
        // osn desactive le jtextField
        this.txtExtension.setEditable(false);

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
        gridBag.setConstraints(lblExtension, c);
        centralPanel.add(lblExtension);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 0;
        this.txtExtension.setText(extension);

        // on lie les contraintes au textField extension
        gridBag.setConstraints(this.txtExtension, c);
        centralPanel.add(this.txtExtension);

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
        this.txtPath.setText(path);

        // on lie les contraintes au textField path
        gridBag.setConstraints(this.txtPath, c);
        centralPanel.add(this.txtPath);

        // on ajoute le bouton parcourir
        // on definit les contraintes pour le bouton

        c.gridx = 2;
        c.gridy = 1;

        // on lie les contraintes au bouton parcourir
        gridBag.setConstraints(this.btBrowse, c);
        centralPanel.add(this.btBrowse);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.btUpdate);
        bottomPanel.add(this.btDefault);
        
        bottomPanel.add(this.btCancel);

        /*
         * maintenant que les panneaux sont cr?es il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * AddExtensionDialog c'est une methode herite de JDialog
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
                AddExtensionDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Ajout et Annuler
         */
        this.btUpdate.addActionListener(new ListenerBoutons());
        this.btCancel.addActionListener(new ListenerBoutons());

        this.btDefault.addActionListener(new ListenerBoutons());
        this.btBrowse.addActionListener(new ListenerBoutons());
        // ajout de listener sur les deux texte field
        this.txtExtension.setFocusable(true);
        this.txtExtension.addCaretListener(new ListenerTextField());
        this.txtPath.setFocusable(true);
        this.txtPath.addCaretListener(new ListenerTextField());
        this.addKeyListener(new ListenerKey());
        /*
         * on demande au gestionnaire d'adapter la taille de la fenetre en
         * fonction des composants qui sont dedans
         */
        this.pack();
        this.setLocationRelativeTo(parent);
        // les fenetres par defaut sont invisible donc on la rend visible
        this.setVisible(true);

    }

    /***************************************************************************
     * Constructeurs de la AddExtensionDialog avec un param?tre indiquant l
     * extension
     * 
     * @param parent
     * @param extension
     * @throws NotInitializedException
     * @throws MissingResourceException
     **************************************************************************/
    public AddExtensionDialog(JFrame parent, String extension)
    {
        // appel de la methode parente
        super(parent, LanguagesManager.getInstance()
                .getString("AddExtTitreAdd"), true);
        // on sp?cifie les bouton avec leur titre

        this.btCancel = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtCancel"));
        this.btBrowse = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtBrowse"));
        this.btDefault = new JButton (LanguagesManager.getInstance().getString(
        "AddExtBtDefault"));
        this.btUpdate = new JButton(LanguagesManager.getInstance().getString(
                "AddExtBtUpdate"));
        
        
        JLabel lblExtension = new JLabel(LanguagesManager.getInstance()
                .getString("AddExtLabelExt"));
        JLabel lblPath = new JLabel(LanguagesManager.getInstance().getString(
                "AddExtLabelPath"));
        // osn desactive le jtextField
        this.txtExtension.setEditable(false);

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
        gridBag.setConstraints(lblExtension, c);
        centralPanel.add(lblExtension);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 0;
        this.txtExtension.setText(extension);

        // on lie les contraintes au textField extension
        gridBag.setConstraints(this.txtExtension, c);
        centralPanel.add(this.txtExtension);

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

        // on lie les contraintes au textField path
        gridBag.setConstraints(this.txtPath, c);
        centralPanel.add(this.txtPath);

        // on ajoute le bouton parcourir
        // on definit les contraintes pour le bouton
        c.gridx = 2;
        c.gridy = 1;

        // on lie les contraintes au bouton parcourir
        gridBag.setConstraints(this.btBrowse, c);
        centralPanel.add(this.btBrowse);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.btUpdate);
        bottomPanel.add(this.btDefault);
        bottomPanel.add(this.btCancel);

        /*
         * maintenant que les panneaux sont cr?es il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * AddExtensionDialog c'est une methode herite de JDialog
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
                AddExtensionDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Ajout et Annuler
         */
        this.btUpdate.addActionListener(new ListenerBoutons());
        this.btCancel.addActionListener(new ListenerBoutons());
        this.btDefault.addActionListener(new ListenerBoutons());
        this.btBrowse.addActionListener(new ListenerBoutons());
        // on ajoute des listener sur les boutons

        this.txtPath.setFocusable(true);
        this.txtPath.addCaretListener(new ListenerTextField());

        this.addKeyListener(new ListenerKey());
        /*
         * on demande au gestionnaire d'adapter la taille de la fenetre en
         * fonction des composants qui sont dedans
         */
        this.pack();
        this.setLocationRelativeTo(parent);
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

            if (!(AddExtensionDialog.this.txtExtension.getText().equals(""))
                    && !(AddExtensionDialog.this.txtPath.getText().equals("")))
            {
                if (AddExtensionDialog.this.btAdd != null)
                    AddExtensionDialog.this.btAdd.setEnabled(true);
                else if (AddExtensionDialog.this.btUpdate != null)
                    AddExtensionDialog.this.btUpdate.setEnabled(true);

            }
            else
            {
                if (AddExtensionDialog.this.btAdd != null)
                    AddExtensionDialog.this.btAdd.setEnabled(false);
                else if (AddExtensionDialog.this.btUpdate != null)
                    AddExtensionDialog.this.btUpdate.setEnabled(false);

            }
            if (!(AddExtensionDialog.this.txtExtension.getText().equals("")))
            {
            	AddExtensionDialog.this.btDefault.setEnabled(true);
            }
            else
            {
            	AddExtensionDialog.this.btDefault.setEnabled(false);
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
             * maintenant il faut savoir si c sur le bouton Ajout, Parcourir ou
             * sur le bouton Annuler qu'on a cliqu? c'est pour cette raison
             * qu'on a mis les 3 boutons en attributs
             */

            if (b == AddExtensionDialog.this.btAdd
                    || b == AddExtensionDialog.this.btUpdate)
            {
                // on a cliqu? sur le btAdd

                // on va v?rifier que les 2 champs soient bien
                // remplis

                try
                {
                    // on ajoute directement
                    PreferencesManager.getInstance().setPreference(
                            AddExtensionDialog.this.txtExtension.getText(),
                            AddExtensionDialog.this.txtPath.getText());
                    AddExtensionDialog.this.dispose();
                }
                catch (FileNotFoundException e1)
                {
                    // on affiche un message explicant que le programme
                    // n'existe pas
                    JOptionPane.showMessageDialog(AddExtensionDialog.this,
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorFileNotFoundException"),
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorTitle"),
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (InvalidExtensionException e1)
                {
                    // on affiche un message explicant que l'extension saisi
                    // est invalide
                    JOptionPane.showMessageDialog(AddExtensionDialog.this,
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorInvalidExtensionException"),
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorTitle"),
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (FileNotExecuteException e1)
                {
                    // on affiche un message explicant que le programme
                    // saisi pour ouvir l'extensio
                    // n'est pas executable
                    JOptionPane.showMessageDialog(AddExtensionDialog.this,
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorFileNotExecuteException"),
                            LanguagesManager.getInstance().getString(
                                    "AddExtErrorTitle"),
                            JOptionPane.ERROR_MESSAGE);
                }

            }
            else if (b == AddExtensionDialog.this.btBrowse)
            {

                // retourne la vue du syst?me
                FileSystemView viewSystem = FileSystemView.getFileSystemView();
                // r?cup?ration des r?pertoires
                File home = viewSystem.getHomeDirectory();
                // cr?ation et affichage de l'arborescence
                // a partir du poste de travail
                JFileChooser homeChooser = new JFileChooser(home);

                int returnVal = homeChooser.showOpenDialog(null);

                // une fois qu'on a choisi une application
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {

                    // Recupere le path du fichier choisi
                    File file = homeChooser.getSelectedFile();
                    String filePath = file.getAbsoluteFile().getAbsolutePath();

                    // Met lengthPath dans le txtfield
                    AddExtensionDialog.this.txtPath.setText(filePath);

                }
            }
            else if (b == AddExtensionDialog.this.btDefault)
            {
            	 try
                 {
                     // on ajoute directement
                     PreferencesManager.getInstance().setPreference(
                             AddExtensionDialog.this.txtExtension.getText(),"default");
                     AddExtensionDialog.this.dispose();
                 }
                 catch (FileNotFoundException e1)
                 {
                     // on affiche un message explicant que le programme
                     // n'existe pas
                     JOptionPane.showMessageDialog(AddExtensionDialog.this,
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorFileNotFoundException"),
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorTitle"),
                             JOptionPane.ERROR_MESSAGE);
                 }
                 catch (InvalidExtensionException e1)
                 {
                     // on affiche un message explicant que l'extension saisi
                     // est invalide
                     JOptionPane.showMessageDialog(AddExtensionDialog.this,
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorInvalidExtensionException"),
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorTitle"),
                             JOptionPane.ERROR_MESSAGE);
                 }
                 catch (FileNotExecuteException e1)
                 {
                     // on affiche un message explicant que le programme
                     // saisi pour ouvir l'extensio
                     // n'est pas executable
                     JOptionPane.showMessageDialog(AddExtensionDialog.this,
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorFileNotExecuteException"),
                             LanguagesManager.getInstance().getString(
                                     "AddExtErrorTitle"),
                             JOptionPane.ERROR_MESSAGE);
                 }
            }
            else
            {
                /*
                 * vu qu'il y a que 3 boutons sur ecoute si on a pas clique sur
                 * btAdd ou sur btBrowse c'est qu'on a clique sur btCancel
                 */

                // on ferme la fenetre
                AddExtensionDialog.this.dispose();

            }

        }
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
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
            {
                AddExtensionDialog.this.btCancel.doClick();
            }
            else if (e.getKeyChar() == KeyEvent.VK_ENTER)
            {
                if (AddExtensionDialog.this.btUpdate != null)
                    AddExtensionDialog.this.btUpdate.doClick();
                else
                    AddExtensionDialog.this.btAdd.doClick();
            }

        }
    }
}
