package tsp.heuristic;

import tsp.Instance;

/**
 * This is the abstract class for Insertion Heuristic
 * @author Axel Grimault
 * @version 2017
 *
 */
abstract public class HInsertion extends AHeuristic
{
	
	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------
	
	/** Number of inserted cities in the solution */
	protected int m_nbInsertedCities;

	/** indicates if a city is in the tour or not */
	protected boolean[] m_isVisited;
	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------
	
	public HInsertion(Instance instance, String name) throws Exception {
		super(instance,name);
		m_isVisited = new boolean[m_instance.getNbCities()];
		computeInitialSubTour();
	}
	
	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/** Initialize the construction heuristic with a subtour */
	public void computeInitialSubTour() throws Exception   {
		
		m_nbInsertedCities = 0;
		m_solution.setCityPosition(0, 0);
		
		// Find the farthest city from 0
		int farthestCity = -1;
		long farthestDistance = 0;
		for (int i = 1; i < m_instance.getNbCities(); i++)
		{
			long distance = m_instance.getDistances(0, i);
			if (distance > farthestDistance)
			{
				farthestCity = i;
				farthestDistance = distance;
			}
		}
		m_solution.setCityPosition(farthestCity, 1);
		m_solution.setCityPosition(0,2);
		m_isVisited[0]=true;
		m_isVisited[farthestCity]=true;
		m_nbInsertedCities = 3;
	}
	
}








