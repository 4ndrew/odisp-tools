package com.novel.tools.multimap;

import java.util.Iterator;

import junit.framework.TestCase;

/** Набор тестов для MultiMap.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: TestMultiMap.java,v 1.2 2005/07/11 16:08:30 valeks Exp $
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
