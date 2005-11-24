/*
 * Projet PAGOD
 * 
 * $Id: Project.java,v 1.1 2005/11/24 19:30:00 biniou Exp $
 */
package pagod.common.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

//import javax.swing.JFileChooser;

//import pagod.common.ui.WorkspaceFileChooser;
//import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.PreferencesManager;

/**
 * @author biniou
 *
 */

public class Project
{
	
	/**
	 * sName est le nom (identifiant) du projet
	 */
	private String sName;
	
	/**
	 * sNameDPC est le nom du dpc utilisé par le projet
	 */
	private String sNameDPC = null;
	
	/**
	 * currentProcess est le processus contenant le modèle métier
	 * du dpc appartenant au projet
	 */
	private Process currentProcess;

	/**
	 *  Constructeur de la classe Project
	 * @param name 
	 */
	public Project (String name)
	{
		this.sName = name;
	}
	
	/**
	 * @param name est le nom que l'on veut donner au projet
	 * @throws IOException 
	 */
	public void createProject(String name) throws IOException
	{
		// si le workspace n'est pas défini on force le choix
		if (!PreferencesManager.getInstance().containWorkspace())
        {
        	// TODO 
        }
		
		// on crée l'arborescence : d'abord le repertoire Project
		File projectDirectory = new File(
					PreferencesManager.getInstance().getWorkspace()+"/"+name);
		if (projectDirectory.mkdir())
		{
			System.out.println("Le repertoire projet est bien créé.");
		}
		else 
		{
			System.err.println("Le repertoire que vous voulez creer existe déjà.");
		}
		
		// creation du repertoire doc dans le repertoire projet
		File docDirectory = new File(
				projectDirectory.getAbsolutePath()+"/"+"docs");
		
		if (docDirectory.mkdir())
		{
			System.out.println("Le repertoire docs est bien créé.");
		}
		else 
		{
			System.err.println("Repertoire docs deja présent.");
		}
		
		// creation du .properties pour les documents
		File documentationPreferenceFile = new File(
				projectDirectory.getAbsolutePath()+"/"+"documentation.properties");
		
		if (documentationPreferenceFile.createNewFile())
		{
			System.out.println("Le fichier properties est bien créé.");
		}
		else 
		{
			System.err.println("Le fichier properties est deja présent.");
		}
		
		// creation du .properties pour les stats
		/*File statsFile = new File(
				projectDirectory.getAbsolutePath()+"/"+"");
		
		if (documentationPreferenceFile.createNewFile())
		{
			System.out.println("Le fichier properties est bien créé.");
		}
		else 
		{
			System.err.println("Le fichier properties est deja présent.");
		}*/
		
	}
	
	/**
	 * @param prod est le produit que l'utilisateur veut créer
	 * @param name est le nom qui sera donné au document
	 * @throws IOException 
	 */
	public void createDocument(Product prod, String name) throws IOException
	{
		// on crée le document dans le repertoire docs
		File documentationFile = new File(
				PreferencesManager.getInstance().getWorkspace()+"/"+this.sName+"/docs/");
		
		if (documentationFile.createNewFile())
		{
			System.out.println("Le document est bien créé.");
		}
		else 
		{
			System.err.println("Un document portant le meme nom est deja présent.");
		}
		
		// on rajoute une entrée dans le .properties
		// TODO quand on aura créé la classe manager du .properties
		
	}
	
	/**
	 * @param DPC est le chemin du nouveau .dpc que l'on
	 * veut associer au projet
	 * @throws IOException 
	 */
	public void changeDPC(File DPC) throws IOException
	{
		
		File dpcCurrentFile = new File(
				PreferencesManager.getInstance().getWorkspace()+"/"+this.sName+"/"+this.sNameDPC);
		
//		 s'il existe on supprime l'ancien dpc
		if( dpcCurrentFile.exists())
		{			
			// on supprime l'ancien dpc
			if (dpcCurrentFile.delete())
			{
				System.out.println("L'ancien dpc est supprimé.");
			}
			else
			{
				System.err.println("l'ancien dpc n'a pas été supprimé.");
			}
		}
		
//		 on réinitialise l'attribut sNameDPC
		this.setNameDPC(dpcCurrentFile.getName());
		
//		on crée le fichier qui va accueillir le flux
		dpcCurrentFile = new File(
				PreferencesManager.getInstance().getWorkspace()+"/"+this.sName+"/"+DPC.getName());
		
		
		// on copie le  fichier en parametre et on le redirige
		// vers le .dpc du projet grace a des flux
		
		FileInputStream fis = new FileInputStream(DPC); 
		FileOutputStream fos = new FileOutputStream(dpcCurrentFile);

	    FileChannel channelSrc   = fis.getChannel();
	    FileChannel channelDest = fos.getChannel();

	    try
	    {
	    	channelSrc.transferTo(0, channelSrc.size() , channelDest);
	    }
	    catch(IOException ioEx)
	    {
	    	System.err.println("Erreur pendant la copie du fichier.");    	
	    }

	    fis.close();
	    fos.close();
	}

	/**
	 * @return Retourne l'attribut currentProcess
	 */
	public Process getCurrentProcess ()
	{
		return this.currentProcess;
	}

	/**
	 * @param currentProcess Valeur à donner à currentProcess
	 */
	public void setCurrentProcess (Process currentProcess)
	{
		this.currentProcess = currentProcess;
	}

	/**
	 * @return Retourne l'attribut sName
	 */
	public String getName ()
	{
		return this.sName;
	}

	/**
	 * @param name Valeur à donner à sName
	 */
	public void setName (String name)
	{
		this.sName = name;
	}

	/**
	 * @return Retourne l'attribut sNameDPC
	 */
	public String getNameDPC ()
	{
		return this.sNameDPC;
	}

	/**
	 * @param nameDPC Valeur à donner à sNameDPC
	 */
	public void setNameDPC (String nameDPC)
	{
		this.sNameDPC = nameDPC;
	}
	
	

}
