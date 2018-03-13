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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.lume.wordgen.lib.ast.EmptySyllable;
import at.lume.wordgen.lib.ast.ListSyllable;
import at.lume.wordgen.lib.ast.Syllable;
import at.lume.wordgen.lib.ast.Syllable.SyllablePosition;
import at.lume.wordgen.lib.ast.expression.CharacterMatchExpression;
import at.lume.wordgen.lib.ast.expression.Expression;
import at.lume.wordgen.lib.ast.expression.parser.AcceptParser;
import at.lume.wordgen.lib.ast.expression.parser.FlagParser;
import at.lume.wordgen.lib.ast.expression.parser.FollowedByParser;
import at.lume.wordgen.lib.ast.expression.parser.LengthParser;
import at.lume.wordgen.lib.ast.expression.parser.NonRepeatableParser;
import at.lume.wordgen.lib.ast.expression.parser.PrecededByParser;
import at.lume.wordgen.lib.ast.flag.Flag;
import at.lume.wordgen.lib.util.FilesUtil;

/**
 * Word generator source file parser.
 * <br />
 * <br />
 * Use the {@link Builder} to create a new {@link WordGenParser} instance.<br />
 * To add custom syllable expressions use {@link #registerExpressionParser(String, ExpressionParser)}
 * @author Lukasz Budryk
 *
 */
public class WordGenParser {

    public static final String VOWELS = "aeiouyäöüáéíóúýàèìòùỳâêîôûŷ";

    public static final String NUMBERS = "0123456789";
    
    public static interface ExpressionParser<T extends Expression> {
    	T parse(final Matcher matcher, final String expression);
    }
    
    private final Map<Pattern, ExpressionParser<?>> parserMap = new HashMap<>();
    
    /**
     * registers a custom expression parser
     * @param regex
     * @param parser
     */
    public <T extends Expression> void registerExpressionParser(final String regex, final ExpressionParser<T> parser) {
    	parserMap.put(Pattern.compile(regex), parser);
    }

    private final String vowels;
    
    private final String numbers;
    
    /**
     * Builder for {@link WordGenParser}
     * 
     * @author Lukasz Budryk
     *
     */
    public static class Builder {
    	
    	private String vowels = VOWELS;
    	
    	private String numbers = NUMBERS;

    	/**
    	 * define custom vowels
    	 * @param vowels
    	 * @return
    	 */
    	public Builder setVowels(final String vowels) {
    		this.vowels = vowels;
    		return this;
    	}
    	
    	/**
    	 * define custom numbers
    	 * @param numbers
    	 * @return
    	 */
    	public Builder setNumbers(final String numbers) {
    		this.numbers = numbers;
    		return this;
    	}

    	/**
    	 * create this {@link WordGenParser}
    	 * @return
    	 */
    	public WordGenParser build() {
    		return new WordGenParser(vowels, numbers);
    	}
    }
    
    public WordGenParser(final String vowels, final String numbers) {
    	this.vowels = vowels;
    	this.numbers = numbers;
    	registerExpressionParser("\\-([c|v|n])$", new PrecededByParser());
    	registerExpressionParser("\\+([c|v|n])$", new FollowedByParser());
    	registerExpressionParser("([+|-]{1})([max|min]{0,3}len)\\(([+|-]?[0-9]*)[,]?([+|-]?[0-9]*)?\\)", new LengthParser());
    	registerExpressionParser("([+|-]{1})(flag)\\((.*)\\)", new FlagParser());
    	registerExpressionParser("([+|-]{1})(acceptStrict)\\((.*)\\)", new AcceptParser());
    	registerExpressionParser("([+|-]{1})(accept)\\((.*)\\)", new AcceptParser());
    	registerExpressionParser("\\+(noRepeat)", new NonRepeatableParser());
    }
    
    protected WordGenParser() {
    	this(VOWELS, NUMBERS);
    }
    
    public WordGen fromFile(final File file) throws IOException {
    	return fromFile(file, System.currentTimeMillis());
    }

