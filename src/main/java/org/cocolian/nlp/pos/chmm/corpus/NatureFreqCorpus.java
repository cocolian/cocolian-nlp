package org.cocolian.nlp.pos.chmm.corpus;

import org.cocolian.nlp.Nature;

/**
 * 
 * @author lixf
 *
 */
public interface NatureFreqCorpus {

	/**
	 * 获取指定词性出现的频率
	 * @param nature
	 * @return
	 */
	public int getFrequency(Nature nature);

	/**
	 * 获取总大小
	 * @return
	 */
	public int getSize();

}