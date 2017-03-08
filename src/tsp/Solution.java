/*
	tsp-framework
	Copyright (C) 2012 Fabien Lehu�d� / Damien Prot

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

import java.io.PrintStream;
import java.util.Arrays;

/**
 * 
 * This class models a TSP Solution.
 * 
 * **Internal structure of the class:**
 * 
 * This solution itself is stored in the array {@link m_solution}.
 * {@link m_solution[i]} should be the index of the element at index i in the solution.
 * 
 * The attribute {@link m_nbVertices} is the number of vertices in the problem.
 * The array {@link m_solution} has size m_nbVertices+1 and it should start with the first vertex for the route and end with the same vertex.
 * 
 * For example, suppose a problem with 5 customers (hence, the corresponding vertices index range from 0 to 4).
 * The following array represents a TSP solution (tour) that visit all customers by index order:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * m_solution = [0, 1, 2, 3, 4, 0];
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * 
 * A recommendation is to always have **m_solution[0]=m_solution[m_nbVertices]= 0**.
 * 
 * So a complete feasible solution should respect the following rules:
 * 	- m_solution[0]=m_solution[m_nbVertices]
 * 	- Apart from the vertex at indices 0 and {@link m_nbVertices}, each customer index should be represented once and only one in {@link m_solution}.
 * 
 * ** Creating, accessing or modifying a solution:**
 * 
 * A Solution object is created for a given TSP problem, represented by an Instance object.
 * Let ist be an object of class {@link Instance}, the following code creates a solution for this problem:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Solution mysol = new Solution(ist);
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * 
 * During its creation, this object is initialized with vertex 0 at all position. It should be modified to represent a feasible solution.
 * This modification is done by the method {@link #setVertexPosition(int s, int i)}, which sets vertex s at position i in the solution.
 * For example, the following code creates a TSP solution that visits all customers in increasing vertex order:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * for(int i=0; i < ist.getNbVertices(); i++)
 * {
 * 	mysol.setVertexPosition(i, i);
 * }
 * mysol.setVertexPosition(0, ist.getNbVertices());
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * To explore a {@link Solution} object, the vertex at position i can be obtained calling {@link #getSolution(int)}.
 * 
 * The {@link #validate()} method allows to check the feasibility of a solution.
 * 
 * To calculate the cost of a solution, you can call {@link #evaluate()}.
 * 
 * Note that {@link #evaluate()} recomputes every distance from zero. If you do a slight modification of the solution it is less time consuming to update
 * yourself the cost of the modified solution using the function {@link #setObjective(long newval )}.
 * 
 * 
 * @author Fabien Lehuédé
 * 
 */
public class Solution{

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/**
	 * 
	 * This array stores the route that constitutes the solution.
	 * <code>m_solution[i]</code> is the index of the element at index i in the
	 * solution.
	 */
	private int[] m_solution;

	/**
	 * Route cost or length (objective function). This value should be updated
	 * when the solution is modified, either calling <code>mysol.evaluate</code>
	 * , which recomputes the cost or <code>mysol.setObjective(newvalue)</code>.
	 */
	private long m_objective;

	/** Data of the problem associated with the solution */
	private Instance m_instance;

	/** Number of vertices in the problem. */
	private int m_nbVertices;

	/** Error code returned by <code>validate</code> */
	private String m_error;

	// -----------------------------------------
	// --------------- METHODS -----------------
	// -----------------------------------------

	/**
	 * @return Route length / cost. This value should be updated when the
	 *         solution is modified, either calling <code>mysol.evaluate</code>,
	 *         which recomputes the cost or
	 *         <code>mysol.setObjective(newvalue)</code>.
	 */
	public long getObjective() {
		return m_objective;
	}

	/**
	 * Sets the cost of the solution to the value newval.
	 * 
	 * @param newval
	 *            : new solution cost
	 */
	public void setObjective(long newval) {
		this.m_objective = newval;
	}

	/**
	 * 
	 * @return Returns a pointer to the data of the problem associated with the
	 *         solution.
	 */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * Returns the index of the vertex at position i in the route.
	 * 
	 * @throws Exception
	 *             returns an error if i is not a valid position.
	 */
	public int getSolution(int i) throws Exception {
		if ((i < 0) || (i > m_nbVertices))
			throw new Exception("Error Instance.getSolution : index " + i
					+ " is not valid, it should range between 0 and "
					+ m_nbVertices);
		return m_solution[i];
	}

	/**
	 * 
	 * @return Error code returned by <code>validate</code>
	 */
	public String getError() {
		return m_error;
	}

	// -------------------------------------
	// ------------ CONSTRUCTOR ------------
	// -------------------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in
	 * <code>inst</code>.
	 */
	public Solution(Instance inst) {
		m_instance = inst;
		m_nbVertices = inst.getNbVertices();
		m_solution = new int[m_nbVertices + 1];
	}

	public Solution copy() {
		Solution copie = new Solution(m_instance);
		copie.m_solution = Arrays.copyOf(m_solution, m_nbVertices + 1);
		copie.m_objective = m_objective;
		return copie;

	}

	// -------------------------------------
	// -------------- METHODS --------------
	// -------------------------------------

	/**
	 * Set vertex s at position i in the solution. <br>
	 * Note that if there is already a vertex index at position i then this
	 * vertex is replaced by s.
	 * 
	 * @param s
	 *            index of the vertex to insert.
	 * @param i
	 *            insertion position.
	 * @throws Exception
	 *             returns an error if i is not a valid insertion position or s
	 *             is not a valid vertex number.
	 */
	public void setVertexPosition(int s, int i) throws Exception {
		if ((i < 0) || (i > m_nbVertices))
			throw new Exception(
					"Error Instance.setVertexPosition(i,s): index i=" + i
					+ ", must range between 0 and " + m_nbVertices);
		if ((s < 0) || (s >= m_nbVertices))
			throw new Exception(
					"Error Instance.setVertexPosition(i,s) : vertex value s="
							+ s + ", must range between 0 and "
							+ (m_nbVertices - 1));
		m_solution[i] = s;
	}

