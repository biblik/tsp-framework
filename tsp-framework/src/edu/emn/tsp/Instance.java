/*
	tsp-framework
	Copyright (C) 2012 Fabien Lehuédé / Damien Prot

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along
	with this program; if not, write to the Free Software Foundation, Inc.,
	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package edu.emn.tsp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * The Instance class allows to create an object that contains the data stored
 * in a tsp file. <br>
 * <br>
 * Only 2D EUCLIDEAN problems can be read, that is problems where the customer
 * coordinates are given and the distance between two customers is the euclidean
 * distance. <br>
 * <br>
 * The class is created through its constructor that takes the data file as
 * parameter. The data file is read and the data are stored in the Instance
 * object. The data can then be access calling the object methods.
 * 
 * @author Fabien Lehuédé
 * 
 */
public class Instance {

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/** Number of vertices. */
	private int m_nbVertices;
	/** x coordinates for each customer */
	private double[] m_x;

	/** y coordinates for each customer */
	private double[] m_y;

	/**
	 * Vertices labels (read from the TSP files).
	 */
	private String[] labels;

	/** Distance matrix */
	private long[][] m_distances;

	/** TSP file from the Euclidean tsp files of the TSPLib that is loaded. */
	private String m_fileName;

	// -----------------------------------------
	// --------------- METHODS -----------------
	// -----------------------------------------

	/** @return the number of vertices in the problem */
	public int getNbVertices() {
		return m_nbVertices;
	}

	/**
	 * @param i
	 *            vertex number (should be defined between 0 an the number of
	 *            vertices in the problem minus one). Note the vertex number is
	 *            not the label !
	 * @return the x coordinate of vertex i.
	 **/
	public double getX(int i) throws Exception {
		if ((i < 0) || (i >= m_nbVertices))
			throw new Exception("Error : vertex index " + i
					+ " should range between 0 and " + (m_nbVertices - 1) + ".");
		return m_x[i];
	}

	/**
	 * @param i
	 *            vertex number (should be defined between 0 an the number of
	 *            vertices in the problem minus one). Note the vertex number is
	 *            not the label !
	 * @return the y coordinate of vertex i.
	 * @throws Exception
	 **/
	public double getY(int i) throws Exception {
		if ((i < 0) || (i >= m_nbVertices))
			throw new Exception("Error : vertex index " + i
					+ " should range between 0 and " + (m_nbVertices - 1) + ".");
		return m_y[i];
	}

	/**
	 * @param i
	 *            vertex number (should be defined between 0 an the number of
	 *            vertices in the problem minus one). Note the vertex number is
	 *            not the label !
	 * @return vertex i label.
	 * @throws Exception
	 **/
	public String getLabel(int i) throws Exception {
		if ((i < 0) || (i >= m_nbVertices))
			throw new Exception("Error : vertex index " + i
					+ " should range between 0 and " + (m_nbVertices - 1) + ".");
		return labels[i];
	}

	/**
	 * Returns the euclidean distance rounded to the nearest integer value
	 * between two vertices. All distances are calculated when the tsp file is
	 * loaded, so this function does not calculate distances. Note: problems are
	 * symmetric, the distance from vertex i to vertex j is equal to the
	 * distance from j to i.
	 * 
	 * @param i
	 *            origin vertex (should range between 0 and nbVertex-1).
	 * @param j
	 *            destination vertex (should range between 0 and nbVertex-1).
	 * @return Returns the euclidean distance from i to j rounded to the nearest
	 *         integer value
	 * @throws Exception
	 *             returns an error if i or j are not valid vertex numbers.
	 **/
	public long getDistances(int i, int j) throws Exception {
		if ((i < 0) || (i >= m_nbVertices))
			throw new Exception("Error : vertex index " + i
					+ " should range between 0 and " + (m_nbVertices - 1) + ".");
		if ((j < 0) || (j >= m_nbVertices))
			throw new Exception("Error : vertex index " + j
					+ " should range between 0 and " + (m_nbVertices - 1) + ".");
		return m_distances[i][j];
	}

	/**
	 * @return Returns the whole distance matrix.
	 */
	public long[][] getDistances() {
		return m_distances;
	}

