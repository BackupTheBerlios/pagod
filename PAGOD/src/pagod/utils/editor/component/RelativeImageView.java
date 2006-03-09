/*
GNU Lesser General Public License

RelativeImageView
Copyright (C) 2001  Frits Jalvingh & Howard Kistler

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

/**
  * @author <a href="mailto:jal@grimor.com">Frits Jalvingh</a>
  * @version 1.0
  *
  * This code was modeled after an artice on
  * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip109.html">
  * JavaWorld</a> by Bob Kenworthy.
  */

public class RelativeImageView extends View implements ImageObserver, MouseListener, MouseMotionListener
{
	/**
	 * Commentaire pour <code>TOP</code>
	 */
	public static final String TOP       = "top";
	/**
	 * Commentaire pour <code>TEXTTOP</code>
	 */
	public static final String TEXTTOP   = "texttop";
	/**
	 * Commentaire pour <code>MIDDLE</code>
	 */
	public static final String MIDDLE    = "middle";
	/**
	 * Commentaire pour <code>ABSMIDDLE</code>
	 */
	/**
	 * Commentaire pour <code>ABSMIDDLE</code>
	 */
	public static final String ABSMIDDLE = "absmiddle";
	
	/**
	 * Commentaire pour <code>CENTER</code>
	 */
	public static final String CENTER    = "center";
	
	/**
	 * Commentaire pour <code>BOTTOM</code>
	 */
	public static final String BOTTOM    = "bottom";
	/**
	 * Commentaire pour <code>IMAGE_CACHE_PROPERTY</code>
	 */
	public static final String IMAGE_CACHE_PROPERTY = "imageCache";

	private static Icon sPendingImageIcon;
	private static Icon sMissingImageIcon;
	private static final String PENDING_IMAGE_SRC = "icons/ImagePendingHK.gif";
	private static final String MISSING_IMAGE_SRC = "icons/ImageMissingHK.gif";
	private static final int DEFAULT_WIDTH  = 32;
	private static final int DEFAULT_HEIGHT = 32;
	private static final int DEFAULT_BORDER = 1;

	private AttributeSet attr;
	private Element      fElement;
	private Image        fImage;
	private int          fHeight;
	private int          fWidth;
	private Container    fContainer;
	private Rectangle    fBounds;
	private Component    fComponent;
	private Point        fGrowBase; // base of drag while growing image
	private boolean      fGrowProportionally; // should grow be proportional?
	private boolean      bLoading; // set to true while the receiver is locked, to indicate the reciever is loading the image. This is used in imageUpdate.

	/** Constructor
	  * Creates a new view that represents an IMG element.
	  * @param elem the element to create a view for
	  */
	public RelativeImageView(Element elem)
	{
		super(elem);
		initialize(elem);
		StyleSheet sheet = getStyleSheet();
		this.attr = sheet.getViewAttributes(this);
	}

	private void initialize(Element elem)
	{
		synchronized(this)
		{
			this.bLoading = true;
			this.fWidth  = 0;
			this.fHeight = 0;
		}
		int width = 0;
		int height = 0;
		boolean customWidth = false;
		boolean customHeight = false;
		try
		{
			this.fElement = elem;
			// request image from document's cache
			AttributeSet attr = elem.getAttributes();
			if(isURL())
			{
				URL src = getSourceURL();
				if(src != null)
				{
					Dictionary cache = (Dictionary)getDocument().getProperty(IMAGE_CACHE_PROPERTY);
					if(cache != null)
					{
						this.fImage = (Image)cache.get(src);
					}
					else
					{
						this.fImage = Toolkit.getDefaultToolkit().getImage(src);
					}
				}
			}
			else
			{
				// load image from relative path
				String src = (String)this.fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
				src = processSrcPath(src);
				this.fImage = Toolkit.getDefaultToolkit().createImage(src);
				try
				{
					waitForImage();
				}
				catch(InterruptedException ie)
				{
					this.fImage = null;
					// possibly replace with the ImageBroken icon, if that's what is happening
				}
			}

			// get height & width from params or image or defaults
			height = getIntAttr(HTML.Attribute.HEIGHT, -1);
			customHeight = (height > 0);
			if(!customHeight && this.fImage != null)
			{
				height = this.fImage.getHeight(this);
			}
			if(height <= 0)
			{
				height = DEFAULT_HEIGHT;
			}

			width = getIntAttr(HTML.Attribute.WIDTH, -1);
			customWidth = (width > 0);
			if(!customWidth && this.fImage != null)
			{
				width = this.fImage.getWidth(this);
			}
			if(width <= 0)
			{
				width = DEFAULT_WIDTH;
			}

			if(this.fImage != null)
			{
				if(customHeight && customWidth)
				{
					Toolkit.getDefaultToolkit().prepareImage(this.fImage, height, width, this);
				}
				else
				{
					Toolkit.getDefaultToolkit().prepareImage(this.fImage, -1, -1, this);
				}
			}
		}
		finally
		{
			synchronized(this)
			{
				this.bLoading = false;
				if(customHeight || this.fHeight == 0)
				{
					this.fHeight = height;
				}
				if(customWidth || this.fWidth == 0)
				{
					this.fWidth = width;
				}
			}
		}
	}

