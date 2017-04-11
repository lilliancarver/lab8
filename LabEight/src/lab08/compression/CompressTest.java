package lab08.compression;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompressTest {

	private static final String FILE = "src/comp132/labs/lab08/compression/documents/";
	private static final String FILE_HBD = FILE + "hbd";
	private static final String IN_FILE_HBD = FILE_HBD + ".txt";
	private static final String COMPRESSED_HBD = IN_FILE_HBD + ".dczip";

	private static final String FILE_TST = FILE + "tst";
	private static final String IN_FILE_TST = FILE_TST + ".txt";
	private static final String COMPRESSED_TST = IN_FILE_TST + ".dczip";

	private DataInputStream disHBD;
	private DataInputStream disTST;

	@BeforeClass
	public static void setUpClass() throws FileNotFoundException {
		// create new files in case the given one has been corrupted.

		PrintWriter pw = new PrintWriter(new FileOutputStream(IN_FILE_HBD));
		pw.println("Happy birthday to you, Happy birthday to you,");
		pw.print("Happy birthday dear Susie, Happy birthday to you.");
		pw.close();

		DCZip.compress(IN_FILE_HBD);

		new File(IN_FILE_HBD).deleteOnExit();
		new File(COMPRESSED_HBD).deleteOnExit();

		PrintWriter pw2 = new PrintWriter(new FileOutputStream(IN_FILE_TST));
		pw2.println("the line after this one has no words at all");
		pw2.println();
		pw2.println("this line has double  spaces  between  some  words");
		pw2.println("and this one has triple   spaces   between   other words");
		pw2.println("this line has one space at the end ");
		pw2.println("this line has two spaces at the end  ");
		pw2.println(" this line has one space before any words");
		pw2.println("  this line has two spaces before any words");
		pw2.close();

		DCZip.compress(IN_FILE_TST);

		new File(IN_FILE_TST).deleteOnExit();
		new File(COMPRESSED_TST).deleteOnExit();
	}

	@Before
	public void setUp() throws FileNotFoundException {
		disHBD = new DataInputStream(new FileInputStream(COMPRESSED_HBD));
		disTST = new DataInputStream(new FileInputStream(COMPRESSED_TST));
	}

	@After
	public void tearDown() throws IOException {
		disHBD.close();
	}

	@Test
	public void testNumWords() throws IOException {
		int words = disHBD.readInt();
		assertEquals("Integer at start of compressed file indicates an incorrect number of words.", 7, words);
	}

	@Test
	public void testWordsInKey() throws IOException {
		String[] checkWords = new String[] { "Happy", "birthday", "to", "you,", "dear", "Susie,", "you." };

		disHBD.readInt(); // discard number of words.
		for (int i = 0; i < 7; i++) {
			String word = disHBD.readUTF();
			assertEquals("Incorrect word in key at start of compressed happybirthday file.", checkWords[i], word);
		}
	}

	@Test
	public void testFirstWordCode() throws IOException {
		disHBD.readInt(); // discard number of words
		for (int i = 0; i < 7; i++) {
			disHBD.readUTF(); // discard key words.
		}
		byte first = disHBD.readByte();
		assertEquals("Incorrect code for first word in happybirthday.", 1, first);
	}

	@Test
	public void testFirstLineCodes() throws IOException {
		disHBD.readInt(); // discard number of words
		for (int i = 0; i < 7; i++) {
			disHBD.readUTF(); // discard key words.
		}

		int[] checkBytes = new int[] { 1, 2, 3, 4, 1, 2, 3, 4 };
		for (int i = 0; i < 8; i++) {
			byte code = disHBD.readByte();
			assertEquals("Incorrect code for word " + i + " in first line of happybirthday.", checkBytes[i], code);
		}
	}

	@Test
	public void testNewLineAfterFirstLine() throws IOException {
		disHBD.readInt(); // discard number of words
		for (int i = 0; i < 7; i++) {
			disHBD.readUTF(); // discard key words.
		}
		for (int i = 0; i < 8; i++) {
			disHBD.readByte(); // discard first line of words.
		}

		byte code = disHBD.readByte();
		assertEquals("Incorrect newline code at end of first line of happybirthday.", 0, code);
	}

	@Test
	public void testSecondLineCodes() throws IOException {
		disHBD.readInt(); // discard number of words
		for (int i = 0; i < 7; i++) {
			disHBD.readUTF(); // discard key words.
		}
		for (int i = 0; i < 8; i++) {
			disHBD.readByte(); // discard first line of words.
		}
		disHBD.readByte(); // discard the new line code.

		int[] checkBytes = new int[] { 1, 2, 5, 6, 1, 2, 3, 7 };
		for (int i = 0; i < 8; i++) {
			byte code = disHBD.readByte();
			assertEquals("Incorrect code for word " + i + " in second line of happybirthday.", checkBytes[i], code);
		}
	}

	@Test
	public void testBlankLine() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 10; i++) {
			disTST.readByte(); // discard words on first line.
		}
		byte nl1 = disTST.readByte();
		assertEquals("No new line code at end of first line of testfile.", 0, nl1);

		byte nl2 = disTST.readByte();
		assertEquals("Blank line should have \"\" code in testfile.", 1, nl2);

		byte nl3 = disTST.readByte();
		assertEquals("Should be new line code at end of blank line in testfile.", 0, nl3);
	}
	
	@Test
	public void testDoubleSpaces() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 17; i++) {
			disTST.readByte(); // discard words up to double space.
		}
		byte sp1 = disTST.readByte();
		assertEquals("Double space should be a \"\" code in test file.", 1, sp1);
	}
	
	@Test
	public void testTripleSpaces() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 31; i++) {
			disTST.readByte(); // discard words up to triple space.
		}
		byte sp1 = disTST.readByte();
		assertEquals("Triple space missing a \"\" code in test file.", 1, sp1);
		byte sp2 = disTST.readByte();
		assertEquals("Triple space missing a \"\" code in test file.", 1, sp2);
	}
	
	@Test
	public void testOneSpaceAtEndOfLine() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 50; i++) {
			disTST.readByte(); // discard words up to space at end of line.
		}	
		byte sp1 = disTST.readByte();
		assertEquals("Missing a \"\" code with space at end of line in test file.", 1, sp1);
	}
	
	@Test
	public void testTwoSpacesAtEndOfLine() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 60; i++) {
			disTST.readByte(); // discard words up to double space at end of line.
		}	
		byte sp1 = disTST.readByte();
		assertEquals("Missing first \"\" code with double space at end of line in test file.", 1, sp1);
		
		byte sp2 = disTST.readByte();
		assertEquals("Missing second \"\" code with double space at end of line in test file.", 1, sp2);
	}
	
	@Test
	public void testOneSpaceAtStartOfLine() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 63; i++) {
			disTST.readByte(); // discard words up to double space at end of line.
		}	
		byte sp1 = disTST.readByte();
		assertEquals("Missing \"\" code with space at start of line in test file.", 1, sp1);
	}
	
	@Test
	public void testDoubleSpaceAtStartOfLine() throws IOException {
		int size = disTST.readInt(); // number of words in key
		for (int i = 0; i < size; i++) {
			disTST.readUTF(); // discard key
		}
		for (int i = 0; i < 73; i++) {
			disTST.readByte(); // discard words up to double space at end of line.
		}	
		byte sp1 = disTST.readByte();
		assertEquals("Missing first \"\" code with space at start of line in test file.", 1, sp1);
		
		byte sp2 = disTST.readByte();
		assertEquals("Missing second \"\" code with space at start of line in test file.", 1, sp2);
	}
}
