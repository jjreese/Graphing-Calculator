//Geoffrey Balshaw, Rachel Corey White, Jonathan Reese
//edit 4/3/18

import java.awt.Graphics;
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
	double[] yValues;
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
	int y0Index;
	boolean xContains0 = false;
	int x0Index;
	
	public RefreshGraphPanel(GraphingCalculator gc,
							 String expression, 
							 double[] xValues,
							 double[] yValues, double[] yScaleValues) throws IllegalArgumentException {
		this.xValues = xValues;
		this.yValues = yValues;
		this.yScaleValues = yScaleValues;
		this.expression = expression;
		this.gc = gc;
		displayXYpairWindow.setSize(100, 40);
		displayXYpairWindow.add(xTextField, "North");
		displayXYpairWindow.add(yTextField, "South");
		this.addMouseListener(this);
		xTicArray = new String[xValues.length];
		yTicArray = new Double[yScaleValues.length];
		for(int i =0; i < xValues.length; i++) {
			xTicArray[i] = Double.toString(xValues[i]);
			if(xValues[i] == 0) { 
				xContains0 = true; 
				x0Index = i;
			}
		}
		
		// TODO : generate algorithm for making nice neat Y values
		// TODO : below statement is just a placeholder
		for(int i =0; i < yScaleValues.length; i++) {
			yTicArray[i] = (yScaleValues[i]);
			if(yScaleValues[i] == 0) { 
				yContains0 = true;
				y0Index = i;
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
			
		if(yContains0) {
			yPixelPointer = (int) (windowHeight - (y0Index * yValueToPixelsConversionFactor));
		}
		else { yPixelPointer = windowHeight - 50; }
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
		}
		
		if(yContains0) {
			yPixelPointer -= 20;
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
				
				g.drawString(xTicArray[i], xPixelPointer, yPixelPointer);
			}
		}
		else {
			yPixelPointer += 20;
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
				
				g.drawString(xTicArray[i], xPixelPointer, yPixelPointer);
			}
		}
		
		
		// generate y-axis tic marks
		if(xContains0) {
			xPixelPointer = (int) ((x0Index + 1) * xValueToPixelsConversionFactor);
		}
		else { xPixelPointer = 50; }
		for(int i=0; i < yTicArray.length; i++) {
			if(i==0) {
				yPixelPointer = 50;
			}			
			else{
				yPixelPointer += (int)(((yTicArray[i]) - (yTicArray[i-1]))  * yValueToPixelsConversionFactor);
				
			}
			g.drawString("--", xPixelPointer, yPixelPointer);
		}
		
		xPixelPointer -=20;
		for(int i=0; i < yTicArray.length; i++) {
			if(i==0) {
				yPixelPointer = 50;
			}			
			else{
				yPixelPointer += (int)(((yTicArray[i]) - (yTicArray[i-1]))  * yValueToPixelsConversionFactor);
				
			}
			g.drawString(yTicArray[yTicArray.length - 1 -i].toString(), xPixelPointer, yPixelPointer);
		}
				
		
		// fill array with pixel values for all of our xy pairs
		int[] xDrawArray = new int[xValues.length];
		int[] yDrawArray = new int[yValues.length];
		
		for(int i=0; i<xValues.length; i++) {
			xDrawArray[i] = (int)(Double.parseDouble(xTicArray[i]) *xValueToPixelsConversionFactor) + 50;
			yDrawArray[i] = windowHeight - ((int)((yValues[i]) * yValueToPixelsConversionFactor) + 50);
		}
		// for all the values we're displaying, draw an oval of width & height 10px at their calculated x and y pixel values
		for(int i=0; i<xValues.length; i++) {
			g.drawOval(xDrawArray[i], yDrawArray[i], 10, 10);
		}
		// for all ovals we generated, draw lines between them
		for(int i=0; i<xValues.length -1; i++) {
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
	    double xValue = xInPixels / xValueToPixelsConversionFactor;
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