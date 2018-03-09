/*
	tsp-framework

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

package tsp;

import java.io.IOException;

import tsp.gui.TSPGUI;

/**
 * This class contains the Main function, that is the function that is launched
 * when the program is run.
 * 
 * DO NOT WRITE OR MODIFY THIS CLASS.
 * 
 * Read the description of the Main method below, it describes how to set some
 * parameters for the program (such as the filename of the problem to solve).
 * 
 * When the program is run, the input data file is loaded in an Instance object.
 * A TSPSolver object is then created, given the Instance object, and its solve
 * method TSPSolver::solve is called. This is where you have to code your
 * algorithms.
 * 
 * The class to be modified is TSPSolver, from where you may also create your
 * own classes.
 * 
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class Main {

	/**
	 * Main method.
	 * 
	 * DO NOT MODIFY THIS METHOD.
	 * 
	 * The parameters of the java program are described below:
	 * 
	 * **command**: java Main [options] datafile
	 * 
	 * **Options**:
	 *  - -help : prints this parameter description
	 *  - -t (int) :maximum number of seconds given to the algorithm
	 *  - -g : graphical output of the solution
	 *  - -v : trace level (print the solution at the end if true)
	 * 
	 * **Program output**: fileName;routeLength;time;e 
	 * 
	 * e is an error code:
	 *  - e = 0 -> the solution is feasible and returned within the time limit
	 *  - e = 1 -> unfeasible solution
	 *  - e = 2 -> overtime.
	 * 
	 * @param args program parameters.
	 */
	public static void main(String[] args) {
		String filename = null;
		long max_time = 60;
		boolean verbose = false;
		boolean graphical = false;
		int typeInstance = 0;

		// Parse commande line
		for (int i = 0; i < args.length; i++) {
			if (args[i].compareTo("-help") == 0) {
				System.err.println("The Traveling Salesman Problem");
				System.err.println("Program parameters:");
				System.err.println("command: java Main [options] dataFile");
				System.err.println("Options:");
				System.err.println("\t-help\t: prints this parameter description");
				System.err.println("\t-t\t\t: maximum number of seconds given to the algorithm (int)");
				System.err.println("\t-g\t\t: graphical output of the solution");
				System.err.println("\t-v\t\t: trace level");
				return;

			} else if (args[i].compareTo("-v") == 0) {
				verbose = true;
			} else if (args[i].compareTo("-g") == 0) {
				graphical = true;
			} else if (args[i].compareTo("-t") == 0) {
				try {
					max_time = Integer.parseInt(args[++i]);
				} catch (Exception e) {
					System.out.println("Error: The time given for -t is not a valid integer value.");
					System.exit(1);
				}
			} else if (args[i].compareTo("-i") == 0) {
				try {
					typeInstance = Integer.parseInt(args[++i]);
				} catch (Exception e) {
					System.out.println("error : the type of instance is not a valid type");
					System.exit(1);
				}
			} else {
				if (filename != null) {
					System.err.println("Error: There is a problem in the program parameters.");
					System.err.println("Value " + args[i] + " is not a valid parameter.");
					System.exit(1);
				}
				filename = args[i];
			}
		}

		// Create and solve problem
		try {
			
			// Read data
			Instance data = new Instance(filename, typeInstance);
			
			// Create a new problem
			TSPSolver tsp = new TSPSolver(data,max_time);

			// Solve the problem
			long t = System.currentTimeMillis();
			tsp.solve();
			t = System.currentTimeMillis() - t;

			// Evaluate the solution (and check whether it is feasible)
			boolean feasible = tsp.getSolution().isFeasible();

			int e = 0;
			if (!feasible) {
				e = 1;
				System.err.println(tsp.getSolution().getError());
			} else {
				if (t > (max_time + 1) * 1000) {
					e = 2;
					System.err.println("Error: Time limit exeeced !!!");
				}
			}
			
			if(graphical)
			{
				TSPGUI gui = new TSPGUI(tsp.getSolution());
			}
			
			System.out.println(filename + ";"
					+ tsp.getSolution().getObjectiveValue() + ";" + t + ";" + e);

			// If verbose, print the solution
			if (verbose) {
				data.print(System.err);
				tsp.getSolution().print(System.err);
				if (e == 1)
					System.err.println("Error: There is an error in the solution: " + tsp.getSolution().getError());
			}


		} catch (IOException e) {
			System.err.println("Error: An error has been met when reading the input file: " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			System.err.println();
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return;
	}
}
