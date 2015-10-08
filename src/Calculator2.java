import java.util.Scanner;
/**
 * 
 * @author Johan Johansson
 * @author Mattias Landkvist
 * 
 */
public class Calculator2 {

	public static void main(String[] args) {
		String infix, postfix, result, input;

		Scanner scan = new Scanner(System.in);
		System.out.println("+***************************************+");
		System.out.println("*                                       *");
		System.out.println("*      Welcome to DIT948 Calculator     *");
		System.out.println("*                                       *");
		System.out.println("+***************************************+");
		System.out.println();
		while (true) {
			System.out.print("Press \"E\" to exit or any other button to continue> ");
			input = scan.next(); // Scan from inputstream
			input = input.toLowerCase();
			if (!input.equals("e")) {	// If input isn't 'e', continue with program.
				System.out.println();
				System.out.print("Please enter an arithmetic expression to evaluate> ");
				infix = checkInput(scan.next());

				if (infix == "e") {		// If a broken arithmetic expression was given. Proceed to next iteration of the program.
					System.out.println();
					System.out.println("You can only enter expressions represented with ()1234567890+-*/");
					System.out.println("Don't be stupid!");
					System.out.println();
					continue;
				} else {		// Main subroutine calls and outputs.
					postfix = infixToPostfix(infix);	// Converts infix to postfix notation and saves in the postfix variable.
					System.out.println();
					System.out.print("The RPN representation of your expression is> ");
					System.out.println(postfix.replaceAll("\\s", ""));	// Output postfix variable without whitespace.
					System.out.println();

					result = computePostfix(postfix);	// Compute postfix variable and save to the result variable.
					if (result == "e") {	// Expression involves division with zero.
						System.out.println("Black hole creation avoided, please do not try to divide by zero!");
						System.out.println();

						continue;
					} else if (result == "er") {	// Error due to illegal declaration of a negative number. Only supported with ex (1-6) --> 16- --> -5.
						System.out.println("Declare negative numbers with at least two integers, ex (1-5) . Don't input (-1)");
						System.out.println();

						continue;
					} else {	// The final result.
						System.out.print("The final result is> ");
						System.out.println(result);
						System.out.println();

					}
				}

			} else {	// You wrote 'E' or 'e' in the beginning. That means that the program terminates.
				System.out.println();
				System.out.println("Bye Bye");
				System.exit(0);
				break;
			}
		}
		scan.close();	// Close scanner to avoid unnecessary warnings and potential problems.
	}

	/**
	 * Converts a string, namely an arithmetic expression, into postfix
	 * @param input string
	 * @return a string in postfix notation
	 */
	public static String infixToPostfix(String input) {
		String tmp, output;
		output = "";
		tmp = "";

		for (int i = 0; i < input.length(); i++) { 	// For-loop over the length of input.

			if (Character.isDigit(input.charAt(i))) { 	// First condition. Updates the output variable with digits and whitespace prior to operators. Takes any digits.
				output += input.charAt(i);
				for (int j = i + 1; j < input.length(); j++) { // Nested for-loop magic. The brain of multi digit handling.

					if (Character.isDigit(input.charAt(j))) { // Updates the output variable with "the next" digit and increment i in the utmost for-loop.
						output += input.charAt(j);
						i++;
					} else if (input.charAt(j) == '(' || input.charAt(j) == ')') { // Bypass ( and ).
						break;
					} else { // Updates the output variable with a whitespace prior to operators.
						output += " ";
						break;
					}
				} // End of nested for-loop.

			} else if (input.charAt(i) == '(') { // Push the left parenthesis at 'i' in input to the variable tmp.
				tmp = push(input.charAt(i), tmp);

			} else if (input.charAt(i) == ')') { // In case of right parenthesis at 'i' of input.

				{
					while (true) {
						if (tmp.isEmpty()) { // Tmp is empty. Do nothing.
							break;

						} else if (tmp.charAt(0) == '(') { // If the first character in tmp is a right parenthesis, pop it.
							tmp = pop(tmp);
							break;

						} else if (tmp.charAt(0) != '(') { // If it isn't, update the output variable with the first character in tmp. Then pop tmp.
							output += tmp.charAt(0);
							tmp = pop(tmp);
						}
					} // End of while-loop.
				}

			} else { // If 'i' of input is an operator.
				while (true) {
					// If tmp is empty, the first character in tmp is a left parenthesis or the priority of the first character in tmp
					// is lower than the priority of 'i' in input. Push 'i' in input to tmp.
					if (tmp.isEmpty() || tmp.charAt(0) == '(' || priority(tmp.charAt(0)) < priority(input.charAt(i))) {

						tmp = push(input.charAt(i), tmp);
						break;

					} else { // Update output with the first character in tmp.
						output += (tmp.charAt(0));
					}
					tmp = pop(tmp);
				} // End of while-loop.
			}
		}

		output += tmp;
		return output; // Return string in postfix notation.
	}

