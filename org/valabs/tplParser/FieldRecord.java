package org.valabs.tplParser;


/**
 * ����� ���������� ��������� ���� ���������
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (C) 2004 ��� "�����-��"
 * @version $Id: FieldRecord.java,v 1.1 2005/02/02 20:22:28 valeks Exp $
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
  public void setCheck(String check) {
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
  public void setDesc(String desc) {
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
  public void setType(String type) {
    this.type = type;
  }
}