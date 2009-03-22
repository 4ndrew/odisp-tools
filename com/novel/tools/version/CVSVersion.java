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
