package com.novel.tools.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

/** TODO: insert comment there
 * 
 * @author <a href="dron@novel-il.ru">Your_Realname</a>
 * @author (C) 2004 îðð "îÏ×ÅÌ-éì"
 * @version $Id: Comparator.java,v 1.2 2004/09/16 16:02:46 dron Exp $
 */
public class Comparator {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("use: Comparator <input1> <input2>");
      return;
    }
    
    System.out.println("Input file1:\n" + args[0]);
    System.out.println("Input file2:\n" + args[1]);
    
    Properties prop1 = new Properties();
    Properties prop2 = new Properties();
    try {
      prop1.load(new FileInputStream(new File(args[0])));
    	prop2.load(new FileInputStream(new File(args[1])));
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return;
    }
    
    Iterator it = prop1.keySet().iterator();
    System.out.println("=== contains(file1) && !contains(file2)");
    while (it.hasNext()) {
      String buff = (String) it.next();
      if (!prop2.containsKey(buff)) {
        System.out.println("  key: " + buff);
      }
    }
    System.out.println("=======================================");
    it = prop2.keySet().iterator();
    System.out.println("=== !contains(file1) && contains(file2)");
    while (it.hasNext()) {
      String buff = (String) it.next();
      if (!prop1.containsKey(buff)) {
        System.out.println("  key: " + buff);
      }
    }
    System.out.println("=======================================");
  }
}
