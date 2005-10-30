/*
 * $Id: LanguageChooserPanel.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.configurator.ui; 

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import pagod.configurator.control.Constants;
import pagod.configurator.control.PreferencesManager;
import pagod.utils.FilesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

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

    /**
     * combo representant la langue
     */
    private JComboBox cbLanguages = null;

    /**
     * Attribut permettant de specifier le repertoire de sortie des fichiers du
     * configurateur
     */
    JTextField tfOutputDirectory = null;

    /**
     * Bouton parcourir
     */
    JButton bpBrowse = null;

    /**
     * Constructeur de la classe LanguageChooserPanel
     * 
     * @throws MissingResourceException
     *             clé non trouvée dans le fichier de resources
     * @throws NotInitializedException
     *             languageManager pas initialisé
     * 
     */
    public LanguageChooserPanel() throws MissingResourceException,
                                 NotInitializedException

    {
        super();

        this.languages = new ArrayList<String>();
        this.initLanguages();

        // panneau contenant le label de langue et le comboBox
        JPanel pCenter = new JPanel();

        // on cree un GridBagLayout qui sera a 2 lignes et 3 colonnes
        GridBagLayout gridBag = new GridBagLayout();

        // on defini le layout du panneau central
        pCenter.setLayout(gridBag);

        // contraintes pour l'ajout des composants
        GridBagConstraints c = new GridBagConstraints();

        JLabel lLangue = new JLabel(LanguagesManager.getInstance().getString(
                "langueMessage"));

        // on ajoute le label lLangue
        // on definit les contraintes pour le label nom
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au label nom
        gridBag.setConstraints(lLangue, c);
        pCenter.add(lLangue);

        // on cree le combo pour les langues
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

        // on ajoute le comboBox cbLanguages
        // on definit les contraintes pour le textField nom
        c.gridx = 1;
        c.gridy = 0;

        // on lie les contraintes au textField nom
        gridBag.setConstraints(this.cbLanguages, c);
        pCenter.add(this.cbLanguages);

        // on ajoute le libelle du repertoire de sortie
        JLabel lOutputDirectory = new JLabel(LanguagesManager.getInstance()
                .getString("outputDirectory"));

        // on ajoute le label lLangue
        // on definit les contraintes pour le label lOutputDirectory
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au label lOutputDirectory
        gridBag.setConstraints(lOutputDirectory, c);
        pCenter.add(lOutputDirectory);

        // on ajoute le textFiel contenant le chemin du repertoire de sortie
        this.tfOutputDirectory = new JTextField();

        String sPath = PreferencesManager.getInstance().getOutputDirectory();
        if (sPath != null)
            this.tfOutputDirectory.setText(sPath);

        // on specifie un taille pour le texte field
        this.tfOutputDirectory.setColumns(20);

        // on definit les contraintes pour le textField tfOutputDirectory
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au textField tfOutputDirectory
        gridBag.setConstraints(this.tfOutputDirectory, c);
        pCenter.add(this.tfOutputDirectory);

        // on ajoute le bouton parcourir
        this.bpBrowse = new JButton(LanguagesManager.getInstance()
                .getString("browseButtonLabel"));

        // on definit les contraintes pour le bouton parcourir
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;

        // on lie les contraintes au textField tfOutputDirectory
        gridBag.setConstraints(this.bpBrowse, c);
        pCenter.add(this.bpBrowse);

        // on met le bouton browse sur ecoute
        this.bpBrowse.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent arg0)
            {

                // retourne la vue du système
                FileSystemView viewSystem = FileSystemView.getFileSystemView();

                // récupération des répertoires
                File home = viewSystem.getHomeDirectory();

                // création et affichage de l'arborescence
                // a partir du poste de travail
                JFileChooser homeChooser = new JFileChooser(home);

                homeChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnVal = homeChooser.showSaveDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {

                    // Recupere le path du fichier choisi
                    File directory = homeChooser.getSelectedFile();
                    String filePath = directory.getAbsoluteFile()
                            .getAbsolutePath();
                    LanguageChooserPanel.this.tfOutputDirectory
                            .setText(filePath);

                }

            }

        });

        this.add(pCenter);

    }

    /**
     * Methode prive qui initialise les langues disponibles dans le combo en
     * fonction des fichiers de langues present dans le repertoire prevu a cet
     * effet
     */
    private void initLanguages()
    {

        // construction du chemin d'acces au repertoire contenant les fichiers
        // de langues
        String sPath = FilesManager.getInstance().getRootPath()
                + Constants.LANGUAGES_OUTPUT_DIRECTORY;

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
    public String getSelectedLanguage()
    {
        return (String) this.cbLanguages.getSelectedItem();
    }

    /**
     * Retourne le chemin du repertoire de sorti des fichiers du configurateur
     * defini par l'utilisateur
     * 
     * @return une chaine de caractere contenant e chemin du repertoire de sorti
     *         des fichiers du configurateur
     */
    public String getOutputDirectory()
    {
        return this.tfOutputDirectory.getText();
    }
}