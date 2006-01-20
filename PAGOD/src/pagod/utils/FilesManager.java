/*
 * $Id: FilesManager.java,v 1.2 2006/01/20 15:53:49 flotueur Exp $
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
 * SPWIZ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SPWIZ; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.utils;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import pagod.wizard.control.PreferencesManager;


/**
 * Gestionnaire de fichier impl?ment? comme un singleton Cette Classe permet
 * d'acc?der a des resource exterieure et permet de situer l'application.
 * 
 * @author MoOky
 */
public class FilesManager
{
    
	/**
     * Instance du gestionnaire d'images
     */
    private static FilesManager fmInstance = null;

    /**
     * Chemin du repertoire ou est situ? l'application
     */
    private String applicationPath = null;

    /**
     * Constructeur priv? du gestionnaire d'images (impl?mentation d'un
     * singleton)
     */
    private FilesManager()
    {
    }

    /**
     * retourne l'instance du gestionnaire d'images
     * 
     * @return instance du gestionnaire d'images
     */
    static public FilesManager getInstance()
    {
        if (FilesManager.fmInstance == null)
        {
            FilesManager.fmInstance = new FilesManager();
        }
        return FilesManager.fmInstance;
    }

    /**
     * retourne la racine de l'application (le chemin du repertoire contenant le
     * jar)
     * 
     * @return le chemin d'acces au fichier
     */
    public String getRootPath()
    {
        if (this.applicationPath == null)
        {
            // on recupere la location du code source
            URL path = this.getClass().getProtectionDomain().getCodeSource()
                    .getLocation();
            // on cr?er le fichier
            File f;
            try
            {
                f = new File(URLDecoder.decode(path.getFile(), "UTF-8"));
                // on recupere le protocole
                String protocol = path.getProtocol();
                // si le fichier existe
                if (f.exists())
                {
                    // si c'est un URL de type chemin de fichier
                    if (protocol == "file")
                    {
                        if (f.isDirectory())
                            this.applicationPath = f.getAbsolutePath()
                                    + File.separator;
                        else
                            this.applicationPath = f.getParentFile()
                                    .getAbsolutePath()
                                    + File.separator;
                    }
                }
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Bloc de traitement des exceptions g?n?r? automatiquement
                ExceptionManager.getInstance().manage(e);
            }
        }
        return this.applicationPath;
    }

    /**
     * Copier un fichier vers un autre
     * 
     * @param source
     * @param target
     * @throws IOException
     */
    public void copyFile(InputStream source, OutputStream target)
                                                                 throws IOException
    {
        final byte[] buffer = new byte[1024];
        int n;
        while ((n = source.read(buffer)) != -1)
            target.write(buffer, 0, n);
        source.close();
        target.close();
    }

    /**
     * Cr?e un fichier temporaire ? partir d'un flux
     * 
     * @param streamToCopy
     *            flux a partir du quel on souhaite cr?er une image temporaire
     * @param fileName
     *            nom du fichier temporaire
     * @param extension
     *            extension avec le point
     * @param readyOnly
     *            vrai si l'on souahite que le fichier temporaire soit en
     *            lecture seule
     * @return Fichier temporaire, null s'il n'a pu ?tre cr??
     */
    public File createTempFile(InputStream streamToCopy, String fileName,
                               String extension, boolean readyOnly)
    {
        try
        {
            // cr?ation du fichier temporaire
            final File resultFile = File.createTempFile(fileName, extension);
            resultFile.deleteOnExit();

            // cr?ation du flux de sortie
            final FileOutputStream outputStream = new FileOutputStream(
                    resultFile);

            // copie du fichier original
            this.copyFile(streamToCopy, outputStream);

            // fermeture des flux
            streamToCopy.close();
            outputStream.close();

            if (readyOnly)
                resultFile.setReadOnly();
            // retourne le fichier temporaire
            return resultFile;
        }
        catch (IOException e)
        {
            //TODO a voir
            ExceptionManager.getInstance().manage(e);
        }
        // la g?n?ration du fichier temporaire a ?chou?
        return null;
    }

