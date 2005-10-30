/*
 * $Id: StepPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.Color;

import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pagod.common.model.Step;
import pagod.utils.LanguagesManager;

/**
 * @author MoOky
 * 
 */
public class StepPanel extends JScrollPane
{
    /***************************************************************************
     * Label de présentation de l'étape
     */
    private JLabel header;

    /**
     * Contenu de l'étape
     */
    private JEditorPane body;

    /**
     * @param stepToPresent
     * @param rang
     * @param total
     */
    public StepPanel(Step stepToPresent, int rang, int total)
    {

        super();
        // panneaux global nécessaire pour des contrainte de présentation
        JPanel globalPane = new JPanel();
        globalPane.setLayout(new BorderLayout());
        globalPane.setBackground(Color.WHITE);
        int globalPaneBorderSize = 7;
        globalPane.add(Box.createVerticalStrut(globalPaneBorderSize),
                BorderLayout.NORTH);
        globalPane.add(Box.createHorizontalStrut(globalPaneBorderSize),
                BorderLayout.EAST);
        globalPane.add(Box.createHorizontalStrut(globalPaneBorderSize),
                BorderLayout.WEST);
        // panneaux interne
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BorderLayout());
        innerPane.setOpaque(false);
        int innerPaneBorderSize = 7;
        innerPane.add(Box.createHorizontalStrut(innerPaneBorderSize),
                BorderLayout.EAST);
        innerPane.add(Box.createHorizontalStrut(innerPaneBorderSize),
                BorderLayout.WEST);
        globalPane.add(innerPane, BorderLayout.CENTER);
        // entete du descriptif de l'étape
        String title = "<html><u>"
            + LanguagesManager.getInstance().getString(
                    "stepPresentation");
        if (stepToPresent.getName() != null)
        {
            String name = stepToPresent.getName();
            title += " " + name ;
        }
        title+= " (" + rang+ "/" + total + ") : </u></html>";
        this.header = new JLabel(title);
        innerPane.add(this.header, BorderLayout.NORTH);
    
        // corps du descriptif de l'étape
        this.body = new JEditorPane();
        this.body.setEditable(false);
        this.body.setOpaque(false);
        this.body.setContentType("text/html");
        String message;
        if (stepToPresent.getComment() == null)
        {
            message = LanguagesManager.getInstance().getString("noComment");

        }
        else
        {
            message = stepToPresent.getComment();
        }
        this.body.setText(message);
        innerPane.add(this.body, BorderLayout.CENTER);
        // ajout du globalpane dans le viewportview
        this.setViewportView(globalPane);
    }
}
