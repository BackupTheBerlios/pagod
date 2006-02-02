/*
 * Projet PAGOD
 * 
 * $Id: TimeCouple.java,v 1.2 2006/02/02 19:23:49 psyko Exp $
 */
package pagod.common.model;

/**
 * @author Fabfoot
 *
 */
public class TimeCouple
{
	 private int iTimeElapsed;
	 private int iTimeRemaining;

	 /**
	 * @param elapsed
	 * @param remaining
	 */
	public TimeCouple(int elapsed, int remaining) {
		 this.iTimeElapsed  = elapsed ;
		 this.iTimeRemaining  = remaining ;
	 }

	/**
	 * @return iTimeElapsed
	 */
	 
	 public int getTimeElapsed() {
	      return this.iTimeElapsed ;
	   }
	
	/**
	 * @return iTimeRemaining
	 */
	public int getTimeRemaining() {
		return this.iTimeRemaining  ;
	}
	/**
	 * @param elapsed 
	 */
	public void setTimeElapsed(int elapsed) {
		this.iTimeElapsed = elapsed ;
	}
	
	/**
	 * @param remaining
	 */
	public void setTimeRemaining(int remaining) {
		this.iTimeRemaining = remaining   ;
	}
}
