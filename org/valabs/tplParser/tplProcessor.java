package org.valabs.tplParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Ядро парсинга tpl файла
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: tplProcessor.java,v 1.15 2004/10/26 09:25:07 dron Exp $
 */
public class tplProcessor {

  private String packageName = "";

  private String messageName = "";

  private String actionName = "";

  private String tagNAMEstrings = "";

  private String tagIMPORTstrings = "";

  private String tagAUTHORstrings = "";

  private String tagDESCstrings = "";

  private String tagDEFORIGINstrings = "";

  private String tagDEFDESTstrings = "";

  private String tagDEFIDstrings = "";

  private boolean tagDEFROUTABLE = false;

  private boolean tagDEFOOB = false;

  private String tagDEFREPLTOstrings = "";

  private String tagVERBATIMstrings = "";

  private String tagCVSidstrings = "";

  private BufferedReader tplReader = null;

  private PrintStream javaWriter = null;

  private ArrayList fields;

  private ArrayList fieldNames;

  /**
   * Конструктор обработчика tpl файла
   * 
   * @param tplName
   *            имя tpl файла
   * @param javaName
   *            имя соответствующего java файла
   */
  tplProcessor(String tplName, String javaName) {
    fields = new ArrayList();
    fieldNames = new ArrayList();
    try {
      tplReader = new BufferedReader(new FileReader(tplName));
      javaWriter = new PrintStream(new FileOutputStream(javaName));
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
      return;
    }
  }

  /**
   * Запуск парсинга tpl файла
   * 
   * читаем файл построчно и отдаем каждую строку обработчику строки .tpl
   * файла
   * 
   * @return true - все прошло удачно, false - есть исключения
   */
  boolean go() {
    try {
      String line;
      while ((line = tplReader.readLine()) != null) {
        parseTagLine(line);
      }
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      return false;
    }
    writeJavaFile(javaWriter);
    return true;
  }

  /**
   * Запись с переводом строки
   * 
   * @param s
   *            строка которую пишем
   */
  private void write(String s) {
    javaWriter.println(s);
  }

  /**
   * Запись без перевода строки
   * 
   * @param s
   *            строка которую пишем
   */
  private void writec(String s) {
    javaWriter.print(s);
  }

  /**
   * Генерация содержимого .java файла
   * 
   * @param javaWriter
   *            имя .java файла
   */
  private void writeJavaFile(PrintStream javaWriter) {
    write("package " + packageName + ";\n");
    write("import org.valabs.odisp.common.Message;\n");

    if (tagIMPORTstrings != "") {
      write(tagIMPORTstrings);
    }

    write("/** " + tagDESCstrings);
    write(" *");
    write(tagAUTHORstrings);
    write(tagCVSidstrings);
    write(" */");

    write("public final class " + messageName + " {");
    write("  /** Строковое представление сообщения. */");
    write("  public static final String NAME = \"" + actionName + "\";\n");

    for (int i = 0; i < fieldNames.size(); i++) {
      String key = (String) fieldNames.get(i);
      write("  /** Индекс для поля " + key + ". */");
      write("  private static String idx" + key.toUpperCase() + " = \""
          + key.toLowerCase() + "\";");
    }

    write("\n");
    write("  /** Запрет на создание объекта. */");
    write("  private " + messageName + "() { }\n");

    write("  /** Проверка сообщения на корректность.");
    write("   *");
    write("   * @param msg Сообщение");
    write("   */");
    write("  private static void checkMessage(final Message msg) {");

    if (!fieldNames.isEmpty()) {
      write("    try {");
      for (int i = 0; i < fieldNames.size(); i++) {
        String key = (String) fieldNames.get(i);
        write("      assert get" + key + "(msg) != null : \"Message field "
            + key + " is null.\";");
      }
      write("    } catch (AssertionError e) {");
      write("      System.err.println(\"Message assertion :\" + e.toString());");
      write("      e.printStackTrace();\n    }\n");
      write("    msg.setCorrect(");

      int flag = 0;
      for (int i = 0; i < fieldNames.size(); i++) {
        String fieldName = (String) fieldNames.get(i);
        String fieldCheck = ((FieldRecord) fields.get(i)).getCheck();

        if (fieldCheck.trim().length() == 0) {
          fieldCheck = "get" + fieldName + "(msg) != null";
        }
        if (flag == 1) {
          write("      && " + fieldCheck);
        } else {
          write("      " + fieldCheck);
          flag = 1;
        }
      }

    } else {
      write("    msg.setCorrect(\n      true");
    }
    write("    );");
    write("  }\n");

    write("  /** Инициализация основных свойств сообщения.");
    write("   *");
    write("   * @param msg Сообщение.");

    if (tagDEFDESTstrings == "") {
      write("   * @param destination Точка назначения.");
    }

    if (tagDEFORIGINstrings == "") {
      write("   * @param origin Точка отправления.");
    }

    write("   * @param replyTo Идентификатор сообщения, на которое это является ответом.");
    write("   * @return ссылка на инициализированное сообщение");
    write("   */");

    writec("  public static Message setup(final Message msg");

    if (tagDEFDESTstrings == "") {
      writec(",\n                              final String destination");
    }

    if (tagDEFORIGINstrings == "") {
      writec(",\n                              final String origin");
    }

    if (tagDEFREPLTOstrings == "") {
      writec(",\n                              final int replyTo");
    }

    write(") {\n    msg.setAction(NAME);");

    if (tagDEFDESTstrings == "") {
      write("    msg.setDestination(destination);");
    } else {
      write("    msg.setDestination(\"" + tagDEFDESTstrings + "\");");
    }

    if (tagDEFORIGINstrings == "") {
      write("    msg.setOrigin(origin);");
    } else {
      write("    msg.setOrigin(\"" + tagDEFORIGINstrings + "\");");
    }

    if (tagDEFREPLTOstrings == "") {
      write("    msg.setReplyTo(replyTo);");
    } else {
      write("    msg.setReplyTo(" + tagDEFREPLTOstrings + ");");
    }

    if (!tagDEFROUTABLE) {
      write("    msg.setRoutable(false);");
    }
    if (tagDEFOOB) {
      write("    msg.setOOB(true);");
    }

    write("    checkMessage(msg);");
    write("    return msg;");
    write("  }\n");

    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);

