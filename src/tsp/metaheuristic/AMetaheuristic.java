package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;

/**
 * This is the abstract class for Metaheuristic
 * @author Axel Grimault
 * @version 2017
 *
 */
abstract public class AMetaheuristic {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/** The data of the problem */
	protected Instance m_instance;

	/** The name of the metaheuristic */
	protected String m_name;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Constructor
	 * @param instance the instance of the problem
	 * @param name the name of the metaheuristic
	 */
	public AMetaheuristic(Instance instance, String name) throws Exception {
		m_instance = instance;
		m_name = name;
	}


	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/** Apply the metaheuristic on a solution to get a local optimum solution */
	public abstract Solution solve(Solution sol) throws Exception;


	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** Get the name of the metaheuristic */
	public String getName()
	{
		return m_name;
	}
}