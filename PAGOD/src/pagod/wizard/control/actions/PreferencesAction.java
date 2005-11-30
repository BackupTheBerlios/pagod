/*
 * $Id: PreferencesAction.java,v 1.3 2005/11/30 08:57:47 yak Exp $
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

package pagod.wizard.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.ui.PreferencesDialog;

/**
 * Action de configuration des preferences
 *  
 * @author MoOky
 */
public class PreferencesAction extends AbstractPagodAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public PreferencesAction() throws LanguagesManager.NotInitializedException,
                              IOException,
                              ImagesManager.NotInitializedException
    {
        super("preferences", "PreferencesIcon.gif",
        		new Request(Request.RequestType.PREFERENCES));
    }
    /**
     * Methode appélée lorsque l'action est déclenché
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    /*
    public void actionPerformed(ActionEvent actionEvent)
    {
    	//on ouvre un nouvelle fenetre
    	PreferencesDialog pd = new PreferencesDialog(ApplicationManager.getInstance().getMfPagod());
		pd.setVisible(true);
    }*/
}
