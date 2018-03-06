package at.lume.wordgen.lib.scanner.stats;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class FrequencyStats implements Stats {
	
	public static class FrequencyPositionStats {
		int startCount = 0;
		int midCount = 0;
		int endCount = 0;
		public FrequencyPositionStats() {
			this(0,0,0);
		}
		public FrequencyPositionStats(final int startCount, final int midCount, final int endCount) {
			
		}
		public void incStartCount() {
			startCount+=1;
		}
		public void incEndCount() {
			endCount+=1;
		}
		public void incMidCount() {
			midCount+=1;
		}
		public int getStartCount() {
			return startCount;
		}
		public void setStartCount(int startCount) {
			this.startCount = startCount;
		}
		public int getMidCount() {
			return midCount;
		}
		public void setMidCount(int midCount) {
			this.midCount = midCount;
		}
		public int getEndCount() {
			return endCount;
		}
		public void setEndCount(int endCount) {
			this.endCount = endCount;
		}
		public int getTotalCount() {
			return startCount+midCount+endCount;
		}
		@Override
		public String toString() {
			return "FrequencyPositionStats [startCount=" + startCount + ", midCount=" + midCount + ", endCount="
					+ endCount + "]";
		}		
	}
	
	private TreeMap<String, Float> frequencyMap = new TreeMap<>();
	
	private TreeMap<String, FrequencyPositionStats> countMap = new TreeMap<>();
	
	private int stringLength = 1;

	private boolean ignoreSingles = false;
	
	public FrequencyStats() {
		this(null);
	}
	
	public FrequencyStats(final Set<String> words) {
		evaluate(words);
	}
	
	public boolean isIgnoreSingles() {
		return ignoreSingles;
	}

	public FrequencyStats setIgnoreSingles(boolean ignoreSingles) {
		this.ignoreSingles = ignoreSingles;
		return this;
	}

	public int getStringLength() {
		return stringLength;
	}

	public FrequencyStats setStringLength(int stringLength) {
		this.stringLength = stringLength;
		return this;
	}

	public TreeMap<String, Float> getFrequencyMap() {
		return frequencyMap;
	}

	public void setFrequencyMap(TreeMap<String, Float> frequencyMap) {
		this.frequencyMap = frequencyMap;
	}

	public TreeMap<String, FrequencyPositionStats> getCountMap() {
		return countMap;
	}

	public void setCountMap(TreeMap<String, FrequencyPositionStats> countMap) {
		this.countMap = countMap;
	}

	@Override
	public void evaluate(Set<String> words) {
		long totalCount = 0;
		if (words == null) return;
		
		for (final String word : words) {
			for (int i = 0; i <= (word.length()-stringLength); i++) {
				final String singleCharacter = word.substring(i, i+stringLength);
				FrequencyPositionStats count = countMap.get(singleCharacter);
				if (count == null) {
					count = new FrequencyPositionStats();
					countMap.put(singleCharacter, count);
				}
				
				if (i==0) {
					count.incStartCount();
				}
				else if (i == (word.length()-stringLength)) {
					count.incEndCount();
				}
				else {
					count.incMidCount();
				}
				
				totalCount++;
			}
		}
		
		final Iterator<Entry<String, FrequencyPositionStats>> it = countMap.entrySet().iterator();

		while (it.hasNext()) {
			final Entry<String, FrequencyPositionStats> e = it.next();
			if (e.getValue().getTotalCount() == 1 && ignoreSingles) {
				it.remove();
				frequencyMap.remove(e.getKey());
			}
			else {
				frequencyMap.put(e.getKey(), (float)e.getValue().getTotalCount() / (float)totalCount);
			}
		}
	}

	@Override
	public String prettyPrint() {
		final StringBuilder sb = new StringBuilder();
		final Formatter fmt = new Formatter(sb);
		for (String c : countMap.keySet()) {
			final FrequencyPositionStats s = countMap.get(c);
			fmt.format("%-3s%6d (%4d,%4d,%4d)%10.3f%% (1:%d)\n", c, s.getTotalCount(),
					s.getStartCount(), s.getMidCount(), s.getEndCount(),
					(frequencyMap.get(c) * 100f), (int)(1f / frequencyMap.get(c)));
		}
		fmt.close();
		return sb.toString();
	}

	@Override
	public String toString() {
		return "FrequencyStats [frequencyMap=" + frequencyMap + ", countMap=" + countMap + "]";
	}
}
