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

import at.lume.wordgen.lib.scanner.Scanner;
import at.lume.wordgen.lib.util.FilesUtil;

/**
 * Scanner example uses {@link Scanner#scanFilesForRules(File...)} for scanning files and generating rules based on them. <br />
 * In this example three languages (english, norwegian, polish) are used
 * @author lb
 *
 */
public class ScannerExample {

	public static void main(String[] args) throws IOException {
		ScannerExample se = new ScannerExample();
		se.runEnglishScanner();
		se.runNorwegianScanner();
		se.runPolishScanner();
	}
	
	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/examples/scanner/" + resFielName).getFile());
		return file;
	}
	
	public void runEnglishScanner() throws IOException {
		final Scanner scan = new Scanner.Builder().build();
		FilesUtil.writeLines(scan.scanFilesForRules(getFileForRes("en.txt")), new File("rules_en.txt"));
	}
	
	public void runNorwegianScanner() throws IOException {
		final Scanner scan = new Scanner.Builder().build();
		FilesUtil.writeLines(scan.scanFilesForRules(getFileForRes("nor.txt")), new File("rules_nor.txt"));
	}
	
	public void runPolishScanner() throws IOException {
		final Scanner scan = new Scanner.Builder().build();
		FilesUtil.writeLines(scan.scanFilesForRules(getFileForRes("pl.txt")), new File ("rules_pl.txt"));
	}
}
