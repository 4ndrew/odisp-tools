package com.novel.tools.log;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** �������������� ��������� � ���� ������
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

  /** ��������� �������������� ������. �������� ������ � ����:
   * HH:MM:SS LOGLEVEL CLASSNAME.METHOD: MESSAGE. PARAMETERS+
   * @param record ������ ��� �������
   * @return ����������������� ������
   */
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
      final Object[] params = record.getParameters();
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

  private final String logLevelToOneChar(final int logLevel) {
  	String result;
    switch (logLevel) {
      case Integer.MAX_VALUE: result ="-"; break;
      case SEVERE: result ="!"; break;
      case WARNING: result ="W"; break;
      case CONFIG: result ="C"; break;
      case INFO: result ="I"; break;
      case FINE: result ="f"; break;
      case FINER: result ="F"; break;
      case FINEST: result ="d"; break;
      case Integer.MIN_VALUE: result ="a"; break;
      default: result = "~";
    }
    return result;
  }
}
