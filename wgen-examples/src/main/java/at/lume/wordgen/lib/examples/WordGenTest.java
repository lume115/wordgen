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
package at.lume.wordgen.lib.examples;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import at.lume.wordgen.lib.WordGen;
import at.lume.wordgen.lib.WordGenParser;
import at.lume.wordgen.lib.util.FilesUtil;

/**
 * 
 * Example that generates 3 x 1000 words and exports them to .txt files
 *
 * @author Lukasz Budryk
 */
public class WordGenTest {

	public static void main(String[] args) throws IOException {
		final WordGenTest ngExample = new WordGenTest();
		ngExample.testSimpli();
		ngExample.testBrarto();
		ngExample.testFinnish();
	}
	
	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/examples/" + resFielName).getFile());
		return file;
	}
	
	public void testSimpli() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("lang_simpli.txt"));
		long start = System.currentTimeMillis();
		final Set<String> words = new TreeSet<>();
		for (int i = 0; i < 1000; i++) {
			final String word = ng.nextWord(3, 6);
			//System.out.println(word);
			words.add(word);
		}
		FilesUtil.writeLines(words, new File("example_simpli.txt"));
		System.out.println("time needed for simpli: " + (System.currentTimeMillis() - start));
	}

	public void testBrarto() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("lang_brarto.txt"));
		long start = System.currentTimeMillis();
		final Set<String> words = new TreeSet<>();
		while (words.size() < 1000) {
			final String word = ng.nextWord(3, 6);
			//System.out.println(word);
			words.add(word);
		}
		FilesUtil.writeLines(words, new File("example_brarto.txt"));
		System.out.println("time needed for brarto: " + (System.currentTimeMillis() - start));
	}

	public void testFinnish() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("lang_fin.txt"));
		long start = System.currentTimeMillis();
		final Set<String> words = new TreeSet<>();
		while (words.size() < 1000) {
			final String word = ng.nextWord(3, 6);
			//System.out.println(word);
			words.add(word);
		}
		FilesUtil.writeLines(words, new File("example_fin.txt"));
		System.out.println("time needed for pseudo-finnish: " + (System.currentTimeMillis() - start));
	}
}
