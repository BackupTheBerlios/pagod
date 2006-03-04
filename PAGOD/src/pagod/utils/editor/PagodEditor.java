/*
GNU Lesser General Public License

PagodEditor - Java Swing HTML Editor & Viewer
Copyright (C) 2000 Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package pagod.utils.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import pagod.common.model.Step;
import pagod.configurator.control.ApplicationManager;
import pagod.configurator.ui.StepsTable;
import pagod.utils.LanguagesManager;
import pagod.utils.editor.PagodEditorCore;


/**
 * PAGOD EDITOR
 * @author Arno
 *
 */
public class PagodEditor extends JFrame implements WindowListener, Runnable
{
	private PagodEditorCore pagodEditorCore;
	private JPanel fromPanel = null;
	// PagodObserver editorObserver = new PagodObserver();
	private File currentFile = (File)null;

	/** Master Constructor
	  * @param sDocument         [String]  A text or HTML document to load in the editor upon startup.
	  * @param sStyleSheet       [String]  A CSS stylesheet to load in the editor upon startup.
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	  * @param urlStyleSheet     [URL]     A URL reference to the CSS style sheet.
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar.
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run PagodEditor in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run PagodEditor in.
	  * @param base64            [boolean] Specifies whether the raw document is Base64 encoded or not.
	  * @param debugMode         [boolean] Specifies whether to show the Debug menu or not.
	  * @param useSpellChecker   [boolean] Specifies whether to include the spellchecker or not.
	  * @param multiBar          [boolean] Specifies whether to use multiple toolbars or one big toolbar.
	  */
	public PagodEditor(String sDocument, String sStyleSheet, String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean useSpellChecker, boolean multiBar, boolean b)
	{
		/*
		if(useSpellChecker)
		{
			pagodEditorCore = new EkitCoreSpell(sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, true, multiBar, (multiBar ? PagodEditorCore.TOOLBAR_DEFAULT_MULTI : PagodEditorCore.TOOLBAR_DEFAULT_SINGLE));
		}*/
		this.pagodEditorCore = new PagodEditorCore(sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? PagodEditorCore.TOOLBAR_DEFAULT_MULTI : PagodEditorCore.TOOLBAR_DEFAULT_SINGLE));
		

		pagodEditorCore.setFrame(this);

		/* Add the components to the app */
		if(includeToolBar)
		{
			if(multiBar)
			{
				this.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill       = GridBagConstraints.HORIZONTAL;
				gbc.anchor     = GridBagConstraints.NORTH;
				gbc.gridheight = 1;
				gbc.gridwidth  = 1;
				gbc.weightx    = 1.0;
				gbc.weighty    = 0.0;
				gbc.gridx      = 1;

				gbc.gridy      = 1;
				this.getContentPane().add(pagodEditorCore.getToolBarMain(includeToolBar), gbc);

				gbc.gridy      = 2;
				this.getContentPane().add(pagodEditorCore.getToolBarFormat(includeToolBar), gbc);

				gbc.gridy      = 3;
				this.getContentPane().add(pagodEditorCore.getToolBarStyles(includeToolBar), gbc);

				gbc.anchor     = GridBagConstraints.SOUTH;
				gbc.fill       = GridBagConstraints.BOTH;
				gbc.weighty    = 1.0;
				gbc.gridy      = 4;
				this.getContentPane().add(pagodEditorCore, gbc);
			}
			else
			{
				this.getContentPane().setLayout(new BorderLayout());
				this.getContentPane().add(pagodEditorCore, BorderLayout.CENTER);
				this.getContentPane().add(pagodEditorCore.getToolBar(includeToolBar), BorderLayout.NORTH);
			}
		}
		else
		{
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(pagodEditorCore, BorderLayout.CENTER);
		}

		this.setJMenuBar(pagodEditorCore.getMenuBar());

		this.addWindowListener(this);

