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

import java.util.ArrayList;
import java.util.List;

import at.lume.wordgen.lib.ast.Syllable;

public class AcceptExpression implements Expression {
	
	private boolean isPreceding = true;
	
	/**
	 * Only important for + (following syllables) <br />
	 * If accept is strict then .equals is used to compare syllables, else .startsWith is used <br />
	 */
	private boolean isStrict = false;
	
	private List<String> acceptList = new ArrayList<>();
	
	public boolean isPreceding() {
		return isPreceding;
	}

	public List<String> getAcceptList() {
		return acceptList;
	}

	public boolean isStrict() {
		return isStrict;
	}

	public void setStrict(boolean isStrict) {
		this.isStrict = isStrict;
	}

	public void setAcceptList(List<String> acceptList) {
		this.acceptList = acceptList;
	}


	public void setPreceding(boolean isPreceding) {
		this.isPreceding = isPreceding;
	}

	@Override
	public boolean matchPre(final String currentWord, final Syllable prevSyllable) {
		if (!isPreceding) {
			return true;
		}
		
		for (final String accept : acceptList) {
			if (currentWord.endsWith(accept)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean matchPost(final String currentWord, final String newSyllable, final Syllable nextSyllable) {
		if (isPreceding) {
			return true;
		}

		for (final String accept : acceptList) {
			if (!isStrict) {
				if (newSyllable.startsWith(accept)) {
					return true;
				}
			}
			else {
				if (newSyllable.equals(accept)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "AcceptExpression [isPreceding=" + isPreceding + ", acceptList=" + acceptList + "]";
	}
}
