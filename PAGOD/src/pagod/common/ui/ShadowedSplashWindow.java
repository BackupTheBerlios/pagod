package pagod.common.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JWindow;

import pagod.configurator.control.Constants;
import pagod.utils.ImagesManager;

/**
 * @author Arno
 * 
 */
public class ShadowedSplashWindow extends JWindow {
	
    private BufferedImage splash = null;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     *l'instance courante
     */
    private static ShadowedSplashWindow instance;
    
    /**
     * l'image
     */
    private Icon image;
    
    /**
     * variable informant si l'application a demander a dessiner le splash screen,
     * 
     */
    private boolean paintCalled = false;
    
    /**
     * ouvre le splash screen
     * @param imageName l'image du splash screen.
     */
    public static void splash(String imageName) {
        if (instance == null && imageName != null) {
            Frame f = new Frame();
            
            // on cree la fentre avec l'image
            instance = new ShadowedSplashWindow(f, imageName);
            
            // on affiche la fenetre.
            instance.setVisible(true);
            
           //on attend jusqu'a ce que paint ait ete appel? pour que l'utilisateur ai un chance de voir le splash screen
            
            if (! EventQueue.isDispatchThread() ) {
                synchronized (instance) {
                    while (! instance.paintCalled) {
                        try { instance.wait(); } catch (InterruptedException e) {}
                    }
                }
            }
        }
    }
    
    /**
     * @param parent
     * @param imagePath
     */
    public ShadowedSplashWindow(Frame parent, String imagePath) {
    	super(parent);
        ImagesManager.getInstance()
        .setImagePath(Constants.IMAGES_DIRECTORY);
        this.image = ImagesManager.getInstance().getIcon( imagePath);
        
    	BufferedImage b_image = null;
		try
		{
			b_image = ImageIO.read(ShadowedSplashWindow.class.getResourceAsStream("/resources/images/logo.png"));
		}
		catch (IOException e)
		{
			// TODO Bloc de traitement des exceptions généré automatiquement
			e.printStackTrace();
		}
        // ShadowedSplashWindow window = new ShadowedSplashWindow(image);
        
        createShadowPicture(b_image);
        this.setVisible(true);
        
//      pour que l'utilisateur puisse ferm? les splash screen en cliquant dessus
        MouseAdapter disposeOnClick = new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
               // pour eviter que le clique n'interfre avec le paint
                synchronized(ShadowedSplashWindow.this) {
                	ShadowedSplashWindow.this.paintCalled = true;
                	ShadowedSplashWindow.this.notifyAll();
                }
                dispose();
            }
        };
        addMouseListener(disposeOnClick);
    }

    /** 
     * @see java.awt.Container#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        if (this.splash != null) {
            g.drawImage(this.splash, 0, 0, null);
        }
        // on informe l'instance que le dessin a ete demander
        if (! this.paintCalled) {
            this.paintCalled = true;
            synchronized (this) { notifyAll(); }
        }
    }
    
    private void createShadowPicture(BufferedImage b_image) {
        int width = b_image.getWidth();
        int height = b_image.getHeight();
        int extra = 14;

        setSize(new Dimension(width + extra, height + extra));
        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();

        this.splash = new BufferedImage(width + extra, height + extra, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) this.splash.getGraphics();

        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture = robot.createScreenCapture(new Rectangle(windowRect.x, windowRect.y, windowRect.width + extra, windowRect.height + extra));
            g2.drawImage(capture, null, 0, 0);
        } catch (AWTException e) { }

        BufferedImage shadow = new BufferedImage(width + extra, height + extra, BufferedImage.TYPE_INT_ARGB); 
        Graphics g = shadow.getGraphics();
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        g.fillRoundRect(6, 6, width, height, 12, 12);

        g2.drawImage(shadow, getBlurOp(7), 0, 0);
        g2.drawImage(b_image, 0, 0, this);
    }

    private ConvolveOp getBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1 / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }
    
    /**
     * appell la methode main de la classe pass? en parametre.
     * @param className Le nom de la classe dont on doit appeler le main
     * @param args les arguments de la ligne de commande
     */
    public static void invokeMain(String className, String[] args) {
        try {
            Class.forName(className)
            .getMethod("main", new Class[] {String[].class})
            .invoke(null, new Object[] {args});
        } catch (Exception e) {
            InternalError error = new InternalError("No main in the class :"+className);
            error.initCause(e);
            throw error;
        }
    }
    
    /**
     * ferme le splash screen.
     */
    public static void disposeSplash() {
        if (instance != null) {
            instance.getOwner().dispose();
            instance = null;
        }
    }
}
