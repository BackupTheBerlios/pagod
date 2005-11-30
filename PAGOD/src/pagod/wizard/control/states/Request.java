/*
 * Projet PAGOD
 * 
 * $Id: Request.java,v 1.3 2005/11/30 12:21:17 cyberal82 Exp $
 */
package pagod.wizard.control.states;

/**
 * @author yak
 *
 * classe requete qui represente une demande a traiter
 */
public class Request
{

	/**
     * Requete suceptible d'etre soumis ? l'application manager
     */
    public enum RequestType {
        /**
         * Lancer l'application
         */
        RUN_APPLICATION,
        /**
         * Quitter l'application
         */
        QUIT_APPLICATION,
        /**
         * Ouvrir un processus
         */
        OPEN_PROCESS,
        /**
         * Fermer un processus
         */
        CLOSE_PROCESS,
        /**
         * Ouvrir un project
         */
        OPEN_PROJECT,
        /**
         * Fermer un project
         */
        CLOSE_PROJECT,
        /**
         * Afficher le a propos
         */
        SHOW_ABOUT,
        /**
         * Afficher la fenetre de configuration des preferences
         */
        PREFERENCES,
        /**
         * Afficher la fenetre de configuration des outils
         */
        SET_TOOLS,
        /**
         * Lancer une activit?
         */
        RUN_ACTIVITY,
        /**
         * Demande la suite
         */
        NEXT,
        /**
         * Demande le retour
         */
        PREVIOUS,
        /**
         * Terminer l'activit?
         */
        TERMINATE_ACTIVITY,
        /**
         * aller ? une ?tape direct en utilisant la comboBox
         */
        GOTOSTEP
    }
    
    /**
     * L'object de type quelconque que l'on joint a la requete
     */
	private Object content;
	
	/**
	 * la requete que l'on traite
	 */
	private RequestType currentRequest;
	
	/**
	 * constructeur
	 * @param requestT la requete que l'on souhaite creer et traiter
	 */
	public Request (RequestType requestT)
	{
		super();
		this.currentRequest = requestT;
		this.content = null;
		
	}
	/**
	 * constructeur
	 * @param requestT la requete que l'on souhaite creer et traiter
	 * @param contentO l'object qui accompagne la requete
	 */
	public Request (RequestType requestT,Object contentO)
	{
		super();
		this.currentRequest = requestT;
		this.content = contentO;
		
	}
	/**
	 *
	 * @return Retourne l'attribut content
	 */
	public Object getContent ()
	{
		return this.content;
	}
	/**
	 * @param content Valeur ? donner ? content
	 */
	public void setContent (Object content)
	{
		this.content = content;
	}
	/**
	 * @return Retourne l'attribut currentRequest
	 */
	public RequestType getCurrentRequest ()
	{
		return this.currentRequest;
	}
	/**
	 * @param currentRequest Valeur ? donner ? currentRequest
	 */
	public void setCurrentRequest (RequestType currentRequest)
	{
		this.currentRequest = currentRequest;
	}
	public String toString()
	{
		if (this.content != null)
			return this.content.toString();
		else
			 return ""+this.currentRequest;
	}

}
