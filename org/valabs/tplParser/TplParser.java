package org.valabs.tplParser;

import java.io.File;
import java.io.IOException;

/**
 * ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @version $Id: TplParser.java,v 1.18 2004/11/07 13:50:54 dron Exp $
 * 
 * ������ ��������:
 * 
 * NAME [�����] [��� ������] [�������� ODISP action] IMPORT [�����] (*)
 * 
 * AUTHOR [����� (��� ���� (a)author)] (*)
 * 
 * DESC [�������� ���������] (*) FIELD [��� ���� (� ��������� �����)] [��� ����]
 * 
 * FCHECK [��� ����] [��������� ��� �������� � boolean checkMessage()] (**)
 * 
 * FDESC [��� ����] [�������� ����] (*) DEFORIGIN [����� �����������
 * ��-���������]
 * 
 * DEFDEST [����� ���������� ��-���������]
 *
 * DEFROUTABLE [Routable ��-���������]
 * 
 * DEFREPLTO [����� ��������� �� ������� ������������ ����� �� ���������]
 * 
 * DEFOOB [OOB ��-���������]
 * 
 * VERBATIM (***)
 * 
 * ������ ��� ���� (a)version ������� �� CVS-���� Id.
 * 
 * (*) �������������� multiline comments, ��� ���� ������������, ������
 * ���������� � ������ ��������� �����. ��������: AUTHOR 1 ������ AUTHOR 2
 * ������
 * 
 * (**) �������� ��-��������� get[��� ����](msg) != null
 * 
 * (***) VERBATIM �������� ����� �������� ������ � �������������� ���������.
 * ����������� ��������� VERBATIM. ����� ����������� ��������� ���, �� ���������
 * ����� ������� ������ � ����� ���������.
 */

public class TplParser {
  /** ������������ ��� ������ ������ �������� ������ java-������. */
  private static boolean cleanOnly = false;

  /** �ޣ���� ������������ ������. */
  private int countProcessed = 0;
  /** �ޣ���� ����������� ������ ��-�� ����, ��� �� up-to-date. */
  private int countSkipped = 0;
  /** �ޣ���� ������. */
  private int countError = 0;

  /**
   * ������� ����� �������
   * 
   * @param args ��������� ������
   */
  public static void main(String[] args) {
    TplParser newTplParser = new TplParser();
    System.out.println("TPL parser started");
    newTplParser.resetCounters();
    if (args.length > 0 && args[0].equals("clean")) {
      cleanOnly = true;
      System.out.println("Performing clean only");
    }
    File f = new File(".");
    newTplParser.listDir(f);
    if (cleanOnly) {
      System.out.println(
        newTplParser.getProcessedCount()
        + " parsed, "
        + newTplParser.getParsedCount()
        + " deleted, "
        + newTplParser.getErrorsCount()
        + " errors.");
    } else {
      System.out.println(
        newTplParser.getProcessedCount()
        + " parsed, "
        + newTplParser.getParsedCount()
        + " generated, "
        + newTplParser.getSkippedCount()
        + " skipped due up-to-date, "
        + newTplParser.getErrorsCount()
        + " errors.");
    }
    System.out.println("TPL parser finished");
  }

  /** ��������� �ޣ������.
   */
  public void resetCounters() {
    countProcessed = 0;
    countSkipped = 0;
    countError = 0;
  }

  /** ��������� ������� ���������� ������������� ������.
   *
   * @return ���������� ������������� ������.
   */
  public int getProcessedCount() {
    return countProcessed + countSkipped + countError;
  }

  /** ��������� ������� ���������� ������������ ������.
   *
   * @return ���������� ������������ ������.
   */
  public int getParsedCount() {
    return countProcessed;
  }

  /** ��������� ������� ���������� ����������� ������.
   *
   * @return ���������� ����������� ������.
   */  
  public int getSkippedCount() {
    return countSkipped;
  }

  /** ��������� ������� ���������� ������ ��� ������ � �������.
   *
   * @return ���������� ������.
   */
  public int getErrorsCount() {
    return countError;
  }

  /**
   * ����������� ����� ������ �� �����������
   * 
   * @param f ��������� �� ���������� ��� ������
   */
  private void listDir(File f) {
    File fileList[] = f.listFiles();
    for (int i = 0; i < fileList.length; i++) {
      if (fileList[i].isDirectory()) {
        listDir(fileList[i]);
      } else {
        if (isFileMach(fileList[i].getName())) {
          processFile(fileList[i]);
        }
      }
    }
  }

  /**
   * �������� ����������� ���������� ����� ����� ���������
   * 
   * @param fileName ��� �����
   * 
   * @return ���������� true ���� ���� �������� false ���� �� ��������
   */
  private boolean isFileMach(String fileName) {
    return fileName.endsWith(".tpl");
  }

  /**
   * ��������� tpl ����� � �������� ���������������� ����� .java
   * 
   * @param tplFile ��� �����
   */
  private void processFile(File tplFile) {
    File javaFile = new File(tplFile.getPath().replaceAll(".tpl$", ".java"));
    try {
      if (cleanOnly) {
        javaFile.delete();
        countProcessed++;
        System.out.println("Java file deleted: " + javaFile.getPath());
      } else {
        if (javaFile.lastModified() <= tplFile.lastModified()) {
          System.out.println("Parsing tpl: " + tplFile.getName());
          countProcessed++;
          javaFile.delete();
          if (javaFile.createNewFile()) {
            tplProcessor tplProc = new tplProcessor(tplFile.getPath(), javaFile
                .getPath());
            if (tplProc.go()) {
              System.out.println("Java file created " + javaFile.getName());
            }
          } else {
            countError++;
          }
        } else {
          countSkipped++;
        }
      }
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }
  }
}
