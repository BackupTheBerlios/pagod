
package pagod.wizard.ui;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

import pagod.configurator.control.Constants;
import pagod.utils.ImagesManager;

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
     *l'instance courante
     */
    private static SplashWindow instance;
    
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
     * constructeur
     * @param parent le parent de la fenetre
     * @param imagePath l'image du splashscreen.
     * 
     */
    private SplashWindow(Frame parent, String imagePath) {
        super(parent);
        ImagesManager.getInstance()
        .setImagePath(Constants.IMAGES_DIRECTORY);
        this.image = ImagesManager.getInstance().getIcon( imagePath);
       
        // chargement de l'image
        
        // centre l'image sur l'ecrant
        int imgWidth = this.image.getIconWidth();
        int imgHeight = this.image.getIconHeight();
        setSize(imgWidth, imgHeight);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
        (screenDim.width - imgWidth) / 2,
        (screenDim.height - imgHeight) / 2
        );
        
        //pour que l'utilisateur puisse ferm? les splash screen en cliquant dessus
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
    	this.image.paintIcon(this,g,0,0);
       // g.drawImage(this.image., 0, 0, this);
       
        
        // on informe l'instance que le dessin a ete demander
        if (! this.paintCalled) {
            this.paintCalled = true;
            synchronized (this) { notifyAll(); }
        }
    }
  
    /**
     * ouvre le splash screen
     * @param imageName l'image du splash screen.
     */
    public static void splash(String imageName) {
        if (instance == null && imageName != null) {
            Frame f = new Frame();
            
            // on cree la fentre avec l'image
            instance = new SplashWindow(f, imageName);
            
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
     * ferme le splash screen.
     */
    public static void disposeSplash() {
        if (instance != null) {
            instance.getOwner().dispose();
            instance = null;
        }
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
}
