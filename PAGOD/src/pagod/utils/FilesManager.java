/*
 * $Id: FilesManager.java,v 1.1 2005/10/30 10:44:59 yak Exp $
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

/**
 * Gestionnaire de fichier implémenté comme un singleton Cette Classe permet
 * d'accéder a des resource exterieure et permet de situer l'application.
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
     * Chemin du repertoire ou est situé l'application
     */
    private String applicationPath = null;

    /**
     * Constructeur privé du gestionnaire d'images (implémentation d'un
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
            // on créer le fichier
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
                // TODO Bloc de traitement des exceptions généré automatiquement
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
     * Crée un fichier temporaire à partir d'un flux
     * 
     * @param streamToCopy
     *            flux a partir du quel on souhaite créer une image temporaire
     * @param fileName
     *            nom du fichier temporaire
     * @param extension
     *            extension avec le point
     * @param readyOnly
     *            vrai si l'on souahite que le fichier temporaire soit en
     *            lecture seule
     * @return Fichier temporaire, null s'il n'a pu être créé
     */
    public File createTempFile(InputStream streamToCopy, String fileName,
                               String extension, boolean readyOnly)
    {
        try
        {
            // création du fichier temporaire
            final File resultFile = File.createTempFile(fileName, extension);
            resultFile.deleteOnExit();

            // création du flux de sortie
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
        // la génération du fichier temporaire a échoué
        return null;
    }

    /**
     * Crée un fichier temporaire à partir d'un fichier
     * 
     * @param fileToCopy
     *            fichier a partir du quel on souhaite créer une image
     *            temporaire
     * @param fileName
     *            nom du fichier temporaire
     * @param extension
     *            extension avec le point
     * @param readyOnly
     *            vrai si l'on souahite que le fichier temporaire soit en
     *            lecture seule
     * @return Fichier temporaire, null s'il n'a pu être créé
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
}
