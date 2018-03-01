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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.lume.wordgen.lib.ast.expression.Expression;
import at.lume.wordgen.lib.ast.flag.Flag;

public abstract class AbstractSyllable implements Syllable {

	protected SyllablePosition position = SyllablePosition.ANY;
	
	protected List<Expression> modifiers = new ArrayList<>();
	
	protected List<Flag> flags = new ArrayList<>();
	
	@Override
	public void setFlags(final List<Flag> flags) {
		this.flags = flags;
	}
	
	@Override
	public List<Flag> getFlags() {
		return flags;
	}
	
	@Override
	public boolean hasFlag(final List<Flag> flags) {	
		if (flags != null && flags.size() > 0 && this.flags != null && this.flags.size() > 0) {
			for (final Flag flag : flags) {
				boolean found = false;
				for (final Flag f : this.flags) {
					if (f.getFlag().equals(flag.getFlag())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setPosition(SyllablePosition position) {
		this.position = position;
	}
	
	@Override
	public SyllablePosition getPosition() {
		return position;
	}
	
	@Override
	public void setModifers(List<Expression> modifierList) {
		this.modifiers = modifierList;
	}

	@Override
	public List<Expression> getModifiers() {
		return modifiers;
	}
	
	@Override
	public String getSyllable(final Random rnd, final String currentWord, final Syllable previousSyllable) {
		for (final Expression e : getModifiers()) {
			if (!e.matchPre(currentWord, previousSyllable)) {
				return null;
			}
		}
		
		final String currentSyllable = getSyllableString(rnd);
		
		if (previousSyllable != null) {
			for (final Expression e : previousSyllable.getModifiers()) {
				if (!e.matchPost(currentWord, currentSyllable, this)) {
					return null;
				}
			}			
		}
		
		return currentSyllable;
	}
	
	protected abstract String getSyllableString(final Random rnd);
}
