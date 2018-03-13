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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import at.lume.wordgen.lib.ast.expression.AcceptExpression;
import at.lume.wordgen.lib.ast.expression.Expression;
import at.lume.wordgen.lib.ast.expression.FlagExpression;
import at.lume.wordgen.lib.ast.expression.NonRepeatableExpression;
import at.lume.wordgen.lib.ast.flag.Flag;

public class NameGenTestPreConditions {

	private File getFileForRes(final String resFielName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("at/lume/wordgen/lib/" + resFielName).getFile());
		return file;
	}

	@Test
	public void testParsingFlagsAndPreConditions() throws IOException {
		WordGen ng = new WordGenParser.Builder().build().fromFile(getFileForRes("parse12_1.txt"));
		assertNotNull("ng not null", ng);
		final List<Expression> exprList = new ArrayList<>();
		final FlagExpression flagExpr = new FlagExpression();
		flagExpr.setPreceding(false);
		flagExpr.setFlags(Arrays.asList(new Flag[] {new Flag("a")}));
		exprList.add(flagExpr);
		
		assertEquals("random word = 'a'", "a", ng.nextWord(1, 1, exprList));
		assertEquals("random word = 'aa'", "aa", ng.nextWord(2, 2, exprList));
		assertEquals("random word = 'aaa'", "aaa", ng.nextWord(3, 3, exprList));
		assertEquals("random word = 'aaaa'", "aaaa", ng.nextWord(4, 4, exprList));
		assertEquals("random word = 'aaaaa'", "aaaaa", ng.nextWord(5, 5, exprList));
		assertEquals("random word = 'aaaaaa'", "aaaaaa", ng.nextWord(6, 6, exprList));
		assertEquals("random word = 'aaaaaaa'", "aaaaaaa", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaaaab'", "aaaaaab", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaaabb'", "aaaaabb", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaabbb'", "aaaabbb", ng.nextWord(7, 7, exprList));
	}	

	@Test
	public void testParsingFlagsAndPreConditionsWithExpressionString() throws IOException {
		final WordGenParser wgp = new WordGenParser.Builder().build();
		final WordGen ng = wgp.fromFile(getFileForRes("parse12_1.txt"));
		assertNotNull("ng not null", ng);
		final List<Expression> exprList = wgp.parseExpressions("+flag(a)");  		
		assertEquals("random word = 'a'", "a", ng.nextWord(1, 1, exprList));
		assertEquals("random word = 'aa'", "aa", ng.nextWord(2, 2, exprList));
		assertEquals("random word = 'aaa'", "aaa", ng.nextWord(3, 3, exprList));
		assertEquals("random word = 'aaaa'", "aaaa", ng.nextWord(4, 4, exprList));
		assertEquals("random word = 'aaaaa'", "aaaaa", ng.nextWord(5, 5, exprList));
		assertEquals("random word = 'aaaaaa'", "aaaaaa", ng.nextWord(6, 6, exprList));
		assertEquals("random word = 'aaaaaaa'", "aaaaaaa", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaaaab'", "aaaaaab", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaaabb'", "aaaaabb", ng.nextWord(7, 7, exprList));
		assertNotEquals("random word != 'aaaabbb'", "aaaabbb", ng.nextWord(7, 7, exprList));
	}
	
	@Test
	public void testParsingExpressionString() throws IOException {
		final String expressionString = "+flag(a) -accept(ab) +noRepeat #a #b #xyz";
		final WordGenParser wgp = new WordGenParser.Builder().build();
		final List<Expression> exprList = wgp.parseExpressions(expressionString);
		assertEquals("three expressions parsed", 3, exprList.size());
		assertEquals("first expressions = FlagExpression.class", FlagExpression.class, exprList.get(0).getClass());
		assertEquals("second expressions = AcceptExpression.class", AcceptExpression.class, exprList.get(1).getClass());
		assertEquals("third expressions = NonRepeatableExpression.class", NonRepeatableExpression.class, exprList.get(2).getClass());
		
		final List<Flag> flagList = wgp.parseFlags(expressionString);
		assertEquals("three flags parsed", 3, flagList.size());
		assertEquals("first flag = a", "a", flagList.get(0).getFlag());
		assertEquals("second flag = b", "b", flagList.get(1).getFlag());
		assertEquals("third flag = xyz", "xyz", flagList.get(2).getFlag());
	}	
}
