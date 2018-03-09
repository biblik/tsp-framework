package tsp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * The Instance class allows to create an object that contains the data stored
 * in a tsp file. <br>
 * <br>
 * Only 2D EUCLIDEAN and GEOGRAPHICAL problems can be read, that is problems where the customer
 * coordinates are given and the distance between two customers is the euclidean
 * distance or the GEOGRAPHICAL one. <br>
 * <br>
 * The class is created through its constructor that takes the data file as
 * parameter. The data file is read and the data are stored in the Instance
 * object. The data can then be access calling the object methods.
 * 
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 */
public class Instance {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/** Number of cities. */
	private int m_nbCities;

	/** x coordinates for each city */
	private double[] m_x;

	/** y coordinates for each city */
	private double[] m_y;

	/** Boolean for knowing if data are geographical */
	private boolean m_isGeographic;

	/** Cities labels (read from the TSP files). */
	private String[] m_labels;

	/** Distance matrix */
	private long[][] m_distances;

	/** TSP file from the Euclidean tsp files of the TSPLib that is loaded. */
	private String m_fileName;
	
	/** Instance type */
	private int m_typeInstance;



	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Constructor: this method creates an object of class Instance. It calls
	 * the read method to load the data file given as parameter.
	 * 
	 * @param fileName instance file
	 * @throws IOException Returns an error when a problem is met reading the data file.
	 */
	public Instance(String fileName, int typeInstance) throws IOException {
		m_fileName = fileName;
		m_typeInstance = typeInstance;
		if(m_typeInstance == 1)
		{
			parseEdgeInstance();
		}
		else
		{
			parse();
		}
	}
	
	
	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * Parse the input file to construct Instance		
	 */
	private void parse() throws IOException {

		File mfile = new File(m_fileName);
		if (!mfile.exists()) {
			throw new IOException("The instance file : " + m_fileName + " does not exist.");
		}
		Scanner sc = new Scanner(mfile);

		String line;
		do
		{
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("DIMENSION"));

		Scanner lineSc = new Scanner(line);
		lineSc.next();
		if (!lineSc.hasNextInt()) {
			lineSc.next();
		}
		m_nbCities = lineSc.nextInt();
		m_x = new double[m_nbCities];
		m_y = new double[m_nbCities];
		m_labels = new String[m_nbCities];

		do {
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("EDGE_WEIGHT_TYPE"));

		if (line.endsWith("GEO")){
			m_isGeographic = true;
		}
		else if (line.endsWith("EUC_2D")){
			m_isGeographic = false;
		}
		else{
			System.err.println("Distance is not handled");
		}
		line = sc.nextLine();
		line = sc.nextLine();

		int index = 0;
		while ((!line.startsWith("EOF")) && (sc.hasNext())) {
			if(index > m_nbCities)
			{
				System.exit(1);
			}
			// System.out.println(line);
			lineSc = new Scanner(line);
			lineSc.useLocale(Locale.US);
			m_labels[index] = lineSc.next();
			m_x[index] = lineSc.nextDouble();
			m_y[index] = lineSc.nextDouble();
			line = sc.nextLine();
			index++;
		}

		// Create the distance matrix
		m_distances = new long[m_nbCities][];
		for (int i = 0; i < m_nbCities; i++) {
			m_distances[i] = new long[m_nbCities];
		}

		// Compute distances
		for (int i = 0; i < m_nbCities; i++) {
			m_distances[i][i] = 0;
			for (int j = i + 1; j < m_nbCities; j++) {
				long dist = -1;
				if (m_isGeographic)
				{
					dist = geoDist(i,j); 
				}
				else
				{
					dist = distance(i, j);
				}
				m_distances[i][j] = dist;
				m_distances[j][i] = dist;
			}
		}
		
		if(m_isGeographic)
		{
			for(int i = 0; i < m_nbCities; i++)
			{
				double tempX = ((680/360.0) * (180 + m_x[i]));
				double tempY = ((680/360.0) * (180 + m_y[i]));
				m_x[i] = tempX;
				m_y[i] = -tempY;
			}
		}

		sc.close();
		lineSc.close();
	}
	
	/**
	 * Parse the input file to construct Instance		
	 */
	private void parseEdgeInstance() throws IOException {

		File mfile = new File(m_fileName);
		if (!mfile.exists()) {
			throw new IOException("The instance file : " + m_fileName + " does not exist.");
		}
		Scanner sc = new Scanner(mfile);

		String line;
		do
		{
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("DIMENSION"));

		Scanner lineSc = new Scanner(line);
		lineSc.next();
		if (!lineSc.hasNextInt()) {
			lineSc.next();
		}
		m_nbCities = lineSc.nextInt();
		m_x = new double[m_nbCities];
		m_y = new double[m_nbCities];
		m_labels = new String[m_nbCities];

		do {
			line = sc.nextLine();
			System.err.println(line);
		} while (!line.startsWith("EDGE_WEIGHT_SECTION"));


		int index = 0;

		// Create the distance matrix
		m_distances = new long[m_nbCities][];
		for (int i = 0; i < m_nbCities; i++) {
			m_distances[i] = new long[m_nbCities];
			m_x[i] = 0;
			m_x[i] = 0;
			m_labels[i] = Integer.toString(i);
		}
		
		line = sc.nextLine();
		// Compute distances
		for (int i = 0; i < m_nbCities-1; i++) {
			m_distances[i][i] = 0;
			lineSc = new Scanner(line);
			lineSc.useLocale(Locale.US);
			for (int j = i + 1; j < m_nbCities; j++) {
				long dist = lineSc.nextInt();
				System.out.println(dist);
				m_distances[i][j] = dist;
				m_distances[j][i] = dist;
			}
			line = sc.nextLine();
		}

		sc.close();
		lineSc.close();
	}

