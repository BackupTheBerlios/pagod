package mav.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdesktop.binding.BindingContext;
import org.jdesktop.swingx.DataBoundUtils;

import MAV.Candidat;

@SuppressWarnings("serial")
public class JXCandidateShelf extends JPanel {
    /**
     * For data binding
     */
    private String dataPath = "";
    private BindingContext ctx = null;
    private CandidateShelf shelf;

    public JXCandidateShelf() {
        super(new StackLayout());
        GradientPanel gp = new GradientPanel();
       
        add(gp, StackLayout.TOP);
        this.setBackground(Color.BLACK);
        shelf = new CandidateShelf();    
       
        add(shelf, StackLayout.TOP);
       
    }
  
    public void loadChunk(int startRow, int endRow) {
        shelf.loadChunk(startRow, endRow);
    }
    
    public void setList(List list) {
        if (list != shelf.getList()) {
        	
            shelf.setList(list);
           
        }
    }

    public List getList() {
        return shelf.getList();
    }
    
    
  
    public void addNotify() {
        super.addNotify();
      
    }

    public void removeNotify() {
        super.removeNotify();
    }
    
    //BEANS SPECIFIC CODE:
    private boolean designTime = false;
    public void setDesignTime(boolean designTime) {
        this.designTime = designTime;
    }
    public boolean isDesignTime() {
        return designTime;
    }
   /* public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (designTime && dataPath != null && !dataPath.equals("")) {
            //draw the binding icon
            ImageIcon ii = new ImageIcon(getClass().getResource("/org/jdesktop/swingx/icon/chain.png"));
            g.drawImage(ii.getImage(), getWidth() - 13, 0, 13, 13, ii.getImageObserver());
        }
    }*/
	public CandidateShelf getShelf() {
		return shelf;
	}
	public void setShelf(CandidateShelf shelf) {
		this.shelf = shelf;
	}
	public Candidat getSelectedCandidat()
    {
    	return this.shelf.getSelectedCandidat();
    }
}
