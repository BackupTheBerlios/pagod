package mav.layout;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class CDFX {
    private BufferedImage cdCase;
    private BufferedImage stitch;
    private BufferedImage reflections;
    
    public CDFX() {
        loadPictures();
    }

    public BufferedImage getCoverArt(Image cover) {
        BufferedImage image = createCrystalCase(cover);
        return image;
    }
    
    public BufferedImage createCrystalCase(Image cover) {
        BufferedImage crystal = new BufferedImage(cdCase.getWidth(),
                                                  cdCase.getHeight(),
                                                  BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = crystal.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        g2.drawImage(cover, 19, 3, 240, 227, null);
        g2.drawImage(reflections, 0, 0, null);
        g2.drawImage(stitch, 19, 3, null);
        g2.drawImage(cdCase, 0, 0, null);

        g2.dispose();
        
        return crystal;
    }

    public void loadPictures() {
    	
       try {
            cdCase = ImageIO.read(getClass().getResource("/cd_case.png"));
            stitch = ImageIO.read(getClass().getResource("/stitch.png"));
            reflections = ImageIO.read(getClass().getResource("/reflections.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage createReflectedPicture(BufferedImage avatar,
                                                 BufferedImage alphaMask) {
        int avatarWidth = avatar.getWidth();
        int avatarHeight = avatar.getHeight();

        BufferedImage buffer = createReflection(avatar,
                                                avatarWidth, avatarHeight);

        applyAlphaMask(buffer, alphaMask, avatarWidth, avatarHeight);

        return buffer;
    }

    public void applyAlphaMask(BufferedImage buffer,
                                BufferedImage alphaMask,
                                int avatarWidth, int avatarHeight) {

        Graphics2D g2 = buffer.createGraphics();
        g2.setComposite(AlphaComposite.DstOut);
        g2.drawImage(alphaMask, null, 0, avatarHeight);
        g2.dispose();
    }

    public BufferedImage createReflection(BufferedImage avatar,
                                           int avatarWidth,
                                           int avatarHeight) {

        BufferedImage buffer = new BufferedImage(avatarWidth, avatarHeight << 1,
                                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();

        g.drawImage(avatar, null, null);
        g.translate(0, avatarHeight << 1);

        AffineTransform reflectTransform = AffineTransform.getScaleInstance(1.0, -1.0);
        g.drawImage(avatar, reflectTransform, null);
        g.translate(0, -(avatarHeight << 1));
        
        g.dispose();

        return buffer;
    }

    public BufferedImage createGradientMask(int avatarWidth, int avatarHeight) {
        BufferedImage gradient = new BufferedImage(avatarWidth, avatarHeight,
                                                   BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = gradient.createGraphics();
        GradientPaint painter = new GradientPaint(0.0f, 0.0f,
                                                  new Color(1.0f, 1.0f, 1.0f, 0.5f),
                                                  0.0f, avatarHeight / 2.0f,
                                                  new Color(1.0f, 1.0f, 1.0f, 1.0f));
        g.setPaint(painter);
        g.fill(new Rectangle2D.Double(0, 0, avatarWidth, avatarHeight));
        
        g.dispose();

        return gradient;
    }
}

