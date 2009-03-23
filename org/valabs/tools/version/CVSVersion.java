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
package org.valabs.tools.version;

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
