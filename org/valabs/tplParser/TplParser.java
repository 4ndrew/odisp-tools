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
 * @version $Id: TplParser.java,v 1.19 2005/02/02 20:22:28 valeks Exp $
 */

public class TplParser {
  /** Используется для режима только удаления файлов java-файлов. */
  private boolean cleanOnly = false;
  
  /** Создавать ли вывод на языке Java. */
  private boolean doJava = true;

  /** Счётчик обработанных файлов. */
  private int countProcessed = 0;
  /** Счётчик пропущенных файлов из-за того, что он up-to-date. */
  private int countSkipped = 0;
  /** Счётчик ошибок. */
  private int countError = 0;

  /**
   * Главный класс парсера
   * 
   * @param args параметры вызова
   */
  public static void main(String[] args) {
    boolean cleanOnly = false;
    List folders = new ArrayList(Arrays.asList(args));
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
    System.out.println("TPL parser started");
    cleanOnly = _cleanOnly;
    Iterator it = folders.iterator();
    while (it.hasNext()) {
      String folder = (String) it.next();
      System.out.println("Processing folder: " + folder);
      processFolder(folder);
    }
    System.out.println(this);
    System.out.println("TPL parser finished");
  }

  /**
   * 
   */
  private void processFolder(String folder) {
    File f = new File(folder);
    if (!f.exists()) {
      System.out.println("Folder does not exists: " + folder);
    }
    List tpls = listDir(f);
    Iterator it = tpls.iterator();
    while (it.hasNext()) {
      File tplFile = (File) it.next();
      try {
        TplFile source = new TplFile(tplFile);
        if (doJava) {
          createJava(source);
        }
      } catch (Exception e) {
        countError++;
        System.out.println("Message generation error: " + e.toString());
      }
    }
  }

  /**
   * @param tplFile
   * @param source
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void createJava(TplFile source) throws IOException, FileNotFoundException {
    File javaFile = new File(source.getFileName().replaceAll(".tpl$", ".java"));
    if (cleanOnly) {
      javaFile.delete();
      countProcessed++;
      System.out.println("Java file deleted: " + javaFile.getPath());
    } else if (javaFile.lastModified() <= source.lastModified()) {
      countProcessed++;
      new MessageFile_Java(source).writeFile(new FileOutputStream(javaFile));
      System.out.println();
    } else {
      countSkipped++;
    }
  }

  /**
   * @param newTplParser
   */
  public String toString() {
    if (cleanOnly) {
      return (countProcessed + countSkipped + countError) + " parsed, " + countProcessed
              + " deleted, " + countError + " errors.";
    } else {
      return (countProcessed + countSkipped + countError) + " parsed, " + countProcessed
              + " generated, " + countSkipped + " skipped due up-to-date, " + countError + " errors.";
    }
  }

  /**
   * Рекурсивный поиск файлов по директориям
   * 
   * @param f указатель на директорию где искать
   */
  private List listDir(File f) {
    List result = new ArrayList();
    File fileList[] = f.listFiles(new FileFilter() {
      public boolean accept(File f) {
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
