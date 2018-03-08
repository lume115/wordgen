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

public class NameGenTestNonRepeatable {

	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/" + resFielName).getFile());
		return file;
	}

	@Test
	public void testParsingNonRepeatable() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse11_1.txt"));
		assertNotNull("ng not null", ng);
		assertEquals("random word = 'p'", "p", ng.nextWord(1, 1));
		assertEquals("random word = 'pe'", "pe", ng.nextWord(2, 2));
		assertEquals("random word = 'pep'", "pep", ng.nextWord(3, 3));
		assertEquals("random word = 'pepe'", "pepe", ng.nextWord(4, 4));
		assertEquals("random word = 'pepep'", "pepep", ng.nextWord(5, 5));
		assertEquals("random word = 'pepepe'", "pepepe", ng.nextWord(6, 6));
	}	

	@Test
	public void testParsingNonRepeatableWithSeperator() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse11_2.txt"));
		assertNotNull("ng not null", ng);
		assertEquals("random word = 'b'", "b", ng.nextWord(1, 1, " "));
		assertEquals("random word = 'b e'", "b e", ng.nextWord(2, 2, " "));
		assertEquals("random word = 'b e b'", "b e b", ng.nextWord(3, 3, " "));
		assertEquals("random word = 'b e b e'", "b e b e", ng.nextWord(4, 4, " "));
		assertEquals("random word = 'b e b e b'", "b e b e b", ng.nextWord(5, 5, " "));
		assertEquals("random word = 'b e b e b e'", "b e b e b e", ng.nextWord(6, 6, " "));
	}	
}
