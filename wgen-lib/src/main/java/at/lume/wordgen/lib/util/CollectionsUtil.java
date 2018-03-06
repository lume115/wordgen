package at.lume.wordgen.lib.util;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public class CollectionsUtil {

	public static <T> SortedSet<String> getByPrefix(final NavigableSet<String> set, final String prefix) {
		return set.subSet(prefix, prefix + Character.MAX_VALUE);
	}
	
	public static <T> SortedMap<String, T> getBySuffix(final NavigableMap<String, T> myMap, final String suffix) {
	    final TreeMap<String, T> map = new TreeMap<>();
	    for (final Entry<String, T> e : myMap.entrySet()) {
	    	if (e.getKey().endsWith(suffix)) {
	    		map.put(e.getKey(), e.getValue());
	    	}
	    }
	    return map;
	}  
	
	public static <T> SortedMap<String, T> getByPrefix(final NavigableMap<String, T> myMap, final String prefix ) {
	    return myMap.subMap(prefix, prefix + Character.MAX_VALUE);
	} 

}
