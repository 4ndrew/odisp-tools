package com.novel.tools.log;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/** Форматирование сообщения в одну строку
 * @author <a href="mailto:valeks@novel-il.ru">Valentin A. Alekseev</a>
 * @version $Id: OneLineFormatter.java,v 1.3 2004/02/27 10:58:11 valeks Exp $
 */
public class OneLineFormatter extends Formatter {
  /** Выполнить форматирование записи. Выходная строка в виде:
   * HH:MM:SS LOGLEVEL CLASSNAME.METHOD: MESSAGE. PARAMETERS+
   * @param record запись для анализа
   * @return отформатированная строка
   */
  private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private static int recordidx = 0;
  public final String format(final LogRecord record){
    String result = "";
    //    result+=sdf.format(new Date(record.getMillis()))+" ";
    recordidx++;
    result+=logLevelToOneChar(record.getLevel().intValue()) + ":" + recordidx + " ";
    result+=record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf(".")+1)+".";
    result+=record.getSourceMethodName()+": ";
    result+=record.getMessage()+".";
    if(record.getParameters() != null &&record.getParameters().length > 0){
      Object[] params = record.getParameters();
      for(int i = 0; i < params.length-1; i++)
	result+=params[i].toString()+", ";
      result+=params[params.length].toString();
    }
    result+="\n";
    return result;
  }

  private final String logLevelToOneChar(int logLevel) {
    switch (logLevel) {
    case Integer.MAX_VALUE: return "-";
    case 1000: return "!";
    case 900: return "w";
    case 800: return "i";
    case 700: return "a";
    case 500: return "f";
    case 400: return "F";
    case 300: return "d";
    case Integer.MIN_VALUE: return "a";
    default: return "~";
    }
  }
}
