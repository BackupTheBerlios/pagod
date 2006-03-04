/*
 * Projet PAGOD
 * 
 * $Id: StepOverviewFrame.java,v 1.10 2006/03/04 10:25:38 garwind111 Exp $
 */
package pagod.configurator.ui;


import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import pagod.common.control.ModelResourcesManager;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.utils.LanguagesManager;

/**
 * @author Arno
 *
 */
public class StepOverviewFrame extends JFrame
{	   

    private JLabel jLabel_name;
    private JLabel jLabel_content;
    private JLabel jLabel_products;
    private JScrollPane jScrollPane_content;
    private JScrollPane jScrollPane_products;
    private JEditorPane jEditorPane_name;
	private JEditorPane	jEditorPane_content;
	private JList JListProducts;
	private List<Product> productslist = null;
	private int coordw = 400;
	private int coordh = 480;
	
    
    /**
     * 
     * @param steptoshow
     */
    public StepOverviewFrame(Step steptoshow) {
        initComponents(steptoshow);
        this.setBounds(100,100,this.coordw,this.coordh);
        stepOverview(steptoshow);
    }
    
    private void stepOverview (Step steptoshow)
	{
		this.jEditorPane_name.setText(steptoshow.getName());
		this.jEditorPane_name.setCaretPosition(0);
		this.jEditorPane_content.setText(steptoshow.getComment());
		this.jEditorPane_content.setCaretPosition(0);
		this.productslist = steptoshow.getOutputProducts();
	}

    
    private class ListProductRenderer extends DefaultListCellRenderer
	{

		/**
		 * @param list
		 * @param value
		 * @param index
		 * @param isSelected
		 * @param cellHasFocus
		 * @return Composant ? afficher
		 * 
		 */
		public Component getListCellRendererComponent (JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus)
		{
			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			
			label.setIcon(ModelResourcesManager.getInstance().getSmallIcon(
					(Product) value));
			Font LabelFont = label.getFont();
			
			label.setFont(LabelFont.deriveFont(Font.TRUETYPE_FONT));
			
			//TODO : c est ici qu on rajoute le cell renderer pr la JList je pense
			if (value instanceof Product)
			{
				// TODO, qd on aura un process avec des produits à description
				// faudra supprimer l affichage du nom
				String sTTT = ((Product) value).getName();
				if (((Product) value).getDescription() != null)
				{
					sTTT += " : "+((Product) value).getDescription(); 
				}
				this.setToolTipText(sTTT);
			}

			return label;
		}
	}
    
	private void initComponents(Step steptoshow) {
    	this.jLabel_name = new javax.swing.JLabel();
    	this.jEditorPane_name = new javax.swing.JEditorPane("text/html", "");
    	this.jLabel_content = new javax.swing.JLabel();
    	this.jScrollPane_content = new javax.swing.JScrollPane();
    	this.jScrollPane_products = new javax.swing.JScrollPane();
    	this.jEditorPane_content = new javax.swing.JEditorPane("text/html", "");
    	this.jLabel_products = new javax.swing.JLabel();
    	
    	this.productslist = steptoshow.getOutputProducts();
    	
    	DefaultListModel model = new DefaultListModel();
    	for (Product p : this.productslist){
    		model.addElement(p);
    	}
    	this.JListProducts = new JList(model);
    	this.JListProducts.setCellRenderer(new ListProductRenderer());
    	this.JListProducts.setSelectedIndex(0);
    	/*
    	this.jScrollPane_products = new javax.swing.JScrollPane();
    	this.jEditorPane_products = new javax.swing.JEditorPane("text/html", "");*/
    	// this.JListProducts = new JList();
    	
        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(LanguagesManager.getInstance().getString("OverviewFrame_frameTitle"));
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        
        
        this.jLabel_name.setText(LanguagesManager.getInstance().getString("OverviewFrame_stepName"));
        getContentPane().add(this.jLabel_name);
        this.jLabel_name.setBounds(15, 5, 160, 14);
        
        this.jEditorPane_name.setEditable(true);
        getContentPane().add(this.jEditorPane_name);
        this.jEditorPane_name.setBounds(15, 25, 360, 25);
        this.jEditorPane_name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        
        
        this.jLabel_content.setText(LanguagesManager.getInstance().getString("OverviewFrame_stepContent"));
        getContentPane().add(this.jLabel_content);
        this.jLabel_content.setBounds(15, 60, 160, 14);

        this.jEditorPane_content.setEditable(true);
        this.jScrollPane_content.setViewportView(this.jEditorPane_content);
        getContentPane().add(this.jScrollPane_content);
        this.jScrollPane_content.setBounds(15, 80, 360, 230);

        if (productslist.size() > 1) this.jLabel_products
				.setText(LanguagesManager.getInstance().getString(
						"OverviewFrame_stepProducts"));
		else this.jLabel_products
		.setText(LanguagesManager.getInstance().getString(
				"OverviewFrame_stepProduct"));
			
			getContentPane().add(this.jLabel_products);
        this.jLabel_products.setBounds(15, 315, 160, 14);
        
        /*
        this.jEditorPane_products.setEditable(false);
        this.jScrollPane_products.setViewportView(this.jEditorPane_products);
        getContentPane().add(this.jScrollPane_products);
        this.jScrollPane_products.setBounds(15, 335, 360, 100);
        getContentPane().add(this.JListProducts);
        this.JListProducts.setBounds(15, 335, 360, 100);
        this.JListProducts.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));*/
        
        this.jScrollPane_products.setViewportView(this.JListProducts);
        getContentPane().add(this.jScrollPane_products);
        this.jScrollPane_products.setBounds(15, 335, 360, 100);
	}
    
}
