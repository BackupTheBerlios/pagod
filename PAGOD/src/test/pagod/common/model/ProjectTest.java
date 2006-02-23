/*
 * Projet PAGOD
 * 
 * $Id: ProjectTest.java,v 1.5 2006/02/23 01:43:15 psyko Exp $
 */
package test.pagod.common.model;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import pagod.common.model.Process;
import pagod.common.model.Product;
import pagod.common.model.Project;
import pagod.wizard.control.Constants;
import pagod.wizard.control.PreferencesManager;

/**
 * Junit de la classe Project
 * 
 * @author biniou
 * 
 */
public class ProjectTest extends TestCase
{
	/* objet que l'on teste */
	private Project	project;

	// indique si le workspace utiliser pour le test est le repertoire
	// temporaire de la machine
	private boolean	bWorkspaceIsTmpDir	= false;

	/**
	 * @param arg0
	 */
	public ProjectTest (String arg0)
	{
		super(arg0);
	}

	protected void setUp () throws Exception
	{
		super.setUp();

		// on verifie que l'utilisateur a defini un espace de travail
		if (!PreferencesManager.getInstance().containWorkspace())
		{
			String sTmpDir = System.getProperty("java.io.tmpdir");
			if (sTmpDir == null) fail("Impossible de recuperer un repertoire temporaire pour en faire l'espace de travail durant le test");

			// on definit le repertoire temporaire comme espace de travail pour
			// le test
			PreferencesManager.getInstance().setWorkspace(sTmpDir);
			this.bWorkspaceIsTmpDir = true;
		}

		// creation d'un projet fictif
		String sName = "nomProjet";
		this.project = new Project(sName);
		try
		{
			this.project.createProject(sName);
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}
	}

	protected void tearDown () throws Exception
	{
		super.tearDown();
		// repertoire projet
		File projectDirectory = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + this.project.getName());

		// repertoire docs
		File docDirectory = new File(projectDirectory.getAbsolutePath()
				+ File.separator + Constants.DOCS_DIRECTORY);

		// documentation.properties
		File documentationPreferenceFile = new File(projectDirectory
				.getAbsolutePath()
				+ File.separator + "documentation.properties");

		// fichier xml
		File timeFile = new File(projectDirectory.getAbsolutePath()
				+ File.separator + Constants.NAME_FILE_TIME);

		docDirectory.delete();
		documentationPreferenceFile.delete();
		timeFile.delete();
		projectDirectory.delete();

