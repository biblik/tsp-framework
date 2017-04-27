package tsp.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tsp.Solution;

public class TSPGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	Solution m_solution;
	
	public TSPGUI(Solution solution)
	{
		super("TSP VIZUALISATION");
		this.m_solution = solution;
		
		final int GUIwidth = 800;
		final int GUIheight = 800;
		this.setSize(new Dimension(GUIwidth, GUIheight));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		JPanel p = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			int widthOffset = 100;
			int heightOffset = 100;
			int margin = 10;
			
			@Override
			public void paintComponent(Graphics g)
			{
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(1));
				g2.setRenderingHint(
	                    RenderingHints.KEY_ANTIALIASING, 
	                    RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawLine(widthOffset/2, heightOffset/2, GUIwidth - widthOffset/2, heightOffset/2);
				g2.drawLine(widthOffset/2, heightOffset/2, widthOffset/2, GUIheight - heightOffset/2);
				g2.drawLine(widthOffset/2, GUIheight - heightOffset/2, GUIwidth - widthOffset/2, GUIheight - heightOffset/2);
				g2.drawLine(GUIwidth - widthOffset/2, heightOffset/2, GUIwidth - widthOffset/2, GUIheight - heightOffset/2);
								
				for(int i = 0; i < m_solution.getInstance().getNbCities(); i++)
				{
					int cityOrigin = 0;
					int cityDestination = 0;
					try
					{
						cityOrigin = m_solution.getCity(i);
						cityDestination = m_solution.getCity(i+1);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
					double XOrigin = 0;
					double XDestination = 0;
					double YOrigin = 0;
					double YDestination = 0;
					try
					{
						XOrigin = scaleCoordinate(
								m_solution.getInstance().getX(cityOrigin),
								m_solution.getInstance().getMinX(),
								m_solution.getInstance().getMaxX(),
								GUIwidth - widthOffset - 2*margin,
								widthOffset/2 + margin);
						XDestination = scaleCoordinate(
								m_solution.getInstance().getX(cityDestination),
								m_solution.getInstance().getMinX(),
								m_solution.getInstance().getMaxX(),
								GUIheight - heightOffset - 2*margin,
								heightOffset/2 + margin);
						YOrigin = scaleCoordinate(
								m_solution.getInstance().getY(cityOrigin),
								m_solution.getInstance().getMinY(),
								m_solution.getInstance().getMaxY(),
								GUIwidth - widthOffset - 2*margin,
								widthOffset/2 + margin);
						YDestination = scaleCoordinate(
								m_solution.getInstance().getY(cityDestination),
								m_solution.getInstance().getMinY(),
								m_solution.getInstance().getMaxY(),
								GUIheight - heightOffset - 2*margin,
								heightOffset/2 + margin);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					g2.setColor(Color.black);
					Shape line = new Line2D.Double(XOrigin+5,YOrigin+5,XDestination+5,YDestination+5);
					g2.setRenderingHint(
		                    RenderingHints.KEY_ANTIALIASING, 
		                    RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setStroke(new BasicStroke(2));
					g2.draw(line);
					
				}
				
				for(int i = 0; i < m_solution.getInstance().getNbCities(); i++)
				{
					g2.setColor(Color.BLUE);
					double Xcoord = 0;
					double Ycoord = 0;
					try
					{
						Xcoord = scaleCoordinate(
								m_solution.getInstance().getX(i),
								m_solution.getInstance().getMinX(),
								m_solution.getInstance().getMaxX(),
								GUIwidth - widthOffset - 2*margin,
								widthOffset/2 + margin);
						Ycoord = scaleCoordinate(
								m_solution.getInstance().getY(i),
								m_solution.getInstance().getMinY(),
								m_solution.getInstance().getMaxY(),
								GUIheight - heightOffset - 2*margin,
								heightOffset/2 + margin);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					Shape round = new Ellipse2D.Double(Xcoord,Ycoord,10,10);
					g2.fill(round);
					String a = String.valueOf(i+1);
					char[] ac = a.toCharArray();
					g2.drawChars(ac, 0, ac.length, (int)Xcoord, (int)Ycoord);
				}
				
				g2.setColor(Color.BLACK);
				
				AttributedString as1 = new AttributedString("Objective Value: "+String.valueOf(m_solution.getObjectiveValue()));
			    as1.addAttribute(TextAttribute.SIZE, 15);
			    g2.drawString(as1.getIterator(), (int)widthOffset/2, (int)heightOffset/3);
				
			}
		};
		
		this.getContentPane().add(p);
	}
	
	
	public static double scaleCoordinate(double coordinate, double min, double max, double size, double offset)
	{
		return (offset + ((coordinate-min)/(max-min))*size);
	}


	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
