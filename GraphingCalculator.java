//Geoffrey Balshaw, Rachel Corey White, Jonathan Reese

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GraphingCalculator implements Calculator, KeyListener, ActionListener {

	String enteredExpression;
	String enteredX;

	//Calculator GUI
	JFrame calcFrame = new JFrame();
	JPanel ioPanel = new JPanel();
	JPanel northPanel = new JPanel();
	JPanel southPanel = new JPanel();

	JLabel inputLabel = new JLabel("Input");
	JTextField inputTextField = new JTextField();
	JLabel xLabel = new JLabel("X = ");
	JTextField xTextField = new JTextField();
	JLabel graphLabel = new JLabel("X increment");
	JTextField graphTextField = new JTextField();

	JButton recallButton = new JButton("Recall");
	JTextField errorTextField = new JTextField();

	JLabel outputLabel = new JLabel("Log");
	JTextArea outputTextArea = new JTextArea();
	JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
	//
	
	
	public GraphingCalculator() {
		// TODO Auto-generated constructor stub
		System.out.println("Jonathan Reese, Rachel Corey White, Geoffrey Balshaw");
		
		calcFrame.getContentPane().add(ioPanel, "Center");
		calcFrame.getContentPane().add(northPanel, "North");
		calcFrame.getContentPane().add(southPanel, "South");

		calcFrame.setTitle("Expression Caclulator");
		ioPanel.setLayout(new GridLayout(1, 2));
		ioPanel.add(inputTextField);
		ioPanel.add(outputScrollPane);

		outputTextArea.setEditable(false);
		inputLabel.setHorizontalAlignment(JLabel.CENTER);
		outputLabel.setHorizontalAlignment(JLabel.CENTER);

		northPanel.setLayout(new GridLayout(1, 2));
		northPanel.add(inputLabel);
		northPanel.add(outputLabel);

		southPanel.setLayout(new GridLayout(1, 6));
		southPanel.add(xLabel);
		southPanel.add(xTextField);
		southPanel.add(graphLabel);
		southPanel.add(graphTextField);
		southPanel.add(recallButton);
		southPanel.add(errorTextField);

		errorTextField.setEditable(false);
		errorTextField.setBackground(Color.WHITE);
		errorTextField.setSelectedTextColor(Color.BLACK);

		inputTextField.addKeyListener(this);
		xTextField.addKeyListener(this);
		graphTextField.addKeyListener(this);
		recallButton.addActionListener(this);

		calcFrame.setSize(500, 500);
		calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calcFrame.setVisible(true);
	}

	public static void main(String args[]) {
		System.out.println("Jonathan Reese, Rachel Corey White, Geoffrey Balshaw");
		new GraphingCalculator();
		// handleParenthesis("(10+5+2-3)*54+69/(19+4)");
	}
	
	@Override
	public double calculate(String expression, String x) throws Exception {
		//System.out.println("Jonathan Reese, Rachel Corey White, Geoffrey Balshaw");
		enteredExpression = expression;
		String smallExpression = null;
		expression = operandSubstitution(expression, x);
		// while (expression.contains("^") || expression.contains("r")
		// ||expression.contains( "*") || expression.contains("/") ||
		// expression.contains("+") || expression.contains("-") ) { //LOOP HERE FOR EACH
		// PART
		// as long as there are still operations to be done, evaluate the expression

		if (expression.contains("(")) {
			//System.out.println("expression to be handled is " + expression);
			expression = handleParenthesis(expression);

		} else {
			smallExpression = expression;
		}
		smallExpression = evaluateComplexExpression(expression);
		smallExpression = smallExpression.replace('u', '-');
		//System.out.println(smallExpression);
		// }
		return Double.parseDouble(smallExpression); // TEMP RETURN VALUE

	}

	private String handleParenthesis(String expression) throws Exception {
		// TODO Find the innermost parenthesis set and returns its section of the
		// expression

		// SHOULD RETURN AN ERROR IF NOT AN EQUAL # OF PARENTHESIS?

		System.out.println("Entering handleParenthesis");
		int leftParenIndex = 0; // = expression.lastIndexOf("(");
		int rightParenIndex = 0; // = expression.indexOf(")");
		int parenCount = 0;

		int i = 0;
		int[] countarray = new int[expression.length()];

		//Fixes Parenthesis Error
		if (expression.indexOf(')') < expression.indexOf('('))
			throw new IllegalArgumentException("Mismatched parenthesis");
		
		for (i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(')
				parenCount++;
			if (expression.charAt(i) == ')')
				parenCount--;
			countarray[i] = parenCount;
		}

		if (countarray[expression.length() - 1] != 0) {
			System.out.println("Uneven left and right parenthesis.");
			throw new IllegalArgumentException("Uneven left and right parenthesis.");
		}

		int max = 0;

		for (i = 0; i < countarray.length; i++) {
			if (countarray[i] > max)
				max = countarray[i];
		}

		for (i = 0; i < countarray.length; i++) {
			if (countarray[i] == max) {
				leftParenIndex = i;
				break;
			}
		}
		
		// String afterParen = expression.substring(leftParenIndex+1);
		rightParenIndex = expression.indexOf(')', leftParenIndex);

		String leftOfParen = expression.substring(0, leftParenIndex);

		// if(leftParenIndex != 0) {
		// String leftOfParen = expression.substring(0, leftParenIndex-1);
		// }
		// else {
		// String leftOfParen = null;
		// }

		String rightOfParen = expression.substring(rightParenIndex + 1);
		String innerExpression = expression.substring(leftParenIndex + 1, rightParenIndex);

		String intermedExpression = evaluateComplexExpression(innerExpression);

		String finalExpression = leftOfParen + intermedExpression + rightOfParen;

		//System.out.println("finalExpression is " + finalExpression);

		if (finalExpression.contains("("))
			finalExpression = handleParenthesis(finalExpression);

		return finalExpression;
	}

	private String evaluateComplexExpression(String complexExpression) throws Exception {
		// TODO Handle complex expressions, calling evaluateSimpleExpression when 2 are
		// left
		System.out.println("Entering complex eval");
		String simpleExpress = " ";
		String simpleExpressVal = " ";
		int beforeOperator = ' ';
		int afterOperator = ' ';
		int currentOperator = ' ';
		int i;

		System.out.println("complexExpression is " + complexExpression);
		
		//Fixes Missing operand issue
		if( (complexExpression.length() - complexExpression.replace(".","").length() > 1 || complexExpression.contains(" ") ) &&  !(( complexExpression.contains("r") || complexExpression.contains("^") || complexExpression.contains("*")
				|| complexExpression.contains("/") || complexExpression.contains("+")
				|| complexExpression.contains("-")))) 
			throw new IllegalArgumentException("Missing operand");
		//
		
		while (complexExpression.contains("r") || complexExpression.contains("^") || complexExpression.contains("*")
				|| complexExpression.contains("/") || complexExpression.contains("+")
				|| complexExpression.contains("-")) {

			if (complexExpression.charAt(0) == '-') {
				complexExpression = 'u' + complexExpression.substring(1);
			}

			if (complexExpression.contains("r") && complexExpression.contains("^")) {
				if (complexExpression.indexOf("r") < complexExpression.indexOf("^")) {
					currentOperator = complexExpression.indexOf("r");
				} else {
					currentOperator = complexExpression.indexOf("^");
				}
			}

			else if (complexExpression.contains("r")) {
				currentOperator = complexExpression.indexOf("r");
			} else if (complexExpression.contains("^")) {
				currentOperator = complexExpression.indexOf("^");
			}

			else if (complexExpression.contains("*") && complexExpression.contains("/")) {
				if (complexExpression.indexOf("*") < complexExpression.indexOf("/")) {
					currentOperator = complexExpression.indexOf("*");
				} else {
					currentOperator = complexExpression.indexOf("/");
				}
			}

			else if (complexExpression.contains("*")) {
				currentOperator = complexExpression.indexOf("*");
			} else if (complexExpression.contains("/")) {
				currentOperator = complexExpression.indexOf("/");
			}

			else if (complexExpression.contains("+") && complexExpression.contains("-")) {
				if (complexExpression.indexOf("+") < complexExpression.indexOf("-")) {
					currentOperator = complexExpression.indexOf("+");
				} else {
					currentOperator = complexExpression.indexOf("-");
				}
			}

			else if (complexExpression.contains("+")) {
				currentOperator = complexExpression.indexOf("+");
			} else if (complexExpression.contains("-")) {
				currentOperator = complexExpression.indexOf("-");
			}
		

			//System.out.println("index of currentOperator is " + currentOperator);

			for (i = currentOperator - 1; i >= 0; i--) {
				if ((complexExpression.charAt(i) == 'r') || (complexExpression.charAt(i) == '^')
						|| (complexExpression.charAt(i) == '*') || (complexExpression.charAt(i) == '/')
						|| (complexExpression.charAt(i) == '+') || (complexExpression.charAt(i) == '-')) {
					beforeOperator = i;
					break;
				} else {
					beforeOperator = -1;
				}
			}

			//System.out.println("index of beforeOperator is " + beforeOperator);

			for (i = currentOperator + 1; i < complexExpression.length(); i++) {
				if ((complexExpression.charAt(i) == 'r') || (complexExpression.charAt(i) == '^')
						|| (complexExpression.charAt(i) == '*') || (complexExpression.charAt(i) == '/')
						|| (complexExpression.charAt(i) == '+') || (complexExpression.charAt(i) == '-')) {
					afterOperator = i;
					break;
				} else {
					afterOperator = -1;
				}
			}

			//System.out.println("index of afterOperator is " + afterOperator);

			if ((beforeOperator == -1) && (afterOperator == -1)) {
				simpleExpress = complexExpression;
			} else if (beforeOperator == -1) {
				simpleExpress = complexExpression.substring(0, afterOperator);
			} else if (afterOperator == -1) {
				simpleExpress = complexExpression.substring(beforeOperator + 1);
			} else {
				simpleExpress = complexExpression.substring(beforeOperator + 1, afterOperator);
			}
		
			if (simpleExpress.charAt(0) == 'u') {
				simpleExpress = '-' + simpleExpress.substring(1);
			}

					
			//Fix of double minus issue
			simpleExpress = simpleExpress.replace("-u","+");	
		
			//System.out.println("simpleExpress is " + simpleExpress);
			simpleExpressVal = evaluateSimpleExpression(simpleExpress);
			
			if (simpleExpress.charAt(0) == '-') {
				simpleExpress = 'u' + simpleExpress.substring(1);
			}

		

			
			//System.out.println("simpleExpressVal is " + simpleExpressVal);
			//System.out.println("simpleExpress is " + simpleExpress);
			//System.out.println("Complex ex is" + complexExpression);
			complexExpression = complexExpression.replace(simpleExpress, simpleExpressVal);
			simpleExpressVal = simpleExpressVal.replace('-', 'u');
			if (complexExpression.charAt(0) == '-') {
				complexExpression = 'u' + complexExpression.substring(1);
			}
			//Fix of Double Minus Issue
			complexExpression = complexExpression.replace("-u","+");
			
			//System.out.println("Complex express at bottom of while is " + complexExpression);
		}
		
		return complexExpression;
	}

	private String evaluateSimpleExpression(String simpleExpression) throws Exception {
		// TODO Handles 1 operator remaining
		simpleExpression = simpleExpression.replaceAll("u", "-");
		//System.out.println("simpleExpression is " + simpleExpression);
		char operator = ' ';
		int i;
		for (i = 1; i < simpleExpression.length(); i++) {
			if ((simpleExpression.charAt(i) == '+') || (simpleExpression.charAt(i) == '-')
					|| (simpleExpression.charAt(i) == '*') || (simpleExpression.charAt(i) == '/')
					|| (simpleExpression.charAt(i) == '^') || (simpleExpression.charAt(i) == 'r')) {
				operator = simpleExpression.charAt(i);
				break;
			}
		}

		if ((i == simpleExpression.length()) || (i == simpleExpression.length() - 1)) {
			System.out.println("Expression is not an operator surrounded by operands");
			throw new IllegalArgumentException("Expression is not an operator surrounded by operands");
			// return null;
		}

		String leftOperand = simpleExpression.substring(0, i).trim();
		String rightOperand = simpleExpression.substring(i + 1).trim();

		double leftNumber;
		try {
			leftNumber = Double.parseDouble(leftOperand);
			//System.out.println("leftOperand is " + leftOperand);
		} catch (NumberFormatException nfe) {
			System.out.println("Left operand " + leftOperand + " is not numeric.");
			throw new IllegalArgumentException("Left operand " + leftOperand + " is not numeric.");
			// return null; }
		}

		double rightNumber;
		try {
			rightNumber = Double.parseDouble(rightOperand);
		} catch (NumberFormatException nfe) {
			System.out.println("Right operand is not numeric.");
			throw new IllegalArgumentException("Right operand is not numeric.");
			// return null; }
		}

		double result = 0;
		switch (operator) {
		case '+':
			result = leftNumber + rightNumber;
			break;
		case '-':
			result = leftNumber - rightNumber;
			break;
		case '*':
			result = leftNumber * rightNumber;
			break;
		case '/':
			result = leftNumber / rightNumber;
			break;
		case '^':
			result = Math.pow(leftNumber, rightNumber);
			break;
		case 'r':
			result = Math.pow(leftNumber, 1 / rightNumber);
			break;
		}

		return Double.toString(result);
	}

	private String operandSubstitution(String expression, String x) throws Exception {
		// TODO Folds to lowercase, replaces pi, e, and x in the expression
		System.out.println("SUBSTITUTING");
		expression = expression.trim().toLowerCase();

		if ((expression.contains("x"))) { // No valid x given
			try {
				Double.parseDouble(x);
			} catch (Exception e) {
				throw new IllegalArgumentException("x is not parseable to a double");
			}
		} else if (!x.equals("") && !expression.contains("x"))
			throw new IllegalArgumentException("Expression does not contain x with x value specified");

		if (x.startsWith("-"))
			x = 'u' + x.substring(1);
		
		// Data Validation for x //
		String tempex = expression.replace(" ", "");
		int numx = tempex.length() - tempex.replace("x", "").length();
		for (int i = 0; i < numx; i++) {
			if (tempex.indexOf("x") != 0) {
				switch (tempex.charAt(tempex.indexOf("x") - 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
				case '(':
					break;
				default:
					throw new IllegalArgumentException("x has no operator in front of it");
				}
			}
			if (tempex.indexOf("x") != tempex.length() - 1) {
				switch (tempex.charAt(tempex.indexOf("x") + 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
				case ')':
					break;
				default:
					throw new IllegalArgumentException("x has no operator behind it");
				}
			//	System.out.println("Old tempex: " + tempex);
				tempex = tempex.substring(tempex.indexOf("x") + 1, tempex.length());
			//	System.out.println("New tempex: " + tempex);
			}
		}
		//
		expression = expression.replaceAll("x", x);

		// Data Validation for e //
		tempex = expression.replace(" ", "");
		numx = tempex.length() - tempex.replace("e", "").length();
		for (int i = 0; i < numx; i++) {
			if (tempex.indexOf("e") != 0) {
				switch (tempex.charAt(tempex.indexOf("e") - 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
					break;
				default:
					throw new IllegalArgumentException("e has no operator next to it");
				}
			}
			if (tempex.indexOf("e") != tempex.length() - 1) {
				switch (tempex.charAt(tempex.indexOf("e") + 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
					break;
				default:
					throw new IllegalArgumentException("e has no operator next to it");
				}
				//System.out.println("Old tempex: " + tempex);
				tempex = tempex.substring(tempex.indexOf("e") + 1, tempex.length());
				//System.out.println("New tempex: " + tempex);
			}
		}
		expression = expression.replaceAll("e", String.valueOf(Math.E));

		// Data Validation for pi //
		tempex = expression.replace(" ", "");
		tempex = tempex.replace("pi", "e");
		numx = tempex.length() - tempex.replace("e", "").length();
		for (int i = 0; i < numx; i++) {
			if (tempex.indexOf("e") != 0) {
				switch (tempex.charAt(tempex.indexOf("e") - 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
					break;
				default:
					throw new IllegalArgumentException("pi has no operator next to it");
				}
			}
			if (tempex.indexOf("e") != tempex.length() - 1) {
				switch (tempex.charAt(tempex.indexOf("e") + 1)) {
				case '+':
				case '-':
				case '*':
				case '/':
				case 'r':
				case '^':
					break;
				default:
					throw new IllegalArgumentException("pi has no operator next to it");
				}
			//	System.out.println("Old tempex: " + tempex);
				tempex = tempex.substring(tempex.indexOf("e") + 1, tempex.length());
			//	System.out.println("New tempex: " + tempex);
			}
		}
		expression = expression.replaceAll("pi", String.valueOf(Math.PI));

		//System.out.println(expression);
		return expression;
	}



	@Override
	public void keyPressed(KeyEvent e) {
			// This method runs if there is an expression in the text area

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					if(!graphTextField.getText().equals("")) {
						//Graphing Time!//
						System.out.println("Graphing Time!");
						
						Graph(inputTextField.getText(),xTextField.getText(),graphTextField.getText());
					}
					else
					{
						double answer = calculate(inputTextField.getText(), xTextField.getText());
					outputTextArea.append(enteredExpression + " = " + answer);
					if (inputTextField.getText().toLowerCase().contains("x")) {
						outputTextArea.append(" for x = " + xTextField.getText() + "\n");
					} else {
						outputTextArea.append("\n");
					}
					}
					outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());
					inputTextField.setText("");
					errorTextField.setText("");
					xTextField.setText("");
					graphTextField.setText("");
					errorTextField.setBackground(Color.WHITE);
				} 
				catch (Exception e1) {
					errorTextField.setText(e1.toString());
					errorTextField.setBackground(Color.PINK);
				}
			}

		}

	private void Graph(String equationInput, String xInput, String xScaleInput) {
		// TODO Draws the graph, calculate's y values, and x scale increments
		
		double xValue;
		double xScaleValue;
				
		double xScaleArray[] = new double[10];		
		
		try {xValue = Double.parseDouble(xInput); xScaleValue = Double.parseDouble(xScaleInput);} 
		catch(Exception ile) {throw new IllegalArgumentException("No valid x and/or x scale value");}
		
		for (int i = 0; i < 10; i++) {
			xScaleArray[i] = xValue + (i*xScaleValue);
		}
		
		if((xScaleArray[9] > 0) && (xScaleArray[0] < 0)) {
			xScaleArray = Arrays.copyOf(xScaleArray, xScaleArray.length+1);
			xScaleArray[10] = xValue + (10*xScaleValue);
		}
		
		double yValArray[] = new double[xScaleArray.length];
		double yScaleArray[] = new double[xScaleArray.length];
		
		for (int i = 0; i < xScaleArray.length; i++) {
			try { yValArray[i] = calculate(equationInput,Double.toString(xScaleArray[i])); }
			catch(Exception e) {throw new IllegalArgumentException(e);}
		}
		
		//CHECK FOR VALUES NOT BEING A NUMBER (SECTION T IN RUBRIC)
		for (int i = 0; i < xScaleArray.length; i++) {
		if(Double.isNaN(yValArray[i])) {//Not a number, shift down
			System.out.println("I: " + i);
			for (int j = i; j < xScaleArray.length -1; j++)
			{
				yValArray[j] = yValArray[j+1];
				xScaleArray[j] = xScaleArray[j+1];
			}
			i--;
			yValArray = Arrays.copyOf(yValArray,yValArray.length -1);
			xScaleArray = Arrays.copyOf(xScaleArray, xScaleArray.length-1);
			}
		}
				//
		
	 	yScaleArray = calcYScalePrintValues(yValArray);
	 	
	 	System.out.println("X Vals are ");
	 	for(int i=0; i<xScaleArray.length; i++) {
	 		System.out.println(xScaleArray[i]);
	 		System.out.println(" ");
	 	}
	 	System.out.println("Y Vals are ");
	 	for(int i=0; i<yValArray.length; i++) {
	 		System.out.println(yValArray[i]);
	 		System.out.println(" ");
	 	}
	 	System.out.println("Y Scale Vals are ");
	 	for(int i=0; i<yScaleArray.length; i++) {
	 		System.out.println(yScaleArray[i]);
	 		System.out.println(" ");
	 	}
				
		//Graph
		JFrame graphFrame = new JFrame();
		RefreshGraphPanel graphPanel = new RefreshGraphPanel(this, enteredExpression, xScaleArray, yValArray, yScaleArray);
		graphFrame.getContentPane().add(graphPanel,"Center");
		graphFrame.setSize(500,500);
		graphFrame.setTitle(enteredExpression);
		graphFrame.setLocation(500,0);
		graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		graphFrame.setVisible(true);
	}


	private double[] calcYScalePrintValues(double yValArray[]) {
		double yScaleArray[] = new double[yValArray.length];
		
		double yMin = yValArray[0];
		double yMax = yValArray[0];
		double yRange;
		int initialIncrement;
		
		int i;
		String zeros = "0000000000";
		
		for(i=0; i < yValArray.length; i++) {
			if(yValArray[i] > yMax) { yMax = yValArray[i]; }
			if(yValArray[i] < yMin) { yMin = yValArray[i]; }
 		}
		
		yRange = yMax - yMin;
		initialIncrement = (int) yRange/yValArray.length;
		
		double dubIncrement=0;
		//CORRECTION FOR INCREMENT VALUE BEING LESS THAN 1//
		if (initialIncrement == 0)
		{
			dubIncrement  = yRange/yValArray.length;
			System.out.println("DUB: " + dubIncrement);
			for (i = 0; i < yScaleArray.length; i++)
			{
				yScaleArray[i] = yMin + i*dubIncrement;
			}
			return yScaleArray;
		}
		/////////////////////////////////////////////////////
		
		
		String initialIncrementString = String.valueOf(initialIncrement);
		
		String leadingDigit = initialIncrementString.substring(0,1);
		int leadingNumber = Integer.parseInt(leadingDigit);
		int bumpedLeadingNumber = leadingNumber + 1;
		String bumpedLeadingDigit = String.valueOf(bumpedLeadingNumber);
		String upperIncrementString = bumpedLeadingDigit + zeros.substring(0,initialIncrementString.length()-1);
		String lowerIncrementString = leadingDigit       + zeros.substring(0,initialIncrementString.length()-1);
		int upperIncrement = Integer.parseInt(upperIncrementString);
		int lowerIncrement = Integer.parseInt(lowerIncrementString);
		System.out.println("Upper increment alternative = " + upperIncrement);
		System.out.println("Lower increment alternative = " + lowerIncrement);
		
		int selectedIncrement;
		
		//int distanceToUpper = upperIncrement - initialIncrement;
		//int distanceToLower = initialIncrement - lowerIncrement;
		//if (distanceToUpper > distanceToLower)
			//selectedIncrement = lowerIncrement;
		//else
		selectedIncrement = upperIncrement;
		
		
		int numberOfYscaleValues = 0;
		int lowestYscaleValue    = 0;
		if (yMin < 0) {
		     for (; lowestYscaleValue > yMin; lowestYscaleValue-=selectedIncrement)
		          numberOfYscaleValues++;
		}
		if (yMin > 0) {
			 for (; lowestYscaleValue < yMin; lowestYscaleValue+=selectedIncrement)
			      numberOfYscaleValues++;
		     numberOfYscaleValues--;
		     lowestYscaleValue -= selectedIncrement;
		}
		
		for(i=0; i<yValArray.length;i++) {
			yScaleArray[i] = lowestYscaleValue + selectedIncrement*i;
		}
		
		if(!((yScaleArray[0] < 0) && (yScaleArray[(yScaleArray.length)-1] > 0))) {
			//does not include zero
			if((yScaleArray[0] > 0) && (yScaleArray[(yScaleArray.length)-1]/selectedIncrement <= 3) ) {
				System.out.println("Lower y scale can be adjusted to include the 0 point");
				yScaleArray[0] = 0;	
			}
			if((yScaleArray[yScaleArray.length-1] <0) && (yScaleArray[yScaleArray.length-1]/selectedIncrement <= 3)) {
				System.out.println("Upper y scale can be adjusted to include the 0 point");
				yScaleArray[yScaleArray.length-1] = 0;
			}
		}
		
		int yScaleValue = (int) yScaleArray[0];
		while(yScaleValue < yScaleArray[yScaleArray.length-1]) {
			System.out.println(yScaleValue + ",");
			yScaleValue += selectedIncrement;
		}
		System.out.println(yScaleValue);
		
		return yScaleArray;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getSource() == recallButton) {
			inputTextField.setText(enteredExpression);
			xTextField.setText(enteredX);
			errorTextField.setText("");
			errorTextField.setBackground(Color.WHITE);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		/* Ignore */ }

	@Override
	public void keyTyped(KeyEvent arg0) {
		/* Ignore */ }

}
