package com.novel.tools;

/** Итем. =))))
 * 
 * @author <a href="dron@novel-il.ru">Андрей А. Порохин</a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: ObjectItem.java,v 1.2 2004/11/01 11:04:19 dron Exp $
 */
public class ObjectItem {
  /** Список зависемостей */
  public String depends[];
  /** Список предоставляемых сервисов */
  public String providing[];
  /** Удолетворены ли все зависемости? */
  boolean correct;
}
