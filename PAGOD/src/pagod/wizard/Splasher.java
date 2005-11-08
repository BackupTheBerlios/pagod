

/*
 * @(#)Splasher.java  2.0  January 31, 2004
 *
 * Copyright (c) 2003-2004 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * This software is in the public domain.
 */
package pagod.wizard;
import pagod.wizard.ui.SplashWindow;
/**
 *
 * @author  yak
 */
public class Splasher {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        SplashWindow.splash(ClassLoader
                .getSystemResource("resources/images/logo.png"));
        SplashWindow.invokeMain("pagod.wizard.MainWizard", args);
        
        
        SplashWindow.disposeSplash();
    }
    
}
