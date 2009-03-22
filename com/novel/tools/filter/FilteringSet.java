package com.novel.tools.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/** ����������� Set.
 * @author valeks
 * @version $Id: FilteringSet.java,v 1.1 2005/04/27 14:01:34 valeks Exp $
 */
public class FilteringSet implements Set {
	private Set backend;
	private Filter filter;
	private int size;
	
	public FilteringSet(final Set _backend, final Filter _filter) {
		backend = _backend;
		filter = _filter;
	}
	
	private void setupSize() {
		size = 0;
		Iterator pairIt = backend.iterator();
		while (pairIt.hasNext()) {
			Object elt = pairIt.next();
			if (filter.accept(elt)) {
				size++;
			}
		}		
	}
	
	/**
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return size;
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return backend.isEmpty();
	}

	/**
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		boolean result = false;
		if (filter.accept(o)) {
			result = backend.contains(o);
		}
		return result;
	}

	/**
	 * @see java.util.Collection#iterator()
	 */
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return backend.iterator();
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return backend.toArray();
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return backend.toArray(a);
	}

	/**
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(Object o) {
		if (filter.accept(o) && !backend.contains(o)) {
			size++;
		}
		return backend.add(o);
	}

	/**
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		if (filter.accept(o) && backend.contains(o)) {
			size--;
		}
		return backend.remove(o);
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection c) {
		boolean result = true;
		Iterator colIt = c.iterator();
		while (colIt.hasNext()) {
			Object element = (Object) colIt.next();
			if (!filter.accept(element) || !backend.contains(element)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) {
		boolean result = false;
		Iterator colIt = c.iterator();
		while (colIt.hasNext()) {
			Object element = (Object) colIt.next();
			result = add(element);
		}
		return result;
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("retainAll not supported in FilteringSet");
//		return false;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		boolean result = false;
		Iterator colIt = c.iterator();
		while (colIt.hasNext()) {
			Object element = (Object) colIt.next();
			result = remove(element);
		}
		return result;
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		backend.clear();
		size = 0;
	}

}
