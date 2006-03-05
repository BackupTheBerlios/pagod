/*
GNU Lesser General Public License

PagodEditorCore - Base Java Swing HTML Editor & Viewer Class (Core)
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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import pagod.common.model.Step;
import pagod.configurator.ui.StepsTable;
import pagod.utils.editor.action.CustomAction;
import pagod.utils.editor.action.FormatAction;
import pagod.utils.editor.action.ListAutomationAction;
import pagod.utils.editor.action.SetFontFamilyAction;
import pagod.utils.editor.action.StylesAction;
import pagod.utils.editor.component.ExtendedHTMLDocument;
import pagod.utils.editor.component.ExtendedHTMLEditorKit;
import pagod.utils.editor.component.HTMLUtilities;
import pagod.utils.editor.component.ImageFileChooser;
import pagod.utils.editor.component.ImageURLDialog;
import pagod.utils.editor.component.JButtonNoFocus;
import pagod.utils.editor.component.JComboBoxNoFocus;
import pagod.utils.editor.component.JToggleButtonNoFocus;
import pagod.utils.editor.component.MutableFilter;
import pagod.utils.editor.component.PagodEditorAboutDialog;
import pagod.utils.editor.component.PropertiesDialog;
import pagod.utils.editor.component.SearchDialog;
import pagod.utils.editor.component.SimpleInfoDialog;
import pagod.utils.editor.component.UnicodeDialog;
import pagod.utils.editor.hexidec.util.Base64Codec;
import pagod.utils.editor.hexidec.util.Translatrix;


/**
 * PAGOD EDITOR CORE
 * @author Arno
 *
 */
public class PagodEditorCore extends JPanel implements ActionListener, KeyListener, FocusListener, DocumentListener
{
	final private String PAGOD_EDITOR_VERSION = "Pagod Editor 1.0";
	// Integration du modele pagod
	private StepsTable stepsTable = null;
	private int StepRowNumber = 0;
	private Step step = null;
	// public Observable editorObservable = new Observable();
	JPanel jpanel_source;
	
	/* Components */
	private JSplitPane jspltDisplay;
	private JTextPane jtpMain;
	private ExtendedHTMLEditorKit htmlKit;
	private ExtendedHTMLDocument htmlDoc;
	private StyleSheet styleSheet;
	private JTextArea jtpSource;
	private JScrollPane jspSource;
	private JToolBar jToolBar;
	private JToolBar jToolBarMain;
	private JToolBar jToolBarFormat;
	private JToolBar jToolBarStyles;

	private JButtonNoFocus jbtnNewHTML;
	// private JButtonNoFocus jbtnOpenHTML;
	private JButtonNoFocus jbtnSaveHTML;
	private JButtonNoFocus jbtnCut;
	private JButtonNoFocus jbtnCopy;
	private JButtonNoFocus jbtnPaste;
	private JButtonNoFocus jbtnUndo;
	private JButtonNoFocus jbtnRedo;
	private JButtonNoFocus jbtnBold;
	private JButtonNoFocus jbtnItalic;
	private JButtonNoFocus jbtnUnderline;
	private JButtonNoFocus jbtnStrike;
	private JButtonNoFocus jbtnSuperscript;
	private JButtonNoFocus jbtnSubscript;
	private JButtonNoFocus jbtnUList;
	private JButtonNoFocus jbtnOList;
	private JButtonNoFocus jbtnAlignLeft;
	private JButtonNoFocus jbtnAlignCenter;
	private JButtonNoFocus jbtnAlignRight;
	private JButtonNoFocus jbtnAlignJustified;
	private JButtonNoFocus jbtnFind;
	private JButtonNoFocus jbtnUnicode;
	private JButtonNoFocus jbtnUnicodeMath;
	private JButtonNoFocus jbtnAnchor;
	private JToggleButtonNoFocus jtbtnViewSource;
	private JComboBoxNoFocus jcmbStyleSelector;
	private JComboBoxNoFocus jcmbFontSelector;

	private Frame frameHandler;

	private HTMLUtilities htmlUtilities = new HTMLUtilities(this);

	/* Actions */
	private StyledEditorKit.BoldAction actionFontBold;
	private StyledEditorKit.ItalicAction actionFontItalic;
	private StyledEditorKit.UnderlineAction actionFontUnderline;
	private FormatAction actionFontStrike;
	private FormatAction actionFontSuperscript;
	private FormatAction actionFontSubscript;
	private ListAutomationAction actionListUnordered;
	private ListAutomationAction actionListOrdered;
	private SetFontFamilyAction actionSelectFont;
	private StyledEditorKit.AlignmentAction actionAlignLeft;
	private StyledEditorKit.AlignmentAction actionAlignCenter;
	private StyledEditorKit.AlignmentAction actionAlignRight;
	private StyledEditorKit.AlignmentAction actionAlignJustified;
	private CustomAction actionInsertAnchor;

	protected UndoManager undoMngr;
	protected UndoAction undoAction;
	protected RedoAction redoAction;

	/* Menus */
	private JMenuBar jMenuBar;
	private JMenu jMenuFile;
	private JMenu jMenuEdit;
	private JMenu jMenuView;
	private JMenu jMenuFont;
	private JMenu jMenuFormat;
	private JMenu jMenuInsert;
	private JMenu jMenuTable;
	// private JMenu jMenuForms;
	private JMenu jMenuSearch;
	private JMenu jMenuTools;
	private JMenu jMenuHelp;
	private JMenu jMenuDebug;

	private JMenu jMenuToolbars;
	private JCheckBoxMenuItem jcbmiViewToolbar;
	private JCheckBoxMenuItem jcbmiViewToolbarMain;
	private JCheckBoxMenuItem jcbmiViewToolbarFormat;
	private JCheckBoxMenuItem jcbmiViewToolbarStyles;
	private JCheckBoxMenuItem jcbmiViewSource;

	/* Constants */
	// Menu Keys
	/**
	 * Commentaire pour <code>KEY_MENU_FILE</code>
	 */
	public static final String KEY_MENU_FILE   = "file";
	/**
	 * 
	 */
	public static final String KEY_MENU_EDIT   = "edit";
	/**
	 * 
	 */
	public static final String KEY_MENU_VIEW   = "view";
	/**
	 * 
	 */
	public static final String KEY_MENU_FONT   = "font";
	/**
	 * 
	 */
	public static final String KEY_MENU_FORMAT = "format";
	/**
	 * 
	 */
	public static final String KEY_MENU_INSERT = "insert";
	/**
	 * 
	 */
	public static final String KEY_MENU_TABLE  = "table";
	/**
	 * 
	 */
	public static final String KEY_MENU_FORMS  = "forms";
	/**
	 * 
	 */
	public static final String KEY_MENU_SEARCH = "search";
	/**
	 * 
	 */
	public static final String KEY_MENU_TOOLS  = "tools";
	/**
	 * 
	 */
	public static final String KEY_MENU_HELP   = "help";
	/**
	 * 
	 */
	public static final String KEY_MENU_DEBUG  = "debug";

	// Tool Keys
	/**
	 * 
	 */
	public static final String KEY_TOOL_SEP       = "SP";
	/**
	 * 
	 */
	public static final String KEY_TOOL_NEW       = "NW";
	/**
	 * 
	 */
	public static final String KEY_TOOL_OPEN      = "OP";
	public static final String KEY_TOOL_SAVE      = "SV";
	public static final String KEY_TOOL_CUT       = "CT";
	public static final String KEY_TOOL_COPY      = "CP";
	public static final String KEY_TOOL_PASTE     = "PS";
	public static final String KEY_TOOL_UNDO      = "UN";
	public static final String KEY_TOOL_REDO      = "RE";
	public static final String KEY_TOOL_BOLD      = "BL";
	public static final String KEY_TOOL_ITALIC    = "IT";
	public static final String KEY_TOOL_UNDERLINE = "UD";
	public static final String KEY_TOOL_STRIKE    = "SK";
	public static final String KEY_TOOL_SUPER     = "SU";
	public static final String KEY_TOOL_SUB       = "SB";
	public static final String KEY_TOOL_ULIST     = "UL";
	public static final String KEY_TOOL_OLIST     = "OL";
	public static final String KEY_TOOL_ALIGNL    = "AL";
	public static final String KEY_TOOL_ALIGNC    = "AC";
	public static final String KEY_TOOL_ALIGNR    = "AR";
	public static final String KEY_TOOL_ALIGNJ    = "AJ";
	public static final String KEY_TOOL_UNICODE   = "UC";
	public static final String KEY_TOOL_UNIMATH   = "UM";
	public static final String KEY_TOOL_FIND      = "FN";
	public static final String KEY_TOOL_ANCHOR    = "LK";
	public static final String KEY_TOOL_SOURCE    = "SR";
	public static final String KEY_TOOL_STYLES    = "ST";
	public static final String KEY_TOOL_FONTS     = "FO";

	public static final String TOOLBAR_DEFAULT_MULTI  = "NW|OP|SV|SP|CT|CP|PS|SP|UN|RE|SP|FN|SP|UC|UM|SP|SR|*|BL|IT|UD|SP|SK|SU|SB|SP|AL|AC|AR|AJ|SP|UL|OL|SP|LK|*|ST|SP|FO";
	public static final String TOOLBAR_DEFAULT_SINGLE = "NW|OP|SV|SP|CT|CP|PS|SP|UN|RE|SP|BL|IT|UD|SP|FN|SP|UC|SP|LK|SP|SR|SP|ST";

	public static final int TOOLBAR_SINGLE = 0;
	public static final int TOOLBAR_MAIN   = 1;
	public static final int TOOLBAR_FORMAT = 2;
	public static final int TOOLBAR_STYLES = 3;

	// Menu & Tool Key Arrays
	private static Hashtable htMenus = new Hashtable();
	private static Hashtable htTools = new Hashtable();

	private final String appName = "PAGOD Editor";
	private final String menuDialog = "..."; /* text to append to a MenuItem label when menu item opens a dialog */

	private final boolean useFormIndicator = true; /* Creates a highlighted background on a new FORM so that it may be more easily edited */
	private final String clrFormIndicator = "#cccccc";

	// System Clipboard Settings
	private java.awt.datatransfer.Clipboard sysClipboard;
	private SecurityManager secManager;

	/* Variables */
	private int iSplitPos = 0;

	private boolean exclusiveEdit = true;

	private String lastSearchFindTerm     = null;
	private String lastSearchReplaceTerm  = null;
	private boolean lastSearchCaseSetting = false;
	private boolean lastSearchTopSetting  = false;

	private File currentFile = null;
	private String imageChooserStartDir = ".";

	private int indent = 0;
	private final int indentStep = 4;

	// File extensions for MutableFilter
	private final String[] extsHTML = { "html", "htm", "shtml" };
	private final String[] extsCSS  = { "css" };
	private final String[] extsIMG  = { "gif", "jpg", "jpeg", "png" };
	private final String[] extsRTF  = { "rtf" };
	private final String[] extsB64  = { "b64" };
	private final String[] extsSer  = { "ser" };

