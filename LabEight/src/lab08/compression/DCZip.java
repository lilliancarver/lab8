package lab08.compression;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
	 * Compress the specified text file into a binary file with the same name with
	 * the extension ".dczip" appended to it.
	 * 
	 * The compressed file should begin with an integer indicating the number of
	 * words in the key. This integer should be followed by each of the words in the
	 * key, in the order they will be used to generate the codes for the words (i.e.
	 * from most frequent to least frequent). The words of the key will be followed
	 * by the codes for each word in the text as byte or short values depending upon
	 * the word. Byte values are positive and short values are negative, so that
	 * they can be differentiated. New lines in the text are indicated by a code
	 * value 0.
	 * 
	 * @param inFile
	 *            the path to the file to be compressed.
	 */
	public static void compress(String inFile) {
		System.out.println("Compressing: " + inFile + " into " + inFile + ".dczip");

		WordList wl = createWordList(inFile);
		writeBinaryFile(wl, inFile);

		System.out.println("Done.");
	}

	public static WordList createWordList(String inFile) {
		Scanner scr = null;
		WordList wl = new WordList();

		try {
			scr = new Scanner(new FileInputStream(inFile));

			while (scr.hasNextLine()) {
				String line = scr.nextLine();
				String[] words = line.split(" ", -1);

				for (int i = 0; i < words.length; i++) {
					String hold = words[i];
					wl.addWord(hold);
				}
			}
			wl.sortByFrequency();
			return wl;

		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file");
			return null;
		} finally {
			if (scr != null) {
				scr.close();
			}
		}
	}

	private static void writeBinaryFile(WordList wl, String inFile) {
		DataOutputStream dos = null;
		Scanner scr = null;

		try {
			scr = new Scanner(new FileInputStream(inFile));
			dos = new DataOutputStream(new FileOutputStream((inFile + ".dczip"), false));

			dos.writeInt(wl.size());

			for (int i = 0; i < wl.size(); i++) {
				dos.writeUTF(wl.getWord(i));
			}

			while (scr.hasNextLine()) {
				String line = scr.nextLine();
				String[] words = line.split(" ", -1);

				for (int i = 0; i < words.length; i++) {
					String hold = words[i];
					int index = wl.getIndex(hold);
					if (index < 127) {
						int code = index + 1;
						dos.writeByte(code);
					} else {
						int code = index + 1;
						int shortCode = (0 - code) ;
						dos.writeShort(shortCode);
					}
					if (scr.hasNextLine()) {
						dos.writeByte(0);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file");
		} catch (IOException e) {
			System.out.println("Error reading file");
		} finally {
			if (scr != null) {
				scr.close();
			}
		}
	}

	/**
	 * Expand the specified compressed binary file back into a text file with the
	 * specified name.
	 * 
	 * @param inFile
	 *            the path to the compressed binary file to be expanded.
	 * @param outFile
	 *            the path for the expanded text file.
	 */
	public static void expand(String inFile, String outFile) {
		System.out.println("Expanding: " + inFile + " into " + outFile);

		String[] words = readToArray(inFile);
		writeText(inFile, outFile, words);

		System.out.println("Done.");
	}

	public static String[] readToArray(String inFile) {
		DataInputStream dis = null;

		try {
			dis = new DataInputStream(new FileInputStream(inFile));

			int numWords = dis.readInt();
			String[] words = new String[numWords];

			for (int i = 0; i < numWords; i++) {
				String hold = dis.readUTF();
				words[i] = hold;
			}

			return words;

		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file: " + inFile);
			return null;
		} catch (IOException e) {
			System.out.println("Error reading file: " + inFile);
			return null;
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					System.out.println("Error closing file: " + inFile);
				}
			}
		}
	}

	public static void writeText(String inFile, String outFile, String[] words) {
		PrintWriter pw = null;
		DataInputStream dis = null;

		try {
			dis = new DataInputStream(new FileInputStream(inFile));
			pw = new PrintWriter(new FileOutputStream(outFile, false));

			int numWords = dis.readInt();
			System.err.println("num words: " + numWords);
			dis.skipBytes(numWords * 7 - 2);

			while (dis.available() > 0) {
				byte code = dis.readByte();
				if (code < 0) {
					int shortCode = 0 - makeShort(code, dis.readByte());
					String word = words[shortCode - 1];
					pw.write(word);
					pw.write(" ");
				} else if (code > 0) {
					int intCode = code;
					String word = words[intCode - 1];
					pw.write(word);
					pw.write(" ");
				} else {
					pw.write("\n");
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open the file: " + inFile);
		} catch (IOException e) {
			System.out.println("Error reading file: " + inFile);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
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
	 * @param args
	 *            none
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
