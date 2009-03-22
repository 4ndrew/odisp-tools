package com.novel.tools.version;

/**
 * Автоматическое получение версии класса из данных $Revision: 1.1 $ CVS
 * (ну или чего еще захочется). 
 * 
 * Пример использования:
 * <code>
 * public static final String VERSION = CVSVersion.getCVSRevisionVersion("$Revision: 1.1 $");
 * </code>
 * 
 * @author <a href="boris@novel-il.ru">Борис В. Волковыский</a>
 * @author (C) 2004-2005 НПП "Новел-ИЛ"
 * @version $Id: CVSVersion.java,v 1.1 2005/11/25 14:45:18 dron Exp $
 */
public final class CVSVersion {

  /**
   * Получение версии из CVS $Revision: 1.1 $
   * 
   * @param revision должен быть "$Revision: 1.1 $"
   * @return версия
   */
  public static String getCVSRevisionVersion(String revision) {
    String[] version = revision.split(" ");
    return version[1];
  }
}
