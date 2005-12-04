/*
 * $Id: ModelResourcesManager.java,v 1.2 2005/12/04 18:20:06 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of pagod.
 *
 *PAGOD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * PAGOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PAGOD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.common.control;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.filetypes.Action;
import org.jdesktop.jdic.filetypes.Association;
import org.jdesktop.jdic.filetypes.AssociationService;

import pagod.common.model.Activity;
import pagod.common.model.Guidance;
import pagod.common.model.ProcessComponent;
import pagod.common.model.ProcessElement;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Tool;
import pagod.common.model.WorkDefinition;
import pagod.utils.ExceptionManager;
import pagod.utils.FilesManager;
import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;
import pagod.utils.ImagesManager.NotInitializedException;
import pagod.wizard.control.PreferencesManager;
import pagod.wizard.control.PreferencesManager.InvalidExtensionException;
import pagod.wizard.ui.AddExtensionDialog;
import pagod.wizard.ui.SetToolDialog;

/**
 * Gestionnaire de ressource Impl?ment? comme un singleton Cette classe permet
 * d'acceder facilement au ressource du mod?le M?tier
 * 
 * @author MoOky
 */
public class ModelResourcesManager
{

	/**
	 * Instance du gestionnaire de ressource
	 */
	private static ModelResourcesManager	mrmInstance	= null;

	/**
	 * Cache des Images
	 */
	private HashMap<URL, Image>				imagesMap	= new HashMap<URL, Image>();

	/**
	 * Constructeur priv? du gestionnaire de ressource (impl?mentation d'un
	 * singleton)
	 */
	private ModelResourcesManager ()
	{
	}

	/**
	 * retourne l'instance du gestionnaire de ressources
	 * 
	 * @return instance du gestionnaire de ressources
	 */
	static public ModelResourcesManager getInstance ()
	{
		if (ModelResourcesManager.mrmInstance == null)
		{
			ModelResourcesManager.mrmInstance = new ModelResourcesManager();
		}
		return ModelResourcesManager.mrmInstance;
	}

	/**
	 * Retourne l'image de l'element du processus ou une image par default du
	 * type si getIconPath() == null
	 * 
	 * @param processElement
	 *            l'element de processus
	 * @return Image de l'element du processus ou l'icone par defaut du type
	 * 
	 */
	public Image getImage (ProcessElement processElement)
	{
		// element n'a pas d'icone
		if (processElement.getIconURL() == null)
		{
			// on retourne l'image par defaut
			return this.getDefaultImage(processElement);
		}
		else
		{
			// on recherche l'image
			try
			{
				return this.getImageResource(processElement.getIconURL());
			}
			// si le fichier n'existe pas
			catch (Exception e)
			{
				// on retourne l'image par defaut
				return this.getDefaultImage(processElement);
			}
		}
	}

	/**
	 * @param imageURL
	 *            URL de l'image
	 * @return Image Image de l'URL
	 * @throws IOException
	 */
	private Image getImageResource (URL imageURL) throws IOException
	{
		// on recherche si il est dans le cache
		Image img;
		if ((img = this.imagesMap.get(imageURL)) == null)
		{
			// si il n'est pas dans le cache
			// ouvrir l'image
			img = ImageIO.read(imageURL);
			// l'ajouter dans le cache
			this.imagesMap.put(imageURL, img);
		}
		return img;
	}

	/**
	 * Retourne l'image par defaut d'un element de processus
	 * 
	 * @param processElement
	 *            un element de processus
	 * @return l'image par defaut d'un element de processus
	 */
	public Image getDefaultImage (ProcessElement processElement)
	{
		ImagesManager imageManager = ImagesManager.getInstance();
		Image ImageToReturned;
		// on met un icone par defaut
		if (processElement.getClass() == Role.class) ImageToReturned = imageManager
				.getImageResource("RoleIcon.gif");
		else if (processElement.getClass() == Activity.class) ImageToReturned = imageManager
				.getImageResource("ActivityIcon.gif");
		else if (processElement.getClass() == WorkDefinition.class) ImageToReturned = imageManager
				.getImageResource("WorkDefinitionIcon.gif");
		else if (processElement.getClass() == ProcessComponent.class) ImageToReturned = imageManager
				.getImageResource("ProcessComponentIcon.gif");
		else if (processElement.getClass() == Guidance.class) ImageToReturned = imageManager
				.getImageResource("GuidanceIcon.gif");
		else if (processElement.getClass() == Product.class) ImageToReturned = imageManager
				.getImageResource("ProductIcon.gif");
		else if (processElement.getClass() == Process.class) ImageToReturned = imageManager
				.getImageResource("ProcessIcon.gif");
		else
			ImageToReturned = imageManager.getImageResource("NoIcon.gif");
		return ImageToReturned;
	}

	/**
	 * Retourne l'icone de taille 16x16 d'un element de processus
	 * 
	 * @param processElement
	 * @return l'icone
	 * 
	 * @throws NotInitializedException
	 *             Si le chemin des Images n'a pas ?t? d?finis.
	 * @see ImagesManager#setImagePath
	 */
	public Icon getSmallIcon (ProcessElement processElement)
	{
		return new ImageIcon(this.getImage(processElement).getScaledInstance(
				16, 16, Image.SCALE_SMOOTH));
	}

