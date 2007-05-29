package mav.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mav.MachineAVoter;
import MAV.SRV;
import MAV.SRVPackage.AlreadyVoteException;
import MAV.SRVPackage.BadAuthentificationException;
import MAV.SRVPackage.IncorrectBVPersonException;
import MAV.SRVPackage.InternalErrorException;

@SuppressWarnings("serial")
public class UserConnectionDialog extends JDialog {

	JTextField nom;

	JTextField password;

	SRV srvRef;

	MachineAVoter theMav;

	public UserConnectionDialog(MachineAVoter mav, SRV srvRef) {
		super(mav.getMainFrame());
		this.init();
		theMav = mav;
		this.srvRef = srvRef;
		this.setLocationRelativeTo(mav.getMainFrame());

	}

	public void init() {

		JButton button;
		JLabel label;
		JPanel panel = new JPanel();

		JPanel PanelButton = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, // top
				10, // left
				10, // bottom
				10)); // right
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;

		label = new JLabel("Numéro insee : ");
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);

		this.nom = new JTextField();
		this.nom.setColumns(20);
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(this.nom, c);

		label = new JLabel("Password : ");

		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;

		panel.add(label, c);

		this.password = new JTextField("");
		this.password.setColumns(20);
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		panel.add(this.password, c);

		button = new JButton("Ok");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int numinsee = Integer.parseInt(UserConnectionDialog.this.nom
						.getText());

				try {
					try {
						srvRef.authPersonne(numinsee,
								UserConnectionDialog.this.password.getText(),
								UserConnectionDialog.this.theMav.getId_bv());
						UserConnectionDialog.this.theMav
								.setNuminsee_voter(numinsee);
						UserConnectionDialog.this.theMav
								.setPassword_voter(UserConnectionDialog.this.password
										.getText());
						UserConnectionDialog.this.setVisible(false);
						UserConnectionDialog.this.dispose();
					} catch (BadAuthentificationException e1) {
						JOptionPane.showMessageDialog(
								UserConnectionDialog.this, "Personne inconnue",
								"Erreur de connexion",
								JOptionPane.ERROR_MESSAGE);

					} catch (AlreadyVoteException e1) {
						JOptionPane.showMessageDialog(
								UserConnectionDialog.this,
								"Vous avez deja voter", "Erreur de connexion",
								JOptionPane.ERROR_MESSAGE);

					} catch (IncorrectBVPersonException e1) {
						JOptionPane.showMessageDialog(
								UserConnectionDialog.this, "Bureau de vote "
										+ UserConnectionDialog.this.theMav
												.getId_bv() + " inconnu",
								"Erreur de connexion",
								JOptionPane.ERROR_MESSAGE);

					}

				} catch (InternalErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		PanelButton.add(button);
		button = new JButton("cancel");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				UserConnectionDialog.this.setVisible(false);
				UserConnectionDialog.this.dispose();

			}
		});
		PanelButton.add(button);

		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		this.add(PanelButton, BorderLayout.CENTER);
		this.pack();
		this.setTitle("Connexion de la personne");
	}
}
