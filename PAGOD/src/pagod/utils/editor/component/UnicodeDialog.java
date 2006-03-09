/*
GNU Lesser General Public License

UnicodeDialog
Copyright (C) 2004 Howard Kistler & Michael Pearce

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

/*
Unicode block names and character value ranges taken from data sheets at http://www.unicode.org/charts/
Copyright © 1991-2004 Unicode, Inc. All rights reserved. Distributed under the Terms of Use in http://www.unicode.org/copyright.html.
*/

package pagod.utils.editor.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import pagod.utils.editor.PagodEditorCore;
import pagod.utils.editor.hexidec.util.Translatrix;

/**
 * @author 
 *
 */
public class UnicodeDialog extends JDialog implements ActionListener
{
	/**
	 * Commentaire pour <code>UNICODE_BASE</code>
	 */
	public static final int UNICODE_BASE = 0;
	/**
	 * Commentaire pour <code>UNICODE_SIGS</code>
	 */
	public static final int UNICODE_SIGS = 47;
	/**
	 * Commentaire pour <code>UNICODE_SPEC</code>
	 */
	public static final int UNICODE_SPEC = 48;
	/**
	 * Commentaire pour <code>UNICODE_MATH</code>
	 */
	/**
	 * Commentaire pour <code>UNICODE_MATH</code>
	 */
	public static final int UNICODE_MATH = 49;
	/**
	 * Commentaire pour <code>UNICODE_DRAW</code>
	 */
	public static final int UNICODE_DRAW = 54;
	/**
	 * Commentaire pour <code>UNICODE_DING</code>
	 */
	public static final int UNICODE_DING = 56;

	private static final int    UNICODEBLOCKSIZE  = 256;
	private static final String CMDCHANGEBLOCK    = "changeblock";

	private static final String[] unicodeBlocks =
	{
		"Basic Latin & Latin-1 Supplement",		"Latin Extended-A",		"Latin Extended-B",		"IPA Extensions",		"Spacing Modifier Letters",		"Combining Diacritical Marks",		"Greek and Coptic",		"Cyrillic",		"Cyrillic Supplement",		"Armenian",		"Hebrew",		"Arabic",		"Syriac",		"Thaana",		"Devanagari",		"Bengali",		"Gurmukhi",		"Gujarati",		"Oriya",		"Tamil",		"Telugu",		"Kannada",		"Malayalam",		"Sinhala",		"Thai",		"Lao",		"Tibetan",		"Myanmar",		"Georgian",		"Hanjul Jamo",		"Ethiopic",		"Cherokee",		"Unified Canadian Aboriginal Syllabics",		"Ogham",		"Runic",		"Tagalog",		"Hanunoo",		"Buhid",		"Tagbanwa",		"Khmer",		"Mongolian",		"Limbu",		"Tai Le",		"Khmer Symbols",		"Phonetic Extensions",		"Latin Extended Additional",		"Greek Extended",		"Punctuation / Scripts / Currency / Diacriticals",
		"Letterlike Symbols / Number Forms / Arrows",
		"Mathematical Operators",
		"Miscellaneous Technical",
		"Control Pictures",
		"Optical Character Recognition",
		"Enclosed Alphanumerics",
		"Box Drawing / Block Elements / Geometric Shapes",
		"Miscellaneous Symbols",
		"Dingbats / Math-A / Arrows-A",
		"Braille Patterns",
		"Arrows-B / Math-B",
		"Supplemental Mathematical Operators",
		"Miscellaneous Symbols and Arrows",
		"CJK Radicals Supplement",
		"Kangxi Radicals",
		"Ideographic Description Characters",
		"CJK Symbols and Punctuation",
		"Hiragana",
		"Katakana",
		"Bopomofo",
		"Hangul Compatibility Jamo",
		"Kanbun",
		"Bopomofo Extended",
		"Katakana Phonetic Extensions",
		"Enclosed CJK Letters and Months",
		"CJK Compatibility",
		"CJK Unified Ideographs Extension A",
		"Yijing Hexagram Symbols",
		"CJK Unified Ideographs",
		"Yi Syllables",
		"Yi Radicals",
		"Hangul Symbols",
		"RESERVED AREA: High Surrogates",
		"RESERVED AREA: Low Surrogates",
		"RESERVED AREA: Private Use",
		"CJK Compatibility Ideographs",
		"Alphabetic Presentation Forms",
		"Arabic Presentation Forms-A",
		"Variation Selectors",
		"Combining Half Marks",
		"CJK Compatibility Forms",
		"Small Form Variants",
		"Arabic Presentation Forms-B",
		"Halfwidth and Fullwidth Forms",
		"Specials"
	};

