package mav;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mav.dialogs.MavConnectionDialog;
import mav.dialogs.UserConnectionDialog;
import mav.layout.CandidateImageCacheManager;
import mav.layout.JXCandidateShelf;

import org.jdesktop.swingx.JXFrame;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import sun.management.snmp.util.JvmContextFactory;

import MAV.Candidat;
import MAV.SRV;
import MAV.SRVHelper;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.IncorrectBVPersonException;
import MAV.SRVPackage.InternalErrorException;
import constants.Constants;

public class MachineAVoter {

	int id_mav;

	int id_bv;

	int numinsee_voter;

	String password_voter;

	/**
	 * image manager
	 */
	CandidateImageCacheManager imageManager;

	private JPanel connectedPanel;
	private JLabel descriptionLabel;

	/**
	 * la frame de l'application
	 */
	private JFrame mainFrame;

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
	 * bouton de déconnexion
	 */
	private JButton discButton;
	/**
	 * bouton voter
	 */
	private JButton voteButton;

	/**
	 * candidate
	 */
	private JXCandidateShelf cdShelf;

	private SRV srvRef;

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

		this.cdShelf = new JXCandidateShelf(this);
		List list = new ArrayList();
		for (int i = 0; i < candidats.length; i++) {
			list.add(candidats[i]);
		}

		this.cdShelf.setList(list);
		

		
		this.voteButton = new JButton("voter");
		this.voteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			try {
				
				if (JOptionPane.showConfirmDialog( MachineAVoter.this.mainFrame,"Voulez vous voter pour"+MachineAVoter.this.cdShelf.getSelectedCandidat().prenom()+" "+MachineAVoter.this.cdShelf.getSelectedCandidat().nom()+" ?","Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					
				{
					MachineAVoter.this.srvRef.vote(MachineAVoter.this.numinsee_voter,MachineAVoter.this.password_voter,MachineAVoter.this.cdShelf.getSelectedCandidat().id(),MachineAVoter.this.id_bv);
					MachineAVoter.this.reconnect();
				}
			} catch (BadAuthentificationException e1) {
				JOptionPane.showMessageDialog(
						MachineAVoter.this.mainFrame,
						"Vous n'êtes pas authentifier", "Erreur au cours du vote",
						JOptionPane.ERROR_MESSAGE);
			} catch (AlreadyVoteException e1) {
				JOptionPane.showMessageDialog(
						MachineAVoter.this.mainFrame,
						"Vous avez deja voté", "Erreur au cours du vote",
						JOptionPane.ERROR_MESSAGE);
			} catch (InternalErrorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IncorrectBVPersonException e1) {
				JOptionPane.showMessageDialog(
						MachineAVoter.this.mainFrame,
						"Vous ne pouvez voter dans ce bureau de vote", "Erreur au cours du vote",
						JOptionPane.ERROR_MESSAGE);
			}

			}
		});

		this.rightButton = new JButton(">");
		this.rightButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MachineAVoter.this.cdShelf.getShelf().scrollAndAnimateBy(1);
				MachineAVoter.this.updateDescription();

			}
		});
		this.leftButton = new JButton("<");
		this.leftButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MachineAVoter.this.cdShelf.getShelf().scrollAndAnimateBy(-1);
				MachineAVoter.this.updateDescription();
			}
		});
		
		this.discButton = new JButton("Déconnection");
		this.discButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MachineAVoter.this.reconnect();

			}
		});
