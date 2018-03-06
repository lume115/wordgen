package at.lume.wordgen.lib.scanner.stats;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class LengthStats implements Stats {
	
	public Map<Integer, Float> lengthStats = new TreeMap<>();
	
	public Map<Integer, Integer> lengthCountStats = new TreeMap<>();
	
	public LengthStats() { }
	
	public LengthStats(final Set<String> words) {
		evaluate(words);
	}
	
	@Override
	public void evaluate(Set<String> words) {
		for (final String word : words) {
			final Integer count = lengthCountStats.get(word.length());
			lengthCountStats.put(word.length(), count == null ? 1 : count+1);
		}
		
		for (final Entry<Integer, Integer> e : lengthCountStats.entrySet()) {
			lengthStats.put(e.getKey(), (float)e.getValue() / (float)words.size());
		}		
	}

	@Override
	public String prettyPrint() {
		return lengthCountStats.toString();
	}

	@Override
	public String toString() {
		return "LengthStats [lengthStats=" + lengthStats + ", lengthCountStats=" + lengthCountStats + "]";
	}
}
