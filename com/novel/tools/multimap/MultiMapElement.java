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
import java.util.Collection;
import java.util.List;

/** ������� ������� MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MultiMapElement.java,v 1.3 2005/07/22 15:34:33 valeks Exp $
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
	 * @return ������ �� ������� ������
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
	 * @return ������ �� ������� ������
	 */
	public MultiMapElement c(Collection values) {
		addAll(values);
		return this;
	}
}
