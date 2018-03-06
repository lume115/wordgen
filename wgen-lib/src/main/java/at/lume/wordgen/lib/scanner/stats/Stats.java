package at.lume.wordgen.lib.scanner.stats;

import java.util.Set;

public interface Stats {

	public void evaluate(final Set<String> words);
	
	public String prettyPrint();
}
