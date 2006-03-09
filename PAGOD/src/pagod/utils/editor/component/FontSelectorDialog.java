/*
GNU Lesser General Public License

FontSelectorDialog
Copyright (C) 2003 Howard Kistler

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

package pagod.utils.editor.component;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import pagod.utils.editor.hexidec.util.Translatrix;


/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class FontSelectorDialog extends JDialog implements ItemListener
{
	private Vector vcFontnames = null;
	private final JComboBox jcmbFontlist;
	private String fontName = new String();
	private JOptionPane jOptionPane;
	private final JTextPane jtpFontPreview;
	private String defaultText;

	/**
	 * @param parent
	 * @param title
	 * @param bModal
	 * @param attribName
	 * @param demoText
	 */
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName, String demoText)
	{
		super(parent, title, bModal);

		if(demoText != null && demoText.length() > 0)
		{
			if(demoText.length() > 24)
			{
				this.defaultText = demoText.substring(0, 24);
			}
			else
			{
				this.defaultText = demoText;
			}
		}
		else
		{
			this.defaultText = "aAbBcCdDeEfFgGhH,.0123";
		}

		/* Obtain available fonts */
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		this.vcFontnames = new Vector(fonts.length - 5);
		for(int i = 0; i < fonts.length; i++)
		{
			if(!fonts[i].equals("Dialog") && !fonts[i].equals("DialogInput") && !fonts[i].equals("Monospaced") && !fonts[i].equals("SansSerif") && !fonts[i].equals("Serif"))
			{
				this.vcFontnames.add(fonts[i]);
			}
		}
		this.jcmbFontlist = new JComboBox(this.vcFontnames);
		this.jcmbFontlist.addItemListener(this);

		this.jtpFontPreview = new JTextPane();
		final HTMLEditorKit kitFontPreview = new HTMLEditorKit();
		final HTMLDocument docFontPreview = (HTMLDocument)(kitFontPreview.createDefaultDocument());
		this.jtpFontPreview.setEditorKit(kitFontPreview);
		this.jtpFontPreview.setDocument(docFontPreview);
		this.jtpFontPreview.setMargin(new Insets(4, 4, 4, 4));
		this.jtpFontPreview.setBounds(0, 0, 120, 18);
		this.jtpFontPreview.setText(getFontSampleString(this.defaultText));
		Object[] panelContents = { attribName, this.jcmbFontlist, Translatrix.getTranslationString("FontSample"), this.jtpFontPreview };
		final Object[] buttonLabels = { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };

		this.jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		setContentPane(this.jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		this.jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() 
					&& (e.getSource() == jOptionPane)
					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					if(value.equals(buttonLabels[0]))
					{
						fontName = (String)(jcmbFontlist.getSelectedItem());
						setVisible(false);
					}
					else
					{
						fontName = null;
						setVisible(false);
					}
				}
			}
		});
		this.pack();
		this.show();
	}

	/* ItemListener method */
	/** (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getStateChange() == ItemEvent.SELECTED)
		{
			this.jtpFontPreview.setText(getFontSampleString(this.defaultText));
		}
	}

	/**
	 * @param parent
	 * @param title
	 * @param bModal
	 * @param attribName
	 */
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	/**
	 * @return string
	 */
	public String getFontName()
	{
		return this.fontName;
	}

	private String getFontSampleString(String demoText)
	{
		return "<HTML><BODY><FONT FACE=" + '"' + this.jcmbFontlist.getSelectedItem() + '"' + ">" + demoText + "</FONT></BODY></HTML>";
	}

}

