package mav;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mav.layout.CandidateImageCacheManager;
import mav.layout.JXCandidateShelf;
import mav.layout.MavConnectionDialog;

import org.jdesktop.swingx.JXFrame;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import MAV.Candidat;
import MAV.SRV;
import MAV.SRVHelper;
import constants.Constants;

public class MachineAVoter {

	/**
	 * image manager
	 */
	CandidateImageCacheManager imageManager;

	private JPanel connectedPanel;

	private JPanel disconnectedPanel;

	/**
	 * la frame de l'application
	 */
	private JXFrame mainFrame;

	/**
	 * panneau titre
	 */
	private JPanel titlePanel;

	/**
	 * 
	 */
	private JLabel titleLabel;

	/**
	 * panneau desc
	 */
	private JPanel descPanel;

	/**
	 * 
	 */
	private JLabel descLabel;

	/**
	 * panneau bouton
	 */
	private JPanel buttonPanel;

	/**
	 * bouton droit
	 */
	private JButton rightButton;

	/**
	 * bouton gauche
	 */
	private JButton leftButton;

	/**
	 * bouton voter
	 */
	private JButton voteButton;

	/**
	 * candidate
	 */
	private JXCandidateShelf cdShelf;

	/**
	 * initialise les fenetres
	 */
	private void init(Candidat[] candidats, SRV srvRef) {
		createBasicFrame(candidats);

	}

	private void createConnectedPanel(Candidat[] candidats) {
		this.connectedPanel = new JPanel();
		// setting title
		this.titlePanel = new JPanel();
		this.titlePanel.setBackground(Color.BLACK);
		this.titleLabel = new JLabel();
		this.titleLabel.setForeground(Color.WHITE);
		this.titleLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
		this.titleLabel.setText("Election présidentielle 2007");
		this.titlePanel.add(this.titleLabel);

		this.cdShelf = new JXCandidateShelf();
		List list = new ArrayList();
		for (int i = 0; i < candidats.length; i++) {
			list.add(candidats[i]);
		}

		this.cdShelf.setList(list);

		this.buttonPanel = new JPanel();
		this.voteButton = new JButton("voter");
		this.voteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO faire un confirmation avec le nom du candidat
				System.out.println(MachineAVoter.this.cdShelf
						.getSelectedCandidat().nom());

			}
		});

		this.rightButton = new JButton(">");
		this.rightButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MachineAVoter.this.cdShelf.getShelf().scrollAndAnimateBy(1);

			}
		});
		this.leftButton = new JButton("<");
		this.leftButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MachineAVoter.this.cdShelf.getShelf().scrollAndAnimateBy(-1);

			}
		});
		this.buttonPanel.setBackground(Color.BLACK);
		this.buttonPanel.add(this.leftButton);
		this.buttonPanel.add(this.voteButton);
		this.buttonPanel.add(this.rightButton);

		/**
		 * setting jpanel
		 */
		this.connectedPanel.setLayout(new BorderLayout());
		this.connectedPanel.add(this.titlePanel, BorderLayout.NORTH);
		this.connectedPanel.add(this.cdShelf, BorderLayout.CENTER);
		this.connectedPanel.add(this.buttonPanel, BorderLayout.SOUTH);
	}

	private void createDisconnectedPanel() {
		this.disconnectedPanel = new JPanel();
		this.disconnectedPanel.setLayout(new BorderLayout());
		this.disconnectedPanel.add(this.titlePanel, BorderLayout.NORTH);

	}

	private JXFrame createBasicFrame(Candidat[] candidats) {
		this.mainFrame = new JXFrame("Machine a voter");
		this.mainFrame.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);

		// setting desc
		this.descPanel = new JPanel();
		this.descPanel.setBackground(Color.BLACK);
		this.descLabel = new JLabel();
		this.descLabel.setForeground(Color.WHITE);
		this.descLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
		this.descLabel.setText("Election présidentielle 2007");
		this.descPanel.add(this.descLabel);

		this.createConnectedPanel(candidats);
		this.createDisconnectedPanel();

		this.mainFrame.add(this.disconnectedPanel);
		this.mainFrame.add(this.connectedPanel);
		this.mainFrame.setLocationRelativeTo(null);
		return mainFrame;
	}

	public void connect() {
		this.connectedPanel.setVisible(false);
		this.disconnectedPanel.setVisible(true);
		this.mainFrame.setVisible(true);
	}

	public void disconnect() {
		this.connectedPanel.setVisible(true);
		this.disconnectedPanel.setVisible(false);
		this.mainFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("erreur pas assez d'arg");
			return;
		}

		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME, "");
			NameComponent path[] = { nc };
			SRV srvRef = SRVHelper.narrow(ncRef.resolve(path));

			// récupération des identifiants de la machine
			int idMav = Integer.parseInt(args[0]);
			int idBV = Integer.parseInt(args[1]);
			System.out.println("" + srvRef.authMAV(idMav, idBV));

			srvRef.authPersonne(1, "1234", 1);
			System.out.println("Liste candidat: ");
			Candidat[] tabCandidat = srvRef.listeCandidat();

			MachineAVoter m = new MachineAVoter();
			m.init(tabCandidat, srvRef);
			MavConnectionDialog tvd = new MavConnectionDialog(m, srvRef);
			tvd.init();

			tvd.setLocationRelativeTo(null);
			tvd.setVisible(true);

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

	}

}
