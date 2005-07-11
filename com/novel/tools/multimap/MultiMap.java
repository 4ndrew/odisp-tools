package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/** �������� ����� �������������� ������ MultiMap.
 * <p>MultiMap ������������ ��� �������� ������ � ����������� ������ �� ����� �� ������ �������.
 * ���������� �������� � ������ ������ ������� �� ����������� ������ ���������. 
 * ������ �� ������ ���� �� ������, ��� ���������� �������� ��������, �������
 * �������� ��� ��������������� ������.</p>
 * 
 * <p>������ ������ ������� - ������ ��������� �� ������������ ���������.
 * ��� �������� ������ ������̣� ����� MultiMapElement, ������� ����� �����ݣ���� ����������
 * �������� ����� ������������ �������.</p>
 * @see java.util.Map  
 * @see com.novel.tools.multimap.MultiMapElement
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MultiMap.java,v 1.1 2005/07/11 15:29:50 valeks Exp $
 */
public class MultiMap {
	/** ��������� ��� ���������. */
	private List contents = new ArrayList();
	/** ���������� �������� ��������. */
	private int columns;
	
	/** �������� ������� � �������� ���-��� �������� ��������.
	 * @param _columns ���-�� �������� ��������
	 */
	public MultiMap(int _columns) {
		columns = _columns;
	}
	
	/** ��������� ������ ������� �� ����� �� ��������� ��������� �������.
	 * @param column ����� ��������� �������
	 * @param key �������� ��������� ����
	 * @return ������ �� ���������� ������ ��������� ������ ��� null ���� �� ����� ������ �� �������
	 * @throws AssertionError � ������ ���� ����� ��������� ������� ���������
	 *  �������� ��� �������� ���-�� �������� ��������.
	 */
	public MultiMapElement get(int column, final Object key) {
		assert column < columns : "Key column index exceeds stored ones.";
		MultiMapElement result = null;
		Iterator it = contents.iterator();
		while (it.hasNext()) {
			MultiMapElement item = (MultiMapElement) it.next();
			if (item.get(column).equals(key)) {
				result = item;
				break;
			}
		}
		return result;
	}
	
	/** �������� ������ ������� �� ţ �������.
	 * @param idx ������ ������
	 * @return ������ �� ���������� ������ �� ��������� ������� 
	 * @throws IndexOutOfBoundsException � ������ ���� ������ �� �������� � �������: 0 <= idx < size
	 */
	public MultiMapElement row(int idx) throws IndexOutOfBoundsException {
		return (MultiMapElement) contents.get(idx);
	}
	
	/** �������� ������ � �������� ��������.
	 * @param idx ������ ������
	 * @throws IndexOutOfBoundsException � ������ ���� ������ �� �������� � �������: 0 <= idx < size
	 */
	public void remove(int idx) throws IndexOutOfBoundsException {
		contents.remove(idx);
	}
	
	/** ������� ������ �������� � �������� ������ �������.
	 * @param element ������ ��������
	 * @throws AssertionError ���� ���������� ��������� � ������ �������� ������ 
	 * ��� �������� ���������� �������� ��������
	 */
	public void put(MultiMapElement element) {
		assert element.size() >= columns : "Column count less than expected."; 
		contents.add(element);
	}
	
	/** ���������� ����� ������ � ������� � ������� ţ ��� ��������������. */
	public MultiMapElement addRow() {
		MultiMapElement mme = new MultiMapElement(columns);
		put(mme);
		return mme;
	}
	
	/** ���������� ����� � �������.
	 * @return ���-�� ����� � �������
	 */
	public int size() {
		return contents.size();
	}
	
	/** ��������� ����� ��� ������� � ����������� ������� �������� */
	List getContents() {
		return contents;
	}
	
	/** ��������� ��������� �� ������� �������.
	 * �������� ������������ �������� ��������.
	 * @return �������� �� ������� 
	 */
	public Iterator iterator() {
		return new MMIterator(this);
	}
	
	/** ��������� ��������� �� ������� �������.
	 * @return �������� �� ������� 
	 */
	public Enumeration rows() {
		return new MMEnumeration(this);
	}
	
	/** ���������� ����� ��� ��������� ���������. */
	private class MMIterator implements Iterator {
		private MultiMap mm;
		private int idx = 0;
		private int lastIdx = 0;
		MMIterator(MultiMap _mm) {
			mm = _mm;
		}
		
		public boolean hasNext() {
			return idx < mm.size();
		}

		public Object next() {
			lastIdx = idx;
			return (Object) mm.row(idx++);
		}

		public void remove() {
			mm.remove(lastIdx);
		}
	}
	
	/** ���������� ����� ��� ��������� Enumeration */
	private class MMEnumeration extends MMIterator implements Enumeration {
		MMEnumeration(MultiMap _mm) {
			super(_mm);
		}

		public boolean hasMoreElements() {
			return super.hasNext();
		}

		public Object nextElement() {
			return super.next();
		}
	}
}
