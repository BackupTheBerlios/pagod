/*
 * $Id: AddToolDialog.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.MissingResourceException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pagod.common.model.Tool;
import pagod.configurator.control.adapters.ToolsProductsAssociationsTreeModel;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * @author Biniou & psyko
 */

public class AddToolDialog extends JDialog
{

    // model dans lequel on doit supprimer , ajouter modif
    ToolsProductsAssociationsTreeModel tmModel;

    /* attributs contenant l'extension et le chemin qu'aura saisi l'utilisateur */
    private JTextField txtTool = new JTextField(20);

    /* Attribut contenant les boutons */
    private JButton btAdd = null;

    private JButton btCancel;

    private JButton btUpdate = null;

    private String oldToolValue;
    
    private JLabel labErr;

    /**
     * Constructeur de la AddToolDialog
     * 
     * @param parent
     * @param model 
     * @throws NotInitializedException
     * @throws MissingResourceException
     */
    public AddToolDialog(JFrame parent, ToolsProductsAssociationsTreeModel model)
                                                                                 throws MissingResourceException,
                                                                                 NotInitializedException
    {

        // appel de la methode parente
        super(parent, LanguagesManager.getInstance().getString(
                "AddToolTitreAdd"), true);

       
        this.tmModel = model;

        // on spécifie les bouton avec leur titre
        this.btAdd = new JButton(LanguagesManager.getInstance().getString(
                "AddToolBtAdd"));
        this.btCancel = new JButton(LanguagesManager.getInstance().getString(
                "AddToolBtCancel"));

        JLabel lblTool = new JLabel(LanguagesManager.getInstance().getString(
                "AddToolLabel"));

        //
        /* panneau qui va contenir les JLabel et les JTextField */
        JPanel centralPanel = new JPanel();

        /*
         * on met un GridLayout qui sera un tableau a 2 lignes et 3 colonnes
         */
        GridBagLayout gridBag = new GridBagLayout();
        centralPanel.setLayout(gridBag);

        // contraintes pour l'ajout des composants
        GridBagConstraints c = new GridBagConstraints();

//      on y ajoute les JLabel et les JTextField
        this.labErr = new JLabel ("");
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        //on lie les contraintes au label extension
        gridBag.setConstraints( this.labErr, c);
        centralPanel.add( this.labErr);
        

        // on ajoute le label extension
        // on definit les contraintes pour le label extension
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au label extension
        gridBag.setConstraints(lblTool, c);
        centralPanel.add(lblTool);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 1;

        // on lie les contraintes au textField extension
        gridBag.setConstraints(this.txtTool, c);
        centralPanel.add(this.txtTool);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.btAdd);
        bottomPanel.add(this.btCancel);

        /*
         * maintenant que les panneaux sont crées il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * AddToolDialog c'est une methode herite de JDialog
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
                AddToolDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Ajout et Annuler
         */
        ListenerBoutons listenerBoutons = new ListenerBoutons();
        this.btAdd.addActionListener(listenerBoutons);
        this.btCancel.addActionListener(listenerBoutons);

        // ajout de listener sur les deux texte field
        this.txtTool.setFocusable(true);
        ListenerTextField listenerTextField = new ListenerTextField();
        this.txtTool.addCaretListener(listenerTextField);
        this.txtTool.addKeyListener(listenerTextField);

        // et on desactive le bouton
        AddToolDialog.this.btAdd.setEnabled(false);
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
     * Constructeurs de la AddToolDialog avec un paramètre indiquant l extension
     * et un paramètre indiquant le chemin
     * 
     * @param parent
     * @param tTool 
     * @param model
     * @throws NotInitializedException
     * @throws MissingResourceException
     **************************************************************************/

