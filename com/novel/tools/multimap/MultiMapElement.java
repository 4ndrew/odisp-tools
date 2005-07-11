package com.novel.tools.multimap;

import java.util.List;
import java.util.ArrayList;

/** Элемент таблицы MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MultiMapElement.java,v 1.1 2005/07/11 15:29:50 valeks Exp $
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
	public MultiMapElement(List elements) {
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
	 * @see java.util.List#add(int, java.lang.Object)
	 * @return ссылка на изменённый элемент
	 */
	public MultiMapElement c(int column, Object value) {
		add(column, value);
		return this;
	}
	
	/** Задание значение на следующем по порядку столбце.
	 * @param value значение на столбце
	 * @see java.util.List#add(java.lang.Object)
	 * @return ссылка на изменённый элемент
	 */
	public MultiMapElement c(Object value) {
		add(value);
		return this;
	}
}
