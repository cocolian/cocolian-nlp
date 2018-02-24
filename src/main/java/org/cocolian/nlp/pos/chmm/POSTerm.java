package org.cocolian.nlp.pos.chmm;

import org.cocolian.nlp.Nature;
import org.cocolian.nlp.Term;


/**
 * 在POS操作中使用到的Term
 * @author lixf
 * 
 */
public interface POSTerm extends Term {

	/**
	 * 获得这个term的所有词性
	 * 
	 * @return
	 */
	public TermNatures getTermNatures();

	/**
	 * 
	 * @param nature
	 */
	public void setNature(Nature nature);
	/**
	 * 
	 * 等同于setNature(Nature.valueOf(nature));
	 * @param nature
	 */
	public void setNature(String nature);

}