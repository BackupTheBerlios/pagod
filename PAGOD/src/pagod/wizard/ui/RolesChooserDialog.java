/*
 * $Id: RolesChooserDialog.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pagod.utils.LanguagesManager;
import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Role;

/**
 * Fenetre de dialogue de choix des roles
 * 
 * @author MoOky
 */
public class RolesChooserDialog extends JDialog
{
    /**
     * Retourne cette valeur si ok ou validé est choisie.
     */
    public static final int APPROVE_OPTION = 0;

    /**
     * Retourne cette valeur si annuler est choisie.
     */
    public static final int CANCEL_OPTION = 1;

    /**
     * Retourne cette valeur si une erreur survient.
     */
    public static final int ERROR_OPTION = 2;

    /**
     * valeur de retour
     */
    private int returnValue;

    /**
     * Element graphique qui affichera les roles possibles
     */
    private JList listPossibleRoles;

    /**
     * Element graphique qui affichera les roles choisis
     */
    private JList listChosenRoles;

    /**
     * Bouton permettant de faire passer un role de la liste des roles possibles
     * a la liste de roles choisis
     */
    private JButton bt_RightButton = new JButton(">");

    /**
     * Gestionnaire d'évènements du bouton ">"
     */
    private ActionListener alRightButton;

    /**
     * Bouton permettant de faire passer un role de la liste des roles choisis a
     * la liste de roles possibles
     */
    private JButton bt_LeftButton = new JButton("<");

    /**
     * Gestionnaire d'évènements du bouton " <"
     */
    private ActionListener alLeftButton;

    /**
     * Bouton permettant valider le choix des roles
     */
    private JButton bt_OKButton = new JButton(LanguagesManager.getInstance()
            .getString("OKButtonLabel"));

    /**
     * Bouton permettant d'annuler l'affichage de la fenetre de choix des roles
     */
    private JButton bt_CancelButton = new JButton(LanguagesManager
            .getInstance().getString("CancelButtonLabel"));