	/** Determines if path is in the form of a URL
	 * @return bool
	  */
	private boolean isURL()
	{
		String src = (String)this.fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
		return src.toLowerCase().startsWith("file") || src.toLowerCase().startsWith("http");
	}

	/** Checks to see if the absolute path is availabe thru an application
	  * global static variable or thru a system variable. If so, appends
	  * the relative path to the absolute path and returns the String.
	 * @param src 
	 * @return string
	  */
	private String processSrcPath(String src)
	{
		String val = src;
		File imageFile = new File(src);
		if(imageFile.isAbsolute())
		{
			return src;
		}
		boolean	found = false;
		Document doc = getDocument();
		if(doc != null)
		{
			String pv = (String)doc.getProperty("pagod.utils.editor.docsource");
			if(pv != null)
			{
				File f = new File(pv);
				val	= (new File(f.getParent(), imageFile.getPath().toString())).toString();
				found = true;
			}
		}
		if(!found)
		{
			String imagePath = System.getProperty("system.image.path.key");
			if(imagePath != null)
			{
				val = (new File(imagePath, imageFile.getPath())).toString();
			}
		}
		return val;
	}

	/** Method insures that the image is loaded and not a broken reference
	 * @throws InterruptedException 
	  */
	private void waitForImage()
	throws InterruptedException
	{
		int w = this.fImage.getWidth(this);
		int h = this.fImage.getHeight(this);
		while (true)
		{
			int flags = Toolkit.getDefaultToolkit().checkImage(this.fImage, w, h, this);
			if(((flags & ERROR) != 0) || ((flags & ABORT) != 0 ))
			{
				throw new InterruptedException();
			}
			else if((flags & (ALLBITS | FRAMEBITS)) != 0)
			{
				return;
			}
			Thread.sleep(10);
		}
	}

	/** Fetches the attributes to use when rendering. This is
	  * implemented to multiplex the attributes specified in the
	  * model with a StyleSheet.
	 * @return AttributeSet
	  */
	public AttributeSet getAttributes()
	{
		return this.attr;
	}

	/** Method tests whether the image within a link
	 * @return bool
	  */
	boolean isLink()
	{
		AttributeSet anchorAttr = (AttributeSet)this.fElement.getAttributes().getAttribute(HTML.Tag.A);
		if(anchorAttr != null)
		{
			return anchorAttr.isDefined(HTML.Attribute.HREF);
		}
		return false;
	}

	/** Method returns the size of the border to use
	 * @return int
	  */
	int getBorder()
	{
		return getIntAttr(HTML.Attribute.BORDER, isLink() ? DEFAULT_BORDER : 0);
	}

	/** Method returns the amount of extra space to add along an axis
	 * @param axis 
	 * @return int
		*/
	int getSpace(int axis)
	{
		return getIntAttr((axis == X_AXIS) ? HTML.Attribute.HSPACE : HTML.Attribute.VSPACE, 0);
	}

	/** Method returns the border's color, or null if this is not a link
	 * @return color
	*/
	Color getBorderColor()
	{
		StyledDocument doc = (StyledDocument)getDocument();
		return doc.getForeground(getAttributes());
	}

