package lab08.compression;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A list of WordIntPairs and some operations that facilitate text compression.
 * As WordIntPairs are added to the list, the int value in the WordIntPair is
 * used to maintain a frequency count, which indicates the number of times a
 * word has been added to the list. The list can then be sorted such that the
 * most frequent words appear that the beginning of the list. This will form the
 * basis for finding the codes that will be used to compress the text file.
 * 
 * @author Grant Braught
 * @author Dickinson College
 * @version Nov 5, 2016
 */
public class WordList {

	private ArrayList<WordIntPair> wordList;

	/**
	 * Construct a new empty WordList.
	 */
	public WordList() {
		wordList = new ArrayList<WordIntPair>();
	}

	/**
	 * Get the number of words in the list.
	 *
	 * @return the number of words in the list.
	 */
	public int size() {
		return wordList.size();
	}

	/**
	 * Add a word to the list. If the word does not exist, it should be added to the
	 * list with a count of 1. If the word already exists in the list, then the
	 * count for the existing word should be increased.
	 * 
	 * @param word
	 *            the word to be added to the list.
	 */
	public void addWord(String word) {

		try {
			WordIntPair hold = wordList.get(getIndex(word));
			hold.setValue(hold.getIntValue() + 1);
		} catch (IllegalArgumentException e) {
			WordIntPair newWord = new WordIntPair(word);
			wordList.add(newWord);
		}
	}

	/**
	 * Sort list of words such that those with the largest frequency counts appear
	 * at the start of the list.
	 */
	public void sortByFrequency() {
		Collections.sort(wordList);
	}

	/**
	 * Get the index of the given word in the list. When done after the list is
	 * sorted the index of more frequent words will have smaller indices (that we
	 * will be able to represent by a byte). Less frequent words will have larger
	 * indices (that may requires a short).
	 * 
	 * @param word
	 *            the word for which to find the index.
	 * @return the index at which the word appears in the list.
	 * @throws IllegalArgumentException
	 *             if word does not appear in the list.
	 */
	public int getIndex(String word) {

		for (int i = 0; i < wordList.size(); i++) {
			WordIntPair hold = wordList.get(i);
			if ((hold.getWord()).equals(word)) {
				return i;
			}
		}

		throw new IllegalArgumentException();

	}

	/**
	 * Get the word that appears at the specified index.
	 * 
	 * @param index
	 *            the index
	 * @return the word that appears at the specified index in the list.
	 */
	public String getWord(int index) {
		WordIntPair found = wordList.get(index);
		return found.getWord();
	}
}