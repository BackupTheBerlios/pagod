package vtr;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import MAV.ResultVote;
import MAV.SRV;
import MAV.SRVHelper;
import MAV.SRVPackage.InternalErrorException;
import constants.Constants;

public class FenetreVTR extends JFrame {

	private VTRServant vtrServant;

	private ORB orb;

	private NamingContext ncRef;

	List srvList;

	// le vtr auquelle est connecté le VTR
	private SRV srvRef;

	private JTable table;

	private JButton btConnect;

	private JButton btDisconnect;

	// la liste des srv
	private JComboBox comboBox;

	private ActionListener btListener;

	public FenetreVTR() {
		try {
			// create and initialize the ORB
			this.orb = ORB.init(new String[0], null);

			// get the root naming context
			org.omg.CORBA.Object objRef;

			objRef = this.orb.resolve_initial_references("NameService");
			this.ncRef = NamingContextHelper.narrow(objRef);

			this.vtrServant = new VTRServant();

			// on recupere la liste des srv
			this.srvList = listBind(ncRef, Constants.SRV_KIND);

			// creation de l'ihm

			// creation de la combo qui affiche la liste des srv
			comboBox = new JComboBox();
			// on remplit la combo
			for (Iterator it = this.srvList.iterator(); it.hasNext();) {
				comboBox.addItem(it.next());
			}

			// creation des boutons
			this.btConnect = new JButton("Connecter");
			this.btDisconnect = new JButton("Déconnecter");
			// on grise le bouton de déconnecté
			FenetreVTR.this.btDisconnect.setEnabled(false);

			// ajout des listener sur les boutons
			this.btListener = new BtVTRListener();
			this.btConnect.addActionListener(this.btListener);
			this.btDisconnect.addActionListener(this.btListener);

			// creation du panneau contenant la combo pour ce connecter ou se
			// deconnecter d'un srv
			JPanel northPanel = new JPanel();
			northPanel.setLayout(new FlowLayout());

			// ajout au panneau sud de la combo ainsi que des boutons
			northPanel.add(this.comboBox);
			northPanel.add(this.btConnect);
			northPanel.add(this.btDisconnect);

			// creation d'une jtabel
			this.table = new JTable();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			// paramètrage de la fenetre
			this.setTitle("VTR");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLocation(0, 0);
			this.setLayout(new BorderLayout());

			// ajout des elements a la fenetre
			this.add(northPanel, BorderLayout.NORTH);
			this.add(scrollPane, BorderLayout.CENTER);

			this.setVisible(true);
			this.pack();
		} catch (InvalidName e) {
			JOptionPane
					.showMessageDialog(
							FenetreVTR.this,
							"Problème Corba",
							"Impossible d'initialiser l'orb corba ou de récuperer le namingContext.",
							JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Fonction qui permet de se connecter a un bureau de vote dont
	 * l'identifiant est passé en paramètre.
	 * 
	 * @param idBV -
	 *            l'identifiant du bureau de vote
	 * @return le SRV
	 * @throws Exception
	 * 
	 */
	private void connectBV(String BVNameComponent) throws Exception {

		// resolve the Object Reference in Naming
		NameComponent nc = new NameComponent(BVNameComponent,
				Constants.SRV_KIND);
		NameComponent path[] = { nc };
		this.srvRef = SRVHelper.narrow(this.ncRef.resolve(path));
	}

	/**
	 * Retourne la liste de tous les bind qui sont du type passé en paramètre
	 * 
	 * @param namingContext -
	 *            le namingContext
	 * @param kind -
	 *            le type de bind que l'on souhaite récuperer
	 * 
	 * @return La liste des identifiants des bind qui sont du type passé en
	 *         paramètre. (List<String>)
	 */
	private static List listBind(NamingContext namingContext, String king) {
		List l = new ArrayList();

		BindingListHolder bl = new BindingListHolder();
		BindingIteratorHolder bi = new BindingIteratorHolder();
		namingContext.list(50, bl, bi);
		Binding[] bindings = bl.value;
		for (int i = 0; i < bindings.length; i++) {
			NameComponent[] nameComponent = bindings[i].binding_name;
			for (int j = 0; j < nameComponent.length; j++) {
				if (nameComponent[j].kind.equals(king)) {
					l.add(nameComponent[j].id);
				}
			}
		}

		return l;
	}

	/**
	 * Classe qui permet d'effectuer les actions lorsque l'utilisateur du vtr
	 * clique sur les boutons connect et disconnect.
	 * 
	 */
	private class BtVTRListener implements ActionListener {

		// methode appelé quand on clique sur un bouton
		public void actionPerformed(ActionEvent e) {

			// si on a cliqué sur le bouton connecté
			if (e.getSource() == FenetreVTR.this.btConnect) {
				System.out.println("Clique sur btconnect");

				// on se connecte au srv selectionné dans la combo
				String BVNameComponent = (String) FenetreVTR.this.comboBox
						.getSelectedItem();
				try {
					FenetreVTR.this.connectBV(BVNameComponent);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(FenetreVTR.this,
							"Problème de connection",
							"Impossible de se connecter au bureau de vote "
									+ BVNameComponent,
							JOptionPane.ERROR_MESSAGE);
				}

				// si srvRef est different de null c'est qu'on est connecté
				// dessus
				if (FenetreVTR.this.srvRef != null) {
					// on enregistre le vtr pour qu'il soit notifié
					FenetreVTR.this.srvRef
							.enregistrerVTR(FenetreVTR.this.vtrServant);

					// on recupere les resultats courants
					ResultVote[] resultVote;
					try {
						resultVote = FenetreVTR.this.srvRef.listeResultat();
						vtrServant.setResultVote(resultVote);

						// on rend visible la JTable car elle va etre acutalisé
						FenetreVTR.this.table.setVisible(true);

						// on change le model de la jTable
						FenetreVTR.this.table.setModel(vtrServant
								.getResultVoteTableModel());

						// on grise la combo
						FenetreVTR.this.comboBox.setEnabled(false);

						// on grise le bouton de connection
						FenetreVTR.this.btConnect.setEnabled(false);

						// on dégrise le bouton de déconnecté
						FenetreVTR.this.btDisconnect.setEnabled(true);
					} catch (InternalErrorException e1) {
						JOptionPane.showMessageDialog(FenetreVTR.this,
								"Problème de consultation des resultats",
								"Impossible de consulter les resultats du bureau de vote "
										+ BVNameComponent,
								JOptionPane.ERROR_MESSAGE);

						// vu qu'il y a un problème pour consulter les resultats
						// on se desenregistre
						FenetreVTR.this.srvRef
								.desenristrerVTR(FenetreVTR.this.vtrServant);
						FenetreVTR.this.srvRef = null;
					}

				}
			}

			// si on a cliqué sur le bouton déconnecté
			if (e.getSource() == FenetreVTR.this.btDisconnect) {
				System.out.println("Clique sur btDisconnect");

				// on se desenregistre du srv afin de ne plus recevoir de
				// notification
				FenetreVTR.this.srvRef
						.desenristrerVTR(FenetreVTR.this.vtrServant);

				// on remet a null le srv auquel on est connecté
				FenetreVTR.this.srvRef = null;

				// on rend invisible la JTable car son contenu n'est plus
				// actualisé au fur et a mesure
				FenetreVTR.this.table.setVisible(false);

				// on dégrise la combo
				FenetreVTR.this.comboBox.setEnabled(true);

				// on dégrise le bouton de connection
				FenetreVTR.this.btConnect.setEnabled(true);

				// on grise le bouton de déconnecté
				FenetreVTR.this.btDisconnect.setEnabled(false);

			}

		}

	}
}
