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
package com.mhelenurm.bug.application;

import com.mhelenurm.bug.global.GlobalGenInf;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class is an application for solving the bug application.
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class BugTester {
	private static final Logger LOG = Logger.getLogger(BugTester.class.getName());

	/**
	 * Runs the BugTester application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		while (true) {
            String name = JOptionPane.showInputDialog(null, "Enter your last name: ");
            Random generator = new Random(name.hashCode());

            int[] map = {0, 1, 2, 3, 4, 5};
            for (int i = 0; i < 100; i++) {
                int p1 = generator.nextInt(map.length);
                int p2 = generator.nextInt(map.length);
                int t = map[p1];
                map[p1] = map[p2];
                map[p2] = t;
            }
            GlobalGenInf.setMap(map);

            GlobalGenInf.mapdists = new int[2];
            GlobalGenInf.mapdists[0] = generator.nextInt(50);
            GlobalGenInf.mapdists[1] = generator.nextInt(50);

            String output = "";
            for (int i = 0; i < 6; i++) {
                output += GlobalGenInf.linkageStrings[i] + " trait is " + GlobalGenInf.phenoStrings[GlobalGenInf.map[i]] + "\n";
            }
            output += "\n";

            output += GlobalGenInf.phenoStrings[GlobalGenInf.map[2]] + " trait is linked to " + GlobalGenInf.phenoStrings[GlobalGenInf.map[1]] + " trait with a " + GlobalGenInf.mapdists[0]*2 + "% recombinant frequency.\n";
            output += GlobalGenInf.phenoStrings[GlobalGenInf.map[3]] + " trait is linked to " + GlobalGenInf.phenoStrings[GlobalGenInf.map[1]] + " trait with a " + GlobalGenInf.mapdists[1]*2 + "% recombinant frequency.";

            JOptionPane.showMessageDialog(null, output);
        }
    }
}
