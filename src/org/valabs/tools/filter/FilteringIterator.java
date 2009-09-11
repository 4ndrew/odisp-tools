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

import java.util.Iterator;

/** Итератор с фильтрацией значений.
 * Реализация "жадная". При создании и после каждой операции производится поиск
 * следующего значения подпадающего под фильтр значения.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: FilteringIterator.java,v 1.2 2005/10/20 13:02:46 valeks Exp $
 */
public class FilteringIterator implements Iterator {
  /** Текущее значение. */
  private Object currentValue;
  /** Реальный итератор. */
	private Iterator realIt;
  /** Фильтр. */
  private Filter filter;
	
  /** Создать фильтрующий итератор для заданного базового на основе данного фильтра.
   * @param _realIt базовый итератор
   * @param _filter фильтр
   */
	public FilteringIterator(final Iterator _realIt, final Filter _filter) {
    filter = _filter;
    realIt = _realIt;
	  findNextValue();
  }
  
  /** Поиск следующего значения. */
  private void findNextValue() {
    do {
    currentValue = realIt.next();
    } while (!filter.accept(currentValue) && realIt.hasNext());
  }
  
	/** Проверка существования следующего значения.
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
	  return currentValue != null && filter.accept(currentValue);
	}

	/** Получение следующего значения из итератора.
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
	  Object oldCurrentValue = currentValue;
    findNextValue();
    return oldCurrentValue;
	}

	/** Удаление текущего значения из итератора.
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		realIt.remove();
    findNextValue();
	}

}
