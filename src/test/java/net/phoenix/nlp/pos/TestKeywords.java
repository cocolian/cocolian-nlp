/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cocolian.nlp.Tokenizer;
import org.cocolian.nlp.keyword.KeywordExtractor;
import org.cocolian.nlp.keyword.TFIDFKeywordExtractor;
import org.cocolian.nlp.pos.chmm.HMMTokenizer;
import org.cocolian.nlp.sentence.Detector;
import org.cocolian.nlp.sentence.SimpleDetector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author lixf
 *
 */
@RunWith(JUnit4.class)
public class TestKeywords  {
	private static  Log log = LogFactory.getLog(TestKeywords.class);
	private static final String TEXT_PATH = "news2.txt";
	@Test
	public void testText() throws IOException {
		Tokenizer tokenizer = HMMTokenizer.newBuilder().build();
		Detector detector = new SimpleDetector();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(TEXT_PATH);
		try {
			Reader paragraph = new InputStreamReader(is, "UTF-8");
			KeywordExtractor extractor = new TFIDFKeywordExtractor(tokenizer, detector);
			for(String  keyword: extractor.extract(paragraph, 8)){
				log.info(keyword+" ");
			}
		} finally {
			is.close();
		}
		
	}
}
