/*
 * $Id: CheckPane.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Activity;
import pagod.common.model.Guidance;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Tool;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * @author pierrot Panneau de controle d'entree dans une activite
 */
public class CheckPane extends JScrollPane
{
    private Activity activity;

    /**
     * Constructeur du paneau de controle d'entree en activite
     * 
     * @param activity
     *            l'activite a controler
     */
    public CheckPane(Activity activity)
    {
        super();
        this.activity = activity;

        // recuperation de la taille de la fenetre
        Rectangle screenSize = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int iWidth = screenSize.width;
        // ImagesManager imagesManager = ImagesManager.getInstance();
        JPanel pCenterPanel = new JPanel();
        pCenterPanel
                .setLayout(new BoxLayout(pCenterPanel, BoxLayout.PAGE_AXIS));

        // Ajout du role
        Role r = null;
        r = this.activity.getRole();
        String role = new String("");
        role = r.getName();
        JPanel pRoleTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lRoleTitleLabel = new JLabel("");
        lRoleTitleLabel = new JLabel("["
                + LanguagesManager.getInstance().getString("CheckRoleLabel")
                + role + "]");
        lRoleTitleLabel.setBackground(Color.WHITE);
        pRoleTitlePanel.setBackground(Color.WHITE);

        pRoleTitlePanel.add(lRoleTitleLabel);
        Dimension roleTitleLabelSize = lRoleTitleLabel.getPreferredSize();
        pRoleTitlePanel.setMaximumSize(new Dimension(iWidth,
                roleTitleLabelSize.height));
        pCenterPanel.add(pRoleTitlePanel);
        pCenterPanel.add(Box.createVerticalStrut(10));

        // section produits
        if (this.activity.hasInputProducts())
        {
            // mise en place de la phrase de titre
            JPanel pProductTitlePanel = new JPanel(new FlowLayout(
                    FlowLayout.LEFT));
            JLabel lProductsTitleLabel = new JLabel("");
            lProductsTitleLabel = new JLabel(LanguagesManager.getInstance()
                    .getString("CheckProductsLabel"), SwingConstants.LEFT);
            lProductsTitleLabel.setBackground(Color.WHITE);
            pProductTitlePanel.setBackground(Color.WHITE);
            pProductTitlePanel.add(lProductsTitleLabel);
            Dimension productTitleLabelSize = lProductsTitleLabel
                    .getPreferredSize();
            pProductTitlePanel.setMaximumSize(new Dimension(iWidth,
                    productTitleLabelSize.height));
            pCenterPanel.add(pProductTitlePanel);
            pCenterPanel.add(Box.createVerticalStrut(10));
            // mise en place des produits (1 ligne par produit)
            for (Product product : this.activity.getInputProducts())
            {
                JPanel pProduct = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pProduct.add(Box.createHorizontalStrut(15));
                JLabel lProductLabel = new JLabel(product.getName(),
                        SwingConstants.LEFT);
                // les lignes suivantes permettent l'affichage de l'icone avec
                // le libelle
                lProductLabel.setIcon(ModelResourcesManager.getInstance()
                        .getSmallIcon(product));
                lProductLabel.setBackground(Color.WHITE);
                Font fontProduct = pProductTitlePanel.getFont();
                lProductLabel.setFont(fontProduct.deriveFont(Font.ITALIC));
                pProduct.setBackground(Color.WHITE);
                pProduct.add(lProductLabel);
                Dimension productLabelSize = lProductLabel.getPreferredSize();
                pProduct.setMaximumSize(new Dimension(iWidth,
                        productLabelSize.height));
                pCenterPanel.add(pProduct);
            }
            pCenterPanel.add(Box.createVerticalStrut(10));
            // section outils
            if (activity.needsTools())
            {
                // mise en place de la phrase de titre
                JPanel pToolTitlePanel = new JPanel(new FlowLayout(
                        FlowLayout.LEFT));
                JLabel lToolsTitleLabel = new JLabel("");
                lToolsTitleLabel = new JLabel(LanguagesManager.getInstance()
                        .getString("CheckToolsLabel"), SwingConstants.LEFT);
                lToolsTitleLabel.setBackground(Color.WHITE);
                pToolTitlePanel.setBackground(Color.WHITE);
                pToolTitlePanel.add(lToolsTitleLabel);
                Dimension toolTitleLabelSize = lToolsTitleLabel
                        .getPreferredSize();
                pToolTitlePanel.setMaximumSize(new Dimension(iWidth,
                        toolTitleLabelSize.height));
                pCenterPanel.add(pToolTitlePanel);
                pCenterPanel.add(Box.createVerticalStrut(10));
                // mise en place des outils (1 ligne par produit)
                HashSet<Tool> hashSetOutilsDejaAjoutes = new HashSet<Tool>();
                for (Product product : this.activity.getOutputProducts())
                {

                    if (product.getEditor() != null)
                    {
                        if (!hashSetOutilsDejaAjoutes.contains(product
                                .getEditor()))
                        {
                            hashSetOutilsDejaAjoutes.add(product.getEditor());
                            JPanel pTool = new JPanel(new FlowLayout(
                                    FlowLayout.LEFT));
                            pTool.add(Box.createHorizontalStrut(15));
                            JLabel lToolLabel;
                            try
                            {
                                lToolLabel = new JLabel(product.getEditor()
                                        .getName(), ImagesManager.getInstance()
                                        .getSmallIcon("ToolIcon.gif"),
                                        SwingConstants.LEFT);
                            }
                            catch (Exception e2)
                            {
                                // initialiser le label mais sans l'ic?ne
                                lToolLabel = new JLabel(product.getEditor()
                                        .getName(), SwingConstants.LEFT);
                            }
                            lToolLabel.setBackground(Color.WHITE);
                            Font fontTool = pProductTitlePanel.getFont();
                            lToolLabel
                                    .setFont(fontTool.deriveFont(Font.ITALIC));
                            pTool.add(lToolLabel);
                            pTool.setBackground(Color.WHITE);
                            Dimension toolLabelSize = lToolLabel
                                    .getPreferredSize();
                            pTool.setMaximumSize(new Dimension(iWidth,
                                    toolLabelSize.height));
                            pCenterPanel.add(pTool);
                        }
                    }

                }
            }
        }
        pCenterPanel.add(Box.createVerticalStrut(10));
        // section guides
        if (activity.getGuidances().size() != 0)
        {
            // mise en place de la phrase de titre
            JPanel pGuidanceTitlePanel = new JPanel(new FlowLayout(
                    FlowLayout.LEFT));
            JLabel lGuidancesTitleLabel = new JLabel("");
            lGuidancesTitleLabel = new JLabel(LanguagesManager.getInstance()
                    .getString("CheckGuidancesLabel"), SwingConstants.LEFT);
            lGuidancesTitleLabel.setBackground(Color.WHITE);
            pGuidanceTitlePanel.setBackground(Color.WHITE);
            pGuidanceTitlePanel.add(lGuidancesTitleLabel);
            Dimension toolTitleLabelSize = lGuidancesTitleLabel
                    .getPreferredSize();
            pGuidanceTitlePanel.setMaximumSize(new Dimension(iWidth,
                    toolTitleLabelSize.height));
            pCenterPanel.add(pGuidanceTitlePanel);
            pCenterPanel.add(Box.createVerticalStrut(10));
            // mise en place des guides (1 ligne par produit)
            for (Guidance guidance : this.activity.getGuidances())
            {

                JPanel pGuidance = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pGuidance.add(Box.createHorizontalStrut(15));
                JLabel lGuidanceLabel;
                try
                {
                    lGuidanceLabel = new JLabel(guidance.getName(),
                            ModelResourcesManager.getInstance().getSmallIcon(
                                    guidance), SwingConstants.LEFT);
                    lGuidanceLabel.addMouseListener( new MouseAdapter()
                    {
                       public void mouseEntered(MouseEvent e)
                       {
                          
                          JLabel ltemp;
                           ltemp =  (JLabel)e.getSource();
                           ltemp.setForeground(Color.blue);
                       } 
                       public void mouseExited(MouseEvent e)
                       {
                            JLabel ltemp;
                            ltemp =  (JLabel)e.getSource();
                            ltemp.setForeground(Color.black);
                       }
                    
                    });
                    lGuidanceLabel.addMouseListener(new MouseAdapter()
                    {

                        public void mouseClicked(MouseEvent e)
                        {
                            if (e.getClickCount() == 2)
                            {
                                JLabel jLabelSource = (JLabel) e.getSource();
                                List<Guidance> lguidance = CheckPane.this.activity.getGuidances();
                                Guidance guidance1;
                                Iterator it  = lguidance.iterator();
                                while (it.hasNext())
                                {    
                                    guidance1 = (Guidance)it.next();
                                    if (jLabelSource.getText() == guidance1.getName())
                                    {    ModelResourcesManager
                                        .getInstance()
                                        .launchContentFile(guidance1);
                                    }
                                }
                             }
                         }          
                      });
                }
                catch (Exception e2)
                {
                    // initialiser le label mais sans l'ic?ne
                    lGuidanceLabel = new JLabel(guidance.getName(),
                            SwingConstants.LEFT);
                }
                lGuidanceLabel.setBackground(Color.WHITE);
                Font fontTool = pGuidanceTitlePanel.getFont();
                lGuidanceLabel.setFont(fontTool.deriveFont(Font.ITALIC));
                pGuidance.add(lGuidanceLabel);
                pGuidance.setBackground(Color.WHITE);
                Dimension toolLabelSize = lGuidanceLabel.getPreferredSize();
                pGuidance.setMaximumSize(new Dimension(iWidth,
                        toolLabelSize.height));
                pCenterPanel.add(pGuidance);

            }
        }
        this.setBackground(Color.WHITE);
        pCenterPanel.setBackground(Color.WHITE);
        pCenterPanel.add(Box.createVerticalGlue());
        this.add(pCenterPanel);
        this.setViewportView(pCenterPanel);

    }
}
