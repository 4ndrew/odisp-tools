package com.novel.tools.log;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** Форматирование сообщения в одну строку
 * @author <a href="mailto:valeks@novel-il.ru">Valentin A. Alekseev</a>
 * @version $Id: OneLineFormatter.java,v 1.4 2004/07/05 14:37:56 dron Exp $
 */
public class OneLineFormatter extends Formatter {
  static final int SEVERE = 1000;
  static final int WARNING = 900;
  static final int INFO = 800;
  static final int CONFIG = 700;
  static final int FINE = 500;
  static final int FINER = 400;
  static final int FINEST = 300;

  /** Выполнить форматирование записи. Выходная строка в виде:
   * HH:MM:SS LOGLEVEL CLASSNAME.METHOD: MESSAGE. PARAMETERS+
   * @param record запись для анализа
   * @return отформатированная строка
   */
  private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private static int recordidx = 0;
  public final String format(final LogRecord record) {
    String result = "";
    //    result+=sdf.format(new Date(record.getMillis()))+" ";
    recordidx++;
    result += logLevelToOneChar(record.getLevel().intValue())
      + ":" + recordidx + " ";
    result += record.getSourceClassName().substring(
        record.getSourceClassName().lastIndexOf(".") + 1) + ".";
    result += record.getSourceMethodName() + ": ";
    result += record.getMessage() + ".";
    if (record.getParameters() != null 
      && record.getParameters().length > 0) {
      Object[] params = record.getParameters();
      for (int i = 0; i < params.length - 1; i++) {
        result += params[i].toString() + ", ";
      }
      result += params[params.length].toString();
    }
    result += "\n";
    
    if (record.getLevel() == Level.SEVERE) {
      System.out.print("\007");
    }
    
    return result;
  }

  private final String logLevelToOneChar(int logLevel) {
    switch (logLevel) {
      case Integer.MAX_VALUE: return "-";
      case SEVERE: return "!";
      case WARNING: return "W";
      case CONFIG: return "C";
      case INFO: return "I";
      case FINE: return "f";
      case FINER: return "F";
      case FINEST: return "d";
      case Integer.MIN_VALUE: return "a";
      default: return "~";
    }
  }
}
