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
package org.valabs.tplParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/** Утилита для генерации классов сообщений ODISP на основе шаблонов.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @author <a href="mailto:boris@novel-il.ru">Волковыский Борис В. </a>
 * @version $Id: TplParser.java,v 1.27 2006/01/23 11:12:39 valeks Exp $
 */

public class TplParser {
  /** Используется для режима только удаления файлов java-файлов. */
  private final boolean cleanOnly;
  
  /** Создавать ли вывод на языке Java. */
  private boolean doJava = true;
  /** Создавать ли документацию на языке HTML. */
  private boolean doHTML = false;

  /** Счётчик обработанных файлов. */
  private int countProcessed = 0;
  /** Счётчик пропущенных файлов из-за того, что он up-to-date. */
  private int countSkipped = 0;
  /** Счётчик ошибок. */
  private int countError = 0;
  
  private List writers = new ArrayList();

  /**
   * Главный класс парсера
   * 
   * @param args параметры вызова
   */
  public static void main(String[] args) {
    boolean cleanOnly = false;
    final List folders = new ArrayList(Arrays.asList(args));
    if (args.length > 0 && args[0].equals("-clean")) {
      cleanOnly = true;
      folders.remove(0);
      System.out.println("Performing clean only");
    }
    if (folders.size() == 0) {
      folders.add(".");
    }
    new TplParser(cleanOnly, folders);
  }
  
  public TplParser(boolean _cleanOnly, List folders) {
    System.out.println("TPL parser started. Working folder is: " + new File(".").getAbsolutePath());
    cleanOnly = _cleanOnly;
    if (doJava) {
      writers.add(new MessageFile_Java());
    }
    
    if (doHTML) {
      writers.add(new MessageFile_HTML());
    }
    Iterator it = folders.iterator();
    while (it.hasNext()) {
      String folder = (String) it.next();
      System.out.println("Processing folder: " + folder);
      processFolder(folder);
    }
    System.out.println(this);
    it = writers.iterator();
    while (it.hasNext()) {
      MessageFile writer = (MessageFile) it.next();
      System.out.println(writer.toString());
    }
    System.out.println("TPL parser finished");
  }

  /**
   * 
   */
  private void processFolder(final String folder) {
    final File f = new File(folder);
    if (!f.exists()) {
      System.out.println("Folder does not exists: " + folder);
    }
    final List tpls = listDir(f);
    final Iterator it = tpls.iterator();
    while (it.hasNext()) {
      final File tplFile = (File) it.next();
      try {
        TplFile source = new TplFile(tplFile);
        createOutput(source);
      } catch (Exception e) {
        countError++;
        System.out.println("Message generation error: " + e.toString());
        e.printStackTrace();
      }
    }
  }

  /**
   * @param tplFile
   * @param source
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void createOutput(final TplFile source) throws IOException, FileNotFoundException {
    final Iterator it = writers.iterator();
    while (it.hasNext()) {
      final MessageFile writer = (MessageFile) it.next();
      final String extension = "(" + writer.getExtension() + ")";
      final String outputFileName = source.getFileName().replaceAll(".tpl$", writer.getExtension());
      final File javaFile = new File(outputFileName);
      if (cleanOnly) {
        javaFile.delete();
        System.out.println();
        System.out.println("File deleted: " + javaFile.getPath());
        if (source.getErrorMessage() != null) {
          new File(source.getErrorMessage().getFileName().replaceAll(".tpl$", writer.getExtension())).delete();
          System.out.println("File deleted: " + source.getErrorMessage().getFileName().replaceAll(".tpl$", writer.getExtension()));
        }
        if (source.getReplyMessage() != null) {
          new File(source.getReplyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())).delete();
          System.out.println("File deleted: " + source.getReplyMessage().getFileName().replaceAll(".tpl$", writer.getExtension()));
        }
        if (source.getNotifyMessage() != null) {
          new File(source.getNotifyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())).delete();
          System.out.println("File deleted: " + source.getNotifyMessage().getFileName().replaceAll(".tpl$", writer.getExtension()));
        }
        countProcessed++;
      } else if (javaFile.lastModified() <= source.lastModified()) {
        countProcessed++;
        
        StringBuffer l = new StringBuffer("TPL parsed: ").append(source.getFileName()).append("main").append(extension);
        writer.writeFile(source, new FileOutputStream(outputFileName));
        if (source.getReplyMessage() != null) {
          l.append("reply").append(extension);
          writer.writeFile(source.getReplyMessage(), new FileOutputStream(source.getReplyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        if (source.getErrorMessage() != null) {
          l.append("error").append(extension);
          writer.writeFile(source.getErrorMessage(), new FileOutputStream(source.getErrorMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        if (source.getNotifyMessage() != null) {
          l.append("notify").append(extension);
          writer.writeFile(source.getNotifyMessage(), new FileOutputStream(source.getNotifyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        System.out.println(l.toString());
        System.out.println();
      } else {
        System.out.println();
        countSkipped++;
      }
    }
  }

  /**
   * @return статистика работы парсера
   */
  public String toString() {
  	String result = (countProcessed + countSkipped + countError) + " parsed, " + countProcessed
    + " generated, " + countSkipped + " skipped due up-to-date, " + countError + " errors."; 
    if (cleanOnly) {
      result = (countProcessed + countSkipped + countError) + " parsed, " + countProcessed
              + " deleted, " + countError + " errors.";
    }
    return result;
  }

  /**
   * Рекурсивный поиск файлов по директориям
   * 
   * @param dirToCheck указатель на директорию где искать
   */
  private List listDir(final File dirToCheck) {
    final List result = new ArrayList();
    final File fileList[] = dirToCheck.listFiles(new FileFilter() {
      public boolean accept(final File f) {
        return f.isDirectory() || f.getName().endsWith(".tpl");
      }
    });
    for (int i = 0; i < fileList.length; i++) {
      if (fileList[i].isDirectory()) {
        result.addAll(listDir(fileList[i]));
      }else {
        result.add(fileList[i]);
      }
    }
    return result;
  }
}
