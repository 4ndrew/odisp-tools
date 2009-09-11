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
package org.valabs.tools.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** Форматирование сообщения в одну строку
 * @author <a href="mailto:valeks@novel-il.ru">Valentin A. Alekseev</a>
 * @version $Id: OneLineFormatter.java,v 1.6 2005/04/27 09:25:40 valeks Exp $
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
  private static int recordidx = 0;
  public final String format(final LogRecord record) {
    StringBuffer result = new StringBuffer(180);
    recordidx++;
    result.append(logLevelToOneChar(record.getLevel().intValue()))
      .append("(").append(record.getThreadID()).append("):").append(recordidx).append(" ");
    
    result.append(record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf(".") + 1)).append(".");
    
    result.append(record.getSourceMethodName()).append(": ");
    result.append(record.getMessage()).append(".");
    if (record.getParameters() != null 
      && record.getParameters().length > 0) {
      final Object[] params = record.getParameters();
      for (int i = 0; i < params.length - 1; i++) {
        result.append(params[i].toString()).append(", ");
      }
      result.append(params[params.length].toString());
    }
    result.append("\n");
    
    // Print stack trace if available
    if (record.getThrown() != null) {
      try {
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          record.getThrown().printStackTrace(pw);
          pw.close();
          result.append(sw.toString());
      } catch (Exception ex) {
      }
    }
    
    if (record.getLevel() == Level.SEVERE) {
      System.out.print("\007");
    }
    
    return result.toString();
  }

  private final String logLevelToOneChar(final int logLevel) {
  	String result;
    switch (logLevel) {
      case Integer.MAX_VALUE: result ="-"; break;
      case SEVERE: result ="!"; break;
      case WARNING: result ="W"; break;
      case CONFIG: result ="C"; break;
      case INFO: result ="I"; break;
      case FINE: result ="F"; break;
      case FINER: result ="f"; break;
      case FINEST: result ="d"; break;
      case Integer.MIN_VALUE: result ="a"; break;
      default: result = "~";
    }
    return result;
  }
}
