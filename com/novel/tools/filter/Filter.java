package com.novel.tools.filter;

/** ��������� ������� ����������� ����������.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �. ��������</a>
 * @version $Id: Filter.java,v 1.1 2005/04/27 14:01:34 valeks Exp $
 */
public interface Filter {
	boolean accept(Object obj);
	static Filter ALWAYS_TRUE = new Filter() {
		public boolean accept(Object obj) {
			return true;
		}
	};
}
