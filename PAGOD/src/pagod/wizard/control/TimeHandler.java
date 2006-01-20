/*
 * Projet PAGOD
 * 
 * $Id: TimeHandler.java,v 1.3 2006/01/20 10:12:41 fabfoot Exp $
 */
package pagod.wizard.control;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

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
	 */
	public void fillModel()
	{
		
		
	}
	
	/**
	 * 
	 */
	public void loadModel()
	{}
	
	
	
	/**
	 * @param sNameproject 
	 */
	public void writeXML(String sNameproject)
	{
		 try
		   {
		      //On utilise ici un affichage classique avec getPrettyFormat()
		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		      sortie.output(this.doc,new FileOutputStream(sNameproject));
	
		   }
		 catch(java.io.IOException e){}
	}
}
		