	/**
	 * Computes the geographical distance between two cities
	 * @param i the first index
	 * @param j the second index
	 * @return the geographical distance between i and j 
	 */
	private long geoDist(int i, int j) {

		double PI = 3.141592;
		double longRadianI = PI*m_x[i]/180.0;
		double latRadianI = PI*m_y[i]/180.0;
		double longRadianJ = PI*m_x[j]/180.0;
		double latRadianJ = PI*m_y[j]/180.0;
		double RRR = 6378.388;
		double q1 = Math.cos(longRadianI - longRadianJ);
		double q2 = Math.cos(latRadianI - latRadianJ);
		double q3 = Math.cos(latRadianI + latRadianJ);

		int res = (int) (RRR*Math.acos(0.5*((1.0+q1)*q2-(1.0-q1)*q3))+1.0);
		return res;
	}

	/**
	 * 
	 * @param i the first index
	 * @param j the second index
	 * @return the euclidian distance between i and j 
	 */
	private long distance(int i, int j) {
		double dx = m_x[i] - m_x[j];
		double dy = m_y[i] - m_y[j];
		return (long) Math.rint(Math.hypot(dx, dy));
	}

	/**
	 * @return the greatest x coordinate.
	 */
	public double getMaxX() {

		return getMax(m_x);
	}

	/**
	 * @return the greatest y coordinate.
	 */
	public double getMaxY() {

		return getMax(m_y);
	}

	/**
	 * @return the smallest x
	 */
	public double getMinX() {

		return getMin(m_x);
	}

	/**
	 * @return the smallest y
	 */
	public double getMinY() {

		return getMin(m_y);
	}

	/**
	 * Compute the maximum value of an array
	 * @param array an array
	 * @return the maximum value
	 */
	private double getMax(double[] array) {
		double maxVal = -Double.MAX_VALUE;
		for (int i = 0; i < m_nbCities; i++) {
			if (maxVal < array[i]) {
				maxVal = array[i];
			}
		}

		return maxVal;
	}

	/**
	 * Compute the minimum value of an array
	 * @param array an array
	 * @return the minimum value
	 */
	private double getMin(double[] array) {
		double minVal = Double.MAX_VALUE;
		for (int i = 0; i < m_nbCities; i++) {
			if (minVal > array[i]) {
				minVal = array[i];
			}
		}

		return minVal;
	}

	/**
	 * Print data on the output given as a parameter.
	 * 
	 * @param out : output stream
	 */
	public void print(PrintStream out) {

		out.println("Distance matrix:");
		for (int i = 0; i < m_nbCities; i++) {
			for (int j = 0; j < m_nbCities; j++) {
				out.print(m_distances[i][j] + ";");
			}
			out.println();
		}
		out.println();
	}

	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** @return the number of cities in the problem */
	public int getNbCities() {
		return m_nbCities;
	}

	/**
	 * @param i city number (should be defined between 0 an the number of
	 *          cities in the problem minus one). Note the city number is
	 *          not the label !
	 * @return the x coordinate of city i.
	 * @throws Exception
	 **/
	public double getX(int i) throws Exception {
		if((i < 0) || (i >= m_nbCities)) {
			throw new Exception("Error : city index " + i + " should range between 0 and " + (m_nbCities - 1) + ".");
		}
		return m_x[i];
	}

	/**
	 * @param i city number (should be defined between 0 an the number of
	 *          cities in the problem minus one). Note the city number is
	 *          not the label !
	 * @return the y coordinate of city i.
	 * @throws Exception
	 **/
	public double getY(int i) throws Exception {
		if((i < 0) || (i >= m_nbCities)) {
			throw new Exception("Error : city index " + i + " should range between 0 and " + (m_nbCities - 1) + ".");
		}
		return m_y[i];
	}

	/**
	 * Boolean to set if coordinates are geographical or not
	 **/
	public boolean m_isGeographic() {
		return m_isGeographic;
	}

	/**
	 * Set the coordinates attribute
	 * @param isGeographic the value of the coordinate `true` if coordinates are geographical
	 **/
	public void setGeographic(boolean isGeographic) {
		this.m_isGeographic = isGeographic;
	}

	/**
	 * @param i city number (should be defined between 0 an the number of cities in the problem minus one).
	 * 		**Note the city number is not the label !**
	 * @return the label of city i.
	 * @throws Exception
	 **/
	public String getLabel(int i) throws Exception {
		if((i < 0) || (i >= m_nbCities)) {
			throw new Exception("Error : city index " + i + " should range between 0 and " + (m_nbCities - 1) + ".");
		}
		return m_labels[i];
	}

	/**
	 * Returns the euclidean distance rounded to the nearest integer value
	 * between two cities. All distances are calculated when the tsp file is
	 * loaded, so this function does not calculate distances. Note: problems are
	 * symmetric, the distance from city i to city j is equal to the
	 * distance from j to i.
	 * 
	 * @param i origin city (should range between 0 and nbcity-1).
	 * @param j destination city (should range between 0 and nbcity-1).
	 * @return Returns the euclidean distance from i to j rounded to the nearest
	 *         integer value
	 * @throws Exception returns an error if i or j are not valid city numbers.
	 **/
	public long getDistances(int i, int j) throws Exception {
		if((i < 0) || (i >= m_nbCities)) {
			throw new Exception("Error : city index " + i + " should range between 0 and " + (m_nbCities - 1) + ".");
		}
		if((j < 0) || (j >= m_nbCities)) {
			throw new Exception("Error : city index " + j + " should range between 0 and " + (m_nbCities - 1) + ".");
		}
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

}
