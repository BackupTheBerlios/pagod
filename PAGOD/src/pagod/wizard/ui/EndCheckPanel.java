/*
 * Projet PAGOD
 * 
 * $Id: EndCheckPanel.java,v 1.1 2005/11/08 17:33:01 yak Exp $
 */
package pagod.wizard.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.utils.LanguagesManager;

/**
 * @author yak
 *
 */
public class EndCheckPanel extends JScrollPane
{
    private Activity activity;

    /**
     * Constructeur du paneau de controle d'entree en activite
     * 
     * @param activity
     *            l'activite a controler
     */
    public EndCheckPanel(Activity activity)
    {
        super();
        this.activity = activity;

        // recuperation de la taille de la fenetre
        Rectangle screenSize = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int iWidth = screenSize.width;
       
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
        if (this.activity.hasOutputProducts())
        {
            // mise en place de la phrase de titre
            JPanel pProductTitlePanel = new JPanel(new FlowLayout(
                    FlowLayout.LEFT));
            JLabel lProductsTitleLabel = new JLabel("");
            //TODO changer le label
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
            for (Product product : this.activity.getOutputProducts())
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
           
        }
        pCenterPanel.add(Box.createVerticalStrut(10));
       
        this.setBackground(Color.WHITE);
        pCenterPanel.setBackground(Color.WHITE);
        pCenterPanel.add(Box.createVerticalGlue());
        this.add(pCenterPanel);
        this.setViewportView(pCenterPanel);

    }
}
