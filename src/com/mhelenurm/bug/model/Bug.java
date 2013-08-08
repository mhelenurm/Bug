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

import com.mhelenurm.bug.global.GlobalGenInf;
import java.util.logging.Logger;

/*
 * 0        = SEX
 * 1 - 3    = LINKED
 * 4 - 5    = SINGLES
 */
/**
 * This class represents an insect with a variable genotype.
 *
 * @author Mark Helenurm <mhelenurm@gmail.com>
 * @version 1.0
 * @since Jun 25, 2013
 */
public class Bug {

	private static final Logger LOG = Logger.getLogger(Bug.class.getName());
	//all the alleles over all the chromosomes
	private boolean[][] c = new boolean[2][GlobalGenInf.CHARACTERS];

	/**
	 * Initializes a bug from two parent's chromosomes.
	 *
	 * @param p1c Parent 1's genetic information.
	 * @param p2c Parent 2's genetic information.
	 */
	public Bug(boolean[] p1c, boolean[] p2c) {
		c[0] = p1c;
		c[1] = p2c;
	}

	/**
	 * Returns the alleles for the bug.
	 *
	 * @return The genetic information pair array.
	 */
	public boolean[][] getAlleles() {
		return c;
	}

	/**
	 * Returns if the gene at an index is dominant (at least one dominant allele) or recessive (two
	 * recessive alleles).
	 *
	 * @param gene The index of the gene.
	 * @return Whether the gene is dominant or recessive.
	 */
	public boolean isDominant(int gene) {
		if (gene != 5) {
			return c[0][gene] || c[1][gene];
		} else {
			if (c[0][0] == true) { //if c1 is the male chromosome, output c2's sex-linked gene
				return c[1][5];
			} else if (c[1][0] == true) { //if c2 is the male chromosome, output c1's sex-linked gene
				return c[0][5];
			} else {
				return c[0][gene] || c[1][gene];
			}
		}
	}

	/**
	 * Returns whether a gene is recessive.
	 *
	 * @param gene The index of the gene.
	 * @return Whether the gene is recessive (returns !isDominant(gene)).
	 */
	public boolean isRecessive(int gene) {
		return !isDominant(gene);
	}

	/**
	 * Gets the phenotype of the bug.
	 *
	 * @return The array of dominant or recessive genes.
	 */
	public boolean[] getPheno() {
		boolean[] l = new boolean[GlobalGenInf.CHARACTERS];
		for (int i = 0; i < GlobalGenInf.CHARACTERS; i++) {
			l[i] = isDominant(i);
		}
		return l;
	}

	/**
	 * Gets the phenotype as a byte type.
	 *
	 * @return The phenotype as a byte.
	 */
	public byte getPhenoAsByte() {
		byte ret = 0;
		for (int i = 0; i < GlobalGenInf.CHARACTERS; i++) {
			if (isDominant(i)) {
				ret += (int) (Math.pow(2, i));
			}
		}
		return ret;
	}
}
