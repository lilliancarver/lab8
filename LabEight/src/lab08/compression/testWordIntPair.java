package lab08.compression;

import static org.junit.Assert.*;

import org.junit.Test;

public class testWordIntPair {

	@Test
	public void testConstructor() {
		WordIntPair hold = new WordIntPair("Hello");
		assertEquals("Hello", hold.getWord());
		assertEquals(1, hold.getIntValue());
	}

	@Test
	public void testSet() {
		WordIntPair hold = new WordIntPair("Hello");
		hold.setValue(3);
		assertEquals(3, hold.getIntValue());
	}

	@Test
	public void testCompareTo() {
		WordIntPair hold = new WordIntPair("Hello");
		WordIntPair toCompare = new WordIntPair("World");
		
		// return 1;
		toCompare.setValue(3);
		assertEquals(1, hold.compareTo(toCompare));
		
		// return -1;
		hold.setValue(3);
		toCompare.setValue(1);
		assertEquals(-1, hold.compareTo(toCompare));
				
		// return 0;
		toCompare.setValue(3);
		assertEquals(0, hold.compareTo(toCompare));
	}
	
}