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
import java.util.Arrays;

import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.T2SCorpus;

/**
 * @author lixf
 * 
 */
public class T2SFileCorpus extends FileCorpus implements T2SCorpus {
	private static final String CORPUS_PATH = "data/pos/t2s.data";
	private static final String CORPUS_ENCODER = "UTF-8";
	private char map[]; // 简繁体映射表；

	/**
	 * 
	 * @param mapFile
	 *            简繁体映射文件
	 * @throws IOException
	 */
	public T2SFileCorpus() throws IOException {
		//super(parent, "t2s.data");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
		
	}

	private void load(InputStream mapFile) throws IOException {
		if (this.map == null) {
			this.map = new char[65535];
			Arrays.fill(map, (char) 0);
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(mapFile,CORPUS_ENCODER));
			String temp;
			while ((temp = reader.readLine()) != null) {
				temp = temp.trim();
				if (isBlank(temp)) {
					continue;
				}
				if (map[temp.charAt(0)] == 0) {
					map[temp.charAt(0)] = temp.charAt(2);
				}
			}
			reader.close();

		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.T2SCorpus#toSimple(char)
	 */
	@Override
	public char toSimple(char c) {
		char value = map[c];
		if (value == 0) {
			return c;
		}
		return value;
	}

}
