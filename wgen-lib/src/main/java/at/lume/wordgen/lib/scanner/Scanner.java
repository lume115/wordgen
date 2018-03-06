package at.lume.wordgen.lib.scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import at.lume.wordgen.lib.scanner.stats.FrequencyStats;
import at.lume.wordgen.lib.scanner.stats.FrequencyStats.FrequencyPositionStats;
import at.lume.wordgen.lib.scanner.stats.Stats;
import at.lume.wordgen.lib.util.CollectionsUtil;
import at.lume.wordgen.lib.util.FilesUtil;

public class Scanner {
	
	private final static String FORBIDDEN_CHARS = "[«¦»!\"§$%&/\\(\\)=\\?1234567890\\{\\}\\[\\]\\\\\\+\\-\\*/;:,\\.<>|#'~–]";
	
	private final static String REMOVE_CHARS = "[,?!]$";
	
	public static class Builder {
		Locale locale = Locale.US;
		boolean isCaseSensitive = false;
		String forbiddenCharsRegex = FORBIDDEN_CHARS;
		String removeCharsRegex = REMOVE_CHARS;
		
		public Scanner build() {
			final Scanner scanner = new Scanner();
			scanner.locale = this.locale;
			scanner.isCaseSensitive = this.isCaseSensitive;
			scanner.forbiddenCharsRegex = this.forbiddenCharsRegex;
			scanner.removeCharsRegex = this.forbiddenCharsRegex;
			return scanner;
		}
		
		public Builder setIsCaseSensitive(final boolean isCaseSensitive) {
			this.isCaseSensitive = isCaseSensitive;
			return this;
		}
		
		public Builder setLocale(final Locale locale) {
			this.locale = locale;
			return this;
		}
		
		public Builder setForbiddenChars(final String regex) {
			this.forbiddenCharsRegex = regex;
			return this;
		}
		
		public Builder setRemoveChars(final String regex) {
			this.removeCharsRegex = regex;
			return this;
		}
	}
	
	private final List<Stats> stats = new ArrayList<>();
	
	private Locale locale = Locale.US;
	
	private boolean isCaseSensitive = false;
	
	private String forbiddenCharsRegex = FORBIDDEN_CHARS;
	
	private String removeCharsRegex = REMOVE_CHARS;
	
	private Scanner() { }
	
