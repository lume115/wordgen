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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import at.lume.wordgen.lib.WordGen;
import at.lume.wordgen.lib.WordGenParser;
import at.lume.wordgen.lib.ast.Syllable.SyllablePosition;

public class NameGenParserTest {

	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/" + resFielName).getFile());
		return file;
	}
	
	@Test
	public void testParsingPositionAndListSyllables() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse01.txt"));
		assertNotNull(ng);
		assertEquals("generator has 4 elements", 4, ng.getSyllables().size());
		assertEquals("1 element with pos END", 1, ng.getSyllables().get(SyllablePosition.END).size());
		assertEquals("1 element with pos MID", 1, ng.getSyllables().get(SyllablePosition.MID).size());
		assertEquals("1 element with pos START", 1, ng.getSyllables().get(SyllablePosition.START).size());
		assertEquals("1 element with pos ANY", 1, ng.getSyllables().get(SyllablePosition.ANY).size());
	}

	@Test
	public void testParsingPositionAndListSyllablesWithoutAny() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse02.txt"));
		assertNotNull(ng);
		assertEquals("generator has 4 elements", 3, ng.getSyllables().size());
		assertEquals("1 element with pos END", 1, ng.getSyllables().get(SyllablePosition.END).size());
		assertEquals("1 element with pos MID", 1, ng.getSyllables().get(SyllablePosition.MID).size());
		assertEquals("1 element with pos START", 1, ng.getSyllables().get(SyllablePosition.START).size());
		assertNull("no element with pos ANY", ng.getSyllables().get(SyllablePosition.ANY));
	}
}