    public AddToolDialog(JFrame parent, Tool tTool,ToolsProductsAssociationsTreeModel model)
                                                          throws MissingResourceException,
                                                          NotInitializedException
    {
        // appel de la methode parente
        super(parent, LanguagesManager.getInstance().getString(
                "AddToolTitreUpdate"), true);
                
      
        this.tmModel = model;
        // on spécifie les bouton avec leur titre
        this.oldToolValue = tTool.getName();
        this.btCancel = new JButton(LanguagesManager.getInstance().getString(
                "AddToolBtCancel"));

        this.btUpdate = new JButton(LanguagesManager.getInstance().getString(
                "AddToolBtUpdate"));
        JLabel lblTool = new JLabel(LanguagesManager.getInstance().getString(
                "AddToolLabel"));

        // panneau qui va contenir les JLabel et les JTextField
        JPanel centralPanel = new JPanel();

        // on met un GridLayout qui sera un tableau a 2 lignes et 3 colonnes
        GridBagLayout gridBag = new GridBagLayout();
        centralPanel.setLayout(gridBag);

        // contraintes pour l'ajout des composants
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
//      on y ajoute les JLabel et les JTextField
        this.labErr = new JLabel ("");
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        
        //on lie les contraintes au label extension
        gridBag.setConstraints( this.labErr, c);
        centralPanel.add( this.labErr);

        // on ajoute le label extension
        // on definit les contraintes pour le label extension
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au label extension
        gridBag.setConstraints(lblTool, c);
        centralPanel.add(lblTool);

        // on ajoute le textField extension
        // on definit les contraintes pour le textField extension
        c.gridx = 1;
        c.gridy = 1;
        this.txtTool.setText(tTool.getName());

        // on lie les contraintes au textField extension
        gridBag.setConstraints(this.txtTool, c);
        centralPanel.add(this.txtTool);

        // panneau qui va contenir les autres boutons
        JPanel bottomPanel = new JPanel();

        /*
         * cette ligne est inutile car par defaut FlowLayout est le gestionnaire
         * de placement d'un JPanel
         */
        bottomPanel.setLayout(new FlowLayout());

        // on ajoute les boutons
        bottomPanel.add(this.btUpdate);
        bottomPanel.add(this.btCancel);

        /*
         * maintenant que les panneaux sont crées il faut les ajouter a la
         * fenetre par defaut le gestionnaire de placement d'une JFrame est un
         * BorderLayout this.getContentPane() retourne le panneau de
         * AddToolDialog c'est une methode herite de JDialog
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
                AddToolDialog.this.dispose();

            }
        });

        /*
         * maintenant il faut definir un ecouteur (Listener) d'evenements de la
         * fenetre pour gerer les actions a effectuer lors du clic sur les
         * boutons Parcourir, Ajout et Annuler
         */
        ListenerBoutons listenerBoutons = new ListenerBoutons();
        this.btUpdate.addActionListener(listenerBoutons);
        this.btCancel.addActionListener(listenerBoutons);
        // ajout de listener sur les deux texte field
        this.txtTool.setFocusable(true);
        ListenerTextField listenerTextField = new ListenerTextField();
        this.txtTool.addCaretListener(listenerTextField);
        this.txtTool.addKeyListener(listenerTextField);

        /*
         * on demande au gestionnaire d'adapter la taille de la fenetre en
         * fonction des composants qui sont dedans
         */
        this.pack();
        this.setLocationRelativeTo(parent);
        // les fenetres par defaut sont invisible donc on la rend visible
        this.setVisible(true);

    }

    private class ListenerTextField implements CaretListener, KeyListener
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

            if (!(AddToolDialog.this.txtTool.getText().equals("")))
            {
                if (AddToolDialog.this.btAdd != null)
                    AddToolDialog.this.btAdd.setEnabled(true);
                else if (AddToolDialog.this.btUpdate != null)
                    AddToolDialog.this.btUpdate.setEnabled(true);

            }
            else
            {
                if (AddToolDialog.this.btAdd != null)
                    AddToolDialog.this.btAdd.setEnabled(false);
                else if (AddToolDialog.this.btUpdate != null)
                    AddToolDialog.this.btUpdate.setEnabled(false);

            }
        }

        /**
         * @param e
         * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
         */
        public void keyReleased(KeyEvent e)
        {
            // appui sur la touche entrée = valide la saisie
            // TODO : j'ai pas pris en compte le futur clic sur update (ptêt que
            // faire les deux doClick à la suite ne posera pas de pb !
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                if (AddToolDialog.this.btUpdate == null)
                    AddToolDialog.this.btAdd.doClick();
                else
                    AddToolDialog.this.btUpdate.doClick();
                    
        }

        /**
         * @param e
         * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
         */
        public void keyTyped(KeyEvent e)
        {
        }

        /**
         * @param e
         * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
         */
        public void keyPressed(KeyEvent e)
        {
        }

    }

    private class ListenerBoutons implements ActionListener
    {
        /**
         * @param e
         */
        public void actionPerformed(ActionEvent e)
        {

            // on recupere le bouton sur lequel on a clique grace a
            // l'ActionEvent e
            JButton b = (JButton) e.getSource();

            /*
             * maintenant il faut savoir si c sur le bouton Ajout, Parcourir ou
             * sur le bouton Annuler qu'on a cliqué c'est pour cette raison
             * qu'on a mis les 3 boutons en attributs
             */

            if (b == AddToolDialog.this.btAdd
                    || b == AddToolDialog.this.btUpdate)
            {
                if (AddToolDialog.this.btUpdate != null)
                {
                    //si on fait update
                    
                    if (AddToolDialog.this.tmModel
                            .modifyTool( AddToolDialog.this.oldToolValue, AddToolDialog.this.txtTool.getText()))
                    
                        AddToolDialog.this.dispose();
                  
                    else
                    {
                        AddToolDialog.this.labErr.setText(LanguagesManager.getInstance()
                        .getString("AddToolWrongNameMsg"));
                        AddToolDialog.this.pack();

                    }
                }
                else
                {
                    // si on fait add
                    if (AddToolDialog.this.tmModel
                            .insertTool(AddToolDialog.this.txtTool.getText()))
                        AddToolDialog.this.dispose();
                    else
                    {
                        AddToolDialog.this.labErr.setText(LanguagesManager.getInstance()
                        .getString("AddToolWrongNameMsg"));
                        AddToolDialog.this.pack();

                    }
                }
            }

            else
            {
                /*
                 * vu qu'il y a que 3 boutons sur ecoute si on a pas clique sur
                 * btAdd ou sur btBrowse c'est qu'on a clique sur btCancel
                 */

                // on ferme la fenetre
                AddToolDialog.this.dispose();

            }

        }
    }

}