	private final int[] unicodeBlockStart =
	{
		0,
		256,
		384,
		592,
		688,
		768,
		880,
		1024,
		1280,
		1328,
		1424,
		1536,
		1792,
		1920,
		2304,
		2432,
		2560,
		2688,
		2816,
		2944,
		3072,
		3200,
		3328,
		3456,
		3584,
		3712,
		3840,
		4096,
		4256,
		4352,
		4608,
		5024,
		5120,
		5760,
		5792,
		5888,
		5920,
		5952,
		5984,
		6016,
		6144,
		6400,
		6480,
		6624,
		7424,
		7680,
		7936,
		8192,
		8448,
		8704,
		8960,
		9216,
		9280,
		9312,
		9472,
		9728,
		9984,
		10240,
		10496,
		10752,
		11008,
		11904,
		12032,
		12272,
		12288,
		12352,
		12448,
		12544,
		12592,
		12688,
		12704,
		12784,
		12800,
		13056,
		13312,
		19904,
		19968,
		40960,
		42128,
		44032,
		55296,
		56320,
		57344,
		63744,
		64256,
		64336,
		65024,
		65056,
		65072,
		65104,
		65136,
		65280,
		65520
	};

	private final int[] unicodeBlockEnd =
	{
		255,
		383,
		591,
		687,
		767,
		879,
		1023,
		1279,
		1327,
		1423,
		1535,
		1791,
		1871,
		1983,
		2431,
		2559,
		2687,
		2815,
		2943,
		3071,
		3199,
		3327,
		3455,
		3583,
		3711,
		3839,
		4095,
		4255,
		4351,
		4607,
		4991,
		5119,
		5759,
		5791,
		5887,
		5919,
		5951,
		5983,
		6015,
		6143,
		6319,
		6479,
		6527,
		6655,
		7551,
		7935,
		8191,
		8447,
		8703,
		8959,
		9215,
		9279,
		9311,
		9471,
		9727,
		9983,
		10239,
		10495,
		10751,
		11007,
		11263,
		12031,
		12255,
		12287,
		12351,
		12447,
		12543,
		12591,
		12687,
		12703,
		12735,
		12799,
		13055,
		13311,
		19903,
		19967,
		40879,
		42127,
		42191,
		55215,
		56319,
		57343,
		63743,
		64255,
		64335,
		65023,
		65039,
		65071,
		65103,
		65135,
		65279,
		65519,
		65535
	};

/*
	Original Sets (some are combined to make the dialog more usable)
	"Basic Latin & Latin-1 Supplement"
		"Basic Latin"								-> 0-127
		"Latin-1 Supplement"						-> 128-255
	"Punctuation / Scripts / Currency / Diacriticals"
		"General Punctuation"						-> 8192-8303
		"Superscripts and Subscripts"				-> 8304-8351
		"Currency Symbols"							-> 8352-8399
		"Combining Diacritical Marks for Symbols"	-> 8400-8447
	"Letterlike Symbols / Number Forms / Arrows"
		"Letterlike Symbols"						-> 8448-8527
		"Number Forms"								-> 8528-8591
		"Arrows"									-> 8592-8703
	"Box Drawing / Block Elements / Geometric Shapes"
		"Box Drawing" 								-> 9472-9599
		"Block Elements"							-> 9600-9631
		"Geometric Shapes"							-> 9632-9727
	"Dingbats / Math-A / Arrows-A"
		"Dingbats"									-> 9984-10175
		"Miscellaneous Mathematical Symbols-A"		-> 10176-10223
		"Supplemental Arrows-A"						-> 10224-10239
	"Arrows-B / Math-B"
		"Supplemental Arrows-B"						-> 10496-10623
		"Miscellaneous Mathematical Symbols-B"		-> 10624-10751
*/

/*
	Unicode high characters (these are out of range for Java char, which cuts off at 65536)
	"Linear B Syllabary"						-> 65536-65663
	"Linear B Ideograms"						-> 65664-65791
	"Aegean Numbers"							-> 65792-65855
	"Old Italic"								-> 66304-66351
	"Gothic"									-> 66352-66383
	"Ugaritic"									-> 66432-66463
	"Deseret"									-> 66560-66639
	"Shavian"									-> 66640-66687
	"Osmanya"									-> 66688-66735
	"Cypriot Syllabary"							-> 67584-67647
	"Byzantine Musical Symbols"					-> 118784-119039
	"Musical Symbols"							-> 119040-119295
	"Tai Xuan Jing Symbols"						-> 119552-119647
	"Mathematical Alphanumeric Symbols"			-> 119808-120831
	"CJK Unified Ideographic Extension B"		-> 131072-173791
	"CJK Compatibility Ideographs Supplement"	-> 194560-195103
	"Tags"										-> 917504-917631
	"Variations Selector Supplement"			-> 917760-917999
	"Supplementary Private Use Area-A"			-> 983040-1048573
	"Supplementary Private Use Area-B"			-> 1048576-1114109
*/

