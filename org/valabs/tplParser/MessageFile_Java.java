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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

/** �������� ������ ��������� �� ����� Java.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MessageFile_Java.java,v 1.9 2006/03/20 09:52:29 valeks Exp $
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
    return "Java message output. Counters: " + countMain + " main, " + countReply + " replys, " + countError + " error, " + countNotify + " notifys.";
  }

  public void writeFile(final TplFile tplSource, final OutputStream out) throws IOException {
    writeJavaHeader(tplSource, out);

    writeClassJavadoc(tplSource, out);

    writeMessageConstants(tplSource, out);

    writeCheckMessage(tplSource, out);

    writeSetup(tplSource, out);

    final Iterator it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String fieldName = (String) it.next();
      writeField(tplSource, out, fieldName);
    }

    writeMisc(tplSource, out);

    writeInitAll(tplSource, out);

    writeHelper(tplSource, out);

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
    default:
      throw new IOException("Unknown message type used");
    }

    System.out.print("(java)");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeJavaFooter(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "\n} // " + tplSource.getMessageName() + " \n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeVerbatim(final TplFile tplSource, final OutputStream out) throws IOException {
    final Iterator it = tplSource.getVerbatim().iterator();
    while (it.hasNext()) {
      final String element = (String) it.next();
      write(out, element + "\n");
    }
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeInitAll(final TplFile tplSource, final OutputStream out) throws IOException {
    Iterator it;
    write(out, "  /** �������� ������ ���������� ���� ����� ��������� �����.\n");
    write(out, "   * @return ������ �� ���������\n");
    write(out, "   * @param m ��������� ��� ���������\n");

    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String fieldName = (String) it.next();
      write(out, "   * @param " + fieldName.toLowerCase(Locale.ENGLISH) + " " + ((FieldRecord) tplSource.getFields().get(fieldName)).getDesc() + "\n");
    }

    write(out, "  */\n");

    write(out, "  public static Message initAll(final Message m");
    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String fieldName = (String) it.next();
      write(out, (",\n                                final " + ((FieldRecord) tplSource.getFields().get(fieldName)).getType() + " " + fieldName.toLowerCase(Locale.ENGLISH)));
    }
    write(out, ") {\n");

    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String fieldName = (String) it.next();
      write(out, "    set" + fieldName + "(m, " + fieldName.toLowerCase(Locale.ENGLISH) + ");\n");
    }

    write(out, "    return m;\n  }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeMisc(TplFile tplSource, OutputStream out) throws IOException {
    Iterator it;
    write(out, "  /** �������� �� ��������� ���������� ����� ����.\n");
    write(out, "   * @param msg ���������.\n");
    write(out, "   * @return true - ���� ��������, false - �����.\n");
    write(out, "   */\n");
    write(out, "  public static boolean equals(final Message msg) {\n");
    write(out, "    return msg.getAction().equals(NAME);\n");
    write(out, "  }\n");
    if (tplSource.getFields().size() > 0) {
      write(out, "  /** ����������� ����� �� ������ ��������� � ������.\n");
      write(out, "  * @param dest ����������.\n");
      write(out, "  * @param src ��������.\n");
      write(out, "  */\n");
      write(out, "  public static void copyFrom(final Message dest, final Message src) {\n");
      it = tplSource.getFields().keySet().iterator();
      while (it.hasNext()) {
        final String fieldName = (String) it.next();
        write(out, "    set" + fieldName + "(dest, get" + fieldName + "(src));\n");
      }
      write(out, "  }\n");
    }
    write(out, "  /** ������������� ����������� hash-���� ���������.\n");
    write(out, "   * ������ ����� 0.\n");
    write(out, "   * @return hash-��� ���������.\n");
    write(out, "   */\n");
    write(out, "  public int hashCode() {\n");
    write(out, "    return 0;\n");
    write(out, "  }\n");
    write(out, "  /** �������� Helper-����� ��� ������� ���������.\n");
    write(out, "   * @param msg ��������� ��� �������� ����c��� Helper.\n");
    write(out, "   * @return ��������� ������ " + tplSource.getMessageName() + "Helper ����������� �� ������ ���������.\n");
    write(out, "   */\n");
    write(out, "  public static " + tplSource.getMessageName() + "Helper getHelper(Message msg) {\n");
    write(out, "    return new " + tplSource.getMessageName() + "Helper(msg);\n");
    write(out, "  }\n");
  }

  /**
   * @param out
   * @param fieldName
   * @throws IOException
   */
  private void writeField(final TplFile tplSource, final OutputStream out, final String fieldName) throws IOException {
    final FieldRecord fr = (FieldRecord) tplSource.getFields().get(fieldName);
    write(out, "  /** ���������� " + fr.getName() + ".\n");
    if (fr.getDesc().length() > 0) {
      write(out, "   * " + fr.getDesc() + "\n");
    }
    write(out, "   * @param msg ��������� ��� ������� ������������ ��������.\n");
    write(out, "   * @param newValue ����� �������� ��� ����.\n");
    write(out, "   * @return ������ �� ���������\n");
    write(out, "   */\n\n");
    write(out, "  public static Message set" + fr.getName() + "(final Message msg, final " + fr.getType() + " newValue) {\n");
    write(out, "    msg.addField(idx" + fr.getName().toUpperCase() + ", newValue);\n");
    write(out, "    checkMessage(msg);\n");
    write(out, "    return msg;\n");
    write(out, "  }\n");
    write(out, "  /** �������� " + fr.getName() + ".\n");
    write(out, "   *" + fr.getDesc() + "\n");
    write(out, "   * @param msg ��������� ��� ������� ������������ ��������.\n");
    write(out, "   * @return �������� ����\n");
    write(out, "   */\n");
    write(out, "  public static " + fr.getType() + " get" + fr.getName() + "(final Message msg) {\n");
    write(out, "    return " + (fr.getType().equals("Object") ? "" : "(" + fr.getType() + ")") + " msg.getField(idx" + fr.getName().toUpperCase() + ");\n");
    write(out, "  }\n\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeSetup(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "  /** ������������� �������� ������� ���������.\n");
    write(out, "   * @param msg ���������.\n");

    if (tplSource.getDefaultDestination() == null) {
      write(out, "   * @param destination ����� ����������.\n");
    }

    if (tplSource.getDefaultOrigin() == null) {
      write(out, "   * @param origin ����� �����������.\n");
    }

    if (tplSource.getDefaultReplyTo() == null) {
      write(out, "   * @param replyTo ������������� ���������, �� ������� ��� �������� �������.\n");
    }
    write(out, "   * @return ������ �� ������������������ ���������\n");
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
  private void writeCheckMessage(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "  /** �������� ��������� �� ������������.\n");
    write(out, "   * @param msg ���������\n");
    write(out, "   */\n");
    write(out, "  private static void checkMessage(final Message msg) {\n");

    if (tplSource.getFields().isEmpty()) {
      write(out, "    msg.setCorrect(\n      true\n");
    } else {
      write(out, "    try {\n");
      Iterator it = tplSource.getFields().keySet().iterator();
      while (it.hasNext()) {
        String fieldName = (String) it.next();
        String fieldCheck = ((FieldRecord) tplSource.getFields().get(fieldName)).getCheck();

        if (fieldCheck.trim().length() == 0) {
          fieldCheck = "get" + fieldName + "(msg) != null";
        }
        write(out, "      assert " + fieldCheck + " : \"Message has invalid field '" + fieldName + "'\";\n");
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
    }
    write(out, "    );\n");
    write(out, "  }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeMessageConstants(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "public final class " + tplSource.getMessageName() + " {\n");
    write(out, "  /** ��������� ������������� ���������. */\n");
    write(out, "  public static final String NAME = \"" + tplSource.getActionName() + "\";\n");

    Iterator it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String key = (String) it.next();
      write(out, "  /** ������ ��� ���� " + key + ". */\n");
      write(out, "  public static final String idx" + key.toUpperCase(Locale.ENGLISH) + " = \"" + key.toLowerCase(Locale.ENGLISH) + "\";\n");
    }

    it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String key = (String) it.next();
      FieldRecord fr = (FieldRecord) tplSource.getFields().get(key);
      if (!"".equals(fr.getDefault())) {
        write(out, "  /** �������� ��-��������� ��� ���� " + key + ". */\n");
        write(out, "  public static final " + fr.getType() + " " + key.toUpperCase(Locale.ENGLISH) + "_DEFAULT = " + fr.getDefault() + ";\n");
      }
    }
    write(out, "\n");
    write(out, "  /** ������ �� �������� �������. */\n");
    write(out, "  private " + tplSource.getMessageName() + "() { /* Single-model. �� ����������� ��������� ������. */ }\n");
  }

  /**
   * @param out
   * @throws IOException
   */
  private void writeClassJavadoc(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "/** ");
    Iterator it = tplSource.getDescription().iterator();
    while (it.hasNext()) {
      final String element = (String) it.next();
      write(out, element);
      write(out, "\n * ");
    }
    it = tplSource.getAuthors().iterator();
    while (it.hasNext()) {
      final String element = (String) it.next();
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
  private void writeJavaHeader(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "package " + tplSource.getPackageName() + ";\n\n");
    write(out, "import org.valabs.odisp.common.Message;\n");
    write(out, "import org.doomdark.uuid.UUID;\n");

    if (tplSource.getImports().size() > 0) {
      final Iterator it = tplSource.getImports().iterator();
      while (it.hasNext()) {
        final String element = (String) it.next();
        write(out, "import " + element + ";\n");
      }
    }
    write(out, "\n");
  }

  private void writeHelper(final TplFile tplSource, final OutputStream out) throws IOException {
    write(out, "  public static final class " + tplSource.getMessageName() + "Helper {\n");
    write(out, "    private final Message msg;\n");
    write(out, "    " + tplSource.getMessageName() + "Helper(final Message _msg) {\n");
    write(out, "      msg = _msg;\n");
    write(out, "    }\n");
    final Iterator it = tplSource.getFields().keySet().iterator();
    while (it.hasNext()) {
      final String fieldName = (String) it.next();
      writeHelperField(tplSource, out, fieldName);
    }

    write(out, "    /** �������� ������ �� �������������� ���������. */\n");
    write(out, "    public Message message() {\n");
    write(out, "      return msg;\n");
    write(out, "    }\n");
    write(out, "  }\n");
  }

  /**
   * @param out
   * @param fieldName
   * @throws IOException
   */
  private void writeHelperField(final TplFile tplSource, final OutputStream out, final String fieldName) throws IOException {
    final FieldRecord fr = (FieldRecord) tplSource.getFields().get(fieldName);
    write(out, "    /** ���������� " + fr.getName() + ".\n");
    if (fr.getDesc().length() > 0) {
      write(out, "     * " + fr.getDesc() + "\n");
    }
    write(out, "     * @param newValue ����� �������� ��� ����.\n");
    write(out, "     * @return ������ �� ���������\n");
    write(out, "     */\n\n");
    write(out, "    public " + tplSource.getMessageName() + "Helper set" + fr.getName() + "(final " + fr.getType() + " newValue) {\n");
    write(out, "      " + tplSource.getMessageName() + ".set" + fr.getName() + "(msg, newValue);\n");
    write(out, "      checkMessage(msg);\n");
    write(out, "      return this;\n");
    write(out, "    }\n");
    write(out, "    /** �������� " + fr.getName() + ".\n");
    write(out, "     *" + fr.getDesc() + "\n");
    write(out, "     * @return �������� ����\n");
    write(out, "     */\n");
    write(out, "    public " + fr.getType() + " get" + fr.getName() + "() {\n");
    if (!"".equals(fr.getDefault())) {
      write(out, "      if (msg.getField(idx" + fr.getName().toUpperCase(Locale.ENGLISH) + ") == null) {\n");
      write(out, "        return " + fr.getName().toUpperCase(Locale.ENGLISH) + "_DEFAULT;\n");
      write(out, "      } else {\n");
      write(out, "        return " + (fr.getType().equals("Object") ? "" : "(" + fr.getType() + ")") + " msg.getField(idx" + fr.getName().toUpperCase(Locale.ENGLISH) + ");\n");
      write(out, "      }\n");
    } else {
      write(out, "      return " + (fr.getType().equals("Object") ? "" : "(" + fr.getType() + ")") + " msg.getField(idx" + fr.getName().toUpperCase(Locale.ENGLISH) + ");\n");
    }
    write(out, "    }\n\n");
  }

  private void write(final OutputStream out, final String line) throws IOException {
    out.write(line.getBytes());
  }
}
