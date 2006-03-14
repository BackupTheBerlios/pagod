

package pagod.utils.editor.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pagod.utils.ImagesManager;
import pagod.utils.LanguagesManager;

/**
 * 
 * @author Arno
 *
 */
public class PagodEditorAboutDialog extends JFrame implements ActionListener
{
    /** Bouton OK */
    private JButton pbOk = null;

   /**
    * 
    * @param sVersion
    */
    public PagodEditorAboutDialog(String sVersion,JFrame fParent)
    {

        this.pbOk = new JButton("OK");

        GridBagConstraints gridBagConstraints;

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        // Boutton par défaut : bouton "OK" (=> touche ENTER = bouton OK)
        this.pbOk.setDefaultCapable(true);
        this.getRootPane().setDefaultButton(this.pbOk);

        this.pbOk.addActionListener(this);

        southPanel.add(this.pbOk);

        // contenu du about
        JLabel lbl;

        lbl = new JLabel(ImagesManager.getInstance().getIcon("logoPAGOD.png"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
        "aboutEditorWhatIsPagodEditorDialog"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogLicence"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        centerPanel.add(lbl, gridBagConstraints);
        
        /*
        lbl = new JLabel("<html>Authors:<ul><li>Arnaud GIULIANI</li></ul></html>");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        centerPanel.add(lbl, gridBagConstraints);*/

        lbl = new JLabel(LanguagesManager.getInstance().getString(
                "aboutDialogUsedComponents"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        centerPanel.add(lbl, gridBagConstraints);

        lbl = new JLabel(sVersion);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(lbl, gridBagConstraints);

        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        // boîte de dialogue modale et centrée par rapport à l'appelant
        this.setLocation(fParent.getX()+((fParent.getWidth()-this.getWidth())/4) , fParent.getY()+((fParent.getHeight()-this.getHeight())/4));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        // clic sur OK --> fermer la fenêtre
        if (e.getSource() == this.pbOk)
            dispose();
    }

}