//		 setting desc
		
		JPanel p = new JPanel();
		p.setBackground(Color.BLACK);
		p.setLayout(new FlowLayout());
		p.add(this.leftButton);
		p.add(this.voteButton);
		p.add(this.discButton);
		p.add(this.rightButton);
		this.descLabel = new JLabel();
		this.descLabel.setBackground(Color.BLACK);
		this.descLabel.setForeground(Color.WHITE);
		this.descLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.descLabel.setText("");
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setBackground(Color.BLACK);
		this.buttonPanel.setLayout(new BorderLayout());
		this.buttonPanel.add(p, BorderLayout.SOUTH);
		
		
		this.buttonPanel.add(this.descLabel, BorderLayout.NORTH);

		/**
		 * setting jpanel
		 */
		this.connectedPanel.setLayout(new BorderLayout());
		this.connectedPanel.add(this.titlePanel, BorderLayout.NORTH);
		this.connectedPanel.add(this.cdShelf, BorderLayout.CENTER);
		this.connectedPanel.add(this.buttonPanel, BorderLayout.SOUTH);
	}

	private JFrame createBasicFrame(Candidat[] candidats) {
		this.mainFrame = new JFrame("Machine a voter");
		this.mainFrame.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);

		

		this.createConnectedPanel(candidats);
		this.mainFrame.add(this.connectedPanel);
		this.descLabel.setText("<html><body><strong><u>DESCRIPTION<br></u></strong>"+candidats[0].description()+"</body></html>");

		return mainFrame;
	}

	public void connect(int id_mav, int id_bv) {
		this.id_mav = id_mav;
		this.id_bv = id_bv;
		Candidat[] tabCandidat;
		try {
			tabCandidat = this.srvRef.listeCandidat();
			this.init(tabCandidat, this.srvRef);
			UserConnectionDialog ucd = new UserConnectionDialog(this, srvRef);
			ucd.setModal(true);
			
			//this.mainFrame.setLocationRelativeTo(null);
			this.connectedPanel.setVisible(true);
			this.mainFrame.setResizable(false);
			
			this.mainFrame.setSize(500,500);
			this.mainFrame.setLocationRelativeTo(null);
			this.mainFrame.setVisible(true);
			ucd.setVisible(true);
			
			
			
			
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void reconnect()
	{
			UserConnectionDialog ucd = new UserConnectionDialog(this, srvRef);
			ucd.setModal(true);
			
			//this.mainFrame.setLocationRelativeTo(null);
			this.connectedPanel.setVisible(true);
			this.mainFrame.setResizable(false);
			
			this.mainFrame.setSize(500,500);
			this.mainFrame.setLocationRelativeTo(null);
			this.mainFrame.setVisible(true);
			ucd.setVisible(true);
			
	}

	public boolean authMAV(int id_mav, int id_bv) {
		try {
			SRV srv = this.getSRCConnect(id_bv);
			return srv.authMAV(id_mav, id_bv);

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
		return false;

	}

	public SRV getSRCConnect(int id) throws InvalidName, NotFound,
			CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		if (this.srvRef == null) {
			String[] args = new String[0];
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			NameComponent nc = new NameComponent(Constants.SRV_SERVANT_NAME
					+ id, Constants.SRV_KIND);
			NameComponent path[] = { nc };
			this.srvRef = SRVHelper.narrow(ncRef.resolve(path));
		}

		return this.srvRef;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// creation de la machine a voter
		MachineAVoter m = new MachineAVoter();

		// creation du dialog
		MavConnectionDialog tvd = new MavConnectionDialog(m);
		tvd.init();
		tvd.setVisible(true);
		tvd.setLocationRelativeTo(null);

	}

	public int getId_bv() {
		return id_bv;
	}

	public int getId_mav() {
		return id_mav;
	}

	public int getNuminsee_voter() {
		return numinsee_voter;
	}

	public void setNuminsee_voter(int numinsee_voter) {
		this.numinsee_voter = numinsee_voter;
	}

	public String getPassword_voter() {
		return password_voter;
	}

	public void setPassword_voter(String password_voter) {
		this.password_voter = password_voter;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}
	public void exit()
	{
		this.mainFrame.dispose();
		System.exit(0);
	}
	public void hide()
	{
		this.mainFrame.setVisible(false);
		this.mainFrame.dispose();
	}
	public void updateDescription()
	{
		this.descLabel.setText("<html><body><strong><u>DESCRIPTION<br></u></strong>"+this.cdShelf.getSelectedCandidat().description()+"</body></html>");
	}

}
