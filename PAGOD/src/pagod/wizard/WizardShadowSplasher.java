

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
import pagod.common.ui.ShadowedSplashWindow;
/**
 *
 * @author  yak
 */
public class WizardShadowSplasher {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
    	ShadowedSplashWindow.splash("logo.gif");
    	ShadowedSplashWindow.invokeMain("pagod.wizard.MainWizard", args);

    	ShadowedSplashWindow.disposeSplash();
    }
    
}
