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
	
	public RefreshGraphPanel(GraphingCalculator gc,
							 String expression, 
							 double[] xValues,
							 double[] yValues) throws IllegalArgumentException {
		this.xValues = xValues;
		this.yValues = yValues;
		this.expression = expression;
		this.gc = gc;
		displayXYpairWindow.setSize(200, 200);
		displayXYpairWindow.add(xTextField);
		displayXYpairWindow.add(yTextField);
		this.addMouseListener(this);
				
		for(int i =0; i < xValues.length; i++) {
			xTicArray[i] = Double.toString(xValues[i]);
		}
		
		// TODO : generate algorithm for making nice neat Y values
		// TODO : below statement is just a placeholder
		for(int i =0; i < yValues.length; i++) {
			yTicArray[i] = (yValues[i]);
		}
	}
	
	@Override
	public void paint(Graphics g)//Override paint
	{
		int windowWidth = this.getWidth();
		int windowHeight = this.getHeight();
		System.out.println("Current graph size is " + windowWidth + " x " + windowHeight);
		
		Integer xPixelPointer = 50;
		Integer yPixelPointer = windowHeight - 50;
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
			
		xValueToPixelsConversionFactor = ((windowWidth) -50 -50) / ((xValues[xValues.length] - xValues[0]));   
		yValueToPixelsConversionFactor = ((windowHeight) -50 -50) / ((yValues[yValues.length] - yValues[0]));
			
		// generate x-axis tic marks
		for(int i=0; i < xValues.length; i++) {
			// add difference in xTicArray values * conversion factor to xPixelPointer
				// ex: if each 1 gets 40 pixels, and our array is [-1, 0, 1]
					// xPixelPointer will start at 50, then add (0-(-1))*40 = 40px for the next index, etc.
			if(i==0) {
				xPixelPointer = 50;
			}
			else{
				xPixelPointer += ((Integer.parseInt(xTicArray[i]) - Integer.parseInt(xTicArray[i-1])) * xValueToPixelsConversionFactor.intValue());
			}
			
			g.drawString("|", xPixelPointer, yPixelPointer);
		}
		
		// generate y-axis tic marks
		xPixelPointer =50;
		for(int i=0; i < yTicArray.length; i++) {
			if(i==0) {
				yPixelPointer = 50;
			}			
			else{
				yPixelPointer += (((yTicArray[i]).intValue() - (yTicArray[i-1]).intValue())  * yValueToPixelsConversionFactor.intValue());
				g.drawString("--", xPixelPointer, yPixelPointer);
			}
		}
				
		
		// fill array with pixel values for all of our xy pairs
		int[] xDrawArray = new int[xValues.length];
		int[] yDrawArray = new int[yValues.length];
		
		for(int i=0; i<xValues.length; i++) {
			xDrawArray[i] = (Integer.parseInt(xTicArray[i]) *xValueToPixelsConversionFactor.intValue()) + 50;
			yDrawArray[i] = ((yTicArray[i]).intValue() *yValueToPixelsConversionFactor.intValue()) + 50;
		}
		// for all the values we're displaying, draw an oval of width & height 20px at their calculated x and y pixel values
		for(int i=0; i<xValues.length; i++) {
			g.drawOval(xDrawArray[i], yDrawArray[i], 20, 20);
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
	    int xInPixels = me.getX();
	    double xValue = xInPixels * xValueToPixelsConversionFactor;
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
	    displayXYpairWindow.setLocation(me.getX(), me.getY());
	    displayXYpairWindow.setVisible(true); 
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// "erase" mini x,y display window	
	    displayXYpairWindow.setVisible(false);
		
	}

}
