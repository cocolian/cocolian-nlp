/**
 * 
 */
package org.cocolian.nlp.pos.chmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cocolian.nlp.Term;
import org.cocolian.nlp.Tokenizer;
import org.cocolian.nlp.pos.chmm.corpus.CharDFACorpus;
import org.cocolian.nlp.pos.chmm.corpus.CharsetCorpus;
import org.cocolian.nlp.pos.chmm.corpus.CompanyNameLengthCorpus;
import org.cocolian.nlp.pos.chmm.corpus.CooccurrenceCorpus;
import org.cocolian.nlp.pos.chmm.corpus.NatureFreqCorpus;
import org.cocolian.nlp.pos.chmm.corpus.T2SCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.CharDFAFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.CharsetFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.CompanyNameLengthFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.CooccurrenceFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.NatureCooccurrenceFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.NatureFreqFileCorpus;
import org.cocolian.nlp.pos.chmm.corpus.file.T2SFileCorpus;
import org.cocolian.nlp.pos.chmm.npath.CooccurrenceNPathGenerator;
import org.cocolian.nlp.pos.chmm.recognitor.AsianNameRecognitor;
import org.cocolian.nlp.pos.chmm.recognitor.CompanyRecognitor;
import org.cocolian.nlp.pos.chmm.recognitor.DateTimeRecognitor;
import org.cocolian.nlp.pos.chmm.recognitor.ForeignNameRecognitor;
import org.cocolian.nlp.pos.chmm.recognitor.NatureRecognitor;
import org.cocolian.nlp.pos.chmm.recognitor.NumberRecognitor;
import org.cocolian.nlp.pos.chmm.scorer.SimplePathScorer;
import org.cocolian.nlp.pos.chmm.segmenter.CharTreeSegmenter;

/**
 * 、 基于层叠隐式马可夫模型的词性标注和分词工具。算法原理来自《基于层叠隐马模型的汉语词法分析》，刘群和张华平的论文。 原论文中基于隐马模型，引入CHMM
 * 统一建模, 该模型包含原子切分、普通未登录词识别、嵌套的复杂未登录词识别、基于类的隐马切分、词类标注共5 个层面的隐马模型, 处理流程如下： 1.
 * 粗分词，切分成已登录词， 2. 根据粗分词结果，形成 N条最短路径； 3. 对N条最短路径中的进行识别： 1. 数字 2. 人名 3. 地名 4. 机构名
 * 5. 时间/日期。每个识别可能会继续产生更多路径； 4. 从识别最终结果中选取最佳的标注路径
 * 
 * @author lixf
 * 
 */
public class HMMTokenizer implements Tokenizer {

	public static class Builder {
		private List<Recognitor> recognitors;
		private NPathGenerator npath;
		private Segmenter segmenter;
		private PathScorer scorer;

		public Builder() {
			this.recognitors = new ArrayList<Recognitor>();
		}

		public Builder npath(NPathGenerator npath) {
			this.npath = npath;
			return this;
		}

		public Builder segmenter(Segmenter segmenter) {
			this.segmenter = segmenter;
			return this;
		}

		public Builder pathScorer(PathScorer scorer) {
			this.scorer = scorer;
			return this;
		}

		public HMMTokenizer build() throws IOException {
			NatureFreqCorpus nff = new NatureFreqFileCorpus();
			CharDFACorpus charDFA = new CharDFAFileCorpus();
			T2SCorpus t2s = new T2SFileCorpus();
			CooccurrenceCorpus cooccurrence = new CooccurrenceFileCorpus();
			CharsetCorpus charset = new CharsetFileCorpus();
			if (this.scorer == null)
				this.scorer = new SimplePathScorer();
			if (this.segmenter == null)
				this.segmenter = new CharTreeSegmenter(charDFA, t2s);
			if (this.npath == null)
				this.npath = new CooccurrenceNPathGenerator(cooccurrence);

			this.recognitors.add(new NumberRecognitor(charset));
			this.recognitors.add(new AsianNameRecognitor(cooccurrence));
			CompanyNameLengthCorpus cnl = new CompanyNameLengthFileCorpus();
			this.recognitors.add(new CompanyRecognitor(cnl));
			this.recognitors.add(new ForeignNameRecognitor(charset, cooccurrence));
			this.recognitors.add(new DateTimeRecognitor(charset));
			this.recognitors.add(new NatureRecognitor(new NatureCooccurrenceFileCorpus()));
			return new HMMTokenizer(this);
		}
	}

	private Builder builder;

	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * 分词的类
	 * 
	 * @throws IOException
	 */
	private HMMTokenizer(Builder builder) {
		this.builder = builder;
	}

	/**
	 * 分句；按照，。和？分隔；
	 * 
	 * @param sentence
	 * @return
	 */
	@Override
	public List<Term> tokenize(String sentence) {
		// long startTime = System.currentTimeMillis();
		Pattern pattern = Pattern.compile("[，。？！　 ]");
		Matcher matcher = pattern.matcher(sentence);
		int start = 0;
		List<Term> result = new ArrayList<Term>();
		while (matcher.find(start)) {
			result.addAll(this.parse(sentence.substring(start, matcher.start() + 1)));
			start = matcher.start() + 1;
		}
		if (start < sentence.length() - 1) {
			result.addAll(this.parse(sentence.substring(start)));
		}
		// log.info("[" + (System.currentTimeMillis() - startTime) + "] :" +
		// result);
		return result;
	}

	public List<POSTerm> parse(String temp) {
		if (temp.trim().length() == 0)
			return new ArrayList<POSTerm>();
		// 初分词，生成 graph;
		TermGraph graph = builder.segmenter.segment(temp.trim());
		List<TermPath> pathes = builder.npath.process(graph);
		List<TermPath> generated = new ArrayList<TermPath>();
		for (Recognitor recognitor : builder.recognitors) {
			generated.clear();
			for (TermPath path : pathes) {
				generated.addAll(recognitor.process(path.toTermGraph()));
			}
			pathes.clear();
			pathes.addAll(generated);

		}

		TermPath shortest = null;
		double score = Double.MAX_VALUE;
		for (TermPath path : pathes) {
			double currentScore = builder.scorer.score(path);
			// log.info("candidate-[" + currentScore + "] :" + path);
			if (shortest == null || score > currentScore) {
				shortest = path;
				score = currentScore;

			}
		}
		// log.info("[" + score + "] :" + shortest);
		List<POSTerm> terms = new ArrayList<POSTerm>(shortest.getVertextList());
		terms.remove(terms.size() - 1);
		terms.remove(0);
		return terms;
	}

}
