package at.lume.wordgen.lib.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtils {

	public static String join(final Collection<String> strings, final String separator) {
		if (strings == null) {
			return null;
		}
		
		if (strings.isEmpty()) {
			return "";
		}
		
		final Iterator<String> it = strings.iterator();

		final String first = it.next();
		if (!it.hasNext()) {
			return first;
		}
		
		final StringBuilder sb = new StringBuilder(128);
		sb.append(first);
		
		while (it.hasNext()) {
			sb.append(separator);
			final String next = it.next();
			if (next != null) {
				sb.append(next);
			}
		}
		
		return sb.toString();
	}
	
}
