/*
 * Projet PAGOD
 * 
 * $Id: TimeHandler.java,v 1.2 2006/01/19 08:32:59 fabfoot Exp $
 */
package pagod.wizard.control;
import java.io.FileOutputStream;
import java.text.Format;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



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
 * @param d
 */
public TimeHandler(Document d)
   {
	   this.doc = d;
   }
	
	/**
	 * @param process
	 * @param sNameproject
	 * @return document
	 */
	public static Document init(Process process,String sNameproject)
	{
		final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = null ;
		try
		{
			constructeur = fabrique.newDocumentBuilder();
			//document  = constructeur.newDocument();
			PreferencesManager.getInstance().getWorkspace();     
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		final Document document = constructeur.newDocument();
        final Element racine = document.createElement("stats");

        // récupération des activités via le processus
        final Collection<Activity > cactivity = process.getAllActivities ();
        for (Activity acty : cactivity)
        {
             final Element activi = document.createElement("activity");
             activi.setAttribute("idref", acty.getId());
             final Element time = document.createElement("time");
             time.setTextContent("");
             activi.appendChild(time);
             racine.appendChild(activi);
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
		return this.doc;
	}

		 
	
	/**
	 * @param idActivity
	 * @return
	 */
	public int getTime(String idActivity)
	{
		int timeActivity=0;
		return timeActivity ;
		
	}
	/**
	 * @param idActivity
	 * @param timeActivity
	 */
	public void setTime(String idActivity,int timeActivity)
	{
		
	}
	/**
	 * @param document
	 */
	public void writeXML(String sNameproject)
	{}
}
		

