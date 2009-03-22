package test.com.novel.tools;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.novel.tools.filter.Filter;
import com.novel.tools.filter.FilteringMap;

/** ����� ��� ������������ �������.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: TestFilteringMap.java,v 1.1 2005/09/10 13:20:07 dron Exp $
 */
public class TestFilteringMap extends TestCase {
	private Map backend = new HashMap();
	private Filter moreThan3 = new Filter() {
		public boolean accept(final Object obj) {
			boolean result = false;
			if (obj instanceof String) {
				String objStr = (String) obj;
				result = Integer.parseInt(objStr) > 3;
			}
			return result;
		}
		};

	public void setUp() {
		backend.put("1", "6");
		backend.put("2", "5");
		backend.put("3", "4");
		backend.put("4", "3");
		backend.put("5", "2");
		backend.put("6", "1");
	}

	public void testSize() {
		// ����������� �� �����
		assertEquals(new FilteringMap(backend, moreThan3).size(), 3);
		// �� ��������
		assertEquals(new FilteringMap(backend, Filter.ALWAYS_TRUE, moreThan3).size(), 3);
		// �� ����� � ��������
		assertEquals(new FilteringMap(backend, moreThan3, moreThan3).size(), 0);
	}

	public void testContainsKey() {
		// ����������� �� ������ � �������� 2
		assertFalse(new FilteringMap(backend, moreThan3).containsKey("2"));
		// ����������� �� ��������� � �������� ���� 4
		assertFalse(new FilteringMap(backend, Filter.ALWAYS_TRUE, moreThan3).containsKey("4"));
	}

	public void testContainsValue() {
		// �� ������
		assertTrue(new FilteringMap(backend, moreThan3).containsValue("5"));
		// �� ���������
		assertFalse(new FilteringMap(backend, Filter.ALWAYS_TRUE, moreThan3).containsValue("2"));
	}

	public void testGet() {
		// �� ������
		assertNull(new FilteringMap(backend, moreThan3).get("1"));
		// �� ���������
		assertNull(new FilteringMap(backend, Filter.ALWAYS_TRUE, moreThan3).get("6"));
	}

	public void testPut() {
		FilteringMap m = new FilteringMap(backend, moreThan3);
		m.put("0", "10"); // ������ ��������� � backend, �� �� ������������ ����� ������
		assertTrue(backend.containsKey("0"));
		assertFalse(m.containsKey("0"));
		backend.remove("0");
	}
}
