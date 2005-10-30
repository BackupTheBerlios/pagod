/*
 * $Id: MnemonicMenu.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.common.ui;


import javax.swing.JMenu;


/**
 * Menu capable de reconnaitre le & pour les definir en tant que Mnemonic
 * @author MoOky
 * 
 */
public class MnemonicMenu extends JMenu
{
    /**
     * Définie le titre du menu et le mnemonic à partir de la chaine passer en parametre
     * @param name
     *            titre du menu (exemple : name = &File
     *            donne : 
     *              -titre = File
     *              -Mnemonic = F
     */
    public MnemonicMenu(String name)
    {
        super();
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
                this.setMnemonic(charCodeAfterAnd);
                String stringBeforeAnd = name.substring(0, index);
                String stringAfterAnd = name.substring(index + 1);
                name = stringBeforeAnd.concat(stringAfterAnd);
            }
        }
        this.setText(name);
    }
}
