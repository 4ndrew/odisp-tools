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
package org.valabs.tools;

import java.util.Iterator;

import org.valabs.tools.multimap.MultiMap;
import org.valabs.tools.multimap.MultiMapElement;


import junit.framework.TestCase;

/** Набор тестов для MultiMap.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: TestMultiMap.java,v 1.1 2005/09/10 13:20:07 dron Exp $
 */
public class TestMultiMap extends TestCase {

	public void testGet() {
		MultiMap mm = new MultiMap(4);
		mm.put(new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
		mm.put(new MultiMapElement().c("Andrew").c("Porohin").c("dron").c(new Integer(234)));
		assertEquals(mm.get(2, "valeks"), new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
	}

	public void testPut() {
		MultiMap mm = new MultiMap(4);
		mm.put(new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
		mm.put(new MultiMapElement().c("Andrew").c("Porohin").c("dron").c(new Integer(234)));
		assertEquals(mm.size(), 2);
		mm.put(new MultiMapElement().c("Vasily").c("Pupkin").c("vpupkin").c(new Integer(345)).c(new Boolean(false)));
		assertEquals(mm.size(), 3);
	}

	public void testIterator() {
		MultiMap mm = new MultiMap(4);
		mm.put(new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
		mm.put(new MultiMapElement().c("Andrew").c("Porohin").c("dron").c(new Integer(234)));
		Iterator it = mm.iterator();
		assertEquals(((MultiMapElement) it.next()).get(0), "Valentin");
		assertEquals(((MultiMapElement) it.next()).get(0), "Andrew");
		assertFalse(it.hasNext());
		it.remove();
		assertEquals(mm.size(), 1);
	}
	
	public void testAddElement() {
		MultiMap mm = new MultiMap(4);
		mm.addRow().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123));
		mm.addRow().c("Andrew").c("Porohin").c("dron").c(new Integer(234));
		assertEquals(mm.size(), 2);
		Iterator it = mm.iterator();
		assertEquals(((MultiMapElement) it.next()).get(0), "Valentin");
		assertEquals(((MultiMapElement) it.next()).get(0), "Andrew");
		assertFalse(it.hasNext());
		mm.addRow().c("Vasily").c("Pupkin").c("vpupkin").c(new Integer(345)).c(new Boolean(false));
		assertEquals(mm.size(), 3);		
	}
	
	public void testToString() {
		MultiMap mm = new MultiMap(4);
		mm.addRow().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123));
		mm.addRow().c("Andrew").c("Porohin").c("dron").c(new Integer(234));
		assertEquals(mm.toString(), "{[Valentin, Alekseev, valeks, 123], [Andrew, Porohin, dron, 234]}");
	}
}
