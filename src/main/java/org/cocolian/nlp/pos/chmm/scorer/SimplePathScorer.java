/**
 * 
 */
package org.cocolian.nlp.pos.chmm.scorer;

import org.cocolian.nlp.pos.chmm.PathScorer;
import org.cocolian.nlp.pos.chmm.TermPath;

/**
 * @author lixf
 *
 */
public class SimplePathScorer implements PathScorer{

	@Override
	public double score(TermPath path) {		
		return path.getEdgeList().size();
	}


}