      write("  /** Установить " + fieldName + ".");
      write(" " + ((FieldRecord) fields.get(i)).getDesc());
      write("   *");
      write("   * @param msg Сообщение над которым производится действие.");
      write("   * @param newValue Новое значение для поля.");
      write("   * @return ссылка на сообщение");
      write("   */");
      write("  public static Message set" + fieldName
          + "(final Message msg, final "
          + ((FieldRecord) fields.get(i)).getType() + " newValue) {");
      write("    msg.addField(idx" + fieldName.toUpperCase() + ", newValue);");
      write("    checkMessage(msg);");
      write("    return msg;");
      write("  }\n");
      write("  /** Получить " + fieldName + ".");
      write(" " + ((FieldRecord) fields.get(i)).getDesc());
      write("   *");
      write("   * @param msg Сообщение над которым производится действие.");
      write("   * @return значение поля");
      write("   */");
      write("  public static " + ((FieldRecord) fields.get(i)).getType()
          + " get" + fieldName + "(final Message msg) {");
      write("    return (" + ((FieldRecord) fields.get(i)).getType()
          + ") msg.getField(idx" + fieldName.toUpperCase() + ");");
      write("  }\n");
    }

    write("  /** Является ли экземпляр сообщением этого типа.");
    write("   *");
    write("   * @param msg Сообщение.");
    write("   * @return true - если является, false - иначе.");
    write("   */");
    write("  public static boolean equals(final Message msg) {");
    write("    return msg.getAction().equals(NAME);");
    write("  }\n");
    write("  /** Копирование полей из одного сообщения в другое.");
    write("  *");
    write("  * @param dest Получатель.");
    write("  * @param src Источник.");
    write("  */");
    write("  public static void copyFrom(final Message dest, final Message src) {");

    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);
      write("    set" + fieldName + "(dest, get" + fieldName + "(src));");
    }
    write("  }\n");

    write("  /** Генерирование уникального hash-кода сообщения.");
    write("   * Всегда равен 0.");
    write("   * @return hash-код сообщения.");
    write("   */");
    write("  public int hashCode() {");
    write("    return 0;");
    write("  }\n");

    write("  /** Короткий способ заполнения всех полей сообщения сразу.");
    write("   * @return ссылку на сообщение");

    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);
      write("   * @param " + fieldName.toUpperCase() + " "
          + ((FieldRecord) fields.get(i)).getDesc());
    }

    write("  */");

    writec("  public static Message initAll(final Message m");
    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);
      writec((",\n                                final "
          + ((FieldRecord) fields.get(i)).getType() + " " + fieldName
          .toLowerCase()));
    }
    write(") {");

    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);
      write("    set" + fieldName + "(m, " + fieldName.toLowerCase() + ");");
    }

    write("    return m;\n  }\n");

    write(tagVERBATIMstrings);

    write("\n}");
  }

  /**
   * Обработчик строки .tpl файла
   * 
   * @param tagLine
   *            строка .tpl файла
   */
  private void parseTagLine(String tagLine) {
    if (tagLine.startsWith("NAME")) {
      String[] s = tagLine.split(" ");
      packageName = s[1];
      messageName = s[2];
      actionName = s[3];
    } else

    if (tagLine.startsWith("$")) {
      tagCVSidstrings = " * @version " + tagLine;
    } else

    if (tagLine.startsWith("IMPORT")) {
      tagIMPORTstrings += "import " + tagLine.split(" ")[1] + ";\n";
    } else if (tagLine.startsWith("AUTHOR")) {
      if (tagAUTHORstrings == "") {
        tagAUTHORstrings += " * @author " + tagLine.substring(7);
      } else {
        tagAUTHORstrings += "\n * @author " + tagLine.substring(7);
      }
    } else if (tagLine.startsWith("DESC")) {
      tagDESCstrings += tagLine.substring(5);
    } else if (tagLine.startsWith("FIELD")) {
      String[] tokens = tagLine.split(" ");
      if (!fieldNames.contains(tokens[1])) {
        fieldNames.add(tokens[1]);
        fields.add(fieldNames.indexOf(tokens[1]), new FieldRecord());
      }
      ((FieldRecord) fields.get(fieldNames.indexOf(tokens[1])))
          .setType(tokens[2]);
    } else if (tagLine.startsWith("FCHECK")) {
      String[] tokens = tagLine.split(" ");
      if (!fieldNames.contains(tokens[1])) {
        fieldNames.add(tokens[1]);
        fields.add(fieldNames.indexOf(tokens[1]), new FieldRecord());
      }
      ((FieldRecord) fields.get(fieldNames.indexOf(tokens[1])))
          .setCheck(tagLine.substring(7 + tokens[1].length()));
    } else if (tagLine.startsWith("FDESC")) {
      String[] tokens = tagLine.split(" ");
      if (!fieldNames.contains(tokens[1])) {
        fieldNames.add(tokens[1]);
        fields.add(fieldNames.indexOf(tokens[1]), new FieldRecord());
      }
      ((FieldRecord) fields.get(fieldNames.indexOf(tokens[1])))
          .setDesc(tagLine.substring(6 + tokens[1].length()));
    } else if (tagLine.startsWith("DEFORIGIN")) {
      tagDEFORIGINstrings = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFDEST")) {
      tagDEFDESTstrings = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFID")) {
      tagDEFIDstrings = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFROUTABLE")) {
      tagDEFROUTABLE = true;
    } else if (tagLine.startsWith("DEFREPLTO")) {
      tagDEFREPLTOstrings = tagLine.split(" ")[1];
    } else if (tagLine.startsWith("DEFOOB")) {
      System.out.println("WARNING: Generating OOB message " + messageName);
      tagDEFOOB = true;
    } else

    if (!tagLine.startsWith("#")) {
      //комментарии пропускаем
      //TODO may be it's good to skip empty lines
      if (tagLine != "") {
        tagVERBATIMstrings += tagLine + "\n";
      }
    }
  }

  /**
   * Класс содержащий параметры поля сообщения
   * 
   * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
   * @author (C) 2004 НПП "Новел-ИЛ"
   * @version $Id: tplProcessor.java,v 1.15 2004/10/26 09:25:07 dron Exp $
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
     * @param check
     *            текст функции проверки поля сообщения.
     */
    public void setCheck(String check) {
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
     * @param desc
     *            описание поля сообщения.
     */
    public void setDesc(String desc) {
      if (this.desc == "") {
        this.desc += "  *" + desc;
      } else {
        this.desc += "\n  *" + desc;
      }
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
     * @param type
     *            тип поля сообщения.
     */
    public void setType(String type) {
      this.type = type;
    }
  }

}