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
package at.lume.wordgen.lib.ast.expression.parser;

import java.util.regex.Matcher;

import at.lume.wordgen.lib.WordGenParser;
import at.lume.wordgen.lib.ast.expression.CharacterMatchOption;
import at.lume.wordgen.lib.ast.expression.PrecededByExpression;

public class PrecededByParser implements WordGenParser.ExpressionParser<PrecededByExpression> {

	@Override
	public PrecededByExpression parse(Matcher matcher, String expression) {
		if (matcher.groupCount() == 1) {
			CharacterMatchOption option = null;
			if ("c".equals(matcher.group(1))) {
				option = CharacterMatchOption.CONSONANT;
			}
			else if ("v".equals(matcher.group(1))) {
				option = CharacterMatchOption.VOWEL;
			}
			else if ("n".equals(matcher.group(1))) {
				option = CharacterMatchOption.NUMBER;
			}
			return new PrecededByExpression(option);
		}		
		return null;
	}

}
