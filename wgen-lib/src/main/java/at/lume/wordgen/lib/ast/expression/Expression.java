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
package at.lume.wordgen.lib.ast.expression;

import at.lume.wordgen.lib.ast.Syllable;

/**
 * name gen expression
 * @author lb
 *
 */
public interface Expression {
	
	/**
	 * before selecting the new syllable a pre-match is done using the current word
	 * @param currentWord
	 * @return
	 */
	boolean matchPre(final String currentWord, final Syllable previousSyllable);
	
	/**
	 * after a syllable has been selected a post match is done using the new syllable
	 * @param newSyllable
	 * @return
	 */
	boolean matchPost(final String currentWord, final String newSyllable, final Syllable nextSyllable);
}
