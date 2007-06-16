package server.test;

import server.Server;

/**
 * Classe qui permet de créer un thread qui lance un SRV.
 *
 */
public class SRVThread extends Thread {
	
	/**
	 * L'identifiant du srv que le thread doit lancer
	 */
	private String SRVId;
	
	/**
	 * La date de fin de l'election
	 */
	private String endDateElection;
	
	/**
	 * Construit le SRVThread.
	 * 
	 * @param SRVId - l'identifiant du SRV
	 * @param endDateElection - la date de fin de l'election
	 */
	public SRVThread(String SRVId, String endDateElection) {
		this.SRVId = SRVId;
		this.endDateElection = endDateElection;
	}
	
	@Override
	public void run() {
		super.run();
		
		// Construit le tableau d'argument pour pouvoir lancer le SRV
		String[] srvArgs = {SRVId, endDateElection};
		
		// TODO a supprimer
		System.out.println("Thread SRV " + SRVId + " go !");
		
		// lance le SRV
		Server.main(srvArgs);
	}

}
