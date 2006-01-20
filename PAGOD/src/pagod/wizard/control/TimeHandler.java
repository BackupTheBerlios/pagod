/*
 * Projet PAGOD
 * 
 * $Id: TimeHandler.java,v 1.6 2006/01/20 13:23:05 garwind111 Exp $
 */
package pagod.wizard.control;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pagod.common.model.Activity;
import pagod.common.model.Process;

/**
 * @author Fabfoot
 *
 */
public class TimeHandler
{
	/** Processus lu par le parser */
   private Document doc;
   
  
  
/**
 * 
 */
public TimeHandler()
   {
	   this.doc = new Document ();
   }
	
	/**
	 * @param process
	 * @return document
	 */
	public static Document init(Process process)
	{
		
		final Element racine = new Element("stats");
		final Document document = new Document(racine);
        // r?cup?ration des activit?s via le processus
        final Collection<Activity > cactivity = process.getAllActivities ();
        for (Activity acty : cactivity)
        {
        	 Element activi= new Element("activity");
             racine.addContent(activi);
             Attribute id = new Attribute("idref",acty.getId());
             activi.setAttribute(id);
             Element time = new Element ("time");
             time.setText("");
             activi.addContent(time);
        }
            
        return document;
	}
	
	/**
	 * @param process
	 * @param sNameproject 
	 * @return document
	 */
	public Document loadXML(Process process,String sNameproject)
	{	     
		 SAXBuilder sxb = new SAXBuilder();
	      try
	      {
	    	  this.doc = sxb.build(new File(sNameproject));
	      }
	      catch(Exception e){}
		  		
		return this.doc;
	}

	/**
	 * Charge le model metier a partir du doc
	 * @param process 
	 */
	public void fillModel(Process process)
	{
		// Recuperation de la structure
		Element rootnode = this.doc.getRootElement();
		List nodelist = rootnode.getChildren("activty");
		// Iterateur de la structure
		Iterator nodeiterator = nodelist.iterator();
		// Charge les activités dans une collection
		Collection<Activity > cactivity = process.getAllActivities ();
		
		while(nodeiterator.hasNext()) {
			Element node = (Element) nodeiterator.next();
			Attribute idnode = node.getAttribute("idref");
			// Recherche l'activité dans les process
			for (Activity acty : cactivity) {
				String activity_id = acty.getId();
				// Si id activité trouvé
				if (activity_id == idnode.toString()) {
					Element time = node.getChild("time");
					time.setText(Integer.toString(acty.getTime()));
					break;
				}
			}
		}
	}
	
	/**
	 * charge le doc a partir du modele metier
	 * @param process
	 * @return Document xml
	 */
	public Document loadModel(Process process)
	{
		final Element racine = new Element("stats");
		final Document document = new Document(racine);
        // r?cup?ration des activit?s via le processus
        final Collection<Activity > cactivity = process.getAllActivities ();
        for (Activity acty : cactivity)
        {
        	 Element activi= new Element("activity");
             racine.addContent(activi);
             Attribute id = new Attribute("idref",acty.getId());
             activi.setAttribute(id);
             Element time = new Element ("time");
             time.setText(Integer.toString(acty.getTime())); // Cast Int - Integer -> string
             activi.addContent(time);
        }
            
        return document;
		
	}
	
	
	
	/**
	 * @param sNameproject 
	 */
	public void writeXML(String sNameproject)
	{
		String sworkspace; 
		try
		   {
		      //On utilise ici un affichage classique avec getPrettyFormat()
		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		       sworkspace = PreferencesManager.getInstance().getWorkspace();   
		      sworkspace = sworkspace + sNameproject ; 
		       sortie.output(this.doc,new FileOutputStream(sworkspace));
	
		   }
		 catch(java.io.IOException e){}
	}
}
		

