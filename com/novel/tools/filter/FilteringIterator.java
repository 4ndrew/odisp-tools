package com.novel.tools.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author valeks
 * @version $Id: FilteringIterator.java,v 1.1 2005/04/27 14:01:34 valeks Exp $
 */
public class FilteringIterator implements Iterator {
	private Iterator realIt;
	
	public FilteringIterator(final Iterator _realIt, final Filter _filter) {
		final List objectRefs = new ArrayList(); 
		while (_realIt.hasNext()) {
			Object element = (Object) _realIt.next();
			if (_filter.accept(element)) {
				objectRefs.add(element);
			}
		}
		realIt = objectRefs.iterator();
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return realIt.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		return realIt.next();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		realIt.remove();
	}

}
