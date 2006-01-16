/*
 * Projet PAGOD
 * 
 * $Id: TimeHandler.java,v 1.1 2006/01/16 17:33:25 fabfoot Exp $
 */
package pagod.wizard.control;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import pagod.common.control.DPCHandler.XMLGeneratorException;
import pagod.common.model.Process;
/**
 * @author Fabfoot
 *
 */
public class TimeHandler
{
	/** Processus lu par le parser */
   
	public TimeHandler()
	    {}
	
	/**
	 * @param process
	 */
	public void loadXML(Process process)
	{
		  final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		  final DocumentBuilder constructeur ;
		  final Document document;
		  try
		{
			constructeur = fabrique.newDocumentBuilder();
			document  = constructeur.newDocument();
			PreferencesManager.getInstance().getWorkspace();     
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		 
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
	public void writeXML(Document document)
	{}
}
