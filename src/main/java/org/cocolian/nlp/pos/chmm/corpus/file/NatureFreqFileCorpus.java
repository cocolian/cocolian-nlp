/**
 * 
 */
package org.cocolian.nlp.pos.chmm.corpus.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cocolian.nlp.Nature;
import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.NatureFreqCorpus;

/**
 * Nature出现的频率
 * 
 * @author lixf
 *
 */
public class NatureFreqFileCorpus extends FileCorpus implements NatureFreqCorpus {
	private static final String CORPUS_PATH = "data/pos/nature.freq.data";
	private static final String CORPUS_ENCODER = "UTF-8";
	/**
	 * 词性的字符串对照索引位的hashmap
	 */
	private Map<Nature, Integer> natureFrequency;

	public NatureFreqFileCorpus() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
	}

	@Override
	public int getSize() {
		return this.natureFrequency.size();
	}

	@Override
	public int getFrequency(Nature nature) {
		return this.natureFrequency.get(nature);
	}

	private void load(InputStream is) throws IOException {
		if (this.natureFrequency == null)
			natureFrequency = new ConcurrentHashMap<Nature, Integer>();
		String split = "\t";
		// 加载词对照性表
		// nature/nature.map
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, CORPUS_ENCODER));
		String temp = null;
		String[] strs = null;
		int index = 0;
		int freq = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(split);
			if (strs.length != 4)
				continue;

			index = Integer.parseInt(strs[0]);
			// p1 = Integer.parseInt(strs[1]);
			freq = Integer.parseInt(strs[3]);
			Nature nature = Nature.register(strs[2], index);
			this.natureFrequency.put(nature, freq);

		}
		reader.close();
	}
}
