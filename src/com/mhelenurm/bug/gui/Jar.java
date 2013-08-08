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
package com.mhelenurm.bug.gui;

import com.mhelenurm.bug.global.GlobalGFXResources;
import com.mhelenurm.bug.global.GlobalGenInf;
import com.mhelenurm.bug.model.Bug;
import com.mhelenurm.bug.model.Loc2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class handles an insect petri dish UI.
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class Jar {
	private static final Logger LOG = Logger.getLogger(Jar.class.getName());
    
    private ArrayList<BugInfo> bugs;
    private BufferedImage buffer;
    //for bug movement
    private final int JARWIDTH = 500;
    private final int JARHEIGHT = 500;
    double upcount = 0;
    private boolean frozen = false;
    private double frozenscalar = .03;
    
    //for bug selection
    private boolean isSelected = false;
    private int selectedIndex = -1;
    
	/**
	 * Initializes the Jar.
	 */
	public Jar() {
		buffer = null;
		bugs = new ArrayList<BugInfo>(10);
	}

	/**
	 * Returns whether an insect is at mx,my.
	 *
	 * @param mx The x location of the select.
	 * @param my The y location of the select.
	 * @return Whether an insect is at the location.
	 */
	public boolean select(int mx, int my) {
		boolean found = false;
		int index = -1;
		double lowest = -1;
		double scale = .5;
		Loc2D pointloc = new Loc2D(mx, my);
		for (int i = 2; i < bugs.size(); i++) {
			double dist = distance(pointloc, bugs.get(i).loc);
			double radius = (Math.max(bugs.get(i).img.getHeight(), bugs.get(i).img.getWidth()) * scale) / 2.0;
			if (dist < radius) {
				if (found) {
					if (dist < lowest) {
						index = i;
						lowest = dist;
					}
				} else {
					found = true;
					index = i;
					lowest = dist;
				}
			}
		}
		if (found) {
			isSelected = true;
			selectedIndex = index;
		}
		return found;
	}
    
    private double distance(Loc2D l1, Loc2D l2) {
		return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getY() - l2.getY(), 2));
	}

	/**
	 * Toggles whether the insects are frozen.
	 */
    public void toggleFreeze() {
		frozen = !frozen;
	}

	/**
	 * Gets the selected bug.
	 *
	 * @return The selected bug.
	 */
    public Bug getSelected() {
		if (isSelected) {
			return bugs.get(selectedIndex).bug;
		}
		return null;
	}

	/**
	 * Removes the selected bug.
	 */
    public void removeSelected() {
		if (isSelected) {
			bugs.remove(selectedIndex);
			deselect();
		}
	}

	/**
	 * Deselects the currently selected bug.
	 */
    public void deselect() {
		isSelected = false;
		selectedIndex = -1;
	}

	/**
	 * Adds a bug to the container.
	 *
	 * @param b The bug to add.
	 */
    public void addBug(Bug b) {
        BugInfo newb = new BugInfo();
        newb.bug = b;
        double random = Math.random() * Math.PI * 2;
        double scalar = Math.random() * .5;
        newb.loc = new Loc2D(Math.cos(random) * JARWIDTH / 2 * scalar + JARWIDTH / 2, Math.sin(random) * JARHEIGHT / 2 * scalar + JARHEIGHT / 2);
        newb.img = GlobalGFXResources.getImage(b);
        newb.ori = 2 * (Math.random() - .5) * Math.PI * 2;
        newb.dor = 2 * (Math.random() - .5) * Math.PI * 2;
        newb.spd = Math.random() * .5 + .3;
        bugs.add(newb);
    }

	/**
	 * Gets the buffer for the Jar.
	 *
	 * @return The buffer for the Jar.
	 */
    public BufferedImage getBuffer() {
        return buffer;
    }
    
	/**
	 * Breeds the parent insects in the jar.
	 */
    public void breed() {
        if (bugs.size() >= 2 && bugs.size() < 30) {
            if (bugs.get(0).bug.isDominant(0) ^ bugs.get(1).bug.isDominant(0)) {
                int offspring = (int) (Math.random() * 10) + 5;
                for (int i = 0; i < offspring; i++) {
                    addBug(GlobalGenInf.breed(bugs.get(0).bug, bugs.get(1).bug));
                }
            }
        }
    }

	/**
	 * Kills all insects except parents (if specified).
	 *
	 * @param cankillparents Whether the parents can be killed.
	 */
    public void kill(boolean cankillparents) {
        if (bugs.size() > 2) {
            while (bugs.size() > 2) {
                bugs.remove(2);
            }
        } else if(cankillparents) {
            bugs.clear();
        }
    }

	/**
	 * Steps the animation ahead by a small amount.
	 *
	 * @param time The delta time.
	 */
    public void timestep(double time) {
        final double ddoscalar = 10.0f;
        final double maxturnspd = Math.PI / 5.0;
        final double updatetime = 1.0;
        upcount += time;

        for (BugInfo b : bugs) {
            if (upcount > updatetime) {

                b.ddor = 2 * (Math.random() - .5) * (ddoscalar);
            }
            b.dor += b.ddor * time;
            b.dor = Math.max(Math.min(b.dor, maxturnspd), -maxturnspd);
            b.ori += b.dor * time * ((frozen)?frozenscalar:1);
            Loc2D dd = new Loc2D(b.loc.getX(), b.loc.getY());
            b.loc.addCoords(Math.cos(b.ori - Math.PI / 2) * b.spd * ((frozen)?frozenscalar:1), Math.sin(b.ori - Math.PI / 2) * b.spd * ((frozen)?frozenscalar:1));
			int diameter = (Math.max(b.img.getHeight(), b.img.getWidth())) / 2;
            if (Math.pow(b.loc.getX() - (JARWIDTH) / 2, 2) / Math.pow(((JARWIDTH - 60 - diameter) / 2), 2) + Math.pow(b.loc.getY() - (JARHEIGHT) / 2, 2) / Math.pow(((JARHEIGHT - 60 - diameter) / 2), 2) > 1.0) {
                b.dor = Math.max(Math.min(b.dor * 2, maxturnspd), -maxturnspd);
                b.ddor = 0.0;
                b.loc = dd;
            }
        }
        if (upcount > updatetime) {
            upcount -= updatetime;
        }

    }

	/**
	 * Renders the buffer.
	 */
    public void render() {
        final double scale = .5;

        BufferedImage image = new BufferedImage(JARWIDTH, JARHEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = image.createGraphics();
        gfx.setColor(new Color(255, 255, 255, 150));
        gfx.fillOval(30, 30, JARWIDTH - 60, JARHEIGHT - 60);
        int ct = 0;
        for (BugInfo b : bugs) {
            AffineTransform at = new AffineTransform();
            at.translate(b.loc.getX(), b.loc.getY());
            at.rotate(b.ori);
            at.scale(scale, scale);
            at.translate(-b.img.getWidth() / 2, -b.img.getHeight() / 2);
            gfx.drawImage(b.img, at, null);
            int diameter = (int) (Math.max(b.img.getHeight(), b.img.getWidth()) * scale);
            if(ct == selectedIndex && isSelected) {
                gfx.setColor(Color.yellow);
                
                gfx.drawOval((int) b.loc.getX() - diameter / 2, (int) b.loc.getY() - diameter / 2, diameter, diameter);
            }
            if (ct < 2) {
                gfx.setColor(Color.green);
                
                gfx.drawOval((int) b.loc.getX() - diameter / 2, (int) b.loc.getY() - diameter / 2, diameter, diameter);
            }
            ct++;
        }
        gfx.drawImage(GlobalGFXResources.petridish, 0, 0, null);
        buffer = image;
    }

	private class BugInfo {
		Bug bug; //the bug data
		Loc2D loc; //the bug's current location
		BufferedImage img; //the bug's bufferedimage
		double ori; //orientation of bug (radians)
		double dor; //delta orientation
		double spd; //speed
		double ddor; //delta ori
	}
}
