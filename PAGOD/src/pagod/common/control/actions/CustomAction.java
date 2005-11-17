/*
 * $Id: CustomAction.java,v 1.2 2005/11/17 01:12:53 psyko Exp $
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

package pagod.common.control.actions;

import java.io.IOException;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;

/**
 * Classe Action personnalisé permettant facilitant la creation des actions gère
 * les Mnémonics à partir d'un de type "&nom"
 * 
 * @author MoOky
 * 
 */
public abstract class CustomAction extends AbstractAction
{

    /**
     * Constructeur d'une Action composé d'un nom  d'un icone
     * 
     * @param label
     *            clé de la chaine souhaité en fonction de la locale de
     *            l'application
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public CustomAction(String label)
                                                  throws NotInitializedException,
                                                  IOException
    {
        super();
        // definir le mnémonic si lon trouve le caractere & dans le nom
        String name = LanguagesManager.getInstance().getString(label);
        int index;
        if ((index = name.indexOf('&')) != -1)
        {
            // si le & n'est pas a la fin
            if (index < name.length() - 1)
            {
                // on recupere le caractere apres le & et on le defini comme
                // Mnemonic
                int charCodeAfterAnd = Character.toUpperCase(name
                        .charAt(index + 1));
                this.putValue(Action.MNEMONIC_KEY, charCodeAfterAnd);
                String stringBeforeAnd = name.substring(0, index);
                String stringAfterAnd = name.substring(index + 1);
                name = stringBeforeAnd.concat(stringAfterAnd);
            }
        }
        this.putValue(Action.NAME, name);
        this.putValue(Action.SHORT_DESCRIPTION, name);
    }
	
    /**
     * Constructeur d'une Action composé d'un nom et d'un icone
     * 
     * @param label
     *            clé de la chaine souhaité en fonction de la locale de
     *            l'application
     * @param icon
     *            nom du fichier de l'icone se trouvant dans le repertoire des
     *            Images de l'application
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public CustomAction(String label, String icon)
                                                  throws NotInitializedException,
                                                  IOException,
                                                  ImagesManager.NotInitializedException
    {
        super();
        // definir le mnémonic si lon trouve le caractere & dans le nom
        String name = LanguagesManager.getInstance().getString(label);
        int index;
        if ((index = name.indexOf('&')) != -1)
        {
            // si le & n'est pas a la fin
            if (index < name.length() - 1)
            {
                // on recupere le caractere apres le & et on le defini comme
                // Mnemonic
                int charCodeAfterAnd = Character.toUpperCase(name
                        .charAt(index + 1));
                this.putValue(Action.MNEMONIC_KEY, charCodeAfterAnd);
                String stringBeforeAnd = name.substring(0, index);
                String stringAfterAnd = name.substring(index + 1);
                name = stringBeforeAnd.concat(stringAfterAnd);
            }
        }
        this.putValue(Action.NAME, name);
        this.putValue(Action.SMALL_ICON, ImagesManager.getInstance()
                .getSmallIcon(icon));
        this.putValue(Action.SHORT_DESCRIPTION, name);
    }

    /**
     * Constructeur d'une Action composé d'un nom et d'un icone
     * 
     * @param label
     *            clé de la chaine souhaité en fonction de la locale de
     *            l'application
     * @param icon
     *            nom du fichier de l'icone se trouvant dans le repertoire des
     *            Images de l'application
     * @param key
     * @param eventMask
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public CustomAction(String label, String icon, char key, int eventMask)
                                                                           throws NotInitializedException,
                                                                           IOException,
                                                                           ImagesManager.NotInitializedException
    {
        this(label, icon);
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, eventMask));
    }

    /**
     * Constructeur d'une Action composé d'un nom et d'un icone
     * 
     * @param label
     *            clé de la chaine souhaité en fonction de la locale de
     *            l'application
     * @param icon
     *            nom du fichier de l'icone se trouvant dans le repertoire des
     *            Images de l'application
     * @param keystroke
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public CustomAction(String label, String icon, KeyStroke keystroke)
                                                                       throws NotInitializedException,
                                                                       IOException,
                                                                       ImagesManager.NotInitializedException
    {
        this(label, icon);
        putValue(Action.ACCELERATOR_KEY, keystroke);
    }
    
     /**
     * @param rootPane
     *            le rootPane a qui l'action doit etre associé
     * @param condition
     *            la condition qui doit etre respecté pour declanché l'action :
     *            WHEN_FOCUSED The action will be invoked only when the
     *            keystroke occurs while the component has the focus.
     *            WHEN_IN_FOCUSED_WINDOW The action will be invoked when the
     *            keystroke occurs while the component has the focus or if the
     *            component is in the window that has the focus. Note that the
     *            component need not be an immediate descendent of the window --
     *            it can be anywhere in the window's containment hierarchy. In
     *            other words, whenever any component in the window has the
     *            focus, the action registered with this component is invoked.
     *            WHEN_ANCESTOR_OF_FOCUSED_COMPONENT The action will be invoked
     *            when the keystroke occurs while the component has the focus or
     *            if the component is an ancestor of the component that has the
     *            focus.
     */
    public void configureRootPane(JRootPane rootPane, int condition)
    {
        InputMap inputMap = rootPane.getInputMap(condition);
        inputMap.put((KeyStroke) this.getValue(Action.ACCELERATOR_KEY), this
                .getValue(Action.NAME));
        rootPane.getActionMap().put(this.getValue(Action.NAME), this);
    }
}