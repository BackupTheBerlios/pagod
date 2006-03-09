/*
GNU Lesser General Public License

MutableFilter
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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/** Class for providing JFileChooser with a FileFilter
  */
public class MutableFilter extends FileFilter
{
	private String[] acceptableExtensions;
	private String descriptor;

	/**
	 * @param exts
	 * @param desc
	 */
	public MutableFilter(String[] exts, String desc)
	{
		this.acceptableExtensions = exts;
		StringBuffer strbDesc = new StringBuffer(desc + " (");
		for(int i = 0; i < this.acceptableExtensions.length; i++)
		{
			if(i > 0) { strbDesc.append(", "); }
			strbDesc.append("*." + this.acceptableExtensions[i]);
		}
		strbDesc.append(")");
		this.descriptor = strbDesc.toString();
	}

	/** (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file)
	{
		if(file.isDirectory())
		{
			return true;
		}
		String fileName = file.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		if(fileExt != null)
		{
			for(int i = 0; i < this.acceptableExtensions.length; i++)
			{
				if(fileExt.equals(this.acceptableExtensions[i]))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/** (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription()
	{
		return this.descriptor;
	}
}

