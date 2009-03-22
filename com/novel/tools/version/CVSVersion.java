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
package com.novel.tools.version;

/**
 * �������������� ��������� ������ ������ �� ������ $Revision: 1.1 $ CVS
 * (�� ��� ���� ��� ���������). 
 * 
 * ������ �������������:
 * <code>
 * public static final String VERSION = CVSVersion.getCVSRevisionVersion("$Revision: 1.1 $");
 * </code>
 * 
 * @author <a href="boris@novel-il.ru">����� �. �����������</a>
 * @author (C) 2004-2005 ��� "�����-��"
 * @version $Id: CVSVersion.java,v 1.1 2005/11/25 14:45:18 dron Exp $
 */
public final class CVSVersion {

  /**
   * ��������� ������ �� CVS $Revision: 1.1 $
   * 
   * @param revision ������ ���� "$Revision: 1.1 $"
   * @return ������
   */
  public static String getCVSRevisionVersion(String revision) {
    String[] version = revision.split(" ");
    return version[1];
  }
}
