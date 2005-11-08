/*
 * Projet PAGOD
 * 
 * $Id: TimerManager.java,v 1.1 2005/11/08 17:36:31 yak Exp $
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
	private Timer theSwingTimer =  null;
	
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
		}
		return instance;
	}
	
	/**
	 * Demarre le timer et l'initialise si besoin est
	 */
	public void start()
	{
		//si le timer n'est pas initialisé on le fait en partant de 0;
		if (this.theSwingTimer == null)
		{
			this.value = 0;
			this.theSwingTimer = new Timer(1000,instance);
		}
		this.theSwingTimer.start();
	}

	/**
	 * Arrete le timer en retournant la valeur
	 * @return la valeur du timer
	 */
	public int stop ()
	{
		//on arrete le timer et on retourne la valeur
		this.theSwingTimer.stop();
		return this.value;
	}
	
	/**
	 * @param initValue la valeur d'initialisation du timer 
	 */
	public void init (int initValue)
	{
		
		this.value = initValue;
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
