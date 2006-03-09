/*
GNU Lesser General Public License

JButtonNoFocus
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

package pagod.utils.editor.component;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/** Class for providing a JButton that does not obtain focus
  */
public class JButtonNoFocus extends JButton
{
	/**
	 * 
	 */
	public JButtonNoFocus()                       { super();           this.setRequestFocusEnabled(false); }
	/**
	 * @param a
	 */
	public JButtonNoFocus(Action a)               { super(a);          this.setRequestFocusEnabled(false); }
	/**
	 * @param icon
	 */
	public JButtonNoFocus(Icon icon)              { super(icon);       this.setRequestFocusEnabled(false); }
	/**
	 * @param text
	 */
	public JButtonNoFocus(String text)            { super(text);       this.setRequestFocusEnabled(false); }
	/**
	 * @param text
	 * @param icon
	 */
	public JButtonNoFocus(String text, Icon icon) { super(text, icon); this.setRequestFocusEnabled(false); }

	/** (non-Javadoc)
	 * @see java.awt.Component#isFocusable()
	 */
	public boolean isFocusable()
	{
		return false;
	}

	/*** (non-Javadoc)
	 * @return bool
	 * @see java.awt.Component
	 */
	// deprecated in 1.4 with isFocusable(), left in for 1.3 compatibility
	public boolean isFocusTraversable()
	{
		return false;
	}
}
