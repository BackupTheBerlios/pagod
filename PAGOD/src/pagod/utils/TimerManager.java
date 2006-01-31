/*
 * Projet PAGOD
 * 
 * $Id: TimerManager.java,v 1.6 2006/01/31 21:34:31 yak Exp $
 */
package pagod.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

/**
 * @author yak
 * 
 */
public class TimerManager extends Observable implements ActionListener
{

	/**
	 * Instance du timer
	 */
	private static TimerManager	instance		= null;
	/**
	 * le timer swing
	 */
	private static Timer		theSwingTimer	= null;

	/**
	 * la valeur du timer
	 */
	private int					value;

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
			theSwingTimer = new Timer(1000, instance);
		}
		return instance;
	}

	/**
	 * Demarre le timer et l'initialise si besoin est
	 */
	public void start ()
	{
		// on initialise le timer a 0
		this.value = 0;
		theSwingTimer.start();
		// on notify
		this.setChanged();
		this.notifyObservers(new Integer(this.value));
	}

	/**
	 * @param initValue
	 */
	public void start (int initValue)
	{
		this.value = initValue;
		theSwingTimer.start();
		// on notify
		this.setChanged();
		this.notifyObservers(new Integer(this.value));

	}

	/**
	 * Arrete le timer en retournant la valeur
	 * 
	 * @return la valeur du timer
	 */
	public int stop ()
	{
		// on arrete le timer et on retourne la valeur
		theSwingTimer.stop();

		return this.value;
	}

	/**
	 * retourne vrai si le timer est lancer
	 * 
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent arg0)
	{
		// on augmente la valeur
		this.value++;
		// on notifie les observer
		this.setChanged();
		this.notifyObservers(new Integer(this.value));

	}
	/**
	 * methode permettant de transformer un int representant un temps en seocnde sous la forme stirng
	 * @param timeInCar le string sous forme h:m:s
	 * @return le temps sous forme de int en seconde
	 */
	public static int timeFromString (String timeInCar)
	{

		String tabTime[] = timeInCar.split(":");
		int h = (Integer.parseInt(tabTime[0]));
		int m = (Integer.parseInt(tabTime[1]));
		int s = (Integer.parseInt(tabTime[2]));
		return h * 3600 + m * 60 + s;

	}
	/**
	 * methode permettant de transformer un int representant un temps en seocnde sous la forme string
	 * @param timeInSec le temps en seconde a analyser
	 * @return le temps sous forme h:m:s
	 */
	public static String stringFromTime (int timeInSec)
	{
		int sec = timeInSec % 60;
		int reste = timeInSec / 60;

		int minute = reste % 60;
		int heure = reste / 60;
		
		return new String(heure + ":" + minute + ":" + sec);

	}

}
