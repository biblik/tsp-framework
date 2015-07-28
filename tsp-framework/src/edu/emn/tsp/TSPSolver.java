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
/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * <br> 
 * <br> The method you must implement is <code>solve()</code>. This method is called by the programmer 
 * after loading the data. 
 * <br>    
 * <br> The <code>TSPSolver</code> object is created by the <code>Main</code> class.
 * <br> The other objects that are created in <code>Main </code> can be accessed through the following 
 * <code>TSPSolver</code> attributes: 
 * 	<li><code>m_instance</code> the <code>Instance</code> object which contains the problem data.
 * 	<li><code>m_solution</code> the <code>Solution</code> object to modify. This object will store the result of the program.
 * 	<li><code>m_time</code> the maximum time limit (in seconds) given to the program.
 *  <br><br>
 * 
 * @author Damien Prot, Fabien Lehu�d� 2012
 * 
 */
public class TSPSolver {

  // --------------------------------------------
  // --------------- ATTRIBUTES ------------------
  // --------------------------------------------

  /** The traveling salesman problem solution that will be returned by the program */
	private Solution m_solution;

  /** The TSP data.*/
	private Instance m_instance;

  /** Time given to solve the problem. */
	private long m_time;

	
  // --------------------------------------------
  // ------------ GETTERS AND SETTERS -----------
  // --------------------------------------------

  // These methods allow to access the class attributes from outside the class.

  /** @return the problem solution */
	public Solution getSolution() {
		return m_solution;
	}

  /** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

  /** @return Time given to solve the problem */
	public long getTime() {
		return m_time;
	}

  /**
   * Initializes the problem solution with a new Solution object (the old one will be deleted).
   * 
   * @param sol : new solution
   */
	public void setSolution(Solution sol) {
		this.m_solution = sol;
	}

  /**
   * Sets the problem data
   * 
   * @param inst
   *          : the Instance object which contains the data.
   */
	public void setInstance(Instance inst) {
		this.m_instance = inst;
	}

  /**
   * Sets the time limit (in seconds).
   * 
   * @param time : time given to solve the problem
   */
	public void setTime(long time) {
		this.m_time = time;
	}

	
  // --------------------------------------
  // -------------- METHODS ---------------
  // --------------------------------------
	
  /**
   * TODO Modify this method to solve the problem.
   * 
   * <br> Do not print text on the standard output (eg. using <code>System.out.print()</code> 
   * or <code>System.out.println()</code>. This output is dedicated to the result analyzer
   * that will be used to evaluate your code on multiple instances. 
   * 
   * <br> You can print using the error output (<code>System.err.print/println</code>).
   *
   * <br> When your algorithm terminates, make sure the attribute <code>m_solution</code>
   * in this class points to the solution you want to return.
   * 
   * <br> You have to make sure that your algorithm does not take more time 
   * than the time limit <code>m_time</code>.    
   * 
   * @throws Exception
   *           May return some error, in particular if some vertices index are wrong. 
   */
	public void solve() throws Exception {

				
	}

}
