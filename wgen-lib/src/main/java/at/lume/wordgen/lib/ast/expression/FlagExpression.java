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

import java.util.List;

import at.lume.wordgen.lib.ast.Syllable;
import at.lume.wordgen.lib.ast.flag.Flag;

public class FlagExpression implements Expression {

	List<Flag> flags;

	private boolean isPreceding = true;
	
	public boolean isPreceding() {
		return isPreceding;
	}

	public void setPreceding(boolean isPreceding) {
		this.isPreceding = isPreceding;
	}

	@Override
	public boolean matchPre(final String currentWord, final Syllable prevSyllable) {
		if (!isPreceding) {
			return true;
		}

		return (prevSyllable.hasFlag(flags));
	}

	public List<Flag> getFlags() {
		return flags;
	}

	public void setFlags(List<Flag> flags) {
		this.flags = flags;
	}

	@Override
	public boolean matchPost(final String currentWord, final String newSyllable, final Syllable nextSyllable) {
		if (isPreceding) {
			return true;
		}
		
		final boolean hasFlag = nextSyllable.hasFlag(flags);
		return hasFlag;
	}

	@Override
	public String toString() {
		return "FlagExpression [flags=" + flags + ", isPreceding=" + isPreceding + "]";
	}
}
