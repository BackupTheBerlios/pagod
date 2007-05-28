/**
 * Copyright (c) 2004, Sun Microsystems, Inc All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the Ping demo
 * project nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package mav.layout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Random;

/**
 * ZoomyBackground
 * 
 * Another implementation of the background. Note that this animation is fps
 * based as opposed to time-based: it calculates and renders a new frame on each
 * render call, not taking into account how much time has passed since the last
 * frame.
 * 
 * @author Jim Graham
 */

public class ZoomyBackground {
    static Random rand = new Random();

    BufferedImage bimg1;
    BufferedImage bimg2;

    Image offscr;
    int imgarray1[];
    int imgarray2[];

    int scan1;
    int scan2;

    public static final int DISP_SHIFT = 12;
    public static final int DISP_ONE = (1 << DISP_SHIFT);
    public static final int DISP_FRACTMASK = DISP_ONE - 1;

    int dispmap[];
    int maptype;

    public ZoomyBackground() {
        init();
    }

    public void init() {
        maptype = rand.nextInt(1 << 16);
    }

    public synchronized void refreshSource() {
        Graphics2D g2d = bimg1.createGraphics();
        int w = bimg1.getWidth();
        int h = bimg1.getHeight();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
    }

    static final DispMapMaker makers[] = {
                                          new ZoomMapMaker(5000, 10.0, 1.0,
                                                           true),
                                          new RandomMapMaker(8000, 2.0),
                                          new TranslateMapMaker(3000, .25, .5),
                                          new SinMapMaker(8000, 15.0, 15.0),
                                          new VortexMapMaker(5000, 5.0), };

    public synchronized void makeDisplacementMap() {
        if (bimg1 == null) {
            return;
        }
        int w = bimg1.getWidth();
        int h = bimg1.getHeight();
        int len = w * h * 2;
        if (dispmap == null || dispmap.length != len) {
            dispmap = new int[len];
        }

        int type = maptype % makers.length;
        if (type < 0) {
            type += makers.length;
        }
        DispMapMaker dmm = makers[type];
        dmm.makeMap(dispmap, w, h);
    }

    public static abstract class DispMapMaker {
        long timeout;

        public DispMapMaker(long timeout) {
            this.timeout = timeout;
        }

        public long getTimeOut() {
            return timeout;
        }