	/**
	 * @return Return the name of the instance file.
	 */
	public String getFileName() {
		return m_fileName;
	}

	// -------------------------------------
	// ------------ CONSTRUCTOR ------------
	// -------------------------------------

	/**
	 * Constructor: this method creates an object of class Instance. It calls
	 * the read method to load the data file given as parameter.
	 * 
	 * @param fileName
	 *            instance file
	 * @throws IOException
	 *             Returns an error when a problem is met reading the data file.
	 */
	public Instance(String fileName) throws IOException {
		m_fileName = fileName;
		read();
	}

	// -------------------------------------
	// -------------- METHODS --------------
	// -------------------------------------

	private void read() throws IOException {

		File mfile = new File(m_fileName);
		if (!mfile.exists()) {
			throw new IOException("The instance file : " + m_fileName
					+ " does not exist.");
		}
		Scanner sc = new Scanner(mfile);

		String line;
		do {
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("DIMENSION"));
		Scanner lineSc = new Scanner(line);
		lineSc.next();
		if (!lineSc.hasNextInt()) {
			lineSc.next();
		}
		m_nbVertices = lineSc.nextInt();
		m_x = new double[m_nbVertices];
		m_y = new double[m_nbVertices];
		labels = new String[m_nbVertices];

		do {
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("NODE_COORD_SECTION"));
		line = sc.nextLine();

		int idx = 0;
		while ((!line.startsWith("EOF")) && (sc.hasNext())) {
			assert (idx < m_nbVertices);
			// System.out.println(line);
			lineSc = new Scanner(line);
			lineSc.useLocale(Locale.US);
			labels[idx] = lineSc.next();
			m_x[idx] = lineSc.nextDouble();
			m_y[idx] = lineSc.nextDouble();
			line = sc.nextLine();
			idx++;
		}

		// Create the distance matrix
		m_distances = new long[m_nbVertices][];
		for (int i = 0; i < m_nbVertices; i++) {
			m_distances[i] = new long[m_nbVertices];
		}

		// Compute distances
		for (int i = 0; i < m_nbVertices; i++) {
			m_distances[i][i] = 0;
			for (int j = i + 1; j < m_nbVertices; j++) {
				long dist = distance(i, j);
				// System.out.println("Distance " + i + " " +j + ": " + dist);
				m_distances[i][j] = dist;
				m_distances[j][i] = dist;
			}
		}

		sc.close();
		lineSc.close();
	}

	/** Computes the distance between two vertices */
	private long distance(int i, int j) {
		double dx = m_x[i] - m_x[j];
		double dy = m_y[i] - m_y[j];
		return (long) Math.rint(Math.hypot(dx, dy));
	}

	/**
	 * 
	 * @return the greatest x coordinate.
	 */
	public double getMaxX() {

		return getMax(m_x);
	}

	/**
	 * 
	 * @return the greatest y coordinate.
	 */
	public double getMaxY() {

		return getMax(m_y);
	}

	/**
	 * 
	 * @return the smallest x
	 */
	public double getMinX() {

		return getMin(m_x);
	}

	/**
	 * 
	 * @return the smallest y
	 */
	public double getMinY() {

		return getMin(m_y);
	}

	private double getMax(double[] array) {
		double maxVal = Double.MIN_VALUE;
		for (int i = 0; i < m_nbVertices; i++) {
			if (maxVal < array[i])
				maxVal = array[i];
		}

		return maxVal;
	}

	private double getMin(double[] array) {
		double minVal = Double.MAX_VALUE;
		for (int i = 0; i < m_nbVertices; i++) {
			if (minVal > array[i])
				minVal = array[i];
		}

		return minVal;
	}

	/**
	 * Print data on the output given as a parameter.
	 * 
	 * @param out
	 *            : output stream
	 */
	public void print(PrintStream out) {

		out.println("Distance matrix:");
		for (int i = 0; i < m_nbVertices; i++) {
			for (int j = 0; j < m_nbVertices; j++) {
				out.print(m_distances[i][j] + ";");
			}
			out.println();
		}
		out.println();
	}

}
