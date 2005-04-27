package org.valabs.tplParser;


/**
 * Класс содержащий параметры поля сообщения
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: FieldRecord.java,v 1.3 2005/04/27 09:25:40 valeks Exp $
 */
class FieldRecord {

  /**
   * Тип поля
   */
  private String type = "";

  /**
   * Функция проверки значения поля
   */
  private String check = "";

  /**
   * Описание поля
   */
  private String desc = "";
  
  /** Имя поля. */
  private final String name;

  public FieldRecord(String _name) {
    name = _name;
  }
  
  public String getName() {
    return name;
  }
  
  /**
   * Получить функцию проверки поля сообщения
   * 
   * @return текст функции проверки поля сообщения
   */
  public String getCheck() {
    return check;
  }

  /**
   * Установить функцию проверки поля сообщения
   * 
   * @param check текст функции проверки поля сообщения.
   */
  public void setCheck(final String check) {
    this.check = check;
  }

  /**
   * Получить описание поля сообщения
   * 
   * @return описание поля сообщения.
   */
  public String getDesc() {
    return desc;
  }

  /**
   * Установить описание поля сообщения
   * 
   * @param desc описание поля сообщения.
   */
  public void setDesc(final String desc) {
    this.desc = desc;
  }

  /**
   * Получить тип поля сообщения
   * 
   * @return тип поля сообщения.
   */
  public String getType() {
    return type;
  }

  /**
   * Установить тип поля сообщения
   * 
   * @param type тип поля сообщения.
   */
  public void setType(final String type) {
    this.type = type;
  }
  
  public String toString() {
    return type + " " + name  + " // " + desc;
  }
}