	/** Method returns the image's vertical alignment
	 * @return float
	  */
	float getVerticalAlignment()
	{
		String align = (String)this.fElement.getAttributes().getAttribute(HTML.Attribute.ALIGN);
		if(align != null)
		{
			align = align.toLowerCase();
			if(align.equals(TOP) || align.equals(TEXTTOP))
			{
				return 0.0f;
			}
			else if(align.equals(this.CENTER) || align.equals(MIDDLE) || align.equals(ABSMIDDLE))
			{
				return 0.5f;
			}
		}
		return 1.0f; // default alignment is bottom
	}

	boolean hasPixels(ImageObserver obs)
	{
		return ((this.fImage != null) && (this.fImage.getHeight(obs) > 0) && (this.fImage.getWidth(obs) > 0));
	}

	/** Method returns a URL for the image source, or null if it could not be determined
	 * @return URL
	  */
	private URL getSourceURL()
	{
		String src = (String)this.fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
		if(src == null)
		{
			return null;
		}
		URL reference = ((HTMLDocument)getDocument()).getBase();
		try
		{
			URL u = new URL(reference,src);
			return u;
		}
		catch(MalformedURLException mue)
		{
			return null;
		}
	}

	/** Method looks up an integer-valued attribute (not recursive!)
	 * @param name 
	 * @param iDefault 
	 * @return int
	  */
	private int getIntAttr(HTML.Attribute name, int iDefault)
	{
		AttributeSet attr = this.fElement.getAttributes();
		if(attr.isDefined(name))
		{
			int i;
			String val = (String)attr.getAttribute(name);
			if(val == null)
			{
				i = iDefault;
			}
			else
			{
				try
				{
					i = Math.max(0, Integer.parseInt(val));
				}
				catch(NumberFormatException nfe)
				{
					i = iDefault;
				}
			}
			return i;
		}
		else
		{
			return iDefault;
		}
	}

	/**
	* Establishes the parent view for this view.
	* Seize this moment to cache the AWT Container I'm in.
	 * @param parent 
	*/
	public void setParent(View parent)
	{
		super.setParent(parent);
		this.fContainer = ((parent != null) ? getContainer() : null);
		if((parent == null) && (this.fComponent != null))
		{
			this.fComponent.getParent().remove(this.fComponent);
			this.fComponent = null;
		}
	}

