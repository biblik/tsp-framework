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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- {@link m_instance} :  the Instance object which contains the problem data
 * 	- {@link m_solution} : the Solution object to modify. This object will store the result of the program.
 * 	- {@link m_time} : the maximum time limit (in seconds) given to the program.
 *  
 * @author Damien Prot, Fabien Lehuédé 2012
 * 
 */
public class TSPSolver extends Observable{

	// ---------------------------------------------
	// --------------- ATTRIBUTES ------------------
	// ---------------------------------------------

	/**
	 * The Traveling Salesman Problem Solution that will be returned by the
	 * program
	 */
	private Solution m_solution;

	/** The TSP data. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_time;

	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	// These methods allow to access the class attributes from outside the
	// class.

	/** @return the problem Solution */
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
	 * Initializes the problem solution with a new Solution object (the old one
	 * will be deleted).
	 * 
	 * @param sol : new solution
	 */
	public void setSolution(Solution sol) {
		this.m_solution = sol;
	}

	/**
	 * Sets the problem data
	 * 
	 * @param inst : the Instance object which contains the data.
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
	 * Method to modify the solution shown in GUI
	 * It has to be called each time you want to display a new solution
	 * @param sol
	 */
	private void changeGUI(Solution sol) {
		setChanged();
		notifyObservers(sol);
	}
	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using <code>System.out.print()</code> or <code>System.out.println()</code>).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (<code>System.err.print()</code> or <code>System.err.println()</code>).
	 * 
	 * When your algorithm terminates, make sure the attribute {@link m_solution} in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit {@link m_time}.
	 * 
	 * @throws Exception
	 *             May return some error, in particular if some vertices index
	 *             are wrong.
	 */
	public void solve() throws Exception {


		// Exemple simpliste o� le sommet i est ins�r� en position i dans la
		// tourn�e.
//		for (int i=0; i < m_instance.getNbVertices(); i++) {
//			m_solution.setVertexPosition(i, i);
//		}
		
		m_solution = nearestNeighbor();
		System.out.println("before two-opt : "+ m_solution.getObjective());
		changeGUI(m_solution);
		twoOpt(m_solution);
		
		System.out.println("after two-opt : "+ m_solution.getObjective());
		
		Solution bestSol = m_solution.copy();
		double bestEval = bestSol.evaluate();

		int nbRestart = 100;
		int nbIter = 200000;
		for (int j=0;j<nbRestart;j++){
			System.out.println("----- Restart  "+ j + " ------");
			Solution bestLocal = randomize(m_solution, nbIter);
			twoOpt(bestLocal);
			double eval = bestLocal.evaluate();
			if (eval < bestEval){
				bestSol = bestLocal.copy();
				bestEval = eval;
				changeGUI(bestSol);
				System.out.println("Amélioration globale : valeur de "+ eval);
			}
		}
		m_solution = bestSol.copy();
		
	}

	
	
	private void twoOpt(Solution sol) throws Exception { 
		boolean move = true;
		long bestEval = (long) sol.evaluate();
		int nbV = m_instance.getNbVertices();
		while (move){
			move = false;
			double bestGain = 0;
			int bestVertex1 = -1,bestVertex2=-1;
			for (int i = 0; i < nbV-1; i++) {
				for (int j=i+1;j<nbV-1;j++){

					int point1 = sol.getSolution(i);
					int pred1 = -1;
					if (i==0)
						pred1 = sol.getSolution(nbV-1);
					else
						pred1 = sol.getSolution(i-1);
					int point2 = sol.getSolution(j);
					int succ2 = sol.getSolution(j+1);
					
					double diff = m_instance.getDistances(pred1, point1)+ m_instance.getDistances(point2, succ2)
					- m_instance.getDistances(point1, succ2)-m_instance.getDistances(pred1, point2);

					if (diff>bestGain){
						bestGain=diff;
						bestVertex1=i;
						bestVertex2=j;
					}
				}
			}
			if (bestGain!=0){
				move = true;
				sol.reverse(bestVertex1, bestVertex2);
				if (bestVertex1==0){
					sol.setVertexPosition(sol.getSolution(0),nbV); 
				}

				bestEval = (long) (bestEval-bestGain);
				sol.setObjective(bestEval);
				if (m_solution.getObjective() >= bestEval){
					m_solution = sol.copy();
					changeGUI(sol); 
				}
				System.out.println(" new best : "+ (int)bestEval);
			} 
		}

		sol.evaluate();
	
	}


	private Solution nearestNeighbor() throws Exception {
		Solution sol = new Solution(m_instance);
		sol.setVertexPosition(0, 0);
		sol.setVertexPosition(0, m_instance.getNbVertices());
		 
		ArrayList<Integer> availableIndex = new ArrayList<Integer>();
		for (int i=1;i<m_instance.getNbVertices();i++){
			availableIndex.add(i);
		}
		
		
		int currentPosition = 1;
		while (!availableIndex.isEmpty()){
			
			long smallestDistance = Long.MAX_VALUE;
			int bestIndex = -1;
			for (Integer index:availableIndex){
				long dist= m_instance.getDistances(sol.getSolution(currentPosition-1), index);
				if (dist < smallestDistance){
					smallestDistance = dist;
					bestIndex = index;
				}
			}
			sol.setVertexPosition(bestIndex, currentPosition);
			currentPosition++;
			availableIndex.remove((Integer)bestIndex);
		}
		sol.evaluate();
		return sol;
	}

	private Solution randomize(Solution initialSol, int nbIter) throws Exception { 
		Solution localBest = initialSol.copy();
		for (int i=0;i<nbIter;i++){
			Solution newSol = localBest.copy();
			Random r = new Random();
			int firstPoint = r.nextInt(m_instance.getNbVertices()-1)+1;
			int secondPoint = r.nextInt(m_instance.getNbVertices()-1)+1;
			while (firstPoint >= secondPoint){
				firstPoint = r.nextInt(m_instance.getNbVertices()-1)+1;
				secondPoint = r.nextInt(m_instance.getNbVertices()-1)+1;
			}
			newSol.reverse(firstPoint, secondPoint);
			double eval = newSol.evaluate();
			if (eval<localBest.evaluate()){
				twoOpt(newSol);
				localBest=newSol.copy();
//				System.out.println("Amélioration locale à l'itération : "+ i + " : valeur de "+ newSol.getObjective());
			}
		}
		return localBest;
	}

	
	


}
