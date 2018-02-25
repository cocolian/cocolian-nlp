/**
 * 
 */
package org.cocolian.nlp.pos.chmm.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.collections.map.MultiKeyMap;
import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.CooccurrenceCorpus;

/**
 * 词与词之间的共现关系。
 * @author lixf
 *
 */
public class CooccurrenceFileCorpus  implements CooccurrenceCorpus{
	private static final String CORPUS_PATH = "data/pos/term.cooccurrence.data";
	private static final String CORPUS_ENCODER = "UTF-8";	
	private MultiKeyMap bigramTables; //词之间的共现关系；
	
	public CooccurrenceFileCorpus() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
	}

	public void load(InputStream bigramFilePath) throws IOException {
		if(this.bigramTables == null)
			this.bigramTables = new MultiKeyMap();
		BufferedReader reader = new BufferedReader(new InputStreamReader(bigramFilePath,CORPUS_ENCODER));
		String row = reader.readLine().trim();
		while (row != null) {
			row = row.trim();
			if (row.length() > 0) {
				String[] items = row.split("\\s");
				int freq = Integer.parseInt(items[2]);
				bigramTables.put(items[0], items[1], freq);
			}
			row = reader.readLine();
		}
		reader.close();
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CooccurrenceCorpus#getCooccurrenceFrequency(java.lang.String, java.lang.String)
	 */
	@Override
	public int getCooccurrenceFrequency(String from, String to) {
		Integer freq = (Integer) bigramTables.get(from, to);
		if (freq == null)
			return 0;
		return freq;
	}
	
}
