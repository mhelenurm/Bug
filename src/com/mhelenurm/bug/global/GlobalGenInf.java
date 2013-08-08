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
import java.util.logging.Logger;

/*
 * 0 = sex-linked
 * 1-3 = linked
 * 4,5 = unlinked
 */
//STORES ALL GENETIC INFORMATION
/**
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class GlobalGenInf {
	/**
	 * The linkage of the traits.
	 */
	public static final String[] linkageStrings = {"Sex-determining", "Linked", "Linked", "Linked", "Unlinked", "Sex-linked"};
	/**
	 * The phenotype names of the traits.
	 */
	public static final String[] phenoStrings = {"Antennae", "Eyes", "Hands", "Spots", "Feet", "Abdomen"};
	/**
	 * The Java resource names for the traits.
	 */
	public static final String[][] resourceStrings = {{"r-ant.png", "r-eye.png", "r-han.png", "r-spt.png", "r-fee.png", "r-abd.png"}, {"d-ant.png", "d-eye.png", "d-han.png", "d-spt.png", "d-fee.png", "d-abd.png"}};
    /* CHARATERISTIC VALUES:
     * 0 = antennae
     * 1 = eyes
     * 2 = hands
     * 3 = spots
     * 4 = feet
     * 5 = abdomen
     * 
     * (map[0] = 3) means that the sex-determining trait is spots
     * (map[4] = 5) means that the abdomen is an unlinked characteristic

	 */
	/**
	 * The map of which indicies correspond to which traits.
	 */
	public static int[] map;
	/**
	 * The map of which traits correspond to which indicies.
	 */
    public static int[] revmap;
	/**
	 * The number of genetic characteristics.
	 */
    public static final int CHARACTERS = 6;
	/**
	 * The three map distances for the linked genes.
	 */
    public static int[] mapdists;
	private static final Logger LOG = Logger.getLogger(GlobalGenInf.class.getName());

	/**
	 * Private method made for simplifying the public breeding method; simulates the production of a
	 * random sex cell by a bug.
	 */
    private static boolean[] recombine(Bug b) {
        boolean[] results = new boolean[CHARACTERS];

        results[0] = b.getAlleles()[rand(2)][0];
        results[5] = b.getAlleles()[rand(2)][5];
        results[4] = b.getAlleles()[rand(2)][4];
        if (results[0] == true) {
            results[5] = false;
        }


        int[] traits = {1, 0, 0};
        if(rand(50)<mapdists[0]) {
            traits[1] = 1;
        }
        if(rand(50)<mapdists[1]) {
            traits[2] = 1;
        }
        
        int rand = rand(2);
        for(int i = 0; i < 3; i++) {
            results[i+1] = (rand==0)?b.getAlleles()[traits[i]][i+1]:b.getAlleles()[1-traits[i]][i+1];
        }

        return results;
    }

    private static int rand(int max) {
        return (int) (Math.random() * max);
    }

	/**
	 * Checks whether two bugs can breed.
	 *
	 * @param a The first bug.
	 * @param b The second bug.
	 * @return Whether the two bugs can breed.
	 */
    public static boolean canBreed(Bug a, Bug b) {
        return a.isRecessive(0) ^ b.isRecessive(0);
    }

	/**
	 * Gets the bug product of two parents; assumes bugs can mate.
	 *
	 * @param a The first bug.
	 * @param b The second bug.
	 * @return The bug child.
	 */
    public static Bug breed(Bug a, Bug b) {
        return new Bug(recombine(a), recombine(b));
    }

	//generates map and revmap
	/**
	 * Generates map and revmap.
	 *
	 * @param mapp The map of values.
	 */
    public static void setMap(int[] mapp) {
        map = new int[6];
        revmap = new int[6];
        for (int i = 0; i < 6; i++) {
            map[i] = mapp[i];
            revmap[mapp[i]] = i;
        }
    }

	private GlobalGenInf() {
	}
}
