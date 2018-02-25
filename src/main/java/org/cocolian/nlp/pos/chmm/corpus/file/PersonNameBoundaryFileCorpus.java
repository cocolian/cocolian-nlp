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

import org.cocolian.nlp.Nature;
import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.CharNode;
import org.cocolian.nlp.pos.chmm.TermNatures;
import org.cocolian.nlp.pos.chmm.corpus.CharDFACorpus;
import org.cocolian.nlp.pos.chmm.corpus.PersonTermAttribute;

/**
 * 一个词作为名字前缀、后缀出现的概率
 * @author lixf
 * 
 */
public class PersonNameBoundaryFileCorpus extends FileCorpus {
	private static final String CORPUS_PATH = "data/pos/person.boundary.data";
	private static final String CORPUS_ENCODER = "UTF-8";
	private CharDFACorpus charTree;

	public PersonNameBoundaryFileCorpus(CharDFACorpus charTree) throws IOException {
		this.charTree = charTree;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
	}

	private void load(InputStream boundaryFrequencyFile) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(boundaryFrequencyFile, CORPUS_ENCODER));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] strs = temp.split("\t");
				this.addBoundaryFrequency(strs[0], Integer.parseInt(strs[1]),
						Integer.parseInt(strs[2]));
			}
		} finally {
			if (br != null)
				br.close();
		}
	}

	/**
	 * 为DFA字典补录文字；
	 */
	private void addBoundaryFrequency(String name, int position, int frequency) {
		CharNode node = this.charTree.getNode(name);
		if (node == null)
			node = this.charTree.addNewWord(name, Nature.PersonName);
		TermNatures natures = node.getTermNatures();
		PersonTermAttribute attr = (PersonTermAttribute) natures
				.getAttribute(PersonTermAttribute.ATTRIBUTE);
		if (attr == null) {
			attr = new PersonTermAttribute();
			natures.setAttribute(PersonTermAttribute.ATTRIBUTE, attr);
		}
		attr.addBoundaryFrequency(position, frequency);
	}

}
