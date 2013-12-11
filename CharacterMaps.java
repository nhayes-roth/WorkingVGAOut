/*
 * File:	CharacterMaps.java
 * ---------------------------
 * Author:	Nathan Hayes-Roth
 * ---------------------------
 * Transforms the bit mapping for CP437 into several new character maps:
 * 		- inverted
 *		- reflected
 */

import java.util.*;
import java.io.*;

class CharacterMaps {

	/* Class Variables */
	private static String file_name = "screenfont.h";

	/* Main Function */
	public static void main (String[] args) throws IOException{
		String code_page = readScreenFont(file_name);
		String[][] string_array = stringToArray(code_page);
		print(string_array, 0);
	}

	/*
	 * Prints the byte of the given index.
	 */
	private static void print(String[][] string_array, int index) {
		System.out.println("--- Byte " + index + " ---");
		for (int i = 0; i < 8; i++){
			System.out.println("Row:" + (i+1) + " " + string_array[i][index]);
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
		String byte_delim	= ", ";							// byte separator
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
			split_row = row.split(byte_delim);
			rows[i] = split_row;
			start_index = end_index;
		}
		return rows;

	}
}