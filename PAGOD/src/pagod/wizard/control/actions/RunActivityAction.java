/*
 * $Id: RunActivityAction.java,v 1.6 2006/02/07 08:15:36 flotueur Exp $
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

import pagod.common.model.Activity;
import pagod.common.model.TimeCouple;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.TimerManager;
import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;
import pagod.wizard.ui.TimeEditDialog;

/**
 * Lance l'assitance d'une activité
 * 
 * @author MoOky
 */
public class RunActivityAction extends AbstractPagodAction
{
    /**
     * Contructeur
     * 
     * @throws LanguagesManager.NotInitializedException
     * @throws ImagesManager.NotInitializedException
     * @throws IOException
     */
    public RunActivityAction() throws LanguagesManager.NotInitializedException,
                              IOException,
                              ImagesManager.NotInitializedException
    {
        super("runActivity", "LaunchActivity.gif",
        		new Request(Request.RequestType.RUN_ACTIVITY), KeyStroke
                        .getKeyStroke(KeyEvent.VK_ENTER, 0));
    }
    
    /**
     * Methode appelée lorsque l'action est déclenché
     * 
     * @param actionEvent
     *            Evenement survenue
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
    	//si le project a pu etre ouvert alors on delegue la requete à l'application manager
    	Activity aTemp = ApplicationManager.getInstance().getMfPagod().getActivity();
    	
    	// Modif Flotueur : on appelle la boîte de dialogue TimeEditDialog qui va servir à
    	// connaitre et à modifier le temps passé sur une activité
    	TimeEditDialog ted1 = new TimeEditDialog(ApplicationManager.getInstance().getMfPagod(),aTemp);
    	ted1.pack();
    	ted1.setVisible(true);
    	// Fin modif Flotueur
    	
    	this.request.setContent(aTemp);
//    	on recupere le numéro de l'it
    	int iCurrentIt = 
			ApplicationManager.getInstance().getCurrentProject().getItCurrent();
    	TimeCouple tcTemp = aTemp.gethmTime(iCurrentIt);
    	
    	//on demarre le manager 
    	TimerManager.getInstance().start(tcTemp.getTimeElapsed(),tcTemp.getTimeRemaining());
    	
    	ApplicationManager.getInstance().manageRequest(this.request);
    }
}
