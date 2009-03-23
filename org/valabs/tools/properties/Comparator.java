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
package org.valabs.tools.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

/** Сравнение файлов языковой поддержки на предмет полноты набора ключей
 * перевода.
 * 
 * @author <a href="dron@novel-il.ru">Your_Realname</a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: Comparator.java,v 1.3 2004/11/01 11:04:19 dron Exp $
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
