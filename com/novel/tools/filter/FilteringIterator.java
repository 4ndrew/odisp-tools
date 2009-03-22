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
