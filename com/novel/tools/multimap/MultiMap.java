package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/** Основной класс обеспечивающий работу MultiMap.
 * <p>MultiMap предназначен для хранения таблиц и обеспечения поиска по ключу из любого столбца.
 * Количество столбцов в каждой строке таблицы не обязательно должно совпадать. 
 * Однако их должно быть не меньше, чем количество ключевых столбцов, которое
 * задаётся при инстанцировании класса.</p>
 * 
 * <p>Каждая строка таблицы - экземпляркласс MultiMapElement, который расширяет класс ArrayList
 * добавляя методы с более короткими названиями и рассчитаными на стандартные операции со строками.</p>
 * @see java.util.Map  
 * @see com.novel.tools.multimap.MultiMapElement
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MultiMap.java,v 1.2 2005/07/11 16:07:15 valeks Exp $
 */
public class MultiMap {
	/** Хранилище для строк. */
	private List contents = new ArrayList();
	/** Количество ключевых столбцов. */
	private int keyColumnCount;
	
	/** Создание таблицы с заданным кол-вом ключевых столбцов.
	 * @param _keyColumnCount кол-во ключевых столбцов
	 */
	public MultiMap(int _keyColumnCount) {
		keyColumnCount = _keyColumnCount;
	}
	
	/** Получение строки таблицы по ключу из заданного ключевого столбца.
	 * @param column номер ключевого столбца
	 * @param key значение ключевого поля
	 * @return список со значениями первой найденной строки или null если ни одной строки не найдено
	 * @throws AssertionError в случае если номер ключевого столбца превышает
	 *  заданное при создание кол-во ключевых столбцов.
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
	
	/** Получить строку целиком по её индексу.
	 * @param idx индекс строки
	 * @return список со значениями строки по заданному индексу 
	 * @throws IndexOutOfBoundsException в случае если индекс не попадает в пределы: 0 <= idx < size
	 */
	public MultiMapElement row(int idx) throws IndexOutOfBoundsException {
		return (MultiMapElement) contents.get(idx);
	}
	
	/** Удаление строки с заданным индексом.
	 * @param idx индекс строки
	 * @throws IndexOutOfBoundsException в случае если индекс не попадает в пределы: 0 <= idx < size
	 */
	public void remove(int idx) throws IndexOutOfBoundsException {
		contents.remove(idx);
	}
	
	/** Занести список значений в качестве строки таблицы.
	 * @param element список значение
	 * @throws AssertionError если количество элементов в списке значений меньше 
	 * чем заданное количество ключевых столбцов
	 */
	public void put(MultiMapElement element) {
		assert element.size() >= keyColumnCount : "Column count less than expected."; 
		contents.add(element);
	}
	
	/** Добавление новой строки в таблицу и возврат её для редактирования.
	 * Количество столбцов изначально устанавливается равным количеству ключевых столбцов.
	 * @return новая строка после добавления в таблицу
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
	
	/** Количество строк в таблице.
	 * @return кол-во строк в таблице
	 */
	public int size() {
		return contents.size();
	}
	
	/** Количество ключевых элементов таблицы.
	 * @return количество ключевых элементов в строках таблицы
	 */
	public int getKeyColumnCount() {
		return keyColumnCount;
	}
	
	/** Внутрений метод для доступа к содержимому таблицы напрямую */
	List getContents() {
		return contents;
	}
	
	/** Получение итератора по строкам таблицы.
	 * Итератор поддерживает операцию удаления.
	 * @return итератор по строкам 
	 */
	public Iterator iterator() {
		return new MMIterator(this);
	}
	
	/** Получение итератора по строкам таблицы.
	 * @return итератор по строкам 
	 */
	public Enumeration rows() {
		return new MMEnumeration(this);
	}
	
	/** Внутренний класс для поддержки итератора. */
	private class MMIterator implements Iterator {
		/** Итерируемая таблица. */
		private MultiMap mm;
		/** Текущий индекс. */
		private int idx = 0;
		/** Предыдущий индекс. */
		private int lastIdx = 0;
		/** Создание итератора для заданной таблицы. */
		MMIterator(MultiMap _mm) {
			mm = _mm;
		}
		
		/** Проверка на существование следующего элемента. */
		public boolean hasNext() {
			return idx < mm.size();
		}

		/** Выдача следующей строки. */
		public Object next() {
			lastIdx = idx;
			return (Object) mm.row(idx++);
		}

		/** Удаление текущей строки. */
		public void remove() {
			mm.remove(lastIdx);
		}
	}
	
	/** Внутренний класс для поддержки Enumeration */
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
