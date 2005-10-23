/* 
 * Copyright (C) 2004 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */
package pagod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

public class Tray implements ActionListener, ItemListener
{

	SystemTray			tray	= SystemTray.getDefaultSystemTray();
	TrayIcon			ti;
	protected Timer		timer;
	protected int		nbSec	= 0;
	protected JMenuItem	timeItem;
	protected xmlSaver	xs;

	public Tray ()
	{
		this.xs = new xmlSaver();
		this.xs.load();
		JPopupMenu menu;

		JMenuItem menuItem;
		this.timer = new Timer(1000, this);
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (Integer
				.parseInt(System.getProperty("java.version").substring(2, 3)) >= 5) System
				.setProperty("javax.swing.adjustPopupLocationToFit", "false");
		menu = new JPopupMenu("Menu");
		this.timeItem = new JMenuItem("non lancé");
		menu.add(this.timeItem);
		menu.addSeparator();
		// a group of JMenuItems
		menuItem = new JMenuItem("Start");

		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Stop");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// "Quit" menu item
		menu.addSeparator();
		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// ImageIcon i = new ImageIcon("duke.gif");
		ImageIcon i = new ImageIcon(Tray.class.getResource("/images/duke.gif"));

		ti = new TrayIcon(i, "Pagod Time", menu);

		ti.setIconAutoSize(true);
		ti.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				// TODO LANCER le counter
				if (Tray.this.timer.isRunning())
				{
					Tray.this.stop();
					Tray.this.timer.stop();
				}
				else
				{
					Tray.this.ti.displayMessage("START : Pagod Time",
							"Compteur lancé", 0);
					Tray.this.timer.start();
				}
			}
		});

		tray.addTrayIcon(ti);

	}

	// Returns just the class name -- no package info.
	protected String getClassName (Object o)
	{
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");

		return classString.substring(dotIndex + 1);
	}

	public void actionPerformed (ActionEvent e)
	{

		if (e.getSource() instanceof Timer)
		{
			this.nbSec++;
			ti.setCaption("Temps : " + Tray.this.time2String(Tray.this.nbSec));
			this.timeItem.setText("Temps : "
					+ Tray.this.time2String(Tray.this.nbSec));
		}
		else
		{
			JMenuItem source = (JMenuItem) (e.getSource());
			String s = source.getText();
			if (s.equalsIgnoreCase("Quit"))
			{

				System.exit(0);
			}
			else if (s.equalsIgnoreCase("start"))
			{
				this.timer.start();
			}
			else if (s.equalsIgnoreCase("stop"))
			{
				Tray.this.stop();
				this.timer.stop();
			}
		}

	}

	// fonction dont on se fout
	public void itemStateChanged (ItemEvent e)
	{

	}

	public static void main (String[] args)
	{

		new Tray();
	}

	public String time2String (int timeInSec)
	{
		int sec = timeInSec % 60;
		int reste = timeInSec / 60;
		int minute = reste % 60;
		int heure = reste / 60;
		return new String(heure + ":" + minute + ":" + sec);

	}

	public void stop ()
	{

		String motif = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(motif, Locale.FRANCE);

		this.xs.addTime(sdf.format(new Date()), this.nbSec);

		this.ti.displayMessage("STOP : Pagod Time ", "Total Projet : "
				+ this.xs.getAllTime() + "\n" + "Total jour    : "
				+ this.xs.getTodayTime() + "\n" + "Tps activité : "
				+ xmlSaver.stringFromTime(this.nbSec), 0);
		this.nbSec = 0;
	}
}
