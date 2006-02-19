/*
 * $Id: HtmlStepsAutodetector.java,v 1.2 2006/02/19 09:34:09 garwind111 Exp $
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

import java.net.URL;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.*;
import org.htmlparser.visitors.ObjectFindingVisitor;

import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.model.Step;

/**
 * Classe permettant d'extraire des �tapes dans un fichier HTML de contenu
 */
public class HtmlStepsAutodetector
{
	private static Vector<String> getStepsNames(URL url)
    {
        Parser parser;
        Vector<String> vector = null;
        vector = new Vector<String>();
        try
        {

            parser = new Parser(url.openConnection());
            // parser.registerScanners();
            ObjectFindingVisitor visitor = new ObjectFindingVisitor(Div.class);
            parser.visitAllNodesWith(visitor);
            Node[] nodes = visitor.getTags();
            for (int i = 0 ; i < nodes.length ; i++)
            {
                Div noeud = (Div) nodes[i];
                if (noeud.getAttribute("class") != null
                        && noeud.getAttribute("class").equals(
                                CSSClassChooser.getInstance().getTitleClass()))
                    vector.add(noeud.toPlainTextString());
            }
        }
        catch (Exception e)
        {
            return vector;
        }
        return (vector);
    }

    private static Vector<String> getStepsContents(URL url)
    {
        Parser parser;
        Vector<String> vector = null;
        vector = new Vector<String>();
        try
        {

            parser = new Parser(url.openConnection());
            // parser.registerScanners();
            ObjectFindingVisitor visitor = new ObjectFindingVisitor(Div.class);
            parser.visitAllNodesWith(visitor);
            Node[] nodes = visitor.getTags();
            for (int i = 0 ; i < nodes.length ; i++)
            {
                Div noeud = (Div) nodes[i];
                if (noeud.getAttribute("class") != null
                        && noeud.getAttribute("class").equals(
                                CSSClassChooser.getInstance().getBodyClass()))
                    vector.add(noeud.getChildrenHTML());
            }
        }
        catch (Exception e)
        {
            return vector;
        }
        return (vector);
    }
    
    /**
     * remplissage automatique des �tapes de l'activit� activity. Affiche une
     * bo�te de dialogue de choix des classes balises d'autod�tection
     * 
     * @param activity
     *            l'activit� dont on veut remplir automatiquement les �tapes
     * @return vrai si au moins une �tape a �t� autod�tect�e
     * 
     */
    public static boolean FillSteps(Activity activity)
    {
        return HtmlStepsAutodetector.FillSteps(activity, true);
    }
    
    /**
     * @param activity
     *            l'activit� dont on veut remplir automatiquement les �tapes
     * @param bDisplayClassChooser
     *            indique si l'on veut ou non afficher la fen�tre de choix des
     *            classes � d�tecter
     * @return vrai si au moins une �tape a �t� autod�tect�e
     */
    public static boolean FillSteps(Activity activity,
                                    boolean bDisplayClassChooser)
    {
        if (bDisplayClassChooser)
            if (!CSSClassChooser.getInstance().choose())
                return true;

        URL urlFichierContenuActivite = activity.getFileURL();
        if (urlFichierContenuActivite != null)
        {
            Vector<String> vectNomEtapes = HtmlStepsAutodetector
                    .getStepsNames(urlFichierContenuActivite);
            Vector<String> vectContenusEtapes = HtmlStepsAutodetector
                    .getStepsContents(urlFichierContenuActivite);
            int i = 0;
            
            // si pas d'�tapes autod�tect�es, renvoyer faux !
            if (vectNomEtapes.size() == 0)
            {
                return false;
            }
            
            // si + de noms d'�tapes d�tect�es que de contenu,
            // compl�ter le vector des contenus d'�tapes avec des cha�nes
            // vides
            if (vectNomEtapes.size() > vectContenusEtapes.size())
            {
                for (int ii = vectContenusEtapes.size() ; ii < vectNomEtapes
                        .size() ; ii++)
                {
                    vectContenusEtapes.add("");
                }
            }
            // Indique le flag pars�
            if (!activity.getIsParsed())
            	activity.setIsparsed(true);
            // Ajout de steps conditionnel
            if (activity.getIsParsed())
			{
				activity.removeAllSteps();
            	for (String s : vectNomEtapes)
				{
					activity.addStep(new Step(null, s, vectContenusEtapes
							.get(i++), null));
				}
			}
            else {
            	for (String s : vectNomEtapes)
				{
					activity.addStep(new Step(null, s, vectContenusEtapes
							.get(i++), null));
				}
            }
		}
		return true;

    }

    /**
	 * @param process
	 *            processus pour lequel on veut faire une d�tection de toutes
	 *            les �tapes de toutes les activit�s !
	 */
    public static void FillSteps(Process process)
    {
        boolean premierPassage = true;
        for (Activity a : process.getAllActivities())
        {
            if (premierPassage)
            {
                // premier passage, on demande les classes � utiliser pour la
                // d�tection
                HtmlStepsAutodetector.FillSteps(a, true);
                premierPassage = false;
            }
            else
            {
                // passages suivants : remplissage sans rechoisir les classes
                HtmlStepsAutodetector.FillSteps(a, false);
            }
        }
    }
}
