/*
 * Projet PAGOD
 * 
 * $Id: AbstractState.java,v 1.1 2005/11/27 20:37:52 yak Exp $
 */
package pagod.wizard.control.states;

/**
 * @author yak
 *
 */
public abstract class AbstractState
{

	/**
	 * Classe générique pour les states
	 */
	public AbstractState ()
	{
		super();
		// TODO Corps de constructeur généré automatiquement
	}
	
	/**
	 * @param request la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public abstract boolean manageRequest (Request request);
}
