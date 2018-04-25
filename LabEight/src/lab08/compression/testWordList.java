package lab08.compression;

import static org.junit.Assert.*;

import org.junit.Test;

public class testWordList {

	@Test
	public void testConstructor() {
		WordList wl1 = new WordList();
		assertEquals(0, wl1.size());
	}
	
	@Test
	public void testAddWord() {
		
		WordList wl1 = new WordList();
		wl1.addWord("Hello");
		wl1.addWord("Hello");
		wl1.addWord("Hola");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		
		assertEquals(3, wl1.size());
		
	}
	
	@Test
	public void testGetIndexUnsorted() {
		WordList wl1 = new WordList();
		wl1.addWord("Hello");
		wl1.addWord("Hello");
		wl1.addWord("Hola");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		
		assertEquals(0, wl1.getIndex("Hello"));
		assertEquals(1, wl1.getIndex("Hola"));
		assertEquals(2, wl1.getIndex("Ciao"));
	}
	
	@Test
	public void testGetIndexSorted() {
		WordList wl1 = new WordList();
		wl1.addWord("Hello");
		wl1.addWord("Hello");
		wl1.addWord("Hola");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		
		wl1.sortByFrequency();
		
		assertEquals(0, wl1.getIndex("Ciao"));
		assertEquals(1, wl1.getIndex("Hello"));
		assertEquals(2, wl1.getIndex("Hola"));
	}
	
	@Test
	public void testGetWordUnsorted() {
		WordList wl1 = new WordList();
		wl1.addWord("Hello");
		wl1.addWord("Hello");
		wl1.addWord("Hola");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		
		assertEquals("Hello", wl1.getWord(0));
		assertEquals("Ciao", wl1.getWord(2));
		
		wl1.sortByFrequency();
		
		assertEquals("Ciao", wl1.getWord(0));
		assertEquals("Hola", wl1.getWord(2));
	}
	
	@Test
	public void testGetWordSorted() {
		WordList wl1 = new WordList();
		wl1.addWord("Hello");
		wl1.addWord("Hello");
		wl1.addWord("Hola");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		wl1.addWord("Ciao");
		
		wl1.sortByFrequency();
		
		assertEquals("Ciao", wl1.getWord(0));
		assertEquals("Hola", wl1.getWord(2));
	}

}
