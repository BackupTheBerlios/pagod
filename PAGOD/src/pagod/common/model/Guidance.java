/*
 * $Id: Guidance.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.common.model;

import java.net.URL;

/**
 * Guide d'un élément de présentation
 * 
 * @author MoOky
 */
public class Guidance extends ProcessElement
{
    /**
     * Type du guide (exemple, plan type, ...)
     */
    private String type;

    /**
     * @param id
     * @param name
     * @param fileURL
     * @param iconURL
     * @param type
     */
    public Guidance(String id, String name, URL fileURL, URL iconURL,
                    String type)
    {
        super(id, name, fileURL, iconURL);
        this.type = type;
    }

    /**
     * @return Retourne le type du guide
     */
    public String getType()
    {
        return this.type;
    }

}
