import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import javax.swing.JButton;

public class Main {
	String s = ""; // String built from user input
	double result = 0; // final result
	double tempResult; // result used in the output
	double finalNum = 0; // Used in the Math() method to keep track of previous operation result

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		Window window = new Window(); // Draw Window
		JButton[] buttons = window.getButtons();// Store JButtons

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(new ActionListener() {// Add actionListner to buttons
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.print(e.getActionCommand());
					if (e.getActionCommand() == "=") {
						Calculate(s);// Calculate the operation
						s = Double.toString(tempResult);
						window.txtArea.setText(Double.toString(tempResult));// display result
					} 
					else if (e.getActionCommand() != "C"){
						s += e.getActionCommand(); // Build the string to be processed
						window.txtArea.setText(s);// display pressed keys
					}
					else{
						s = Clear(s);
						window.txtArea.setText(s);
					}
				}
			});
		}
	}
	
	private String Clear(String s){
		s = s.substring(0, s.length() - 1);
		return s;
	}
	
	private void Calculate(String s) {

		int countBrackets = CountBrackets(s);
		
		while (countBrackets != 0) {// Do calculation inside brackets first, return result with new string 
			s = mathBrackets(s);
			System.out.println(s);
			countBrackets--;
		}

		int countOper = CountOperations(s);
		
		while (countOper != 0) {// Do the X and / operations first "count times"
			s = mathOne(s);
			countOper--;
		}

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '+' || s.charAt(i) == '-') { // Make sure only +, and - operations are left (Bedmass)
				mathTwo(s);// Calculate the rest of the operations
				break;
			} else if (i >= s.length() - 1) {
				result = Double.parseDouble(s); // if there is no more operations left, no need for calculation. end
			}
		}

		System.out.print(" " + result); // print result

		// ---RESET---
		tempResult = result; // set result to be printed out on textArea, before result gets reset
		result = 0;
		finalNum = 0;
		System.out.println("");// new line for console debugging
	}

	private int CountOperations(String s) { // count the amount of "X" and "/" operations to be done first
		int count = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'x' || s.charAt(i) == '/') {
				count++;
			}
		}
		return count; // return that number
	}
	
	private int CountBrackets(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i ++) {
			if (s.charAt(i) == '(') {
				count++;
			}
			if (s.charAt(i) == ')') {
				count++;
			}
		}
		return count/2; 
	}
	
	private String mathBrackets(String s) {
		String bracketsS = ""; 
		int point1 = 0;
		int point2 = 0;
		
		for (int i = 0; i < s.length(); i ++) {
			if (s.charAt(i) == '(') {
				point1 = i;
			}
			if (s.charAt(i) == ')') {
				point2 = i;
				break;
			}
		}
		
		bracketsS = s.substring(point1 + 1, point2);
		Calculate(bracketsS);
		s = s.replace(s.substring(point1, point2 + 1), String.valueOf(tempResult));
		return s;
	}

	private String mathOne(String s) { // To do "X" and "/" first
		String newString = "";
		String temp1 = "";
		String temp2 = ""; //
		char operation = 0; // store operation per loop
		double num = 0; // keep track of result per loop

		// ----Break String for info------
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'x' || s.charAt(i) == '/') {
				temp1 = s.substring(0, i);// Store leftside of operation
				temp2 = s.substring(i + 1, s.length());// Store rightside of operation
				operation = s.charAt(i);// Store specific operation for current needed operation
				break;// no need to continue looping after all info has been stored
			}
		}

		// --- Gather left and right side numbers----
		for (int i = temp1.length() - 1; i >= 0; i--) { // leftside - checking backwards to get the number left of
														// operation
			if (temp1.charAt(i) == '+' || temp1.charAt(i) == '-' || temp1.charAt(i) == 'x' || temp1.charAt(i) == '/') {
				temp1 = temp1.substring(i + 1, temp1.length());// gets the number
				break;
			}
		}

		for (int i = 0; i < temp2.length(); i++) { // right side - checking forward to get the number right of the
													// operation
			if (temp2.charAt(i) == '+' || temp2.charAt(i) == '-' || temp2.charAt(i) == 'x' || temp2.charAt(i) == '/') {
				temp2 = temp2.substring(0, i);// gets the number
				break;
			}
		}

		// ----Calculate the operation and create new String------
		num = doOperation(Double.parseDouble(temp1), Double.parseDouble(temp2), operation); // Do operation Ex: 4 x 5
		newString = temp1 + operation + temp2; // Create String with the operation that has just been done
		s = s.replaceFirst(newString, Double.toString(num)); // replace the string (operation thats been done) with
		// its corresponding result
		System.out.println(s); // track for debugging
		return s; // return the new "S" string
	}

	private void mathTwo(String s) { // To do remaining calculations "+" and "-"
		double num1 = 0;
		double num2 = 0;
		char operation = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == 'x' || s.charAt(i) == '/') {// check if
																										// operation
																										// exists to be
																										// done
				String string = new String(); // Create String for temp use

				// ----------Do operation----------------- Ex: 4 + 5
				string = s.substring(0, i);// gets 4
				num1 = Double.parseDouble(string); // converts 4 to double
				operation = s.charAt(i); // gets operation, "+" in this case
				s = s.substring(i + 1, s.length()); // cuts the original string "4 + 5", becomes "5"
				num2 = getNumber(s);// Finds the number second number from the left String, will return 5
				finalNum = doOperation(num1, num2, operation); // calculate 4 + 5, will return 9

				// --------Set New String--------
				s = getNewString(s); // rebuild a new string to check, return 9 in this case
				System.out.println("New String " + s); // track for debugging
				i = 0;
			} else if (i >= s.length() - 1) {
				result = finalNum; // if no operation is found, the result is the final Number that was from the
									// previous/last operation
			}
		}
	}

	private String getNewString(String s) { // rebuild a new string, Ex: 40 + 5 + 6 = 45 + 6
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == 'x' || s.charAt(i) == '/') {
				s = finalNum + s.substring(i, s.length());
				break;
			} else if (i >= s.length() - 1) { // if there is no operation found, the last result from an operation is
												// returned
				s = Double.toString(finalNum);
			}
		}

		return s;
	}

	private double getNumber(String str) {// For use in Math() method, used to find the second needed number for the
											// DoOperation() method
		double num = 0;// return variable

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '+' || str.charAt(i) == '-' || str.charAt(i) == 'x' || str.charAt(i) == '/') {
				String string = str.substring(0, i);
				num = Double.parseDouble(string);
				break;
			} else if (i >= str.length() - 1) {// After checking every position "i", if no operation is found means
												// only a number is left, return that number
				num = Double.parseDouble(str);
			}
		}
		return num;
	}

	private double doOperation(double num1, double num2, char operation) { // does operation given a 2 numbers and
																			// operation sign, returns result
		double n = 0;
		if (operation == '+') {
			n = num1 + num2;
		}
		if (operation == '-') {
			n = num1 - num2;

		}
		if (operation == 'x') {
			n = num1 * num2;

		}
		if (operation == '/') {
			n = num1 / num2;

		}
		return n;
	}
}