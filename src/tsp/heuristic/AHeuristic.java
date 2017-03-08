package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

/**
 * This is the abstract class for Heuristic
 * @author Axel Grimault
 * @version 2017
 *
 */
abstract public class AHeuristic {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------
	
	/** The data of the problem */
	protected Instance m_instance;
	
	/** The solution built */
	protected Solution m_solution;
	
	/** The name of the heuristic */
	protected String m_name;
	
	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------
	
	/**
	 * Constructor
	 * @param instance the instance of the problem
	 * @param name the name of the metaheuristic
	 */
	public AHeuristic(Instance instance, String name) throws Exception {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_name = name;
	}
	
	
	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/** Apply the heuristic to build m_solution */
	public abstract Solution solve() throws Exception;
	
	
	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------
	
	/**
	 * Returns the solution built with this heuristic
	 */
	public Solution getSolution() {
		return m_solution;
	}
	
	/**
	 * Returns the name of the heuristic
	 */
	public String getName() {
		return m_name;
	}

}








