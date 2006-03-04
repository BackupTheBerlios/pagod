/*
 * Projet PAGOD
 * 
 * $Id: TimerManager.java,v 1.9 2006/03/04 15:54:32 biniou Exp $
 */
package pagod.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

import pagod.common.model.TimeCouple;

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
	 * la valeur du timer a incrementé
	 */
	private int					valueElapsed;
	/**
	 * la valeur du timer a decrementé
	 */
	private int					valueRemaining;

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
		this.valueElapsed = 0;
		this.valueRemaining = 0;
		theSwingTimer.start();
		// on notify
		this.setChanged();
		this.notifyObservers(new TimeCouple(this.valueElapsed,this.valueRemaining));
	}

	/**
	 * @param initValueElapsed
	 * @param initValueRemaining 
	 */
	public void start (int initValueElapsed,int initValueRemaining)
	{
		//on initialise 
		this.valueElapsed = initValueElapsed;
		this.valueRemaining = initValueRemaining;
		theSwingTimer.start();
		// on notify
		this.setChanged();
		this.notifyObservers(new TimeCouple(this.valueElapsed,this.valueRemaining));

	}

	/**
	 * Arrete le timer en retournant la valeur
	 */
	public void stop ()
	{
		// on arrete le timer et on retourne la valeur
		theSwingTimer.stop();
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
	public int getValueElapsed ()
	{
		return this.valueElapsed;
	}
	/**
	 * 
	 * @return la valeur du timer
	 * 
	 */
	public int getValueRemaining ()
	{
		return this.valueRemaining;
	}
	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent arg0)
	{
		// on augmente la valeur
		this.valueElapsed++;
		if (this.valueRemaining != 0)
		{
			this.valueRemaining--;
		}
		// on notifie les observer
		this.setChanged();
		this.notifyObservers(new TimeCouple(this.valueElapsed,this.valueRemaining));

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

	/**
	 * @param time : une chaine de caractere de la forme h:m:s
	 * @return une chaine de caractere de la forme h:s, si erreur en entrée on renvoie null
	 */
	public static String displayTimeWithoutSeconds (String time)
	{
		String newTime = null;
		boolean isValid = time.matches("[0-9]+:[0-5]?[0-9]:[0-5]?[0-9]");
		if (isValid)
		{
			String tabTime[] = time.split(":");
			newTime = tabTime[0]+":"+tabTime[1] ;
			return (newTime);
		}
		else return null;
	}
	
	/**
	 * @param timeWithoutSeconds : une chaine de caracteres sans secondes h:m
	 * @return le temps sous forme h:m:s
	 */
	public static int timeFromStringWithoutSeconds (String timeWithoutSeconds)
	{

		String tabTime[] = timeWithoutSeconds.split(":");
		int h = (Integer.parseInt(tabTime[0]));
		int m = (Integer.parseInt(tabTime[1]));
		int s = 0 ;
		return h * 3600 + m * 60 + s;

	}

}