	/**
	 * reverse the sequence of vertices in the solution that range between
	 * indices firstIdx and lastIdx (both included). <br>
	 * The objective function is updated according to the modification. <br>
	 * <br>
	 * For example, if the solution is [0,1,2,3,4,0], reverse(2,4) produces
	 * solution [0,1,4,3,2,0].
	 * 
	 * @throws Exception
	 *             produces an error if the indices are not valid.
	 */
	public void reverse(int firstIdx, int lastIdx) throws Exception {
		// Checking the validity of the reverse operation :
		if ((firstIdx < 0) || (firstIdx > m_nbVertices))
			throw new Exception(
					"Error Instance.reverse(int firstIdx, int lastIdx)): index firstIdx="
							+ firstIdx + ", must range between 0 and "
							+ m_nbVertices);
		else {
			if ((lastIdx < 0) || (lastIdx > m_nbVertices))
				throw new Exception(
						"Error Instance.reverse(int firstIdx, int lastIdx)): index lastIdx="
								+ lastIdx + ", must range between 0 and "
								+ m_nbVertices);
			//			else {
			//				if (((firstIdx == 0) && (lastIdx < m_nbVertices))
			//						|| ((firstIdx > 0) && (lastIdx == m_nbVertices))) {
			//					System.err
			//					.println("WARNING Instance.reverse(int firstIdx, int lastIdx)): \n firstIdx="
			//							+ firstIdx
			//							+ " , lastIdx="
			//							+ lastIdx
			//							+ ". The first and the last vertex of the solution will not be identical after this operation.");
			//				}
			//
			//			}
		}

		if (firstIdx > 0) {
			m_objective -= m_instance.getDistances(m_solution[firstIdx - 1],
					m_solution[firstIdx]);
			m_objective += m_instance.getDistances(m_solution[firstIdx - 1],
					m_solution[lastIdx]);
		}
		if (lastIdx < m_nbVertices) {
			m_objective -= m_instance.getDistances(m_solution[lastIdx],
					m_solution[lastIdx + 1]);
			m_objective += m_instance.getDistances(m_solution[firstIdx],
					m_solution[lastIdx + 1]);
		}

		for (int i = 0; i <= (lastIdx - firstIdx) / 2; i++) {
			int swapfirst = firstIdx + i;
			int swaplast = lastIdx - i;
			int tmp = m_solution[swapfirst];
			m_solution[swapfirst] = m_solution[swaplast];
			m_solution[swaplast] = tmp;
		}

	}

	/**
	 * Recomputes the cost of the solution and return its value.
	 * 
	 * @throws Exception
	 */
	public double evaluate() throws Exception {
		m_objective = 0;
		for (int i = 0; i < m_nbVertices; i++) {
			m_objective += m_instance.getDistances(m_solution[i],
					m_solution[i + 1]);
		}
		return m_objective;
	}

	/**
	 * Check that the solution is feasible.
	 * 
	 * <br>
	 * The performed tests are the following :
	 * 
	 * <li> <code>m_solution[0]=m_solution[m_nbVertices]</code> <li>Apart from
	 * the vertex at indices 0 and <code>m_nbVertices</code>, each customer
	 * index should be represented once and only one in <code>m_solution</code>.
	 * 
	 * <br>
	 * The cost is recomputed.
	 * 
	 * <br>
	 * When an error is met, the error code can be obtained calling the method
	 * <code>getError()</code>.
	 * 
	 * @return <code>true</code> if the solution is feasible <code>false</code>
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean validate() throws Exception {
		boolean result = true;
		m_error = "";
		// Check first that the first and last vertices are the same
		if (m_solution[0] != m_solution[m_nbVertices]) {
			m_error += "Error : The route should start and end with the same vertex.\n";
			m_error += "The first vertex is " + m_solution[0]
					+ " and the last vertex is " + m_solution[m_nbVertices]
							+ ".\n";
			result = false;
		}

		// Check that all vertices are visited
		int[] occurences = new int[m_nbVertices];
		Arrays.fill(occurences, 0);
		for (int i = 0; i < m_nbVertices; i++) { // the last vertex is not
			// included
			occurences[m_solution[i]]++;
		}
		for (int i = 1; i < m_nbVertices; i++) {
			if (occurences[i] != 1) {
				m_error += "Error : the vertex " + (i + 1) + " is visited "
						+ occurences[i] + " times.\n";
				result = false;
			}
		}

		evaluate();
		if (result)
			m_error += "The solution is feasible.";
		else
			m_error += "The solution is not feasible.";
		return result;
	}

	/**
	 * Print the solution on output <code>out</code>. The solution is printed
	 * using vertices numbers and not labels.
	 * 
	 * For example if you want to print a solution <code>mysol</code> on the
	 * screen, use :
	 * 
	 * <code>mysol.print(System.err);</code>
	 * 
	 * Reminder: do not print on System.out.
	 * 
	 * @param out
	 *            : output
	 */
	public void print(PrintStream out) {
		out.println("TSP solution, cost " + m_objective + ", solution :");
		out.print(m_solution[0]);
		for (int i = 1; i <= m_nbVertices; i++) {
			out.print("-" + m_solution[i]);
		}
		out.println();
	}
}
