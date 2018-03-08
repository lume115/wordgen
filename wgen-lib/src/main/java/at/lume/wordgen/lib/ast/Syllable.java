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
package at.lume.wordgen.lib.ast;

import java.util.List;
import java.util.Random;

import at.lume.wordgen.lib.ast.expression.Expression;
import at.lume.wordgen.lib.ast.flag.Flag;

public interface Syllable {

	public enum SyllablePosition {
		/**
		 * starts with - (minus)
		 */
		START,
		/**
		 * starts with no modifier
		 */
		MID,
		/**
		 * starts with + (plus)
		 */
		END,
		/**
		 * starts with = (equal sign)
		 */
		ANY
	}
	
	String getSyllable(final Random rnd, final List<String> currentWord, final Syllable previousSyllable);
	
	SyllablePosition getPosition();
	
	void setPosition(final SyllablePosition position);
	
	List<Expression> getModifiers();
	
	void setModifers(final List<Expression> modifierList);
	
	List<Flag> getFlags();
	
	void setFlags(final List<Flag> flags);

	boolean hasFlag(final List<Flag> flags);
}
