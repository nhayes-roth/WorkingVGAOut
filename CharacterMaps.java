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
		String[] string_array = stringToArray(code_page);
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
	private static String[] stringToArray(String code_page){
		String [] rows 		= new String [8];				// array to be returned
		String row 			= "";							// string of each row
		String row_delim 	= " { \n  ";					// deliminates the start of a new row
		int start_index 	= code_page.indexOf(row_delim);	// 
		int end_index 		= start_index;
		for (int i = 0; i < 8; i++){
			end_index = code_page.indexOf("\n { ", start_index+1);
			if (end_index == -1){
				row = code_page.substring(start_index + 7, code_page.length()-37);
			} else {
				row = code_page.substring(start_index + 7, end_index - 17);
			}
			System.out.println("Row " + (i+1) + ": " + row.substring(0,5) + "..." + row.substring(row.length()-6));
			rows[i] = row;
			start_index = end_index;
		}
		return rows;

	}
}