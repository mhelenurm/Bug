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

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class GlobalMan {
	/**
	 * {@value #resourcePath}
	 */
	public static String resourcePath = "rsrc/";
	private static final Logger LOG = Logger.getLogger(GlobalMan.class.getName());

	/**
	 *
	 */
	public static void alloc() {
		GlobalMan g = new GlobalMan();
        
		java.net.URL filelocc = g.getClass().getClassLoader().getResource("images/");
		LOG.log(Level.OFF, new StringBuilder("File: ").append(filelocc).toString());
		File fff = new File(filelocc.getFile());
		try {
			resourcePath = fff.getCanonicalPath() + "/";
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Resource load failed.");
		}
	}

	/**
	 * Gets the resource path for a file.
	 *
	 * @param file The file to get the path for.
	 * @return The URL of the file.
	 */
    public static URL getResourcePath(String file) {
        GlobalMan g= new GlobalMan();
		return g.getClass().getResource("/images/" + file);
    }

	private GlobalMan() {
	}
}