    public WordGen fromFile(final File file, final long seed) throws IOException {
    	final List<String> rules = FilesUtil.readLines(file);
    	return fromRules(rules, seed);
    }
    
    public WordGen fromRules(final List<String> rules, final long seed) {
    	final WordGen ng = new WordGen(seed);
    	//final WordGenParser ngp = new WordGenParser();
    	
    	for (final String line : rules) {
    		final Syllable syl = parseLine(line);
    		if (syl != null) {
    			List<Syllable> syllableList = ng.getSyllables().get(syl.getPosition());
    			if (syllableList == null) {
    				syllableList = new ArrayList<>();
    				ng.getSyllables().put(syl.getPosition(), syllableList);
    			}
    			syllableList.add(syl);
    		}
    	}
    	
    	return ng;    	
    }
    
    /**
     * parses an expression string and returns all valid expressions 
     * @param expressionString
     * @return
     */
    public List<Expression> parseExpressions(final String expressionString) {
    	final Syllable syllable = parseLine(" " + expressionString);
    	if (syllable != null) {
    		return syllable.getModifiers();
    	}
    	
    	return null;
    }

    /**
     * parses an expression string and returns all valid flags
     * @param expressionString
     * @return
     */
    public List<Flag> parseFlags(final String expressionString) {
    	final Syllable syllable = parseLine(" " + expressionString);
    	if (syllable != null) {
    		return syllable.getFlags();
    	}
    	
    	return null;
    }

    private Map<Pattern, ExpressionParser<?>> getParserMap() {
		return parserMap;
	}

	private Syllable parseLine(final String line) {
    	if (line == null || line.trim().length() == 0) {
    		return null;
    	}
    	
    	Syllable syllable = null;
    	SyllablePosition sylPos = SyllablePosition.ANY;
    	
    	int curIndex = 0;
    	//determine position
    	if (line.startsWith("+")) {
    		sylPos = SyllablePosition.END;
    		curIndex = 1;
    	}
    	else if (line.startsWith("-")) {
    		sylPos = SyllablePosition.START;
    		curIndex = 1;
    	}
    	else if (line.startsWith("=")) {
    		sylPos = SyllablePosition.ANY;
    		curIndex = 1;
    	}
    	else {
    		sylPos = SyllablePosition.MID;
    		curIndex = 0;
    	}
    	

    	if ("[".equals(line.substring(curIndex,curIndex+1))) {
    		syllable = new ListSyllable();
	        while (line.substring(curIndex).contains("[")) {
	    		int startPos = line.indexOf("[", curIndex)+1;
	    		int endPos = line.indexOf("]", curIndex);
	    		((ListSyllable)syllable).getSyllableList().add(Arrays.asList(line.substring(startPos, endPos).split(",")));
	    		curIndex = endPos+1;
	    	}
    	}
    	else {
    		syllable = new EmptySyllable();
    	}

		final List<Expression> expressionList = new ArrayList<>();
    	for (final String expression : line.substring(curIndex).trim().split(" ")) {
    		//System.out.println("parsing: " + expression);
    		if (expression.trim().startsWith("#")) {
    			//flag
    			syllable.getFlags().add(new Flag(expression.trim().substring(1)));
    		}
    		else {
	    		for (final Entry<Pattern, ExpressionParser<?>> e : getParserMap().entrySet()) {
	    			final Matcher m = e.getKey().matcher(expression);
	    			if (m.find()) {
	    				final Expression expr = e.getValue().parse(m, expression);
	    				if (expr != null) {
	    					if (expr instanceof CharacterMatchExpression) {
	    						((CharacterMatchExpression) expr).setNumbers(numbers);
	    						((CharacterMatchExpression) expr).setVowels(vowels);
	    					}
		    				//System.out.println("found: " + expr);
	    					expressionList.add(expr);
	    					break;
	    				}
	    			}
	    		}
    		}
    	}
    	
		if (expressionList.size() > 0) {
			syllable.setModifers(expressionList);
		}
    	
    	syllable.setPosition(sylPos);
    	
    	return syllable;
    }
}
