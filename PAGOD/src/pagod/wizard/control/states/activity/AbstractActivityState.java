/*
 * Projet PAGOD
 * 
 * $Id: AbstractActivityState.java,v 1.3 2005/11/29 18:11:15 yak Exp $
 */
package pagod.wizard.control.states.activity;

import java.util.List;

import pagod.common.model.Activity;
import pagod.common.model.Step;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.states.AbstractState;
import pagod.wizard.control.states.Request;

/**
 * @author Cyberal
 * 
 */
public abstract class AbstractActivityState extends AbstractState
{
	/**
	 * on stocke l'activityScheduler car un etat doit pouvoir changer l'etat de
	 * l'ActivityScheduler (invocation de la methode setState())
	 */
	protected ActivityScheduler	activityScheduler;

	protected Activity			activity	= null;

	/**
	 * indice de l'etape
	 */
	protected int				index		= 0;

	protected List<Step>		stepList;

	/**
	 * Etape Courante;
	 */
	protected Step				step;

	/**
	 * Step utilis? par la combo en cas d acces direct
	 */
	private int					goToStepInd;

	/**
	 * 
	 * @param activityScheduler
	 * @param activity
	 */
	public AbstractActivityState (ActivityScheduler activityScheduler,
			Activity activity)
	{
		super();
		// TODO Corps de constructeur g?n?r? automatiquement

		this.activityScheduler = activityScheduler;
		this.activity = activity;
		this.stepList = activity.getSteps();
		this.step = null;

	}

	/**
	 * 
	 * @param activityScheduler
	 * @param activity
	 * @param iCurrentStep
	 */

	/**
	 * 
	 */
	public abstract void terminate ();

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString ();

	/**
	 * 
	 */
	public abstract void display ();

	/**
	 * @param goToStepInd
	 */
	public void setGoToStepInd (int goToStepInd)
	{
		this.goToStepInd = goToStepInd;
	}

	/**
	 * @return goToStepInd
	 */
	public int getGoToStepInd ()
	{
		return this.goToStepInd;
	}

	/**
	 * @return liste
	 */
	public List<Step> getStepList ()
	{
		return this.stepList;
	}

	/**
	 * @param request
	 *            la requete que l'on doit traiter
	 * @return retourn vrai si on a changé d'état faut sinon
	 */
	public boolean manageRequest (Request request)
	{
		

		// on regarde le type de requete que l'on recoit
		switch (request.getCurrentRequest())
		{
			//code commun a toute les méthodes.
			case GOTOSTEP:
				if (request.getContent()  instanceof AbstractActivityState)
				{
					this.activityScheduler.setState((AbstractActivityState)request.getContent());
					return true;
				}
				else
				{
					//TODO a supprimer
					System.err.println("AbstractActivityState.manageresquest : le content de la requete n'est pas un AbstractActivityState");
					return false;
				}
	
			default:
				return false;
		}
		
	}

	/**
	 * @return Retourne l'attribut activity
	 */
	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * @return Retourne l'attribut index
	 */
	public int getIndex ()
	{
		return this.index;
	}

	/**
	 * @return Retourne l'attribut step
	 */
	public Step getStep ()
	{
		return this.step;
	}

	/**
	 * @param stepList Valeur à donner à stepList
	 */
	public void setStepList (List<Step> stepList)
	{
		this.stepList = stepList;
	}

}