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

/** ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @author <a href="mailto:boris@novel-il.ru">����������� ����� �. </a>
 * @version $Id: TplParser.java,v 1.20 2005/02/03 12:40:26 valeks Exp $
 */

public class TplParser {
  /** ������������ ��� ������ ������ �������� ������ java-������. */
  private boolean cleanOnly = false;
  
  /** ��������� �� ����� �� ����� Java. */
  private boolean doJava = true;

  /** �ޣ���� ������������ ������. */
  private int countProcessed = 0;
  /** �ޣ���� ����������� ������ ��-�� ����, ��� �� up-to-date. */
  private int countSkipped = 0;
  /** �ޣ���� ������. */
  private int countError = 0;
  
  private List writers = new ArrayList();

  /**
   * ������� ����� �������
   * 
   * @param args ��������� ������
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
    if (doJava) {
      writers.add(new MessageFile_Java());
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
  private void createOutput(TplFile source) throws IOException, FileNotFoundException {
    Iterator it = writers.iterator();
    while (it.hasNext()) {
      MessageFile writer = (MessageFile) it.next();
      String outputFileName = source.getFileName().replaceAll(".tpl$", writer.getExtension());
      File javaFile = new File(outputFileName);
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
        writer.writeFile(source, new FileOutputStream(outputFileName));
        if (source.getErrorMessage() != null) {
          writer.writeFile(source.getErrorMessage(), new FileOutputStream(source.getErrorMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        if (source.getReplyMessage() != null) {
          writer.writeFile(source.getReplyMessage(), new FileOutputStream(source.getReplyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        if (source.getNotifyMessage() != null) {
          writer.writeFile(source.getNotifyMessage(), new FileOutputStream(source.getNotifyMessage().getFileName().replaceAll(".tpl$", writer.getExtension())));
        }
        System.out.println();
      } else {
        System.out.println();
        countSkipped++;
      }
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
   * ����������� ����� ������ �� �����������
   * 
   * @param f ��������� �� ���������� ��� ������
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
