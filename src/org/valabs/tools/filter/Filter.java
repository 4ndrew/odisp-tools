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

/** Интерфейс классов реализующих фильтрацию.
 * @author <a href="mailto:valeks@valabs.spb.ru">Валентин А. Алексеев</a>
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
