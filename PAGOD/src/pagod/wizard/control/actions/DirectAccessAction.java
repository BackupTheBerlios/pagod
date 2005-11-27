/*
 * $Id: DirectAccessAction.java,v 1.4 2005/11/27 20:36:49 yak Exp $
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

import javax.swing.JComboBox;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;
import pagod.wizard.ui.ButtonPanel;

/** 
 * 
 * @author Psyko
 */
public class DirectAccessAction extends AbstractPagodAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public DirectAccessAction() throws LanguagesManager.NotInitializedException,
                       IOException
    {
        super(Constants.ACTION_GOTOSTEP, new Request(Request.RequestType.GOTOSTEP));
    }
    
    /**
     * Methode appélée lorsque l'action est déclenché
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
    	// TODO a suppr
    	if (actionEvent.getSource() instanceof JComboBox)
    	{
    		ApplicationManager.getInstance().manageRequest((Request)((JComboBox)actionEvent.getSource()).getSelectedItem());
    	}
    	else
    		System.err.println("pb de caste dans DirectAccessAction");
    	
		
    }
}

