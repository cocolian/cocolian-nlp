/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
	@Test
	public void testText() throws IOException {
		File folder = new File("D:\\github\\jigsaw-nlp\\data\\pos");
		Tokenizer tokenizer = new HMMTokenizer(folder);
		Detector detector = new SimpleDetector();
		Reader paragraph = new FileReader("D:\\github\\jigsaw-nlp\\java\\algorithm\\test\\news2.txt");
		Summarization summarization = new TextRankSummarization(tokenizer, detector);
		for(Sentence sentence : summarization.summarize(paragraph, 4)){
			double score = ((SentenceWrapper)sentence).getScore();
			System.out.println("["+ score+"]"+ sentence);
		}
	}
}