	/**
	 * Retourne l'icone d'un element de processus
	 * 
	 * @param processElement
	 * 
	 * @return l'icone
	 */
	public Icon getIcon (ProcessElement processElement)
	{
		return new ImageIcon(this.getImage(processElement));
	}

	/**
	 * lance le Loutil necessaire a la visualisation d'un fichier de contenu
	 * 
	 * @param processElement
	 */
	public void launchContentFile (ProcessElement processElement)
	{
		// on recupere l'url du fichier de contenu
		URL file = processElement.getFileURL();
		// on recupere le nom du fichier et l'extension
		if (file != null)
		{
			String sExt = file.toString().substring(
					file.toString().lastIndexOf("."), file.toString().length());
			String sName = file.toString().substring(
					file.toString().lastIndexOf("/") + 1,
					file.toString().length());
			// on va recopier le fichier, dans un fichier temporaire
			InputStream fileStream = null;
			try
			{
				fileStream = file.openStream();
			}
			catch (IOException e2)
			{
				// TODO Bloc de traitement des exceptions généré automatiquement
				ExceptionManager.getInstance().manage(e2);
			}
			if (fileStream != null)
			{
				File fTempFile = FilesManager.getInstance().createTempFile(
						fileStream, sName, sExt, true);
				String tempFilePath = fTempFile.getAbsolutePath();
				// si extension n'est pas défini on demande a l'utilisateur
				if (!PreferencesManager.getInstance().containPreference(sExt))
				{
					JOptionPane.showMessageDialog(null, LanguagesManager
							.getInstance().getString("nonDefineExtension"),
							LanguagesManager.getInstance().getString(
									"nonDefineExtensionTitle"),
							JOptionPane.WARNING_MESSAGE);
					new AddExtensionDialog(null, sExt);
				}

				// on test si l'extension existe
				String sCommand = "";

				// TODO Supprimer les commentaires
				if (PreferencesManager.getInstance().containPreference(sExt))
				{
					try
					{
						sCommand = PreferencesManager.getInstance()
								.getPreference(sExt);
					}
					catch (InvalidExtensionException e)
					{
						// TODO gerer cette erreur
						ExceptionManager.getInstance().manage(e);
					}
					// si aucune erreur ne s'est produite
					// on cree la commande et on lance le logiciel

					// si la commande est de lancer avec le logiciel pardefaut
					// alors
					if (sCommand.equals("default"))
					{
						try
						{
							Desktop.open(new File(tempFilePath));
						}
						catch (DesktopException e)
						{
							// TODO informer l'utilisateur que jdic n'est pas installer
							e.printStackTrace();
						}
					}
					else
					{

						if (sCommand != "") sCommand += " " + tempFilePath;
						try
						{
							Process p = Runtime.getRuntime().exec(sCommand);
							if (p == null) JOptionPane.showMessageDialog(null,
									LanguagesManager.getInstance().getString(
											"ErreurDeLancement"),
									LanguagesManager.getInstance().getString(
											"ErreurDeLancementTitle"),
									JOptionPane.WARNING_MESSAGE);
						}
						catch (IOException e)
						{
							// TODO gerer cette erreur
							ExceptionManager.getInstance().manage(e);
						}
					}
				}
			}
		}
	}

	/**
	 * lance l'outil associé au produit
	 * 
	 * @param product
	 *            produit pour lequel il faut lancer l'application
	 */
	public void launchProductApplication (Product product)
	{
		// on recupere le logiciel
		Tool tToolProduct = product.getEditor();
		if (tToolProduct == null)
		{
			JOptionPane.showMessageDialog(null, LanguagesManager.getInstance()
					.getString("nonDefineTool"), LanguagesManager.getInstance()
					.getString("nonDefineToolTitle"),
					JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			// recuperation du nom et du chemin
			String sPath = tToolProduct.getPath();
			// on test si le path existe
			if (sPath == null || sPath.equals(""))
			{
				JOptionPane.showMessageDialog(null, LanguagesManager
						.getInstance().getString("nonDefineToolPath"),
						LanguagesManager.getInstance().getString(
								"nonDefineToolPathTitle"),
						JOptionPane.WARNING_MESSAGE);
				// on ouvre le dialogue
				new SetToolDialog(null, tToolProduct);
			}
			else
			{
				// execution de la commande complete
				try
				{
					Process p = Runtime.getRuntime().exec(sPath);
					if (p == null) JOptionPane.showMessageDialog(null,
							LanguagesManager.getInstance().getString(
									"ErreurDeLancement"), LanguagesManager
									.getInstance().getString(
											"ErreurDeLancementTitle"),
							JOptionPane.WARNING_MESSAGE);
				}
				catch (IOException e)
				{
					// TODO GERER CETTE ERREUR
					// automatiquement
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, LanguagesManager
							.getInstance().getString("ErreurDeLancement"),
							LanguagesManager.getInstance().getString(
									"ErreurDeLancementTitle"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
}