	/**
	 * Computes an arithmetic expression in postfix notation
	 * @return the computation of the input string.
	 */
	public static String computePostfix(String input) {
		String A = "";
		String B = "";
		String C = "";
		String tmp = "";


		for (int i = 0; i < input.length(); i++) {// For-loop over the length of input.
System.out.println("A:" + A + " B:" + B + " C:" + C);
			if (Character.isDigit(input.charAt(i))) { // When digit. Update B with 'i' of input.
//				if(!input.contains(" ")){
//					return input;	// If there is no whitespace. Return input.
//				}
				B += input.charAt(i) + "";
				int j = i + 1;
				if (input.length() > B.length() && input.charAt(j) == ' ') { // Swap if next character is a whitespace .
					tmp = A;
					A = C;
					C = tmp;
				}
			}else{
				switch (input.charAt(i)) {
				case ('+'):
					if (!B.isEmpty()) {	// If B isn't empty A+B
						A = (Integer.parseInt(A) + Integer.parseInt(B)) + "";
						B = "";

					}else{	// Else A+C
						A = (Integer.parseInt(C) + Integer.parseInt(A)) + "";
						B = "";
						C = "";
					}
				break;
				case ('-'):
					if (!B.isEmpty()) { 	// If B isn't empty. Check A.
						if (!A.isEmpty()) { // If A isn't empty A-B
							A = (Integer.parseInt(A) - Integer.parseInt(B)) + "";
							B = "";
						} else { // Else input is illegal. ex (-1)
							return "er";
						}
					} else { // Else A-C.

						A = (Integer.parseInt(C) - Integer.parseInt(A)) + "";
						B = "";
						C = "";

					}
				break;
				case ('*'):
					if (!B.isEmpty()) {	// If A isn't empty A*B
						A = (Integer.parseInt(A) * Integer.parseInt(B)) + "";
						B = "";
					} else {	// Else A*C
						A = (Integer.parseInt(C) * Integer.parseInt(A)) + "";
						B = "";
						C = "";
					}
				break;
				case ('/'):
					if (!B.isEmpty()) {
						if (!A.isEmpty()) {	// If B isn't empty and the first character in B isn't '0'.  A/B
							A = (Integer.parseInt(A) / Integer.parseInt(B)) + "";
							B = "";
						} else {
							if (A.charAt(0) == '0' && C.charAt(0) == '0') { // In case of 0/0
								return "0";
							} else {	// In case of A/0
								return "e";
							}
						}

					} else {
						if (A.charAt(0) != '0') { // Else, as long as the first character in A isn't '0'.  A/B
							A = (Integer.parseInt(C) / Integer.parseInt(A)) + "";
							B = "";
							C = "";
						} else {
							return "e";
						}
					}
				break;
				default:	// Swap if next character is a whitespace when A == "".
					tmp = B;
					B = A;
					A = tmp;
				}
			}
		}
		return A;
	}

	/**
	 * Checks the priority of an operator
	 * @param c character
	 * @return the priority as an int
	 */
	public static int priority(char c) {
		if (c == '+' || c == '-') {
			return 0;
		} else if (c == '*' || c == '/') {
			return 1;
		} else {
			return -1;
		}
	}


	/**
	 * Pop the first element of the string
	 * @param s string to pop
	 * @return a substring of the input starting at 1
	 */
	public static String pop(String s) {
		return s.substring(1);
	}

	/**
	 * Appends a character to the start of a string
	 * @param c character
	 * @param s string
	 * @return the new string
	 */
	public static String push(char c, String s) {
		return c + s;
	}

	/**
	 * Check if c is an operator
	 * @param c character as operator
	 * @return true if c is an operator
	 */
	public static boolean isOperator(char c){
		if(c == '+' || c == '-' || c == '*' || c == '/'){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the input contains forbidden characters.
	 * @param input string in checkInput
	 * @return a correct arithmetic expression as a string or "e" for error
	 */
	public static String checkInput(String input) {
		input = input.replaceAll("\\s", ""); // Removes all whitespaces from the input
		if(isOperator(input.charAt(0))){
			return "e";
		}
		for (int i = 1; i < input.length(); i++) { 
			int j = i-1;
			if(isOperator(input.charAt(i))){ 	// If 'i' in input is an operator. Continue unless previous character is empty, 'i' is the last character of the string,
												//		the next character is an operator or the previous character is a left parenthesis.
				if((input.charAt(j) + "").isEmpty() || input.endsWith(input.charAt(i) + "")   || isOperator(input.charAt(j)) || input.charAt(j) == '('){
					return "e";		// "e" for Error.
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return input;
	}
}


