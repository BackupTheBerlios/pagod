/*
 * Projet PAGOD
 * 
 * $Id: Request.java,v 1.10 2006/02/19 15:53:40 yak Exp $
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
		 * Afficher la Jtable des temps passes
		 */
		TIME_ACTIVITY,
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
		 * suspendre l'activité
		 */
		SUSPEND_ACTIVITY,
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

			// on remplace les &eacute; par le caractere unicode correspondant
			// je met pas le caractere directement car le serveur marine ne les
			// supporte pas, il les remplace par des ?
			sTemp = sTemp.replaceAll("&eacute;", "\u00E9");

			// on remplace les &egrave; par les caracteres unicode
			// correspondants
			sTemp = sTemp.replaceAll("&egrave;", "\u00E8");

			// on remplace les &ccedil; par les caracteres unicode
			// correspondants
			sTemp = sTemp.replaceAll("&ccedil;", "\u00E7");

			// on remplace les &ugrave; par les caracteres unicode
			// correspondants
			sTemp = sTemp.replaceAll("&ugrave;", "\u00F9");

			// on remplace les &agrave; par les caracteres unicode
			// correspondants
			sTemp = sTemp.replaceAll("&agrave;", "\u00E0");

			// on remplace les &ecirc; par les caracteres unicode correspondants
			sTemp = sTemp.replaceAll("&ecirc;", "\u00EA");

			// on remplace les &ocirc; par les caracteres unicode correspondants
			sTemp = sTemp.replaceAll("&ocirc;", "\u00F4");

			// on remplace les &nbsp; par des espaces
			sTemp = sTemp.replaceAll("&nbsp;", "\u00A0");

			// Rq : je fais dans cette ordre pour gerer de maniere simple le
			// fait qu'un \r peut etre tt seul sur la ligne idem pour \n

			// on remplace les retours chariots \r suivit d'un saut de ligne \n
			// (ou l'inverse) par des espaces
			sTemp = sTemp.replaceAll("\r\n", "\u00A0");
			sTemp = sTemp.replaceAll("\n\r", "\u00A0");

			// on remplace les retours chariots \r par des espaces
			sTemp = sTemp.replaceAll("\r", "\u00A0");

			// on remplace les sauts de ligne \n par des espaces
			sTemp = sTemp.replaceAll("\n", "\u00A0");

			// on supprime les espaces avant est apres la chaine
			sTemp = sTemp.trim();

			return sTemp;
		}
		else
			return "" + this.currentRequest;
	}
}