	/** My attributes may have changed. 
	 * @param e 
	 * @param a 
	 * @param f */
	public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f)
	{
		super.changedUpdate(e, a, f);
		float align = getVerticalAlignment();

		int height = this.fHeight;
		int width  = this.fWidth;

		initialize(getElement());

		boolean hChanged = this.fHeight != height;
		boolean wChanged = this.fWidth != width;
		if(hChanged || wChanged || getVerticalAlignment() != align)
		{
			getParent().preferenceChanged(this, hChanged, wChanged);
		}
	}


	/**
	  * Paints the image.
	  *
	  * @param g the rendering surface to use
	  * @param a the allocated region to render into
	  * @see View#paint
	  */
	public void paint(Graphics g, Shape a)
	{
		Color oldColor = g.getColor();
		this.fBounds = a.getBounds();
		int border = getBorder();
		int x = this.fBounds.x + border + getSpace(X_AXIS);
		int y = this.fBounds.y + border + getSpace(Y_AXIS);
		int width = this.fWidth;
		int height = this.fHeight;
		int sel = getSelectionState();

		// If no pixels yet, draw gray outline and icon
		if(!hasPixels(this))
		{
			g.setColor(Color.lightGray);
			g.drawRect(x, y, width - 1, height - 1);
			g.setColor(oldColor);
			loadImageStatusIcons();
			Icon icon = ((this.fImage == null) ? sMissingImageIcon : sPendingImageIcon);
			if(icon != null)
			{
				icon.paintIcon(getContainer(), g, x, y);
			}
		}

		// Draw image
		if(this.fImage != null)
		{
			g.drawImage(this.fImage, x, y, width, height, this);
		}

		// If selected exactly, we need a black border & grow-box
		Color bc = getBorderColor();
		if(sel == 2)
		{
			// Make sure there's room for a border
			int delta = 2 - border;
			if(delta > 0)
			{
				x += delta;
				y += delta;
				width -= delta << 1;
				height -= delta << 1;
				border = 2;
			}
			bc = null;
			g.setColor(Color.black);
			// Draw grow box
			g.fillRect(x + width - 5, y + height - 5, 5, 5);
		}

		// Draw border
		if(border > 0)
		{
			if(bc != null)
			{
				g.setColor(bc);
			}
			// Draw a thick rectangle:
			for(int i = 1; i <= border; i++)
			{
				g.drawRect(x - i, y - i, width - 1 + i + i, height - 1 + i + i);
			}
			g.setColor(oldColor);
		}
	}

	/** Request that this view be repainted. Assumes the view is still at its last-drawn location.
	 * @param delay 
	  */
	protected void repaint(long delay)
	{
		if((this.fContainer != null) && (this.fBounds != null))
		{
			this.fContainer.repaint(delay, this.fBounds.x, this.fBounds.y, this.fBounds.width, this.fBounds.height);
		}
	}

	/**
	  * Determines whether the image is selected, and if it's the only thing selected.
	  * @return  0 if not selected, 1 if selected, 2 if exclusively selected.
	  * "Exclusive" selection is only returned when editable.
	  */
	protected int getSelectionState()
	{
		int p0 = this.fElement.getStartOffset();
		int p1 = this.fElement.getEndOffset();
		if(this.fContainer instanceof JTextComponent)
		{
			JTextComponent textComp = (JTextComponent)this.fContainer;
			int start = textComp.getSelectionStart();
			int end = textComp.getSelectionEnd();
			if((start <= p0) && (end >= p1))
			{
				if((start == p0) && (end == p1) && isEditable())
				{
					return 2;
				}
				else
				{
					return 1;
				}
			}
		}
		return 0;
	}

	protected boolean isEditable()
	{
		return ((this.fContainer instanceof JEditorPane) && ((JEditorPane)this.fContainer).isEditable());
	}

	/** Returns the text editor's highlight color.
	 * @return color
	  */
	protected Color getHighlightColor()
	{
		JTextComponent textComp = (JTextComponent)this.fContainer;
		return textComp.getSelectionColor();
	}

	// Progressive display -------------------------------------------------

	// This can come on any thread. If we are in the process of reloading
	// the image and determining our state (loading == true) we don't fire
	// preference changed, or repaint, we just reset the fWidth/fHeight as
	// necessary and return. This is ok as we know when loading finishes
	// it will pick up the new height/width, if necessary.

	private static boolean sIsInc = true;
	private static int sIncRate = 100;

	/** (non-Javadoc)
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
	public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height)
	{
		if((this.fImage == null) || (this.fImage != img))
		{
			return false;
		}

		// Bail out if there was an error
		if((flags & (ABORT|ERROR)) != 0)
		{
			this.fImage = null;
			repaint(0);
			return false;
		}

		// Resize image if necessary
		short changed = 0;
		if((flags & ImageObserver.HEIGHT) != 0)
		{
			if(!getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT))
			{
				changed |= 1;
			}
		}
		if((flags & ImageObserver.WIDTH) != 0)
		{
			if(!getElement().getAttributes().isDefined(HTML.Attribute.WIDTH))
			{
				changed |= 2;
			}
		}

		synchronized(this)
		{
			if((changed & 1) == 1)
			{
				this.fWidth = width;
			}
			if((changed & 2) == 2)
			{
				this.fHeight = height;
			}
			if(this.bLoading)
			{
				// No need to resize or repaint, still in the process of loading
				return true;
			}
		}

		if(changed != 0)
		{
			// May need to resize myself, asynchronously
			Document doc = getDocument();
			try
			{
				if(doc instanceof AbstractDocument)
				{
					((AbstractDocument)doc).readLock();
				}
				preferenceChanged(this, true, true);
			}
			finally
			{
				if(doc instanceof AbstractDocument)
				{
					((AbstractDocument)doc).readUnlock();
				}
			}
			return true;
		}

		// Repaint when done or when new pixels arrive
		if((flags & (FRAMEBITS|ALLBITS)) != 0)
		{
			repaint(0);
		}
		else if((flags & SOMEBITS) != 0)
		{
			if(sIsInc)
			{
				repaint(sIncRate);
			}
		}
		return ((flags & ALLBITS) == 0);
	}

	// Layout --------------------------------------------------------------

	/** Determines the preferred span for this view along an axis.
	  *
	  * @param axis may be either X_AXIS or Y_AXIS
	 * @return float
	  * @returns  the span the view would like to be rendered into.
	  *           Typically the view is told to render into the span
	  *           that is returned, although there is no guarantee.
	  *           The parent may choose to resize or break the view.
	  */
	public float getPreferredSpan(int axis)
	{
		int extra = 2 * (getBorder() + getSpace(axis));
		switch(axis)
		{
			case View.X_AXIS:
				return this.fWidth+extra;
			case View.Y_AXIS:
				return this.fHeight+extra;
			default:
				throw new IllegalArgumentException("Invalid axis in getPreferredSpan() : " + axis);
		}
	}

	/** Determines the desired alignment for this view along an
	  * axis. This is implemented to give the alignment to the
	  * bottom of the icon along the y axis, and the default
	  * along the x axis.
	  *
	  * @param axis may be either X_AXIS or Y_AXIS
	  *@return the desired alignment. This should be a value
	  *   between 0.0 and 1.0 where 0 indicates alignment at the
	  *   origin and 1.0 indicates alignment to the full span
	  *   away from the origin. An alignment of 0.5 would be the
	  *   center of the view.
	  */
	public float getAlignment(int axis)
	{
		switch(axis)
		{
			case View.Y_AXIS:
				return getVerticalAlignment();
			default:
				return super.getAlignment(axis);
		}
	}

	/** Provides a mapping from the document model coordinate space
	  * to the coordinate space of the view mapped to it.
	  *
	  * @param pos the position to convert
	  * @param a the allocated region to render into
	 * @param b 
	  * @return the bounding box of the given position
	  * @exception BadLocationException if the given position does not represent a
	  *   valid location in the associated document
	  * @see View
	  */
	public Shape modelToView(int pos, Shape a, Position.Bias b)
	throws BadLocationException
	{
		int p0 = getStartOffset();
		int p1 = getEndOffset();
		if((pos >= p0) && (pos <= p1))
		{
			Rectangle r = a.getBounds();
			if(pos == p1)
			{
				r.x += r.width;
			}
			r.width = 0;
			return r;
		}
		return null;
	}

	/** Provides a mapping from the view coordinate space to the logical
	  * coordinate space of the model.
	  *
	  * @param x the X coordinate
	  * @param y the Y coordinate
	  * @param a the allocated region to render into
	 * @param bias 
	  * @return the location within the model that best represents the
	  *         given point of view
	  * @see View
	  */
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
	{
		Rectangle alloc = (Rectangle) a;
		if(x < (alloc.x + alloc.width))
		{
			bias[0] = Position.Bias.Forward;
			return getStartOffset();
		}
		bias[0] = Position.Bias.Backward;
		return getEndOffset();
	}

	/** Change the size of this image. This alters the HEIGHT and WIDTH
	  * attributes of the Element and causes a re-layout.
	 * @param width 
	 * @param height 
	  */
	protected void resize(int width, int height)
	{
		if((width == this.fWidth) && (height == this.fHeight))
		{
			return;
		}
		this.fWidth = width;
		this.fHeight= height;
		// Replace attributes in document
		MutableAttributeSet attr = new SimpleAttributeSet();
		attr.addAttribute(HTML.Attribute.WIDTH ,Integer.toString(width));
		attr.addAttribute(HTML.Attribute.HEIGHT,Integer.toString(height));
		((StyledDocument)getDocument()).setCharacterAttributes(this.fElement.getStartOffset(), fElement.getEndOffset(), attr, false);
	}

	// Mouse event handling ------------------------------------------------

	/** Select or grow image when clicked.
	 * @param e 
	  */
	public void mousePressed(MouseEvent e)
	{
		Dimension size = this.fComponent.getSize();
		if((e.getX() >= (size.width - 7)) && (e.getY() >= (size.height - 7)) && (getSelectionState() == 2))
		{
			// Click in selected grow-box:
			Point loc = this.fComponent.getLocationOnScreen();
			this.fGrowBase = new Point(loc.x + e.getX() - this.fWidth, loc.y + e.getY() - this.fHeight);
			this.fGrowProportionally = e.isShiftDown();
		}
		else
		{
			// Else select image:
			this.fGrowBase = null;
			JTextComponent comp = (JTextComponent)this.fContainer;
			int start = this.fElement.getStartOffset();
			int end = this.fElement.getEndOffset();
			int mark = comp.getCaret().getMark();
			int dot  = comp.getCaret().getDot();
			if(e.isShiftDown())
			{
				// extend selection if shift key down:
				if(mark <= start)
				{
					comp.moveCaretPosition(end);
				}
				else
				{
					comp.moveCaretPosition(start);
				}
			}
			else
			{
				// just select image, without shift:
				if(mark != start)
				{
					comp.setCaretPosition(start);
				}
				if(dot != end)
				{
					comp.moveCaretPosition(end);
				}
			}
		}
	}

	/** Resize image if initial click was in grow-box: 
	 * @param e */
	public void mouseDragged(MouseEvent e)
	{
		if(this.fGrowBase != null)
		{
			Point loc = this.fComponent.getLocationOnScreen();
			int width = Math.max(2, loc.x + e.getX() - this.fGrowBase.x);
			int height= Math.max(2, loc.y + e.getY() - this.fGrowBase.y);
			if(e.isShiftDown() && this.fImage != null)
			{
				// Make sure size is proportional to actual image size
				float imgWidth = this.fImage.getWidth(this);
				float imgHeight = this.fImage.getHeight(this);
				if((imgWidth > 0) && (imgHeight > 0))
				{
					float prop = imgHeight / imgWidth;
					float pwidth = height / prop;
					float pheight = width * prop;
					if(pwidth > width)
					{
						width = (int)pwidth;
					}
					else
					{
						height = (int)pheight;
					}
				}
			}
			resize(width,height);
		}
	}

	/** (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me)
	{
		this.fGrowBase = null;
		//! Should post some command to make the action undo-able
	}

	/** On double-click, open image properties dialog.
	 * @param me 
	  */
	public void mouseClicked(MouseEvent me)
	{
		if(me.getClickCount() == 2)
		{
			//$ IMPLEMENT
		}
	}

	/** (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent me) { }
	/** (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me)   { }
	/** (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent me)  {  }

	// Static icon accessors -----------------------------------------------

	private Icon makeIcon(final String gifFile)
	throws IOException
	{
		/* Copy resource into a byte array. This is
		 * necessary because several browsers consider
		 * Class.getResource a security risk because it
		 * can be used to load additional classes.
		 * Class.getResourceAsStream just returns raw
		 * bytes, which we can convert to an image.
		 */
		InputStream resource = RelativeImageView.class.getResourceAsStream(gifFile);

		if(resource == null)
		{
			return null;
		}
		BufferedInputStream in = new BufferedInputStream(resource);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int n;
		while((n = in.read(buffer)) > 0)
		{
			out.write(buffer, 0, n);
		}
		in.close();
		out.flush();

		buffer = out.toByteArray();
		if(buffer.length == 0)
		{
			System.err.println("WARNING : " + gifFile + " is zero-length");
			return null;
		}
		return new ImageIcon(buffer);
	}

	private void loadImageStatusIcons()
	{
		try
		{
			if(sPendingImageIcon == null)
			{
				sPendingImageIcon = makeIcon(PENDING_IMAGE_SRC);
			}
			if(sMissingImageIcon == null)
			{
				sMissingImageIcon = makeIcon(MISSING_IMAGE_SRC);
			}
		}
		catch(Exception e)
		{
			System.err.println("ImageView : Couldn't load image icons");
		}
	}

	protected StyleSheet getStyleSheet()
	{
		HTMLDocument doc = (HTMLDocument)getDocument();
		return doc.getStyleSheet();
	}

}
