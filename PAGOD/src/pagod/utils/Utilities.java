/*
 * $Id: Utilities.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.utils;

import java.awt.Component;
import java.awt.Container;
/**
 * @author Benjamin
 */
public class Utilities
{
    /**
     * Désactive ou active le container cont et tout son contenu (récursivement)
     * 
     * @param cont
     *            conteneur à impacter
     * @param enabled
     *            true: activer; false: désactiver
     */
    public static void setEnabledContainer(Container cont, boolean enabled)
    {
        Component[] components = cont.getComponents();
        for (Component c : components)
        {
            c.setEnabled(enabled);
            if (c instanceof Container)
            {
                setEnabledContainer((Container) c, enabled);
            }
        }
    }
}
