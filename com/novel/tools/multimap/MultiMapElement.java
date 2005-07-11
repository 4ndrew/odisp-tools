package com.novel.tools.multimap;

import java.util.List;
import java.util.ArrayList;

/** ������� ������� MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MultiMapElement.java,v 1.1 2005/07/11 15:29:50 valeks Exp $
 */
public class MultiMapElement extends ArrayList {
	/** ����������� ��-���������. */
	public MultiMapElement() {
		super();
	}
	
	/** ����������� ��� ��������� ������������ ���-�� ���������.
	 * @param cols ����������� ���-�� ��������� 
	 */
	public MultiMapElement(int cols) {
		super(cols);
	}
	
	/** �������� ������ �� ������ ������ ��������.
	 * @param elements ������ ��������
	 */
	public MultiMapElement(List elements) {
		addAll(elements);
	}
	
	/** �������� ������ �� ������ ������� ��������.
	 * @param elements ������ ���������
	 */
	public MultiMapElement(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			add(elements[i]);
		}
	}
	
	/** ������� �������� �� ��������� �������.
	 * @param column ����� �������
	 * @param value �������� �� �������
	 * @see java.util.List#add(int, java.lang.Object)
	 * @return ������ �� ����Σ���� �������
	 */
	public MultiMapElement c(int column, Object value) {
		add(column, value);
		return this;
	}
	
	/** ������� �������� �� ��������� �� ������� �������.
	 * @param value �������� �� �������
	 * @see java.util.List#add(java.lang.Object)
	 * @return ������ �� ����Σ���� �������
	 */
	public MultiMapElement c(Object value) {
		add(value);
		return this;
	}
}
