/*
 * $Id: ActionManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of PAGOD.
 *
 * SPWIZ is free software; you can redistribute it and/or modify
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

package pagod.utils;
 
import java.util.HashMap;

import javax.swing.AbstractAction;

/**
 * Gestionnaire d'actions implémenté comme un singleton Cette classe gére les
 * actions de l'application
 * 
 * @author MoOky
 */
public class ActionManager
{
    /**
     * Exception lévée s'il n'existe pas d'action associée à une clé
     */
    public class KeyNotFoundException extends RuntimeException
    {
    }

    /**
     * Exception lévée si l'on tente d'associer deux actions à la même clé
     */
    public class DupValForKeyException extends RuntimeException
    {
    }

    /**
     * Instance du gestionnaire d'actions
     */
    private static ActionManager amInstance = null;

    /**
     * Hashmap contenant toutes les actions gérées
     */
    private HashMap<String, AbstractAction> actionMap = new HashMap<String, AbstractAction>();

    /**
     * Constructeur privé du gestionnaire d'actions (implémentation d'un
     * singleton)
     */
    private ActionManager()
    {
    }

    /**
     * Retourne l'instance du gestionnaire d'actions
     * 
     * @return instance du gestionnaire d'actions
     */
    public static ActionManager getInstance()
    {
        if (ActionManager.amInstance == null)
        {
            ActionManager.amInstance = new ActionManager();
        }
        return (ActionManager.amInstance);
    }

    /**
     * Enregistre une action auprès du gestionnaire d'actions
     * 
     * @param key
     *            clé servant d'identifiant pour à enregistrer
     * @param action
     *            action à enregistrer
     * @exception DupValForKeyException
     *                S'il existe deja une action associée à cette clé
     */
    public void registerAction(String key, AbstractAction action)
    {
        try
        {
            getAction(key);
            throw new DupValForKeyException();
        }
        catch (KeyNotFoundException ex)
        {
            this.actionMap.put(key, action);
        }
    }

    /**
     * retourne une action à partir de la clé passer en parametre
     * 
     * @param key
     *            clé servant d'identifiant pour l'action rechercher
     * @return action correspondant à la clé passé en parametre
     * @exception KeyNotFoundException
     *                S'il n'existe pas d'action associée à cette clé
     */
    public AbstractAction getAction(String key)
    {
        AbstractAction action = this.actionMap.get(key);
        if (action == null)
        {
            throw new KeyNotFoundException();            
        }
        return action;
    }
}