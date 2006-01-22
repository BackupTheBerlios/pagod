/*
 * Projet PAGOD
 * 
 * $Id: TimerManager.java,v 1.3 2006/01/22 15:45:39 yak Exp $
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
		//TODO a supprimer
		System.out.println("demarrage a "+initValue);
		theSwingTimer.start();
		
	}

	/**
	 * Arrete le timer en retournant la valeur
	 * @return la valeur du timer
	 */
	public int stop ()
	{
		//on arrete le timer et on retourne la valeur
		System.out.println("TimerManager arreter a "+this.value);
		theSwingTimer.stop();
		return this.value;
	}
	/**
	 * retourne vrai si le timer est lancer
	 * @return retourne vrai si le timer est lancer vrai sinon
	 */
	public boolean isStarted ()
	{
		return theSwingTimer.isRunning();
	}
	/**
	 * 
	 * @return la valeur du timer
	 * 
	 */
	public int getValue ()
	{
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
