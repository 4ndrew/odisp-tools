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
package com.novel.tools.filter;

import java.util.Iterator;

/** �������� � ����������� ��������.
 * ���������� "������". ��� �������� � ����� ������ �������� ������������ �����
 * ���������� �������� ������������ ��� ������ ��������.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: FilteringIterator.java,v 1.2 2005/10/20 13:02:46 valeks Exp $
 */
public class FilteringIterator implements Iterator {
  /** ������� ��������. */
  private Object currentValue;
  /** �������� ��������. */
	private Iterator realIt;
  /** ������. */
  private Filter filter;
	
  /** ������� ����������� �������� ��� ��������� �������� �� ������ ������� �������.
   * @param _realIt ������� ��������
   * @param _filter ������
   */
	public FilteringIterator(final Iterator _realIt, final Filter _filter) {
    filter = _filter;
    realIt = _realIt;
	  findNextValue();
  }
  
  /** ����� ���������� ��������. */
  private void findNextValue() {
    do {
    currentValue = realIt.next();
    } while (!filter.accept(currentValue) && realIt.hasNext());
  }
  
	/** �������� ������������� ���������� ��������.
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
	  return currentValue != null && filter.accept(currentValue);
	}

	/** ��������� ���������� �������� �� ���������.
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
	  Object oldCurrentValue = currentValue;
    findNextValue();
    return oldCurrentValue;
	}

	/** �������� �������� �������� �� ���������.
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		realIt.remove();
    findNextValue();
	}

}
