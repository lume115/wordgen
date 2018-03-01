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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import at.lume.wordgen.lib.WordGen;
import at.lume.wordgen.lib.WordGenParser;

public class NameGenTestCharMatch {

	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/" + resFielName).getFile());
		return file;
	}

	@Test
	public void testParsingPositionAndListSyllablesWithCharacterMatchWithoutNumber() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse03.txt"));
		assertNotNull(ng);
		assertEquals("random word = 'a'", "a", ng.nextWord(1, 1));
		assertEquals("random word = 'az'", "az", ng.nextWord(2, 2));
		assertEquals("random word = 'abu'", "abu", ng.nextWord(3, 3));
		assertEquals("random word = 'abiz'", "abiz", ng.nextWord(4, 4));
		assertEquals("random word = 'abibu'", "abibu", ng.nextWord(5, 5));
		assertEquals("random word = 'abibiz'", "abibiz", ng.nextWord(6, 6));
		assertEquals("random word = 'abibibu'", "abibibu", ng.nextWord(7, 7));
	}
	
	@Test
	public void testParsingPositionAndListSyllablesWithCharacterMatchInclNumber() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse05.txt"));
		assertNotNull(ng);
		assertEquals("random word = 'a'", "a", ng.nextWord(1, 1));
		assertEquals("random word = 'a2'", "a2", ng.nextWord(2, 2));
		assertEquals("random word = 'a1c'", "a1c", ng.nextWord(3, 3));
		assertEquals("random word = 'a1b2'", "a1b2", ng.nextWord(4, 4));
		assertEquals("random word = 'a1b1c'", "a1b1c", ng.nextWord(5, 5));
		assertEquals("random word = 'a1b1b2'", "a1b1b2", ng.nextWord(6, 6));
		assertEquals("random word = 'a1b1b1c'", "a1b1b1c", ng.nextWord(7, 7));
	}
	
	@Test
	public void testParsingPositionAndListSyllablesWithCharacterMatchAndLongSyllables() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse07.txt"));
		assertNotNull(ng);
		assertEquals("random word = 'aa'", "aa", ng.nextWord(1, 1));
		assertEquals("random word = 'aaxx'", "aaxx", ng.nextWord(2, 2));
		assertEquals("random word = 'aababex'", "aababex", ng.nextWord(3, 3));
		assertEquals("random word = 'aababeexx'", "aababeexx", ng.nextWord(4, 4));
	}

	@Test
	public void testParsingPositionAndListSyllablesWithCharacterMatchAndUnknownVowels() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse09.txt"));
		assertNotNull(ng);
		assertEquals("random word = 'b'", "b", ng.nextWord(1, 1));
		assertEquals("random word = 'b'", "b", ng.nextWord(2, 2));
		assertEquals("random word = 'b'", "b", ng.nextWord(3, 3));
		assertEquals("random word = 'b'", "b", ng.nextWord(4, 4));
	}
	
	@Test
	public void testParsingPositionAndListSyllablesWithCharacterMatchAndUnknownAndCustomVowels() throws IOException {
		WordGen ng = new WordGenParser.Builder()
				.setVowels(WordGenParser.VOWELS + "ȩ")
				.build()
				.fromFile(getFileForRes("parse09.txt"));
		assertNotNull(ng);
		assertEquals("random word = 'b'", "b", ng.nextWord(1, 1));
		assertEquals("random word = 'b'", "b", ng.nextWord(3, 3));
		assertEquals("random word = 'b'", "b", ng.nextWord(4, 4));
	}
}
