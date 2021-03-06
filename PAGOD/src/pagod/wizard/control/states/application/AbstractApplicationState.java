/*
 * Projet PAGOD
 * 
 * $Id: AbstractApplicationState.java,v 1.5 2006/02/19 12:07:20 yak Exp $
 */
package pagod.wizard.control.states.application;

import pagod.wizard.control.ApplicationManager;
import pagod.wizard.control.states.Request;

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
	 * @return retourn vrai si on a chang� d'�tat faut sinon
	 */
	public abstract boolean manageRequest (Request request);
}
