package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;


/** Создание класса сообщения на языке Java.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MessageFile_Java.java,v 1.2 2005/02/03 12:40:26 valeks Exp $
 */
class MessageFile_Java implements MessageFile {
  
  private int countMain = 0;
  private int countReply = 0;
  private int countError = 0;
  private int countNotify = 0;
  
  public String getExtension() {
    return ".java";
  }
  
  public String toString() {
    return "Java message output. Counters: " + countMain + " main, " + countReply + " replys, " + countError
            + " error, " + countNotify + " notifys.";
  }
  
  public void writeFile(TplFile tplSource, OutputStream out) throws IOException {
    writeJavaHeader(tplSource, out);

    writeClassJavadoc(tplSource, out);

    writeMessageConstants(tplSource, out);

    writeCheckMessage(tplSource, out);

    writeSetup(tplSource, out);

    Iterator it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      String fieldName = (String) it.next();
      writeField(tplSource, out, fieldName);
    }

    writeMisc(tplSource, out);

    writeInitAll(tplSource, out);

    writeVerbatim(tplSource, out);
    
    writeJavaFooter(tplSource, out);
    
    switch (tplSource.getType()) {
      case TplFile.TYPE_PLAIN:
        System.out.print(", main");
        countMain++;
        break;
      case TplFile.TYPE_ERROR:
        System.out.print(", error");
        countError++;
        break;
      case TplFile.TYPE_REPLY:
        System.out.print(", reply");
        countReply++;
        break;
      case TplFile.TYPE_NOTIFY:
        System.out.print(", notify");
        countNotify++;
        break;
    }

