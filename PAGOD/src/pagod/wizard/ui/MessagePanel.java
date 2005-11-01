/*
 * $Id: MessagePanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;


import pagod.utils.ImagesManager;

/**
 * Panneaux d'affichage de message
 * 
 * @author MoOky
 */
public class MessagePanel extends JPanel
{
    /**
     * Message
     */
    private JEditorPane messageArea;

    /**
     * Avatar de PAGOD
     */
    private JLabel avatar;

    /**
     * Constructeur du Panneaux d'affichage de message
     * 
     */
    public MessagePanel()
    {
        super();
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        // mise en forme du l'avatar
        Icon avatarIcon = ImagesManager.getInstance().getIcon("avatar.gif");
        this.avatar = new JLabel("", avatarIcon, JLabel.LEFT);
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.add(this.avatar,BorderLayout.SOUTH);
        this.add(iconPanel, BorderLayout.WEST);
        // mise en forme du message
        this.messageArea = new JEditorPane();
        this.messageArea.setEditable(false);
        this.messageArea.setOpaque(false);
        this.messageArea.setContentType("text/html");
        this.messageArea.setFocusable(false);
        // panneaux utilis� pour 
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.setBorder(new BalloonBorder());
        messagePanel.add(this.messageArea);
        messagePanel.add(this.messageArea);
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.setOpaque(false);
        layoutPanel.add(messagePanel, BorderLayout.CENTER);
        layoutPanel.add(Box.createVerticalStrut(avatarIcon.getIconHeight()/2),BorderLayout.SOUTH);
        this.add(layoutPanel, BorderLayout.CENTER);
        //mise en page
        int borderSize = 7;
        // ajout d'une glue pour laisser aerer au dessus
        this.add(Box.createVerticalStrut(borderSize), BorderLayout.NORTH);
        // ajout d'une glue pour laisser aerer sur le cot�
        this.add(Box.createHorizontalStrut(borderSize), BorderLayout.EAST);
        // ajout d'une glue pour laisser aerer en dessous
        this.add(Box.createVerticalStrut(borderSize), BorderLayout.SOUTH);
    }

    /**
     * Modifie le texte du message panel
     * 
     * @param message
     */
    public void setMessage(String message)
    {

        this.messageArea.setText("<center>"+message+"</center>");
        // this.messageLabel.setHorizontalTextPosition(JLabel.CENTER);
    }

    private class BalloonBorder extends MatteBorder
    {
         private final Color borderColor = Color.BLACK;
         private final Color fillColor = new Color(244, 229, 176); 
         private final static int arrowHeight = 12 ;
         private final static int arrowWidth = 12 ;
         private final static int borderSize = 3;
        
         
         /**
         * Constructeur
         */
        public BalloonBorder()
        {
            super(borderSize, borderSize, borderSize+arrowHeight, borderSize, Color.BLACK);
        }

        /**
         * @param c
         * @param g
         * @param x
         * @param y
         * @param width
         * @param height
         */
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height)
        {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            int round = 20;
            int arrowX = width/100+round/2;            
            // dessin de la fleche
            g2.setColor(this.borderColor);
            Polygon arrow = new Polygon();
            arrow.addPoint(arrowX,height-arrowHeight);
            arrow.addPoint(arrowX,height);
            arrow.addPoint(arrowX+arrowWidth,height-arrowHeight);
            g2.drawPolygon(arrow);
            
            //dessin de la bulle
            g2.setColor(this.borderColor);     
            g2.drawRoundRect(x, y, width - 1, height - arrowHeight+1, round, round);
            g2.setColor(this.fillColor);
            g2.fillRoundRect(x+1, y+1, width - 2, height - arrowHeight, round, round);
            
            // colore la fleche
            g2.setColor(this.fillColor);
            arrow = new Polygon();
            arrow.addPoint(arrowX+1,height-arrowHeight);
            arrow.addPoint(arrowX+1,height-1);
            arrow.addPoint(arrowX+arrowWidth,height-arrowHeight);
            g2.fillPolygon(arrow);

        }
    }
}