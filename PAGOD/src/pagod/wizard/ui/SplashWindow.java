
package pagod.wizard.ui;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * @author yak
 *
 */
public class SplashWindow extends Window {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The current instance of the splash window.
     * (Singleton design pattern).
     */
    private static SplashWindow instance;
    
    /**
     * The splash image which is displayed on the splash window.
     */
    private Image image;
    
    /**
     * variable informant si l'application a demander a dessiner le splash screen,
     * 
     */
    private boolean paintCalled = false;
    
    /**
     * constructeur
     * @param parent le parent de la fenetre
     * @param image l'image du splashscreen.
     */
    private SplashWindow(Frame parent, Image image) {
        super(parent);
        this.image = image;

        // chargement de l'image
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image,0);
        try {
            mt.waitForID(0);
        } catch(InterruptedException ie){}
        
        // centre l'image sur l'ecrant
        int imgWidth = image.getWidth(this);
        int imgHeight = image.getHeight(this);
        setSize(imgWidth, imgHeight);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
        (screenDim.width - imgWidth) / 2,
        (screenDim.height - imgHeight) / 2
        );
        
        //pour que l'utilisateur puisse fermé les splash screen en cliquant dessus
        MouseAdapter disposeOnClick = new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
               // pour eviter que le clique n'interfre avec le paint
                synchronized(SplashWindow.this) {
                    SplashWindow.this.paintCalled = true;
                    SplashWindow.this.notifyAll();
                }
                dispose();
            }
        };
        addMouseListener(disposeOnClick);
        
    }
    
    /**
     *  met a jour l'affichage
     * @param g 
     */
    public void update(Graphics g) {
        // empeche le clignotement
        paint(g);
    }
    /**
     * dessine a l'ecran
     * @param g 
     */
    public void paint(Graphics g) {
        g.drawImage(this.image, 0, 0, this);
       
        
        // on informe l'instance que le dessin a ete demander
        if (! this.paintCalled) {
            this.paintCalled = true;
            synchronized (this) { notifyAll(); }
        }
    }
  
    /**
     * ouvre le splash screen
     * @param image l'image du splash screen.
     */
    public static void splash(Image image) {
        if (instance == null && image != null) {
            Frame f = new Frame();
            
            // on cree la fentre avec l'image
            instance = new SplashWindow(f, image);
            
            // on affiche la fenetre.
            instance.setVisible(true);
            
           //on attend jusqu'a ce que paint ait ete appelé pour que l'utilisateur ai un chance de voir le splash screen
            
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
     * ouvre le splash screen
     * @param imageURL l'url de l'image.
     */
    public static void splash(URL imageURL) {
        if (imageURL != null) {
            splash(Toolkit.getDefaultToolkit().createImage(imageURL));
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
    
    /**
     * appell la methode main de la classe passé en parametre.
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
}
