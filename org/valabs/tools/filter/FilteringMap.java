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
package org.valabs.tools.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** Реализация фильтрующей реализации Map.
 * Этот класс реализует лишь фильтрацию, реальный словарь остаётся
 * без изменения. Даже если объект не удовлетворяет фильтру при добавлении
 * он будет записан в реальный словарь. Фильтрация производится сначало по ключу,
 * а потом по значению (если указан соответствующий фильтр).
 * @author <a href="mailto:valeks@valabs.spb.ru">Валентин А. Алексеев</a>
 * @version $Id: FilteringMap.java,v 1.1 2005/04/27 14:01:34 valeks Exp $
 */
public class FilteringMap implements Map {
	private final Map backend;
	private Filter keyFilter;
	private Filter valueFilter;
	private int size;
	
	/** Создание фильтрующего словаря с заданным фильтром ключей.
	 * @param _backend реальный словарь
	 * @param _keyFilter фильтр ключей
	 */
	public FilteringMap(final Map _backend, final Filter _keyFilter) {
		backend = _backend;
		keyFilter = _keyFilter;
		setupSize();
	}
	
	public FilteringMap(final Map _backend, final Filter _keyFilter, final Filter _valueFilter) {
		backend = _backend;
		keyFilter = _keyFilter;
		valueFilter = _valueFilter;
		setupSize();
	}
	
	private void setupSize() {
		size = 0;
		Iterator pairIt = backend.entrySet().iterator();
		while (pairIt.hasNext()) {
			Map.Entry elt = (Map.Entry) pairIt.next();
			if (keyFilter.accept(elt.getKey()) && (valueFilter == null || valueFilter.accept(elt.getValue()))) {
				size++;
			}
		}		
	}
	
	/** Возвращает размер отфильтрованого словаря.
	 * @see java.util.Map#size()
	 */
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return backend.isEmpty();
	}

	public boolean containsKey(Object key) {
		boolean result = false;
		
		Object val = backend.get(key);
		if (keyFilter.accept(key) && (valueFilter == null || (val != null && valueFilter.accept(val)))) {
			result =backend.containsKey(key); 
		}
		return result;
	}

	/** Проверка на существование значения.
	 * В проверке фильтра участвует только фильтр на значение, так как ключ не известен.
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		boolean result = backend.containsValue(value);
		if (valueFilter != null && !valueFilter.accept(value)) {
			result = false;
		}
		return result;
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		Object result = backend.get(key);
		if (!keyFilter.accept(key) || (valueFilter != null && result != null && !valueFilter.accept(result))) {
			result = null;
		}
		return result;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		if (keyFilter.accept(key) && (valueFilter == null || valueFilter.accept(value))) {
			size++;
		}
		return backend.put(key, value);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		if (keyFilter.accept(key)) {
			size--;
		}
		return backend.remove(key);
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t) {
		Iterator pairIt = t.entrySet().iterator();
		while (pairIt.hasNext()) {
			Map.Entry elt = (Map.Entry) pairIt.next();
			put(elt.getKey(), elt.getValue());
		}
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		size = 0;
		backend.clear();
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		// TODO impl
		return backend.keySet();
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		// TODO impl
		return backend.values();
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		// TODO impl
		return backend.entrySet();
	}

}
