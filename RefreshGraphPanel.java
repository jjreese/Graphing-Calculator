import java.awt.Graphics;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class RefreshGraphPanel extends JPanel implements MouseListener{

	/**
	 * 
	 */
	double[] xValues;
	double xMin;
	double xMax;
	double[] yValues;
	double yMin;
	double yMax;
	double[] yScaleValues;
	String expression;
	GraphingCalculator gc;
	private static final long serialVersionUID = 1L;
	JWindow displayXYpairWindow = new JWindow();
	JTextField xTextField = new JTextField();
	JTextField yTextField = new JTextField();
	Double xValueToPixelsConversionFactor =0.0;
	Double yValueToPixelsConversionFactor =0.0;
	String[] xTicArray;
	Double[] yTicArray;
	boolean yContains0 = false;
	double y0Index = 0;
	boolean xContains0 = false;
	double x0Index = 0;
	
	public RefreshGraphPanel(GraphingCalculator gc,
							 String expression, 
							 double[] xValues,
							 double[] yValues, double[] yScaleValues) throws IllegalArgumentException {
		this.xValues = xValues;
		this.yValues = yValues;
		this.yScaleValues = yScaleValues;
		this.expression = expression;
		this.gc = gc;
		displayXYpairWindow.setSize(150, 40);
		displayXYpairWindow.add(xTextField, "North");
		displayXYpairWindow.add(yTextField, "South");
		this.addMouseListener(this);
		xTicArray = new String[xValues.length];
		yTicArray = new Double[yScaleValues.length];
		for(int i =0; i < xValues.length; i++) {
			xTicArray[i] = Double.toString(xValues[i]);
			if(xValues[i] > xMax) { xMax = xValues[i]; }
			if(xValues[i] < xMin) { xMin = xValues[i]; }
			
			if(xValues[i] < 0 && xValues[i+1] <= 0) { x0Index++; }
			else if(xValues[i] < 0 && xValues[i+1] > 0) {
				x0Index = x0Index + Math.abs(xValues[i]) / (Math.abs(xValues[i])+xValues[i+1]);
			}
		}
		
		// TODO : generate algorithm for making nice neat Y values
		// TODO : below statement is just a placeholder
		for(int i =0; i < yScaleValues.length; i++) {
			yTicArray[i] = (yScaleValues[i]);
			if(yScaleValues[i] > yMax) { yMax = yScaleValues[i]; }
			if(yScaleValues[i] < yMin) { yMin = yScaleValues[i]; }
			
			if(yScaleValues[i] < 0 && yScaleValues[i+1] <= 0) { y0Index++; }
			else if(yScaleValues[i] < 0 && yScaleValues[i+1] > 0) {
				y0Index = y0Index + Math.abs(yScaleValues[i]) / (Math.abs(yScaleValues[i])+yScaleValues[i+1]);
			}
		}
	}
	
	@Override
	public void paint(Graphics g)//Override paint
	{
		int windowWidth = this.getWidth();
		int windowHeight = this.getHeight();
		System.out.println("Current graph size is " + windowWidth + " x " + windowHeight);
		
		Integer xPixelPointer = null;
		Integer yPixelPointer = null;
		//Now draw graph
		
		//int xTicSpace = ((windowWidth) -50 -50) / (xValues.length -1);
		//int yTicSpace = ((windowHeight) -50 -50) / (yValues.length -1);
		
		// value to pixel conversion factor
			// take width of graph display (width - 50 from left border - 50 from right border)
				// this is number of pixels across
			// take difference in values we're looking at (value in last array index - value in first array index)
			// divide pixels by value difference
		// ex: 500px display, - 50 - 50 = 400px graph
			// looking at values from 0 to 10
			// we get 40 pixels for every 1 increase in X
			
		xValueToPixelsConversionFactor = ((windowWidth) -50 -50) / ((xValues[xValues.length-1] - xValues[0]));   
		yValueToPixelsConversionFactor = ((windowHeight) -50 -50) / ((yScaleValues[yScaleValues.length-1] - yScaleValues[0]));
			
		
		
		if(yMin < 0 && yMax > 0) {
			yPixelPointer = 50 + (int) (y0Index * (((yTicArray[2]) - (yTicArray[1]))  * yValueToPixelsConversionFactor));
		}
		else { yPixelPointer = windowHeight - 50; }
		//draw horizontal line
		g.setColor(Color.green);
		g.drawLine(0, yPixelPointer, windowHeight, yPixelPointer);
		g.setColor(Color.black);
		for(int i=0; i < xValues.length; i++) {
			// add difference in xTicArray values * conversion factor to xPixelPointer
				// ex: if each 1 gets 40 pixels, and our array is [-1, 0, 1]
					// xPixelPointer will start at 50, then add (0-(-1))*40 = 40px for the next index, etc.
			if(i==0) {
				xPixelPointer = 50;
			}
			else {
				xPixelPointer += (int)((Double.parseDouble(xTicArray[i]) - Double.parseDouble(xTicArray[i-1])) * xValueToPixelsConversionFactor);
			}
			
			g.drawString("|", xPixelPointer, yPixelPointer);
			g.drawString(xTicArray[i], xPixelPointer, yPixelPointer-20);
		}
		
		// generate y-axis tic marks
		if(xMin < 0 && xMax > 0) {
			xPixelPointer = 50 + (int) (x0Index * ((Double.parseDouble(xTicArray[2]) - Double.parseDouble(xTicArray[1])) * xValueToPixelsConversionFactor));
		}
		else { xPixelPointer = 50; }
		
		g.setColor(Color.green);
		g.drawLine(xPixelPointer, 0, xPixelPointer, windowWidth);
		g.setColor(Color.black);
		
		for(int i=0; i < yTicArray.length; i++) {
			if(i==0) {
				yPixelPointer = 50;
			}			
			else {
				yPixelPointer += (int)(((yTicArray[i]) - (yTicArray[i-1]))  * yValueToPixelsConversionFactor);
			}
				g.drawString("--", xPixelPointer, yPixelPointer);
				g.drawString(yTicArray[yTicArray.length - 1 -i].toString(), xPixelPointer-30, yPixelPointer);
		}
				
		
		// fill array with pixel values for all of our xy pairs
		int[] xDrawArray = new int[xValues.length];
		int[] yDrawArray = new int[yValues.length];
		
		while(Double.parseDouble(xTicArray[0]) < 0) {
			for(int i = 0; i < xTicArray.length; i++) {
				xTicArray[i] = Double.toString(Double.parseDouble(xTicArray[i]) + 1);
			}
		}
		
		
		for(int i = 0; i < yValues.length; i++) {
			while(yValues[i] < 0) {
				for(int k = 0; k < yValues.length; k++) {
					yValues[k] = yValues[k] + 1;
				}
			}
		}		
		
		System.out.println("Y Values shifted to positive are: ");		
	 	for(int i=0; i<yValues.length; i++) {
	 		System.out.println(yValues[i]);
	 	}
	 	System.out.println("windowHeight is " + windowHeight);
	 	System.out.println("yValueToPixelConversion factor is " + yValueToPixelsConversionFactor);
		
		for(int i=0; i<xValues.length; i++) {
			xDrawArray[i] = (int)(Double.parseDouble(xTicArray[i]) *xValueToPixelsConversionFactor) + 50;
			yDrawArray[i] = windowHeight - (int) (((yValues[i]) * yValueToPixelsConversionFactor) + 50);
		}
		// for all the values we're displaying, draw an oval of width & height 10px at their calculated x and y pixel values
		for(int i=0; i<xValues.length; i++) {
			g.drawOval(xDrawArray[i]-5, yDrawArray[i]-5, 10, 10);
		}
		// for all ovals we generated, draw lines between them
		for(int i=0; i<xValues.length - 1; i++) {
			g.drawLine(xDrawArray[i], yDrawArray[i], xDrawArray[i+1], yDrawArray[i+1]);
		}
		
		
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		// TODO Auto-generated method stub
				// xTextField and yTextField are in the mini displayXYpairWindow
				int windowWidth = this.getWidth();
				int windowHeight = this.getHeight();
				int xInPixels = me.getX();
			    double xValue = (xInPixels - 50 + (xValues[0]*xValueToPixelsConversionFactor)) / xValueToPixelsConversionFactor;
			    String xValueString = String.valueOf(xValue);
			    xTextField.setText("X = " + xValueString);
			  
			    Double yValue;
				try {
					yValue = gc.calculate(expression,xValueString);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new IllegalArgumentException(e);
				}	    
			    String yValueString = String.valueOf(yValue); 
			    yTextField.setText("Y = " + yValueString);

			    // show mini x,y display window
			    displayXYpairWindow.setLocation(me.getX() + windowWidth + 30, me.getY());
			    displayXYpairWindow.setVisible(true); 		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// "erase" mini x,y display window	
	    displayXYpairWindow.setVisible(false);
		
	}

}