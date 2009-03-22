/* ODISP -- Message Oriented Middleware
 * Copyright (C) 2003-2005 Valentin A. Alekseev
 * Copyright (C) 2003-2005 Andrew A. Porohin 
 * 
 * ODISP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 2.1 of the License.
 * 
 * ODISP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ODISP.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/** �������� ����� �������������� ������ MultiMap.
 * <p>MultiMap ������������ ��� �������� ������ � ����������� ������ �� ����� �� ������ �������.
 * ���������� �������� � ������ ������ ������� �� ����������� ������ ���������. 
 * ������ �� ������ ���� �� ������, ��� ���������� �������� ��������, �������
 * ���c��� ��� ��������������� ������.</p>
 * 
 * <p>������ ������ ������� - �������������� MultiMapElement, ������� ��������� ����� ArrayList
 * �������� ������ � ����� ��������� ���������� � ������������ �� ����������� �������� �� ��������.</p>
 * @see java.util.Map  
 * @see com.novel.tools.multimap.MultiMapElement
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MultiMap.java,v 1.3 2005/07/22 15:34:33 valeks Exp $
 */
public class MultiMap {
	/** ��������� ��� �����. */
	private List contents = new ArrayList();
	/** ���������� �������� ��������. */
	private int keyColumnCount;
	
	/** �������� ������� � �������� ���-��� �������� ��������.
	 * @param _keyColumnCount ���-�� �������� ��������
	 */
	public MultiMap(int _keyColumnCount) {
		keyColumnCount = _keyColumnCount;
	}
	
	/** ��������� ������ ������� �� ����� �� ��������� ��������� �������.
	 * @param column ����� ��������� �������
	 * @param key �������� ��������� ����
	 * @return ������ �� ���������� ������ ��������� ������ ��� null ���� �� ����� ������ �� �������
	 * @throws AssertionError � ������ ���� ����� ��������� ������� ���������
	 *  �������� ��� �������� ���-�� �������� ��������.
	 */
	public MultiMapElement get(int column, final Object key) {
		assert column < keyColumnCount : "Key column index exceeds stored ones.";
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
		assert element.size() >= keyColumnCount : "Column count less than expected."; 
		contents.add(element);
	}
	
	/** ���������� ����� ������ � ������� � ������� ţ ��� ��������������.
	 * ���������� �������� ���������� ��������������� ������ ���������� �������� ��������.
	 * @return ����� ������ ����� ���������� � �������
	 */
	public MultiMapElement addRow() {
		MultiMapElement mme = new MultiMapElement(keyColumnCount);
		put(mme);
		return mme;
	}
	
	public String toString() {
		String result = "{";
		Iterator it = iterator();
		while (it.hasNext()) {
			MultiMapElement element = (MultiMapElement) it.next();
			result += element.toString() + (it.hasNext() ? ", " : "");
		}
		return result + "}";
	}
	
	/** ���������� ����� � �������.
	 * @return ���-�� ����� � �������
	 */
	public int size() {
		return contents.size();
	}
	
	/** ���������� �������� ��������� �������.
	 * @return ���������� �������� ��������� � ������� �������
	 */
	public int getKeyColumnCount() {
		return keyColumnCount;
	}
	
	/** ��������� ����� ��� ������� � ����������� ������� ��������.
	 * @return ���������� �������
	 */
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
		/** ����������� �������. */
		private MultiMap mm;
		/** ������� ������. */
		private int idx = 0;
		/** ���������� ������. */
		private int lastIdx = 0;
		/** �������� ��������� ��� �������� �������.
		 * @param _mm ������� ��� ������� ����c��� ��������
		 */
		MMIterator(MultiMap _mm) {
			mm = _mm;
		}
		
		/** �������� �� ������������� ���������� ��������.
		 * @return ������� ������������� ���������� ��������
		 */
		public boolean hasNext() {
			return idx < mm.size();
		}

		/** ������ ��������� ������.
		 * @return ��������� ������ � �������
		 */
		public Object next() {
			lastIdx = idx;
			return (Object) mm.row(idx++);
		}

		/** �������� ������� ������. */
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