	/** Master Constructor
	  * @param sDocument         [String]  A text or HTML document to load in the editor upon startup.
	  * @param sStyleSheet       [String]  A CSS stylesheet to load in the editor upon startup.
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	  * @param sdocSource        [StyledDocument] Optional document specification, using javax.swing.text.StyledDocument.
	  * @param urlStyleSheet     [URL]     A URL reference to the CSS style sheet.
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar(s).
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run PagodEditor in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run PagodEditor in.
	  * @param base64            [boolean] Specifies whether the raw document is Base64 encoded or not.
	  * @param debugMode         [boolean] Specifies whether to show the Debug menu or not.
	  * @param hasSpellChecker   [boolean] Specifies whether or not this uses the SpellChecker module
	  * @param multiBar          [boolean] Specifies whether to use multiple toolbars or one big toolbar.
	  * @param toolbarSeq        [String]  Code string specifying the toolbar buttons to show.
	  */
	@SuppressWarnings("unchecked")
	public PagodEditorCore(String sDocument, String sStyleSheet, String sRawDocument, StyledDocument sdocSource, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean hasSpellChecker, boolean multiBar, String toolbarSeq)
	{
		super();

		this.exclusiveEdit = editModeExclusive;

		this.frameHandler = new Frame();

		// Determine if system clipboard is available
		this.secManager = System.getSecurityManager();
		if(this.secManager != null)
		{
			try
			{
				this.secManager.checkSystemClipboardAccess();
				this.sysClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			}
			catch (SecurityException se)
			{
				this.sysClipboard = null;
			}
		}

		/* Localize for language */
		Translatrix.setBundleName("resources.languages.editor.LanguageResources");
		Locale baseLocale = null; // (Locale) null
		if(sLanguage != null && sCountry != null)
		{
			baseLocale = new Locale(sLanguage, sCountry);
		}
		Translatrix.setLocale(baseLocale);

		/* Create the editor kit, document, and stylesheet */
		this.jtpMain = new JTextPane();
		this.htmlKit = new ExtendedHTMLEditorKit();
		this.htmlDoc = (ExtendedHTMLDocument)(this.htmlKit.createDefaultDocument());
		this.styleSheet = this.htmlDoc.getStyleSheet();
		this.htmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.jtpMain.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		/* Set up the text pane */
		this.jtpMain.setEditorKit(this.htmlKit);
		this.jtpMain.setDocument(this.htmlDoc);
		this.jtpMain.setMargin(new Insets(4, 4, 4, 4));
		this.jtpMain.addKeyListener(this);
		this.jtpMain.addFocusListener(this);
//		jtpMain.setDragEnabled(true); // this causes an error in older Java versions

		/* Create the source text area */
		if(sdocSource == null)
		{
			this.jtpSource = new JTextArea();
			this.jtpSource.setText(this.jtpMain.getText());
		}
		else
		{
			this.jtpSource = new JTextArea(sdocSource);
			this.jtpMain.setText(this.jtpSource.getText());
		}
		this.jtpSource.setBackground(new Color(212, 212, 212));
		this.jtpSource.setSelectionColor(new Color(255, 192, 192));
		this.jtpSource.setMargin(new Insets(4, 4, 4, 4));
		this.jtpSource.getDocument().addDocumentListener(this);
		this.jtpSource.addFocusListener(this);
		this.jtpSource.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		/* Add CaretListener for tracking caret location events */
		this.jtpMain.addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent ce)
			{
				handleCaretPositionChange(ce);
			}
		});

		/* Set up the undo features */
		this.undoMngr = new UndoManager();
		this.undoAction = new UndoAction();
		this.redoAction = new RedoAction();
		this.jtpMain.getDocument().addUndoableEditListener(new CustomUndoableEditListener());

		/* Insert raw document, if exists */
		if(sRawDocument != null && sRawDocument.length() > 0)
		{
			if(base64)
			{
				this.jtpMain.setText(Base64Codec.decode(sRawDocument));
			}
			else
			{
				this.jtpMain.setText(sRawDocument);
			}
		}
		this.jtpMain.setCaretPosition(0);
		this.jtpMain.getDocument().addDocumentListener(this);

		/* Import CSS from reference, if exists */
		if(urlStyleSheet != null)
		{
			try
			{
				String currDocText = this.jtpMain.getText();
				this.htmlDoc = (ExtendedHTMLDocument)(this.htmlKit.createDefaultDocument());
				this.styleSheet = this.htmlDoc.getStyleSheet();
				BufferedReader br = new BufferedReader(new InputStreamReader(urlStyleSheet.openStream()));
				this.styleSheet.loadRules(br, urlStyleSheet);
				br.close();
				this.htmlDoc = new ExtendedHTMLDocument(this.styleSheet);
				registerDocument(this.htmlDoc);
				this.jtpMain.setText(currDocText);
				this.jtpSource.setText(this.jtpMain.getText());
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
			}
		}

		/* Preload the specified HTML document, if exists */
		if(sDocument != null)
		{
			File defHTML = new File(sDocument);
			if(defHTML.exists())
			{
				try
				{
					openDocument(defHTML);
				}
				catch(Exception e)
				{
					logException("Exception in preloading HTML document", e);
				}
			}
		}

		/* Preload the specified CSS document, if exists */
		if(sStyleSheet != null)
		{
			File defCSS = new File(sStyleSheet);
			if(defCSS.exists())
			{
				try
				{
					openStyleSheet(defCSS);
				}
				catch(Exception e)
				{
					logException("Exception in preloading CSS stylesheet", e);
				}
			}
		}

		/* Collect the actions that the JTextPane is naturally aware of */
		Hashtable actions = new Hashtable();
		Action[] actionsArray = this.jtpMain.getActions();
		for(int i = 0; i < actionsArray.length; i++)
		{
			Action a = actionsArray[i];
			actions.put(a.getValue(Action.NAME), a);
		}

		/* Create shared actions */
		this.actionFontBold        = new StyledEditorKit.BoldAction();
		this.actionFontItalic      = new StyledEditorKit.ItalicAction();
		this.actionFontUnderline   = new StyledEditorKit.UnderlineAction();
		this.actionFontStrike      = new FormatAction(this, Translatrix.getTranslationString("FontStrike"), HTML.Tag.STRIKE);
		this.actionFontSuperscript = new FormatAction(this, Translatrix.getTranslationString("FontSuperscript"), HTML.Tag.SUP);
		this.actionFontSubscript   = new FormatAction(this, Translatrix.getTranslationString("FontSubscript"), HTML.Tag.SUB);
		this.actionListUnordered   = new ListAutomationAction(this, Translatrix.getTranslationString("ListUnordered"), HTML.Tag.UL);
		this.actionListOrdered     = new ListAutomationAction(this, Translatrix.getTranslationString("ListOrdered"), HTML.Tag.OL);
		this.actionSelectFont      = new SetFontFamilyAction(this, "[MENUFONTSELECTOR]");
		this.actionAlignLeft       = new StyledEditorKit.AlignmentAction(Translatrix.getTranslationString("AlignLeft"), StyleConstants.ALIGN_LEFT);
		this.actionAlignCenter     = new StyledEditorKit.AlignmentAction(Translatrix.getTranslationString("AlignCenter"), StyleConstants.ALIGN_CENTER);
		this.actionAlignRight      = new StyledEditorKit.AlignmentAction(Translatrix.getTranslationString("AlignRight"), StyleConstants.ALIGN_RIGHT);
		this.actionAlignJustified  = new StyledEditorKit.AlignmentAction(Translatrix.getTranslationString("AlignJustified"), StyleConstants.ALIGN_JUSTIFIED);
		this.actionInsertAnchor    = new CustomAction(this, Translatrix.getTranslationString("InsertAnchor") + this.menuDialog, HTML.Tag.A);

		/* Build the menus */
		/* FILE Menu */
		this.jMenuFile              = new JMenu(Translatrix.getTranslationString("File"));
		htMenus.put(KEY_MENU_FILE, this.jMenuFile);
		JMenuItem jmiNew       = new JMenuItem(Translatrix.getTranslationString("NewDocument"));                     jmiNew.setActionCommand("newdoc");        jmiNew.addActionListener(this);      jmiNew.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_MASK, false));      if(showMenuIcons) { jmiNew.setIcon(getEkitIcon("New")); } this.jMenuFile.add(jmiNew);
		JMenuItem jmiSaveBody  = new JMenuItem(Translatrix.getTranslationString("SaveToPagod") + this.menuDialog); jmiSaveBody.setActionCommand("savebody"); jmiSaveBody.addActionListener(this); jmiSaveBody.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK, false)); this.jMenuFile.add(jmiSaveBody);
		// JMenuItem jmiOpenHTML  = new JMenuItem(Translatrix.getTranslationString("OpenDocument") + menuDialog);       jmiOpenHTML.setActionCommand("openhtml"); jmiOpenHTML.addActionListener(this); jmiOpenHTML.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiOpenHTML.setIcon(getEkitIcon("Open")); }; jMenuFile.add(jmiOpenHTML);
		// JMenuItem jmiOpenCSS   = new JMenuItem(Translatrix.getTranslationString("OpenStyle") + menuDialog);          jmiOpenCSS.setActionCommand("opencss");   jmiOpenCSS.addActionListener(this);  jMenuFile.add(jmiOpenCSS);
		// JMenuItem jmiOpenB64   = new JMenuItem(Translatrix.getTranslationString("OpenBase64Document") + menuDialog); jmiOpenB64.setActionCommand("openb64");   jmiOpenB64.addActionListener(this);  jMenuFile.add(jmiOpenB64);
		// JMenuItem jmiSave      = new JMenuItem(Translatrix.getTranslationString("Save"));                  jmiSave.setActionCommand("save");         jmiSave.addActionListener(this);     jmiSave.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiSave.setIcon(getEkitIcon("Save")); }; jMenuFile.add(jmiSave);
		this.jMenuFile.addSeparator();
		JMenuItem jmiSaveRTF   = new JMenuItem(Translatrix.getTranslationString("SaveRTF") + this.menuDialog);  jmiSaveRTF.setActionCommand("savertf");   jmiSaveRTF.addActionListener(this);  this.jMenuFile.add(jmiSaveRTF);
		JMenuItem jmiSaveAs    = new JMenuItem(Translatrix.getTranslationString("SaveAs") + this.menuDialog);   jmiSaveAs.setActionCommand("saveas");     jmiSaveAs.addActionListener(this);   this.jMenuFile.add(jmiSaveAs);
		// arno : menu sauvegarde Save To Pagod
		// JMenuItem jmiSaveB64   = new JMenuItem(Translatrix.getTranslationString("SaveB64") + menuDialog);  jmiSaveB64.setActionCommand("saveb64");   jmiSaveB64.addActionListener(this);  jMenuFile.add(jmiSaveB64);
		/*
		jMenuFile.addSeparator();
		JMenuItem jmiSerialOut = new JMenuItem(Translatrix.getTranslationString("Serialize") + menuDialog);   jmiSerialOut.setActionCommand("serialize");  jmiSerialOut.addActionListener(this); jMenuFile.add(jmiSerialOut);
		JMenuItem jmiSerialIn  = new JMenuItem(Translatrix.getTranslationString("ReadFromSer") + menuDialog); jmiSerialIn.setActionCommand("readfromser"); jmiSerialIn.addActionListener(this);  jMenuFile.add(jmiSerialIn);
		*/
		this.jMenuFile.addSeparator();
		JMenuItem jmiExit      = new JMenuItem(Translatrix.getTranslationString("Exit")); jmiExit.setActionCommand("exit"); jmiExit.addActionListener(this); this.jMenuFile.add(jmiExit);

		/* EDIT Menu */
		this.jMenuEdit            = new JMenu(Translatrix.getTranslationString("Edit"));
		htMenus.put(KEY_MENU_EDIT, this.jMenuEdit);
		if(this.sysClipboard != null)
		{
			// System Clipboard versions of menu commands
			JMenuItem jmiCut     = new JMenuItem(Translatrix.getTranslationString("Cut"));   jmiCut.setActionCommand("textcut");     jmiCut.addActionListener(this);   jmiCut.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_MASK, false));   if(showMenuIcons) { jmiCut.setIcon(getEkitIcon("Cut")); }     this.jMenuEdit.add(jmiCut);
			JMenuItem jmiCopy    = new JMenuItem(Translatrix.getTranslationString("Copy"));  jmiCopy.setActionCommand("textcopy");   jmiCopy.addActionListener(this);  jmiCopy.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK, false));  if(showMenuIcons) { jmiCopy.setIcon(getEkitIcon("Copy")); }   this.jMenuEdit.add(jmiCopy);
			JMenuItem jmiPaste   = new JMenuItem(Translatrix.getTranslationString("Paste")); jmiPaste.setActionCommand("textpaste"); jmiPaste.addActionListener(this); jmiPaste.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiPaste.setIcon(getEkitIcon("Paste")); } this.jMenuEdit.add(jmiPaste);
		}
		else
		{
			// DefaultEditorKit versions of menu commands
			JMenuItem jmiCut     = new JMenuItem(new DefaultEditorKit.CutAction());   jmiCut.setText(Translatrix.getTranslationString("Cut"));     jmiCut.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_MASK, false));   if(showMenuIcons) { jmiCut.setIcon(getEkitIcon("Cut")); }     this.jMenuEdit.add(jmiCut);
			JMenuItem jmiCopy    = new JMenuItem(new DefaultEditorKit.CopyAction());  jmiCopy.setText(Translatrix.getTranslationString("Copy"));   jmiCopy.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK, false));  if(showMenuIcons) { jmiCopy.setIcon(getEkitIcon("Copy")); }   this.jMenuEdit.add(jmiCopy);
			JMenuItem jmiPaste   = new JMenuItem(new DefaultEditorKit.PasteAction()); jmiPaste.setText(Translatrix.getTranslationString("Paste")); jmiPaste.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiPaste.setIcon(getEkitIcon("Paste")); } this.jMenuEdit.add(jmiPaste);
		}
		this.jMenuEdit.addSeparator();
		JMenuItem jmiUndo    = new JMenuItem(this.undoAction); jmiUndo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiUndo.setIcon(getEkitIcon("Undo")); } this.jMenuEdit.add(jmiUndo);
		JMenuItem jmiRedo    = new JMenuItem(this.redoAction); jmiRedo.setAccelerator(KeyStroke.getKeyStroke('Y', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiRedo.setIcon(getEkitIcon("Redo")); } this.jMenuEdit.add(jmiRedo);
		this.jMenuEdit.addSeparator();
		JMenuItem jmiSelAll  = new JMenuItem((Action)actions.get(DefaultEditorKit.selectAllAction));       jmiSelAll.setText(Translatrix.getTranslationString("SelectAll"));        jmiSelAll.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_MASK, false)); this.jMenuEdit.add(jmiSelAll);
		JMenuItem jmiSelPara = new JMenuItem((Action)actions.get(DefaultEditorKit.selectParagraphAction)); jmiSelPara.setText(Translatrix.getTranslationString("SelectParagraph")); this.jMenuEdit.add(jmiSelPara);
		JMenuItem jmiSelLine = new JMenuItem((Action)actions.get(DefaultEditorKit.selectLineAction));      jmiSelLine.setText(Translatrix.getTranslationString("SelectLine"));      this.jMenuEdit.add(jmiSelLine);
		JMenuItem jmiSelWord = new JMenuItem((Action)actions.get(DefaultEditorKit.selectWordAction));      jmiSelWord.setText(Translatrix.getTranslationString("SelectWord"));      this.jMenuEdit.add(jmiSelWord);

		/* VIEW Menu */
		this.jMenuView = new JMenu(Translatrix.getTranslationString("View"));
		htMenus.put(KEY_MENU_VIEW, this.jMenuView);
		if(includeToolBar)
		{
			if(multiBar)
			{
				this.jMenuToolbars = new JMenu(Translatrix.getTranslationString("ViewToolbars"));

				this.jcbmiViewToolbarMain = new JCheckBoxMenuItem(Translatrix.getTranslationString("ViewToolbarMain"), false);
					this.jcbmiViewToolbarMain.setActionCommand("toggletoolbarmain");
					this.jcbmiViewToolbarMain.addActionListener(this);
					this.jMenuToolbars.add(this.jcbmiViewToolbarMain);

				this.jcbmiViewToolbarFormat = new JCheckBoxMenuItem(Translatrix.getTranslationString("ViewToolbarFormat"), false);
					this.jcbmiViewToolbarFormat.setActionCommand("toggletoolbarformat");
					this.jcbmiViewToolbarFormat.addActionListener(this);
					this.jMenuToolbars.add(this.jcbmiViewToolbarFormat);

				this.jcbmiViewToolbarStyles = new JCheckBoxMenuItem(Translatrix.getTranslationString("ViewToolbarStyles"), false);
					this.jcbmiViewToolbarStyles.setActionCommand("toggletoolbarstyles");
					this.jcbmiViewToolbarStyles.addActionListener(this);
					this.jMenuToolbars.add(this.jcbmiViewToolbarStyles);

				this.jMenuView.add(this.jMenuToolbars);
			}
			else
			{
				this.jcbmiViewToolbar = new JCheckBoxMenuItem(Translatrix.getTranslationString("ViewToolbar"), false);
					this.jcbmiViewToolbar.setActionCommand("toggletoolbar");
					this.jcbmiViewToolbar.addActionListener(this);

				this.jMenuView.add(this.jcbmiViewToolbar);
			}
		}
		this.jcbmiViewSource = new JCheckBoxMenuItem(Translatrix.getTranslationString("ViewSource"), false);  this.jcbmiViewSource.setActionCommand("viewsource");     this.jcbmiViewSource.addActionListener(this);  this.jMenuView.add(this.jcbmiViewSource);

		/* FONT Menu */
		this.jMenuFont              = new JMenu(Translatrix.getTranslationString("Font"));
		htMenus.put(KEY_MENU_FONT, this.jMenuFont);
		JMenuItem jmiBold      = new JMenuItem(this.actionFontBold);      jmiBold.setText(Translatrix.getTranslationString("FontBold"));           jmiBold.setAccelerator(KeyStroke.getKeyStroke('B', KeyEvent.CTRL_MASK, false));      if(showMenuIcons) { jmiBold.setIcon(getEkitIcon("Bold")); }           this.jMenuFont.add(jmiBold);
		JMenuItem jmiItalic    = new JMenuItem(this.actionFontItalic);    jmiItalic.setText(Translatrix.getTranslationString("FontItalic"));       jmiItalic.setAccelerator(KeyStroke.getKeyStroke('I', KeyEvent.CTRL_MASK, false));    if(showMenuIcons) { jmiItalic.setIcon(getEkitIcon("Italic")); }       this.jMenuFont.add(jmiItalic);
		JMenuItem jmiUnderline = new JMenuItem(this.actionFontUnderline); jmiUnderline.setText(Translatrix.getTranslationString("FontUnderline")); jmiUnderline.setAccelerator(KeyStroke.getKeyStroke('U', KeyEvent.CTRL_MASK, false)); if(showMenuIcons) { jmiUnderline.setIcon(getEkitIcon("Underline")); } this.jMenuFont.add(jmiUnderline);
		JMenuItem jmiStrike    = new JMenuItem(this.actionFontStrike);    jmiStrike.setText(Translatrix.getTranslationString("FontStrike"));                                                                                                  if(showMenuIcons) { jmiStrike.setIcon(getEkitIcon("Strike")); }       this.jMenuFont.add(jmiStrike);
		JMenuItem jmiSupscript = new JMenuItem(this.actionFontSuperscript); if(showMenuIcons) { jmiSupscript.setIcon(getEkitIcon("Super")); } this.jMenuFont.add(jmiSupscript);
		JMenuItem jmiSubscript = new JMenuItem(this.actionFontSubscript);   if(showMenuIcons) { jmiSubscript.setIcon(getEkitIcon("Sub")); }   this.jMenuFont.add(jmiSubscript);
		this.jMenuFont.addSeparator();
		this.jMenuFont.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatBig"), HTML.Tag.BIG)));
		this.jMenuFont.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatSmall"), HTML.Tag.SMALL)));
		JMenu jMenuFontSize = new JMenu(Translatrix.getTranslationString("FontSize"));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize1"), 8)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize2"), 10)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize3"), 12)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize4"), 14)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize5"), 18)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize6"), 24)));
			jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(Translatrix.getTranslationString("FontSize7"), 32)));
		this.jMenuFont.add(jMenuFontSize);
		this.jMenuFont.addSeparator();
		JMenu jMenuFontSub      = new JMenu(Translatrix.getTranslationString("Font"));
		JMenuItem jmiSelectFont = new JMenuItem(this.actionSelectFont);                              jmiSelectFont.setText(Translatrix.getTranslationString("FontSelect") + this.menuDialog); if(showMenuIcons) { jmiSelectFont.setIcon(getEkitIcon("FontFaces")); }      jMenuFontSub.add(jmiSelectFont);
		JMenuItem jmiSerif      = new JMenuItem((Action)actions.get("font-family-Serif"));      jmiSerif.setText(Translatrix.getTranslationString("FontSerif"));                    jMenuFontSub.add(jmiSerif);
		JMenuItem jmiSansSerif  = new JMenuItem((Action)actions.get("font-family-SansSerif"));  jmiSansSerif.setText(Translatrix.getTranslationString("FontSansserif"));            jMenuFontSub.add(jmiSansSerif);
		JMenuItem jmiMonospaced = new JMenuItem((Action)actions.get("font-family-Monospaced")); jmiMonospaced.setText(Translatrix.getTranslationString("FontMonospaced"));          jMenuFontSub.add(jmiMonospaced);
		this.jMenuFont.add(jMenuFontSub);
		this.jMenuFont.addSeparator();
		JMenu jMenuFontColor = new JMenu(Translatrix.getTranslationString("Color"));
			Hashtable customAttr = new Hashtable(); customAttr.put("color","black");
			jMenuFontColor.add(new JMenuItem(new CustomAction(this, Translatrix.getTranslationString("CustomColor") + this.menuDialog, HTML.Tag.FONT, customAttr)));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorAqua"),    new Color(  0,255,255))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorBlack"),   new Color(  0,  0,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorBlue"),    new Color(  0,  0,255))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorFuschia"), new Color(255,  0,255))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorGray"),    new Color(128,128,128))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorGreen"),   new Color(  0,128,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorLime"),    new Color(  0,255,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorMaroon"),  new Color(128,  0,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorNavy"),    new Color(  0,  0,128))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorOlive"),   new Color(128,128,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorPurple"),  new Color(128,  0,128))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorRed"),     new Color(255,  0,  0))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorSilver"),  new Color(192,192,192))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorTeal"),    new Color(  0,128,128))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorWhite"),   new Color(255,255,255))));
			jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(Translatrix.getTranslationString("ColorYellow"),  new Color(255,255,  0))));
		this.jMenuFont.add(jMenuFontColor);

		/* FORMAT Menu */
		this.jMenuFormat            = new JMenu(Translatrix.getTranslationString("Format"));
		htMenus.put(KEY_MENU_FORMAT, this.jMenuFormat);
		JMenu jMenuFormatAlign = new JMenu(Translatrix.getTranslationString("Align"));
			JMenuItem jmiAlignLeft = new JMenuItem(this.actionAlignLeft);           if(showMenuIcons) { jmiAlignLeft.setIcon(getEkitIcon("AlignLeft")); }           jMenuFormatAlign.add(jmiAlignLeft);
			JMenuItem jmiAlignCenter = new JMenuItem(this.actionAlignCenter);       if(showMenuIcons) { jmiAlignCenter.setIcon(getEkitIcon("AlignCenter")); }       jMenuFormatAlign.add(jmiAlignCenter);
			JMenuItem jmiAlignRight = new JMenuItem(this.actionAlignRight);         if(showMenuIcons) { jmiAlignRight.setIcon(getEkitIcon("AlignRight")); }         jMenuFormatAlign.add(jmiAlignRight);
			JMenuItem jmiAlignJustified = new JMenuItem(this.actionAlignJustified); if(showMenuIcons) { jmiAlignJustified.setIcon(getEkitIcon("AlignJustified")); } jMenuFormatAlign.add(jmiAlignJustified);
		this.jMenuFormat.add(jMenuFormatAlign);
		this.jMenuFormat.addSeparator();
		JMenu jMenuFormatHeading = new JMenu(Translatrix.getTranslationString("Heading"));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading1"), HTML.Tag.H1)));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading2"), HTML.Tag.H2)));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading3"), HTML.Tag.H3)));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading4"), HTML.Tag.H4)));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading5"), HTML.Tag.H5)));
			jMenuFormatHeading.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("Heading6"), HTML.Tag.H6)));
		this.jMenuFormat.add(jMenuFormatHeading);
		this.jMenuFormat.addSeparator();
		// menu unordered list
		JMenuItem jmiUList = new JMenuItem(this.actionListUnordered); if(showMenuIcons) { jmiUList.setIcon(getEkitIcon("UList")); } this.jMenuFormat.add(jmiUList);
		// this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("ListItem"), HTML.Tag.LI))); // if(showMenuIcons) { jmiUList.setIcon(getEkitIcon("UList")); }
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("ListItem"), HTML.Tag.LI))); // if(showMenuIcons) { jmiUList.setIcon(getEkitIcon("UList")); }
		JMenuItem jmiOList = new JMenuItem(this.actionListOrdered);   if(showMenuIcons) { jmiOList.setIcon(getEkitIcon("OList")); this.jMenuFormat.add(jmiOList);
		this.jMenuFormat.addSeparator();
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatBlockquote"), HTML.Tag.BLOCKQUOTE)));
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatPre"), HTML.Tag.PRE)));
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatStrong"), HTML.Tag.STRONG)));
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatEmphasis"), HTML.Tag.EM)));
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatTT"), HTML.Tag.TT)));
		this.jMenuFormat.add(new JMenuItem(new FormatAction(this, Translatrix.getTranslationString("FormatSpan"), HTML.Tag.SPAN)));

		/* INSERT Menu */
		this.jMenuInsert               = new JMenu(Translatrix.getTranslationString("Insert"));
		htMenus.put(KEY_MENU_INSERT, this.jMenuInsert);
		JMenuItem jmiInsertAnchor = new JMenuItem(this.actionInsertAnchor); if(showMenuIcons) { jmiInsertAnchor.setIcon(getEkitIcon("Anchor")); } this.jMenuInsert.add(jmiInsertAnchor);
		JMenuItem jmiBreak        = new JMenuItem(Translatrix.getTranslationString("InsertBreak"));  jmiBreak.setActionCommand("insertbreak");   jmiBreak.addActionListener(this);   jmiBreak.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK, false)); this.jMenuInsert.add(jmiBreak);
		JMenuItem jmiNBSP         = new JMenuItem(Translatrix.getTranslationString("InsertNBSP"));   jmiNBSP.setActionCommand("insertnbsp");     jmiNBSP.addActionListener(this);    jmiNBSP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.SHIFT_MASK, false)); this.jMenuInsert.add(jmiNBSP);
		JMenu jMenuUnicode        = new JMenu(Translatrix.getTranslationString("InsertUnicodeCharacter")); if(showMenuIcons) { jMenuUnicode.setIcon(getEkitIcon("Unicode")); }
		JMenuItem jmiUnicodeAll   = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterAll") + this.menuDialog);  if(showMenuIcons) { jmiUnicodeAll.setIcon(getEkitIcon("Unicode")); } jmiUnicodeAll.setActionCommand("insertunicode");      jmiUnicodeAll.addActionListener(this);   jMenuUnicode.add(jmiUnicodeAll);
		JMenuItem jmiUnicodeMath  = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterMath") + this.menuDialog); if(showMenuIcons) { jmiUnicodeMath.setIcon(getEkitIcon("Math")); }   jmiUnicodeMath.setActionCommand("insertunicodemath"); jmiUnicodeMath.addActionListener(this);  jMenuUnicode.add(jmiUnicodeMath);
		JMenuItem jmiUnicodeDraw  = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterDraw") + this.menuDialog); if(showMenuIcons) { jmiUnicodeDraw.setIcon(getEkitIcon("Draw")); }   jmiUnicodeDraw.setActionCommand("insertunicodedraw"); jmiUnicodeDraw.addActionListener(this);  jMenuUnicode.add(jmiUnicodeDraw);
		JMenuItem jmiUnicodeDing  = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterDing") + this.menuDialog); jmiUnicodeDing.setActionCommand("insertunicodeding"); jmiUnicodeDing.addActionListener(this);  jMenuUnicode.add(jmiUnicodeDing);
		JMenuItem jmiUnicodeSigs  = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterSigs") + this.menuDialog); jmiUnicodeSigs.setActionCommand("insertunicodesigs"); jmiUnicodeSigs.addActionListener(this);  jMenuUnicode.add(jmiUnicodeSigs);
		JMenuItem jmiUnicodeSpec  = new JMenuItem(Translatrix.getTranslationString("InsertUnicodeCharacterSpec") + this.menuDialog); jmiUnicodeSpec.setActionCommand("insertunicodespec"); jmiUnicodeSpec.addActionListener(this);  jMenuUnicode.add(jmiUnicodeSpec);
		this.jMenuInsert.add(jMenuUnicode);
		JMenuItem jmiHRule        = new JMenuItem((Action)actions.get("InsertHR"));                  jmiHRule.setText(Translatrix.getTranslationString("InsertHorizontalRule")); this.jMenuInsert.add(jmiHRule);
		/*
		jMenuInsert.addSeparator();
		JMenuItem jmiImageLocal   = new JMenuItem(Translatrix.getTranslationString("InsertLocalImage") + menuDialog);  jmiImageLocal.setActionCommand("insertlocalimage"); jmiImageLocal.addActionListener(this); jMenuInsert.add(jmiImageLocal);
		JMenuItem jmiImageURL     = new JMenuItem(Translatrix.getTranslationString("InsertURLImage") + menuDialog);    jmiImageURL.setActionCommand("inserturlimage");     jmiImageURL.addActionListener(this);   jMenuInsert.add(jmiImageURL);
		*/
		
		/* TABLE Menu */
		this.jMenuTable              = new JMenu(Translatrix.getTranslationString("Table"));
		htMenus.put(KEY_MENU_TABLE, this.jMenuTable);
		JMenuItem jmiTable       = new JMenuItem(Translatrix.getTranslationString("InsertTable") + this.menuDialog); if(showMenuIcons) { jmiTable.setIcon(getEkitIcon("TableCreate")); } jmiTable.setActionCommand("inserttable");             jmiTable.addActionListener(this);       this.jMenuTable.add(jmiTable);
		this.jMenuTable.addSeparator();
		JMenuItem jmiTableRow    = new JMenuItem(Translatrix.getTranslationString("InsertTableRow"));           if(showMenuIcons) { jmiTableRow.setIcon(getEkitIcon("InsertRow")); } jmiTableRow.setActionCommand("inserttablerow");       jmiTableRow.addActionListener(this);    this.jMenuTable.add(jmiTableRow);
		JMenuItem jmiTableCol    = new JMenuItem(Translatrix.getTranslationString("InsertTableColumn"));        if(showMenuIcons) { jmiTableCol.setIcon(getEkitIcon("InsertColumn")); } jmiTableCol.setActionCommand("inserttablecolumn");    jmiTableCol.addActionListener(this);    this.jMenuTable.add(jmiTableCol);
		this.jMenuTable.addSeparator();
		JMenuItem jmiTableRowDel = new JMenuItem(Translatrix.getTranslationString("DeleteTableRow"));           if(showMenuIcons) { jmiTableRowDel.setIcon(getEkitIcon("DeleteRow")); } jmiTableRowDel.setActionCommand("deletetablerow");    jmiTableRowDel.addActionListener(this); this.jMenuTable.add(jmiTableRowDel);
		JMenuItem jmiTableColDel = new JMenuItem(Translatrix.getTranslationString("DeleteTableColumn"));        if(showMenuIcons) { jmiTableColDel.setIcon(getEkitIcon("DeleteColumn")); } jmiTableColDel.setActionCommand("deletetablecolumn"); jmiTableColDel.addActionListener(this); this.jMenuTable.add(jmiTableColDel);

		/* FORMS Menu */
		/*
		jMenuForms                    = new JMenu(Translatrix.getTranslationString("Forms"));
		htMenus.put(KEY_MENU_FORMS, jMenuForms);
		JMenuItem jmiFormInsertForm   = new JMenuItem(Translatrix.getTranslationString("FormInsertForm")); jmiFormInsertForm.setActionCommand("insertform");     jmiFormInsertForm.addActionListener(this); jMenuForms.add(jmiFormInsertForm);
		jMenuForms.addSeparator();
		JMenuItem jmiFormTextfield    = new JMenuItem(Translatrix.getTranslationString("FormTextfield"));  jmiFormTextfield.setActionCommand("inserttextfield"); jmiFormTextfield.addActionListener(this);  jMenuForms.add(jmiFormTextfield);
		JMenuItem jmiFormTextarea     = new JMenuItem(Translatrix.getTranslationString("FormTextarea"));   jmiFormTextarea.setActionCommand("inserttextarea");   jmiFormTextarea.addActionListener(this);   jMenuForms.add(jmiFormTextarea);
		JMenuItem jmiFormCheckbox     = new JMenuItem(Translatrix.getTranslationString("FormCheckbox"));   jmiFormCheckbox.setActionCommand("insertcheckbox");   jmiFormCheckbox.addActionListener(this);   jMenuForms.add(jmiFormCheckbox);
		JMenuItem jmiFormRadio        = new JMenuItem(Translatrix.getTranslationString("FormRadio"));      jmiFormRadio.setActionCommand("insertradiobutton");   jmiFormRadio.addActionListener(this);      jMenuForms.add(jmiFormRadio);
		JMenuItem jmiFormPassword     = new JMenuItem(Translatrix.getTranslationString("FormPassword"));   jmiFormPassword.setActionCommand("insertpassword");   jmiFormPassword.addActionListener(this);   jMenuForms.add(jmiFormPassword);
		jMenuForms.addSeparator();
		JMenuItem jmiFormButton       = new JMenuItem(Translatrix.getTranslationString("FormButton"));       jmiFormButton.setActionCommand("insertbutton");             jmiFormButton.addActionListener(this);       jMenuForms.add(jmiFormButton);
		JMenuItem jmiFormButtonSubmit = new JMenuItem(Translatrix.getTranslationString("FormButtonSubmit")); jmiFormButtonSubmit.setActionCommand("insertbuttonsubmit"); jmiFormButtonSubmit.addActionListener(this); jMenuForms.add(jmiFormButtonSubmit);
		JMenuItem jmiFormButtonReset  = new JMenuItem(Translatrix.getTranslationString("FormButtonReset"));  jmiFormButtonReset.setActionCommand("insertbuttonreset");   jmiFormButtonReset.addActionListener(this);  jMenuForms.add(jmiFormButtonReset);
		*/
		/* TOOLS Menu */
		if(hasSpellChecker)
		{
			this.jMenuTools = new JMenu(Translatrix.getTranslationString("Tools"));
			htMenus.put(KEY_MENU_TOOLS, this.jMenuTools);
			JMenuItem jmiSpellcheck = new JMenuItem(Translatrix.getTranslationString("ToolSpellcheck")); jmiSpellcheck.setActionCommand("spellcheck"); jmiSpellcheck.addActionListener(this); this.jMenuTools.add(jmiSpellcheck);
		}

		/* SEARCH Menu */
		this.jMenuSearch            = new JMenu(Translatrix.getTranslationString("Search"));
		htMenus.put(KEY_MENU_SEARCH, this.jMenuSearch);
		JMenuItem jmiFind      = new JMenuItem(Translatrix.getTranslationString("SearchFind"));      if(showMenuIcons) { jmiFind.setIcon(getEkitIcon("Find")); }          jmiFind.setActionCommand("find");           jmiFind.addActionListener(this);      jmiFind.setAccelerator(KeyStroke.getKeyStroke('F', KeyEvent.CTRL_MASK, false));      this.jMenuSearch.add(jmiFind);
		JMenuItem jmiFindAgain = new JMenuItem(Translatrix.getTranslationString("SearchFindAgain")); if(showMenuIcons) { jmiFindAgain.setIcon(getEkitIcon("FindAgain")); } jmiFindAgain.setActionCommand("findagain"); jmiFindAgain.addActionListener(this); jmiFindAgain.setAccelerator(KeyStroke.getKeyStroke('G', KeyEvent.CTRL_MASK, false)); this.jMenuSearch.add(jmiFindAgain);
		JMenuItem jmiReplace   = new JMenuItem(Translatrix.getTranslationString("SearchReplace"));   if(showMenuIcons) { jmiReplace.setIcon(getEkitIcon("Replace")); }     jmiReplace.setActionCommand("replace");     jmiReplace.addActionListener(this);   jmiReplace.setAccelerator(KeyStroke.getKeyStroke('R', KeyEvent.CTRL_MASK, false));   this.jMenuSearch.add(jmiReplace);

		/* HELP Menu */
		this.jMenuHelp = new JMenu(Translatrix.getTranslationString("Help"));
		htMenus.put(KEY_MENU_HELP, this.jMenuHelp);
		JMenuItem jmiAbout = new JMenuItem(Translatrix.getTranslationString("About")); jmiAbout.setActionCommand("helpabout"); jmiAbout.addActionListener(this); this.jMenuHelp.add(jmiAbout);

		/* DEBUG Menu */
		this.jMenuDebug           = new JMenu(Translatrix.getTranslationString("Debug"));
		htMenus.put(KEY_MENU_DEBUG, this.jMenuDebug);
		JMenuItem jmiDesc    = new JMenuItem(Translatrix.getTranslationString("DescribeDoc")); jmiDesc.setActionCommand("describe");       jmiDesc.addActionListener(this);    this.jMenuDebug.add(jmiDesc);
		JMenuItem jmiDescCSS = new JMenuItem(Translatrix.getTranslationString("DescribeCSS")); jmiDescCSS.setActionCommand("describecss"); jmiDescCSS.addActionListener(this); this.jMenuDebug.add(jmiDescCSS);
		JMenuItem jmiTag     = new JMenuItem(Translatrix.getTranslationString("WhatTags"));    jmiTag.setActionCommand("whattags");        jmiTag.addActionListener(this);     this.jMenuDebug.add(jmiTag);

		/* Create menubar and add menus */
		this.jMenuBar = new JMenuBar();
		this.jMenuBar.add(this.jMenuFile);
		this.jMenuBar.add(this.jMenuEdit);
		this.jMenuBar.add(this.jMenuView);
		this.jMenuBar.add(this.jMenuFont);
		this.jMenuBar.add(this.jMenuFormat);
		this.jMenuBar.add(this.jMenuSearch);
		this.jMenuBar.add(this.jMenuInsert);
		this.jMenuBar.add(this.jMenuTable);
		// jMenuBar.add(jMenuForms);
		if(this.jMenuTools != null) { this.jMenuBar.add(this.jMenuTools); }
		this.jMenuBar.add(this.jMenuHelp);
		if(debugMode)
		{
			this.jMenuBar.add(this.jMenuDebug);
		}

		/* Create toolbar tool objects */
		this.jbtnNewHTML = new JButtonNoFocus(getEkitIcon("New"));
			this.jbtnNewHTML.setToolTipText(Translatrix.getTranslationString("NewDocument"));
			this.jbtnNewHTML.setActionCommand("newdoc");
			this.jbtnNewHTML.addActionListener(this);
			htTools.put(KEY_TOOL_NEW, this.jbtnNewHTML);
		/*
		jbtnOpenHTML = new JButtonNoFocus(getEkitIcon("Open"));
			jbtnOpenHTML.setToolTipText(Translatrix.getTranslationString("OpenDocument"));
			jbtnOpenHTML.setActionCommand("openhtml");
			jbtnOpenHTML.addActionListener(this);
			htTools.put(KEY_TOOL_OPEN, jbtnOpenHTML);*/
			
		this.jbtnSaveHTML = new JButtonNoFocus(getEkitIcon("Save"));
			this.jbtnSaveHTML.setToolTipText(Translatrix.getTranslationString("SaveToPagod"));
			// arno : modification action savebody
			this.jbtnSaveHTML.setActionCommand("savebody");
			this.jbtnSaveHTML.addActionListener(this);
			htTools.put(KEY_TOOL_SAVE, this.jbtnSaveHTML);
		this.jbtnCut = new JButtonNoFocus(new DefaultEditorKit.CutAction());
			this.jbtnCut.setIcon(getEkitIcon("Cut"));
			this.jbtnCut.setText(null);
			this.jbtnCut.setToolTipText(Translatrix.getTranslationString("Cut"));
			htTools.put(KEY_TOOL_CUT, this.jbtnCut);
		this.jbtnCopy = new JButtonNoFocus(new DefaultEditorKit.CopyAction());
			this.jbtnCopy.setIcon(getEkitIcon("Copy"));
			this.jbtnCopy.setText(null);
			this.jbtnCopy.setToolTipText(Translatrix.getTranslationString("Copy"));
			htTools.put(KEY_TOOL_COPY, this.jbtnCopy);
		this.jbtnPaste = new JButtonNoFocus(new DefaultEditorKit.PasteAction());
			this.jbtnPaste.setIcon(getEkitIcon("Paste"));
			this.jbtnPaste.setText(null);
			this.jbtnPaste.setToolTipText(Translatrix.getTranslationString("Paste"));
			htTools.put(KEY_TOOL_PASTE, this.jbtnPaste);
		this.jbtnUndo = new JButtonNoFocus(this.undoAction);
			this.jbtnUndo.setIcon(getEkitIcon("Undo"));
			this.jbtnUndo.setText(null);
			this.jbtnUndo.setToolTipText(Translatrix.getTranslationString("Undo"));
			htTools.put(KEY_TOOL_UNDO, this.jbtnUndo);
		this.jbtnRedo = new JButtonNoFocus(this.redoAction);
			this.jbtnRedo.setIcon(getEkitIcon("Redo"));
			this.jbtnRedo.setText(null);
			this.jbtnRedo.setToolTipText(Translatrix.getTranslationString("Redo"));
			htTools.put(KEY_TOOL_REDO, this.jbtnRedo);
		this.jbtnBold = new JButtonNoFocus(this.actionFontBold);
			this.jbtnBold.setIcon(getEkitIcon("Bold"));
			this.jbtnBold.setText(null);
			this.jbtnBold.setToolTipText(Translatrix.getTranslationString("FontBold"));
			htTools.put(KEY_TOOL_BOLD, this.jbtnBold);
		this.jbtnItalic = new JButtonNoFocus(this.actionFontItalic);
			this.jbtnItalic.setIcon(getEkitIcon("Italic"));
			this.jbtnItalic.setText(null);
			this.jbtnItalic.setToolTipText(Translatrix.getTranslationString("FontItalic"));
			htTools.put(KEY_TOOL_ITALIC, this.jbtnItalic);
		this.jbtnUnderline = new JButtonNoFocus(this.actionFontUnderline);
			this.jbtnUnderline.setIcon(getEkitIcon("Underline"));
			this.jbtnUnderline.setText(null);
			this.jbtnUnderline.setToolTipText(Translatrix.getTranslationString("FontUnderline"));
			htTools.put(KEY_TOOL_UNDERLINE, this.jbtnUnderline);
		this.jbtnStrike = new JButtonNoFocus(this.actionFontStrike);
			this.jbtnStrike.setIcon(getEkitIcon("Strike"));
			this.jbtnStrike.setText(null);
			this.jbtnStrike.setToolTipText(Translatrix.getTranslationString("FontStrike"));
			htTools.put(KEY_TOOL_STRIKE, this.jbtnStrike);
		this.jbtnSuperscript = new JButtonNoFocus(this.actionFontSuperscript);
			this.jbtnSuperscript.setIcon(getEkitIcon("Super"));
			this.jbtnSuperscript.setText(null);
			this.jbtnSuperscript.setToolTipText(Translatrix.getTranslationString("FontSuperscript"));
			htTools.put(KEY_TOOL_SUPER, this.jbtnSuperscript);
		this.jbtnSubscript = new JButtonNoFocus(this.actionFontSubscript);
			this.jbtnSubscript.setIcon(getEkitIcon("Sub"));
			this.jbtnSubscript.setText(null);
			this.jbtnSubscript.setToolTipText(Translatrix.getTranslationString("FontSubscript"));
			htTools.put(KEY_TOOL_SUB, this.jbtnSubscript);
		this.jbtnUList = new JButtonNoFocus(this.actionListUnordered);
			this.jbtnUList.setIcon(getEkitIcon("UList"));
			this.jbtnUList.setText(null);
			this.jbtnUList.setToolTipText(Translatrix.getTranslationString("ListUnordered"));
			htTools.put(KEY_TOOL_ULIST, this.jbtnUList);
		this.jbtnOList = new JButtonNoFocus(this.actionListOrdered);
			this.jbtnOList.setIcon(getEkitIcon("OList"));
			this.jbtnOList.setText(null);
			this.jbtnOList.setToolTipText(Translatrix.getTranslationString("ListOrdered"));
			htTools.put(KEY_TOOL_OLIST, this.jbtnOList);
		this.jbtnAlignLeft = new JButtonNoFocus(this.actionAlignLeft);
			this.jbtnAlignLeft.setIcon(getEkitIcon("AlignLeft"));
			this.jbtnAlignLeft.setText(null);
			this.jbtnAlignLeft.setToolTipText(Translatrix.getTranslationString("AlignLeft"));
			htTools.put(KEY_TOOL_ALIGNL, this.jbtnAlignLeft);
		this.jbtnAlignCenter = new JButtonNoFocus(this.actionAlignCenter);
			this.jbtnAlignCenter.setIcon(getEkitIcon("AlignCenter"));
			this.jbtnAlignCenter.setText(null);
			this.jbtnAlignCenter.setToolTipText(Translatrix.getTranslationString("AlignCenter"));
			htTools.put(KEY_TOOL_ALIGNC, this.jbtnAlignCenter);
		this.jbtnAlignRight = new JButtonNoFocus(this.actionAlignRight);
			this.jbtnAlignRight.setIcon(getEkitIcon("AlignRight"));
			this.jbtnAlignRight.setText(null);
			this.jbtnAlignRight.setToolTipText(Translatrix.getTranslationString("AlignRight"));
			htTools.put(KEY_TOOL_ALIGNR, this.jbtnAlignRight);
		this.jbtnAlignJustified = new JButtonNoFocus(this.actionAlignJustified);
			this.jbtnAlignJustified.setIcon(getEkitIcon("AlignJustified"));
			this.jbtnAlignJustified.setText(null);
			this.jbtnAlignJustified.setToolTipText(Translatrix.getTranslationString("AlignJustified"));
			htTools.put(KEY_TOOL_ALIGNJ, this.jbtnAlignJustified);
		this.jbtnUnicode = new JButtonNoFocus();
			this.jbtnUnicode.setActionCommand("insertunicode");
			this.jbtnUnicode.addActionListener(this);
			this.jbtnUnicode.setIcon(getEkitIcon("Unicode"));
			this.jbtnUnicode.setText(null);
			this.jbtnUnicode.setToolTipText(Translatrix.getTranslationString("ToolUnicode"));
			htTools.put(KEY_TOOL_UNICODE, this.jbtnUnicode);
		this.jbtnUnicodeMath = new JButtonNoFocus();
			this.jbtnUnicodeMath.setActionCommand("insertunicodemath");
			this.jbtnUnicodeMath.addActionListener(this);
			this.jbtnUnicodeMath.setIcon(getEkitIcon("Math"));
			this.jbtnUnicodeMath.setText(null);
			this.jbtnUnicodeMath.setToolTipText(Translatrix.getTranslationString("ToolUnicodeMath"));
			htTools.put(KEY_TOOL_UNIMATH, this.jbtnUnicodeMath);
		this.jbtnFind = new JButtonNoFocus();
			this.jbtnFind.setActionCommand("find");
			this.jbtnFind.addActionListener(this);
			this.jbtnFind.setIcon(getEkitIcon("Find"));
			this.jbtnFind.setText(null);
			this.jbtnFind.setToolTipText(Translatrix.getTranslationString("SearchFind"));
			htTools.put(KEY_TOOL_FIND, this.jbtnFind);
		this.jbtnAnchor = new JButtonNoFocus(this.actionInsertAnchor);
			this.jbtnAnchor.setIcon(getEkitIcon("Anchor"));
			this.jbtnAnchor.setText(null);
			this.jbtnAnchor.setToolTipText(Translatrix.getTranslationString("ToolAnchor"));
			htTools.put(KEY_TOOL_ANCHOR, this.jbtnAnchor);
		this.jtbtnViewSource = new JToggleButtonNoFocus(getEkitIcon("Source"));
			this.jtbtnViewSource.setText(null);
			this.jtbtnViewSource.setToolTipText(Translatrix.getTranslationString("ViewSource"));
			this.jtbtnViewSource.setActionCommand("viewsource");
			this.jtbtnViewSource.addActionListener(this);
			this.jtbtnViewSource.setPreferredSize(this.jbtnAnchor.getPreferredSize());
			this.jtbtnViewSource.setMinimumSize(this.jbtnAnchor.getMinimumSize());
			this.jtbtnViewSource.setMaximumSize(this.jbtnAnchor.getMaximumSize());
			htTools.put(KEY_TOOL_SOURCE, this.jtbtnViewSource);
		this.jcmbStyleSelector = new JComboBoxNoFocus();
			this.jcmbStyleSelector.setToolTipText(Translatrix.getTranslationString("PickCSSStyle"));
			this.jcmbStyleSelector.setAction(new StylesAction(this.jcmbStyleSelector));
			htTools.put(KEY_TOOL_STYLES, this.jcmbStyleSelector);
		String[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			Vector vcFontnames = new Vector(fonts.length + 1);
			vcFontnames.add(Translatrix.getTranslationString("SelectorToolFontsDefaultFont"));
			for(int i = 0; i < fonts.length; i++)
			{
				vcFontnames.add(fonts[i]);
			}
			Collections.sort(vcFontnames);
			this.jcmbFontSelector = new JComboBoxNoFocus(vcFontnames);
			this.jcmbFontSelector.setAction(new SetFontFamilyAction(this, "[EKITFONTSELECTOR]"));
			htTools.put(KEY_TOOL_FONTS, this.jcmbFontSelector);

		/* Create the toolbar */
		if(multiBar)
		{
			this.jToolBarMain = new JToolBar(JToolBar.HORIZONTAL);
			this.jToolBarMain.setFloatable(false);
			this.jToolBarFormat = new JToolBar(JToolBar.HORIZONTAL);
			this.jToolBarFormat.setFloatable(false);
			this.jToolBarStyles = new JToolBar(JToolBar.HORIZONTAL);
			this.jToolBarStyles.setFloatable(false);

			initializeMultiToolbars(toolbarSeq);

			// fix the weird size preference of toggle buttons
			this.jtbtnViewSource.setPreferredSize(this.jbtnAnchor.getPreferredSize());
			this.jtbtnViewSource.setMinimumSize(this.jbtnAnchor.getMinimumSize());
			this.jtbtnViewSource.setMaximumSize(this.jbtnAnchor.getMaximumSize());
		}
		else
		{
			this.jToolBar = new JToolBar(JToolBar.HORIZONTAL);
			this.jToolBar.setFloatable(false);

			initializeSingleToolbar(toolbarSeq);

			// fix the weird size preference of toggle buttons
			this.jtbtnViewSource.setPreferredSize(this.jbtnAnchor.getPreferredSize());
			this.jtbtnViewSource.setMinimumSize(this.jbtnAnchor.getMinimumSize());
			this.jtbtnViewSource.setMaximumSize(this.jbtnAnchor.getMaximumSize());
		}

		/* Create the scroll area for the text pane */
		JScrollPane jspViewport = new JScrollPane(this.jtpMain);
		jspViewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspViewport.setPreferredSize(new Dimension(400, 400));
		jspViewport.setMinimumSize(new Dimension(32, 32));

		/* Create the scroll area for the source viewer */
		this.jspSource = new JScrollPane(this.jtpSource);
		this.jspSource.setPreferredSize(new Dimension(400, 100));
		this.jspSource.setMinimumSize(new Dimension(32, 32));

		this.jspltDisplay = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.jspltDisplay.setTopComponent(jspViewport);
		if(showViewSource)
		{
			this.jspltDisplay.setBottomComponent(this.jspSource);
		}
		else
		{
			this.jspltDisplay.setBottomComponent(null);
		}

		this.iSplitPos = this.jspltDisplay.getDividerLocation();

		registerDocumentStyles();

		/* Add the components to the app */
		this.setLayout(new BorderLayout());
		this.add(this.jspltDisplay, BorderLayout.CENTER);
		}
	}

	/** Raw/Base64 Document & Style Sheet URL Constructor (Ideal for EkitApplet)
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	 * @param urlStyleSheet 
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar(s).
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run PagodEditor in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run PagodEditor in.
	  * @param base64            [boolean] Specifies whether the raw document is Base64 encoded or not.
	  * @param hasSpellChecker   [boolean] Specifies whether or not this uses the SpellChecker module
	  * @param multiBar          [boolean] Specifies whether to use multiple toolbars or one big toolbar.
	  * @param toolbarSeq        [String]  Code string specifying the toolbar buttons to show.
	  */
	public PagodEditorCore(String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean hasSpellChecker, boolean multiBar, String toolbarSeq)
	{
		this(null, null, sRawDocument, (StyledDocument)null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, false, hasSpellChecker, multiBar, toolbarSeq);
	}

	/** Empty Constructor
	  */
	public PagodEditorCore()
	{
		this((String)null, (String)null, (String)null, (StyledDocument)null, (URL)null, true, false, true, true, (String)null, (String)null, false, false, false, true, TOOLBAR_DEFAULT_MULTI);
	}

	/* ActionListener method *
	 */
	/**
	 * @param ae 
	 *  
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String command = ae.getActionCommand();
			if(command.equals("newdoc"))
			{
				SimpleInfoDialog sidAsk = new SimpleInfoDialog(this.getFrame(), "", true, Translatrix.getTranslationString("AskNewDocument"), SimpleInfoDialog.QUESTION);
				String decision = sidAsk.getDecisionValue();
				if(decision.equals(Translatrix.getTranslationString("DialogAccept")))
				{
					if(this.styleSheet != null)
					{
						this.htmlDoc = new ExtendedHTMLDocument(this.styleSheet);
					}
					else
					{
						this.htmlDoc = (ExtendedHTMLDocument)(this.htmlKit.createDefaultDocument());
					}
					this.jtpMain.setText("<HTML><BODY></BODY></HTML>");
					this.jtpSource.setText(this.jtpMain.getText());
					registerDocument(this.htmlDoc);
					this.currentFile = null;
					updateTitle();
				}
			}
			else if(command.equals("openhtml"))
			{
				openDocument(null);
			}
			else if(command.equals("opencss"))
			{
				openStyleSheet(null);
			}
			else if(command.equals("openb64"))
			{
				openDocumentBase64(null);
			}
			else if(command.equals("save"))
			{
				writeOut((HTMLDocument)(this.jtpMain.getDocument()), this.currentFile);
				updateTitle();
			}
			else if(command.equals("saveas"))
			{
				writeOut((HTMLDocument)(this.jtpMain.getDocument()), null);
			}
			else if(command.equals("savebody"))
			{
				// arno : sauvegarde
				writeOutFragment((HTMLDocument)(this.jtpMain.getDocument()),"body");
			}
			else if(command.equals("savertf"))
			{
				writeOutRTF((this.jtpMain.getStyledDocument()));
			}
			else if(command.equals("saveb64"))
			{
				writeOutBase64(this.jtpSource.getText());
			}
			else if(command.equals("textcut"))
			{
				if(this.jspSource.isShowing() && this.jtpSource.hasFocus())
				{
					this.jtpSource.cut();
				}
				else
				{
					this.jtpMain.cut();
				}
			}
			else if(command.equals("textcopy"))
			{
				if(this.jspSource.isShowing() && this.jtpSource.hasFocus())
				{
					this.jtpSource.copy();
				}
				else
				{
					this.jtpMain.copy();
				}
			}
			else if(command.equals("textpaste"))
			{
				if(this.jspSource.isShowing() && this.jtpSource.hasFocus())
				{
					this.jtpSource.paste();
				}
				else
				{
					this.jtpMain.paste();
				}
			}
			else if(command.equals("describe"))
			{
				System.out.println("------------DOCUMENT------------");
				System.out.println("Content Type : " + this.jtpMain.getContentType());
				System.out.println("Editor Kit   : " + this.jtpMain.getEditorKit());
				System.out.println("Doc Tree     :");
				System.out.println("");
				describeDocument(this.jtpMain.getStyledDocument());
				System.out.println("--------------------------------");
				System.out.println("");
			}
			else if(command.equals("describecss"))
			{
				System.out.println("-----------STYLESHEET-----------");
				System.out.println("Stylesheet Rules");
				Enumeration rules = this.styleSheet.getStyleNames();
				while(rules.hasMoreElements())
				{
					String ruleName = (String)(rules.nextElement());
					Style styleRule = this.styleSheet.getStyle(ruleName);
					System.out.println(styleRule.toString());
				}
				System.out.println("--------------------------------");
				System.out.println("");
			}
			else if(command.equals("whattags"))
			{
				System.out.println("Caret Position : " + this.jtpMain.getCaretPosition());
				AttributeSet attribSet = this.jtpMain.getCharacterAttributes();
				Enumeration attribs = attribSet.getAttributeNames();
				System.out.println("Attributes     : ");
				while(attribs.hasMoreElements())
				{
					String attribName = attribs.nextElement().toString();
					System.out.println("                 " + attribName + " | " + attribSet.getAttribute(attribName));
				}
			}
			else if(command.equals("toggletoolbar"))
			{
				this.jToolBar.setVisible(this.jcbmiViewToolbar.isSelected());
			}
			else if(command.equals("toggletoolbarmain"))
			{
				this.jToolBarMain.setVisible(this.jcbmiViewToolbarMain.isSelected());
			}
			else if(command.equals("toggletoolbarformat"))
			{
				this.jToolBarFormat.setVisible(this.jcbmiViewToolbarFormat.isSelected());
			}
			else if(command.equals("toggletoolbarstyles"))
			{
				this.jToolBarStyles.setVisible(this.jcbmiViewToolbarStyles.isSelected());
			}
			else if(command.equals("viewsource"))
			{
				toggleSourceWindow();
			}
			else if(command.equals("serialize"))
			{
				serializeOut((HTMLDocument)(this.jtpMain.getDocument()));
			}
			else if(command.equals("readfromser"))
			{
				serializeIn();
			}
			else if(command.equals("inserttable"))
			{
				String[] fieldNames = { "rows", "cols", "border", "cellspacing", "cellpadding", "width" };
				String[] fieldTypes = { "text", "text", "text",   "text",        "text",        "text" };
				insertTable((Hashtable)null, fieldNames, fieldTypes);
			}
			else if(command.equals("inserttablerow"))
			{
				insertTableRow();
			}
			else if(command.equals("inserttablecolumn"))
			{
				insertTableColumn();
			}
			else if(command.equals("deletetablerow"))
			{
				deleteTableRow();
			}
			else if(command.equals("deletetablecolumn"))
			{
				deleteTableColumn();
			}
			else if(command.equals("insertbreak"))
			{
				insertBreak();
			}
			else if(command.equals("insertnbsp"))
			{
				insertNonbreakingSpace();
			}
			else if(command.equals("insertlocalimage"))
			{
				insertLocalImage(null);
			}
			else if(command.equals("inserturlimage"))
			{
				insertURLImage();
			}
			else if(command.equals("insertunicode"))
			{
				insertUnicode(UnicodeDialog.UNICODE_BASE);
			}
			else if(command.equals("insertunicodemath"))
			{
				insertUnicode(UnicodeDialog.UNICODE_MATH);
			}
			else if(command.equals("insertunicodedraw"))
			{
				insertUnicode(UnicodeDialog.UNICODE_DRAW);
			}
			else if(command.equals("insertunicodeding"))
			{
				insertUnicode(UnicodeDialog.UNICODE_DING);
			}
			else if(command.equals("insertunicodesigs"))
			{
				insertUnicode(UnicodeDialog.UNICODE_SIGS);
			}
			else if(command.equals("insertunicodespec"))
			{
				insertUnicode(UnicodeDialog.UNICODE_SPEC);
			}
			else if(command.equals("insertform"))
			{
				String[] fieldNames  = { "name", "method",   "enctype" };
				String[] fieldTypes  = { "text", "combo",    "text" };
				String[] fieldValues = { "",     "POST,GET", "text" };
				insertFormElement(HTML.Tag.FORM, "form", (Hashtable)null, fieldNames, fieldTypes, fieldValues, true);
			}
			else if(command.equals("inserttextfield"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "text");
				String[] fieldNames = { "name", "value", "size", "maxlength" };
				String[] fieldTypes = { "text", "text",  "text", "text" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("inserttextarea"))
			{
				String[] fieldNames = { "name", "rows", "cols" };
				String[] fieldTypes = { "text", "text", "text" };
				insertFormElement(HTML.Tag.TEXTAREA, "textarea", (Hashtable)null, fieldNames, fieldTypes, true);
			}
			else if(command.equals("insertcheckbox"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "checkbox");
				String[] fieldNames = { "name", "checked" };
				String[] fieldTypes = { "text", "bool" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("insertradiobutton"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "radio");
				String[] fieldNames = { "name", "checked" };
				String[] fieldTypes = { "text", "bool" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("insertpassword"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "password");
				String[] fieldNames = { "name", "value", "size", "maxlength" };
				String[] fieldTypes = { "text", "text",  "text", "text" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("insertbutton"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "button");
				String[] fieldNames = { "name", "value" };
				String[] fieldTypes = { "text", "text" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("insertbuttonsubmit"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "submit");
				String[] fieldNames = { "name", "value" };
				String[] fieldTypes = { "text", "text" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("insertbuttonreset"))
			{
				Hashtable htAttribs = new Hashtable();
				htAttribs.put("type", "reset");
				String[] fieldNames = { "name", "value" };
				String[] fieldTypes = { "text", "text" };
				insertFormElement(HTML.Tag.INPUT, "input", htAttribs, fieldNames, fieldTypes, false);
			}
			else if(command.equals("find"))
			{
				doSearch((String)null, (String)null, false, this.lastSearchCaseSetting, this.lastSearchTopSetting);
			}
			else if(command.equals("findagain"))
			{
				doSearch(this.lastSearchFindTerm, (String)null, false, this.lastSearchCaseSetting, false);
			}
			else if(command.equals("replace"))
			{
				doSearch((String)null, (String)null, true, this.lastSearchCaseSetting, this.lastSearchTopSetting);
			}
			else if(command.equals("exit"))
			{
				this.dispose();
				// TODO arno : demande sauvegarde ?
			}
			else if(command.equals("helpabout"))
			{
				// arno / about
				new PagodEditorAboutDialog(this.PAGOD_EDITOR_VERSION);
			}
			else if(command.equals("spellcheck"))
			{
				checkDocumentSpelling(this.jtpMain.getDocument());
			}
		}
		catch(IOException ioe)
		{
			logException("IOException in actionPerformed method", ioe);
			@SuppressWarnings("unused") SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorIOException"), SimpleInfoDialog.ERROR);
		}
		catch(BadLocationException ble)
		{
			logException("BadLocationException in actionPerformed method", ble);
			@SuppressWarnings("unused") SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorBadLocationException"), SimpleInfoDialog.ERROR);
		}
		catch(NumberFormatException nfe)
		{
			logException("NumberFormatException in actionPerformed method", nfe);
			@SuppressWarnings("unused") SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorNumberFormatException"), SimpleInfoDialog.ERROR);
		}
		catch(ClassNotFoundException cnfe)
		{
			logException("ClassNotFound Exception in actionPerformed method", cnfe);
			@SuppressWarnings("unused") SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorClassNotFoundException "), SimpleInfoDialog.ERROR);
		}
		catch(RuntimeException re)
		{
			logException("RuntimeException in actionPerformed method", re);
			@SuppressWarnings("unused") SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorRuntimeException"), SimpleInfoDialog.ERROR);
		}
	}

	/* KeyListener methods */
	/** (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent ke)
	{
		Element elem;
		@SuppressWarnings("unused") String selectedText;
		int pos = this.getCaretPosition();
		int repos = -1;
		if(ke.getKeyChar() == KeyEvent.VK_BACK_SPACE)
		{
			try
			{
				if(pos > 0)
				{
					if((selectedText = this.jtpMain.getSelectedText()) != null)
					{
						this.htmlUtilities.delete();
						return;
					}
					else
					{
						int sOffset = this.htmlDoc.getParagraphElement(pos).getStartOffset();
						if(sOffset == this.jtpMain.getSelectionStart())
						{
							boolean content = true;
							if(this.htmlUtilities.checkParentsTag(HTML.Tag.LI))
							{
								elem = this.htmlUtilities.getListItemParent();
								content = false;
								int so = elem.getStartOffset();
								int eo = elem.getEndOffset();
								if(so + 1 < eo)
								{
									char[] temp = this.jtpMain.getText(so, eo - so).toCharArray();
									for(int i=0; i < temp.length; i++)
									{
										if(!(new Character(temp[i])).isWhitespace(temp[i]))
										{
											content = true;
										}
									}
								}
								if(!content)
								{
									Element listElement = elem.getParentElement();
									this.htmlUtilities.removeTag(elem, true);
									this.setCaretPosition(sOffset - 1);
									return;
								}
								else
								{
									this.jtpMain.setCaretPosition(this.jtpMain.getCaretPosition() - 1);
									this.jtpMain.moveCaretPosition(this.jtpMain.getCaretPosition() - 2);
									this.jtpMain.replaceSelection("");
									return;
								}
							}
							else if(this.htmlUtilities.checkParentsTag(HTML.Tag.TABLE))
							{
								this.jtpMain.setCaretPosition(this.jtpMain.getCaretPosition() - 1);
								ke.consume();
								return;
							}
						}
						this.jtpMain.replaceSelection("");
						return;
					}
				}
			}
			catch (BadLocationException ble)
			{
				logException("BadLocationException in keyTyped method", ble);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorBadLocationException"), SimpleInfoDialog.ERROR);
			}
			catch (IOException ioe)
			{
				logException("IOException in keyTyped method", ioe);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorIOException"), SimpleInfoDialog.ERROR);
			}
		}
		else if(ke.getKeyChar() == KeyEvent.VK_ENTER)
		{
			try
			{
				if(this.htmlUtilities.checkParentsTag(HTML.Tag.UL) == true | this.htmlUtilities.checkParentsTag(HTML.Tag.OL) == true)
				{
					elem = this.htmlUtilities.getListItemParent();
					int so = elem.getStartOffset();
					int eo = elem.getEndOffset();
					char[] temp = this.getTextPane().getText(so,eo-so).toCharArray();
					boolean content = false;
					for(int i=0;i<temp.length;i++)
					{
						if(!(new Character(temp[i])).isWhitespace(temp[i]))
						{
							content = true;
						}
					}
					if(content)
					{
						int end = -1;
						int j = temp.length;
						do
						{
							j--;
							if(new Character(temp[j]).isLetterOrDigit(temp[j]))
							{
								end = j;
							}
						} while (end == -1 && j >= 0);
						j = end;
						do
						{
							j++;
							if(!new Character(temp[j]).isSpaceChar(temp[j]))
							{
								repos = j - end -1;
							}
						} while (repos == -1 && j < temp.length);
						if(repos == -1)
						{
							repos = 0;
						}
					}
					if(elem.getStartOffset() == elem.getEndOffset() || !content)
					{
						manageListElement(elem);
					}
					else
					{
						if(this.getCaretPosition() + 1 == elem.getEndOffset())
						{
							insertListStyle(elem);
							this.setCaretPosition(pos - repos);
						}
						else
						{
							int caret = this.getCaretPosition();
							String tempString = this.getTextPane().getText(caret, eo - caret);
							this.getTextPane().select(caret, eo - 1);
							this.getTextPane().replaceSelection("");
							this.htmlUtilities.insertListElement(tempString);
							Element newLi = this.htmlUtilities.getListItemParent();
							this.setCaretPosition(newLi.getEndOffset() - 1);
						}
					}
				}
			}
			catch (BadLocationException ble)
			{
				logException("BadLocationException in keyTyped method", ble);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorBadLocationException"), SimpleInfoDialog.ERROR);
			}
			catch (IOException ioe)
			{
				logException("IOException in keyTyped method", ioe);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorIOException"), SimpleInfoDialog.ERROR);
			}
		}
	}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	/* FocusListener methods */
	public void focusGained(FocusEvent fe)
	{
		if(fe.getSource() == this.jtpMain)
		{
			setFormattersActive(true);
		}
		else if(fe.getSource() == this.jtpSource)
		{
			setFormattersActive(false);
		}
	}
	public void focusLost(FocusEvent fe) {}

	/* DocumentListener methods */
	public void changedUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void insertUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void removeUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void handleDocumentChange(DocumentEvent de)
	{
		if(!this.exclusiveEdit)
		{
			if(isSourceWindowActive())
			{
				if(de.getDocument() instanceof HTMLDocument || de.getDocument() instanceof ExtendedHTMLDocument)
				{
					this.jtpSource.getDocument().removeDocumentListener(this);
					this.jtpSource.setText(this.jtpMain.getText());
					this.jtpSource.getDocument().addDocumentListener(this);
				}
				else if(de.getDocument() instanceof PlainDocument || de.getDocument() instanceof DefaultStyledDocument)
				{
					this.jtpMain.getDocument().removeDocumentListener(this);
					this.jtpMain.setText(this.jtpSource.getText());
					this.jtpMain.getDocument().addDocumentListener(this);
				}
			}
		}
	}

	/** Method for setting a document as the current document for the text pane
	  * and re-registering the controls and settings for it
	  */
	public void registerDocument(ExtendedHTMLDocument htmlDoc)
	{
		this.jtpMain.setDocument(htmlDoc);
		this.jtpMain.getDocument().addUndoableEditListener(new CustomUndoableEditListener());
		this.jtpMain.getDocument().addDocumentListener(this);
		this.jtpMain.setCaretPosition(0);
		purgeUndos();
		registerDocumentStyles();
	}

	/** Method for locating the available CSS style for the document and adding
	  * them to the styles selector
	  */
	public void registerDocumentStyles()
	{
		if(this.jcmbStyleSelector == null || this.htmlDoc == null)
		{
			return;
		}
		this.jcmbStyleSelector.setEnabled(false);
		this.jcmbStyleSelector.removeAllItems();
		this.jcmbStyleSelector.addItem(Translatrix.getTranslationString("NoCSSStyle"));
		for(Enumeration e = this.htmlDoc.getStyleNames(); e.hasMoreElements();)
		{
			String name = (String) e.nextElement();
			if(name.length() > 0 && name.charAt(0) == '.')
			{
				this.jcmbStyleSelector.addItem(name.substring(1));
			}
		}
		this.jcmbStyleSelector.setEnabled(true);
	}

	/** Method for inserting list elements
	  */
	public void insertListStyle(Element element)
	throws BadLocationException,IOException
	{
		// System.err.println("PagodEditorCore.insertListStyle");
		if(element.getParentElement().getName() == "ol")
		{
			this.actionListOrdered.actionPerformed(new ActionEvent(new Object(), 0, "newListPoint"));
		}
		else
		{
			this.actionListUnordered.actionPerformed(new ActionEvent(new Object(), 0, "newListPoint"));
		}
	}

	/** Method for inserting an HTML Table
	  */
	private void insertTable(Hashtable attribs, String[] fieldNames, String[] fieldTypes)
	throws IOException, BadLocationException, RuntimeException, NumberFormatException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		StringBuffer compositeElement = new StringBuffer("<TABLE");
		if(attribs != null && attribs.size() > 0)
		{
			Enumeration attribEntries = attribs.keys();
			while(attribEntries.hasMoreElements())
			{
				Object entryKey   = attribEntries.nextElement();
				Object entryValue = attribs.get(entryKey);
				if(entryValue != null && entryValue != "")
				{
					compositeElement.append(" " + entryKey + "=" + '"' + entryValue + '"');
				}
			}
		}
		int rows = 0;
		int cols = 0;
		if(fieldNames != null && fieldNames.length > 0)
		{
			PropertiesDialog propertiesDialog = new PropertiesDialog(this.getFrame(), fieldNames, fieldTypes, Translatrix.getTranslationString("FormDialogTitle"), true);
			propertiesDialog.show();
			String decision = propertiesDialog.getDecisionValue();
			if(decision.equals(Translatrix.getTranslationString("DialogCancel")))
			{
				propertiesDialog.dispose();
				propertiesDialog = null;
				return;
			}
			else
			{
				for(int iter = 0; iter < fieldNames.length; iter++)
				{
					String fieldName = fieldNames[iter];
					String propValue = propertiesDialog.getFieldValue(fieldName);
					if(propValue != null && propValue != "" && propValue.length() > 0)
					{
						if(fieldName.equals("rows"))
						{
							rows = Integer.parseInt(propValue);
						}
						else if(fieldName.equals("cols"))
						{
							cols = Integer.parseInt(propValue);
						}
						else
						{
							compositeElement.append(" " + fieldName + "=" + '"' + propValue + '"');
						}
					}
				}
			}
			propertiesDialog.dispose();
			propertiesDialog = null;
		}
		compositeElement.append(">");
		for(int i = 0; i < rows; i++)
		{
			compositeElement.append("<TR>");
			for(int j = 0; j < cols; j++)
			{
				compositeElement.append("<TD></TD>");
			}
			compositeElement.append("</TR>");
		}
		compositeElement.append("</TABLE><P>&nbsp;<P>");
		this.htmlKit.insertHTML(this.htmlDoc, caretPos, compositeElement.toString(), 0, 0, HTML.Tag.TABLE);
		this.jtpMain.setCaretPosition(caretPos + 1);
		refreshOnUpdate();
	}

	/** Method for inserting a row into an HTML Table
	  */
	private void insertTableRow()
	{
		int caretPos = this.jtpMain.getCaretPosition();
		Element	element = this.htmlDoc.getCharacterElement(this.jtpMain.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint  = -1;
		int columnCount = -1;
		while(elementParent != null && !elementParent.getName().equals("body"))
		{
			if(elementParent.getName().equals("tr"))
			{
				startPoint  = elementParent.getStartOffset();
				columnCount = elementParent.getElementCount();
				break;
			}
			else
			{
				elementParent = elementParent.getParentElement();
			}
		}
		if(startPoint > -1 && columnCount > -1)
		{
			this.jtpMain.setCaretPosition(startPoint);
	 		StringBuffer sRow = new StringBuffer();
 			sRow.append("<TR>");
 			for(int i = 0; i < columnCount; i++)
 			{
 				sRow.append("<TD></TD>");
 			}
 			sRow.append("</TR>");
 			ActionEvent actionEvent = new ActionEvent(this.jtpMain, 0, "insertTableRow");
 			new HTMLEditorKit.InsertHTMLTextAction("insertTableRow", sRow.toString(), HTML.Tag.TABLE, HTML.Tag.TR).actionPerformed(actionEvent);
 			refreshOnUpdate();
 			this.jtpMain.setCaretPosition(caretPos);
 		}
	}

	/** Method for inserting a column into an HTML Table
	  */
	private void insertTableColumn()
	{
		int caretPos = this.jtpMain.getCaretPosition();
		Element	element = this.htmlDoc.getCharacterElement(this.jtpMain.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint = -1;
		int rowCount   = -1;
		int cellOffset =  0;
		while(elementParent != null && !elementParent.getName().equals("body"))
		{
			if(elementParent.getName().equals("table"))
			{
				startPoint = elementParent.getStartOffset();
				rowCount   = elementParent.getElementCount();
				break;
			}
			else if(elementParent.getName().equals("tr"))
			{
				int rowStart = elementParent.getStartOffset();
				int rowCells = elementParent.getElementCount();
				for(int i = 0; i < rowCells; i++)
				{
					Element currentCell = elementParent.getElement(i);
					if(this.jtpMain.getCaretPosition() >= currentCell.getStartOffset() && this.jtpMain.getCaretPosition() <= currentCell.getEndOffset())
					{
						cellOffset = i;
					}
				}
				elementParent = elementParent.getParentElement();
			}
			else
			{
				elementParent = elementParent.getParentElement();
			}
		}
		if(startPoint > -1 && rowCount > -1)
		{
			this.jtpMain.setCaretPosition(startPoint);
			String sCell = "<TD></TD>";
			ActionEvent actionEvent = new ActionEvent(this.jtpMain, 0, "insertTableCell");
 			for(int i = 0; i < rowCount; i++)
 			{
 				Element row = elementParent.getElement(i);
 				Element whichCell = row.getElement(cellOffset);
 				this.jtpMain.setCaretPosition(whichCell.getStartOffset());
				new HTMLEditorKit.InsertHTMLTextAction("insertTableCell", sCell, HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.TH, HTML.Tag.TD).actionPerformed(actionEvent);
 			}
 			refreshOnUpdate();
 			this.jtpMain.setCaretPosition(caretPos);
 		}
	}

	/** Method for inserting a cell into an HTML Table
	  */
	private void insertTableCell()
	{
		String sCell = "<TD></TD>";
		ActionEvent actionEvent = new ActionEvent(this.jtpMain, 0, "insertTableCell");
		new HTMLEditorKit.InsertHTMLTextAction("insertTableCell", sCell, HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.TH, HTML.Tag.TD).actionPerformed(actionEvent);
		refreshOnUpdate();
	}

	/** Method for deleting a row from an HTML Table
	  */
	private void deleteTableRow()
	throws BadLocationException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		Element	element = this.htmlDoc.getCharacterElement(this.jtpMain.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint = -1;
		int endPoint   = -1;
		while(elementParent != null && !elementParent.getName().equals("body"))
		{
			if(elementParent.getName().equals("tr"))
			{
				startPoint = elementParent.getStartOffset();
				endPoint   = elementParent.getEndOffset();
				break;
			}
			else
			{
				elementParent = elementParent.getParentElement();
			}
		}
		if(startPoint > -1 && endPoint > startPoint)
		{
			this.htmlDoc.remove(startPoint, endPoint - startPoint);
			this.jtpMain.setDocument(this.htmlDoc);
			registerDocument(this.htmlDoc);
 			refreshOnUpdate();
 			if(caretPos >= this.htmlDoc.getLength())
 			{
 				caretPos = this.htmlDoc.getLength() - 1;
 			}
 			this.jtpMain.setCaretPosition(caretPos);
 		}
	}

	/** Method for deleting a column from an HTML Table
	  */
	private void deleteTableColumn()
	throws BadLocationException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		Element	element       = this.htmlDoc.getCharacterElement(this.jtpMain.getCaretPosition());
		Element elementParent = element.getParentElement();
		Element	elementCell   = (Element)null;
		Element	elementRow    = (Element)null;
		Element	elementTable  = (Element)null;
		// Locate the table, row, and cell location of the cursor
		while(elementParent != null && !elementParent.getName().equals("body"))
		{
			if(elementParent.getName().equals("td"))
			{
				elementCell = elementParent;
			}
			else if(elementParent.getName().equals("tr"))
			{
				elementRow = elementParent;
			}
			else if(elementParent.getName().equals("table"))
			{
				elementTable = elementParent;
			}
			elementParent = elementParent.getParentElement();
		}
		int whichColumn = -1;
		if(elementCell != null && elementRow != null && elementTable != null)
		{
			// Find the column the cursor is in
			for(int i = 0; i < elementRow.getElementCount(); i++)
			{
				if(elementCell == elementRow.getElement(i))
				{
					whichColumn = i;
				}
			}
			if(whichColumn > -1)
			{
				// Iterate through the table rows, deleting cells from the indicated column
				for(int i = 0; i < elementTable.getElementCount(); i++)
				{
					elementRow  = elementTable.getElement(i);
					elementCell = (elementRow.getElementCount() > whichColumn ? elementRow.getElement(whichColumn) : elementRow.getElement(elementRow.getElementCount() - 1));
					int columnCellStart = elementCell.getStartOffset();
					int columnCellEnd   = elementCell.getEndOffset();
					this.htmlDoc.remove(columnCellStart, columnCellEnd - columnCellStart);
				}
				this.jtpMain.setDocument(this.htmlDoc);
				registerDocument(this.htmlDoc);
	 			refreshOnUpdate();
	 			if(caretPos >= this.htmlDoc.getLength())
	 			{
	 				caretPos = this.htmlDoc.getLength() - 1;
	 			}
	 			this.jtpMain.setCaretPosition(caretPos);
			}
		}
	}

	/** Method for inserting a break (BR) element
	  */
	private void insertBreak()
	throws IOException, BadLocationException, RuntimeException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		this.htmlKit.insertHTML(this.htmlDoc, caretPos, "<BR>", 0, 0, HTML.Tag.BR);
		this.jtpMain.setCaretPosition(caretPos + 1);
	}

	/** Method for opening the Unicode dialog
	  */
	private void insertUnicode(int index)
	throws IOException, BadLocationException, RuntimeException
	{
		UnicodeDialog unicodeInput = new UnicodeDialog(this, Translatrix.getTranslationString("UnicodeDialogTitle"), false, index);
	}

	/** Method for inserting Unicode characters via the UnicodeDialog class
	  */
	public void insertUnicodeChar(String sChar)
	throws IOException, BadLocationException, RuntimeException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		if(sChar != null)
		{
			this.htmlDoc.insertString(caretPos, sChar, this.jtpMain.getInputAttributes());
			this.jtpMain.setCaretPosition(caretPos + 1);
		}
	}

	/** Method for inserting a non-breaking space (&nbsp;)
	  */
	private void insertNonbreakingSpace()
	throws IOException, BadLocationException, RuntimeException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		this.htmlDoc.insertString(caretPos, "\240", this.jtpMain.getInputAttributes());
		this.jtpMain.setCaretPosition(caretPos + 1);
	}

	/** Method for inserting a form element
	  */
	private void insertFormElement(HTML.Tag baseTag, String baseElement, Hashtable attribs, String[] fieldNames, String[] fieldTypes, String[] fieldValues, boolean hasClosingTag)
	throws IOException, BadLocationException, RuntimeException
	{
		int caretPos = this.jtpMain.getCaretPosition();
		StringBuffer compositeElement = new StringBuffer("<" + baseElement);
		if(attribs != null && attribs.size() > 0)
		{
			Enumeration attribEntries = attribs.keys();
			while(attribEntries.hasMoreElements())
			{
				Object entryKey   = attribEntries.nextElement();
				Object entryValue = attribs.get(entryKey);
				if(entryValue != null && entryValue != "")
				{
					compositeElement.append(" " + entryKey + "=" + '"' + entryValue + '"');
				}
			}
		}
		if(fieldNames != null && fieldNames.length > 0)
		{
			PropertiesDialog propertiesDialog = new PropertiesDialog(this.getFrame(), fieldNames, fieldTypes, fieldValues, Translatrix.getTranslationString("FormDialogTitle"), true);
			propertiesDialog.show();
			String decision = propertiesDialog.getDecisionValue();
			if(decision.equals(Translatrix.getTranslationString("DialogCancel")))
			{
				propertiesDialog.dispose();
				propertiesDialog = null;
				return;
			}
			else
			{
				for(int iter = 0; iter < fieldNames.length; iter++)
				{
					String fieldName = fieldNames[iter];
					String propValue = propertiesDialog.getFieldValue(fieldName);
					if(propValue != null && propValue.length() > 0)
					{
						if(fieldName.equals("checked"))
						{
							if(propValue.equals("true"))
							{
								compositeElement.append(" " + fieldName + "=" + '"' + propValue + '"');
							}
						}
						else
						{
							compositeElement.append(" " + fieldName + "=" + '"' + propValue + '"');
						}
					}
				}
			}
			propertiesDialog.dispose();
			propertiesDialog = null;
		}
		// --- Convenience for editing, this makes the FORM visible
		if(this.useFormIndicator && baseElement.equals("form"))
		{
			compositeElement.append(" bgcolor=" + '"' + this.clrFormIndicator + '"');
		}
		// --- END
		compositeElement.append(">");
		if(baseTag == HTML.Tag.FORM)
		{
			compositeElement.append("<P>&nbsp;</P>");
			compositeElement.append("<P>&nbsp;</P>");
			compositeElement.append("<P>&nbsp;</P>");
		}
		if(hasClosingTag)
		{
			compositeElement.append("</" + baseElement + ">");
		}
		if(baseTag == HTML.Tag.FORM)
		{
			compositeElement.append("<P>&nbsp;</P>");
		}
		this.htmlKit.insertHTML(this.htmlDoc, caretPos, compositeElement.toString(), 0, 0, baseTag);
		// jtpMain.setCaretPosition(caretPos + 1);
		refreshOnUpdate();
	}

	/** Alternate method call for inserting a form element
	  */
	private void insertFormElement(HTML.Tag baseTag, String baseElement, Hashtable attribs, String[] fieldNames, String[] fieldTypes, boolean hasClosingTag)
	throws IOException, BadLocationException, RuntimeException
	{
		insertFormElement(baseTag, baseElement, attribs, fieldNames, fieldTypes, new String[fieldNames.length], hasClosingTag);
	}

	/** Method that handles initial list insertion and deletion
	  */
	public void manageListElement(Element element)
	{
		Element h = this.htmlUtilities.getListItemParent();
		Element listElement = h.getParentElement();
		if(h != null)
		{
			this.htmlUtilities.removeTag(h, true);
		}
	}

	/** Method to initiate a find/replace operation
	  */
	private void doSearch(String searchFindTerm, String searchReplaceTerm, boolean bIsFindReplace, boolean bCaseSensitive, boolean bStartAtTop)
	{
		boolean bReplaceAll = false;
		JTextComponent searchPane = (JTextComponent)this.jtpMain;
		if(this.jspSource.isShowing() || this.jtpSource.hasFocus())
		{
			searchPane = (JTextComponent)this.jtpSource;
		}
		if(searchFindTerm == null || (bIsFindReplace && searchReplaceTerm == null))
		{
			SearchDialog sdSearchInput = new SearchDialog(this.getFrame(), Translatrix.getTranslationString("SearchDialogTitle"), true, bIsFindReplace, bCaseSensitive, bStartAtTop);
			searchFindTerm    = sdSearchInput.getFindTerm();
			searchReplaceTerm = sdSearchInput.getReplaceTerm();
			bCaseSensitive    = sdSearchInput.getCaseSensitive();
			bStartAtTop       = sdSearchInput.getStartAtTop();
			bReplaceAll       = sdSearchInput.getReplaceAll();
		}
		if(searchFindTerm != null && (!bIsFindReplace || searchReplaceTerm != null))
		{
			if(bReplaceAll)
			{
				int results = findText(searchFindTerm, searchReplaceTerm, bCaseSensitive, 0);
				int findOffset = 0;
				if(results > -1)
				{
					while(results > -1)
					{
						findOffset = findOffset + searchReplaceTerm.length();
						results    = findText(searchFindTerm, searchReplaceTerm, bCaseSensitive, findOffset);
					}
				}
				else
				{
					SimpleInfoDialog sidWarn = new SimpleInfoDialog(this.getFrame(), "", true, Translatrix.getTranslationString("ErrorNoOccurencesFound") + ":\n" + searchFindTerm, SimpleInfoDialog.WARNING);
				}
			}
			else
			{
				int results = findText(searchFindTerm, searchReplaceTerm, bCaseSensitive, (bStartAtTop ? 0 : searchPane.getCaretPosition()));
				if(results == -1)
				{
					SimpleInfoDialog sidWarn = new SimpleInfoDialog(this.getFrame(), "", true, Translatrix.getTranslationString("ErrorNoMatchFound") + ":\n" + searchFindTerm, SimpleInfoDialog.WARNING);
				}
			}
			this.lastSearchFindTerm    = new String(searchFindTerm);
			if(searchReplaceTerm != null)
			{
				this.lastSearchReplaceTerm = new String(searchReplaceTerm);
			}
			else
			{
				this.lastSearchReplaceTerm = (String)null;
			}
			this.lastSearchCaseSetting = bCaseSensitive;
			this.lastSearchTopSetting  = bStartAtTop;
		}
	}

	/** Method for finding (and optionally replacing) a string in the text
	  */
	private int findText(String findTerm, String replaceTerm, boolean bCaseSenstive, int iOffset)
	{
		JTextComponent jtpFindSource;
		if(isSourceWindowActive() || this.jtpSource.hasFocus())
		{
			jtpFindSource = (JTextComponent)this.jtpSource;
		}
		else
		{
			jtpFindSource = (JTextComponent)this.jtpMain;
		}
		int searchPlace = -1;
		try
		{
			Document baseDocument = jtpFindSource.getDocument();
			searchPlace =
				(bCaseSenstive ?
					baseDocument.getText(0, baseDocument.getLength()).indexOf(findTerm, iOffset) :
					baseDocument.getText(0, baseDocument.getLength()).toLowerCase().indexOf(findTerm.toLowerCase(), iOffset)
				);
			if(searchPlace > -1)
			{
				if(replaceTerm != null)
				{
					AttributeSet attribs = null;
					if(baseDocument instanceof HTMLDocument)
					{
						Element element = ((HTMLDocument)baseDocument).getCharacterElement(searchPlace);
						attribs = element.getAttributes();
					}
					baseDocument.remove(searchPlace, findTerm.length());
					baseDocument.insertString(searchPlace, replaceTerm, attribs);
					jtpFindSource.setCaretPosition(searchPlace + replaceTerm.length());
					jtpFindSource.requestFocus();
					jtpFindSource.select(searchPlace, searchPlace + replaceTerm.length());
				}
				else
				{
					jtpFindSource.setCaretPosition(searchPlace + findTerm.length());
					jtpFindSource.requestFocus();
					jtpFindSource.select(searchPlace, searchPlace + findTerm.length());
				}
			}
		}
		catch(BadLocationException ble)
		{
			logException("BadLocationException in actionPerformed method", ble);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(), Translatrix.getTranslationString("Error"), true, Translatrix.getTranslationString("ErrorBadLocationException"), SimpleInfoDialog.ERROR);
		}
		return searchPlace;
	}

	/** Method for inserting an image from a file
	  */
	private void insertLocalImage(File whatImage)
	throws IOException, BadLocationException, RuntimeException
	{
		if(whatImage == null)
		{
			whatImage = getImageFromChooser(this.imageChooserStartDir, this.extsIMG, Translatrix.getTranslationString("FiletypeIMG"));
		}
		if(whatImage != null)
		{
			this.imageChooserStartDir = whatImage.getParent().toString();
			int caretPos = this.jtpMain.getCaretPosition();
			this.htmlKit.insertHTML(this.htmlDoc, caretPos, "<IMG SRC=\"" + whatImage + "\">", 0, 0, HTML.Tag.IMG);
			this.jtpMain.setCaretPosition(caretPos + 1);
			refreshOnUpdate();
		}
	}

	/** Method for inserting an image from a URL
	  */
    public void insertURLImage()
    throws IOException, BadLocationException, RuntimeException
    {
		ImageURLDialog iurlDialog = new ImageURLDialog(this.getFrame(), Translatrix.getTranslationString("ImageURLDialogTitle"), true);
		iurlDialog.pack();
		iurlDialog.setVisible(true);
		String whatImage = iurlDialog.getURL();
		if(whatImage != null)
		{
			int caretPos = this.jtpMain.getCaretPosition();
			this.htmlKit.insertHTML(this.htmlDoc, caretPos, "<IMG SRC=\"" + whatImage + "\">", 0, 0, HTML.Tag.IMG);
			this.jtpMain.setCaretPosition(caretPos + 1);
			refreshOnUpdate();
		}
	}

	/** Empty spell check method, overwritten by spell checker extension class
	  */
	public void checkDocumentSpelling(Document doc) { ; }

	/** Method for saving text as a complete HTML document
	  */
	// arno / export html
	private void writeOut(HTMLDocument doc, File whatFile)
	throws IOException, BadLocationException
	{
		/*
		if(whatFile == null)
		{
			whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG, extsHTML, Translatrix.getTranslationString("FiletypeHTML"));
		}
		if(whatFile != null)
		{
			FileWriter fw = new FileWriter(whatFile);
			htmlKit.write(fw, doc, 0, doc.getLength());
			fw.flush();
			fw.close();
			currentFile = whatFile;
			updateTitle();
		}
		refreshOnUpdate();*/
		// arno
		// Sauvegarde Pagod
		this.step.setComment(doc.toString());
		this.stepsTable.getModel().setValueAt(this.step, this.StepRowNumber, 2);
	}

	/** Method for saving text as an HTML fragment
	  */
	private void writeOutFragment(HTMLDocument doc, String containingTag)
	throws IOException, BadLocationException
	{
		
		String docTextCase = this.jtpSource.getText().toLowerCase();
		int tagStart = docTextCase.indexOf("<" + containingTag.toLowerCase());
		int tagStartClose = docTextCase.indexOf(">", tagStart) + 1;
		String closeTag = "</" + containingTag.toLowerCase() + ">";
		int tagEndOpen = docTextCase.indexOf(closeTag);
		if (tagStartClose < 0)
		{
			tagStartClose = 0;
		}
		if (tagEndOpen < 0 || tagEndOpen > docTextCase.length())
		{
			tagEndOpen = docTextCase.length();
		}
		/*
		String bodyText = this.jtpSource.getText().substring(tagStartClose,
				tagEndOpen);*/
		
		String bodyText = getDocumentBody();
		// arno
		// Sauvegarde Pagod
		this.step.setComment(bodyText);
		this.stepsTable.getModel().setValueAt(bodyText, this.StepRowNumber, 2);
		// this.editorObservable.notifyObservers();
		this.jpanel_source.repaint();
		refreshOnUpdate();
	}

	/** Method for saving text as an RTF document
	  */
	private void writeOutRTF(StyledDocument doc)
	throws IOException, BadLocationException
	{
		File whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG, this.extsRTF, Translatrix.getTranslationString("FiletypeRTF"));
		if(whatFile != null)
		{
			FileOutputStream fos = new FileOutputStream(whatFile);
			RTFEditorKit rtfKit = new RTFEditorKit();
			rtfKit.write(fos, doc, 0, doc.getLength());
			fos.flush();
			fos.close();
		}
		refreshOnUpdate();
	}

	/** Method for saving text as a Base64 encoded document
	  */
	private void writeOutBase64(String text)
	throws IOException, BadLocationException
	{
		File whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG, this.extsB64, Translatrix.getTranslationString("FiletypeB64"));
		if(whatFile != null)
		{
			String base64text = Base64Codec.encode(text);
			FileWriter fw = new FileWriter(whatFile);
			fw.write(base64text, 0, base64text.length());
			fw.flush();
			fw.close();
		}
		refreshOnUpdate();
	}

	/** Method to invoke loading HTML into the app
	  */
	private void openDocument(File whatFile)
	throws IOException, BadLocationException
	{
		if(whatFile == null)
		{
			whatFile = getFileFromChooser(".", JFileChooser.OPEN_DIALOG, this.extsHTML, Translatrix.getTranslationString("FiletypeHTML"));
		}
		if(whatFile != null)
		{
			try
			{
				loadDocument(whatFile, null);
			}
			catch(ChangedCharSetException ccse)
			{
				String charsetType = ccse.getCharSetSpec().toLowerCase();
				int pos = charsetType.indexOf("charset");
				if(pos == -1)
				{
					throw ccse;
				}
				while(pos < charsetType.length() && charsetType.charAt(pos) != '=')
				{
					pos++;
				}
				pos++; // Places file cursor past the equals sign (=)
				String whatEncoding = charsetType.substring(pos).trim();
				loadDocument(whatFile, whatEncoding);
			}
		}
		refreshOnUpdate();
	}

	/** Method for loading HTML document
	  */
	public void loadDocument(File whatFile)
	throws IOException, BadLocationException
	{
		try
		{
			loadDocument(whatFile, null);
		}
		catch(ChangedCharSetException ccse)
		{
			String charsetType = ccse.getCharSetSpec().toLowerCase();
			int pos = charsetType.indexOf("charset");
			if(pos == -1)
			{
				throw ccse;
			}
			while(pos < charsetType.length() && charsetType.charAt(pos) != '=')
			{
				pos++;
			}
			pos++; // Places file cursor past the equals sign (=)
			String whatEncoding = charsetType.substring(pos).trim();
			loadDocument(whatFile, whatEncoding);
		}
		refreshOnUpdate();
	}

	/** Method for loading HTML document into the app, including document encoding setting
	  */
	private void loadDocument(File whatFile, String whatEncoding)
	throws IOException, BadLocationException
	{
		Reader r = null;
		this.htmlDoc = (ExtendedHTMLDocument)(this.htmlKit.createDefaultDocument());
		this.htmlDoc.putProperty("pagod.utils.editor.docsource", whatFile.toString());
		try
		{
			if(whatEncoding == null)
			{
				r = new InputStreamReader(new FileInputStream(whatFile));
			}
			else
			{
				r = new InputStreamReader(new FileInputStream(whatFile), whatEncoding);
				this.htmlDoc.putProperty("IgnoreCharsetDirective", new Boolean(true));
			}
			this.htmlKit.read(r, this.htmlDoc, 0);
			r.close();
			registerDocument(this.htmlDoc);
			this.jtpSource.setText(this.jtpMain.getText());
			this.currentFile = whatFile;
			updateTitle();
		}
		finally
		{
			if(r != null)
			{
				r.close();
			}
		}
	}

	/** Method for loading a Base64 encoded document
	  */
	private void openDocumentBase64(File whatFile)
	throws IOException, BadLocationException
	{
		if(whatFile == null)
		{
			whatFile = getFileFromChooser(".", JFileChooser.OPEN_DIALOG, this.extsB64, Translatrix.getTranslationString("FiletypeB64"));
		}
		if(whatFile != null)
		{
			FileReader fr = new FileReader(whatFile);
			int nextChar = 0;
			StringBuffer encodedText = new StringBuffer();
			try
			{
				while((nextChar = fr.read()) != -1)
				{
					encodedText.append((char)nextChar);
				}
				fr.close();
				this.jtpSource.setText(Base64Codec.decode(encodedText.toString()));
				this.jtpMain.setText(this.jtpSource.getText());
				registerDocument((ExtendedHTMLDocument)(this.jtpMain.getDocument()));
			}
			finally
			{
				if(fr != null)
				{
					fr.close();
				}
			}
		}
	}

	/** Method for loading a Stylesheet into the app
	  */
	private void openStyleSheet(File fileCSS)
	throws IOException
	{
		if(fileCSS == null)
		{
			fileCSS = getFileFromChooser(".", JFileChooser.OPEN_DIALOG, this.extsCSS, Translatrix.getTranslationString("FiletypeCSS"));
		}
		if(fileCSS != null)
		{
			String currDocText = this.jtpMain.getText();
			this.htmlDoc = (ExtendedHTMLDocument)(this.htmlKit.createDefaultDocument());
			this.styleSheet = this.htmlDoc.getStyleSheet();
			URL cssUrl = fileCSS.toURL();
			InputStream is = cssUrl.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			this.styleSheet.loadRules(br, cssUrl);
			br.close();
			this.htmlDoc = new ExtendedHTMLDocument(this.styleSheet);
			registerDocument(this.htmlDoc);
			this.jtpMain.setText(currDocText);
			this.jtpSource.setText(this.jtpMain.getText());
		}
		refreshOnUpdate();
	}

	/** Method for serializing the document out to a file
	  */
	public void serializeOut(HTMLDocument doc)
	throws IOException
	{
		File whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG, this.extsSer, Translatrix.getTranslationString("FiletypeSer"));
		if(whatFile != null)
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(whatFile));
			oos.writeObject(doc);
			oos.flush();
			oos.close();
		}
		refreshOnUpdate();
	}

	/** Method for reading in a serialized document from a file
	  */
	public void serializeIn()
	throws IOException, ClassNotFoundException
	{
		File whatFile = getFileFromChooser(".", JFileChooser.OPEN_DIALOG, this.extsSer, Translatrix.getTranslationString("FiletypeSer"));
		if(whatFile != null)
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(whatFile));
			this.htmlDoc = (ExtendedHTMLDocument)(ois.readObject());
			ois.close();
			registerDocument(this.htmlDoc);
			validate();
		}
		refreshOnUpdate();
	}

	/** Method for obtaining a File for input/output using a JFileChooser dialog
	  */
	private File getFileFromChooser(String startDir, int dialogType, String[] exts, String desc)
	{
		JFileChooser jfileDialog = new JFileChooser(startDir);
		jfileDialog.setDialogType(dialogType);
		jfileDialog.setFileFilter(new MutableFilter(exts, desc));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		if(dialogType == JFileChooser.OPEN_DIALOG)
		{
			optionSelected = jfileDialog.showOpenDialog(this);
		}
		else if(dialogType == JFileChooser.SAVE_DIALOG)
		{
			optionSelected = jfileDialog.showSaveDialog(this);
		}
		else // default to an OPEN_DIALOG
		{
			optionSelected = jfileDialog.showOpenDialog(this);
		}
		if(optionSelected == JFileChooser.APPROVE_OPTION)
		{
			return jfileDialog.getSelectedFile();
		}
		return (File)null;
	}

	/** Method for obtaining an Image for input using a custom JFileChooser dialog
	  */
	private File getImageFromChooser(String startDir, String[] exts, String desc)
	{
		ImageFileChooser jImageDialog = new ImageFileChooser(startDir);
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(exts, desc));
		jImageDialog.setDialogTitle(Translatrix.getTranslationString("ImageDialogTitle"));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(this, Translatrix.getTranslationString("Insert"));
		if(optionSelected == JFileChooser.APPROVE_OPTION)
		{
			return jImageDialog.getSelectedFile();
		}
		return (File)null;
	}

	/** Method for describing the node hierarchy of the document
	  */
	private void describeDocument(StyledDocument doc)
	{
		Element[] elements = doc.getRootElements();
		for(int i = 0; i < elements.length; i++)
		{
			this.indent = this.indentStep;
			for(int j = 0; j < this.indent; j++) { System.out.print(" "); }
			System.out.print(elements[i]);
			traverseElement(elements[i]);
			System.out.println("");
		}
	}

	/** Traverses nodes for the describing method
	  */
	private void traverseElement(Element element)
	{
		this.indent += this.indentStep;
		for(int i = 0; i < element.getElementCount(); i++)
		{
			for(int j = 0; j < this.indent; j++) { System.out.print(" "); }
			System.out.print(element.getElement(i));
			traverseElement(element.getElement(i));
		}
		this.indent -= this.indentStep;
	}

	/** Method to locate a node element by name
	  */
	private Element locateElementInDocument(StyledDocument doc, String elementName)
	{
		Element[] elements = doc.getRootElements();
		for(int i = 0; i < elements.length; i++)
		{
			if(elements[i].getName().equalsIgnoreCase(elementName))
			{
				return elements[i];
			}
			else
			{
				Element rtnElement = locateChildElementInDocument(elements[i], elementName);
				if(rtnElement != null)
				{
					return rtnElement;
				}
			}
		}
		return (Element)null;
	}

	/** Traverses nodes for the locating method
	  */
	private Element locateChildElementInDocument(Element element, String elementName)
	{
		for(int i = 0; i < element.getElementCount(); i++)
		{
			if(element.getElement(i).getName().equalsIgnoreCase(elementName))
			{
				return element.getElement(i);
			}
		}
		return (Element)null;
	}

	/** Convenience method for obtaining the WYSIWYG JTextPane
	  */
	public JTextPane getTextPane()
	{
		return this.jtpMain;
	}

	/** Convenience method for obtaining the Source JTextPane
	  */
	public JTextArea getSourcePane()
	{
		return this.jtpSource;
	}

	/** Convenience method for obtaining the application as a Frame
	  */
	public Frame getFrame()
	{
		return this.frameHandler;
	}

	/** Convenience method for setting the parent Frame
	  */
	public void setFrame(Frame parentFrame)
	{
		this.frameHandler = parentFrame;
	}

	/** Convenience method for obtaining the pre-generated menu bar
	  */
	public JMenuBar getMenuBar()
	{
		return this.jMenuBar;
	}

	/** Convenience method for obtaining a custom menu bar
	  */
	public JMenuBar getCustomMenuBar(Vector vcMenus)
	{
		this.jMenuBar = new JMenuBar();
		for(int i = 0; i < vcMenus.size(); i++)
		{
			String menuToAdd = ((String)(vcMenus.elementAt(i))).toLowerCase();
			if(htMenus.containsKey(menuToAdd))
			{
				this.jMenuBar.add((JMenu)(htMenus.get(menuToAdd)));
			}
		}
		return this.jMenuBar;
	}

	/** Convenience method for creating the multiple toolbar set from a sequence string
	  */
	public void initializeMultiToolbars(String toolbarSeq)
	{
		Vector[] vcToolPicks = new Vector[3];
		vcToolPicks[0] = new Vector();
		vcToolPicks[1] = new Vector();
		vcToolPicks[2] = new Vector();

		int whichBar = 0;
		StringTokenizer stToolbars = new StringTokenizer(toolbarSeq.toUpperCase(), "|");
		while(stToolbars.hasMoreTokens())
		{
			String sKey = stToolbars.nextToken();
			if(sKey.equals("*"))
			{
				whichBar++;
				if(whichBar > 2)
				{
					whichBar = 2;
				}
			}
			else
			{
				vcToolPicks[whichBar].add(sKey);
			}
		}

		customizeToolBar(TOOLBAR_MAIN,   vcToolPicks[0], true);
		customizeToolBar(TOOLBAR_FORMAT, vcToolPicks[1], true);
		customizeToolBar(TOOLBAR_STYLES, vcToolPicks[2], true);
	}

	/** Convenience method for creating the single toolbar from a sequence string
	  */
	public void initializeSingleToolbar(String toolbarSeq)
	{
		Vector vcToolPicks = new Vector();
		StringTokenizer stToolbars = new StringTokenizer(toolbarSeq.toUpperCase(), "|");
		while(stToolbars.hasMoreTokens())
		{
			String sKey = stToolbars.nextToken();
			if(sKey.equals("*"))
			{
				// ignore "next toolbar" symbols in single toolbar processing
			}
			else
			{
				vcToolPicks.add(sKey);
			}
		}

		customizeToolBar(TOOLBAR_SINGLE, vcToolPicks, true);
	}

	/** Convenience method for obtaining the pre-generated toolbar
	  */
	public JToolBar getToolBar(boolean isShowing)
	{
		if(this.jToolBar != null)
		{
			this.jcbmiViewToolbar.setState(isShowing);
			return this.jToolBar;
		}
		return (JToolBar)null;
	}

	/** Convenience method for obtaining the pre-generated main toolbar
	  */
	public JToolBar getToolBarMain(boolean isShowing)
	{
		if(this.jToolBarMain != null)
		{
			this.jcbmiViewToolbarMain.setState(isShowing);
			return this.jToolBarMain;
		}
		return (JToolBar)null;
	}

	/** Convenience method for obtaining the pre-generated main toolbar
	  */
	public JToolBar getToolBarFormat(boolean isShowing)
	{
		if(this.jToolBarFormat != null)
		{
			this.jcbmiViewToolbarFormat.setState(isShowing);
			return this.jToolBarFormat;
		}
		return (JToolBar)null;
	}

	/** Convenience method for obtaining the pre-generated main toolbar
	  */
	public JToolBar getToolBarStyles(boolean isShowing)
	{
		if(this.jToolBarStyles != null)
		{
			this.jcbmiViewToolbarStyles.setState(isShowing);
			return this.jToolBarStyles;
		}
		return (JToolBar)null;
	}

	/** Convenience method for obtaining a custom toolbar
	  */
	public JToolBar customizeToolBar(int whichToolBar, Vector vcTools, boolean isShowing)
	{
		JToolBar jToolBarX = new JToolBar(JToolBar.HORIZONTAL);
		jToolBarX.setFloatable(false);
		for(int i = 0; i < vcTools.size(); i++)
		{
			String toolToAdd = ((String)(vcTools.elementAt(i))).toUpperCase();
			if(toolToAdd.equals(KEY_TOOL_SEP))
			{
				jToolBarX.add(new JToolBar.Separator());
			}
			else if(htTools.containsKey(toolToAdd))
			{
				if(htTools.get(toolToAdd) instanceof JButtonNoFocus)
				{
					jToolBarX.add((JButtonNoFocus)(htTools.get(toolToAdd)));
				}
				else if(htTools.get(toolToAdd) instanceof JToggleButtonNoFocus)
				{
					jToolBarX.add((JToggleButtonNoFocus)(htTools.get(toolToAdd)));
				}
				else if(htTools.get(toolToAdd) instanceof JComboBoxNoFocus)
				{
					jToolBarX.add((JComboBoxNoFocus)(htTools.get(toolToAdd)));
				}
				else
				{
					jToolBarX.add((JComponent)(htTools.get(toolToAdd)));
				}
			}
		}
		if(whichToolBar == TOOLBAR_SINGLE)
		{
			this.jToolBar = jToolBarX;
			this.jToolBar.setVisible(isShowing);
			this.jcbmiViewToolbar.setSelected(isShowing);
			return this.jToolBar;
		}
		else if(whichToolBar == TOOLBAR_MAIN)
		{
			this.jToolBarMain = jToolBarX;
			this.jToolBarMain.setVisible(isShowing);
			this.jcbmiViewToolbarMain.setSelected(isShowing);
			return this.jToolBarMain;
		}
		else if(whichToolBar == TOOLBAR_FORMAT)
		{
			this.jToolBarFormat = jToolBarX;
			this.jToolBarFormat.setVisible(isShowing);
			this.jcbmiViewToolbarFormat.setSelected(isShowing);
			return this.jToolBarFormat;
		}
		else if(whichToolBar == TOOLBAR_STYLES)
		{
			this.jToolBarStyles = jToolBarX;
			this.jToolBarStyles.setVisible(isShowing);
			this.jcbmiViewToolbarStyles.setSelected(isShowing);
			return this.jToolBarStyles;
		}
		else
		{
			this.jToolBarMain = jToolBarX;
			this.jToolBarMain.setVisible(isShowing);
			this.jcbmiViewToolbarMain.setSelected(isShowing);
			return this.jToolBarMain;
		}
	}

	/** Convenience method for activating/deactivating formatting commands
	  * depending on the active editing pane
	  */
	private void setFormattersActive(boolean state)
	{
		this.actionFontBold.setEnabled(state);
		this.actionFontItalic.setEnabled(state);
		this.actionFontUnderline.setEnabled(state);
		this.actionFontStrike.setEnabled(state);
		this.actionFontSuperscript.setEnabled(state);
		this.actionFontSubscript.setEnabled(state);
		this.actionListUnordered.setEnabled(state);
		this.actionListOrdered.setEnabled(state);
		this.actionSelectFont.setEnabled(state);
		this.actionAlignLeft.setEnabled(state);
		this.actionAlignCenter.setEnabled(state);
		this.actionAlignRight.setEnabled(state);
		this.actionAlignJustified.setEnabled(state);
		this.actionInsertAnchor.setEnabled(state);
		this.jbtnUnicode.setEnabled(state);
		this.jbtnUnicodeMath.setEnabled(state);
		this.jcmbStyleSelector.setEnabled(state);
		this.jcmbFontSelector.setEnabled(state);
		this.jMenuFont.setEnabled(state);
		this.jMenuFormat.setEnabled(state);
		this.jMenuInsert.setEnabled(state);
		this.jMenuTable.setEnabled(state);
		// jMenuForms.setEnabled(state);
	}

	/** Convenience method for obtaining the current file handle
	  */
	public File getCurrentFile()
	{
		return this.currentFile;
	}

	/** Convenience method for obtaining the application name
	  */
	public String getAppName()
	{
		return this.appName;
	}

	/** Convenience method for obtaining the document text
	  */
	public String getDocumentText()
	{
		if(isSourceWindowActive())
		{
			return this.jtpSource.getText();
		}
		else
		{
			return this.jtpMain.getText();
		}
	}

	/** Convenience method for obtaining the document text
	  * contained within a tag pair
	  */
	public String getDocumentSubText(String tagBlock)
	{
		return getSubText(tagBlock);
	}

	/** Method for extracting the text within a tag
	  */
	private String getSubText(String containingTag)
	{
		this.jtpSource.setText(this.jtpMain.getText());
		String docTextCase = this.jtpSource.getText().toLowerCase();
		int tagStart       = docTextCase.indexOf("<" + containingTag.toLowerCase());
		int tagStartClose  = docTextCase.indexOf(">", tagStart) + 1;
		String closeTag    = "</" + containingTag.toLowerCase() + ">";
		int tagEndOpen     = docTextCase.indexOf(closeTag);
		if(tagStartClose < 0) { tagStartClose = 0; }
		if(tagEndOpen < 0 || tagEndOpen > docTextCase.length()) { tagEndOpen = docTextCase.length(); }
		return this.jtpSource.getText().substring(tagStartClose, tagEndOpen);
	}

	/** Convenience method for obtaining the document text
		* contained within the BODY tags (a common request)
	  */
	public String getDocumentBody()
	{
		return getSubText("body");
	}

	/** Convenience method for setting the document text
	  */
	public void setDocumentText(String sText)
	{
		this.jtpMain.setText(sText);
		((HTMLEditorKit)(this.jtpMain.getEditorKit())).setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.jtpSource.setText(this.jtpMain.getText());
	}

	/** Convenience method for setting the source document
	  */
	public void setSourceDocument(StyledDocument sDoc)
	{
		this.jtpSource.getDocument().removeDocumentListener(this);
		this.jtpSource.setDocument(sDoc);
		this.jtpSource.getDocument().addDocumentListener(this);
		this.jtpMain.setText(this.jtpSource.getText());
		((HTMLEditorKit)(this.jtpMain.getEditorKit())).setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
	}

	/** Convenience method for communicating the current font selection to the CustomAction class
	  */
	public String getFontNameFromSelector()
	{
		if(this.jcmbFontSelector == null || this.jcmbFontSelector.getSelectedItem().equals(Translatrix.getTranslationString("SelectorToolFontsDefaultFont")))
		{
			return (String)null;
		}
		else
		{
			return this.jcmbFontSelector.getSelectedItem().toString();
		}
	}

	/** Convenience method for obtaining the document text
	  */
	private void updateTitle()
	{
		this.frameHandler.setTitle(this.appName + (this.currentFile == null ? "" : " - " + this.currentFile.getName()));
	}

	/** Convenience method for clearing out the UndoManager
	  */
	public void purgeUndos()
	{
		if(this.undoMngr != null)
		{
			this.undoMngr.discardAllEdits();
			this.undoAction.updateUndoState();
			this.redoAction.updateRedoState();
		}
	}

	/** Convenience method for refreshing and displaying changes
	  */
	public void refreshOnUpdate()
	{
		this.jtpMain.setText(this.jtpMain.getText());
		this.jtpSource.setText(this.jtpMain.getText());
		purgeUndos();
		this.repaint();
	}

	/** Convenience method for deallocating the app resources
	  */
	public void dispose()
	{
		this.frameHandler.dispose();
		// System.exit(0);
	}

	/** Convenience method for fetching icon images from jar file
	  */
	
	private ImageIcon getEkitIcon(String iconName)
	{
		URL imageURL = getClass().getResource("/resources/images/pagodeditoricons/" + iconName + "HK.png");
		if(imageURL != null)
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageURL));
		}
		imageURL = getClass().getResource("/resources/images/pagodeditoricons/" + iconName + "HK.gif");
		if(imageURL != null)
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageURL));
		}
		return (ImageIcon)null;
	}

	/** Convenience method for outputting exceptions
	  */
	private void logException(String internalMessage, Exception e)
	{
		System.err.println(internalMessage);
		e.printStackTrace(System.err);
	}

	/** Convenience method for determining if the source window is active
	  */
	private boolean isSourceWindowActive()
	{
		return (this.jspSource != null && this.jspSource == this.jspltDisplay.getRightComponent());
	}

	/** Method for toggling source window visibility
	  */
	private void toggleSourceWindow()
	{
		if(!(isSourceWindowActive()))
		{
			this.jtpSource.setText(this.jtpMain.getText());
			this.jspltDisplay.setRightComponent(this.jspSource);
			if(this.exclusiveEdit)
			{
				this.jspltDisplay.setDividerLocation(0);
				this.jspltDisplay.setEnabled(false);
				this.jtpSource.requestFocus();
			}
			else
			{
				this.jspltDisplay.setDividerLocation(this.iSplitPos);
				this.jspltDisplay.setEnabled(true);
			}
		}
		else
		{
			this.jtpMain.setText(this.jtpSource.getText());
			this.iSplitPos = this.jspltDisplay.getDividerLocation();
			this.jspltDisplay.remove(this.jspSource);
			this.jtpMain.requestFocus();
		}
		this.validate();
		this.jcbmiViewSource.setSelected(isSourceWindowActive());
		this.jtbtnViewSource.setSelected(isSourceWindowActive());
	}

	/** Searches the specified element for CLASS attribute setting
	  */
	private String findStyle(Element element)
	{
		AttributeSet as = element.getAttributes();
		if(as == null)
		{
			return null;
		}
		Object val = as.getAttribute(HTML.Attribute.CLASS);
		if(val != null && (val instanceof String))
		{
			return (String)val;
		}
		for(Enumeration e = as.getAttributeNames(); e.hasMoreElements();)
		{
			Object key = e.nextElement();
			if(key instanceof HTML.Tag)
			{
				AttributeSet eas = (AttributeSet)(as.getAttribute(key));
				if(eas != null)
				{
					val = eas.getAttribute(HTML.Attribute.CLASS);
					if(val != null)
					{
						return (String)val;
					}
				}
			}

		}
		return null;
	}

	/** Handles caret tracking and related events, such as displaying the current style
	  * of the text under the caret
	  */
	private void handleCaretPositionChange(CaretEvent ce)
	{
		int caretPos = ce.getDot();
		Element	element = this.htmlDoc.getCharacterElement(caretPos);
/*
---- TAG EXPLICATOR CODE -------------------------------------------
		javax.swing.text.ElementIterator ei = new javax.swing.text.ElementIterator(htmlDoc);
		Element ele;
		while((ele = ei.next()) != null)
		{
			System.out.println("ELEMENT : " + ele.getName());
		}
		System.out.println("ELEMENT:" + element.getName());
		Element elementParent = element.getParentElement();
		System.out.println("ATTRS:");
		AttributeSet attribs = elementParent.getAttributes();
		for(Enumeration eAttrs = attribs.getAttributeNames(); eAttrs.hasMoreElements();)
		{
			System.out.println("  " + eAttrs.nextElement().toString());
		}
		while(elementParent != null && !elementParent.getName().equals("body"))
		{
			String parentName = elementParent.getName();
			System.out.println("PARENT:" + parentName);
			System.out.println("ATTRS:");
			attribs = elementParent.getAttributes();
			for(Enumeration eAttr = attribs.getAttributeNames(); eAttr.hasMoreElements();)
			{
				System.out.println("  " + eAttr.nextElement().toString());
			}
			elementParent = elementParent.getParentElement();
		}
---- END -------------------------------------------
*/
		if(this.jtpMain.hasFocus())
		{
			if(element == null)
			{
				return;
			}
			String style = null;
			Vector vcStyles = new Vector();
			while(element != null)
			{
				if(style == null)
				{
					style = findStyle(element);
				}
				vcStyles.add(element);
				element = element.getParentElement();
			}
			int stylefound = -1;
			if(style != null)
			{
				for(int i = 0; i < this.jcmbStyleSelector.getItemCount(); i++)
				{
					String in = (String)(this.jcmbStyleSelector.getItemAt(i));
					if(in.equalsIgnoreCase(style))
					{
						stylefound = i;
						break;
					}
				}
			}
			if(stylefound > -1)
			{
				this.jcmbStyleSelector.getAction().setEnabled(false);
				this.jcmbStyleSelector.setSelectedIndex(stylefound);
				this.jcmbStyleSelector.getAction().setEnabled(true);
			}
			else
			{
				this.jcmbStyleSelector.setSelectedIndex(0);
			}
			// see if current font face is set
			if(this.jcmbFontSelector != null && this.jcmbFontSelector.isVisible())
			{
				AttributeSet mainAttrs = this.jtpMain.getCharacterAttributes();
				Enumeration e = mainAttrs.getAttributeNames();
				Object activeFontName = (Object)(Translatrix.getTranslationString("SelectorToolFontsDefaultFont"));
				while(e.hasMoreElements())
				{
					Object nexte = e.nextElement();
					if(nexte.toString().toLowerCase().equals("face") || nexte.toString().toLowerCase().equals("font-family"))
					{
						activeFontName = mainAttrs.getAttribute(nexte);
						break;
					}
				}
				this.jcmbFontSelector.getAction().setEnabled(false);
				this.jcmbFontSelector.getModel().setSelectedItem(activeFontName);
				this.jcmbFontSelector.getAction().setEnabled(true);
			}
		}
	}

	/** Utility methods
	  */
	public ExtendedHTMLDocument getExtendedHtmlDoc()
	{
		return (ExtendedHTMLDocument)this.htmlDoc;
	}

	public int getCaretPosition()
	{
		return this.jtpMain.getCaretPosition();
	}

	public void setCaretPosition(int newPositon)
	{
		boolean end = true;
		do
		{
			end = true;
			try
			{
				this.jtpMain.setCaretPosition(newPositon);
			}
			catch (IllegalArgumentException iae)
			{
				end = false;
				newPositon--;
			}
		} while(!end && newPositon >= 0);
	}

