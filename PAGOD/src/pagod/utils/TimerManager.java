/*
 * Projet PAGOD
 * 
 * $Id: TimerManager.java,v 1.2 2005/11/09 11:40:52 yak Exp $
 */
package pagod.utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * @author yak
 *
 */
public class TimerManager implements ActionListener
{

	/**
	 * Instance du timer
	 */
	private static TimerManager instance = null;
	/**
	 * le timer swing
	 */
	private static Timer theSwingTimer =  null;
	
	/**
	 * la valeur du timer
	 */
	private int value;
	
	/**
	 * Constructeur vide
	 */
	public TimerManager ()
	{
		
	}

	/**
	 * @return Retourne l'attribut instance
	 */
	public static TimerManager getInstance ()
	{
		if (instance == null)
		{
			instance = new TimerManager();
			theSwingTimer = new Timer(1000,instance);
		}
		return instance;
	}
	
	/**
	 * Demarre le timer et l'initialise si besoin est
	 */
	public void start()
	{
		//on initialise le timer a 0
		
			this.value = 0;
			
	
		theSwingTimer.start();
	}
	/**
	 * @param initValue
	 */
	public void start (int initValue)
	{
		this.value = initValue;
		theSwingTimer.start();
		
	}

	/**
	 * Arrete le timer en retournant la valeur
	 * @return la valeur du timer
	 */
	public int stop ()
	{
		//on arrete le timer et on retourne la valeur
		theSwingTimer.stop();
		return this.value;
	}
	
	
	/** (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent arg0)
	{
		//on augmente la valeur
		this.value++;
		
	}
	

}
