package org.valabs.tplParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Класс полностью описывающий шаблон сообщения.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: TplFile.java,v 1.1 2005/02/02 20:22:28 valeks Exp $
 * 
 * Пример шаблонов:
 * 
 * NAME [пакет] [имя класса] [название ODISP action] IMPORT [пакет] (*)
 * 
 * AUTHOR [автор (для тега (a)author)] (*)
 * 
 * DESC [описание сообщения] (*) FIELD [имя поля (с заглавной буквы)] [тип поля]
 * 
 * FCHECK [имя поля] [выражение для проверки в boolean checkMessage()] (**)
 * 
 * FDESC [имя поля] [описание поля] (*) DEFORIGIN [точка отправления
 * по-умолчанию]
 * 
 * DEFDEST [точка назначения по-умолчанию]
 *
 * DEFROUTABLE [Routable по-умолчанию]
 * 
 * DEFREPLTO [номер сообщения на которое производится ответ по умолчанию]
 * 
 * DEFOOB [OOB по-умолчанию]
 * 
 * VERBATIM (***)
 * 
 * Версия для тега (a)version берется из CVS-тега Id.
 * 
 * (*) Поддерживаются multiline comments, все поля комментариев, должны
 * начинаться с нового ключевого слова. Например: AUTHOR 1 строка AUTHOR 2
 * строка
 * 
 * (**) Значение по-умолчанию get[имя поля](msg) != null
 * 
 * (***) VERBATIM включает режим переноса текста в результирующее сообщение.
 * Выключается повторным VERBATIM. Может встречаться несколько раз, но результат
 * будет выведен только в конце сообщения.
 */
class TplFile {
  private String packageName;

  private String messageName;

  private String actionName;

  private List imports = new ArrayList();

  private List authors = new ArrayList();

  private List description = new ArrayList();

  private String defaultOrigin;

  private String defaultDestination;

  private boolean defaultRoutable = false;

  private boolean defaultOOB = false;

  private String defaultReplyTo;

  private List verbatim = new ArrayList();

  private String cvsId;
  
  private Map fields = new HashMap();
  
  private String fileName;
  
  private long lastModified;

  /** Инициализация класса и разбор указанного файла.
   * @param toParse файл для разбора
   * @throws IOException в случае возникновения ошибок при работе с файлом
   */
  public TplFile(File toParse) throws IOException {
    fileName = toParse.getPath();
    lastModified = toParse.lastModified();
    BufferedReader tplReader = new BufferedReader(new FileReader(toParse));
    String line;
    while ((line = tplReader.readLine()) != null) {
      parseTagLine(line);
    }
    System.out.print("TPL parsed: " + fileName);
  }
  
  /** Обработчик строки .tpl файла
   * @param tagLine строка .tpl файла
   */
  private void parseTagLine(String tagLine) {
    if (tagLine.startsWith("NAME")) {
      String[] s = tagLine.split(" ");
      packageName = s[1];
      messageName = s[2];
      actionName = s[3];
    } else

    if (tagLine.startsWith("$")) {
      cvsId = tagLine;
    } else

    if (tagLine.startsWith("IMPORT")) {
      imports.add(tagLine.split(" ")[1]);
    } else if (tagLine.startsWith("AUTHOR")) {
      authors.add(tagLine.substring(7));
    } else if (tagLine.startsWith("DESC")) {
      description.add(tagLine.substring(5));
    } else if (tagLine.startsWith("FIELD")) {
      String[] tokens = tagLine.split(" ");
      getFieldRecordByName(tokens[1]).setType(tokens[2]);
    } else if (tagLine.startsWith("FCHECK")) {
      String[] tokens = tagLine.split(" ");
      getFieldRecordByName(tokens[1]).setCheck(tagLine.substring(8 + tokens[1].length()));
    } else if (tagLine.startsWith("FDESC")) {
      String[] tokens = tagLine.split(" ");
      getFieldRecordByName(tokens[1]).setDesc(tagLine.substring(7 + tokens[1].length()));
    } else if (tagLine.startsWith("DEFORIGIN")) {
      defaultOrigin = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFDEST")) {
      defaultDestination = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFROUTABLE")) {
      defaultRoutable = true;
    } else if (tagLine.startsWith("DEFREPLTO")) {
      defaultReplyTo = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFOOB")) {
      defaultOOB = true;
    } else if (!tagLine.startsWith("#")) {
      //комментарии пропускаем
      //TODO may be it's good to skip empty lines
      if (tagLine != "") {
        verbatim.add(tagLine);
      }
    }
  }
  
  /** Получение записи о поле по имени.
   * @param name имя поля
   */
  private FieldRecord getFieldRecordByName(String name) {
    if (!fields.containsKey(name)) {
      fields.put(name, new FieldRecord());
    }
    return (FieldRecord) fields.get(name);
  }

  public String getActionName() {
    return actionName;
  }
  public List getAuthors() {
    return authors;
  }
  public String getCvsId() {
    return cvsId;
  }
  public String getDefaultDestination() {
    return defaultDestination;
  }
  public boolean isDefaultOOB() {
    return defaultOOB;
  }
  public String getDefaultOrigin() {
    return defaultOrigin;
  }
  public String getDefaultReplyTo() {
    return defaultReplyTo;
  }
  public boolean isDefaultRoutable() {
    return defaultRoutable;
  }
  public List getDescription() {
    return description;
  }
  public Map getFields() {
    return fields;
  }
  public List getImports() {
    return imports;
  }
  public String getMessageName() {
    return messageName;
  }
  public String getPackageName() {
    return packageName;
  }
  public List getVerbatim() {
    return verbatim;
  }
  public String getFileName() {
    return fileName;
  }
  public long lastModified() {
    return lastModified;
  }
}
