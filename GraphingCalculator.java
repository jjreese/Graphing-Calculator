
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
		// simpleExpression = simpleExpression.replaceAll("u", "-");
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

	private void Graph(String equationinput, String xinput, String xscaleinput) {
		// TODO Draws the graph, calculate's y values, and x scale increments
		
		double xValArray[] = new double[10];
		double yValArray[] = new double[10];
		double yScaleArray[] = new double[10];
		double xValue, xscalevalue;
		try {xValue = Double.parseDouble(xinput); xscalevalue = Double.parseDouble(xscaleinput);} 
		catch(Exception ile) {throw new IllegalArgumentException("No valid x/x scale value");}
		
		//Set Parameters//
		for (int i = 0; i < 10; i++) //10 Values of arrays, give us the values to graph
		{
			xValArray[i] = xValue + (i*xscalevalue);
			try {
			yValArray[i] = calculate(equationinput,Double.toString(xValArray[i]));
			}
			catch(Exception e) {throw new IllegalArgumentException(e);}
		}
		//Need yscalearray values here -> calcYscalePrintValues() See online notes
		
	 	//yScaleArray = calcYScalePrintValues(yValArray);
		
		//xscalearray = xvalarray just pass the same value twice
		
		//Need to pop a new graph window here -> drawGraph()
		JFrame graphFrame = new JFrame();
		RefreshGraphPanel graphPanel = new RefreshGraphPanel(this, enteredExpression, xValArray, yValArray);
		graphFrame.getContentPane().add(graphPanel,"Center");
		graphFrame.setSize(500,500);
		graphFrame.setTitle(enteredExpression);
		graphFrame.setLocation(500,0);
		graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		graphFrame.setVisible(true);

		
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