    /**
     * Cr?e un fichier temporaire ? partir d'un fichier
     * 
     * @param fileToCopy
     *            fichier a partir du quel on souhaite cr?er une image
     *            temporaire
     * @param fileName
     *            nom du fichier temporaire
     * @param extension
     *            extension avec le point
     * @param readyOnly
     *            vrai si l'on souahite que le fichier temporaire soit en
     *            lecture seule
     * @return Fichier temporaire, null s'il n'a pu ?tre cr??
     */
    public File createTempFile(File fileToCopy, String fileName,
                               String extension, boolean readyOnly)
    {
        try
        {
            return this.createTempFile(new FileInputStream(fileToCopy),
                    fileName, extension, readyOnly);
        }
        catch (FileNotFoundException fnfe)
        {
            //TODO a voir
            ExceptionManager.getInstance().manage(fnfe);
            return null;
        }
    }
    
    //modif Flotueur
    
	/**
	 * Copie un fichier source vers un fichier destination
	 * 
	 * @param source
	 * 			
	 * @param destination
	 * @return boolean
	 */
    public static boolean copier( File source, File destination )
    {
            boolean resultat = false;
            // Declaration des flux
            java.io.FileInputStream sourceFile=null;
            java.io.FileOutputStream destinationFile=null;
            try {
                    // Cr?ation du fichier :
                    destination.createNewFile();
                    // Ouverture des flux
                    sourceFile = new java.io.FileInputStream(source);
                    destinationFile = new java.io.FileOutputStream(destination);
                    // Lecture par segment de 0.5Mo 
                    byte buffer[]=new byte[512*1024];
                    int nbLecture;
                    while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
                            destinationFile.write(buffer, 0, nbLecture);
                    } 
                    // Copie r?ussie
                    resultat = true;
            } catch( java.io.FileNotFoundException f ) {
            } catch( java.io.IOException e ) {
            } finally {
                    // Quoi qu'il arrive, on ferme les flux
                    try {
                            sourceFile.close();
                    } catch(Exception e) { }
                    try {
                            destinationFile.close();
                    } catch(Exception e) { }
            } 
            return( resultat );
    }
    
	/**
	 * Parcours recursivement un repertoire donne et deplace les fichiers 
	 * vers un repertoire destination
	 * 
	 * @param source_directory
	 * @param target_directory
	 */
    public static void copyDirectory ( File source_directory, File target_directory ) {
        if ( source_directory.isDirectory ( ) ) {
            File[] list = source_directory.listFiles();
            for ( int i = 0; i < list.length; i++) {
            		// Puisque le fichier point est un repertoire, on cree un nouveau repertoire
            		// portant le meme nom dans le repertoire destination
	        		File targetDir = new File(target_directory.getAbsolutePath()+list[i].getName());
	        		if (targetDir.mkdir())
	        		{
	        			System.out.println("Le repertoire projet est bien cr??.");
	        		}
	        		else
	        		{
	        			System.err
	        					.println("Le repertoire que vous voulez creer existe d?j?.");
	        		}
                    // Appel r?cursif sur les sous-r?pertoires
            		copyDirectory(list[i],target_directory);
            } 
        } else {
        	//Si le chemin pointe sur un fichier, on le copie dans l'emplacement destination
        	copier(source_directory,target_directory);
        	
        }
    }
    
    /**
     * 
     * @param path
     * @return resultat
     */
    static public boolean deleteDirectory(File path) { 
        boolean resultat = true; 
        if( path.exists() ) { 
                File[] files = path.listFiles(); 
                for(int i=0; i<files.length; i++) { 
                        if(files[i].isDirectory()) { 
                                resultat &= deleteDirectory(files[i]); 
                        } 
                        else { 
                        resultat &= files[i].delete(); 
                        } 
                } 
        } 
        resultat &= path.delete(); 
        return( resultat ); 
    }
    
    
    //fin modif Flotueur
}
