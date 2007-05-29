package vtr;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import MAV.ResultVote;
import MAV.SRV;
import MAV.SRVHelper;
import constants.Constants;

public class VTR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// recupère le premier argument correspondant a l'identifiant du BV
		if (args.length < 1) {
			System.out.println("ERREUR pas assez d'arg");
			return;
		}

		int idVTR = Integer.parseInt(args[0]);
		int idBV = 1; 
		
		try {
			System.out.println("VTR lancé");
			
			SRV srv = connectBV(idBV);
			
			VTRServant vtrServant = new VTRServant();
			
			// on enregistre le vtr pour qu'il soit notifié
			srv.enregistrerVTR(vtrServant);
			
			// on recupere les resultats courants
			ResultVote[] resultVote = srv.listeResultat();
			vtrServant.setResultVote(resultVote);
			
			vtrServant.setSrv(srv);
			
			// creation de l'ihm
			
			// creation d'une jtabel
			JTable table = new JTable();
			table.setModel(vtrServant.getResultVoteTableModel());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			
			// creation de la fenetre principale
			JFrame mainFrame = new JFrame();
			mainFrame.setTitle("VTR");
			mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mainFrame.setLocation(0, 0);
			mainFrame.setLayout(new BorderLayout());
			
			mainFrame.add(scrollPane, BorderLayout.CENTER);
			
			mainFrame.setVisible(true);
			mainFrame.pack();

		} catch (InterruptedException e) {
			System.err
					.println("ERROR VTR: impossible de mettre le VTR en attente");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("ERROR VTR :");
			e.printStackTrace();
		}

	}
	
	/**
	 * Fonction qui permet de se connecter a un bureau de vote dont l'identifiant est passé en paramètre.
	 * 
	 * @param idBV - l'identifiant du bureau de vote
	 * @return le SRV
	 * @throws Exception
	 * 
	 */
	private static SRV connectBV(int idBV) throws Exception
	{
		// create and initialize the ORB
		ORB orb = ORB.init(new String[0], null);

		// get the root naming context
		org.omg.CORBA.Object objRef = orb
				.resolve_initial_references("NameService");
		NamingContext ncRef = NamingContextHelper.narrow(objRef);

		// resolve the Object Reference in Naming
		NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME + idBV, "");
		NameComponent path[] = { nc };
		SRV srvRef = SRVHelper.narrow(ncRef.resolve(path));
		
		return srvRef;
	}
	

}
