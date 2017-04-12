package lab08.compression;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Perform compression and expansion of text files.
 * 
 * @author Grant Braught
 * @author Dickinson College
 * @version Nov 5, 2016
 */
public class DCZip {

	/**
	 * Compress the specified text file into a binary file with the same name
	 * with the extension ".dczip" appended to it.
	 * 
	 * The compressed file should begin with an integer indicating the number of
	 * words in the key. This integer should be followed by each of the words in
	 * the key, in the order they will be used to generate the codes for the
	 * words (i.e. from most frequent to least frequent). The words of the key
	 * will be followed by the codes for each word in the text as byte or short
	 * values depending upon the word. Byte values are positive and short values
	 * are negative, so that they can be differentiated. New lines in the text
	 * are indicated by a code value 0.
	 * 
	 * @param inFile
	 *            the path to the file to be compressed.
	 */
	public static void compress(String inFile) {
		System.out.println("Compressing: " + inFile + " into " + inFile + ".dczip");

		// Your code here...
		
		System.out.println("Done.");
	}

	/**
	 * Expand the specified compressed binary file back into a text file with
	 * the specified name.
	 * 
	 * @param inFile
	 *            the path to the compressed binary file to be expanded.
	 * @param outFile
	 *            the path for the expanded text file.
	 */
	public static void expand(String inFile, String outFile) {
		System.out.println("Expanding: " + inFile + " into " + outFile);

		// Your code here...
		
		System.out.println("Done.");
	}

	/**
	 * Turn two bytes into a short. For example:
	 * 
	 * <pre>
	 * if b1 = 0011 1101 and b2 = 1011 0110
	 * then the short value returned will be
	 * 0011 1101 1011 0110
	 * </pre>
	 * 
	 * @param b1
	 *            the most significant byte.
	 * @param b2
	 *            the least significant byte.
	 * @return
	 */
	private static short makeShort(byte b1, byte b2) {
		short s1 = (short) ((0x00FF & b1) << 8);
		short s2 = (short) (0x00FF & b2);

		short val = (short) (s1 | s2);

		return val;
	}

	/**
	 * Process commands entered by the user for compressing or expanding files.
	 * 
	 * @param args none
	 */
	public static void main(String[] args) {
		String path = "src/lab08/compression/documents/";

		Scanner scr = new Scanner(System.in);

		System.out.print("Enter Command: ");
		String cmd = scr.nextLine();
		String[] tokens = cmd.split(" ");

		if (tokens[0].equals("compress") && tokens.length == 2) {
			compress(path + tokens[1]);
		} else if (tokens[0].equals("expand") && tokens.length == 3) {
			expand(path + tokens[1], path + tokens[2]);
		} else {
			System.out.println("Unrecognized command: " + cmd);
		}

		scr.close();
	}
}