		this.updateTitle();
		this.pack();
		this.setVisible(b);
	}

	public PagodEditor(JPanel j)
	{
		this(null, null, null, null, true, false, true, true, null, null, false, false, false, true,false);
	}
	
	public PagodEditor(Step steptoedit,String lang, StepsTable stepsTable, int StepRowNumber, boolean shown)
	{
		this(steptoedit.getComment(),null,null,null,true,false,true,true,lang,lang.toUpperCase(),false,false,false,true,shown);
		this.PagodEditorStart(steptoedit.getComment());
		this.pagodEditorCore.setStep(steptoedit);
		this.pagodEditorCore.setStepsTable(stepsTable);
		this.pagodEditorCore.setStepRowNumber(StepRowNumber);
		//		 this.editorObserver.setComposant(fromPanel);
	}
	
	/**
	 * Arno
	 * @param txttoedit
	 * @param x
	 * @param y
	 */
	private void PagodEditorStart(String txttoedit){
		this.setLocation(150, 50);
		pagodEditorCore.setDocumentText(txttoedit);
		pagodEditorCore.setCaretPosition(0);
	}
	
	/* WindowListener methods */
	public void windowClosing(WindowEvent we)
	{
		this.dispose();
		// System.exit(0);
	}
	public void windowOpened(WindowEvent we)      { ; }
	public void windowClosed(WindowEvent we)      { ; }
	public void windowActivated(WindowEvent we)   { ; }
	public void windowDeactivated(WindowEvent we) { ; }
	public void windowIconified(WindowEvent we)   { ; }
	public void windowDeiconified(WindowEvent we) { ; }

	/** Convenience method for updating the application title bar
	  */
	private void updateTitle()
	{
		this.setTitle(pagodEditorCore.getAppName() + (currentFile == null ? "" : " - " + currentFile.getName()));
	}

	/** Usage method
	  */
	public static void usage()
	{
		System.out.println("usage: pagod.utils.editor.PagodEditor [-t|t+|T] [-s|S] [-m|M] [-x|X] [-b|B] [-v|V] [-fFILE] [-cCSS] [-rRAW] [-uURL] [-lLANG] [-d|D] [-h|H|?]");
		System.out.println("       Each option contained in [] brackets is optional,");
		System.out.println("       and can be one of the values separated be the | pipe.");
		System.out.println("       Each option must be proceeded by a - hyphen.");
		System.out.println("       The options are:");
		System.out.println("         t|t+|T : -t = single toolbar, -t+ = multiple toolbars, -T = no toolbar");
		System.out.println("         s|S    : -s = show source window on startup, -S hide source window");
		System.out.println("         m|M    : -m = show icons on menus, -M no menu icons");
		System.out.println("         x|X    : -x = exclusive document/source windows, -X use split window");
		System.out.println("         b|B    : -b = use Base64 document encoding, -B use regular encoding");
		System.out.println("         v|V    : -v = include spell checker, -V omit spell checker");
		System.out.println("         -fFILE : load HTML document on startup (replace FILE with file name)");
		System.out.println("         -cCSS  : load CSS stylesheet on startup (replace CSS with file name)");
		System.out.println("         -rRAW  : load raw document on startup (replace RAW with file name)");
		System.out.println("         -uURL  : load document at URL on startup (replace URL with file URL)");
		System.out.println("         -lLANG : specify the starting language (defaults to your locale)");
		System.out.println("                    replace LANG with xx_XX format (e.g., US English is en_US)");
		System.out.println("         -d|D   : -d = DEBUG mode on, -D = DEBUG mode off (developers only)");
		System.out.println("         -h|H|? : print out this help information");
		System.out.println("         ");
		System.out.println("The defaults settings are equivalent to: -t+ -S -m -x -B -V -D");
		System.out.println("         ");
		System.out.println("For further information, read the README file.");
	}
	
	public PagodEditorCore getPagodEditorCore ()
	{
		return this.pagodEditorCore;
	}

	public void run ()
	{
		// TODO Corps de m�thode g�n�r� automatiquement
		
	}
}