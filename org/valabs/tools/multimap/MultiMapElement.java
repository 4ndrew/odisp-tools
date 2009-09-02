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
package org.valabs.tools.multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Элемент таблицы MultiMap. 
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MultiMapElement.java,v 1.3 2005/07/22 15:34:33 valeks Exp $
 */
public class MultiMapElement extends ArrayList {
  private static final long serialVersionUID = -41285914256284609L;

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
	 * @return ссылка на текущую строку
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
	 * @return ссылка на текущую строку
	 */
	public MultiMapElement c(Collection values) {
		addAll(values);
		return this;
	}
}
