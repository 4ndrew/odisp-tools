package org.valabs.tplParser;

import java.io.File;
import java.io.IOException;

/**
 * Утилита для генерации классов сообщений ODISP на основе шаблонов.
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @version $Id: TplParser.java,v 1.18 2004/11/07 13:50:54 dron Exp $
 * 
 * Пример шаблонов:
 * 
 * NAME [пакет] [имя класса] [название ODISP action] IMPORT [пакет] (*)
 * 
 * AUTHOR [автор (для тега (a)author)] (*)
 * 
 * DESC [описание сообщения] (*) FIELD [имя поля (с заглавной буквы)] [тип поля]
 * 
 * FCHECK [имя поля] [выражение для проверки в boolean checkMessage()] (**)
 * 
 * FDESC [имя поля] [описание поля] (*) DEFORIGIN [точка отправления
 * по-умолчанию]
 * 
 * DEFDEST [точка назначения по-умолчанию]
 *
 * DEFROUTABLE [Routable по-умолчанию]
 * 
 * DEFREPLTO [номер сообщения на которое производится ответ по умолчанию]
 * 
 * DEFOOB [OOB по-умолчанию]
 * 
 * VERBATIM (***)
 * 
 * Версия для тега (a)version берется из CVS-тега Id.
 * 
 * (*) Поддерживаются multiline comments, все поля комментариев, должны
 * начинаться с нового ключевого слова. Например: AUTHOR 1 строка AUTHOR 2
 * строка
 * 
 * (**) Значение по-умолчанию get[имя поля](msg) != null
 * 
 * (***) VERBATIM включает режим переноса текста в результирующее сообщение.
 * Выключается повторным VERBATIM. Может встречаться несколько раз, но результат
 * будет выведен только в конце сообщения.
 */

public class TplParser {
  /** Используется для режима только удаления файлов java-файлов. */
  private static boolean cleanOnly = false;

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

  /** Обнуление счётчиков.
   */
  public void resetCounters() {
    countProcessed = 0;
    countSkipped = 0;
    countError = 0;
  }

  /** Получение полного количества просмотренных файлов.
   *
   * @return Количество просмотренных файлов.
   */
  public int getProcessedCount() {
    return countProcessed + countSkipped + countError;
  }

  /** Получение полного количества обработанных файлов.
   *
   * @return Количество обработанных файлов.
   */
  public int getParsedCount() {
    return countProcessed;
  }

  /** Получение полного количества пропущенных файлов.
   *
   * @return Количество пропущенных файлов.
   */  
  public int getSkippedCount() {
    return countSkipped;
  }

  /** Получение полного количества ошибок при работе с файлами.
   *
   * @return Количество ошибок.
   */
  public int getErrorsCount() {
    return countError;
  }

  /**
   * Рекурсивный поиск файлов по директориям
   * 
   * @param f указатель на директорию где искать
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
   * Проверка соответсвия найденного файла нашим критериям
   * 
   * @param fileName имя файла
   * 
   * @return возвращает true если файл подходит false если не подходит
   */
  private boolean isFileMach(String fileName) {
    return fileName.endsWith(".tpl");
  }

  /**
   * Обработка tpl файла и создание соответствующего файла .java
   * 
   * @param tplFile имя файла
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