/* Inner Classes --------------------------------------------- */

	/** Class for implementing Undo as an autonomous action
	  */
	class UndoAction extends AbstractAction
	{
		public UndoAction()
		{
			super(Translatrix.getTranslationString("Undo"));
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				PagodEditorCore.this.undoMngr.undo();
			}
			catch(CannotUndoException ex)
			{
				ex.printStackTrace();
			}
			updateUndoState();
			PagodEditorCore.this.redoAction.updateRedoState();
		}

		protected void updateUndoState()
		{
			setEnabled(PagodEditorCore.this.undoMngr.canUndo());
		}
	}

	/** Class for implementing Redo as an autonomous action
	  */
	class RedoAction extends AbstractAction
	{
		public RedoAction()
		{
			super(Translatrix.getTranslationString("Redo"));
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				PagodEditorCore.this.undoMngr.redo();
			}
			catch(CannotUndoException ex)
			{
				ex.printStackTrace();
			}
			updateRedoState();
			PagodEditorCore.this.undoAction.updateUndoState();
		}

		protected void updateRedoState()
		{
			setEnabled(PagodEditorCore.this.undoMngr.canRedo());
		}
	}

	/** Class for implementing the Undo listener to handle the Undo and Redo actions
	  */
	class CustomUndoableEditListener implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent uee)
		{
			PagodEditorCore.this.undoMngr.addEdit(uee.getEdit());
			PagodEditorCore.this.undoAction.updateUndoState();
			PagodEditorCore.this.redoAction.updateRedoState();
		}
	}

	public StepsTable getStepsTable ()
	{
		return this.stepsTable;
	}

	public void setStepsTable (StepsTable stepsTable)
	{
		this.stepsTable = stepsTable;
	}

	public int getStepRowNumber ()
	{
		return this.StepRowNumber;
	}

	public void setStepRowNumber (int stepRowNumber)
	{
		this.StepRowNumber = stepRowNumber;
	}

	public Step getStep ()
	{
		return this.step;
	}

	public void setStep (Step step)
	{
		this.step = step;
	}

	public JPanel getJpan ()
	{
		return this.jpanel_source;
	}

	public void setJpan (JPanel jpan)
	{
		this.jpanel_source = jpan;
	}
}
