/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cocolian.nlp.Tokenizer;
import org.cocolian.nlp.pos.chmm.HMMTokenizer;
import org.cocolian.nlp.sentence.Detector;
import org.cocolian.nlp.sentence.Sentence;
import org.cocolian.nlp.sentence.SimpleDetector;
import org.cocolian.nlp.summarization.SentenceWrapper;
import org.cocolian.nlp.summarization.Summarization;
import org.cocolian.nlp.summarization.TextRankSummarization;
import org.junit.Test;

/**
 * @author lixf
 *
 */
public class TestSummarization {
	private static Log log = LogFactory.getLog(TestSummarization.class);
	@Test
	public void testText() throws IOException {
		Tokenizer tokenizer = HMMTokenizer.newBuilder().build();
		Detector detector = new SimpleDetector();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("news2.txt");
		Reader paragraph = new InputStreamReader(is,"UTF-8");
		Summarization summarization = new TextRankSummarization(tokenizer, detector);
		for(Sentence sentence : summarization.summarize(paragraph, 4)){
			double score = ((SentenceWrapper)sentence).getScore();
			log.info("["+ score+"]"+ sentence);
		}
		is.close();
	}
}
