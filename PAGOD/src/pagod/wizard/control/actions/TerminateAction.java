/*
 * $Id: TerminateAction.java,v 1.6 2006/02/04 16:30:28 yak Exp $
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
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.ActionManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.states.Request;

/**
 * Termine l'activit?.
 * 
 * @author MoOky
 */
public class TerminateAction extends AbstractPagodAction
{
    /**
     * @throws LanguagesManager.NotInitializedException
     * @throws IOException
     * @throws ImagesManager.NotInitializedException
     */
    public TerminateAction() throws LanguagesManager.NotInitializedException,
                            IOException, ImagesManager.NotInitializedException
    {
        super("terminate", "TerminateIcon.gif",
        		new Request(Request.RequestType.TERMINATE_ACTIVITY), KeyStroke
                .getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    }
	/**
	 * Methode app?l?e lorsque l'action est d?clench?
	 * 
	 * @param actionEvent
	 *            Evenement survenue
	 */
    public void actionPerformed (ActionEvent actionEvent)
	{
    	if ( ApplicationManager.getInstance().manageRequest(this.request))
    	{
    		//on stop le timer
    		TimerManager.getInstance().stop();
    		Activity aTemp = ApplicationManager.getInstance().getMfPagod().getActivity();
    		//on enregistre le temps
    		aTemp.setTime(TimerManager.getInstance().getValue());
    		/*int iCurrentIt = 
    			ApplicationManager.getInstance().getCurrentProject().getItCurrent();
    		
    		aTemp.sethmTime(iCurrentIt, new TimeCouple(TimerManager.getInstance().getValue(), iTimeRemaining));*/
    		ActionManager.getInstance().getAction(Constants.ACTION_RUN_ACTIVITY).setEnabled(true);
    	}
	
	}
    
    
}
