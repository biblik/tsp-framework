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

import gui.Frame.Frame_;
import gui.Frame.Frame_Swing_;
import gui.abstraction.IntervalWindow_;
import gui.abstraction.Point_;
import gui.components.macro.TSPGraph_;

import java.util.ArrayList;

/**
 * Produces a graphical representation of a TSP solution.
 * 
 * @author Damien Prot
 * 
 */
public class Drawing {

	TSPGraph_ content;

	ArrayList<Point_> towns = new ArrayList<Point_>();
	Frame_ frame;

	private double maxX;

	private double minX;

	private double maxY;

	private double minY;

	int[][] bounds = new int[][] { { 1, 100 }, { 1, 100 } };

	// ///////////////
	// CONSTRUCTOR //
	// ///////////////

	/**
	 * 
	 * Produces a graphical representation of a TSP solution.
	 * 
	 * @param width
	 *            : window width
	 * @param height
	 *            : window height
	 * @param solution
	 *            : solution to represent
	 * @throws Exception
	 */
	public Drawing(final int width, final int height, Solution solution)
			throws Exception {
		super();
		final IntervalWindow_ xAxis = new IntervalWindow_(0, bounds[0][1]);
		final IntervalWindow_ yAxis = new IntervalWindow_(0, bounds[1][1]);

		maxX = solution.getInstance().getMaxX();
		maxY = solution.getInstance().getMaxY();
		minX = solution.getInstance().getMinX();
		minY = solution.getInstance().getMinY();

		double deltaX = (maxX - minX) * 0.05;
		double deltaY = (maxY - minY) * 0.05;

		maxX += deltaX;
		minX -= deltaX;
		maxY += deltaY;
		minY -= deltaY;

		double compressionFactorX = (maxX - minX) / bounds[0][1];
		double compressionFactorY = (maxY - minY) / bounds[1][1];

		// final IntervalWindow_ xAxis = new IntervalWindow_(0, 100);
		// final IntervalWindow_ yAxis = new IntervalWindow_(0, 100);

		frame = new Frame_Swing_(0, 0, width, height);
		content = new TSPGraph_(new double[] { 0, 0, width - 30, height - 50 },
				xAxis, yAxis, frame);
		frame.addContent(content, xAxis, yAxis);

		Instance instance = solution.getInstance();
		// Adding the towns
		// Reordering town by visit
		for (int i = 0; i < instance.getNbVertices(); i++) {
			int idTown = solution.getSolution(i);
			int xTown = (int) ((instance.getX(idTown) - minX) / compressionFactorX);
			int yTown = (int) ((instance.getY(idTown) - minY) / compressionFactorY);
			 String labelTown = instance.getLabel(idTown);
//			String labelTown = "" + i;
			towns.add(new Point_(xTown, yTown, labelTown));

		}
		content.getGeoGraphArea_().addPoints(towns);
		content.getGeoGraphArea_().drawPolygon(towns);
		content.setVal(solution.getObjective());

		frame.confirm();
		frame.refreshView();

	}

}
