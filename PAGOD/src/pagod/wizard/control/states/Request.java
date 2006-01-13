/*
 * Projet PAGOD
 * 
 * $Id: Request.java,v 1.5 2006/01/13 14:28:43 biniou Exp $
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
	public enum RequestType
	{
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
		 * Afficher la Jtable des temps passes
		 */
		TIME_ACTIVITY,
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
	private Object		content;

	/**
	 * la requete que l'on traite
	 */
	private RequestType	currentRequest;

	/**
	 * constructeur
	 * 
	 * @param requestT
	 *            la requete que l'on souhaite creer et traiter
	 */
	public Request (RequestType requestT)
	{
		super();
		this.currentRequest = requestT;
		this.content = null;

	}

	/**
	 * constructeur
	 * 
	 * @param requestT
	 *            la requete que l'on souhaite creer et traiter
	 * @param contentO
	 *            l'object qui accompagne la requete
	 */
	public Request (RequestType requestT, Object contentO)
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
	 * @param content
	 *            Valeur ? donner ? content
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
	 * @param currentRequest
	 *            Valeur ? donner ? currentRequest
	 */
	public void setCurrentRequest (RequestType currentRequest)
	{
		this.currentRequest = currentRequest;
	}

	/**
	 * Fonction appel? lorsqu'on veut afficher un object Request
	 * 
	 * @return une chaine de caractere
	 * 
	 */
	public String toString ()
	{
		if (this.content != null)
		{
			String sTemp = this.content.toString();

			// on remplace les &eacute; par des ?
			sTemp = sTemp.replaceAll("&eacute;", "?");

			// on remplace les &egrave; par des ?
			sTemp = sTemp.replaceAll("&egrave;", "?");

			// on remplace les &ccedil; par des ?
			sTemp = sTemp.replaceAll("&ccedil;", "?");

			// on remplace les &ugrave; par des ?
			sTemp = sTemp.replaceAll("&ugrave;", "?");

			// on remplace les &agrave; par des ?
			sTemp = sTemp.replaceAll("&agrave;", "?");

			// on remplace les &ecirc; par des ?
			sTemp = sTemp.replaceAll("&ecirc;", "?");

			// on remplace les &ocirc; par des ?
			sTemp = sTemp.replaceAll("&ocirc;", "?");

			// on remplace les &nbsp; par des espaces
			sTemp = sTemp.replaceAll("&nbsp;", " ");

			// on supprime les espaces avant est apres la chaine
			sTemp = sTemp.trim();
			
			return sTemp;
		}
		else
			return "" + this.currentRequest;
	}
}
