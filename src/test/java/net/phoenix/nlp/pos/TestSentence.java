/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cocolian.nlp.sentence.Detector;
import org.cocolian.nlp.sentence.Sentence;
import org.cocolian.nlp.sentence.SimpleDetector;
import org.junit.Test;

/**
 * @author lixf
 *
 */
public class TestSentence {
	private static Log log = LogFactory.getLog(TestSentence.class);
	@Test
	public void testText() throws IOException {
		Detector detector = new SimpleDetector();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("sentences.txt");
		Reader paragraph = new InputStreamReader(is);
		Iterator<Sentence> sentences = detector.detect(paragraph);
		while(sentences.hasNext()){
			Sentence sentence = sentences.next();
			log.info(sentence.getStartOffset()+"-"+sentence.getEndOffset()+":"+ sentence.toString());
		}
		is.close();
	}
}
