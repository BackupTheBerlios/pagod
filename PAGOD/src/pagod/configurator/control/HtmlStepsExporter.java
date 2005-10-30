/*
 * $Id: HtmlStepsExporter.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

package pagod.configurator.control;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pagod.common.model.Activity;
import pagod.common.model.Step;

/**
 * Classe destinée à gérer l'export d'une activité (plus précisément : des
 * étapes de cette activité) au format HTML
 * 
 * @author Benjamin
 */
public class HtmlStepsExporter
{

    /**
     * @param activityToExport
     *            activité dont on souhaite exporter les étapes
     * @param savePath
     *            chemin du fichier à sauvegarder
     * @return vrai si export réussi, faux sinon
     */
    public static boolean exportActivityToFile(Activity activityToExport,
                                               String savePath)
    {
        try
        {
            File f = new File(savePath);
            f.createNewFile();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
            dos.writeChars("<html>\n");
            dos.writeChars("<META HTTP-EQUIV=\"Content-Type\" "
                    + "CONTENT=\"text/html; charset=UTF-16\">\n");
            dos.writeChars("<body>\n");
            for (Step s : activityToExport.getSteps())
            {
                // faire un paragraphe pour chaque étape
                dos.writeChars("<p>\n");
                // titre de l'étape (renseigner la classe ad hoc le cas échéant)
                dos.writeChars("<div class=\""
                        + CSSClassChooser.getInstance().getTitleClass() + "\">"
                        + s.getName() + "</div>\n");
                // contenu l'étape (renseigner la classe ad hoc le cas échéant)
                dos.writeChars("<div class=\""
                        + CSSClassChooser.getInstance().getBodyClass() + "\">"
                        + s.getComment() + "</div>\n");
                dos.writeChars("</p>\n");
            }
            dos.writeChars("</body>\n");
            dos.writeChars("</html>\n");
            dos.close();
            return true;
        }
        catch (IOException e1)
        {
            return false;
        }
    }
}
