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
import java.io.IOException;

/**
 * This class contains the main function, that is the function that is launched when 
 * the program is run. 
 * <br> 
 * <br> DO NOT WRITE OR MODIFY THIS CLASS. 
 * <br> 
 * <br> Read the description of the <code>main</code> method below, it describes how to set 
 * some parameters for the program (such as the filename of the problem to solve).
 * <br> 
 *  <br> When the program is run, the input data file is loaded in an <code>Instance</code> object. 
 *  A <code>TSPSolver</code> object is then created, given the <code>Instance</code> object, 
 *  and its <code>solve()</code> method is called. This is where you have to code your algorithms.   
 * <br> 
 * <br> The class to be modified is <code>TSPSolver</code>, from where you may also create your own classes.
 * 
 * @author Damien Prot, Fabien Lehuédé 2012
 * 
 */
public class Main {

  /**
   * Main method. DO NOT MODIFY THIS METHOD.
   * <br> 
   * <br> The parameters of the java program are described below:  
   * <br> command: <code>java Main [options] datafile</code>
   * <br> Options:
   * <li> -help     : prints this parameter description
   * <li> -t (int)  : maximum number of seconds given to the algorithm
   * <li> -v        : trace level (print the solution at the end if true)
   * <li> -g        : if stated, requires a graphical representation of the solution.
   * <br> 
   * <br> 
   * 
   * Program output: fileName;routeLength;time;e
   * <br> e is an error code :
   *  <li> e = 0 -> the solution is feasible and returned within the time limit.
   *  <li> e = 1 -> unfeasible solution.
   *  <li>  e= 2 -> overtime
   *  
   * @param arg program parameters.
   */
  public static void main(String[] arg) {
    String filename = null;
    long max_time = 30;
    boolean verbose = false;
    boolean graphical = false;

    // --- parse command-line ---
    for (int i = 0; i < arg.length; i++) {
      if (arg[i].compareTo("-help") == 0) {
	    System.err.println("The Traveling Salesman Problem");
	    System.err.println("Program parameters:");
	    System.err.println("command: java Main [options] instanceFile");
	    System.err.println("Options:");
	    System.err.println("  -help     : prints this parameter description");
	    System.err.println("  -t (int)  : maximum number of seconds given to the algorithm");
	    System.err.println("  -v        : trace level");
	    System.err.println("  -g        : if stated, requires a graphical representation of the solution.");
        

      } else if (arg[i].compareTo("-v") == 0) {
        verbose = true;
      } else if (arg[i].compareTo("-t") == 0) {
        try {
          max_time = Integer.parseInt(arg[++i]);
        } catch (Exception e) {
          System.out.println("Error: the time given for -t is not a valid integer value.");
          System.exit(1);
        }
      } else if (arg[i].compareTo("-g") == 0) {
        graphical = true;
      } else {
        if (filename != null) {
          System.err.println("Error: there is a problem in the program parameters.");
          System.err.println("Value " + arg[i] + " is not a valid parameter.");
          System.exit(1);
        }
        filename = arg[i];
      }
    }

    // --- create and solve problems ---
    try {
      TSPSolver tsp = new TSPSolver();
      // create a new problem; data is read from file filename
      Instance prob = new Instance(filename);
      tsp.setInstance(prob);
      tsp.setSolution(new Solution(prob));
      tsp.setTime(max_time);

      // print the data [uncomment if wanted]
      // prob.printData(System.err);

      // solve the problem
      long t = System.currentTimeMillis();
      tsp.solve();
      t = System.currentTimeMillis() - t;

      // evaluate the solution (and check whether it is feasible)

      boolean feasible = tsp.getSolution().validate();

      // program output: fileName;routeLength;t;e
      // e is an error code :
      // e = 0 -> the solution is feasible and returned within the time limit.
      // e = 1 -> unfeasible solution.
      // e= 2 -> time limit exceeded
      int e = 0;
      if (!feasible){
        e = 1;
      	System.err.println(tsp.getSolution().getError());
      }
      else {
        if (t > (max_time + 1) * 1000) {
          e = 2;
          System.err.println("Error: Time limit exeeced !!!");
        }
      }
      System.out.println(filename + ";" + tsp.getSolution().getObjective() + ";" + t + ";" + e);

      // if verbose, print the solution
      if (verbose) {
        prob.print(System.err);
        tsp.getSolution().print(System.err);
        if (e == 1)
          System.err.println("Error: There is an error in the solution: " + tsp.getSolution().getError());
      }

      // If graphical and no error, draw
      if (feasible && graphical) {
        // Graphical solution
        new Drawing(900, 700, tsp.getSolution());

      }
    } catch (IOException e) {
      System.err.println("Error: an error has been met when reading the input file: " + e.getMessage());
      System.exit(1);
    } catch (Exception e) {
      System.err.printf("Error: %s", e.getMessage());
      System.err.println();
      e.printStackTrace(System.err);
      System.exit(1);
    }
    return;
  }
}
