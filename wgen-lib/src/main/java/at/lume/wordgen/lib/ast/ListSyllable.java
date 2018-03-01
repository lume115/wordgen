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

public class ListSyllable extends AbstractSyllable {

	private List<List<String>> syllableList = new ArrayList<>();

	public List<List<String>> getSyllableList() {
		return syllableList;
	}

	public void setSyllableList(List<List<String>> syllableList) {
		this.syllableList = syllableList;
	}

	@Override
	protected String getSyllableString(final Random rnd) {
		final StringBuilder sb = new StringBuilder();
		for (final List<String> list : syllableList) {
			sb.append(list.get(rnd.nextInt(list.size())));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "ListSyllable [syllableList=" + syllableList + ", position=" + position + ", modifiers=" + modifiers
				+ ", flags=" + flags + "]";
	}
}
