package org.valabs.tplParser;


/**
 * ����� ���������� ��������� ���� ���������
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (C) 2004 ��� "�����-��"
 * @version $Id: FieldRecord.java,v 1.3 2005/04/27 09:25:40 valeks Exp $
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
}