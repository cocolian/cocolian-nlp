/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
	@Test
	public void testText() throws IOException {
		File folder = new File("D:\\github\\jigsaw-nlp\\data\\pos");
		Tokenizer tokenizer = new HMMTokenizer(folder);
		Detector detector = new SimpleDetector();
		Reader paragraph = new FileReader("D:\\github\\jigsaw-nlp\\java\\algorithm\\test\\news2.txt");
		KeywordExtractor extractor = new TFIDFKeywordExtractor(tokenizer, detector);
		System.out.println();
		for(String  keyword: extractor.extract(paragraph, 8)){
			System.out.print(keyword+" ");
		}
		System.out.println();
	}
}
