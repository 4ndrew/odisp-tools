package org.valabs.tools;

import java.util.ArrayList;
import java.util.List;

/** Тестовый класс, который реализует сортировку файла classes.cfg согласно
 * зависимостям.
 * 
 * Правила сортировки:
 * <ul>
 * <li> Объекты без зависемостей перемещаются вверх списка и отмечаются как
 * корректные.
 * <li> Объекты с зависемостями, которые не удовлетворены втавке в новый
 * список не поджеат
 * <li> Объекты с зависемостями и с 
 * </ul>
 * 
 * @author <a href="mailto:andrew.porokhin@gmail.com">Andrew A. Porokhin</a>
 * @author <a href="mailto:valeks@valabs.spb.ru">Valentin A. Alekseev</a>
 * @version $Id: ObjectSorter.java,v 1.2 2005/07/22 15:04:32 dron Exp $
 */
public class ObjectSorter {
  
  private static Boolean visited[];

  /** O(n^2)
   */  
  public static boolean contains(String args[], String keys[]) {
    for (int i = 0; i < args.length; i++) {
      for (int j = 0; j < keys.length; j++)
        if (args[i].equals(keys[j]))
          return true;
    }
    return false;
  }
  
  public static int getFirstDepend(ObjectItem object, List objs) {
    for (int i = 0; i < objs.size(); i++) {
      ObjectItem oi = (ObjectItem) objs.get(i);
      if (contains(oi.depends, object.providing))
        return i; 
    }
    return -1;
  }
  
  public static int getLastRequire(ObjectItem object, List objs) {
    int depends = object.depends.length;
    for (int i = 0; i < objs.size(); i++) {
      ObjectItem oi = (ObjectItem) objs.get(i);
      if (contains(object.providing, oi.depends))
        depends--;
      if (depends == 0)
        return i;
    }
    return -1;
  }
  
  public static List sortObjects_2(List objs) {
    List list = new ArrayList();

    visited = new Boolean[objs.size()];

    for (int i = 0; i < objs.size(); i++) {
      visit(i, list, objs);
    }

    visited = null;

    return list; 
  }
  
  private static int indexOf(List objs, String name) { 
    for (int i = 0; i < objs.size(); i++) {
      ObjectItem oi = (ObjectItem) objs.get(i);
      for (int j = 0; j < oi.providing.length; j++)
        if (oi.providing[j].equals(name))
          return i;
    }
    return -1;
  }
  
  private static void visit(int index, List newList, List objs) {
    if (visited[index] != null && visited[index].booleanValue()) {
      return;
    }
    
    visited[index] = new Boolean(true);
    ObjectItem oi = (ObjectItem) objs.get(index);
    for (int i = 0; i < oi.depends.length; i++) {
      visit(indexOf(objs, oi.depends[i]), newList, objs);
    }
    
    System.out.println(index);
    newList.add(objs.get(index));
  }

  public static void main(String[] args) {
    List objs = new ArrayList();
    ObjectItem oi = new ObjectItem();
    oi.providing = new String [] { "a" };
    oi.depends = new String [] { "b", "f" };
    oi.correct = false;
    objs.add(oi);
    
    oi = new ObjectItem();
    oi.providing = new String [] { "b" };
    oi.depends = new String [] { "e" };
    oi.correct = false;
    objs.add(oi);
    
    oi = new ObjectItem();
    oi.providing = new String [] { "c" };
    oi.depends = new String [] { "b" };
    oi.correct = false;
    objs.add(oi);
    
    oi = new ObjectItem();
    oi.providing = new String [] { "d" };
    oi.depends = new String [] { "c" };
    oi.correct = false;
    objs.add(oi);
    
    oi = new ObjectItem();
    oi.providing = new String [] { "e" };
    oi.depends = new String [] { };
    oi.correct = false;
    objs.add(oi);
    
    oi = new ObjectItem();
    oi.providing = new String [] { "f" };
    oi.depends = new String [] { "e" };
    oi.correct = false;
    objs.add(oi);
        
    List newList = sortObjects_2(objs);
    for (int i = 0; i < newList.size(); i++) {
      System.out.println(((ObjectItem) newList.get(i)).providing[0]);
    }
  }
}