	private PagodEditorCore parentEkit;
	private Font buttonFont;
	private JToggleButton[] buttonArray = new JToggleButton[UNICODEBLOCKSIZE];
	private ButtonGroup buttonGroup;
	private JComboBox jcmbBlockSelector;
	private JComboBox jcmbPageSelector;

	/**
	 * @param parent
	 * @param title
	 * @param bModal
	 * @param index
	 */
	public UnicodeDialog(PagodEditorCore parent, String title, boolean bModal, int index)
	{
		super(parent.getFrame(), title, bModal);
		this.parentEkit = parent;
		init(index);
	}

	/** (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals(CMDCHANGEBLOCK))
		{
			populateButtons(this.jcmbBlockSelector.getSelectedIndex(), this.jcmbPageSelector.getSelectedIndex());
		}
		else if(ae.getActionCommand().equals("close"))
		{
			setVisible(false);
			this.dispose();
		}
		else if(ae.getActionCommand().equals(""))
		{
			// ignore
		}
		else
		{
			try
			{
				this.parentEkit.insertUnicodeChar(ae.getActionCommand());
			}
			catch(java.io.IOException ioe) { System.out.println("IOException during character insertion : " + ioe.getMessage()); }
			catch(javax.swing.text.BadLocationException ble) { System.out.println("BadLocationException during character insertion : " + ble.getMessage()); }
		}
	}

	/**
	 * @param startIndex
	 */
	public void init(int startIndex)
	{
		String customFont = Translatrix.getTranslationString("UnicodeDialogButtonFont");
		if(customFont != null && customFont.length() > 0)
		{
			this.buttonFont = new Font(Translatrix.getTranslationString("UnicodeDialogButtonFont"), Font.PLAIN, 12);
		}
		else
		{
			this.buttonFont = new Font("Monospaced", Font.PLAIN, 12);
		}

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 17, 0, 0));
		this.buttonGroup = new ButtonGroup();

		int prefButtonWidth  = 32;
		int prefButtonHeight = 32;

		centerPanel.add(new JLabel(""));
		for(int labelLoop = 0; labelLoop < 16; labelLoop++)
		{
			JLabel jlblMarker = new JLabel("x" + (labelLoop > 9 ? "" + (char)(65 + (labelLoop - 10)) : "" + labelLoop));
			jlblMarker.setHorizontalAlignment(SwingConstants.CENTER);
			jlblMarker.setVerticalAlignment(SwingConstants.CENTER);
			jlblMarker.setForeground(new Color(0.5f, 0.5f, 0.75f));
			centerPanel.add(jlblMarker);
		}

		int labelcount  = 0;
		for(int counter = 0; counter < UNICODEBLOCKSIZE; counter++)
		{
			if((counter % 16) == 0)
			{
				JLabel jlblMarker = new JLabel((labelcount > 9 ? "" + (char)(65 + (labelcount - 10)) : "" + labelcount) + "x");
				jlblMarker.setHorizontalAlignment(SwingConstants.CENTER);
				jlblMarker.setVerticalAlignment(SwingConstants.CENTER);
				jlblMarker.setForeground(new Color(0.5f, 0.5f, 0.75f));
				centerPanel.add(jlblMarker);
				labelcount++;
			}
			this.buttonArray[counter] = new JToggleButton(" ");
			this.buttonArray[counter].getModel().setActionCommand("");
			this.buttonArray[counter].setFont(this.buttonFont);
			this.buttonArray[counter].setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			this.buttonArray[counter].addActionListener(this);
			if(counter == 0)
			{
				FontRenderContext frcLocal = ((java.awt.Graphics2D)(this.parentEkit.getGraphics())).getFontRenderContext();
				Rectangle2D fontBounds = this.buttonFont.getMaxCharBounds(frcLocal);
				int maxCharWidth  = (int)(Math.abs(fontBounds.getX())) + (int)(Math.abs(fontBounds.getWidth()));
				int maxCharHeight = (int)(Math.abs(fontBounds.getY())) + (int)(Math.abs(fontBounds.getHeight()));
				Insets buttonInsets = this.buttonArray[counter].getBorder().getBorderInsets(this.buttonArray[counter]);
				prefButtonWidth  = maxCharWidth + buttonInsets.left + buttonInsets.right;
				prefButtonHeight = maxCharHeight + buttonInsets.top + buttonInsets.bottom;
			}
			this.buttonArray[counter].setPreferredSize(new Dimension(prefButtonWidth, prefButtonHeight));
			centerPanel.add(this.buttonArray[counter]);
			this.buttonGroup.add(this.buttonArray[counter]);
		}

		JPanel selectorPanel = new JPanel();

		this.jcmbBlockSelector = new JComboBox(unicodeBlocks);
		this.jcmbBlockSelector.setSelectedIndex(startIndex);
		this.jcmbBlockSelector.setActionCommand(CMDCHANGEBLOCK);
		this.jcmbBlockSelector.addActionListener(this);

		String[] sPages = { "1" };
		this.jcmbPageSelector = new JComboBox(sPages);
		this.jcmbPageSelector.setSelectedIndex(0);
		this.jcmbPageSelector.setActionCommand(CMDCHANGEBLOCK);
		this.jcmbPageSelector.addActionListener(this);

		selectorPanel.add(new JLabel(Translatrix.getTranslationString("SelectorToolUnicodeBlock")));
		selectorPanel.add(this.jcmbBlockSelector);
		selectorPanel.add(new JLabel(Translatrix.getTranslationString("SelectorToolUnicodePage")));
		selectorPanel.add(this.jcmbPageSelector);

		JPanel buttonPanel = new JPanel();

		JButton closeButton = new JButton(Translatrix.getTranslationString("DialogClose"));
		closeButton.setActionCommand("close");
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);

		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(selectorPanel, BorderLayout.NORTH);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		this.pack();

		populateButtons(startIndex, 0);

		this.setVisible(true);
	}

	private void populateButtons(int index, int page)
	{
		int blockPages = ((this.unicodeBlockEnd[index] / UNICODEBLOCKSIZE) - (this.unicodeBlockStart[index] / UNICODEBLOCKSIZE)) + 1;
		if(blockPages != this.jcmbPageSelector.getItemCount())
		{
			this.jcmbPageSelector.setActionCommand("");
			this.jcmbPageSelector.setEnabled(false);
			this.jcmbPageSelector.removeAllItems();
			for(int i = 0; i < blockPages; i++)
			{
				this.jcmbPageSelector.addItem("" + (i + 1));
			}
			this.jcmbPageSelector.setEnabled(true);
			this.jcmbPageSelector.update(this.getGraphics());
			this.jcmbPageSelector.setActionCommand(CMDCHANGEBLOCK);
		}
		if(page > (this.jcmbPageSelector.getItemCount() - 1))
		{
			page = 0;
		}

		int firstInt = ((this.unicodeBlockStart[index] / UNICODEBLOCKSIZE) * UNICODEBLOCKSIZE) + (page * UNICODEBLOCKSIZE);
		int currInt = firstInt;
		for(int charInt = 0; charInt < UNICODEBLOCKSIZE; charInt++)
		{
			currInt = firstInt + charInt;
			this.buttonArray[charInt].setSelected(false);
			if(currInt < this.unicodeBlockStart[index] || currInt > this.unicodeBlockEnd[index])
			{
				this.buttonArray[charInt].setText(" ");
				this.buttonArray[charInt].getModel().setActionCommand(" ");
				this.buttonArray[charInt].setEnabled(false);
				this.buttonArray[charInt].setVisible(false);
			}
			else
			{
				char unichar = (char)currInt;
				String symbol = Character.toString(unichar);
				if(this.buttonFont.canDisplay(unichar))
				{
					this.buttonArray[charInt].setText(symbol);
				}
				else
				{
					this.buttonArray[charInt].setText(" ");
				}
				this.buttonArray[charInt].getModel().setActionCommand(symbol);
				this.buttonArray[charInt].setEnabled(true);
				this.buttonArray[charInt].setVisible(true);
				this.buttonArray[charInt].update(this.getGraphics());
			}
		}
	}
}
