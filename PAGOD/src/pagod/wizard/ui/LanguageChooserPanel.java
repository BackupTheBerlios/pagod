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

package pagod.wizard.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import pagod.utils.FilesManager;
import pagod.utils.LanguagesManager;
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

    /**
     * Constructeur de la classe LanguageChooserPanel
     */
    public LanguageChooserPanel()
    {
        super();

        this.languages = new ArrayList<String>();
        this.initLanguages();

        // panneau contenant le label de langue et le comboBox
        JPanel pCenter = new JPanel();

        pCenter.setLayout(new FlowLayout());

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

        this.add(lLangue);
        this.add(this.cbLanguages);
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
}