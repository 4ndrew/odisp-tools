package com.novel.tools;

/** TODO: Class description here
 * 
 * @author <a href="dron@novel-il.ru">Андрей А. Порохин</a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: ObjectItem.java,v 1.1 2004/06/11 13:26:40 dron Exp $
 */
public class ObjectItem {
  /** Список зависемостей */
  public String depends[];
  /** Список предоставляемых сервисов */
  public String providing[];
  /** Удолетворены ли все зависемости? */
  boolean correct;
}
