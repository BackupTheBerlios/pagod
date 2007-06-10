package mav.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;



public class ZoomyPanel extends JPanel {
    //private static final double ZOOM_FACTOR = 0.5;
    private ZoomyBackground background;
    //private double zoom = 0.0;

    public ZoomyPanel() {
        this.background = new ZoomyBackground();
//        FrequenciesDispatcher.getInstance().addProcessor(this);
    }
    
    protected void paintComponent(Graphics g) {
        if (!isVisible()) {
            return;
        }
        
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Graphics2D g2 = (Graphics2D) g;
//        double sizeCoeff = ZOOM_FACTOR * zoom + 1.0;
//        g2.scale(sizeCoeff, sizeCoeff);
//        g2.translate((getWidth() - getWidth() * sizeCoeff) / 2.0,
//                     (getHeight() - getHeight() * sizeCoeff) / 2.0);
        
        background.render(getSize(), g2);
    }

    public void init() {
        background.init();
        background.refreshSource();
        background.makeDisplacementMap();
    }

    public void process(float[] leftData, float[] rightData) {
        //zoom = Math.sin((leftData[0] + rightData[0]) / 2.0f);
    }
}
