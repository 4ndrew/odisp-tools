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
package org.valabs.tplParser;


/**
 * ����� ���������� ��������� ���� ���������
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (C) 2004 ��� "�����-��"
 * @version $Id: FieldRecord.java,v 1.4 2006/01/23 11:09:48 valeks Exp $
 */
class FieldRecord {

  /**
   * ��� ����
   */
  private String type = "";

  /**
   * ������� �������� �������� ����
   */
  private String check = "";

  /**
   * �������� ����
   */
  private String desc = "";
  
  /** �������� ��-���������. */
  private String defaultv = "";
  
  /** ��� ����. */
  private final String name;

  public FieldRecord(String _name) {
    name = _name;
  }
  
  public String getName() {
    return name;
  }
  
  /**
   * �������� ������� �������� ���� ���������
   * 
   * @return ����� ������� �������� ���� ���������
   */
  public String getCheck() {
    return check;
  }

  /**
   * ���������� ������� �������� ���� ���������
   * 
   * @param check ����� ������� �������� ���� ���������.
   */
  public void setCheck(final String check) {
    this.check = check;
  }

  /**
   * �������� �������� ���� ���������
   * 
   * @return �������� ���� ���������.
   */
  public String getDesc() {
    return desc;
  }

  /**
   * ���������� �������� ���� ���������
   * 
   * @param desc �������� ���� ���������.
   */
  public void setDesc(final String desc) {
    this.desc = desc;
  }

  /**
   * �������� ��� ���� ���������
   * 
   * @return ��� ���� ���������.
   */
  public String getType() {
    return type;
  }

  /**
   * ���������� ��� ���� ���������
   * 
   * @param type ��� ���� ���������.
   */
  public void setType(final String type) {
    this.type = type;
  }
  
  public String toString() {
    return type + " " + name  + " // " + desc;
  }
  
  public String getDefault() {
    return defaultv;
  }
  
  public void setDefault(final String _defaultv) {
    defaultv = _defaultv;
  }
}