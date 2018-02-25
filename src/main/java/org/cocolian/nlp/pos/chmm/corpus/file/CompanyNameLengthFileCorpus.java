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

import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.CompanyNameLengthCorpus;

/**
 * 公司名字长度
 * @author lixf
 * 
 */
public class CompanyNameLengthFileCorpus extends FileCorpus implements CompanyNameLengthCorpus {
	private static final String CORPUS_PATH = "data/pos/company.length.data";
	private static final String CORPUS_ENCODER = "UTF-8";	
	private double freqOfLength[]; // 长度为1~50的机构名字出现的概率

	public CompanyNameLengthFileCorpus() throws IOException {		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CompanyNameLengthCorpus#getFrequency(int)
	 */
	@Override
	public double getFrequency(int length) {
		return freqOfLength[length > 50 ? 50 : length];
	}

	/**
	 * 加载频率表。 频率表记录长度为N的公司名字的出现的概率，其中N=1..50。
	 * 
	 * @throws IOException
	 */
	public void load(InputStream frequencyPath) throws IOException {
		BufferedReader reader = null;
		String temp = null;
		this.freqOfLength = new double[51];
		String[] strs = null;
		int index = 0;
		float fac = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(frequencyPath,CORPUS_ENCODER));
			while ((temp = reader.readLine()) != null) {
				if (isBlank(temp = temp.trim())) {
					continue;
				}
				strs = temp.split("\t");
				index = Integer.parseInt(strs[0]);
				fac = Float.parseFloat(strs[2]);
				if (index > 50) {
					index = 50;
				}
				freqOfLength[index] += fac;
			}

		} finally {
			reader.close();
		}
	}

}
