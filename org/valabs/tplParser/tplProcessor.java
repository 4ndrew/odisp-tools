package org.valabs.tplParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * ���� �������� tpl �����
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (C) 2004 ��� "�����-��"
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
   * ����������� ����������� tpl �����
   * 
   * @param tplName
   *            ��� tpl �����
   * @param javaName
   *            ��� ���������������� java �����
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
   * ������ �������� tpl �����
   * 
   * ������ ���� ��������� � ������ ������ ������ ����������� ������ .tpl
   * �����
   * 
   * @return true - ��� ������ ������, false - ���� ����������
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
   * ������ � ��������� ������
   * 
   * @param s
   *            ������ ������� �����
   */
  private void write(String s) {
    javaWriter.println(s);
  }

  /**
   * ������ ��� �������� ������
   * 
   * @param s
   *            ������ ������� �����
   */
  private void writec(String s) {
    javaWriter.print(s);
  }

  /**
   * ��������� ����������� .java �����
   * 
   * @param javaWriter
   *            ��� .java �����
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
    write("  /** ��������� ������������� ���������. */");
    write("  public static final String NAME = \"" + actionName + "\";\n");

    for (int i = 0; i < fieldNames.size(); i++) {
      String key = (String) fieldNames.get(i);
      write("  /** ������ ��� ���� " + key + ". */");
      write("  private static String idx" + key.toUpperCase() + " = \""
          + key.toLowerCase() + "\";");
    }

    write("\n");
    write("  /** ������ �� �������� �������. */");
    write("  private " + messageName + "() { }\n");

    write("  /** �������� ��������� �� ������������.");
    write("   *");
    write("   * @param msg ���������");
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

    write("  /** ������������� �������� ������� ���������.");
    write("   *");
    write("   * @param msg ���������.");

    if (tagDEFDESTstrings == "") {
      write("   * @param destination ����� ����������.");
    }

    if (tagDEFORIGINstrings == "") {
      write("   * @param origin ����� �����������.");
    }

    write("   * @param replyTo ������������� ���������, �� ������� ��� �������� �������.");
    write("   * @return ������ �� ������������������ ���������");
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

      write("  /** ���������� " + fieldName + ".");
      write(" " + ((FieldRecord) fields.get(i)).getDesc());
      write("   *");
      write("   * @param msg ��������� ��� ������� ������������ ��������.");
      write("   * @param newValue ����� �������� ��� ����.");
      write("   * @return ������ �� ���������");
      write("   */");
      write("  public static Message set" + fieldName
          + "(final Message msg, final "
          + ((FieldRecord) fields.get(i)).getType() + " newValue) {");
      write("    msg.addField(idx" + fieldName.toUpperCase() + ", newValue);");
      write("    checkMessage(msg);");
      write("    return msg;");
      write("  }\n");
      write("  /** �������� " + fieldName + ".");
      write(" " + ((FieldRecord) fields.get(i)).getDesc());
      write("   *");
      write("   * @param msg ��������� ��� ������� ������������ ��������.");
      write("   * @return �������� ����");
      write("   */");
      write("  public static " + ((FieldRecord) fields.get(i)).getType()
          + " get" + fieldName + "(final Message msg) {");
      write("    return (" + ((FieldRecord) fields.get(i)).getType()
          + ") msg.getField(idx" + fieldName.toUpperCase() + ");");
      write("  }\n");
    }

    write("  /** �������� �� ��������� ���������� ����� ����.");
    write("   *");
    write("   * @param msg ���������.");
    write("   * @return true - ���� ��������, false - �����.");
    write("   */");
    write("  public static boolean equals(final Message msg) {");
    write("    return msg.getAction().equals(NAME);");
    write("  }\n");
    write("  /** ����������� ����� �� ������ ��������� � ������.");
    write("  *");
    write("  * @param dest ����������.");
    write("  * @param src ��������.");
    write("  */");
    write("  public static void copyFrom(final Message dest, final Message src) {");

    for (int i = 0; i < fieldNames.size(); i++) {
      String fieldName = (String) fieldNames.get(i);
      write("    set" + fieldName + "(dest, get" + fieldName + "(src));");
    }
    write("  }\n");

    write("  /** ������������� ����������� hash-���� ���������.");
    write("   * ������ ����� 0.");
    write("   * @return hash-��� ���������.");
    write("   */");
    write("  public int hashCode() {");
    write("    return 0;");
    write("  }\n");

    write("  /** �������� ������ ���������� ���� ����� ��������� �����.");
    write("   * @return ������ �� ���������");

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
   * ���������� ������ .tpl �����
   * 
   * @param tagLine
   *            ������ .tpl �����
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
      //����������� ����������
      //TODO may be it's good to skip empty lines
      if (tagLine != "") {
        tagVERBATIMstrings += tagLine + "\n";
      }
    }
  }

  /**
   * ����� ���������� ��������� ���� ���������
   * 
   * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
   * @author (C) 2004 ��� "�����-��"
   * @version $Id: tplProcessor.java,v 1.15 2004/10/26 09:25:07 dron Exp $
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
     * @param check
     *            ����� ������� �������� ���� ���������.
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
     * @param desc
     *            �������� ���� ���������.
     */
    public void setDesc(String desc) {
      if (this.desc == "") {
        this.desc += "  *" + desc;
      } else {
        this.desc += "\n  *" + desc;
      }
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
     * @param type
     *            ��� ���� ���������.
     */
    public void setType(String type) {
      this.type = type;
    }
  }

}