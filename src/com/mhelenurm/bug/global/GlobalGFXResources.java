/*
 * Copyright © 2013 Mark Helenurm
 * 
 * This code is copyrighted by Mark Helenurm.
 * Do not steal this code under the threat of legal
 * prosecution.
 * 
 * If you have suggestions, comments, or requests to
 * borrow code, email me at <mhelenurm@gmail.com>
 */
package com.mhelenurm.bug.global;

import com.mhelenurm.bug.model.Bug;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class GlobalGFXResources {

    private static BufferedImage[][] parts = null;
    private static BufferedImage[] tree = null;
    private static int globalWidth = 0;
    private static int globalHeight = 0;
	/**
	 * The image for the background.
	 */
	public static BufferedImage background;
	/**
	 * The image for the corners of the background.
	 */
	public static BufferedImage goldcorner;
	/**
	 * The image for the wood tabletop.
	 */
	public static BufferedImage wood;
	/**
	 * The image for the petri dish.
	 */
    public static BufferedImage petridish;
	private static final Logger LOG = Logger.getLogger(GlobalGFXResources.class.getName());

	/**
	 * Initializes the GlobalGFXResources.
	 */
    public static void init() {
        GlobalGFXResources g = new GlobalGFXResources();
        
        if (background == null) {
            try {
                background = ImageIO.read(GlobalMan.getResourcePath("leatherbackground.png"));
			} catch (Exception e) {
				LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append("leatherbackground.png could not be read. Error=").append(e.getMessage()).toString());
            }
        }
        if (wood == null) {
            try {
                wood = ImageIO.read(GlobalMan.getResourcePath("wood.jpg"));
            } catch (Exception e) {
				LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append("wood.png could not be read. Error=").append(e.getMessage()).toString());
            }
        }
        if(goldcorner == null) {
           try {
                goldcorner = ImageIO.read(GlobalMan.getResourcePath("goldcorner.png"));
            } catch (Exception e) {
			   LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append("goldcorner.png could not be read. Error=").append(e.getMessage()).toString());
           }
        }
        if(petridish == null) {
           try {
                petridish = ImageIO.read(GlobalMan.getResourcePath("petritop.png"));
            } catch (Exception e) {
			   LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append("petritop.png could not be read. Error=").append(e.getMessage()).toString());
           }
        }
        
        if (parts == null) { //in case of accidental re-init
            parts = new BufferedImage[2][GlobalGenInf.CHARACTERS];
            for (int i = 0; i < 2; i++) { //dom v. rec
                for (int j = 0; j < GlobalGenInf.CHARACTERS; j++) { //character
                    URL url = GlobalMan.getResourcePath(GlobalGenInf.resourceStrings[i][j]);
                    
                    if (url!=null) {
                        try {
                            BufferedImage b = ImageIO.read(url);
                            parts[i][j] = b;
                            globalWidth = b.getWidth();
                            globalHeight = b.getHeight();
                        } catch (Exception e) {
                            parts[i][j] = null;
							LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append(GlobalGenInf.resourceStrings[i][j]).append(" could not be read. Error=").append(e.getMessage()).toString());
                        }
                    } else {
                        parts[i][j] = null;
						LOG.log(Level.SEVERE, new StringBuilder(GlobalMan.resourcePath).append(GlobalGenInf.resourceStrings[i][j]).append(" does not exist.").toString());
                    }
                }
            }
            tree = new BufferedImage[(int) (Math.pow(2, GlobalGenInf.CHARACTERS + 1))];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = null;
            }
        }             //        DRAW ORDER
        /*0 = antennae          5
         * 1 = eyes             4
         * 2 = hands            3  
         * 3 = spots            6
         * 4 = feet             2
         * 5 = abdomen          1
         */
    }

	/**
	 * Gets the image for a bug.
	 *
	 * @param a The bug.
	 * @return The BufferedImage representing the bug.
	 */
	public static BufferedImage getImage(Bug a) {
		if (tree == null) {
			init();
		}
        byte phenos = a.getPhenoAsByte();
        boolean[] phenolist = a.getPheno();
        if (tree[phenos] == null) {
            BufferedImage img = new BufferedImage(globalWidth, globalHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            
            if (parts[(phenolist[GlobalGenInf.revmap[5]] == true) ? 1 : 0][5] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[5]] == true) ? 1 : 0][5], 0, 0, null);
            }
            if (parts[(phenolist[GlobalGenInf.revmap[4]] == true) ? 1 : 0][4] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[4]] == true) ? 1 : 0][4], 0, 0, null);
            }
            if (parts[(phenolist[GlobalGenInf.revmap[2]] == true) ? 1 : 0][2] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[2]] == true) ? 1 : 0][2], 0, 0, null);
            }
            if (parts[(phenolist[GlobalGenInf.revmap[1]] == true) ? 1 : 0][1] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[1]] == true) ? 1 : 0][1], 0, 0, null);
            }
            if (parts[(phenolist[GlobalGenInf.revmap[0]] == true) ? 1 : 0][0] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[0]] == true) ? 1 : 0][0], 0, 0, null);
            }
            if (parts[(phenolist[GlobalGenInf.revmap[3]] == true) ? 1 : 0][3] != null) {
                g2.drawImage(parts[(phenolist[GlobalGenInf.revmap[3]] == true) ? 1 : 0][3], 0, 0, null);
            }
            tree[phenos] = img;
		}
        return tree[phenos];
    }

	private GlobalGFXResources() {
	}
}
