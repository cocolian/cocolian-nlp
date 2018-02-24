/**
 * 
 */
package org.cocolian.nlp.pos.chmm.term;

import org.cocolian.nlp.pos.chmm.POSTerm;
import org.cocolian.nlp.pos.chmm.TermEdge;
import org.jgrapht.EdgeFactory;

/**
 * @author lixf
 * 
 */
public class TermEdgeFactory implements EdgeFactory<POSTerm, TermEdge> {
	public TermEdgeFactory() {
	}

	@Override
	public TermEdge createEdge(POSTerm sourceVertex, POSTerm targetVertex) {
		return new DefaultTermEdge();
	}

}
