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
package com.mhelenurm.bug.model;

import java.util.logging.Logger;

/**
 * This class handles a basic 2D vector location object.
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class Loc2D {

	private static final Logger LOG = Logger.getLogger(Loc2D.class.getName());
	private double x;
	private double y;

	/**
	 * Initializes the Loc2D object to a zero vector.
	 */
	public Loc2D() {
		x = 0;
		y = 0;
	}

	/**
	 * Initializes the Loc2D object with a x component and a y component.
	 *
	 * @param x The x component.
	 * @param y The y component.
	 */
	public Loc2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds coordinates to the vector.
	 *
	 * @param dx The delta x.
	 * @param dy The delta y.
	 */
	public void addCoords(double dx, double dy) {
		x += dx;
		y += dy;
	}

	/**
	 * Sets the x component.
	 *
	 * @param x The x component.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y component.
	 *
	 * @param y The y component.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the x component.
	 *
	 * @return The x component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y component.
	 *
	 * @return The y component.
	 */
	public double getY() {
		return y;
	}
}
