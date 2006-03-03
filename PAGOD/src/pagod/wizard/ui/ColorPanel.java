package pagod.wizard.ui;

/*
 * Projet PAGOD
 * 
 * $Id: ColorPanel.java,v 1.1 2006/03/03 13:48:20 coincoin Exp $
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.color.*;
import java.io.File;
import java.util.MissingResourceException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.JTextComponent;

import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;
import sun.io.Converters;

/**
 * 
 * @author Coin coin
 *
 */
public class ColorPanel extends JPanel
{	
	
	//Deux panels permettant de changer les couleurs:
	//Action Terminée
	private JPanel North = new JPanel();
	
	//Action Suspendue
	private JPanel Center = new JPanel();
	
	// Textes
    private JLabel lColorAF = new JLabel(LanguagesManager.getInstance().getString("colorAFMessage"));
    private JLabel lColorAS = new JLabel(LanguagesManager.getInstance().getString("colorASMessage"));
    
    // Boutons permettant d'acceder a un JColorChooser
    private JButton bChangeColorAF = new JButton(LanguagesManager.getInstance().getString("changeColorButton"));
    private JButton bChangeColorAS = new JButton(LanguagesManager.getInstance().getString("changeColorButton"));
    
    private Color ccolorAF = null;
    private Color ccolorAS = null;
    private Color ccolorTemp = null;
    
/**
 * Constructeur du panel
 * @param parentFrame
 * @throws MissingResourceException
 * @throws NotInitializedException
 */
	public ColorPanel(JFrame parentFrame) throws MissingResourceException,
    NotInitializedException
    {
		super();
		
		//Definition du Layout
		this.setLayout(new BorderLayout());
		
		//Chargement des couleurs prédéfinies
		
		//if (PreferencesManager.getInstance().getColorAF() != null){
		this.setColorAF(PreferencesManager.getInstance().getColorAF());
		//}
		
		//if (PreferencesManager.getInstance().getColorAS() != null){
		this.setColorAS(PreferencesManager.getInstance().getColorAS());
		//}
		
		//Couleur des Activités Terminés
		lColorAF.setForeground(ccolorAF);
		
		
		//Panel North
		//Ajout du Label
		this.North.add(lColorAF);
		
		//Ajout du Listener sur le Bouton
		this.bChangeColorAF.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
        	
        	JColorChooser colorChooser = new JColorChooser();
        	
        	//Affichage du JColorChooser
        	ccolorTemp = colorChooser.showDialog(ApplicationManager.getInstance().getMfPagod(),LanguagesManager.getInstance().getString("AFJColorChooserTitle"),ccolorAF);
        	
        	//Test pour vérifier que l'utilisateur a bien cliqué sur OK et non ANNULER
        	if (ccolorTemp != null){
        		
        		//Prise en compte de la modification
        		ccolorAF = ccolorTemp;
        		
        	}
        	
        	//Mise a jour de l'affichage de la couleur des actions suspendues
        	lColorAF.setForeground(ccolorAF);
        	}
        		
    		
		});	
    	
		
		//Ajout du Bouton
		this.North.add(this.bChangeColorAF);
		
		
		//Panel Center
		//Couleur des Activités Terminés
		lColorAS.setForeground(ccolorAS);
		
		//Ajout du Label
		this.Center.add(lColorAS);
		
		//Ajout du Listener sur le Bouton
		this.bChangeColorAS.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
        		
        	JColorChooser colorChooser = new JColorChooser();
        	
        	//Affichage du JColorChooser	
        	ccolorTemp = colorChooser.showDialog(ApplicationManager.getInstance().getMfPagod(),LanguagesManager.getInstance().getString("AFJColorChooserTitle"),ccolorAS);
        	
        	//Test pour vérifier que l'utilisateur a bien cliqué sur OK et non ANNULER
        	if (ccolorTemp != null){
        		
        		//Prise en compte de la modification
        		ccolorAS = ccolorTemp;
        		
        	}
        	
        	//Mise a jour de l'affichage de la couleur des actions suspendues
        	lColorAS.setForeground(ccolorAS);
        	}
        		
    		
		});	
		
		//Ajout du Bouton
		this.Center.add(bChangeColorAS);
		
		
		//Ajout des deux Panels dans le Panel Principal
		add(this.North, BorderLayout.NORTH);
		add(this.Center, BorderLayout.CENTER);
		
		
    }
	
	/**
	 * Méthode permettant de récupérer la couleur attribuée aux actions finies
	 * @return La couleur des actions finies
	 */
	public String getColorAF(){
		
		return this.getColor(this.ccolorAF);
		
	}
	
	/**
	 * Méthode permettant de récupérer la couleur attribuée aux actions suspendues
	 * @return La couleur des actions suspendues
	 */
	public String getColorAS(){
		
		return this.getColor(this.ccolorAS);
		
	}
	
	/**
	 * Permet de recuperer sous forme de String le code de la couleur passee en parametre.
	 * @param couleur : la couleur sur laquelle on veut effectuer le traitement
	 * @return un chaine representant le code RGB de la couleur
	 */
	public String getColor(Color ccouleur){
		
		//Si la couleur est initialisee
		if (ccouleur != null)
		{
		String stemp;
		int itemp;
		
		//Recuperation du code RGB de la couleur
		itemp = ccouleur.getRGB();
		//Conversion de l'int en string 
		stemp = String.valueOf(itemp);
		
		return stemp;
		} 
		return null;
	}
	
	/**
	 * Méthode permettant de paramétrer la couleur des actions finies
	 * @param couleur
	 */
	public void setColorAF(Color couleur){
		
		this.ccolorAF = couleur;
	}
	
	/**
	 * Méthode permettant de paramétrer la couleur des actions suspendues
	 * @param couleur
	 */
	public void setColorAS(Color couleur){

		this.ccolorAS = couleur;
		
	}
	
}
