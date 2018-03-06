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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import at.lume.wordgen.lib.WordGenParser;
import at.lume.wordgen.lib.ast.expression.AcceptExpression;

public class AcceptParser implements WordGenParser.ExpressionParser<AcceptExpression> {

	@Override
	public AcceptExpression parse(Matcher matcher, String expression) {
		// group 1 = + or -
		// group 2 = accept or accept_strict
		// group 3 = syllables: (syllable,syllable,...)
		if (matcher.groupCount() == 3) {
			Boolean isPreceding = !"+".equals(matcher.group(1));
			final AcceptExpression expr = new AcceptExpression();
			expr.setAcceptList(new ArrayList<String>());
			expr.getAcceptList().addAll(Arrays.asList(matcher.group(3).split(",")));
			expr.setStrict(matcher.group(2).equals("accept_strict"));
			expr.setPreceding(isPreceding);
			
			return expr;
		}
		
		return null;
	}

}
