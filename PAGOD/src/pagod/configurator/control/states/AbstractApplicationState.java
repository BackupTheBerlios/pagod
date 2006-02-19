/*
 * Projet PAGOD
 * 
 * $Id: AbstractApplicationState.java,v 1.1 2006/02/19 15:36:04 yak Exp $
 */
package pagod.configurator.control.states;

import pagod.configurator.control.ApplicationManager;



/**
 * @author m1isi26
 * 
 */
public abstract class AbstractApplicationState
{
	/**
	 * on stocke l'applicationManager car un etat doit pouvoir changer l'etat de
	 * l'applicationManager (invocation de la methode setState())
	 */
	protected ApplicationManager	applicationManager;

	/**
	 * @param applicationManager
	 * 
	 */
	public AbstractApplicationState (ApplicationManager applicationManager)
	{
		super();
		this.applicationManager = applicationManager;
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public abstract boolean manageRequest (Request request);
}
