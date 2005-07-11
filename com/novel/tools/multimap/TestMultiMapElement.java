package com.novel.tools.multimap;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/** Набор тестов для проверки класса описывающего строку таблицы.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: TestMultiMapElement.java,v 1.1 2005/07/11 16:08:54 valeks Exp $
 */
public class TestMultiMapElement extends TestCase {
	public void testMultiMapElementList() {
		List elements = new ArrayList(4);
		elements.add("Valentin");
		elements.add("Alekseev");
		elements.add("valeks");
		elements.add(new Integer(123));
		assertEquals(new MultiMapElement(elements), new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
	}

	public void testMultiMapElementObjectArray() {
		assertEquals(new MultiMapElement(new Object[] {"Valentin", "Alekseev", "valeks", new Integer(123)}), new MultiMapElement().c("Valentin").c("Alekseev").c("valeks").c(new Integer(123)));
	}

	public void testCintObject() {
		List testl = new ArrayList();
		testl.add(0, "test");
		assertEquals(new MultiMapElement().c(0, "test"), testl);
	}

	public void testCObject() {
		List testl = new ArrayList();
		testl.add("test");
		assertEquals(new MultiMapElement().c("test"), testl);
	}
	
	public void testCObjectArray() {
		List testl = new ArrayList();
		testl.add("a");
		testl.add("test");
		assertEquals(new MultiMapElement().c(new Object[] {"a", "test"}), testl);		
	}

	public void testCCollection() {
		List testl = new ArrayList();
		testl.add("a");
		testl.add("test");
		assertEquals(new MultiMapElement().c(testl), testl);		
	}
	
	public void testToString() {
		assertEquals(new MultiMapElement(new Object[] {"Valentin", "Alekseev", "valeks", new Integer(123)}).toString(), "[Valentin, Alekseev, valeks, 123]");
	}
}
