package mav.layout;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class CandidateImageCacheManager {
    private static CandidateImageCacheManager instance;
    private Map<String, Image> coversCache;
    private CDFX fx;
    private BufferedImage mask;
    
    private CandidateImageCacheManager() {
        coversCache = Collections.synchronizedMap(new HashMap<String, Image>(300));
        
        fx = new CDFX();
        BufferedImage noArtImage = null;
        try {
            noArtImage = fx.createCrystalCase(ImageIO.read(getClass().getResource("/no-artwork-cover.png")));
        } catch (IOException e) { e.printStackTrace();}
        mask = fx.createGradientMask(noArtImage.getWidth(),
                                     noArtImage.getHeight());
        noArtImage = fx.createReflectedPicture(noArtImage, mask);
        coversCache.put("nullnull", noArtImage);
    }

    public static CandidateImageCacheManager getInstance() {
        if (instance == null) {
            instance = new CandidateImageCacheManager();
        }
        return instance;
    }
    
    public Image getDefaultImage() {
        return getImage("nullnull");
    }
    
    public Image getImage(String artist, String album) {
        return getImage(artist + album);
    }
    
    public Image getImage(String id) {
        return coversCache.get(id);
    }
    
    public void putImage(String artist, String album, Image image) {
        putImage(artist + album, image);
    }
    
    public void putImage(String id, Image image) {
        coversCache.put(id, image);
    }

    public Image getImageAndCache(String key) {
    	

      
                Image  image = getImage(key);

                 if (image == null) {
                    
                        BufferedImage tempImg;
						try {
							String dir = "/candidates/"+key+".jpg";
							URL path = getClass().getResource("/candidates/"+key+".jpg");
							tempImg = ImageIO.read(path);
							  tempImg = fx.createReflectedPicture(fx.createCrystalCase(tempImg), mask);
	                             putImage(key, tempImg);
	                              image = tempImg;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                           
                              
                 }

          
         
         return image;
    }
}
