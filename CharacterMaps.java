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
		System.out.println(code_page);
	}

	/*
	 * Read in screenfont.h
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
	 * 
	 */
}