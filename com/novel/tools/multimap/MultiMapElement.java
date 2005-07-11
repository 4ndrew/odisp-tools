package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** ������� ������� MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MultiMapElement.java,v 1.2 2005/07/11 16:08:15 valeks Exp $
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
	public MultiMapElement(Collection elements) {
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
	 * @see List#add(int, java.lang.Object)
	 * @return ������ �� ����Σ���� �������
	 */
	public MultiMapElement c(int column, Object value) {
		add(column, value);
		return this;
	}
	
	/** ������� �������� �� ��������� �� ������� �������.
	 * @param value �������� �� �������
	 * @see List#add(java.lang.Object)
	 * @return ������ �� ����Σ���� �������
	 */
	public MultiMapElement c(Object value) {
		add(value);
		return this;
	}
	
	/** ���������� ��������� �� �������.
	 * @param values ������ ��������
	 */
	public MultiMapElement c(Object[] values) {
		for (int i = 0; i < values.length; i++) {
			add(values[i]);
		}
		return this;
	}

	/** ���������� ��������� �� �������.
	 * @param values ������ ��������
	 * @see List#addAll(java.util.Collection)
	 */
	public MultiMapElement c(Collection values) {
		addAll(values);
		return this;
	}
}