	/**
	 * scans a file for words only
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public TreeSet<String> scanFilesForWords(final File...files) throws IOException {
		final Pattern pattern = Pattern.compile(forbiddenCharsRegex);
		final Pattern removePatter = Pattern.compile(removeCharsRegex);
		final TreeSet<String> words = new TreeSet<>();
		
		for (final File file : files) {
			final List<String> lines = FilesUtil.readLines(file);
		
			for (final String line : lines) {
				for (String word : line.split("[\\s\\t]")) {
					word = word.trim();
					word = removePatter.matcher(word).replaceAll("");
					if (word.length() > 0 && !pattern.matcher(word).find()) {
						words.add(isCaseSensitive ? word : word.toLowerCase(locale));
					}
				}
			}
		}
		
		return words;
	}
	
	/**
	 * scans files for rules (and initially for words)
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<String> scanFilesForRules(final File...files) throws IOException {
		final Set<String> words = scanFilesForWords(files);
		
		//stats.add(new LengthStats());
		final FrequencyStats fs1 = new FrequencyStats().setIgnoreSingles(false).setStringLength(1);
		final FrequencyStats fs2 = new FrequencyStats().setIgnoreSingles(false).setStringLength(2);
		final FrequencyStats fs3 = new FrequencyStats().setIgnoreSingles(false).setStringLength(3);
		stats.add(fs1);
		stats.add(fs2);
		stats.add(fs3);
		
		for (final Stats s : stats) {
			s.evaluate(words);
		}
		
		final Map<String, Set<String>> startMap = generateStartMap(fs2, fs3);

		final Map<String, Set<String>> midMap = generateMidMap(fs2, fs3);

		final Set<String> endList = new TreeSet<>();
		for (final Entry<String, FrequencyPositionStats> e : fs2.getCountMap().entrySet()) {
			if (e.getValue().getEndCount() > 0) {
				endList.add(e.getKey());
			}
		}
		for (final Entry<String, FrequencyPositionStats> e : fs1.getCountMap().entrySet()) {
			if (e.getValue().getEndCount() > 0) {
				endList.add(e.getKey());
			}
		}

		//System.out.println(fs3.prettyPrint());
		
		final List<String> rules = new ArrayList<>();
		rules.add(generateRules(startMap, "-"));
		rules.add(generateRules(midMap, ""));
		rules.add(generateRules(endList, "+"));
		return rules;
		
	}

	private Map<String, Set<String>> generateStartMap(final FrequencyStats fs, final FrequencyStats fsLonger) {
		final Map<String, Set<String>> startMap = new TreeMap<>();
		
		for (final Entry<String, FrequencyPositionStats> e : fs.getCountMap().entrySet()) {
			if (e.getValue().getStartCount() > 0) {
				for (final Entry<String, FrequencyPositionStats> ce : CollectionsUtil.getByPrefix(fsLonger.getCountMap(), e.getKey()).entrySet()) {
					if (ce.getValue().getStartCount() > 0) {
						Set<String> curMod = startMap.get(e.getKey().substring(0,1));
						if (curMod == null) {
							curMod = new TreeSet<>();
							startMap.put(e.getKey().substring(0,1), curMod);
						}
						curMod.add(ce.getKey().substring(1));						
						
						final String subKey = e.getKey().substring(1) + ce.getKey().substring(e.getKey().length());
						for (final Entry<String, FrequencyPositionStats> subCe : CollectionsUtil.getByPrefix(fsLonger.getCountMap(), subKey).entrySet()) {
							if (subCe.getValue().getStartCount() > 0) {
								curMod = startMap.get(e.getKey());
								if (curMod == null) {
									curMod = new TreeSet<>();
									startMap.put(e.getKey(), curMod);
								}
								curMod.add(subCe.getKey().substring(1));								
							}
						}
					}
				}
			}
		}

		return startMap;
	}
	
	private Map<String, Set<String>> generateMidMap(final FrequencyStats fs, final FrequencyStats fsLonger) {
		final Map<String, Set<String>> midMap = new TreeMap<>();
		for (final Entry<String, FrequencyPositionStats> e : fs.getCountMap().entrySet()) {
			if (e.getValue().getMidCount() > 0) {
				//it's a mid syllable
				for (final Entry<String, FrequencyPositionStats> ce : CollectionsUtil.getByPrefix(fsLonger.getCountMap(), e.getKey()).entrySet()) {
					//get all longer syllables starting with syllable
					if (ce.getValue().getMidCount() > 0) {
						final String subKey = e.getKey().substring(1) + ce.getKey().substring(e.getKey().length());
						for (final Entry<String, FrequencyPositionStats> subCe : CollectionsUtil.getByPrefix(fsLonger.getCountMap(), subKey).entrySet()) {
							if (subCe.getValue().getMidCount() > 0) {
								Set<String> curMod = midMap.get(e.getKey());
								if (curMod == null) {
									curMod = new TreeSet<>();
									midMap.put(e.getKey(), curMod);
								}
								curMod.add(subCe.getKey().substring(1));
							}
						}
					}
					
					if (ce.getValue().getEndCount() > 0) {
						Set<String> curMod = midMap.get(e.getKey().substring(0,1));
						if (curMod == null) {
							curMod = new TreeSet<>();
							midMap.put(e.getKey().substring(0,1), curMod);
						}
						curMod.add(ce.getKey().substring(1));
					}
				}
			}
		}
		return midMap;
	}
	
	private String generateRules(final Collection<String> syls, final String prefix) {
		final StringBuilder sb = new StringBuilder();
		for (final String s : syls) {
			sb.append(prefix).append("[").append(s).append("]\n");
		}
		return sb.toString();
	}
	
	private String generateRules(final Map<String, Set<String>> sylMap, final String prefix) {
		final StringBuilder sb = new StringBuilder();
		for (final Entry<String, Set<String>> e : sylMap.entrySet()) {
			sb.append(prefix).append("[").append(e.getKey()).append("]");
			if (e.getValue() != null && e.getValue().size() > 0) {
				sb.append(" +accept(");
				final Iterator<String> it = e.getValue().iterator();
				while (it.hasNext()) {
					sb.append(it.next());
					if (it.hasNext()) {
						sb.append(",");
					}
				}
				sb.append(")\n");
			}
		}

		return sb.toString();
	}	
}
