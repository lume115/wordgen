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

public enum CharacterMatchOption {
	VOWEL {
		@Override
		boolean match(final String currentWord, final int position, final String vowels, final String numbers) {
			return currentWord != null && vowels.contains(currentWord.substring(position,position+1));
		}
	},
	CONSONANT {
		@Override
		boolean match(final String currentWord, final int position, final String vowels, final String numbers) {
			return currentWord != null 
					&& !vowels.contains(currentWord.substring(position,position+1))
					&& !numbers.contains(currentWord.substring(position,position+1));
		}
	},
	NUMBER {
		@Override
		boolean match(final String currentWord, final int position, final String vowels, final String numbers) {
			return currentWord != null && numbers.contains(currentWord.substring(position,1));
		}
	};
	
	abstract boolean match(final String currentWord, final int position, final String vowels, final String numbers);
}