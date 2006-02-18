 /*
 * $Id: LanguageChooserPanel.java,v 1.4 2006/02/18 16:35:53 cyberal82 Exp $
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

import java.awt.Component;
import java.awt.FlowLayout;
/*Modif Coin coin*/
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*Fin Modif Coin coin*/
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
/*Modif Coin coin*/
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
/*Fin Modif Coin coin*/

import pagod.common.ui.WorkspaceFileChooser;
import pagod.utils.FilesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.PreferencesManager;


/**
 * 
 * 
 * @author Alexandre Bes
 */
public class LanguageChooserPanel extends JPanel
{

    /**
     * Atribut contenant les langues disponibles
     */
    private ArrayList<String> languages = null;

    private JComboBox cbLanguages = null;
    
    /*Modif Coin coin*/
    private JLabel lChangeWorkspace = new JLabel("Chemin d'acces au Workspace :");
    private JTextField tfChangeWorkspace = new JTextField(
    		PreferencesManager.getInstance().getWorkspace());
    private JButton bChangeWorkspace = new JButton(
    	LanguagesManager.getInstance().getString("BrowseButtonLabel"));
    
    //private JFileChooser fcChangeWorkspace = new JFileChooser();
    /*Fin Modif Coin coin*/
    
    /* Modif Coin coin*/
    JPanel pLangage = new JPanel();
    JPanel pWorkspace = new JPanel();
    /*Fin Modif Coin coin*/

    
    
    /**
     * Constructeur de la classe LanguageChooserPanel
     */
    public LanguageChooserPanel()
    {
        super();

        this.setLayout(new BorderLayout());
        
        this.languages = new ArrayList<String>();
        this.initLanguages();

        // panneau contenant le label de langue et le comboBox
        JPanel pCenter = new JPanel();
               
        
        /*Modif Coin coin*/
        //pCenter.setLayout(new FlowLayout());
        
        // Mise en forme du panel pCenter
        pCenter.setLayout(new BorderLayout());
        
        //Mise en forme des panels inclus dans pCenter 
        
        this.pLangage.setLayout(new FlowLayout());
        this.pWorkspace.setLayout(new FlowLayout());
        
        
        /*Fin Modif Coin coin*/
        
        JLabel lLangue = new JLabel(LanguagesManager.getInstance().getString(
                "langueMessage"));

        this.cbLanguages = new JComboBox(this.languages.toArray());

        // on selectionne la langue defini dans les preferences
        this.cbLanguages.setSelectedItem(PreferencesManager.getInstance()
                .getLanguage());
        this.cbLanguages.setEditable(false);

        /*
         * on change le renderer pour afficher la langue et non pas les
         * initiales de la locale (on affiche Francais au lieu de fr)
         */
        this.cbLanguages.setRenderer(new DefaultListCellRenderer()
        {
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus)
            {
                Locale l = new Locale(value.toString());
                return super.getListCellRendererComponent(list, l
                        .getDisplayLanguage(), index, isSelected, cellHasFocus);
            }
        });

        /*Modif Coin coin*/
        //add(lLangue);
        //add(this.cbLanguages);
        
        this.pLangage.add(lLangue);
        this.pLangage.add(this.cbLanguages);
        
        this.pWorkspace.add(this.lChangeWorkspace);
        this.pWorkspace.add(this.tfChangeWorkspace);
        this.pWorkspace.add(this.bChangeWorkspace);
        

        this.bChangeWorkspace.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
    		WorkspaceFileChooser workspaceChooser = new WorkspaceFileChooser();

    		if (workspaceChooser.showOpenDialog(ApplicationManager.getInstance().getMfPagod()) == JFileChooser.APPROVE_OPTION)
    		{
    			File file = workspaceChooser.getSelectedFile();
    			LanguageChooserPanel.this.tfChangeWorkspace.setText(file.getPath());
    			
    			// mettre le path dans le fichier preferences a la cl?
    			// "workspace"
    			//PreferencesManager.getInstance().setWorkspace(file.getPath());
    		}
    	}});
        
        //this.bChangeWorkspace.addActionListener(new ListenerBoutons());
        
        //this.fcChangeWorkspace.setFileSelectionMode(DIRECTORIES_ONLY);
        //this.pWorkspace.add(this.fcChangeWorkspace);
        
        add(this.pLangage, BorderLayout.NORTH);
        add(this.pWorkspace, BorderLayout.CENTER);
        
        
        /*Fin Modif Coin coin*/
        
        
    }
    	
    	/*Modif Coin coin*/
   
    	/*fin Modif Coin coin*/
    

    /**
     * Methode prive qui initialise les langues disponibles dans le combo en
     * fonction des fichiers de langues present dans le repertoire prevu a cet
     * effet
     */
    private void initLanguages()
    {

        // construction du chemin d'acces au repertoire contenant les fichiers
        // de langues
        String sPath = FilesManager.getInstance().getRootPath()+Constants.LANGUAGES_OUTPUT_DIRECTORY;

        File directory = new File(sPath);

        File[] tabFiles = directory.listFiles();

        // on recupere le prefixe des fichiers de langues
        String sPrefixe = Constants.LANGUAGES_FILE
                .substring(Constants.LANGUAGES_FILE.lastIndexOf("/") + 1);
        sPrefixe += "_";

        /*
         * pour chaque fichier du repertoire contenant les fichiers de langue on
         * recupere la locale et on l'ajoute a l'attribut languages
         */
        for (int i = 0 ; i < tabFiles.length ; i++)
        {
            if (tabFiles[i].getName().startsWith(sPrefixe)
                    && tabFiles[i].getName().endsWith(".properties"))
            {
                this.languages.add(tabFiles[i].getName().substring(
                        sPrefixe.length(),
                        tabFiles[i].getName().lastIndexOf(".")));
            }
        }

    }

    /**
     * Retourne la langue selectionner par l'utilisateur
     * 
     * Attention la langue est en fait une Locale (fr pour francais par exemple)
     * 
     * @return un String qui contient la langue choisi par l'utilisateur
     */
    public String getSelectedLanguage ()
    {
        return (String) this.cbLanguages.getSelectedItem();
    }
    
    /**
     * Retourne le chemin d'acces selectionne par l'utilisateur
     * 
     * 
     * @return un string qui contient le chemin d'acces au workspace
     */
    public String getWorkspace()
    {
    	return this.tfChangeWorkspace.getText();
    }
}