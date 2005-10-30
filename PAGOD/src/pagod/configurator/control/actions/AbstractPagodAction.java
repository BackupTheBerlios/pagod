/*
 * $Id: AbstractPagodAction.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.configurator.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.KeyStroke;

import pagod.common.control.actions.CustomAction;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.LanguagesManager.NotInitializedException;
import pagod.configurator.control.ApplicationManager;

/**
 * Classe Abstraite mère de toutes les actions de PAGOD
 * 
 * @author MoOky
 */
public abstract class AbstractPagodAction extends CustomAction
{
    /**
     * requete
     */
    protected ApplicationManager.Request request;

    /**
     * Constructeur d'une Action composé d'un nom et d'un icone
     * 
     * @param label
     *            clé de la chaine souhaité en fonction de la locale de
     *            l'application
     * @param icon
     *            nom du fichier de l'icone se trouvant dans le repertoire des
     *            Images de l'application
     * @param request
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public AbstractPagodAction(String label, String icon,
                               ApplicationManager.Request request)
                                                                  throws NotInitializedException,
                                                                  IOException,
                                                                  ImagesManager.NotInitializedException
    {
        super(label, icon);
        this.request = request;
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
     * @param request
     * @param key
     * @param eventMask
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public AbstractPagodAction(String label, String icon,
                               ApplicationManager.Request request, char key,
                               int eventMask)
                                             throws NotInitializedException,
                                             IOException,
                                             ImagesManager.NotInitializedException
    {
        super(label, icon, key, eventMask);
        this.request = request;
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
     * @param request
     * @param keystroke
     * @throws LanguagesManager.NotInitializedException
     *             Si le LanguagesManager n'a pas été initialisé.
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     *             Si le ImagesManager n'a pas été initialisé.
     * @see LanguagesManager#setResourceFile(String, Locale)
     */
    public AbstractPagodAction(String label, String icon,
                               ApplicationManager.Request request,
                               KeyStroke keystroke)
                                                   throws NotInitializedException,
                                                   IOException,
                                                   ImagesManager.NotInitializedException
    {
        super(label, icon, keystroke);
        this.request = request;
    }

    /**
     * Methode appélée lorsque l'action est déclencher
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (this.isEnabled())
        {
            ApplicationManager.getInstance().manageRequest(this.request);
        }
    }
}