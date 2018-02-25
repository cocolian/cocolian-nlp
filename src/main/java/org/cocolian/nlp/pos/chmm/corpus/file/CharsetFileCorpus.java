/**
 * 
 */
package org.cocolian.nlp.pos.chmm.corpus.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.cocolian.nlp.corpus.FileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.CharsetCorpus;

/**
 * 字符串集合的设置，默认提供日期、时间、数字、常用名中使用的字符。
 * 默认从chars.data文件中加载数据。
 * @author lixf
 *
 */
public class CharsetFileCorpus implements CharsetCorpus{
	private static final String CORPUS_PATH = "data/pos/chars.data";
	private static final String CORPUS_ENCODER = "UTF-8";	
	private Properties chars;
	
	public CharsetFileCorpus() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CORPUS_PATH);
		try {
			this.load(is);
		} finally {
			is.close();
		}
	}

	private void load(InputStream file) throws IOException {
		if(chars==null)
			chars = new Properties();
		
		Properties properties = new Properties();
		properties.load(new InputStreamReader(file,CORPUS_ENCODER));
		this.chars.putAll(properties);
	}
	
	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CharsetCorpus#getChars(java.lang.String)
	 */
	@Override
	public char[] getChars(String name) {
		StringBuffer buffer  = new StringBuffer("");
		for(Object keyObj : this.chars.keySet()){
			String key = keyObj.toString();
			if(key.startsWith(name))
				buffer.append(this.chars.get(keyObj));
		}
		return buffer.toString().toCharArray();
	}

}
