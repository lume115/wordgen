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
package at.lume.wordgen.lib.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Lukasz Budryk
 *
 */
public class FilesUtil {
	
	public static long writeLines(final Collection<String> lines, final File file) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			return writeLines(lines, new OutputStreamWriter(os));
		} finally {
			closeCloseable(os);
		}		
	}

	public static List<String> readLines(final File file) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return readLines(new InputStreamReader(in));
		} finally {
			closeCloseable(in);
		}		
	}
	
	public static String readAll(final File file) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return readAll(new InputStreamReader(in));
		} finally {
			closeCloseable(in);
		}		
	}
	
	private static void closeCloseable(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (final IOException e) {
			//ignore it
		}		
	}
	
    public static String readAll(final Reader input) throws IOException {
    	final StringBuilder sb = new StringBuilder();
    	for (final String line : readLines(input)) {
    		sb.append(line).append("\n");
    	}
    	return sb.toString();
    }
    
    public static List<String> readLines(final Reader input) throws IOException {
    	final BufferedReader reader = new BufferedReader(input);
    	final List<String> list = new ArrayList<>();
    	String line = reader.readLine();
    	while (line != null) {
    		list.add(line);
    		line = reader.readLine();
    	}
    	return list;
    }

    public static long writeLines(final Collection<String> lines, final Writer out) throws IOException {
    	final BufferedWriter writer = new BufferedWriter(out);
    	int counter = 0;
    	for (final String line : lines) {
    		counter++;
    		writer.write(line);
    		writer.write("\n");
    	}
    	writer.flush();
    	return counter;
    }
}
