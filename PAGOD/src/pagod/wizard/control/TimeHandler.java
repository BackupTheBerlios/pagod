/*
 * Projet PAGOD
 * 
 * $Id: TimeHandler.java,v 1.11 2006/02/02 13:45:23 fabfoot Exp $
 */
package pagod.wizard.control;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pagod.common.model.Activity;
import pagod.common.model.Process;
import pagod.common.model.TimeCouple;

/**
 * @author Fabfoot
 * 
 */
public class TimeHandler
{
	/** Processus lu par le parser */
	private Document	doc;

	/**
	 * constucteur vide
	 */
	public TimeHandler ()
	{
	}

	/**
	 * @param document
	 * 
	 */
	public TimeHandler (Document document)
	{
		this.doc = document;
	}

	/**
	 * 
	 */
	public void affiche ()
	{
		try
		{
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(this.doc, System.out);
		}
		catch (java.io.IOException e)
		{
		}
	}

	/**
	 * 
	 * @param sNameproject
	 * 
	 */
	public void loadXML (String sNameproject)
	{
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			this.doc = sxb.build(new File(PreferencesManager.getInstance()
					.getWorkspace()
					+ File.separator
					+ sNameproject
					+ File.separator
					+ Constants.NAME_FILE_TIME));
		}
		catch (Exception e)
		{
		}

	}

	/**
	 * Charge le model metier a partir du doc
	 * 
	 * @param process
	 */
	public void fillModel (Process process)
	{
		// Recuperation de la structure
		Element rootnode = this.doc.getRootElement();
		List nodelist = rootnode.getChildren("activity");
		// Iterateur de la structure
		Iterator nodeiterator = nodelist.iterator();
		// Charge les activit?s dans une collection
		Collection<Activity> cactivity = process.getAllActivities();
		// System.err.println("test");
		while (nodeiterator.hasNext())
		{
			Element node = (Element) nodeiterator.next();
			Attribute idnode = node.getAttribute("idref");
			// System.err.println("test1");
			// Recherche l'activit? dans les process
			for (Activity acty : cactivity)
			{
				String activity_id = acty.getId();
				// Si id activit? trouv?
				// System.err.println("test de l'activit et de lid");
				if (activity_id.equals(idnode.getValue()))
				{
					Element time = node.getChild("time");
					// System.err.println("id trouv");
					// System.err.println(time.getText());
					acty.setTime(new Integer(time.getText()).intValue());
					// System.err.println(acty.getTime());
					break;
				}
			}
		}
	}

	/**
	 * charge le doc a partir du modele metier
	 * 
	 * @param process
	 */
	public void loadModel (Process process)
	{
		final Element racine = new Element("stats");
		final Document document = new Document(racine);
		// r?cup?ration des activit?s via le processus
		final Collection<Activity> cactivity = process.getAllActivities();
		for (Activity acty : cactivity)
		{
			Element activi = new Element("activity");
			racine.addContent(activi);
			Attribute id = new Attribute("idref", acty.getId());
			activi.setAttribute(id);
			Element time = new Element("time");
			time.setText(Integer.toString(acty.getTime())); // Cast Int -
															// Integer -> string
			activi.addContent(time);
			//System.out.println("test fab");
			HashMap hashmap = acty.getHM();
			Set s = hashmap.keySet();
			Iterator its  = s.iterator(); 
			//System.out.println("test fab");
			while(its.hasNext())
			{
				Integer key = (Integer) its.next(); 
				Element iteration = new Element("iteration");
				activi.addContent(iteration);
				Attribute idit = new Attribute("id_ite", key.toString() );
				iteration.setAttribute(idit);
				TimeCouple tc = acty.gethmTime(key);
				Element timeElapsed = new Element("timeElapsed");
				timeElapsed.setText(Integer.toString(tc.getTimeElapsed())); 
				iteration.addContent(timeElapsed);
				Element timeRemaining = new Element("timeRemaining");
				timeRemaining.setText(Integer.toString(tc.getTimeRemaining ())); 
				iteration.addContent(timeRemaining);
			}
			Element done = new Element("done");
			done.setText(Boolean.toString(acty.getDone()));
			activi.addContent(done);
		}

		this.doc = document;

	}

	/**
	 * @param sNameproject
	 */
	public void writeXML (String sNameproject)
	{
		try
		{
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(this.doc, new FileOutputStream(PreferencesManager
					.getInstance().getWorkspace()
					+ File.separator
					+ sNameproject
					+ File.separator
					+ Constants.NAME_FILE_TIME));

		}
		catch (java.io.IOException e)
		{
		}
	}
}
