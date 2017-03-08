package tsp.neighborhood;

import tsp.Instance;
import tsp.Solution;

/**
 * This is the abstract class for Neighborhood
 * @author Axel Grimault
 * @version 2017
 *
 */
abstract public class ANeighborhood {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/** The data of the problem */
	protected Instance m_instance;

	/** The name of the neighborhood */
	protected String m_name;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Constructor
	 */
	public ANeighborhood(Instance instance, String name) throws Exception {
		m_instance = instance;
		m_name = name;
	}


	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/** Apply the neighborhood to get a neighbor */
	public abstract Solution getNeighborhood(Solution sol) throws Exception ;


	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** Get the name of the neighborhood */
	public String getName()
	{
		return m_name;
	}

}