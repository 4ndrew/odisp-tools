package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Элемент таблицы MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MultiMapElement.java,v 1.2 2005/07/11 16:08:15 valeks Exp $
 */
public class MultiMapElement extends ArrayList {
	/** Конструктор по-умолчанию. */
	public MultiMapElement() {
		super();
	}
	
	/** Конструктор для заданного минимального кол-ва элементов.
	 * @param cols минимальное кол-во элементов 
	 */
	public MultiMapElement(int cols) {
		super(cols);
	}
	
	/** Создание строки на основе списка значений.
	 * @param elements список значений
	 */
	public MultiMapElement(Collection elements) {
		addAll(elements);
	}
	
	/** Создание строки на основе массива значение.
	 * @param elements массив элементов
	 */
	public MultiMapElement(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			add(elements[i]);
		}
	}
	
	/** Задание значение на указанном столбце.
	 * @param column номер столбца
	 * @param value значение на столбце
	 * @see List#add(int, java.lang.Object)
	 * @return ссылка на изменённый элемент
	 */
	public MultiMapElement c(int column, Object value) {
		add(column, value);
		return this;
	}
	
	/** Задание значение на следующем по порядку столбце.
	 * @param value значение на столбце
	 * @see List#add(java.lang.Object)
	 * @return ссылка на изменённый элемент
	 */
	public MultiMapElement c(Object value) {
		add(value);
		return this;
	}
	
	/** Добавление элементов из массива.
	 * @param values массив значений
	 */
	public MultiMapElement c(Object[] values) {
		for (int i = 0; i < values.length; i++) {
			add(values[i]);
		}
		return this;
	}

	/** Добавление элементов из массива.
	 * @param values массив значений
	 * @see List#addAll(java.util.Collection)
	 */
	public MultiMapElement c(Collection values) {
		addAll(values);
		return this;
	}
}