		// si le workspace utilisé pour le test est le repertoire
		// temporaire de la machine on supprime l'espace de travail
		if (this.bWorkspaceIsTmpDir) 
			PreferencesManager.getInstance()
				.removeWorkspace();

	}

	/**
	 * Test method for 'pagod.common.model.Project.finalize()'
	 */
	/*
	 * public void testFinalize () { }
	 */

	/**
	 * test du constructeur
	 * 
	 * Test method for 'pagod.common.model.Project.Project(String)'
	 */
	public void testProject ()
	{
		String sName = "nomProjet";
		Project p = new Project(sName);
		assertEquals(p.getName(), sName);
	}

	/**
	 * Test de la methode createProject()
	 * 
	 * Test method for 'pagod.common.model.Project.createProject(String)'
	 */
	public void testCreateProject ()
	{

		// vérification de l'arborescence créée

		// repertoire projet
		File projectDirectory = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + this.project.getName());
		assertTrue("le repertoire projet existe bien", projectDirectory
				.exists());

		// repertoire docs
		File docDirectory = new File(projectDirectory.getAbsolutePath()
				+ File.separator + Constants.DOCS_DIRECTORY);
		assertTrue("le repertoire doc existe bien", docDirectory.exists());

		// documentation.properties
		File documentationPreferenceFile = new File(projectDirectory
				.getAbsolutePath()
				+ File.separator + "documentation.properties");
		assertTrue("le properties des docs existe bien",
				documentationPreferenceFile.exists());

		// fichier xml
		File timeFile = new File(projectDirectory.getAbsolutePath()
				+ File.separator + Constants.NAME_FILE_TIME);
		assertTrue("le xml existe bien", timeFile.exists());

	}

	/**
	 * test de la methode createdocument
	 * 
	 * Test method for 'pagod.common.model.Project.createDocument(Product,
	 * String)'
	 */
	public void testCreateDocument ()
	{
		// creation d'un produit
		Product prod = new Product("id", "name", null, null, "descr prod", null);

		// nom du document
		String docName = "nomDuDocument";
		// creation d'un document
		File documentationFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator
				+ this.project.getName()
				+ File.separator
				+ Constants.DOCS_DIRECTORY + docName);

		try
		{
			this.project.createDocument(prod, docName);
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}

		// on verifie que le document est bien créé
		assertTrue("le document existe bien", documentationFile.exists());
		// on verifie qu'il y a bien une entree dans le properties
		assertEquals(this.project.getDocsProperties().getProperty("id"),
				docName);

		// on supprime le fichier
		documentationFile.delete();

	}

	/**
	 * test de la methode changedpc
	 * 
	 * Test method for 'pagod.common.model.Project.changeDPC(File)'
	 */
	public void testChangeDPC ()
	{
		String firstName = "premier";
		File dpcFirstFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + firstName);
		try
		{
			dpcFirstFile.createNewFile();
		}
		catch (IOException e1)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e1.printStackTrace();
		}

		String secondName = "second";
		File dpcSecondFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator + secondName);

		try
		{
			dpcSecondFile.createNewFile();
		}
		catch (IOException e1)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e1.printStackTrace();
		}

		/* on associe un dpc pour la 1ere fois */
		try
		{
			this.project.changeDPC(dpcFirstFile);
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}

		File currentDpc = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator
				+ this.project.getName()
				+ File.separator
				+ firstName);

		// on verifie que l'attribut sNameDPC est bien changé
		assertEquals(this.project.getNameDPC(), firstName);

		// on verifie que le fichier est bien créé
		assertTrue("le dpc est bien créé", currentDpc.exists());

		// on associe le 2eme alors qu'un dpc est deja associé
		try
		{
			this.project.changeDPC(dpcSecondFile);
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}

		currentDpc = new File(PreferencesManager.getInstance().getWorkspace()
				+ File.separator + this.project.getName() + File.separator
				+ secondName);

		// on verifie que l'attribut sNameDPC est bien changé
		assertEquals(this.project.getNameDPC(), secondName);

		// on verifie que le nouveau fichier est bien créé
		assertTrue("le dpc est bien créé", currentDpc.exists());

		// on verifie que l'ancien n'est plus present
		assertFalse("l'ancien dpc n'est plus la", (new File(PreferencesManager
				.getInstance().getWorkspace()
				+ File.separator
				+ this.project.getName()
				+ File.separator
				+ firstName)).exists());

		// on supprime tous les fichiers créés
		dpcSecondFile.delete();
		dpcFirstFile.delete();
		currentDpc.delete();
	}

	/**
	 * test de la methode getcurrentprocess
	 * 
	 * Test method for 'pagod.common.model.Project.getCurrentProcess()'
	 */
	public void testGetCurrentProcess ()
	{
		// on associe un process
		Process process = new Process("idprocess", "nameprocess", null, null);
		this.project.setCurrentProcess(process);
		// on teste si c'est bien le meme qui a été associé
		assertEquals(this.project.getCurrentProcess(), process);

	}

	/**
	 * test de la methode setcurrentprocess
	 * 
	 * Test method for 'pagod.common.model.Project.setCurrentProcess(Process)'
	 */
	public void testSetCurrentProcess ()
	{
		// on associe un process
		Process process = new Process("idprocess", "nameprocess", null, null);
		this.project.setCurrentProcess(process);
		// on teste si c'est bien le meme qui a été associé
		assertEquals(this.project.getCurrentProcess(), process);
	}

	/**
	 * test de la methode hascurrentprocess
	 * 
	 * Test method for 'pagod.common.model.Project.hasCurrentProcess()'
	 */
	public void testHasCurrentProcess ()
	{
		Process p = this.project.getCurrentProcess();

		this.project.setCurrentProcess(null);
		assertFalse("il n'y a pas de processus associé", this.project
				.hasCurrentProcess());
		Process process = new Process("idprocess", "nameprocess", null, null);
		this.project.setCurrentProcess(process);
		assertTrue("il y a un process associé", this.project
				.hasCurrentProcess());

		this.project.setCurrentProcess(p);
	}

	/**
	 * test de la methode getname
	 * 
	 * Test method for 'pagod.common.model.Project.getName()'
	 */
	public void testGetName ()
	{
		String tempName = "nomTemporaire";
		String name = this.project.getName();
		this.project.setName(tempName);
		assertEquals(this.project.getName(), tempName);
		this.project.setName(name);
		assertEquals(this.project.getName(), name);
	}

	/**
	 * test de la methode setname
	 * 
	 * Test method for 'pagod.common.model.Project.setName(String)'
	 */
	public void testSetName ()
	{
		String tempName = "nomTemporaire";
		String name = this.project.getName();
		this.project.setName(tempName);
		assertEquals(this.project.getName(), tempName);
		this.project.setName(name);
		assertEquals(this.project.getName(), name);
	}

	/**
	 * test de la methode getnamedpc
	 * 
	 * Test method for 'pagod.common.model.Project.getNameDPC()'
	 */
	public void testGetNameDPC ()
	{
		String dpc = this.project.getNameDPC();
		String namedpc = "nomDPC";
		this.project.setNameDPC(namedpc);
		assertEquals(this.project.getNameDPC(), namedpc);

		this.project.setNameDPC(dpc);
		assertEquals(this.project.getNameDPC(), dpc);
	}

	/**
	 * test de la methode setnamedpc
	 * 
	 * Test method for 'pagod.common.model.Project.setNameDPC(String)'
	 */
	public void testSetNameDPC ()
	{
		String dpc = this.project.getNameDPC();
		String namedpc = "nomDPC";
		this.project.setNameDPC(namedpc);
		assertEquals(this.project.getNameDPC(), namedpc);

		this.project.setNameDPC(dpc);
		assertEquals(this.project.getNameDPC(), dpc);
	}

	/**
	 * test de la methode hasnamedpc
	 * 
	 * Test method for 'pagod.common.model.Project.hasNameDPC()'
	 */
	public void testHasNameDPC ()
	{
		String dpc = this.project.getNameDPC();

		String test = "nomdpc";
		this.project.setNameDPC(null);
		assertFalse(this.project.hasNameDPC());
		this.project.setNameDPC(test);
		assertTrue(this.project.hasNameDPC());

		this.project.setNameDPC(dpc);
	}

	/**
	 * test de la methode getdocsproperties
	 * 
	 * Test method for 'pagod.common.model.Project.getDocsProperties()'
	 */
	public void testGetDocsProperties ()
	{
		//TODO pas trouvé comment testé puisque pas de setter
	}

	/**
	 * test de la methode getdocumentname
	 * 
	 * Test method for 'pagod.common.model.Project.getDocumentName(Product)'
	 */
	public void testGetDocumentName ()
	{
		Product prod = new Product("id", "nom", null, null, "descr prod", null);
		String doc = "nomDoc";

		File documentationFile = new File(PreferencesManager.getInstance()
				.getWorkspace()
				+ File.separator
				+ this.project.getName()
				+ File.separator
				+ Constants.DOCS_DIRECTORY + doc);

		try
		{
			this.project.createDocument(prod, doc);
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}

		assertEquals(this.project.getDocumentName(prod), doc);

		documentationFile.delete();

	}

}
