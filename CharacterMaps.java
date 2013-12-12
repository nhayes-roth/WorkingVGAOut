/*
 * File:	CharacterMaps.java
 * ---------------------------
 * Author:	Nathan Hayes-Roth
 * ---------------------------
 * Transforms the bit mapping for CP437 into several new character maps:
 * 		- inverted		- flip rows (1/8, 2/7, 3/6, 4/5)
 *		- reflected		- 
 *		- highlighted	- 
 */

import java.util.*;
import java.io.*;
import java.lang.Byte;

class CharacterMaps {

	/* Main Function */
	public static void main (String[] args) throws IOException{
		String code_page = readScreenFont("screenfont.h");
		String[][] normal = stringToArray(code_page);
		String[][] inverted = invert(normal);
		Byte[][] bytes = stringToBytes(normal);
		Byte[][] highlighted = highlight(bytes);
		String[][] reversed = reverse(bytes);
		

		writeToFile(normal, "normal", "normal.h");
		writeToFile(inverted, "inverted", "inverted.h");
		writeToFile(highlighted, "highlighted", "highlighted.h");
		writeToFile(reversed, "reversed", "reversed.h");
		
		
		
//		print(normal,42);
//		print(inverted,42);
		
//		System.out.println(normal[0][0]);
//		byte b = (byte)(Integer.parseInt(normal[0][0].substring(2), 16) & 0xff);
//		System.out.printf("0x%02X", b);
	}
	
	/*
	 * Reverse the bits within a byte and return the hex String version.
	 */
	private static String[][] reverse(Byte[][] bytes){
		String[][] result = new String[8][256];
		for (int i=0; i<bytes.length; i++){
			for (int j=0; j<bytes[0].length; j++){
				String str = Integer.toBinaryString(bytes[i][j]);
				str = str.substring(str.length()-8);
				String reversed_binary = new StringBuilder(str).reverse().toString();
				String reversed_hex = String.format("0x%S", Integer.toHexString(Integer.parseInt(reversed_binary, 2)));
				result[i][j] = reversed_hex;
				
			}
		}
		return result;
	}
	
	/*
	 * Negates all the bits in a byte array to, hopefully, create a highlighted effect.
	 */
	private static Byte[][] highlight(Byte[][] bytes) {
		Byte[][] highlighted = new Byte[8][256];
		for (int i=0; i<bytes.length; i++){
			for (int j=0; j<bytes[0].length; j++){
				highlighted[i][j] = (byte) ~bytes[i][j];
			}
		}
		return highlighted;
	}

	/*
	 * Converts the original 2d string array to a 2d byte array.
	 */
	private static Byte[][] stringToBytes(String[][] normal) {
		Byte[][] to_return = new Byte[normal.length][normal[0].length];
		for (int i=0; i<normal.length; i++){
			for (int j=0; j<normal[0].length; j++){
				byte b = (byte)(Integer.parseInt(normal[i][j].substring(2), 16) & 0xff);
				to_return[i][j] = b;
			}
		}
		return to_return;
	}

	/*
	 * Returns a String version of an array.
	 */
	private static String arrayToString(Object[][] arr, String title){
		StringBuilder sb = new StringBuilder();
		// add the title line
		sb.append("byte " + title + " [8] [256] PROGMEM = {\n");
		// add all the rows
		for (int row = 0; row < 8; row++){
			sb.append("// Row " + (row+1) + "\n");
			sb.append(" { ");
			for (int i = 0; i < 256; i++){
				if (i%16 == 0){
					sb.append("\n  ");
				}
				if (arr[0][0] instanceof String){
					sb.append(arr[row][i].toString());
				}
				else {
					sb.append(String.format("0x%02X", arr[row][i]));
				}
				sb.append(", ");
			}
		sb.append("\n  }, \n");
		}
		// add the closing lines
		sb.append("\n};");
		// return
		return sb.toString();
	}

	/*
	 * Writes the provided String[][] to a bit pattern file.
	 */
	private static void writeToFile(Object[][] array, String array_name, String file_name) {
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(file_name));
			out.print(arrayToString(array, array_name));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nError encountered writing to file.");
			System.exit(1);
		}
		
	}

	/*
	 * Inverts the rows of a 2d array.
	 */
	private static String[][] invert(String[][] string_array) {
		String[] temp = new String[256];
		String[][] inverted = new String[8][256];
		for (int i = 0; i < string_array.length/2; i++){
			temp = string_array[i];
			inverted[i] = string_array[string_array.length-1-i];
			inverted[string_array.length-1-i] = temp;
		}
		return inverted;
	}

	/*
	 * Prints the byte of the given index.
	 */
	private static void print(Object[][] array, int index) {
		System.out.println("--- Byte " + index + " ---");
		for (int i = 0; i < 8; i++){
			if (array[0][0] instanceof String){
				System.out.println("Row: " + (i+1) + " " + array[i][index]);	
			}
			else {
				System.out.println("Row: " + (i+1) + " " + String.format("0x%02X", array[i][index]));	
			}
		}
	}

	/*
	 * Read in contents of screenfont.h to String.
	 */
	private static String readScreenFont(String file_name) throws IOException{
		File file = new File(file_name);
		StringBuilder contents = new StringBuilder ((int)file.length());
		Scanner scanner = new Scanner(file);
		String line_separator = System.getProperty("line.separator");

		try {
			while(scanner.hasNextLine()) {
				contents.append(scanner.nextLine() + line_separator);
			}
			return contents.toString();
		} finally {
			scanner.close();
		}
	}

	/*
	 * Convert String to String[8][256].
	 */
	private static String[][] stringToArray(String code_page){
		String [][] rows 	= new String [8][256];			// array to be returned
		String row 			= "";							// string of each row
		String[] split_row	= new String[256];				// row split into array of bytes
		String row_delim 	= " { \n  ";					// deliminates the start of a new row
		String byte_delim	= ",";							// byte separator
		int start_index 	= code_page.indexOf(row_delim);	// start of row
		int end_index 		= start_index;					// end of row
		for (int i = 0; i < 8; i++){
			end_index = code_page.indexOf("\n { ", start_index+1);
			if (end_index == -1){
				row = code_page.substring(start_index + 7, code_page.length()-37);
			} else if (i == 0){
				row = code_page.substring(start_index + 6, end_index - 17);
			} else {
				row = code_page.substring(start_index + 7, end_index - 17);
			}
			row = row.replaceAll("\\s+", "");
			split_row = row.split(byte_delim);
			rows[i] = split_row;
			start_index = end_index;
		}
		return rows;

	}
}