    /**
     * Constructeur d'une fenetre de dialogue modale
     * 
     * @param owner
     *            La fenetre parente
     * @param roles
     *            Listes des roles suceptible d'etre choisis
     */
    public RolesChooserDialog(Frame owner, Collection<Role> roles)
    {
        // créer une un fenetre de dialogue modale dont le parents est owned
        super(owner, LanguagesManager.getInstance().getString(
                "RolesChooserDialogTitle"), true);
        this.setLayout(new BorderLayout());
        DefaultListModel listModelJList1 = new DefaultListModel();
        // initialisation de la liste avec les noms des roles
        for (Role role : roles)
        {
            listModelJList1.addElement(role);
        }
        DefaultListModel listModelJList2 = new DefaultListModel();
        this.listPossibleRoles = new JList(listModelJList1);
        this.listPossibleRoles.setCellRenderer(new PagodListCellRenderer());
        this.listChosenRoles = new JList(listModelJList2);
        this.listChosenRoles.setCellRenderer(new PagodListCellRenderer());
        this.bt_RightButton
                .addActionListener(this.alRightButton = new java.awt.event.ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        putRoleToRightList();
                        if (RolesChooserDialog.this.bt_LeftButton.isEnabled() == false
                                && !((DefaultListModel) RolesChooserDialog.this.listChosenRoles
                                        .getModel()).isEmpty())
                        {
                            RolesChooserDialog.this.bt_LeftButton
                                    .setEnabled(true);
                            RolesChooserDialog.this.bt_OKButton
                                    .setEnabled(true);
                        }
                        if (((DefaultListModel) RolesChooserDialog.this.listPossibleRoles
                                .getModel()).isEmpty())
                        {
                            RolesChooserDialog.this.bt_RightButton
                                    .setEnabled(false);
                        }
                        else
                            RolesChooserDialog.this.listPossibleRoles
                                    .setSelectedIndex(0);
                    }
                });
        this.bt_LeftButton
                .addActionListener(this.alLeftButton = new java.awt.event.ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        putRoleToLeftList();
                        if (RolesChooserDialog.this.bt_RightButton.isEnabled() == false
                                && !((DefaultListModel) RolesChooserDialog.this.listPossibleRoles
                                        .getModel()).isEmpty())
                        {
                            RolesChooserDialog.this.bt_RightButton
                                    .setEnabled(true);
                        }
                        if (((DefaultListModel) RolesChooserDialog.this.listChosenRoles
                                .getModel()).isEmpty())
                        {
                            RolesChooserDialog.this.bt_LeftButton
                                    .setEnabled(false);
                            RolesChooserDialog.this.bt_OKButton
                                    .setEnabled(false);
                        }
                        else
                            RolesChooserDialog.this.listChosenRoles
                                    .setSelectedIndex(0);
                    }
                });
        this.bt_OKButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                RolesChooserDialog.this.returnValue = APPROVE_OPTION;
                RolesChooserDialog.this.setVisible(false);
            }
        });
        this.bt_CancelButton
                .addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        RolesChooserDialog.this.returnValue = CANCEL_OPTION;
                        RolesChooserDialog.this.setVisible(false);
                    }
                });
        this.listPossibleRoles.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                if (RolesChooserDialog.this.alRightButton == null)
                    return;
                Object ob[] = RolesChooserDialog.this.listPossibleRoles
                        .getSelectedValues();
                if (ob.length > 1)
                    return;
                if (me.getClickCount() == 2)
                {
                    RolesChooserDialog.this.alRightButton
                            .actionPerformed(new ActionEvent(this,
                                    ActionEvent.ACTION_PERFORMED, ""));
                    me.consume();
                }
            }
        });
        this.listChosenRoles.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                if (RolesChooserDialog.this.alLeftButton == null)
                    return;
                Object ob[] = RolesChooserDialog.this.listChosenRoles
                        .getSelectedValues();
                if (ob.length > 1)
                    return;
                if (me.getClickCount() == 2)
                {
                    RolesChooserDialog.this.alLeftButton
                            .actionPerformed(new ActionEvent(this,
                                    ActionEvent.ACTION_PERFORMED, ""));
                    me.consume();
                }
            }
        });
        // si il n'y a pas d'elements dans les listes, on rend les boutons
        // inactifs
        if (((DefaultListModel) this.listPossibleRoles.getModel()).isEmpty())
        {
            this.bt_RightButton.setEnabled(false);
        }
        if (((DefaultListModel) this.listChosenRoles.getModel()).isEmpty())
        {
            this.bt_LeftButton.setEnabled(false);
            this.bt_OKButton.setEnabled(false);
        }
        // ajout d'un composant de taille fixe en haut de la fenetre
        this.getContentPane().add(Box.createVerticalStrut(10),
                BorderLayout.NORTH);
        // remplissage de la partie west de la fenetre (liste des roles
        // possibles)
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new BoxLayout(jpanel1, BoxLayout.X_AXIS));
        jpanel1.add(Box.createHorizontalStrut(10));
        JScrollPane scrolledList1 = new JScrollPane(this.listPossibleRoles);
        scrolledList1.setPreferredSize(new Dimension(160, 120));
        jpanel1.add(scrolledList1);
        jpanel1.add(Box.createHorizontalStrut(10));
        this.getContentPane().add(jpanel1, BorderLayout.WEST);
        // remplissage de la partie center de la fenetre (boutons pour naviguer
        // d'une liste à l'autre)
        JPanel jpanel2 = new JPanel();
        jpanel2.setLayout(new BoxLayout(jpanel2, BoxLayout.Y_AXIS));
        jpanel2.add(Box.createVerticalGlue());
        jpanel2.add(this.bt_RightButton);
        jpanel2.add(Box.createVerticalGlue());
        jpanel2.add(this.bt_LeftButton);
        jpanel2.add(Box.createVerticalGlue());
        this.getContentPane().add(jpanel2, BorderLayout.CENTER);
        // remplissage de la partie east de la fenetre (liste des roles choisis)
        JPanel jpanel3 = new JPanel();
        jpanel3.setLayout(new BoxLayout(jpanel3, BoxLayout.X_AXIS));
        JScrollPane scrolledList2 = new JScrollPane(this.listChosenRoles);
        scrolledList2.setPreferredSize(new Dimension(160, 120));
        jpanel3.add(Box.createHorizontalStrut(10));
        jpanel3.add(scrolledList2);
        jpanel3.add(Box.createHorizontalStrut(10));
        this.getContentPane().add(jpanel3, BorderLayout.EAST);
        // remplissage de la partie south de la fenetre (boutons valider et
        // annuler)
        JPanel jpanel4 = new JPanel();
        jpanel4.add(this.bt_OKButton);
        jpanel4.add(Box.createHorizontalStrut(15));
        jpanel4.add(this.bt_CancelButton);
        this.getContentPane().add(jpanel4, BorderLayout.SOUTH);
        // Selection de la premiere ligne par defaut, calcul de la taille et
        // mise en not resizable
        this.listPossibleRoles.setSelectedIndex(0);
        this.pack();
        this.setResizable(false);
        // centre la fenetre par rapport à la fenetre parentes
        this.setLocationRelativeTo(owner);
    }

    /**
     * Permet de transferer un role de la liste de gauche a la liste de droite
     */
    protected void putRoleToRightList()
    {
        // recuperation des indices des elements selectionnes
        int[] indexes = this.listPossibleRoles.getSelectedIndices();
        // pour chaque indice, on ajoute l'element dans la liste cible
        for (int indice : indexes)
        {
            ((DefaultListModel) this.listChosenRoles.getModel())
                    .addElement(this.listPossibleRoles.getModel().getElementAt(
                            indice));
        }
        // parcours de la liste des roles choisis pour les supprimer de la liste
        // des roles possibles
        for (int indice = 0 ; indice < ((DefaultListModel) this.listChosenRoles
                .getModel()).getSize() ; indice++)
        {
            ((DefaultListModel) this.listPossibleRoles.getModel())
                    .removeElement(((DefaultListModel) this.listChosenRoles
                            .getModel()).getElementAt(indice));
        }

    }

    /**
     * Permet de transferer un role de la liste de droite a la liste de gauche
     */
    protected void putRoleToLeftList()
    {
        // recuperation des indices des elements selectionnes
        int[] indexes = this.listChosenRoles.getSelectedIndices();
        // pour chaque indice, on ajoute l'element dans la liste cible
        for (int indice : indexes)
        {
            ((DefaultListModel) this.listPossibleRoles.getModel())
                    .addElement(this.listChosenRoles.getModel().getElementAt(
                            indice));

        }
        // parcours de la liste des roles possibles pour les supprimer de la
        // liste des roles choisis
        for (int indice = 0 ; indice < ((DefaultListModel) this.listPossibleRoles
                .getModel()).size() ; indice++)
        {
            ((DefaultListModel) this.listChosenRoles.getModel())
                    .removeElement(((DefaultListModel) this.listPossibleRoles
                            .getModel()).elementAt(indice));
        }
    }

    /**
     * Ouvre la fenetre de choix des roles
     * 
     * @return retourne l'etat de la fenetre de choix des
     *         roles(CANCEL_OPTION,APPROVE_OPTION,ERROR_OPTION)
     */
    public int showDialog()
    {
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                RolesChooserDialog.this.returnValue = CANCEL_OPTION;
            }
        });
        this.returnValue = ERROR_OPTION;
        this.setVisible(true);
        return this.returnValue;
    }

    /**
     * retourne les roles choisis
     * 
     * @return les roles choisis
     */
    public ArrayList<Role> getChosenRoles()
    {
        ArrayList<Role> chosenRoles = new ArrayList<Role>();
        // on parcourt la liste graphique et on rempli le tableau a retourner
        // avec les roles choisis
        for (int i = 0 ; i < ((DefaultListModel) this.listChosenRoles
                .getModel()).getSize() ; i++)
        {
            chosenRoles.add((Role) ((DefaultListModel) this.listChosenRoles
                    .getModel()).getElementAt(i));
        }
        return chosenRoles;
    }

    /**
     * Renderer des cellules des listes
     */
    class PagodListCellRenderer extends DefaultListCellRenderer
    {
        /**
         * Methode permettant de remplir le composant d'affichage avec le nom et
         * l'icone des roles
         * 
         * @param list
         *            la liste sur laquelle s'applique le renderer
         * @param value
         *            l'element de la liste sur lequel s'applique le renderer
         * @param index
         *            l'index de l'element dans la liste
         * @param isSelected
         *            determine si l'element est selectionne ou non
         * @param cellHasFocus
         *            determine si l'element a le focus ou non
         * @return le composant a afficher
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *      java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            // rempli le composant avec le libelle du role
            String s = value.toString();
            this.setText(s);
            // ajoute l'image du role au composant
            this.setIcon(ModelResourcesManager.getInstance().getSmallIcon(
                        ((Role) value)));
            // //gere le changement de couleur de l'item selectionne
            if (isSelected)
            {
                this.setBackground(list.getSelectionBackground());
                this.setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            return this;
        }
    }

}