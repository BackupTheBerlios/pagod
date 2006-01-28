/*
 * Projet PAGOD
 * 
 * $Id: Project.java,v 1.13 2006/01/28 16:34:02 cyberal82 Exp $
 */
package pagod.common.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import pagod.utils.FilesManager;
import pagod.wizard.control.Constants;
import pagod.wizard.control.PreferencesManager;

/**
 * @author biniou
 * 
 */

public class Project
{
	
	/**
	 * Chemin du r?pertoire ou vont etre stockees les documents produits
	 */
	// TODO rajouter dans le bundle pour avoir products ?
	public static final String	DOCS_DIRECTORY	= "produits" + File.separator;

	/**
	 * sName est le nom (identifiant) du projet
	 */
	private String				sName;

	/**
	 * sNameDPC est le nom du dpc utilis? par le projet
	 */
	private String				sNameDPC		= null;

	/**
	 * currentProcess est le processus contenant le mod?le m?tier du dpc
	 * appartenant au projet
	 */
	private Process				currentProcess	= null;

	/**
	 * docsProperties est le properties contenant les associations noms de
	 * docs/identifiant du produit
	 */
	private Properties			docsProperties	= null;

	/**
	 * Constructeur de la classe Project
	 * 
	 * @param name
	 */
	public Project (String name)
	{
		this.sName = name;
		this.docsProperties = new Properties();
	}

	/**
	 * @param name
	 *            est le nom que l'on veut donner au projet
	 * @return result : vrai si l'operation s'est bien pass?e faux sinon
	 * @throws IOException
	 */
	public boolean createProject (String name) throws IOException
	{
		boolean result = true;
		// on cr?e l'arborescence : d'abord le repertoire Project
		File projectDirectory = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + name);
		if (projectDirectory.mkdir())
		{
			System.out.println("Le repertoire projet est bien cr??.");
		}
		else
		{
			result = false;
			System.err
					.println("Le repertoire que vous voulez creer existe d?j?.");
		}

		// creation du repertoire doc dans le repertoire projet
		File docDirectory = new File(projectDirectory.getAbsolutePath()
				+ File.separator + DOCS_DIRECTORY);

		if (docDirectory.mkdir())
		{
			System.out.println("Le repertoire docs est bien cr??.");
		}
		else
		{
			System.err.println("Repertoire docs deja pr?sent.");
		}

		// creation du .properties pour les documents
		File documentationPreferenceFile = new File(projectDirectory
				.getAbsolutePath()
				+ File.separator + "documentation.properties");

		if (documentationPreferenceFile.createNewFile())
		{
			System.out.println("Le fichier properties est bien cr??.");
		}
		else
		{
			System.err.println("Le fichier properties est deja pr?sent.");
		}

		// creation du .xml pour les temps
		File timeFile = new File(projectDirectory.getAbsolutePath()
				+ File.separator + Constants.NAME_FILE_TIME );
		

		if (timeFile.createNewFile())
		{
			System.out.println("Le fichier des temps est bien cr??.");
		}
		else
		{
			System.err.println("Le fichier des temps est deja pr?sent.");
		}

		return (result);
	}

	/**
	 * @param prod
	 *            est le produit que l'utilisateur veut cr?er
	 * @param name
	 *            est le nom qui sera donn? au document
	 * @throws IOException
	 */
	public void createDocument (Product prod, String name) throws IOException
	{
		// on cr?e le document dans le repertoire docs
		File documentationFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator
				+ this.sName
				+ File.separator
				+ DOCS_DIRECTORY
				+ name);

		if (documentationFile.createNewFile())
		{
			System.out.println("Le document est bien cr??.");
		}
		else
		{
			System.err
					.println("Un document portant le meme nom est deja pr?sent.");
		}

		// on rajoute une entr?e dans le .properties
		this.docsProperties.setProperty(prod.getId(), name);

	}

	/**
	 * @param DPC
	 *            est le chemin du nouveau .dpc que l'on veut associer au projet
	 * @throws IOException
	 */
	public void changeDPC (File DPC) throws IOException
	{

		File dpcCurrentFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + this.sName + File.separator + this.sNameDPC);

		// s'il existe on supprime l'ancien dpc
		if (dpcCurrentFile.exists())
		{
			// on supprime l'ancien dpc
			if (dpcCurrentFile.delete())
			{
				System.out.println("L'ancien dpc est supprim?.");
			}
			else
			{
				System.err.println("l'ancien dpc n'a pas ?t? supprim?.");
			}
		}

		// on r?initialise l'attribut sNameDPC
		this.setNameDPC(DPC.getName());

		// on cr?e le fichier qui va accueillir le flux
		dpcCurrentFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + this.sName + File.separator + DPC.getName());

		// on copie le fichier en parametre et on le redirige
		// vers le .dpc du projet grace a des flux

		FileInputStream fis = new FileInputStream(DPC);
		FileOutputStream fos = new FileOutputStream(dpcCurrentFile);

		FilesManager.getInstance().copyFile(fis, fos);
	}

	/**
	 * stockage et sauvegarde des properties avant de fermer le projet
	 * 
	 * @throws IOException
	 */
	public void finalize () throws IOException
	{
		File documentationPreferenceFile = new File(PreferencesManager
				.getInstance().getWorkspace()
				+ File.separator
				+ this.sName
				+ File.separator
				+ "documentation.properties");
		String comments = "Fichier contenant l'index des documents crees par l'utilisateur.";

		try
		{
			this.docsProperties.store(new FileOutputStream(
					documentationPreferenceFile), comments);
		}
		catch (IOException ioEx)
		{
			System.err.println("Erreur dans le storage des properties.");
		}
	}

	/**
	 * @return Retourne l'attribut currentProcess
	 */
	public Process getCurrentProcess ()
	{
		return this.currentProcess;
	}

	/**
	 * @param currentProcess
	 *            Valeur ? donner ? currentProcess
	 */
	public void setCurrentProcess (Process currentProcess)
	{
		this.currentProcess = currentProcess;
	}

	/**
	 * Retourne true si le projet a un processus associe sinon false
	 * 
	 * @return true si le projet a un processus associe sinon false
	 */
	public boolean hasCurrentProcess ()
	{
		return this.currentProcess != null;
	}

	/**
	 * @return Retourne l'attribut sName
	 */
	public String getName ()
	{
		return this.sName;
	}

	/**
	 * @param name
	 *            Valeur ? donner ? sName
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
	 * @param nameDPC
	 *            Valeur ? donner ? sNameDPC
	 */
	public void setNameDPC (String nameDPC)
	{
		this.sNameDPC = nameDPC;
	}

	/**
	 * retourne vrai si le nomDu dpc est mis en place
	 * 
	 * @return retourne vrai si le nomDu dpc est mis en place
	 */
	public boolean hasNameDPC ()
	{
		return this.sNameDPC != null;
	}

	/**
	 * @return Retourne l'attribut docsProperties
	 */
	public Properties getDocsProperties ()
	{
		return this.docsProperties;
	}

	/**
	 * Retourne le nom du document associe l'identificateur du produit p.
	 * 
	 * 
	 * @param p
	 *            le produit dont on veut recuperer le document (fichier.doc par
	 *            exemple)
	 * @return Retourne le nom du document associe l'identificateur du produit p
	 *         si l'identificateur du produit existe sinon null
	 */
	public String getDocumentName (Product p)
	{
		return this.docsProperties.getProperty(p.getId());
	}
}