        public static void clipValidateDisplacementMap(int dispmap[], int w,
                                                       int h) {
            int index = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int dx = dispmap[index] >> DISP_SHIFT;
                    int dy = dispmap[index + 1] >> DISP_SHIFT;
                    int sx = x + dx;
                    int sy = y + dy;
                    if (sx < 0) {
                        dispmap[index] = ((0 - x) << DISP_SHIFT);
                    } else if (sx > w - 2) {
                        dispmap[index] = (((w - 1 - x) << DISP_SHIFT) - 1);
                    }
                    if (sy < 0) {
                        dispmap[index + 1] = ((0 - y) << DISP_SHIFT);
                    } else if (sy > h - 2) {
                        dispmap[index + 1] = (((h - 1 - y) << DISP_SHIFT) - 1);
                    }
                    index += 2;
                }
            }
        }

        public static void wrapValidateDisplacementMap(int dispmap[], int w,
                                                       int h) {
            int index = 0;
            int wmax = (w - 1) << DISP_SHIFT;
            int hmax = (h - 1) << DISP_SHIFT;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int d = dispmap[index] + (x << DISP_SHIFT);
                    if (d < 0) {
                        do {
                            d += wmax;
                        } while (d < 0);
                    } else {
                        while (d >= wmax) {
                            d -= wmax;
                        }
                    }
                    dispmap[index++] = d - (x << DISP_SHIFT);
                    d = dispmap[index] + (y << DISP_SHIFT);
                    if (d < 0) {
                        do {
                            d += hmax;
                        } while (d < 0);
                    } else {
                        while (d >= hmax) {
                            d -= hmax;
                        }
                    }
                    dispmap[index++] = d - (y << DISP_SHIFT);
                }
            }
        }

        public abstract void makeMap(int dispmap[], int w, int h);
    }

    public static class ZoomMapMaker extends DispMapMaker {
        double xfactor;

        double yfactor;

        boolean xdependsony;

        public ZoomMapMaker(long timeout, double xfactor, double yfactor,
                            boolean xdependsony) {
            super(timeout);
            this.xfactor = xfactor;
            this.yfactor = yfactor;
            this.xdependsony = xdependsony;
        }

        public void makeMap(int dispmap[], int w, int h) {
            double halfh = h / 2.0;
            double halfw = w / 2.0;
            int index = 0;
            for (int y = 0; y < h; y++) {
                double ydisp = yfactor * (halfh - y) / halfh;
                double xmax = xdependsony ? Math.abs(ydisp) + 1.0 : xfactor;
                for (int x = 0; x < w; x++) {
                    double xdisp = xmax * (halfw - x) / halfw;
                    dispmap[index++] = (int) (xdisp * DISP_ONE);
                    dispmap[index++] = (int) (ydisp * DISP_ONE);
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class TwistMapMaker extends DispMapMaker {
        double degrees;

        double scale;

        public TwistMapMaker(long timeout, double degrees, double scale) {
            super(timeout);
            this.degrees = degrees;
            this.scale = scale;
        }

        public void makeMap(int dispmap[], int w, int h) {
            AffineTransform at = new AffineTransform();
            at.translate(w / 2.0, h / 2.0);
            at.rotate(Math.toRadians(degrees));
            at.scale(scale, scale);
            at.translate(-w / 2.0, -h / 2.0);
            double coords[] = new double[2];
            int index = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    coords[0] = x;
                    coords[1] = y;
                    at.transform(coords, 0, coords, 0, 1);
                    dispmap[index++] = (int) ((coords[0] - x) * DISP_ONE);
                    dispmap[index++] = (int) ((coords[1] - y) * DISP_ONE);
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class RandomMapMaker extends DispMapMaker {
        double maxdisp = 2;

        public RandomMapMaker(long timeout, double maxdisp) {
            super(timeout);
            this.maxdisp = maxdisp;
        }

        public void makeMap(int dispmap[], int w, int h) {
            int index = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int r = rand.nextInt();
                    int dx = r >> (32 - DISP_SHIFT);
                    r <<= DISP_SHIFT;
                    int dy = r >> (32 - DISP_SHIFT);
                    dispmap[index++] = (int) (dx * maxdisp);
                    dispmap[index++] = (int) (dy * maxdisp);
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class SinMapMaker extends DispMapMaker {
        double xamplitude;

        double yamplitude;

        public SinMapMaker(long timeout, double xamplitude, double yamplitude) {
            super(timeout);
            this.xamplitude = xamplitude * DISP_ONE;
            this.yamplitude = yamplitude * DISP_ONE;
        }

        public void makeMap(int dispmap[], int w, int h) {
            int index;
            index = 1;
            for (int x = 0; x < w; x++) {
                double angle = Math.PI * 2.0 * x / (w / 4.0);
                dispmap[index] = (int) (Math.sin(angle) * xamplitude);
                index += 2;
            }
            index = 0;
            for (int y = 0; y < h; y++) {
                double angle = Math.PI * 2.0 * y / (h / 4.0);
                dispmap[index] = (int) (Math.sin(angle) * yamplitude);
                index += w * 2;
            }
            index = 0;
            for (int y = 0; y < h; y++) {
                int xval = dispmap[index];
                for (int x = 0; x < w; x++) {
                    dispmap[index++] = xval;
                    dispmap[index++] = dispmap[x * 2 + 1];
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class TranslateMapMaker extends DispMapMaker {
        int xamplitude;

        int yamplitude;

        public TranslateMapMaker(long timeout, double xamplitude,
                                 double yamplitude) {
            super(timeout);
            this.xamplitude = (int) (xamplitude * DISP_ONE);
            this.yamplitude = (int) (yamplitude * DISP_ONE);
        }

        public void makeMap(int dispmap[], int w, int h) {
            int index = 0;
            int xamplitude = this.xamplitude;
            int yamplitude = this.yamplitude;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    dispmap[index++] = xamplitude;
                    dispmap[index++] = yamplitude;
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class VortexMapMaker extends DispMapMaker {
        double scale;

        public VortexMapMaker(long timeout, double scale) {
            super(timeout);
            this.scale = scale;
        }

        public void makeMap(int dispmap[], int w, int h) {
            int index = 0;
            double scale = this.scale * DISP_ONE;
            for (int y = 0; y < h; y++) {
                int yrel = y - h / 2;
                int ydist = yrel * yrel;
                for (int x = 0; x < w; x++) {
                    int xrel = x - w / 2;
                    int dist = ydist + xrel * xrel;
                    if (dist == 0) {
                        dispmap[index++] = 0;
                        dispmap[index++] = 0;
                    } else {
                        double relscale = scale / Math.sqrt(dist);
                        dispmap[index++] = (int) (yrel * relscale);
                        dispmap[index++] = (int) (-xrel * relscale);
                    }
                }
            }
            wrapValidateDisplacementMap(dispmap, w, h);
        }
    }

    public static class FunkyLine {
        private static Color colors[];

        private static int hueBase;

        private int hueOffset;

        private int coords[];

        private int deltas[];

        public void init(int w, int h) {
            if (colors == null) {
                colors = new Color[256];
                for (int i = 0; i < 256; i++) {
                    colors[i] = Color.getHSBColor(i / 256f, 1f, 1f);
                }
            }
            if (coords == null || deltas == null) {
                coords = new int[8];
                deltas = new int[8];
            }
            int lim = w;
            for (int i = 0; i < coords.length; i++) {
                coords[i] = rand.nextInt(lim);
                deltas[i] = rand.nextInt(5) - 2;
                lim = (w + h) - lim;
            }
            hueOffset = rand.nextInt(256 / 8);
        }

        public static void nextSet() {
            hueBase = (int) (System.currentTimeMillis() >> 7);
        }

        public synchronized void render(Graphics2D g2d, int w, int h) {
            if (coords == null || deltas == null) {
                init(w, h);
            }
            int lim = w;
            int numlims = 0;
            for (int i = 0; i < coords.length; i++) {
                int c = coords[i];
                int d = deltas[i];
                d += rand.nextInt(3) - 1;
                int speedlim = Math.min(Math.max(1, lim / 100), 5);
                if (d > speedlim) {
                    d = speedlim;
                } else if (d < -speedlim) {
                    d = -speedlim;
                }
                c += d;
                if (c < -10) {
                    d = -d;
                    numlims++;
                } else if (c >= lim + 10) {
                    d = -d;
                    numlims++;
                }
                coords[i] = c;
                deltas[i] = d;
                lim = (w + h) - lim;
            }
            if (numlims > coords.length / 2) {
                init(w, h);
            }
            g2d.setColor(colors[(hueBase + hueOffset) & 0xff]);
            g2d.draw(new CubicCurve2D.Float(coords[0], coords[1], coords[2],
                                            coords[3], coords[4], coords[5],
                                            coords[6], coords[7]));
        }
    }

    private FunkyLine lines[];

    public synchronized void renderLines() {
        if (bimg1 == null) {
            return;
        }
        if (lines == null) {
            lines = new FunkyLine[5];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new FunkyLine();
            }
        }
        FunkyLine.nextSet();
        Graphics2D g2d = bimg1.createGraphics();
        int w = bimg1.getWidth();
        int h = bimg1.getHeight();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < lines.length; i++) {
            lines[i].render(g2d, w, h);
        }
        g2d.dispose();
    }

    public synchronized void displace() {
        if (dispmap == null) {
            return;
        }
        int mapindex = 0;
        int index = 0;
        int w = bimg1.getWidth();
        int h = bimg1.getHeight();
        int imgarray1[] = this.imgarray1;
        int imgarray2[] = this.imgarray2;
        int dispmap[] = this.dispmap;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int dx = dispmap[mapindex++];
                int dy = dispmap[mapindex++];
                int sindex = ((y + (dy >> DISP_SHIFT)) * scan1 + (x + (dx >> DISP_SHIFT)));
                dx &= DISP_FRACTMASK;
                dy &= DISP_FRACTMASK;

                int rgb = imgarray1[sindex + scan1 + 1];
                int factor = dx * dy;
                int b = (rgb & 0xff) * factor;
                int g = ((rgb >> 8) & 0xff) * factor;
                int r = ((rgb >> 16) & 0xff) * factor;

                rgb = imgarray1[sindex + scan1];
                factor = (dy << DISP_SHIFT) - factor;
                b += (rgb & 0xff) * factor;
                g += ((rgb >> 8) & 0xff) * factor;
                r += ((rgb >> 16) & 0xff) * factor;

                rgb = imgarray1[sindex];
                factor = ((DISP_ONE - dx) << DISP_SHIFT) - factor;
                b += (rgb & 0xff) * factor;
                g += ((rgb >> 8) & 0xff) * factor;
                r += ((rgb >> 16) & 0xff) * factor;

                rgb = imgarray1[sindex + 1];
                factor = ((DISP_ONE - dy) << DISP_SHIFT) - factor;
                b += (rgb & 0xff) * factor;
                g += ((rgb >> 8) & 0xff) * factor;
                r += ((rgb >> 16) & 0xff) * factor;

                imgarray2[index++] = (((r >>> (DISP_SHIFT * 2)) << 16)
                                      + ((g >>> (DISP_SHIFT * 2)) << 8) + ((b >>> (DISP_SHIFT * 2)) << 0));
            }
            index += (scan2 - w);
        }
        BufferedImage tmp = bimg1;
        bimg1 = bimg2;
        bimg2 = tmp;
        this.imgarray1 = imgarray2;
        this.imgarray2 = imgarray1;
        int scan = scan1;
        scan2 = scan1;
        scan1 = scan;
    }

    public synchronized void Xdisplace() {
        if (dispmap == null) {
            return;
        }
        int mapindex = 0;
        int index = 0;
        int w = bimg1.getWidth();
        int h = bimg1.getHeight();
        int imgarray1[] = this.imgarray1;
        int imgarray2[] = this.imgarray2;
        int dispmap[] = this.dispmap;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int dx = dispmap[mapindex++];
                int dy = dispmap[mapindex++];
                int sindex = ((y + (dy >> DISP_SHIFT)) * scan1 + (x + (dx >> DISP_SHIFT)));
                dx &= DISP_FRACTMASK;
                dy &= DISP_FRACTMASK;

                int rgbul = imgarray1[sindex];
                int rgbur = imgarray1[sindex + 1];
                int rgbll = imgarray1[sindex + scan1];
                int rgblr = imgarray1[sindex + scan1 + 1];

                int rgb, ul, ur, ll, lr;

                ul = (rgbul) & 0xff;
                ur = (rgbur) & 0xff;
                ll = (rgbll) & 0xff;
                lr = (rgblr) & 0xff;
                ul = (ul << DISP_SHIFT) + (ur - ul) * dx;
                ll = (ll << DISP_SHIFT) + (lr - ll) * dx;
                ul = (ul << DISP_SHIFT) + (ll - ul) * dy;
                rgb = (ul >>> (DISP_SHIFT * 2));

                ul = (rgbul >> 8) & 0xff;
                ur = (rgbur >> 8) & 0xff;
                ll = (rgbll >> 8) & 0xff;
                lr = (rgblr >> 8) & 0xff;
                ul = (ul << DISP_SHIFT) + (ur - ul) * dx;
                ll = (ll << DISP_SHIFT) + (lr - ll) * dx;
                ul = (ul << DISP_SHIFT) + (ll - ul) * dy;
                rgb |= (ul >>> (DISP_SHIFT * 2)) << 8;

                ul = (rgbul >> 16) & 0xff;
                ur = (rgbur >> 16) & 0xff;
                ll = (rgbll >> 16) & 0xff;
                lr = (rgblr >> 16) & 0xff;
                ul = (ul << DISP_SHIFT) + (ur - ul) * dx;
                ll = (ll << DISP_SHIFT) + (lr - ll) * dx;
                ul = (ul << DISP_SHIFT) + (ll - ul) * dy;
                rgb |= (ul >>> (DISP_SHIFT * 2)) << 16;

                imgarray2[index++] = rgb;
            }
            index += (scan2 - w);
        }
        BufferedImage tmp = bimg1;
        bimg1 = bimg2;
        bimg2 = tmp;
        this.imgarray1 = imgarray2;
        this.imgarray2 = imgarray1;
        int scan = scan1;
        scan2 = scan1;
        scan1 = scan;
    }

    public synchronized void validateSizes(int w, int h) {
        if (bimg1 == null || w != bimg1.getWidth() || h != bimg1.getHeight()) {
            bimg1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Raster r = bimg1.getRaster();
            imgarray1 = ((DataBufferInt) r.getDataBuffer()).getData();
            SinglePixelPackedSampleModel sm = (SinglePixelPackedSampleModel) r
                                                                              .getSampleModel();
            scan1 = sm.getScanlineStride();
            bimg2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            r = bimg2.getRaster();
            imgarray2 = ((DataBufferInt) r.getDataBuffer()).getData();
            sm = (SinglePixelPackedSampleModel) r.getSampleModel();
            scan2 = sm.getScanlineStride();
            refreshSource();
            makeDisplacementMap();
            offscr = null;
        }
    }

    /**
     *
     * the default method of rendering: advance the animation
     * on each frame
     */
    public void render(Dimension dim, Graphics g) {
        displace();
        renderLines();
        int w = dim.width;
        int h = dim.height;
        validateSizes(w, h);
        g.drawImage(bimg1, 0, 0, null);
    }

    public int frameNum = 0;

    /**
     * An alternative method of animation: advance the animation
     * only each fifth frame, and display a cached copy in between
     * the advances
     */
    public void render1(Dimension dim, Graphics g) {
        if (frameNum++ % 5 == 0) {
            displace();
            renderLines();

            int w = dim.width;
            int h = dim.height;
            validateSizes(w, h);
            if (offscr == null) {
                offscr = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                offscr.setAccelerationPriority(1.0f);
            }
            Graphics g2 = offscr.getGraphics();
            g2.drawImage(bimg1, 0, 0, null);
        }

        if (offscr != null) {
            g.drawImage(offscr, 0, 0, null);
        }
    }
}
