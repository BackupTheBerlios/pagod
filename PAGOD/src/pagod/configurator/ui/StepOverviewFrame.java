/*
 * Projet PAGOD
 * 
 * $Id: StepOverviewFrame.java,v 1.1 2006/02/24 07:57:40 garwind111 Exp $
 */
package pagod.configurator.ui;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

/**
 * @author Arno
 *
 */
public class StepOverviewFrame extends JFrame
{	   

    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
	private JEditorPane	jEditorPane2;
    
    
    /**
     * 
     */
    public StepOverviewFrame() {
        initComponents();
        this.setBounds(100,100,400,400);
    }
    
    private void initComponents() {
    	this.jLabel1 = new javax.swing.JLabel();
    	this.jTextField1 = new javax.swing.JTextField();
    	this.jLabel2 = new javax.swing.JLabel();
    	this.jScrollPane1 = new javax.swing.JScrollPane();
    	this.jEditorPane2 = new javax.swing.JEditorPane();
    	this.jLabel3 = new javax.swing.JLabel();
    	this.jScrollPane2 = new javax.swing.JScrollPane();
    	this.jEditorPane1 = new javax.swing.JEditorPane();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Overview");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        this.jLabel1.setText("jLabel1");
        getContentPane().add(this.jLabel1);
        this.jLabel1.setBounds(15, 10, 160, 14);

        this.jTextField1.setEditable(false);
        this.jTextField1.setText("jTextField1");
        getContentPane().add(this.jTextField1);
        this.jTextField1.setBounds(15, 30, 360, 19);

        this.jLabel2.setText("jLabel2");
        getContentPane().add(this.jLabel2);
        this.jLabel2.setBounds(15, 60, 160, 14);

        this.jEditorPane2.setEditable(false);
        this.jScrollPane1.setViewportView(this.jEditorPane2);

        getContentPane().add(this.jScrollPane1);
        this.jScrollPane1.setBounds(15, 80, 360, 100);

        this.jLabel3.setText("jLabel3");
        getContentPane().add(this.jLabel3);
        this.jLabel3.setBounds(15, 180, 160, 14);

        this.jEditorPane1.setEditable(false);
        this.jScrollPane2.setViewportView(this.jEditorPane1);

        getContentPane().add(this.jScrollPane2);
        this.jScrollPane2.setBounds(15, 200, 360, 150);

    }
    
}
