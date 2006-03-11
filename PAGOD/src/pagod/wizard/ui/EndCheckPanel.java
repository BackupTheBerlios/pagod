/*
 * Projet PAGOD
 * 
 * $Id: EndCheckPanel.java,v 1.9 2006/03/11 16:44:12 cyberal82 Exp $
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
import java.util.ArrayList;
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
import pagod.utils.LanguagesManager;

/**
 * @author yak
 *
 */
public class EndCheckPanel extends JScrollPane
{
    private Activity activity;
    
    /**
     * attribut contenant tous les guides associes a un role et/ou une activite qui ne sont pas de type "liste de controle"
     */
    private List<Guidance> lGuidance = new ArrayList<Guidance>();

    /**
     * Constructeur du paneau de controle de sortie d'une activite
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
            
            // pour que le label soit au singulier ou au pluriel selon les cas
            String sProductTitleLabel = "";
            if (this.activity.getOutputProducts().size() == 1)
            	sProductTitleLabel = LanguagesManager.getInstance()
                .getString("CheckOutputProductLabel");
            else
            	sProductTitleLabel = LanguagesManager.getInstance()
                .getString("CheckOutputProductsLabel");
            
            
            JLabel lProductsTitleLabel = new JLabel(sProductTitleLabel, SwingConstants.LEFT);
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
                lProductLabel.setFont(fontProduct.deriveFont(Font.PLAIN));
                pProduct.setBackground(Color.WHITE);
                pProduct.add(lProductLabel);
                String toolTipTempText = product.getName();
                if (product.getDescription() != null)
                	toolTipTempText = toolTipTempText + " : "+ product.getDescription();
                lProductLabel.setToolTipText(toolTipTempText);
                
                lProductLabel.addMouseListener( new MouseAdapter()
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
                
                
                Dimension productLabelSize = lProductLabel.getPreferredSize();
                pProduct.setMaximumSize(new Dimension(iWidth,
                        productLabelSize.height));
                pCenterPanel.add(pProduct);
            }
            //pCenterPanel.add(Box.createVerticalStrut(10));
           
        }
        //pCenterPanel.add(Box.createVerticalStrut(10));

        // pCenterPanel.add(Box.createVerticalStrut(10));
        
        // section guides
        this.lGuidance.addAll(activity.getGuidanceType("Liste de controles"));
        this.lGuidance.addAll(activity.getRole().getGuidanceType("Liste de controles"));
        this.lGuidance.addAll(activity.getOutputProductGuidaceType("Liste de controles"));
        
        if (this.lGuidance.size() != 0)
        {
            // mise en place de la phrase de titre
            JPanel pGuidanceTitlePanel = new JPanel(new FlowLayout(
                    FlowLayout.LEFT));
            
            // pour que le label soit au singulier ou au pluriel selon les cas
            String sGuidancesTitleLabel;
            if (this.lGuidance.size() == 1)
            	sGuidancesTitleLabel = LanguagesManager.getInstance()
                .getString("CheckListLabel");
            else
            	sGuidancesTitleLabel = LanguagesManager.getInstance()
                .getString("CheckListsLabel");
            
            JLabel lGuidancesTitleLabel = new JLabel(sGuidancesTitleLabel, SwingConstants.LEFT);
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
            for (Guidance guidance : this.lGuidance)
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
                                Guidance guidance1;
                                Iterator it  = EndCheckPanel.this.lGuidance.iterator();
                                boolean trouve = false;
                                while (it.hasNext() && !trouve)
                                {    
                                    guidance1 = (Guidance)it.next();
                                    if (jLabelSource.getText() == guidance1.getName())
                                    {    
                                    	trouve = true;
                                    	ModelResourcesManager
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
                lGuidanceLabel.setFont(fontTool.deriveFont(Font.PLAIN));
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
