/*******************************************************************************
 *   Copyright 2018 Lukasz Budryk (https://github.com/lume115)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package at.lume.wordgen.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import at.lume.wordgen.lib.ast.Syllable;
import at.lume.wordgen.lib.ast.Syllable.SyllablePosition;
import at.lume.wordgen.lib.util.StringUtils;

/**
 * Word generator
 * @author Lukasz Budryk
 *
 */
public class WordGen {

	private Map<Syllable.SyllablePosition, List<Syllable>> syllables = new HashMap<>();

	private final long seed;
	
	private Random rnd;
	
	public WordGen() {
		this(System.currentTimeMillis());
	}
	
	public WordGen(final long seed) {
		this.seed = seed;
		this.rnd = new Random(seed);
	}
	
	public long getSeed() {
		return seed;
	}

	public void setSyllables(Map<Syllable.SyllablePosition, List<Syllable>> syllables) {
		this.syllables = syllables;
	}

	public Map<Syllable.SyllablePosition, List<Syllable>> getSyllables() {
		return syllables;
	}
	
	
	public String nextWord(final int minLength, final int maxLength) {
		return nextWord(minLength, maxLength, "");
	}
	
	/**
	 * generates the next random word
	 * @param minLength min length (syllables)
	 * @param maxLength max length (syllables)
	 * @return
	 */
	public String nextWord(final int minLength, final int maxLength, final String separator) {
		//System.out.println("new word");
		final List<String> currentWord = new ArrayList<>();
		String finalWord = "";
		Syllable prevSyllable = null;
		if (maxLength < minLength) return "";
		final int length = rnd.nextInt(maxLength-minLength+1)+minLength;
		final List<Syllable> syllableList = new ArrayList<>();
		
		final boolean hasAny = syllables.containsKey(SyllablePosition.ANY);
		
		boolean listModified = true;
		for (int i = 0; i < length; i++) {
			
			if (i == 0) {
				syllableList.clear();
				if (hasAny) {
					syllableList.addAll(syllables.get(SyllablePosition.ANY));
				}
				syllableList.addAll(syllables.get(SyllablePosition.START));
			}
			else if (i == (length-1)) {
				syllableList.clear();
				if (hasAny) {
					syllableList.addAll(syllables.get(SyllablePosition.ANY));
				}
				syllableList.addAll(syllables.get(SyllablePosition.END));				
			}
			else {
				if (listModified) {
					syllableList.clear();
					if (hasAny) {
						syllableList.addAll(syllables.get(SyllablePosition.ANY));
					}
					syllableList.addAll(syllables.get(SyllablePosition.MID));
				}
			}
			
			if (syllableList.size() > 0) {
				Collections.shuffle(syllableList, rnd);
				final Iterator<Syllable> it = syllableList.iterator();
				boolean needsNext = true;
				while (it.hasNext() && needsNext) {
					final Syllable nextSyllable = it.next();
					final String nextSyllableString = nextSyllable.getSyllable(rnd, currentWord, prevSyllable);
					if (nextSyllableString != null) {
						prevSyllable = nextSyllable;
						currentWord.add(nextSyllableString);
						needsNext = false;
						listModified = true;
					}
				}
			}
			
			
			finalWord = StringUtils.join(currentWord, separator);
		}
		
		return finalWord.trim();
	}
	
	@Override
	public String toString() {
		return "NameGen [syllables=" + syllables + "]";
	}	
}
