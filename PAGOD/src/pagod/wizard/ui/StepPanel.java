/*
 * $Id: StepPanel.java,v 1.4 2006/01/23 21:51:30 psyko Exp $
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
import java.awt.Dimension;

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
     * Label de pr�sentation de l'�tape
     */
    private JLabel header;

    /**
     * Contenu de l'�tape
     */
    private JEditorPane body;

    /**
     * @param stepToPresent
     * @param rang
     * @param total
     */
    public StepPanel(Step stepToPresent, int rang, int total)
    {

        super(  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // panneaux global n�cessaire pour des contrainte de pr�sentation
               
        // panneaux interne
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BorderLayout());
        innerPane.setBackground(Color.WHITE);
        innerPane.setOpaque(true);
        int innerPaneBorderSize = 7;
        innerPane.add(Box.createHorizontalStrut(innerPaneBorderSize),
                BorderLayout.EAST);
        innerPane.add(Box.createHorizontalStrut(innerPaneBorderSize),
                BorderLayout.WEST);
        
        innerPane.setPreferredSize(new Dimension(this.getWidth(), 1500));
        this.setViewportView(innerPane);
        this.viewport.setPreferredSize(this.getSize());
        
        // entete du descriptif de l'�tape
        String title = "<html><u>"
            + LanguagesManager.getInstance().getString("stepPresentation");
        if (stepToPresent.getName() != null)
        {
            String name = stepToPresent.getName();
            title += " " + name ;
        }
        title+= " (" + rang+ "/" + total + ") : </u></html>";
        this.header = new JLabel(title);
        innerPane.add(this.header, BorderLayout.NORTH);
    
        // corps du descriptif de l'�tape
        this.body = new JEditorPane();
        this.body.setEditable(false);
        this.body.setOpaque(true);
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
        
        this.body.setBackground(Color.WHITE);
        this.body.setText(message);
        innerPane.add(this.body);
        
        //JScrollPane jsBodyStep = new JScrollPane(innerPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //innerPane.add(jsBodyStep, BorderLayout.CENTER);
        //this.viewport.setLocation(this.header.getLocation());
    }
}
