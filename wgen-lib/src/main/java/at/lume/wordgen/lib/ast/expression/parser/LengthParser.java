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
import at.lume.wordgen.lib.ast.expression.LengthExpression;

public class LengthParser implements WordGenParser.ExpressionParser<LengthExpression> {

	@Override
	public LengthExpression parse(Matcher matcher, String expression) {
		// group 1 = + or -
		// group 2 = len/maxlen/minlen
		// group 3 = (min)len
		// group 4 = (max)len (len only)
		if (matcher.groupCount() == 4) {
			Integer len1 = 0;
			Integer len2 = -1;
			Boolean isPreceding = !"+".equals(matcher.group(1));
			final String func = matcher.group(2);
			final LengthExpression expr = new LengthExpression();
			if ("len".equals(func)) {
				//set both, maxlen and minlen:
				len1 = Integer.parseInt(matcher.group(3));
				len2 = Integer.parseInt(matcher.group(4));
			}
			else if ("maxlen".equals(func)) {
				//set only maxlen
				len2 = Integer.parseInt(matcher.group(3));
			}
			else if ("minlen".equals(func)) {
				//set only minlen
				len1 = Integer.parseInt(matcher.group(3));
			}
			else {
				return null;
			}			
			expr.setMinLength(len1);
			expr.setMaxLength(len2);
			expr.setPreceding(isPreceding);
			
			return expr;
		}
		
		return null;
	}

}