    System.out.print("(java)");
  }
  
  /**
   * @param out
   * @throws IOException
   */
  private void writeJavaFooter(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "\n} // " + tplSource.getMessageName() + " \n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeVerbatim(TplFile tplSource, OutputStream out) throws IOException {
    Iterator it;
    it = tplSource.getVerbatim().iterator();
    while (it.hasNext()) {
      String element = (String) it.next();
      write(out, element + "\n");
    }
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeInitAll(TplFile tplSource, OutputStream out) throws IOException {
    Iterator it;
    write(out, "  /** Короткий способ заполнения всех полей сообщения сразу.\n");
    write(out, "   * @return ссылку на сообщение\n");
    
    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      String fieldName = (String) it.next();
      write(out, "   * @param " + fieldName.toLowerCase() + " "
        + ((FieldRecord) tplSource.getFields().get(fieldName)).getDesc() + "\n");
    }

    write(out, "  */\n");

    write(out, "  public static Message initAll(final Message m");
    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      String fieldName = (String) it.next();
      write(out, (",\n                                final "
        + ((FieldRecord) tplSource.getFields().get(fieldName)).getType() + " " + fieldName.toLowerCase()));
    }
    write(out, ") {\n");

    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      String fieldName = (String) it.next();
      write(out, "    set" + fieldName + "(m, " + fieldName.toLowerCase() + ");\n");
    }

    write(out, "    return m;\n  }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeMisc(TplFile tplSource, OutputStream out) throws IOException {
    Iterator it;
    write(out, "  /** Является ли экземпляр сообщением этого типа.\n");
    write(out, "   * @param msg Сообщение.\n");
    write(out, "   * @return true - если является, false - иначе.\n");
    write(out, "   */\n");
    write(out, "  public static boolean equals(final Message msg) {\n");
    write(out, "    return msg.getAction().equals(NAME);\n");
    write(out, "  }\n");
    if (tplSource.getFields().size() > 0) {
      write(out, "  /** Копирование полей из одного сообщения в другое.\n");
      write(out, "  * @param dest Получатель.\n");
      write(out, "  * @param src Источник.\n");
      write(out, "  */\n");
      write(out, "  public static void copyFrom(final Message dest, final Message src) {\n");
      it = tplSource.getFields().keySet().iterator();
      while (it.hasNext()) {
        String fieldName = (String) it.next();
        write(out, "    set" + fieldName + "(dest, get" + fieldName + "(src));\n");
      }
      write(out, "  }\n");
    }
    write(out, "  /** Генерирование уникального hash-кода сообщения.\n");
    write(out, "   * Всегда равен 0.\n");
    write(out, "   * @return hash-код сообщения.\n");
    write(out, "   */\n");
    write(out, "  public int hashCode() {\n");
    write(out, "    return 0;\n");
    write(out, "  }\n");
  }

  /**
   * @param out
   * @param fieldName
   * @throws IOException
   */
  private void writeField(TplFile tplSource, OutputStream out, String fieldName) throws IOException {
    FieldRecord fr = (FieldRecord) tplSource.getFields().get(fieldName);
    write(out, "  /** Установить " + fr.getName() + ".\n");
    if (fr.getDesc().length() > 0)
      write(out, "   * " + fr.getDesc() + "\n");
    write(out, "   * @param msg Сообщение над которым производится действие.\n");
    write(out, "   * @param newValue Новое значение для поля.\n");
    write(out, "   * @return ссылка на сообщение\n");
    write(out, "   */\n\n");
    write(out, "  public static Message set" + fr.getName()
      + "(final Message msg, final "
      + fr.getType() + " newValue) {\n");
    write(out, "    msg.addField(idx" + fr.getName().toUpperCase() + ", newValue);\n");
    write(out, "    checkMessage(msg);\n");
    write(out, "    return msg;\n");
    write(out, "  }\n");
    write(out, "  /** Получить " + fr.getName() + ".\n");
    write(out, "   *" + fr.getDesc() + "\n");
    write(out, "   * @param msg Сообщение над которым производится действие.\n");
    write(out, "   * @return значение поля\n");
    write(out, "   */\n");
    write(out, "  public static " + fr.getType()
      + " get" + fr.getName() + "(final Message msg) {\n");
    write(out, "    return " + (fr.getType().equals("Object") ? "" : "(" + fr.getType() + ")")
            + "msg.getField(idx" + fr.getName().toUpperCase() + ");\n");
    write(out, "  }\n\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeSetup(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "  /** Инициализация основных свойств сообщения.\n");
    write(out, "   * @param msg Сообщение.\n");

    if (tplSource.getDefaultDestination() == null) {
      write(out, "   * @param destination Точка назначения.\n");
    }

    if (tplSource.getDefaultOrigin() == null) {
      write(out, "   * @param origin Точка отправления.\n");
    }

    if (tplSource.getDefaultReplyTo() == null) {
      write(out, "   * @param replyTo Идентификатор сообщения, на которое это является ответом.\n");
    }
    write(out, "   * @return ссылка на инициализированное сообщение\n");
    write(out, "   */\n");

    write(out, "  public static Message setup(final Message msg\n");

    if (tplSource.getDefaultDestination() == null) {
      write(out, ",\n                              final String destination");
    }

    if (tplSource.getDefaultOrigin() == null) {
      write(out, ",\n                              final String origin");
    }

    if (tplSource.getDefaultReplyTo() == null) {
      write(out, ",\n                              final UUID replyTo");
    }

    write(out, ") {\n    msg.setAction(NAME);\n");

    if (tplSource.getDefaultDestination() == null) {
      write(out, "    msg.setDestination(destination);\n");
    } else {
      write(out, "    msg.setDestination(\"" + tplSource.getDefaultDestination() + "\");\n");
    }

    if (tplSource.getDefaultOrigin() == null) {
      write(out, "    msg.setOrigin(origin);\n");
    } else {
      write(out, "    msg.setOrigin(\"" + tplSource.getDefaultOrigin() + "\");\n");
    }

    if (tplSource.getDefaultReplyTo() == null) {
      write(out, "    msg.setReplyTo(replyTo);\n");
    } else {
      write(out, "    msg.setReplyTo(" + tplSource.getDefaultReplyTo() + ");\n");
    }

    if (!tplSource.isDefaultRoutable()) {
      write(out, "    msg.setRoutable(false);\n");
    }
    if (tplSource.isDefaultOOB()) {
      write(out, "    msg.setOOB(true);\n");
    }

    write(out, "    checkMessage(msg);\n");
    write(out, "    return msg;\n");
    write(out, "  }\n\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeCheckMessage(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "  /** Проверка сообщения на корректность.\n");
    write(out, "   * @param msg Сообщение\n");
    write(out, "   */\n");
    write(out, "  private static void checkMessage(final Message msg) {\n");

    if (!tplSource.getFields().isEmpty()) {
      write(out, "    try {\n");
      Iterator it = tplSource.getFields().keySet().iterator();
      while (it.hasNext()) {
        String key = (String) it.next();
        // XXX: не учитывается FCHECK
        write(out, "      assert get" + key + "(msg) != null : \"Message field " + key + " is null.\";\n");
      }
      
      write(out, "    } catch (AssertionError e) {\n");
      write(out, "      System.err.println(\"Message assertion :\" + e.toString());\n");
      write(out, "      e.printStackTrace();\n    }\n");
      write(out, "    msg.setCorrect(\n");

      int flag = 0;
      it = tplSource.getFields().keySet().iterator();
      while (it.hasNext()) {
        String fieldName = (String) it.next();
        String fieldCheck = ((FieldRecord) tplSource.getFields().get(fieldName)).getCheck();

        if (fieldCheck.trim().length() == 0) {
          fieldCheck = "get" + fieldName + "(msg) != null";
        }
        if (flag == 1) {
          write(out, "      && " + fieldCheck + "\n");
        } else {
          write(out, "      " + fieldCheck + "\n");
          flag = 1;
        }
      }

    } else {
      write(out, "    msg.setCorrect(\n      true\n");
    }
    write(out, "    );\n");
    write(out, "  }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeMessageConstants(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "public final class " + tplSource.getMessageName() + " {\n");
    write(out, "  /** Строковое представление сообщения. */\n");
    write(out, "  public static final String NAME = \"" + tplSource.getActionName() + "\";\n");

    Iterator it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      write(out, "  /** Индекс для поля " + key + ". */\n");
      write(out, "  private static String idx" + key.toUpperCase() + " = \"" + key.toLowerCase() + "\";\n");      
    }
    write(out, "\n");
    write(out, "  /** Запрет на создание объекта. */\n");
    write(out, "  private " + tplSource.getMessageName() + "() { /* Single-model. Не позволяется создавать объект. */ }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeClassJavadoc(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "/** ");
    Iterator it = tplSource.getDescription().iterator();    
    while (it.hasNext()) {
      String element = (String) it.next();
      write(out, element);
      write(out, "\n * ");
    }
    it = tplSource.getAuthors().iterator();    
    while (it.hasNext()) {
      String element = (String) it.next();
      write(out, "@author " + element);
      write(out, "\n * ");
    }
    write(out, "@version " + tplSource.getCvsId() + "\n");
    write(out, " */\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeJavaHeader(TplFile tplSource, OutputStream out) throws IOException {
    write(out, "package " + tplSource.getPackageName() + ";\n\n");
    write(out, "import org.valabs.odisp.common.Message;\n");
    write(out, "import org.doomdark.uuid.UUID;\n");

    if (tplSource.getImports().size() > 0) {
      Iterator it = tplSource.getImports().iterator();
      while (it.hasNext()) {
        String element = (String) it.next();
        write(out, "import " + element + ";\n");
      }
    }
    write(out, "\n");
  }

  private void write(OutputStream out, String line) throws IOException {
    out.write(line.getBytes());
  }
}
