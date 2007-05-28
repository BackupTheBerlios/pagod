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
import MAV.SRVPackage.InternalErrorException;

@SuppressWarnings("serial")
public class UserConnectionDialog extends JDialog {

	JTextField idMav;
	JTextField idBV;
	SRV srvRef;
	
	MachineAVoter theMav;
	public UserConnectionDialog(MachineAVoter mav, SRV srvRef)
	{
		theMav = mav;
		this.srvRef = srvRef;
		
	}
	public void init()
	{

		JButton button;
		JLabel label;
		JPanel panel = new JPanel();
		
		JPanel PanelButton = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(
                10, //top
                10,     //left
                10, //bottom
                10));   //right
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		 c.anchor = GridBagConstraints.WEST;
		 
		label = new JLabel("Nom : ");
		c.weightx = 0.0 ;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);

		this.idMav = new JTextField();
		this.idMav.setColumns(20);
		c.weightx =1.0;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(this.idMav, c);

		label = new JLabel("Password : ");
		
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		
		panel.add(label, c);

		this.idBV = new JTextField("");
		this.idBV.setColumns(20);
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		panel.add(this.idBV, c);
		
		button = new JButton ("Ok");
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				int id_mav = Integer.parseInt(UserConnectionDialog.this.idMav.getText());
				int id_bv = Integer.parseInt(UserConnectionDialog.this.idBV.getText());
				try {
					if (srvRef.authMAV(id_mav, id_bv))
					{
						UserConnectionDialog.this.theMav.connect();
						UserConnectionDialog.this.setVisible(false);
						UserConnectionDialog.this.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(UserConnectionDialog.this, "Machine à voter non identifier","Erreur de connexion",JOptionPane.ERROR_MESSAGE);
					}
				} catch (InternalErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}});
		PanelButton.add(button);
		button = new JButton ("cancel");
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				UserConnectionDialog.this.setVisible(false);
				UserConnectionDialog.this.dispose();
				
			}});
		PanelButton.add(button);
		
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		this.add(PanelButton, BorderLayout.CENTER);
		this.pack();
		this.setTitle("Connexion de la machine à voter");
	}
}
